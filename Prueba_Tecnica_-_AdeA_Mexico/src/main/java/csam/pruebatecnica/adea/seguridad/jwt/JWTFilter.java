package csam.pruebatecnica.adea.seguridad.jwt;

import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
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
		
		//extensiones de archivos
		"/**/*.js",
		"/**/*.jpg", "/**/*.jpeg",
		"/**/*.json", "/**/*.css",
		"/**/*.png", "/**/*.gif",
		"/**/*.woff", "/**/*.woff2",
		"/**/*.ttf", "/**/*.ico"
	);
		
	
	//NO TENGO FORMA DE SABER CUANDO FILTRAR UNA URL QUE NO EXISTE
	//ANTES DE QUE PASE EL JWTFILTER AL MENOS QUE LO HAGA UN BEAN
	//CON UN ORDEN DE MAYOR PRESCEDENCIA
	private final List<String> URL_POR_FILTRAR = List.of(
		""
	);
	
	@Autowired
	private RequestMappingHandlerMapping visualizadorPath;
	
	private final AntPathMatcher matcher = new AntPathMatcher();
	
	
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
			+ "' , URL(path)='" + path + "' , DispatcherType='" + tipoDispatcher + "'");		
		
		/*StringBuilder sr = new StringBuilder("Headers are: '");
		for (Iterator iterator = request.getHeaderNames().asIterator(); iterator.hasNext();) {
			sr.append((String) iterator.next() + "', '");
		} 
		System.out.println(sr.toString());*/
		
		//Indicamos que el filtro IGNORE si el método HTTP es un OPTIONS
		//Que sería una solicitud PREFLIGHT
		if(request.getMethod().equalsIgnoreCase("OPTIONS") ) {
			return true;
		}
		
		//Indicamos que el filtro se IGNORE las peticiones tipo ERROR, FORWARD 		
		if(request.getDispatcherType() == DispatcherType.ERROR 
				|| request.getDispatcherType() == DispatcherType.FORWARD) {
			return true;
		}
		
		
		//Si la ruta NO existe => ignora el JWTFilter (deja que el ErrorController maneje el 404)
        if (!rutaExiste(request)) {
            return true;
        }
		
		boolean encontrado =
		ELEMENTOS_DESCARTADOS.stream()
			.anyMatch(str -> {
				System.out.println("Ruta descartada: " + str);
				return matcher.match(str, path);
			}
		);
		
		if(!encontrado){
			System.out.println("No hay ruta descartada");
		}
		return encontrado;
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
				+ "' , URL(path)='" + path + "' , DispatcherType='" + tipoDispatcher + "'");
		System.out.println("URLDestino:"+request.getHeader("urlDestino"));
		
		//Exclusión de rutas para evitar baneos de URLs		
		Enumeration<String> headers = request.getHeaderNames();
		
		String auth = request.getHeader(jwtUtil.HEADER_TOKEN);
		
		//Validar que exista Header authorization
		if(auth == null || auth.isBlank()) {			
			System.out.println("token vacio");
			response.sendError(response.SC_FORBIDDEN, "No hay token; No hay permisos");
			//response.sendRedirect(contextPath + "/errores/sinPermisos.html"); 
			//request.getRequestDispatcher("/errores/sinPermisos.html").forward(request, response); 
			return;
		}
				
		//Validar que empiece con "Bearer "
		if(!auth.startsWith(jwtUtil.PREFIJO_TOKEN)) {
			System.out.println("token sin prefijo");
			response.sendError(response.SC_FORBIDDEN, "No empieza por 'Bearer '");
			//response.sendRedirect(contextPath + "/errores/sinPermisos.html");
			//request.getRequestDispatcher("/errores/sinPermisos.html").forward(request, response); 
			return;
		}
		
		//Validar que no ha expirado
		Date ahora = new Date();
		Date expToken = (Date) jwtUtil.getElement(auth, jwtUtil.EXPIRATION_TOKEN);
		
		if( expToken == null){
			System.out.println("token sin fecha de expiracion disponible");
			response.sendError(response.SC_UNAUTHORIZED, "Token sin fecha de expiracion");
			return;
		}
		else if( ahora.after(expToken)){
			System.out.println("token expirado");
			response.sendError(response.SC_UNAUTHORIZED, "Token expirado");			
			//response.sendRedirect(contextPath + "/errores/sinAutorizacion.html");
			//request.getRequestDispatcher("/errores/sinAutorizacion.html").forward(request, response); 
			return;
		}
		
		//Validar que sea un JWT válido 
		if (!jwtUtil.validateToken(auth)) {
			System.out.println("token no valido");
			response.sendError(response.SC_UNAUTHORIZED, "No es un token válido");
			//response.sendRedirect(contextPath + "/errores/sinAutorizacion.html");
			//request.getRequestDispatcher("/errores/sinAutorizacion.html").forward(request, response); 
			return;
		}
		
		//Continúa con la cadena de filtros
		filterChain.doFilter(request, response);
	}

}
