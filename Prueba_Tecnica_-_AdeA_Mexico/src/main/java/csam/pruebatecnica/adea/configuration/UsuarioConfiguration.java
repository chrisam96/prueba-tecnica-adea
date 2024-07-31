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
	
		
}
