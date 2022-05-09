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
    val userAuthDetailsRepository: UserAuthDetailsRepository,
    val userRepository: UserRepository
) : TokenService{

    @Value("\${app.security.jwt.secret}")
    private lateinit var jwtSecret: String

    override fun generateToken(userAuthDetails: UserAuthDetails, user: User): String {
        return JWT.create()
            .withClaim("authId", userAuthDetails.id.toString())
            .withClaim("userId", user.id.toString())
            .withSubject(userAuthDetails.code)
            .withNotBefore(Date())
            .withExpiresAt(DateUtils.addMinutes(Date(),30))
            .sign(Algorithm.HMAC256(jwtSecret))
    }

    override fun parseToken(token: String?, type: String): BaseUser? {
        try {
            if(JWT.decode(token).expiresAt.before(Date()))
                return null

            if(StringUtils.equals(type, "UserAuth"))
            return userAuthDetailsRepository.findByIdAndCode(
                ObjectId(JWT.decode(token).claims["authId"]?.asString() ?: ""),
                JWT.decode(token).subject as String
            )

            if(StringUtils.equals(type, "User"))
            return userRepository.findByIdAndCode(
                ObjectId(JWT.decode(token).claims["userId"]?.asString() ?: ""),
                JWT.decode(token).subject as String
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