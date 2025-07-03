package csam.pruebatecnica.adea.error;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
public class ControllerErrores {

	//@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	//public ModelAndView error405(HttpServletResponse response, HttpRequestMethodNotSupportedException ex ) 
	//public void error405(HttpServletResponse response, HttpRequestMethodNotSupportedException ex ) 
	public ResponseEntity<?> error405(HttpServletResponse response, HttpRequestMethodNotSupportedException ex ) 
	throws IOException{
		System.out.println("=== ControllerErrores.error405(): HttpRequestMethodNotSupportedException ===");
		
		//ResponseEntity<?> o = new ResponseEntity<?>();
		HttpHeaders headers = new HttpHeaders();
		headers.add("mensaje-error-header", ex.getMessage());
		headers.add("status-code", HttpStatus.METHOD_NOT_ALLOWED.toString());
		
		
		URI uri = URI.create("/errores/error.html");
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("Status Code", HttpStatus.METHOD_NOT_ALLOWED.value());
		body.put("mensaje-error", ex.getMessage());
		return ResponseEntity
				.status(HttpStatus.METHOD_NOT_ALLOWED)
				.headers(headers)
				.location(uri)
				.body(body);
		
		/* 
		HttpSerlvetResponse + ModelAndView
		  
		//Sin ModelAndView no sirve y sus datos no pasan al cliente
		// en forma de Headers
		response.setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
		response.addHeader("mensaje-error-header", ex.getMessage());
		//-->No permite ni los Headers ni los StatusCode
		//response.sendRedirect("/errores/error.html"); 
		
		 
		//Al agregar una ruta, en el cliente se muestra como prop. Location
		ModelAndView mav = new ModelAndView("redirect:/errores/error.html");
		mav.setStatus(HttpStatus.METHOD_NOT_ALLOWED);
		//mav.addObject("mensaje-error", ex.getMessage());
		return mav;
		*/
	}
}
