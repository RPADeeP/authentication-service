package deep.auth.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import com.ctc.wstx.util.StringUtil
import deep.auth.model.BaseUser
import deep.auth.model.User
import deep.auth.model.UserAuthDetails
import deep.auth.repository.UserAuthDetailsRepository
import deep.auth.repository.UserRepository
import org.apache.commons.codec.binary.StringUtils
import org.apache.commons.lang.time.DateUtils
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*


interface TokenService {
    fun generateToken(userAuthDetails: UserAuthDetails, user: User): String
    fun parseToken(token: String?, type: String): BaseUser?
    fun getTokenFromAuth(str: String): String
}

@Service
class TokenServiceImpl(
    private val userAuthDetailsRepository: UserAuthDetailsRepository,
    private val userRepository: UserRepository
) : TokenService{

    @Value("\${app.security.jwt.secret}")
    private lateinit var jwtSecret: String

    override fun generateToken(userAuthDetails: UserAuthDetails, user: User): String {
        return JWT.create()
            .withClaim("authId", userAuthDetails.id)
            .withClaim("userId", user.id)
            .withSubject(userAuthDetails.code)
            .withNotBefore(Date())
            .withExpiresAt(DateUtils.addDays(Date(),1))
            .sign(Algorithm.HMAC256(jwtSecret))
    }

    override fun parseToken(token: String?, type: String): BaseUser? {
        try {
            if(JWT.decode(token).expiresAt.before(Date()))
                return null

            if(StringUtils.equals(type, "UserAuth"))
            return userAuthDetailsRepository.findById(
                JWT.decode(token).claims["authId"]?.asString() ?: "",
            )

            if(StringUtils.equals(type, "User"))
            return userRepository.findById(
                JWT.decode(token).claims["userId"]?.asString() ?: "",
            )

            return null

        } catch (e: JWTDecodeException) {
            throw JWTDecodeException("Token is not valid", e.cause)
        }
    }

    override fun getTokenFromAuth(str: String): String {
        return str.split(" ".toRegex()).toTypedArray()[1].trim { it <= ' ' }
    }

}