package csam.pruebatecnica.adea.configuration;

import java.time.Duration;
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
		
		ArrayList<String> list = new ArrayList<>();		
		list.add("*");
		
		config.setAllowedOrigins( Arrays.asList("*") );
		config.addAllowedOrigin("*");
		
		config.setAllowedHeaders( Arrays.asList("*") );
		config.setExposedHeaders( Arrays.asList("*") );
		
		config.setAllowedMethods( Arrays.asList("*") );
		
		config.setMaxAge(Duration.ZERO);
		config.setAllowCredentials(Boolean.TRUE);
		// Instacia que extiende de CorsConfigurationSpurce.
		// Esta clase que permite registrar configuraciones CORS basadas en patrones de URL.
		UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
		
		corsSource.registerCorsConfiguration("/**", config);
		
		return new CorsFilter(corsSource);
	} 
	
	 private static final String ALLOWED_HEADERS = "*";//"x-requested-with, authorization, Content-Type, Content-Length, Authorization, credential, X-XSRF-TOKEN";
	 private static final String ALLOWED_METHODS = "GET, PUT, POST, DELETE, OPTIONS, PATCH";
	 private static final String ALLOWED_ORIGIN = "*";
	 
	 /* [[[ Problema de CORS y Filtro de Spring Gateway ]]]
	  * Como si fuese por "defecto" las peticiones GET no se ven afectadas por
	  * las politicas CORS, sin embargo, otros verbos si.
	  * 
	  * El servidor Gateway bloquea las peticiones POST (y otrs verbos excepto 
	  * GET) procedentes de un Script (del cliente).
	  * 
	  * Y aunque los servicios tengan la anotacion "@CrossOrigin" (que permite
	  * el acceso desde cualquier origen), siguen siendo bloqueadas las 
	  * peticiones (que incluyen ademas objetos JSON) por Gateway. 
	  * 
	  * solucion::
	  * Se tiene que añadir un componente (@Bean) de filtro que se registre en
	  * el servidor Gateway para que no bloquee las peticiones.
	  * */
	/*
	 @Bean
	 public WebFilter corsFilter() {
		return (ServerWebExchange ctx, WebFilterChain chain) 
		-> 
		{
			/*NOTA: Todas las clases deben ser importadas del package "reactive".
			 * Puede que se trabaje con programación reactiva (Spring Boot 3) * /
			ServerHttpRequest request = ctx.getRequest();
			
			/*Compara si en la petición hay una petición del tipo CORS
			 * En caso de haber, se le agregarán las cabeceras pertinentes* /
			if (CorsUtils.isCorsRequest(request)) {
				ServerHttpResponse response = ctx.getResponse();
				HttpHeaders headers = response.getHeaders();
				headers.add("Access-Control-Allow-Origin", ALLOWED_ORIGIN);
				headers.add("Access-Control-Allow-Methods", ALLOWED_METHODS);
				headers.add("Access-Control-Allow-Headers", ALLOWED_HEADERS);
			}
			return chain.filter(ctx);
		};
	 }*/
}
