package csam.pruebatecnica.adea.service;

import static csam.pruebatecnica.adea.utils.UsuarioConstantes.ESTATUS_ACTIVO;
import static csam.pruebatecnica.adea.utils.UsuarioConstantes.ESTATUS_INACTIVO;
import static csam.pruebatecnica.adea.utils.UsuarioConstantes.ESTATUS_REVOCADO;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import csam.pruebatecnica.adea.dao.UsuarioDAO;
import csam.pruebatecnica.adea.model.Usuario;
import csam.pruebatecnica.adea.model.UsuarioCredenciales;
import csam.pruebatecnica.adea.utils.UsuarioConstantes;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	//Bean de UsuarioJPA
	@Autowired
	UsuarioDAO usuarioDAO;
	
	//Recupera el usuario mediante el LOGIN
	@Override
	public Usuario getUsuarioByLogin(String login) {
		if (login == null) {
			System.out.println("No hay login - Se recibio NULL");
			return null;
		}
		return usuarioDAO.getUsuarioByLogin(login);
	}

	//Recupera el usuario mediante el EMAIL
	@Override
	public Usuario getUsuarioByEmail(String email) {
		if (email == null) {
			System.out.println("No hay EMAIL - Se recibio NULL");
			return null;
		}
		return usuarioDAO.getUsuarioByEmail(email);
	}

	//Recupera todos los usuarios
	@Override
	public List<Usuario> getAllUsuario() {
		List<Usuario> usuario = usuarioDAO.getAllUsuario();
		if (usuario.size() <= 0) {
			System.out.println("NO HAY USUARIOS DISPONIBLES");
			return new ArrayList<Usuario>();
		}
		return usuario;
	}

	/*-----------------------------------------------------------------------------------*/
	/*
	@Override
	public Usuario saveUsuario(Usuario u) {		
		return usuarioDAO.saveUsuario(u);
	}
	*/
	
	@Override
	//public Usuario saveUsuario(Usuario u) {
	public String saveUsuario(Usuario u) {
		
		//Validacion de LOGIN != NULL
		if (u.getLogin() == null || u.getLogin().isBlank()) {			
			//System.out.println("No hay Login -> se recibio un NULL");
			//return null;
			return "No hay Login ingresado -> Se recibio un valor NULL";
		}
		
		Usuario encontrado = this.getUsuarioByLogin(u.getLogin());
		
		//Si no existe, se podra registrar
		if ( encontrado == null ) {
			
			//Encryptamos la contraseña antes de guardar al objeto
			u.setPass(this.cifrar_y_encryptar( u.getPass()) );
			
			//FechaModificaion es igual a la FechaAlta
			if (u.getFechaalta() == null) {
				u.setFechaalta(new Date());
			}
			if (u.getFechamodificacion() == null) {
				u.setFechamodificacion(new Date());
			}
			
			Usuario nuevo = usuarioDAO.saveUsuario(u);
			
			//Si devuelve un null es que no se pudo guardar
			if(nuevo != null) {
				System.out.println("Registrado: " + nuevo.getLogin());
				//return nuevo;
				return "Usuario: " + nuevo.getLogin() + " - No. Cliente: " + (int)nuevo.getCliente() + " registrado";
			}
			
			System.out.println("Hay un problema para registrar");
			//return null;
			return "Hay un problema para registrar";
		}
		//Usuario ya existente
		else {
			
			//System.out.println("Usuario ya registrado");
			//return null;
			return "Usuario ya registrado";
		}
		//return usuarioDAO.saveUsuario(u);
	}

	@Override
	//public Usuario saveOrUpdateUsuario(Usuario u) {
	public String saveOrUpdateUsuario(Usuario u) {
		
		//Validacion de LOGIN != NULL
		if (u.getLogin() == null || u.getLogin().isBlank()) {			
			//System.out.println("No hay Login -> se recibio un NULL");
			//return null;
			return "No hay un login ingresado, se recibio un valor NULL";
		}
		
		//Si existe, se podra actualizar
		Usuario encontrado = this.getUsuarioByLogin( u.getLogin() );
		if ( encontrado != null ) {
			
			/* Validacion de la contraseña
			 * 	Se compara si se modifico la contraseña
			 * 		SI ES LA MISMA 	=> ya no se guarda
			 * 		SI ES DIFERENTE => se encrypta
			 */
			String passEncrypt = this.cifrar_y_encryptar(u.getPass());
			if ( !passEncrypt.equals(encontrado.getPass() ) ) {
				u.setPass(passEncrypt);
			}else {
				u.setPass(encontrado.getPass());
			}
			
			//FechaModificaion es igual a la FechaAlta
			if (u.getFechaalta() == null) {
				u.setFechaalta(encontrado.getFechaalta());
			}
			if (u.getFechamodificacion() == null) {
				u.setFechamodificacion(new Date());
			}
			
			Usuario nvo = usuarioDAO.updateUsuario(u);
			
			//Si devuelve un null es que no se pudo guardar
			if(nvo != null) {
				System.out.println("Actualizado: " + nvo.getLogin());
				return "Usuario: " + nvo.getLogin() + " actualizado";
			}
			return "Hay un problema para actualizar al usuario";					
		}
		//No encontrado == null
		
		else {
			return "Usuario no encontrado";
		}
		//return usuarioDAO.updateUsuario(u);
	}

	@Override
	public void deleteUsuario(Usuario u) {
		usuarioDAO.deleteUsuario(u);
	}
	
	/*
	@Override
	public Usuario getUsuarioByCredenciales(String login, String pass) {
		//Validacion de que los parametros no sean nulos
		if (login != null || pass != null) {
			//Validacion de que no este vacio ni sean puros espacios en blanco
			if (!login.isBlank() && !pass.isBlank()) {

			//#VALIDACION# 2 de que la contraseña coincida con la registrada en usuario.
				//Encriptar el pass enviado del front
				uc.setPass(this.cifrar_y_encryptar(uc.getPass()) );
				
				Usuario encontrado;
				
				
				
				return usuarioDAO.getUsuarioByCredenciales(login, pass);
			}
		}
		return null;
	}
	*/

	@Override
	//public Usuario getUsuarioByCredenciales(String login, String pass) {
	public Usuario getUsuarioByCredenciales(UsuarioCredenciales uc) {
		
		//Validacion de que los parametros no sean nulos
		if (uc.getLogin() != null || uc.getPass() != null) {
			//Validacion de que no este vacio ni sean puros espacios en blanco
			if (!uc.getLogin().isBlank() && !uc.getPass().isBlank()) {
				
			//#VALIDACION# 2 de que la contraseña coincida con la registrada en usuario.
				//Encriptar el pass enviado del front
				uc.setPass(this.cifrar_y_encryptar(uc.getPass()) );
				
			//#VALIDACION# 1 de que el usuario existe en la tabla usuario.
				Usuario encontrado = usuarioDAO.getUsuarioByCredenciales(uc.getLogin(), uc.getPass());
				
				//Si es diferente de NULL es porque lo encontro
				if (encontrado != null) {
					
				//#VALIDACION# 4 No permitir el acceso si la fecha de caducidad a vencido
					Date ahora = new Date(); //Fecha actual
					
					//Si HOY es mayor a FechaVigencia => Prohibir el acceso
					//25/02/2024 es mayor 21/02/2024 ==> Para fuera
					System.out.println(encontrado.getFechaVigencia());
					if(ahora.after( encontrado.getFechaVigencia() )) {
						//return new Usuario();
						System.out.println("Usuario con la Fecha de Vigencia EXPIRADA");
						Usuario expirado = new Usuario(uc.getLogin());
						return expirado;
					}
					
					//Al pasar TODAS LAS VALIDACIONES, se devuelve el objeto
					return encontrado;								
				}
				//Usuario no existente
				//return new Usuario();
				return null;
				
				//return usuarioDAO.getUsuarioByCredenciales(login, pass);
			}
		}
		return null;
	}

	@Override
	public Usuario findUsuarioByLoginAndPass(String login, String pass) {
		//Validacion de nulo
		if (login != null || pass != null) {
			//Validacion de que no este vacio ni sean puros espacios en blanco
			if (!login.isBlank() && !pass.isBlank()) {
				return usuarioDAO.findUsuarioByLoginAndPass(login, pass);
			}
		}
		return null;
	}

	/*
	@Override
	public List<Usuario> getListOfUsuariosByEstatus(String estatus) {
		//Validacion de nulo
		if (estatus != null) {
			//Validacion de que no este vacio ni sean puros espacios en blanco
			if (!estatus.isBlank()) {
				return usuarioDAO.getAllUsuarioByEstatus(estatus);
			}
		}
		return null;
	}
	*/
	
	@Override
	public List<Usuario> getListOfUsuariosByEstatus(String estatus) {
		//Validacion de nulo y de string vacio/solo espacios en blanco
		if (validarEstatus(estatus)) {
			List<Usuario> lista = usuarioDAO.getAllUsuarioByEstatus(estatus);
			
			//Si es diferente de NULL es porque encontro a todos los usuarios con el mismo estatus
			if (lista != null) {
				System.out.println("Tamaño de la lista: " + lista.size());
				return lista;
			}
			
			System.out.println("Estatus '" + estatus +"' sin Usuarios ");
			return new ArrayList<Usuario>();			
			//return usuarioDAO.getAllUsuarioByEstatus(estatus);			
		}
		
		System.out.println("Estatus no valido");
		return new ArrayList<Usuario>();
		//return null;
	}

	@Override
	public List<Usuario> getListOfUsuariosByFiltros(Map<String, String> filtros) {
		try {
			
			
			List<Usuario> lista =  usuarioDAO.getAllUsuarioByFiltros(filtros);
		
			return lista;
		}catch (Throwable e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	/*-----------------------------------------------------------------------------------*/
	
	//METODOS AUXILIARES
	private boolean validarEstatus(String estatus) {
		//Validacion del NULL y de que no este vacio ni sean puros espacios en blanco
		if (estatus == null || estatus.isBlank()) {
			return false;
		}
		
		String [] arr_estatus = {ESTATUS_ACTIVO, ESTATUS_INACTIVO, ESTATUS_REVOCADO};
		for (String tipo : arr_estatus) {
			if (estatus.equalsIgnoreCase(tipo)) {
				return true;
			}
		}		
		
		return false;
	}
	
	public String cifrar_y_encryptar(String pass) {
		
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

	@Deprecated
	private String encryptarSHA256aString(String texto) {
		try {
			//Se obtiene la instancia del objeto que hara la función Hash SHA-256
			MessageDigest md =  MessageDigest.getInstance("SHA-256");
			
			//Se le pasa el texto a "ecnryptar" en Hash (a traves de sus byte[] )
			md.update(texto.getBytes("UTF-8"));
			
			//Se convierte a hash el texto y esto devuelve byte[]
			byte[] hashed = md.digest();
			
			System.out.print("\nHash: '");
			System.out.print( hashed );
			System.out.print( "'\n" );
			System.out.println("\nHash (toString()): '" + hashed.toString() 
				+ "'\nRepresentacion Hex:");
			
			// Se escribe byte a byte en hexadecimal
		      for (byte b : hashed) {
		         System.out.print(Integer.toHexString(0xFF & b));
		      }
		      System.out.println();
			
			// Gracias a un constructor de String, se devuelve el hash a una cadena
			//return new String(hashed);
		      
		      return hashed.toString();
		      
		} catch (NoSuchAlgorithmException e) {
			// EXCEPCION en caso de no encontar el algoritmo de la codificacion
			e.printStackTrace();			
		} catch (UnsupportedEncodingException e) {
			// EXCEPCION en caso de no encontrar la codificacion indicada en "UTF-8"
			e.printStackTrace();
		}
		return new String();
	}



}
