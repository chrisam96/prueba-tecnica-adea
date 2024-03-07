package csam.pruebatecnica.adea.controller;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import csam.pruebatecnica.adea.model.Usuario;
import csam.pruebatecnica.adea.model.UsuarioCredenciales;
import csam.pruebatecnica.adea.service.UsuarioService;;

@CrossOrigin(origins = "*")
@RestController
//@Controller
public class UsuarioController {

	//Bean del UsuarioService
	@Autowired
	UsuarioService usuarioService;
	
	@GetMapping(value = "usuario/{login}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Usuario getUsuarioByLogin(@PathVariable("login") String login) {
		
		return usuarioService.getUsuarioByLogin(login);
	}
	
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
	
	//ALT + SHIFT X, B
	
	@GetMapping(value = "/prb") // @GetMapping	(value) == @RequestMapping(path="/foo").
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
	
	/*-----------------------------------------------------------------------------------*/
	// METODOS DE APLICATIVO
	
	@PostMapping(value = "/registrar", consumes = { MediaType.APPLICATION_JSON_VALUE }, 
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE })
	public String saveUsuario(@RequestBody Usuario u) {
	//public Usuario saveUsuario(@RequestBody Usuario u) {
		try {
			return usuarioService.saveUsuario(u);		
		}catch(Throwable t) {
			t.printStackTrace();
			return null;
		}
	}
	
	@PutMapping(value = "/actualizar", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public String updateUsuario(@RequestBody Usuario u) {
		try {
			return usuarioService.saveOrUpdateUsuario(u);
		}catch(Throwable t) {
			t.printStackTrace();
			return null;
		}
	}
	
	@DeleteMapping(value = "/eliminar/{id}", consumes = { MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public void deleteUsuario (@PathVariable("id") String login) {
		try {						
			Usuario u = usuarioService.getUsuarioByLogin(login);
			
			//Validacion de que exista el usuario
			if ( u == null ) {
				System.out.println("Usuario no encontrado");
				return;
			}
			
			usuarioService.deleteUsuario(u);
			
			if (usuarioService.getUsuarioByLogin(login) == null) {
				System.out.println("Eliminado exitosamente");
			}
		}catch(Throwable t) {
			t.printStackTrace();
		}
	}
	
	/*@GetMapping(value = { "/login1/{login}/{pass}", "/get-{login}-{pass}" }
	, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE  })
	public Usuario getUsuarioByCredenciales(@PathVariable("login") String login, @PathVariable("pass") String pass) {*/
	@GetMapping(value = { "/login1", "/get-{login}-{pass}" },
		consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE },
		produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE  })
	public Usuario getUsuarioByCredenciales(@RequestBody UsuarioCredenciales uc) {
		//try {										
			System.out.println("ENTRO: " + uc.toString());
			return usuarioService.getUsuarioByCredenciales(uc);
		/*
		 }catch(Throwable t) {
			t.printStackTrace();
			return new Usuario();
		}
		*/
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
	
	@GetMapping(value = "filtro-estatus", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE })
	public List<Usuario> getListOfUsuariosByEstatus(@RequestParam(name="estatus") String estatus) {		
		return usuarioService.getListOfUsuariosByEstatus(estatus.toUpperCase());
	}
	
	@GetMapping(value = "filtro", //consumes = {MediaType.APPLICATION_JSON_VALUE}, 
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE })
	public List<Usuario> getListOfUsuariosByFiltros
	//(@RequestParam Map<String, String> filtros) {		
	(@RequestParam LinkedHashMap<String, String> filtros) {		
		try {
			return usuarioService.getListOfUsuariosByFiltros(filtros);
		}catch(Throwable t) {			
			t.printStackTrace();
			return new ArrayList<Usuario>();
		}
	}
	
	/* POR ELIMINAR DE ESTA CAPA
	 * */
	
	@GetMapping(value = "pass/", produces = MediaType.APPLICATION_JSON_VALUE)
	public String cifrar_y_encryptar(@RequestParam("pass") String pass) {
		
		System.out.println("\n=============================================");
		
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
		
		/* HIPOTESIS
		 * Prueba para ver si se podia enviar directamente un String (byte[].toString() )
		 * en lugar del byte[] despues de que se encryptase 
		 * (es decir, MessageDigest.digest() ).
		 *  
		 *  RESULTADO
		 *  En cada prueba, al devolver el String encryptado (byte[].toString() ) 
		 *  y pasarlo a cifrar (Base64.encodeToString() ) se cambia el texto
		 *  cifrado por el sistema, y por tanto, la integridad de la contraseña
		 *  
		 *  Por lo tanto, no usar el metodo encryptarSHA256aString(String) 
		 *  
		System.out.println("\n PRUEBA - Hashed.toString() codificado a Base64 \n");
		
		//Convirtiendo a Base64
		String hashedByString = encryptarSHA256aString(pass);
		String hashToString2 = codificarBase64(hashedByString.getBytes()); 		
		System.out.println("Hasheado y Codificado: |" + hashToString2 
				+ "| + length=" + hashToString2.length());
		*/
		
		return hashToString;
	}
	
	/*NOTA
	 * Mover a CAPA Service porque es más logica de negocio 
	 * que de Controller
	 */
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
			
			/*
			System.out.print("\nHash: '");
			System.out.print( hashed );
			System.out.print( "'" );
			System.out.println("\nHash (toString()): '" + hashed.toString() 
				+ "'\nRepresentacion Hex:");
			*/
			
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

}
