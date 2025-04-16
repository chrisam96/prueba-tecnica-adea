package csam.pruebatecnica.adea.controller;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import csam.pruebatecnica.adea.model.Usuario;
import csam.pruebatecnica.adea.model.UsuarioCredenciales;
import csam.pruebatecnica.adea.service.UsuarioService;
import csam.pruebatecnica.adea.utils.UsuarioConstantes;;

/*Notacion @CrossOrigin
 * 
 * Indica que orígenes (de URL) se quieren admitir en el controller. 
 * Al usar * se permiten peticiones desde cualquier URL
 */

/*@CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "*", 
methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE,
		RequestMethod.HEAD})*/
//@CrossOrigin(origins = "*") 
@RestController
public class UsuarioController {

	//PROPIEDADES
	
	//Bean del UsuarioService
	@Autowired
	UsuarioService usuarioService;
	
	//server.port
	@Value("${server.port}")
	public int serverPort;
	
	@GetMapping(value = "correo/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Usuario getUsuarioByEmail(@PathVariable("email") String email) {
		return usuarioService.getUsuarioByEmail(email);
	}
	
	@GetMapping(value = "usuarios", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Usuario> getAllUsuario() {
		return usuarioService.getAllUsuario();
	}
	
	/*-----------------------------------------------------------------------------------*/
	// METODOS DE PRUEBA DE SERVICO EN FUNCION
	@GetMapping(value = "prueba/{s}", produces = { MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public String prueba(@PathVariable("s") String s) {
		return "prueba" + s ;
	}
	
	//ALT + SHIFT + X, B
	
	//PRUEBA DE QUE EL SERVICIO SIRVE CON DIFERENTES URL O RECURSOS
	
	@GetMapping(value = "/prb1") // @GetMapping	(value) == @RequestMapping(path="/foo").
	public String prb() {
		//return "login2";
		return "Prueba - /prb";
	}
	
	@RequestMapping(value = "/prb2") // @GetMapping	(value) == @RequestMapping(path="/foo").
	public String prb2() {
		//return "index";
		return "Prueba - /prb2";
	}
	
	@RequestMapping(value = "/prb3") // @GetMapping	(value) == @RequestMapping(path="/foo").
	public String prb3() {
		//return "home";
		return "Prueba - /prb3";
	}
	
	// PRUEBAS PARA SABER QUE FRONT Y BACK SE COMUNICABAN CORRECTAMENTE
	
	@PostMapping(value = { "/login1", "/get-{login}-{pass}" },
			consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE},
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE  })
		public ResponseEntity<Usuario> getUsuarioByCredenciales(@RequestBody UsuarioCredenciales uc) {
		//public Usuario getUsuarioByCredenciales(@RequestBody UsuarioCredenciales uc) { // USADO SOLO PARA TESTEO DE UN EXPERIMENTO
				//try {
					System.out.println("Entro a /login1: " + uc.toString());
					//return usuarioService.getUsuarioByCredenciales(uc);
					Usuario user = usuarioService.getUsuarioByCredenciales(uc);
					/*
					ResponseEntity<Usuario> resp = new ResponseEntity<Usuario>(user, HttpStatus.OK);				
					return resp;
					*/
					ResponseEntity<Usuario> resp = new ResponseEntity<Usuario>(user, HttpStatus.OK);
					ResponseEntity<Usuario> resp2 = new ResponseEntity<Usuario>(null, null, HttpStatus.OK);
					//resp2.of
					
					if (user != null) {
			            return new ResponseEntity<>(user, HttpStatus.OK);
			            //return user;// USADO SOLO PARA TESTEO DE UN EXPERIMENTO
			        } else {
			            // Si el usuario no se encuentra, puedes devolver un error
			            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			            //return null; // USADO SOLO PARA TESTEO DE UN EXPERIMENTO
			        }
					
				/*} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					// Crear un objeto JSON con un mensaje de error
			        String errorJson = "{\"error\":\"" + e.getMessage() + "\"}";
			        HttpHeaders headers = new HttpHeaders();
			        return new ResponseEntity<Usurio>(errorJson, HttpStatus.INTERNAL_SERVER_ERROR);
				}*/
		}
		
		@GetMapping(value = "/login2/{login}/{pass}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE })
		public Usuario findUsuarioByLoginAndPass(@PathVariable("login") String login, @PathVariable("pass") String pass) {
			Usuario encontrado = usuarioService.findUsuarioByLoginAndPass(login, pass);
			
			//Si es diferente de NULL es porque lo encontro
			if (encontrado != null) {
				return encontrado;
			}
			return new Usuario();
		}	
		
		@GetMapping(value = { "/login3/{login}/{pass}" }
		, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE  })
		public ResponseEntity<Usuario> getUsuarioByCredenciales2(@PathVariable("login") String login, @PathVariable("pass") String pass) {
			//try {	
				ResponseEntity<Usuario> re;
				HttpHeaders headers = new HttpHeaders();
				
				
				System.out.println("VALUE(STRING): " + MediaType.APPLICATION_JSON_VALUE 
						+ " VALUE(ENUM):" + MediaType.APPLICATION_JSON );
				
				headers.setContentType(MediaType.APPLICATION_JSON);
				
				/*Creacion de una lista de MediaType (que son los formatos de entrega de la info),
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

				//Usuario encontrado = usuarioService.getUsuarioByCredenciales(login, pass);
				UsuarioCredenciales uc =  new UsuarioCredenciales(login, pass);
				Usuario encontrado = usuarioService.getUsuarioByCredenciales(uc);
				
				//Si es diferente de NULL es porque lo encontro
				if (encontrado != null) {
					re = new ResponseEntity<Usuario>(encontrado, headers, HttpStatus.OK);
					return re;
					////return encontrado;
				}
				
				//re = new ResponseEntity<Usuario>(null, null, HttpStatus.CONFLICT);
				re = new ResponseEntity<Usuario>(null, headers, HttpStatus.CONFLICT);
				return re;
				////return new Usuario();
			/*
			 }catch(Throwable t) {
				t.printStackTrace();
				return new Usuario();
			}
			*/
		}

	
	/*-----------------------------------------------------------------------------------*/
	@PostMapping(value = { "/getUser"},
			consumes = { MediaType.APPLICATION_JSON_VALUE},
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE  })
	@ResponseBody
	public ResponseEntity<Usuario> getUsuarioByCredencialesTesting(@RequestBody UsuarioCredenciales uc) {
				System.out.println("ENTRO: Login:" + uc.getLogin() + " - Pass:" + uc.getPass());
				
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
				// Sirve para testear de cuando se activa el evento "error()" o "fail()" de Ajax de jQuery
				if (uc.getLogin().equalsIgnoreCase("admin") || uc.getPass().equalsIgnoreCase("admin")) {
					Usuario admin =  new Usuario();						
					return ResponseEntity.ok(admin);
				}
				if (uc.getLogin().equalsIgnoreCase("error") || uc.getPass().equalsIgnoreCase("error")) {
					// Si el usuario no se encuentra, puedes devolver un error
		        	
					headers.add("encontrado","no");
					//headers.add("redireccion","/login");
					headers.add("mensaje","Se ha encontrado a una excepción");
					headers.add("mensaje","BAD_REQUEST 400");
		            
					//return new ResponseEntity<>(HttpStatus.OK); 						//200 ACEPTADO - CON CUERPO
					//return new ResponseEntity<>(headers, HttpStatusCode.valueOf(201));//201
		            //return new ResponseEntity<>(headers, HttpStatus.ACCEPTED);		//202
					//return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT;);		//204 ACEPTADO - SIN CUERPO
		            //return new ResponseEntity<>(headers, HttpStatus.IM_USED);			//226
					//return new ResponseEntity<>(headers, HttpStatus.NOT_MODIFIED);	//304 ACEPTADO - SIN CUERPO
					return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);		//400
					//return new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);		//404
				}
				
				System.out.println("Consultando la base de datos...");
				Usuario user = usuarioService.getUsuarioByCredenciales(uc);
				
				if (user != null) {
					//CODIGO PARA REDIRECCIONAR A LA VISTA "/Home"			
					headers.add("encontrado","si");
					headers.add("redireccion","/home");
					headers.add("mensaje","USUARIO ENCONTRADO");
					headers.add("mensaje","TEST-HEADER");
					
		            //return new ResponseEntity<>(user, HttpStatus.OK);
		            return new ResponseEntity<>(user, headers, HttpStatus.OK);
		        } else {
		            // Si el usuario no se encuentra, puedes devolver un error
		        	
					headers.add("encontrado","no");
					//headers.add("redireccion","/login");
					headers.add("mensaje","USUARIO NO REGISTRADO");
					headers.add("mensaje","NOT_MODIFIED 304");
		            
					//return new ResponseEntity<>(HttpStatus.OK); 						//200 ACEPTADO
					//return new ResponseEntity<>(headers, HttpStatusCode.valueOf(201));//201
		            //return new ResponseEntity<>(headers, HttpStatus.ACCEPTED);		//202
					//return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT;);		//204 ACEPTADO - SIN CUERPO
		            //return new ResponseEntity<>(headers, HttpStatus.IM_USED);			//226
					return new ResponseEntity<>(headers, HttpStatus.NOT_MODIFIED);		//304 ACEPTADO - SIN CUERPO
					//return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);		//400
					//return new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);		//404	

		        }
	}
	/*-----------------------------------------------------------------------------------*/
	// METODOS DE APLICATIVO

	
	@GetMapping(value = "usuario/{login}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Usuario> getUsuarioByLogin(@PathVariable("login") String login) {
	//public Usuario getUsuarioByLogin(@PathVariable("login") String login) {
		
		Usuario recuperado = usuarioService.getUsuarioByLogin(login);
		if (recuperado == null) {
			return new ResponseEntity<Usuario>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Usuario>(recuperado, HttpStatus.OK);
	}
	
	@PostMapping(value = "/registrar", consumes = { MediaType.APPLICATION_JSON_VALUE }, 
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE,
					MediaType.TEXT_XML_VALUE, MediaType.TEXT_HTML_VALUE, MediaType.TEXT_PLAIN_VALUE })// USADO SOLO PARA TESTEO DE UN EXPERIMENTO	
	//public ResponseEntity<HashMap<String, ?>> saveUsuario(@RequestBody Usuario u) {
	public ResponseEntity<HashMap<String, Object>> saveUsuario(@RequestBody Usuario u) {
	//public String saveUsuario(@RequestBody Usuario u) {// USADO SOLO PARA TESTEO DE UN EXPERIMENTO
		
		//Body de la rspuesta
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		//Instancia de Headers
		HttpHeaders headers =  new HttpHeaders();
		
		//Lista de MediaType soportados por este recurso
		List<MediaType> listMediaType = new ArrayList<>();
		listMediaType.add(MediaType.APPLICATION_JSON);
		listMediaType.add(MediaType.APPLICATION_XML);
		listMediaType.add(MediaType.TEXT_XML);
		listMediaType.add(MediaType.TEXT_HTML);
		listMediaType.add(MediaType.TEXT_PLAIN);
		
		//Se agrega los MediaType a los Headers
		headers.setAccept(listMediaType);
		
		//Se declara el ResponseEntity
		ResponseEntity<HashMap<String, Object>> resp = null;
		HttpStatus estatus;
		
		try {
			//Devolucion del Service		
			String mensaje = usuarioService.saveUsuario(u);
			
			//Body
			map.put("mensaje", mensaje);
			//Headers		
			if(mensaje.contains("No. Cliente")) {
				headers.add("resultado", "success");
			}else {
				headers.add("resultado", "fail");				
			}			
			
			//HttpStatusCode
			estatus = HttpStatus.OK;
			
			resp = new ResponseEntity<>(map, headers, estatus);
			return resp;
			
			// ORIGINALMENTE
			//return usuarioService.saveUsuario(u);		
		}catch(Throwable t) {
			t.printStackTrace();
			
			//Body
			map.put("mensaje", "Error en la peticion:\n" + t.toString());
			//Headers
			headers.add("causa", t.getMessage());
			headers.add("error", "No se agrego al usuario");
			headers.add("resultado", "fail");
			//Estatus
			estatus = HttpStatus.BAD_REQUEST;
			
			resp = new ResponseEntity<HashMap<String, Object>>(map, headers, estatus);
			return resp;
		}
	}
	
	@PutMapping(value = "/actualizar", consumes = { MediaType.APPLICATION_JSON_VALUE }, 
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE,
					MediaType.TEXT_XML_VALUE, MediaType.TEXT_HTML_VALUE, MediaType.TEXT_PLAIN_VALUE })// USADO SOLO PARA TESTEO DE UN EXPERIMENTO
	public ResponseEntity<HashMap<String,Object>> updateUsuario(@RequestBody Usuario u) {
		//Body de la rspuesta
		HashMap<String,Object> map = new HashMap<String,Object>();
		
		//Instancia de Headers
		HttpHeaders headers =  new HttpHeaders();
		
		//Lista de MediaType soportados por este recurso
		List<MediaType> listMediaType = new ArrayList<>();
		listMediaType.add(MediaType.APPLICATION_JSON);
		listMediaType.add(MediaType.APPLICATION_XML);
		listMediaType.add(MediaType.TEXT_XML);
		listMediaType.add(MediaType.TEXT_HTML);
		listMediaType.add(MediaType.TEXT_PLAIN);
		
		//Se agrega los MediaType a los Headers
		headers.setAccept(listMediaType);
		
		//Se declara el HttpStatusCode y ResponseEntity
		HttpStatus estatus  = HttpStatus.OK;
		ResponseEntity<HashMap<String,Object>> resp = null;		
		
		try {			
			//Devolucion del Servic
			Map<String, Object> resultado = usuarioService.saveOrUpdateUsuario(u);
			
			//Generacion del Body
			if ( ((Boolean)resultado.get("exito")).booleanValue() ) {
				//Recuperación del objeto
				//Usuario recuperado = usuarioService.getUsuarioByLogin(u.getLogin());
				Usuario recuperado = (Usuario) resultado.get("data");
				
				//Body: Setteando valores al body
				map.put("resultado", "success");
				
				//Headers
				headers.add("resultado", "success");
			} else {
				//Body: Setteando valores al body
				map.put("resultado", "fail");				
				
				//Headers
				headers.add("resultado", "fail");
			}						
			
			//Completando al Body
			map.put("data", (Usuario) resultado.get("data"));
			map.put("mensaje", resultado.get("mensaje"));
			
			//Genacion del ResponseEntity
			resp = new ResponseEntity<>(map, headers, estatus);
			
			return resp;
		}catch(Throwable t) {
			t.printStackTrace();
			
			//Body
			//NO APLICA
			
			//Headers
			headers.add("mensaje", t.getMessage());
			headers.add("descripcion", "No se agrego al usuario");
			headers.add("resultado", "fail");
			headers.add("causa-backend", t.getMessage());
			
			//Estatus y ResponseEntity
			resp = new ResponseEntity<>( headers, HttpStatus.NOT_FOUND);
			
			return resp;
		}
	}
	
	@DeleteMapping(value = "/eliminar/{id}", consumes = { MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE },
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE,
					MediaType.TEXT_XML_VALUE, MediaType.TEXT_HTML_VALUE, MediaType.TEXT_PLAIN_VALUE })// USADO SOLO PARA TESTEO DE UN EXPERIMENTO)
	public ResponseEntity<HashMap<String, Object>> deleteUsuario (@PathVariable("id") String login) {
		//Body de la Respuesta
		HashMap<String,Object> map = new HashMap<String,Object>();
		
		//Instancia de Headers
		HttpHeaders headers =  new HttpHeaders();
		
		//Lista de MediaType soportados por este recurso
		List<MediaType> listMediaType = new ArrayList<>();
		listMediaType.add(MediaType.APPLICATION_JSON);
		listMediaType.add(MediaType.APPLICATION_XML);
		listMediaType.add(MediaType.TEXT_XML);
		listMediaType.add(MediaType.TEXT_HTML);
		listMediaType.add(MediaType.TEXT_PLAIN);
		
		//Se agrega los MediaType a los Headers
		headers.setAccept(listMediaType);
		
		//Se declara el HttpStatusCode y ResponseEntity
		HttpStatus estatus  = HttpStatus.OK;
		ResponseEntity<HashMap<String,Object>> resp = null;	
		
		try {					
			//Se recupera el usuario esperando a que exista
			Usuario u = usuarioService.getUsuarioByLogin(login);
			
			//Validacion de que exista el usuario
			if ( u == null ) {
				System.out.println("Usuario no encontrado");
				//return;
				
				//Body: Setteando valores al body
				map.put("mensaje", "Usuario no encontrado");				
				map.put("resultado", "fail");				
				
				//Headers
				headers.add("mensaje", "No existe usuario");
				headers.add("descripcion", "No se encontró el usuario registrado bajo ese Login (ID)");
				headers.add("resultado", "fail");
				
				//Estatus y ResponseEntity
				return new ResponseEntity<HashMap<String,Object>>(map, headers, estatus);
			}
			
			//Elimina al usuario; el método es un void
			usuarioService.deleteUsuario(u);
			
			//Se comprueba que se haya eliminado
			if (usuarioService.getUsuarioByLogin(login) == null) {
				System.out.println("Eliminado exitosamente");
				
				//Body: Setteando valores al body
				map.put("resultado", "success");				
				map.put("mensaje", "Eliminado exitosamente");
				
				//Headers
				headers.add("resultado", "success");
				
				//SI TODO FUE EXITOSO, SE RETORNA ESTO
				return new ResponseEntity<HashMap<String,Object>>(map, headers, estatus);
			}
			
			//Si no se elimino, se devuelve esto
			//Body: Setteando valores al body
			map.put("resultado", "fail");				
			map.put("mensaje", "No fue posible eliminar al usuario");
			
			//Headers
			headers.add("resultado", "fail");			
			
			resp = new ResponseEntity<HashMap<String,Object>>(map, headers, estatus);
			return resp;
		}catch(Throwable t) {
			t.printStackTrace();
			
			//Body
			//NO APLICA
			
			//Headers
			headers.add("mensaje", t.getMessage());
			headers.add("descripcion", "No se agrego al usuario");
			headers.add("resultado", "fail");
			headers.add("causa-backend", t.getMessage());
			
			//Estatus
			estatus = HttpStatus.BAD_REQUEST;
			
			//Retorno del Catch
			resp = new ResponseEntity<>( headers, estatus);
			return resp;
		}
	}
	
	
	@GetMapping(value = "filtro-estatus", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE })
	public List<Usuario> getListOfUsuariosByEstatus(@RequestParam(name="estatus") String estatus) {		
		return usuarioService.getListOfUsuariosByEstatus(estatus.toUpperCase());
	}
	
	@GetMapping(value = "filtro", //consumes = {MediaType.APPLICATION_JSON_VALUE}, 
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE })
	//public List<Usuario> getListOfUsuariosByFiltros
	public ResponseEntity<List<Usuario>> getListOfUsuariosByFiltros
	//(@RequestParam Map<String, String> filtros) {		
	(@RequestParam LinkedHashMap<String, String> filtros) {	
		//Body de la rspuesta
		//HashMap<String, Object> map = new HashMap<String, Object>();
		
		//Instancia de Headers
		HttpHeaders headers =  new HttpHeaders();
		
		//Lista de MediaType soportados por este recurso
		List<MediaType> listMediaType = new ArrayList<>();
		listMediaType.add(MediaType.APPLICATION_JSON);
		listMediaType.add(MediaType.APPLICATION_XML);
		listMediaType.add(MediaType.TEXT_XML);
		listMediaType.add(MediaType.TEXT_HTML);
		listMediaType.add(MediaType.TEXT_PLAIN);
		
		//Se agrega los MediaType a los Headers
		headers.setAccept(listMediaType);
		
		//HttpStatus
		HttpStatus estatus ;
		
		//Se declara el ResponseEntity
		//ResponseEntity<HashMap<String, Object>> resp = null;
		//ResponseEntity<List<Usuario>> resp = new ResponseEntity<List<Usuario>>(HttpStatus.I_AM_A_TEAPOT);//408		
		ResponseEntity<List<Usuario>> resp = null;		
		
		try {		
			if(filtros.containsKey(UsuarioConstantes.FILTRO_NOMBRE) 
					&& filtros.get(UsuarioConstantes.FILTRO_NOMBRE).equalsIgnoreCase("ERROR")) {
				headers.add("cantidad", "0");
				headers.add("estatus", "202");
				headers.add("status code", String.valueOf(HttpStatus.ACCEPTED));				
				headers.add("resultado", "fail");
				
				//Agregando cabeceras
				//resp =  new ResponseEntity<List<Usuario>>(null, headers, HttpStatus.OK);
				resp =  new ResponseEntity<List<Usuario>>(new ArrayList<Usuario>(),headers, HttpStatus.ACCEPTED);				
				return resp;
			}
			
			//return usuarioService.getListOfUsuariosByFiltros(filtros);
			List<Usuario> lista = usuarioService.getListOfUsuariosByFiltros(filtros);			
			headers.add("cantidad", String.valueOf(lista.size()));
			
			resp =  new ResponseEntity<List<Usuario>>(lista, headers, HttpStatus.OK);
			return resp;
		}catch(Throwable t) {			
			t.printStackTrace();
			//return new ArrayList<Usuario>();
			
			//Headers
			headers.add("mensaje", t.getMessage());
			headers.add("descripcion", "No se agrego al usuario");
			headers.add("resultado", "fail");
			headers.add("causa-backend", t.getMessage());
			
			//Estatus
			estatus = HttpStatus.INTERNAL_SERVER_ERROR;
			
			//Retorno del Catch			
			resp = new ResponseEntity<>( headers, estatus);
			resp.ofNullable(new ArrayList<Usuario>());
			return resp;
		}
	}
	
	//metodo para devolver el server.port = mpdesp
	@GetMapping(value = "mpdesp", //consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
	public Map<String,String> mpdesp() {
		Map<String, String> datos = new HashMap<String, String>();
		datos.put("serverPort",	String.valueOf(serverPort));
		return datos; 
	}
	
	/* POR ELIMINAR DE ESTA CAPA
	 * */
	/*
	@GetMapping(value = "pass", produces = MediaType.APPLICATION_JSON_VALUE)
	public String cifrar_y_encryptar(@RequestParam("pass") String pass) {
		
		System.out.println("\n====UsuarioController.class=========================================");
		
		String codificado = this.codificarBase64(pass.getBytes());
		String decodificado = this.decodificarBase64(codificado);
		
		System.out.println("\nTexto Codificado: '" + codificado + "'\n"
				+ "Texto Decodificado: '" + decodificado + "'");
		
		//Encryptando el texto a SHA-1
		byte [] hashed = this.encryptarSHA256(pass);
		if(hashed.length == 0) {
			return "No se pudo encryptar";
		}
		
		//Convirtiendo a Base64
		String hashToString = codificarBase64(hashed); 		
		System.out.println("Hasheado y Codificado: |" + hashToString 
				+ "| + length=" + hashToString.length());
		
		///* HIPOTESIS
		// * Prueba para ver si se podia enviar directamente un String (byte[].toString() )
		// * en lugar del byte[] despues de que se encryptase 
		// * (es decir, MessageDigest.digest() ).
		// *  
		// *  RESULTADO
		// *  En cada prueba, al devolver el String encryptado (byte[].toString() ) 
		// *  y pasarlo a cifrar (Base64.encodeToString() ) se cambia el texto
		// *  cifrado por el sistema, y por tanto, la integridad de la contraseña
		// *  
		// *  Por lo tanto, no usar el metodo encryptarSHA256aString(String) 
		// *  
		//System.out.println("\n PRUEBA - Hashed.toString() codificado a Base64 \n");
		//
		////Convirtiendo a Base64
		//String hashedByString = encryptarSHA256aString(pass);
		//String hashToString2 = codificarBase64(hashedByString.getBytes()); 		
		//System.out.println("Hasheado y Codificado: |" + hashToString2 
		//		+ "| + length=" + hashToString2.length());
		//
		
		return hashToString;
	}
	
	///NOTA
	 // Mover a CAPA Service porque es más logica de negocio 
	 // que de Controller
	 ///
	private String codificarBase64(byte[] texto) {
		//Obj. de la respuesta despues de ser codificada a Base64
		String texto64;
		
		//Instancia del codificador
		Base64.Encoder encoder = Base64.getEncoder();
		// byte[] txtBase64 = Base64Utils.encode(texto.getBytes()); //Utileria de Spring (deprecada)
		
		// Codificacion
		texto64 = encoder.encodeToString(texto);
		
		return texto64;
	}
	
	private String decodificarBase64(String texto64) {
		Base64.Decoder decoder = Base64.getDecoder();
		
		byte[] decodificado = decoder.decode(texto64);
		
		//convertir el Byte[] a String
		String texto = new String(decodificado);
		return texto;
	}
	
	private byte[] encryptarSHA256(String texto) {
		try {
			//Se obtiene la instancia del objeto que hara la función Hash SHA-256
			MessageDigest md =  MessageDigest.getInstance("SHA-256");
			
			//Se le pasa el texto a "ecnryptar" en Hash (a traves de sus byte[] )
			md.update(texto.getBytes("UTF-8"));
			
			//Se convierte a hash el texto y esto devuelve byte[]
			byte[] hashed = md.digest();
			
			// /*
			// System.out.print("\nHash: '");
			// System.out.print( hashed );
			// System.out.print( "'" );
			// System.out.println("\nHash (toString()): '" + hashed.toString() 
			// 	+ "'\nRepresentacion Hex:");
			// * /
			
			// Se escribe byte a byte en hexadecimal
		      for (byte b : hashed) {
		         System.out.print(Integer.toHexString(0xFF & b));
		      }
		      System.out.println();
			
			// Gracias a un constructor de String, se devuelve el hash a una cadena
			//return new String(hashed); //No funciona
		      
		      return hashed;
		      
		} catch (NoSuchAlgorithmException e) {
			// EXCEPCION en caso de no encontar el algoritmo de la codificacion
			e.printStackTrace();			
		} catch (UnsupportedEncodingException e) {
			// EXCEPCION en caso de no encontrar la codificacion indicada en "UTF-8"
			e.printStackTrace();
		}
		return new byte[0];
	}
	 */
}
