package csam.pruebatecnica.adea.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import csam.pruebatecnica.adea.model.Usuario;
import csam.pruebatecnica.adea.model.UsuarioCredenciales;
import csam.pruebatecnica.adea.service.UsuarioService;

/*Notacion @CrossOrigin
 * 
 * Indica que orígenes (de URL) se quieren admitir en el controller. 
 * Al usar * se permiten peticiones desde cualquier URL
 */

/*@CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "*", 
methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE,
		RequestMethod.HEAD})*/
//@CrossOrigin(origins = "*") 
@Controller
public class UsuarioWebController {
	
	//Bean del UsuarioService
	@Autowired
	UsuarioService usuarioService;

	/*-----------------------------------------------------------------------------------*/
	// VISTAS DE PAGINAS WEB

	// Para "@GetMapping" es igual a:: (value) == @RequestMapping(path="/foo", method = RequestMethod.GET)	
	@RequestMapping(path = {"/login", "/"}, method = RequestMethod.GET) 
	public String login() {
		/*
		 * archivo: login.html
		 * ruta: resources/templates
		 * metodo: UsuarioWebController.getUsuarioByCredenciales(UsuarioCredenciales)
		 */
		return "login";
	}
	
	@GetMapping(value = "/home") // @GetMapping	(value) == @RequestMapping(path="/foo").
	public String home() {
		/*
		 * archivo: home.html
		 * ruta: resources/templates
		 * metodo: UsuarioWebController.home()
		 */
		return "home";
	}
	
	@GetMapping(value = "/sitio/home") // @GetMapping	(value) == @RequestMapping(path="/foo").
	public String sitio_home() {
		/*
		 * archivo: home.html
		 * ruta: resources/templates/sitio
		 * metodo: UsuarioWebController.sitio_home()
		 */
		return "sitio/home";
	}
	
	// Para "@GetMapping" es igual a:: (value) == @RequestMapping(path="/foo", method = RequestMethod.GET)	
	@RequestMapping(path = {"/sitio/gestion-usuarios"}) 
	public String gestion_usuarios() {
		/*
		 * archivo: gestion-usuarios.html
		 * ruta: resources/templates/sitio
		 * metodo: UsuarioWebController.gestion_usuarios()
		 */
		return "sitio/gestion-usuarios";
	}
	
	// Para "@GetMapping" es igual a:: (value) == @RequestMapping(path="/foo", method = RequestMethod.GET)	
	@RequestMapping(path = {"/sitio/tablero", "/tablero1"}) 
	public String tablero_usuarios() {
		/*
		 * archivo: login.html
		 * ruta: resources/templates/sitio
		 * metodo: UsuarioWebController.tablero()
		 */
		return "sitio/tablero";
	}
	
	/* PRUEBA - EXPERIMENTO
	 * Probando la "REIDRECCION" con "redirect:" desde el server
	 * */
	@GetMapping({"/sitio/tabla", "/tablero2"})
	public String redirectToTablero() {
		//return "redirect:/sitio/tablero";			   
		return "redirect:/tablero1";			   
	}
	
	/* PRUEBA - EXPERIMENTO
	 * Probando la "Cargar y devolver contenido HTML desde archivos" 
	 * a tráves de la CARGA en "ClassPathResource" 
	 * a tráves de la DEVOLUCION a tráves de String(byte[] ( FileCopyUtils ) )
	 * desde el server
	 * 
	 * RESULTADO
	 * No sirve con "src/main/resources/templates/sitio/tablero.html" porque:
	 * java.io.FileNotFoundException: class path resource
	 *  [src/main/resources/templates/sitio/tablero.html] cannot be opened because it does not exist
	 *  
	 *  Ni con la ruta relativa "templates/sitio/tablero.html"
	 *  [THYMELEAF][http-nio-9010-exec-8] Exception processing template
	 *  template might not exist or might not be accessible by any of the configured Template Resolvers
	 *  
	 *  org.thymeleaf.exceptions.TemplateInputException: Error resolving template [<!DOCTYPE html>
	 *  template might not exist or might not be accessible by any of the configured Template Resolvers
	 *  
	 *  Servlet.service() for servlet [dispatcherServlet] in context with path [] 
	 *  threw exception 
	 *  [Request processing failed: org.thymeleaf.exceptions.TemplateInputException: 
	 *  	Error resolving template [<!DOCTYPE html>
	 * */
	@Deprecated
	@GetMapping({"/tablero3", "/sitio/tablero3"})
	public String loadHomePage() {
	    Resource resource = new ClassPathResource("templates/sitio/tablero.html");
	    try {
	        InputStream inputStream = resource.getInputStream();
	        byte[] byteData = FileCopyUtils.copyToByteArray(inputStream);
	        String content = new String(byteData, StandardCharsets.UTF_8);
	        return content;
	    } catch (IOException e) {
	    	e.printStackTrace();
	    	
	    	//Si sucede un error, retorna hacia "Home"
	    	return sitio_home();
	    }	    
	}
	
	// VISTAS DE PAGINAS WEB
	/*-----------------------------------------------------------------------------------*/
	// METODOS DE LOGICA DEL CONTROLLER PARA LAS VISTAS


	// METODOS DE LOGICA DEL CONTROLLER PARA LAS VISTAS
	/*-----------------------------------------------------------------------------------*/
	// METODOS DE PRUEBA DE SERVICO EN FUNCION	
	@GetMapping(value = "/prueba1") // @GetMapping	(value) == @RequestMapping(path="/foo").
	public String prb() {
		/*
		 * archivo: login2.html
		 * ruta: resources/static
		 * metodo: UsuarioWebController.prb()
		 */
		return "__login2";
	}
	
	//@RequestMapping(value = "/prueba2") // @GetMapping	(value) == @RequestMapping(path="/foo").
	@RequestMapping(value = "/prueba2") // @GetMapping	(value) == @RequestMapping(path="/foo").
	public String prb2() {
		/*
		 * archivo: index.html
		 * ruta: resources/templates
		 * metodo: UsuarioWebController.prb()
		 */
		return "__index_prb";
	}
	
	@RequestMapping(value = "/prueba3") // @GetMapping	(value) == @RequestMapping(path="/foo").
	public String prb3() {
		/*
		 * archivo: home.html
		 * ruta: resources/templates
		 * metodo: UsuarioWebController.prb()
		 */
		return "__home";
	}
	// METODOS DE PRUEBA DE SERVICO EN FUNCION
	/*-----------------------------------------------------------------------------------*/

}
