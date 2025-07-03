package csam.pruebatecnica.adea.seguridad.jwt;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTFilter extends OncePerRequestFilter {
	
	@Autowired
	public JWTUtil jwtUtil; 

	//Logger log = LoggerFactory.getLogger(this.getClass());
	Logger log = LoggerFactory.getLogger(JWTFilter.class);
	
	private final List<String> ELEMENTOS_DESCARTADOS = List.of(
		//URLs
		"/", "/login", "/login.html", "/getUser", 
		"/sitio/*.html",
		
		//Carpetas de Recursos Estaticos
		"/asset/**", "/errores/**", "/js/**",
		"/.well-known/**", // ====>> /.well-known/appspecific/com.chrome.devtools.json 
		"/h2-console/**",
		
		//extensiones de archivos
		"/**/*.js",
		"/**/*.jpg", "/**/*.jpeg",
		"/**/*.json", "/**/*.css",
		"/**/*.png", "/**/*.gif",
		"/**/*.woff", "/**/*.woff2",
		"/**/*.ttf", "/**/*.ico"
	);
		
	
	@Autowired
	private RequestMappingHandlerMapping visualizadorPath;
	
	private final AntPathMatcher matcher = new AntPathMatcher();
	
	//Filtra URLs existentes. Si existe => TRUE; Si NO existe => FALSE
	private boolean rutaExiste(HttpServletRequest request) {
		try {
			HandlerExecutionChain chain = visualizadorPath.getHandler(request);
			boolean existe = (chain != null) ? true : false;
			return existe;
			
		} catch (Exception e) {
			return false;
		}
	}
	
	//// Lógica del filtro (excluir URLs y recursos estaticos)
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		//log.warn("=== JWTFilter.shouldNotFilter ===");
		
		String path = request.getServletPath();
		String metodo = request.getMethod();
		String tipoDispatcher = request.getDispatcherType().name();
		System.out.println("=> shouldNotFilter: metodo='" + metodo 
			+ "' , URL(path)='" + path + "' , DispatcherType='" + tipoDispatcher + "'"
			+ "\nFull URL: '" + request.getRequestURL().toString()+ "'");
		
		
		
		/*StringBuilder sr = new StringBuilder("Headers are: '");
		for (Iterator iterator = request.getHeaderNames().asIterator(); iterator.hasNext();) {
			sr.append((String) iterator.next() + "', '");
		} 
		System.out.println(sr.toString());*/
		
		// Sepa que hace -> Añade esta condición PRIMERO
        /*if (request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI) != null) {
            return true;
        }*/
		
		//Indicamos que el filtro IGNORE si el método HTTP es un OPTIONS
		//Que sería una solicitud PREFLIGHT
		if(request.getMethod().equalsIgnoreCase("OPTIONS") ) {
			System.out.println("Peticion viene de un Method: OPTIONS");
			return true;
		}
		
		//Indicamos que el filtro se IGNORE las peticiones tipo ERROR, FORWARD 		
		if(request.getDispatcherType() == DispatcherType.ERROR 
				|| 
			request.getDispatcherType() == DispatcherType.FORWARD) {
			System.out.println("Peticion viene de un DispatcherType de ERROR o FORWARD");
			return true;
		}				
		
		//Busqueda entre URLs que deben descartarse
		boolean encontrado =
		ELEMENTOS_DESCARTADOS.stream()
			.anyMatch(str -> {
				System.out.println("Ruta descartada: " + str);
				return matcher.match(str, path);
			}
		);
		
		if(encontrado){
			//return true;
			return encontrado;//true
		}else {
			System.out.println("No hay ruta descartada");
		}
		
		//Si la ruta NO existe => ignora el JWTFilter (deja que el PagsErrorConfiguration maneje el 404)
        if (!rutaExiste(request)) {
        	System.out.println("ruta no existe");
            return true;
        }
        
		System.out.println("pero la ruta existe.");
        return false;
	}
	
	//// Lógica del filtro (validar JWT)
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		//log.warn("=== JWTFilter.doFilterInternal ===");
		
		String path = request.getServletPath();
		String metodo = request.getMethod();
		String tipoDispatcher = request.getDispatcherType().name();
		
		System.out.println("==>> doFilterInternal: metodo='"+ metodo 
				+ "' , URL(path)='" + path + "' , DispatcherType='" + tipoDispatcher + "'"
				+ "\nFull URL: '" + request.getRequestURL().toString()+ "'");		
		//System.out.println("URLDestino: "+request.getHeader("urlDestino"));
		
		StringBuilder sr = new StringBuilder("Headers are: '");
		for (Iterator iterator = request.getHeaderNames().asIterator(); iterator.hasNext();) {
			sr.append((String) iterator.next() + "', '");
		} 
		System.out.println(sr.toString());
		
		String auth = request.getHeader(jwtUtil.HEADER_TOKEN);
		
		//Validar que exista Header authorization
		if(auth == null || auth.isBlank()) {			
			System.out.println("ERROR: token vacio");
			response.setStatus(response.SC_FORBIDDEN);
			//response.sendError(response.SC_FORBIDDEN, "No hay token; No hay permisos");
			//response.sendRedirect(contextPath + "/errores/sinPermisos.html"); 
			request.getRequestDispatcher("/errores/sinPermisos.html")
				.forward(request, response); 
			return;
		}
				
		//Validar que empiece con "Bearer "
		if(!auth.startsWith(jwtUtil.PREFIJO_TOKEN)) {
			System.out.println("ERROR: token sin prefijo");
			response.setStatus(response.SC_FORBIDDEN);
			//response.sendError(response.SC_FORBIDDEN, "No empieza por 'Bearer '");
			//response.sendRedirect(contextPath + "/errores/sinPermisos.html");
			request.getRequestDispatcher("/errores/sinPermisos.html")
				.forward(request, response); 
			return;
		}
		
		//Validar que no ha expirado
		Date ahora = new Date();
		Date expToken = (Date) jwtUtil.getElement(auth, jwtUtil.EXPIRATION_TOKEN);
		
		if( expToken == null){
			System.out.println("ERROR: token sin fecha de expiracion disponible");
			response.setStatus(response.SC_UNAUTHORIZED);
			//response.sendError(response.SC_UNAUTHORIZED, "Token sin fecha de expiracion");
			request.getRequestDispatcher("/errores/sinAutorizacion.html")
				.forward(request, response);
			return;
		}
		else if( ahora.after(expToken)){
			System.out.println("ERROR: token expirado");
			response.setStatus(response.SC_UNAUTHORIZED);
			//response.sendError(response.SC_UNAUTHORIZED, "Token expirado");			
			//response.sendRedirect(contextPath + "/errores/sinAutorizacion.html");
			request.getRequestDispatcher("/errores/sinAutorizacion.html")
				.forward(request, response); 
			return;
		}
		
		//Validar que sea un JWT válido 
		if (!jwtUtil.validateToken(auth)) {
			System.out.println("ERROR: token no valido");
			response.setStatus(response.SC_UNAUTHORIZED);
			//response.sendError(response.SC_UNAUTHORIZED, "No es un token válido");
			//response.sendRedirect(contextPath + "/errores/sinAutorizacion.html");
			request.getRequestDispatcher("/errores/sinAutorizacion.html")
				.forward(request, response); 
			return;
		}
		
		//Continúa con la cadena de filtros
		filterChain.doFilter(request, response);
	}

}
