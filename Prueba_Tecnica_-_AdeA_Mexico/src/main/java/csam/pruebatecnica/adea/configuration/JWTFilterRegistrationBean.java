package csam.pruebatecnica.adea.configuration;


import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import csam.pruebatecnica.adea.seguridad.jwt.JWTFilter;
import jakarta.servlet.DispatcherType;

@Configuration
public class JWTFilterRegistrationBean {

	@Bean
	public FilterRegistrationBean<JWTFilter> jwtFilterRegistration(JWTFilter jwtFilter){
		FilterRegistrationBean<JWTFilter> registry = new FilterRegistrationBean<JWTFilter>();
		
		registry.setFilter(jwtFilter);
		//URLs a las que se le aplica este filtro
		registry.addUrlPatterns("/*");
		//Establece el núm. orden (de ejecución) de este bean
		registry.setOrder(1);
		
		/* NOTA (Sobre DispatcherType)
		 * 1. shouldNotFilter() sólo se invoca si el tipo de filtro (DisptacherType)
		 *  está registrado para ese dispatch (registrado en 
		 *  FilterRegistrationBean<OncePerRequestFilter>)
		 * 2. Para registrar un tipo de filtro, debemos registrarlo (configurarlo) en el
		 * 	FilterRegistrationBean<OncePerRequestFilter>() mediante el método
		 * 	setDispatcherTypes().
		 * 3. Los DispatcherTypes que estan configurados por defecto son 
		 * 	DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE 
		 * 	(y también DispatcherType.ASYNC si fue habilitado).
		 * */
		
		//Indicamos que el filtro se invoque en peticiones de tipo REQUEST, ERROR y FORWARD
		// (y ya con esto configurado después podemos excluirlas en 
		// OncePerRequest.shouldNotFilter(HttpServletRequest)
		registry.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ERROR, DispatcherType.FORWARD);
		
		return registry;
	}
}
