package ge.vakho.boot.openidconnectspringbootexample.config;

import org.mitre.openid.connect.web.UserInfoInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new UserInfoInterceptor());
	}
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		
		// Home page
		registry.addViewController("/home").setViewName("home");
		registry.addViewController("/").setViewName("home");
		
		// Hello page		
		registry.addViewController("/hello").setViewName("hello");
	}
	
	@Bean
	public SpringTemplateEngine templateEngine(ITemplateResolver templateResolver) {
	    SpringTemplateEngine templateEngine = new SpringTemplateEngine();
	    templateEngine.setTemplateResolver(templateResolver);
	    templateEngine.addDialect(new SpringSecurityDialect());
	    return templateEngine;
	}

}