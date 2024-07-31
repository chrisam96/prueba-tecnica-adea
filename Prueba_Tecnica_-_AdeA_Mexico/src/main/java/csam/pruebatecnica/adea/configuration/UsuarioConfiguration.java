package csam.pruebatecnica.adea.configuration;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


@Configuration
public class UsuarioConfiguration {

	@Bean
	public RestTemplate restTemplateBasico() {
		return new RestTemplate();
	}
	
	
	//Configuración de las politicas de seguridad CORS de la aplicación
	@Bean
	public CorsConfigurationSource  corsFilter() {
		
		CorsConfiguration config = new CorsConfiguration();
		
		config.setAllowedOrigins( Arrays.asList("*") );
		config.addAllowedOrigin("*");
		config.setAllowedMethods( Arrays.asList("*") );
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
