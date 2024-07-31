package csam.pruebatecnica.adea.configuration;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
public class UsuarioConfiguration {

	@Bean
	public RestTemplate restTemplateBasico() {
		return new RestTemplate();
	}
	
	
	//Configuración de las politicas de seguridad CORS de la aplicación
	
	@Bean
	public CorsConfigurationSource  corsConfigurationSource() {
		
		CorsConfiguration config = new CorsConfiguration();
		
		config.setAllowedOrigins( Arrays.asList("*") );
		//config.addAllowedOrigin("*");
		config.setAllowedMethods( Arrays.asList(
				RequestMethod.GET.name(),
				RequestMethod.POST.name(),
				RequestMethod.PUT.name(),
				RequestMethod.DELETE.name(),
				RequestMethod.HEAD.name()
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
