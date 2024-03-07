package csam.pruebatecnica.adea.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@CrossOrigin(origins = "*")
@Controller
public class UsuarioWebController {
	
	//@RequestMapping(value = "/prueba2") // @GetMapping	(value) == @RequestMapping(path="/foo").
	@RequestMapping(path = {"/login", "/"}) // @GetMapping	(value) == @RequestMapping(path="/foo").
	public String login() {
		/*
		 * archivo: index.html
		 * ruta: resources/templates
		 * metodo: UsuarioWebController.prb()
		 */
		return "login";
	}
	
	//@RequestMapping(path = "", method = RequestMethod.GET)
	//@RequestMapping(path = "", method = RequestMethod.GET)
	private String pub(ModelAndView maw) {
		
		return "";
	}
	
	/*-----------------------------------------------------------------------------------*/
	// METODOS DE PRUEBA DE SERVICO EN FUNCION	
	@GetMapping(value = "/prueba") // @GetMapping	(value) == @RequestMapping(path="/foo").
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
