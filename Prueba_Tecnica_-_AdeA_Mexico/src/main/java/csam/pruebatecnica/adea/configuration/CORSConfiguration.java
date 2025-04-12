package csam.pruebatecnica.adea.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class CORSConfiguration {

	@Bean
	public RestTemplate restTemplateBasico() {
		return new RestTemplate();
	}
	
	//Configuración de CORS con WebMvcConfigurer
	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			
			public void addCorsMappings(CorsRegistry registry) {
				//Habilita el manejo CORS para el patron de ruta URI especificada
				registry.addMapping("/**")
				//EstableCE los orígenes URL para los cuales se permiten 
				//solicitudes CORS desde un navegador.
					.allowedOrigins("*")
				//Establece los métodos HTTP permitidos
					.allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
				//Establece los headers de una solicitud (pre-flight) puede 
				//incluir como permitidos para su uso
					.allowedHeaders("*")
				//Se admite el acceso a la red privada
					//.allowPrivateNetwork(true)
				//Configurear durante cuánto tiempo en segundos los clientes 
				//pueden almacenar en caché la respuesta de una solicitud
					.maxAge(300);
			}
		};
	}


	//Configuración de CORS con CorsFilter
/*
	//Configuración de las politicas de seguridad CORS de la aplicación
	//con CorsConfigurationSource, CorsFilter y FilterRegistrationBean]
	 
	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	//public CorsConfigurationSource  corsConfigurationSource() {
	//public CorsFilter  corsFilter() {
	public FilterRegistrationBean<CorsFilter> corsFilterRegistration(){
		
		CorsConfiguration config = new CorsConfiguration();
		
		//config.setAllowedOriginPatterns( Arrays.asList("*") );
		config.setAllowedOrigins( Arrays.asList("*") );
		//config.addAllowedOrigin("*");
		config.setAllowedMethods( Arrays.asList(
				//RequestMethod.GET.name(),
				//RequestMethod.POST.name(),
				//RequestMethod.PUT.name(),
				//RequestMethod.DELETE.name(),
				//RequestMethod.HEAD.name() * /
				"*"
				) );
		config.setAllowedHeaders( Arrays.asList("*") );
		config.setExposedHeaders( Arrays.asList("*") );
		
		//config.setMaxAge(Duration.ZERO);

		// * NOTA:
		// * No puedes usar allowedOrigins("*") con allowCredentials(true). 
		// * Debes especificar el origen explícitament e
		// * * /
		//config.setAllowCredentials(Boolean.TRUE);
		
		// Instacia que extiende de CorsConfigurationSpurce.
		// Esta clase que permite registrar configuraciones CORS basadas en patrones de URL.
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		
		/// **
		//  * >> Bean de tipo CorsConfigurationSource <<
		//  * 
		//  * Proporciona la configuración (por ejemplo, mediante un UrlBasedCorsConfigurationSource)
		//  *  que luego es utilizada por un filtro CORS.
		//  *  
		//  *  Debe ser inyectado en un CorsFilter o usado con FilterRegistrationBean. Configurar 
		//  *  solo este bean sin vincularlo a un CorsFilter no activará CORS.
		//  *  
		//  *  NOTA:
		//  *  Si Spring Security no encuentra un bean de CorsFilter, puede crear uno internamente 
		//  *  a partir de este CorsConfigurationSource. Es importante nombrarlo correctamente (se 
		//  *  espera que se llame exactamente “corsConfigurationSource”) para que Spring Security 
		//  *  lo detecte.
		//  * 
		//  * >> Bean de tipo CorsFilter (Servlet) <<
		//  * 
		//  *  Define un bean del filtro CORS para aplicaciones Servlet tradicionales.
		//  *  
		//  *  Para su Funcionamiento se Requiere un CorsConfigurationSource configurado. Si no 
		//  *  se configura el CorsConfigurationSource asociado, el filtro no tendrá efecto.
		//  *  
		//  *  NOTA:
		//  *  Puede ser sobrescrito por otros filtros si se implementa Spring Security.
		//  *  
		//  * /
		
		source.registerCorsConfiguration("/**", config);
		
		
		// *
		// * AGREGAR EXPLICACIÓN
		// * * /
		CorsFilter corsFilter = new CorsFilter(source);
		
		FilterRegistrationBean<CorsFilter> bean = 
				new FilterRegistrationBean<CorsFilter>(corsFilter);
		
		// NOTA:
		// Una alternativa a la anotación de @Order(Ordered.HIGHEST_PRECEDENCE)
		// puede ser bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		///
		 
		//bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		//return new CorsFilter(source);
		//return corsSource;
		return bean;
	}  

 */
}
