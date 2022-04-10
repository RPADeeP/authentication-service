package deep.auth.component

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

// TODO: Proper security configurations
@Configuration
@EnableWebSecurity
class SecurityConfigAdapter : WebSecurityConfigurerAdapter(){

    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
        http.authorizeRequests().antMatchers("/**").permitAll()
    }
}
