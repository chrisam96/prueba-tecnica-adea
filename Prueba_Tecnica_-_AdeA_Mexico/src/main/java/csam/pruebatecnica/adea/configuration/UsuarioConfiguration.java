package csam.pruebatecnica.adea.configuration;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
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
	public CorsFilter corsFilter() {
		
		CorsConfiguration config = new CorsConfiguration();
		
		String [] arr = {"*"};		
		
		config.setAllowedOrigins(Arrays.asList(arr) );
		
		config.setAllowedHeaders(Arrays.asList(arr) );
		
		config.setAllowedMethods(Arrays.asList(arr) );			
		
		/*Instacia que extiende de CorsConfigurationSpurce.
		 * Esta clase que permite registrar configuraciones CORS basadas en patrones de URL.*/
		UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
		
		corsSource.registerCorsConfiguration("/**", config);
		
		return new CorsFilter(corsSource);
	}
}
