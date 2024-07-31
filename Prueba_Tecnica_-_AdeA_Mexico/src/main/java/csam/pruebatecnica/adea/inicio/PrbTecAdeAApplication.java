package csam.pruebatecnica.adea.inicio;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@ComponentScan(basePackages = {
		"csam.pruebatecnica.adea.configuration",
		"csam.pruebatecnica.adea.controller", 
		"csam.pruebatecnica.adea.service", 
		"csam.pruebatecnica.adea.dao", 
		"csam.pruebatecnica.adea.model"} )
@EntityScan (basePackages = {"csam.pruebatecnica.adea.model"})
@EnableJpaRepositories (basePackages = {"csam.pruebatecnica.adea.dao"} )
@SpringBootApplication
public class PrbTecAdeAApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrbTecAdeAApplication.class, args);
	}
	
	
	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public CorsConfigurationSource  corsConfigurationSource() {
		
		CorsConfiguration config = new CorsConfiguration();
		
		config.setAllowedOriginPatterns( Arrays.asList("*") );
		//config.addAllowedOrigin("*");
		config.setAllowedMethods( Arrays.asList(
				/*RequestMethod.GET.name(),
				RequestMethod.POST.name(),
				RequestMethod.PUT.name(),
				RequestMethod.DELETE.name(),
				RequestMethod.HEAD.name()*/
				"*"
				) );
		config.setAllowedHeaders( Arrays.asList("*") );
		config.setExposedHeaders( Arrays.asList("*") );
		
		//config.setMaxAge(Duration.ZERO);
		//config.setAllowCredentials(Boolean.TRUE);
		
		// Instacia que extiende de CorsConfigurationSpurce.
		// Esta clase que permite registrar configuraciones CORS basadas en patrones de URL.
		UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
		
		corsSource.registerCorsConfiguration("/**", config);
		
		//return new CorsFilter(corsSource);
		return corsSource;
	}  


}
