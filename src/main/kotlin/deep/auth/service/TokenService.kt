package deep.auth.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import deep.auth.model.UserAuthDetails
import deep.auth.repository.UserAuthDetailsRepository
import org.apache.commons.lang.time.DateUtils
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*


interface TokenService {
    fun generateToken(user: UserAuthDetails): String
    fun parseToken(token: String?): UserAuthDetails?
    fun getTokenFromAuth(str: String): String
}

@Service
class TokenServiceImpl(
    val userAuthDetailsRepository: UserAuthDetailsRepository
) : TokenService{

    @Value("\${app.security.jwt.secret}")
    private lateinit var jwtSecret: String

    override fun generateToken(user: UserAuthDetails): String {
        return JWT.create()
            .withClaim("id", user.id.toString())
            .withSubject(user.code)
            .withNotBefore(Date())
            .withExpiresAt(DateUtils.addMinutes(Date(),30))
            .sign(Algorithm.HMAC256(jwtSecret))
    }

    override fun parseToken(token: String?): UserAuthDetails? {
        try {
            if(JWT.decode(token).expiresAt.before(Date()))
                return null
            return userAuthDetailsRepository.findByIdAndCode(
                ObjectId(JWT.decode(token).claims["id"]?.asString() ?: ""),
                JWT.decode(token).subject as String
            )
        } catch (e: JWTDecodeException) {
            throw JWTDecodeException("Token is not valid", e.cause)
        }
    }

    override fun getTokenFromAuth(str: String): String {
        return str.split(" ".toRegex()).toTypedArray()[1].trim { it <= ' ' }
    }

}