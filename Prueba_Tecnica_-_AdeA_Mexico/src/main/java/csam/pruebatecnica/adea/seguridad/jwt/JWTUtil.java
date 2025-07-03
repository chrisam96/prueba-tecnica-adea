package csam.pruebatecnica.adea.seguridad.jwt;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtil {

	private static final long DURACION_TOKEN 
		//minutos x seg x milseg
		= 2 * 60 * 1000L;
	
	private static final String SECRET_KEY = "Llave de prueba para el test del JWT";
	
	private static final String EDITOR_TOKEN = "TOKENIZADOR";
	
	public static final String PREFIJO_TOKEN = "Bearer ";

	public static final String HEADER_TOKEN = "authorization";

	public static final String EXPIRATION_TOKEN = "exp";
	
	public static Date FECHA_CREACION = new Date();
	
	public static Date FECHA_EXPIRACION = new Date();
	
	private static final Logger log = LoggerFactory.getLogger(JWTUtil.class);
	
	
	
	public static String generarToken(String username) {
		
		//Solo para test de agregar Claims con un Map
		Map<String, Object> mapOfClaims = new HashMap<String, Object> (); 
		mapOfClaims.put("Map1", "Valor A");
		mapOfClaims.put("Map1", 100L);
		mapOfClaims.put("Map2", "VALOR MAYUS");
		
		System.out.println("la fecha de creacion es: " + new Date());
		
		//Creacion del token
		String token = Jwts //Clase que crea JWT 
				.builder()//Devuelve un JwtBuilder para configurar el JWT
				
				.setSubject(username)
				.setAudience("YO") //Audiencia del Token
				.setIssuedAt(new Date()) //Fecha de creacion 
				.setExpiration(new Date(System.currentTimeMillis() + DURACION_TOKEN)) // Fecha de expiracion
				.setNotBefore(null) //Un JWT obtenido antes de este timestamp NO-sería usado.
				.setIssuer(EDITOR_TOKEN)//Editor del token
				
				//Claims personalizados con claim()
				.claim("claim1", "valor 1")
				.claim("claim1", "valor 2")
				.claim("claim2", "valor 3")
				
				//Claims personalizados con Map
				.addClaims(mapOfClaims)
				
				//.signWith(Key, SignatureAlgorithm)
					//Keys.hmacShaKeyFor: Generador de una SecretKey usando un algoritmo HMAC-SHA y basandose en un array de byte.
					//SignatureAlgorithm: Algoritmo usado para cifrar
				.signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
				
				
				.compact(); //Serializa el JWT y lo retorna como String.
		
		/****/
		//Solo para probar la expiraacion del token
		Date [] fechas = obtenerFechasDelToken(token);
		
		FECHA_CREACION = fechas[0];
		FECHA_EXPIRACION = fechas[1];
		/****/
		
		//JdbcUserDetailsManager jdbcDetails=new JdbcUserDetailsManager(datasource());
		token = PREFIJO_TOKEN.concat(token);
		return token;
	}
	
	private static Date [] obtenerFechasDelToken(String token) {
		Date [] arr = new Date[2];
		
		Claims cl = Jwts
				.parserBuilder()
				.setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))				
				.build()
				.parseClaimsJws(token)
				.getBody();
		
		//Fecha de creacion
		arr[0] = cl.getIssuedAt();
		
		//Fecha de expiracion
		arr[1] = cl.getExpiration();
		
		return arr;
	}
	
	private static Claims getClaims(String token) {
		
		//log.info("=== JWTUtil.getClaims() ===");
		
		try {
			
			if (token == null || token.isBlank()) {
				System.out.println("=== JWTUtil.getClaims(): token vacio o nulo ");
				return null;
			}
						
			//Quitar prefije Bearer
			token = StringUtils.replace(token, PREFIJO_TOKEN, "");
			//System.out.println("token sin prefijo: " + token);
			
			//Credenciales (Claims) del Payload (cuerpo del JWT)
			Claims credenciales = null;
			
			credenciales = (Claims) Jwts
					.parserBuilder()
					//.setSigningKey(SECRET_KEY.getBytes())
					.setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))				
					.build()
					.parseClaimsJws(token)
					//Con getBody() sacamos las credenciales (Claims) del Payload
					.getBody();
			return credenciales;
		} 
		catch(ExpiredJwtException e){
			Long uI = new Date().toInstant().toEpochMilli();
			log.error("=== JWTUtil.getClaims():ExpiredJwtException ===");
			Claims claims = e.getClaims();
		
			System.out.println(".getClaims():ExpiredJwtException ===Fecha de Creacion: " + (claims.getIssuedAt() ));		
			System.out.println(".getClaims():ExpiredJwtException ===Fecha de Expiracion: " + (claims.getExpiration()) );
			
			long fechaEvento = obtenerFechaDelMensajeDeError(e.getMessage());
			System.out.println(".getClaims():ExpiredJwtException ===Aprox. Cant. Milisec del crasheo: " + (fechaEvento - claims.getExpiration().toInstant().toEpochMilli()) );
			System.out.println(".getClaims():ExpiredJwtException ===Cant. Milisec del crasheo: " + (uI - claims.getExpiration().toInstant().toEpochMilli()) );
			
			System.out.println("ERROR at " + new Date().toString() + "\nDescription:\n" + e.getMessage());
			return null;
		}
		catch(IllegalArgumentException | JwtException j) {
			log.error("=== JWTUtil.getClaims():ERROR ===");
			
			System.out.println("ERROR at " + new Date().toString() + "\n" 
					+ j.getMessage());
			return null;
		}
	}
	
	//public static boolean validateToken(String token) {
	public boolean validateToken(String token) {		
		if (getClaims(token) != null) {
			return true;
		} else {
			return false;
		}		
	}
	
	public final static Object getElement(String token, String element) {
		System.out.println("=== JWTUtil.getElement() : "+ element + " ===");
		Claims claims  = getClaims(token);
		
		if (claims == null) {
			return null;
		}
		
		String texto = null;
		Date fecha = new Date();
		
		switch(element) {
			case "sub":
			case "username":
				texto = (String) claims.getSubject();
				break;
			case "aud":
			case "audiencia":
				texto = (String) claims.getAudience();
				break;
			case "iss":
			case "editor_token":
				texto = (String) claims.getAudience();
				break;
			case "iat":
			case "fecha_creacion":
				fecha = (Date) claims.getIssuedAt();
				break;
			case "exp":
			case "fecha_expiracion":
				fecha = (Date) claims.getExpiration();
				break;
			case "nbf":
			case "no_usarse_antes_de":
				fecha = (Date) claims.getNotBefore();
				break;
			default:
				texto = String.valueOf( claims.getOrDefault(element, "No existe") );
				break;
		}
		
		//Object valor = null;
		//valor = (texto != null) ? texto : fecha;
		if (texto != null) {
			return texto;
		}
		return fecha;
	}
	

	private static long obtenerFechaDelMensajeDeError(String message) {
		
		// Patrón para extraer la fecha/hora del tiempo actual
		Pattern pattern = Pattern.compile("Current time: (\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z)");
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            String currentTimeString = matcher.group(1);
            Instant currentTime = Instant.parse(currentTimeString);
           
            // Convertir a zona horaria GMT-6 (America/Belize es GMT-6 sin horario de verano)
            ZonedDateTime zonedDateTime = currentTime.atZone(ZoneId.of("America/Mexico_City"));
            
            // Parsear el instante en UTC (Z indica Zulu time, o GMT)
            //long millis = currentTime.toEpochMilli();            
            long millis = zonedDateTime.toInstant().toEpochMilli();            
            
            
            System.out.println("Fecha y hora extraída: " + currentTimeString);
            System.out.println("Fecha en milisegundos: " + millis);
            
            return millis;
        } else {
            System.out.println("No se pudo encontrar la hora actual en el mensaje.");
            return -1;
        }
	}
	
}
