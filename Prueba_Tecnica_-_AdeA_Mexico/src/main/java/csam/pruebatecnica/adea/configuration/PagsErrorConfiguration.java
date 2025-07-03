package csam.pruebatecnica.adea.configuration;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@Configuration
public class PagsErrorConfiguration {

	private final static Logger log = LoggerFactory.getLogger(PagsErrorConfiguration.class);

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE + 2)
	public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> webServerFactoryCustomizer() {
		return factory -> {
			log.info("=== Registrando páginas de error personalizadas ===");
			
			ErrorPage error404 = new ErrorPage(HttpStatus.NOT_FOUND, "/errores/paginaNoEncontrada.html");
			ErrorPage error401 = new ErrorPage(HttpStatus.UNAUTHORIZED, "/errores/sinAutorizacion.html");
			ErrorPage error403 = new ErrorPage(HttpStatus.FORBIDDEN, "/errores/sinPermisos.html");
			ErrorPage error500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/errores/error.html");
			
			factory.addErrorPages(error404, error401, error403, error500);
		};
	}
	
	//bean para que Spring no oculte el 404 como un "no matching handler"
	@Bean
	//@Order(Ordered.HIGHEST_PRECEDENCE + 1)
	public ErrorAttributes errorAttributes() {
		return new DefaultErrorAttributes(){
			
			@Override
			public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options){
				return super.getErrorAttributes(webRequest, options.including(ErrorAttributeOptions.Include.MESSAGE));
			}
		};
	}
	
	@Bean
    public ErrorViewResolver errorViewResolver() {
        return (request, status, model) -> {
            // Resuelve las vistas de error directamente desde classpath
            if (status == HttpStatus.NOT_FOUND) {
                return new ModelAndView("/errores/paginaNoEncontrada.html", model);
            } else if (status == HttpStatus.UNAUTHORIZED) {
                return new ModelAndView("/errores/sinAutorizacion.html", model);
            } else if (status == HttpStatus.FORBIDDEN) {
                return new ModelAndView("/errores/sinPermisos.html", model);
            } else if (status == HttpStatus.INTERNAL_SERVER_ERROR) {
                return new ModelAndView("/errores/error.html", model);
            }
            return null;
        };
    }
}