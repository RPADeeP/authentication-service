package deep.auth.component.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DepartmentConfig {
    @Value("\${app.department.url}")
    private lateinit var departmentServiceUrl: String

    @Bean
    fun departmentServiceUrl(): String{
        return departmentServiceUrl
    }
}