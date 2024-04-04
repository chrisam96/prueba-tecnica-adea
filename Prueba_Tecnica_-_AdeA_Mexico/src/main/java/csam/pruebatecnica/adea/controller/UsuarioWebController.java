package csam.pruebatecnica.adea.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import csam.pruebatecnica.adea.model.Usuario;
import csam.pruebatecnica.adea.model.UsuarioCredenciales;
import csam.pruebatecnica.adea.service.UsuarioService;

@CrossOrigin(origins = "*")
@Controller
public class UsuarioWebController {
	
	//Bean del UsuarioService
	@Autowired
	UsuarioService usuarioService;

	/*-----------------------------------------------------------------------------------*/
	// VISTAS DE PAGINAS WEB

	// Para "@GetMapping" es igual a:: (value) == @RequestMapping(path="/foo", method = RequestMethod.GET)	
	@RequestMapping(path = {"/login", "/"}) 
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
		return "/sitio/home";
	}
	
	// Para "@GetMapping" es igual a:: (value) == @RequestMapping(path="/foo", method = RequestMethod.GET)	
	@RequestMapping(path = {"/sitio/gestion-usuarios"}) 
	public String gestion_usuarios() {
		/*
		 * archivo: gestion-usuarios.html
		 * ruta: resources/templates/sitio
		 * metodo: UsuarioWebController.gestion_usuarios()
		 */
		return "gestion-usuarios";
	}
	
	// Para "@GetMapping" es igual a:: (value) == @RequestMapping(path="/foo", method = RequestMethod.GET)	
	@RequestMapping(path = {"/sitio/tablero"}) 
	public String tablero_usuarios() {
		/*
		 * archivo: login.html
		 * ruta: resources/templates/sitio
		 * metodo: UsuarioWebController.tablero()
		 */
		return "tablero";
	}
	
	// VISTAS DE PAGINAS WEB
	/*-----------------------------------------------------------------------------------*/
	// METODOS DE LOGICA DEL CONTROLLER PARA LAS VISTAS
	@PostMapping(value = { "/login", "/"},
			consumes = { MediaType.APPLICATION_JSON_VALUE},
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE  })
		public ResponseEntity<Usuario> getUsuarioByCredenciales(@RequestBody UsuarioCredenciales uc) {
					System.out.println("ENTRO: " + uc.toString());
					
					//Instancia de los HttpHeaders
					HttpHeaders headers = new HttpHeaders();
					
					/*[EDITAR]
					 * Creacion de una lista de MediaType (que son los formatos de entrega de la info),
					 * siendo estos guardados en la propiedad "Accept" (HttpHeaders.setAccept())
					 * que es en donde se indica que formatos son soportados en esta petición
					 * tanto como para ser aceptados (REQUEST) 
					 * como para ser devueltos (RESPONSE)
					 */
					List<MediaType> listaMT = new ArrayList<MediaType>();
					listaMT.add(MediaType.APPLICATION_JSON);
					listaMT.add(MediaType.TEXT_PLAIN);
					listaMT.add(MediaType.APPLICATION_XML);
					headers.setAccept(listaMT);

					/*ResponseEntity<Usuario> resp = new ResponseEntity<Usuario>
					 * 					(Usuario body, MultiValueMap<String, String> headers, HttpStatusCode statusCode);
					 * */
					
					Usuario user = usuarioService.getUsuarioByCredenciales(uc);
					
					if (user != null) {
						//CODIGO PARA REDIRECCIONAR A LA VISTA "/Home"			
						headers.add("encontrado","si");
						headers.add("redireccion","/home");
						headers.add("mensaje","prueba1");
						headers.add("mensaje","prueba2");
						
			            //return new ResponseEntity<>(user, HttpStatus.OK);
			            return new ResponseEntity<>(user, headers, HttpStatus.OK);
			        } else {
			            // Si el usuario no se encuentra, puedes devolver un error
			        	
						headers.add("encontrado","no");
						headers.add("redireccion","/login");
						headers.add("mensaje","Usuario no registrado");
						headers.add("mensaje","Usuario desconocido");
			            
						//return new ResponseEntity<>(HttpStatus.OK); 						//200 ACEPTADO
			            //return new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);		//404
			            //return new ResponseEntity<>(headers, HttpStatus.IM_USED);			//226
			            //return new ResponseEntity<>(headers, HttpStatus.ACCEPTED);			//202
						//return new ResponseEntity<>(headers, HttpStatus.NOT_MODIFIED);	//304 ACEPTADO
						return new ResponseEntity<>(headers, HttpStatusCode.valueOf(201));//201
			        }
		}

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
