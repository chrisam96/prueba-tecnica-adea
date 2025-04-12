package csam.pruebatecnica.adea.configuration;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

@Configuration
public class PagsErrorConfiguration {

	private final static Logger log = LoggerFactory.getLogger(PagsErrorConfiguration.class);

	@Bean
	public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer() {
		return factory -> {

			ErrorPage error404 = new ErrorPage(HttpStatus.NOT_FOUND, "/errores/paginaNoEncontrada.html");
			ErrorPage error401 = new ErrorPage(HttpStatus.UNAUTHORIZED, "/errores/sinAutorizacion.html");
			ErrorPage error403 = new ErrorPage(HttpStatus.FORBIDDEN, "/errores/sinPermisos.html");
			ErrorPage error = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/errores/error.html");
			
			factory.addErrorPages(error404, error401, error403, error);
		};
	}
	
	//bean para que Spring no oculte el 404 como un "no matching handler"
	@Bean
	public ErrorAttributes errorAttributes() {
		return new DefaultErrorAttributes(){
			
			@Override
			public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options){
				return super.getErrorAttributes(webRequest, options.including(ErrorAttributeOptions.Include.MESSAGE));
			}
		};
	}
}