package csam.pruebatecnica.adea.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import csam.pruebatecnica.adea.model.Usuario;
import csam.pruebatecnica.adea.utils.UsuarioConstantes;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class UsuarioDAOImpl implements UsuarioDAO {

	//Bean de Spring Data JPA
	@Autowired
	UsuarioJPA jpa;
	
	@PersistenceContext
	private EntityManager em;
	
	
	@Override
	public Usuario getUsuarioByLogin(String login) {
		//jpa.findByLogin(login);
		//Uso de Optional<> porque sí
		return jpa.findById(login).orElse(null);
	}

	@Override
	public Usuario getUsuarioByEmail(String email) {
		return jpa.findByEmail(email);
	}

	@Override
	public List<Usuario> getAllUsuario() {
		return jpa.findAll();
	}
	
	/*-----------------------------------------------------------------------------------*/
	
	@Override
	public Usuario saveUsuario(Usuario u) {
		return jpa.save(u);
	}

	@Override
	public Usuario updateUsuario(Usuario u) {
		return jpa.saveAndFlush(u);
	}

	@Override
	public void deleteUsuario(Usuario u) {
		jpa.deleteById(u.getLogin());
	}

	//Metodo DEFINIDO por un QUERY de JPA (JPQL) "personalizado"
	// 
	@Override
	public Usuario getUsuarioByCredenciales(String login, String pass) {
		return jpa.getByLoginAndPass(login, pass);
	}
	
	//Metodo IMPLICITO DE JPA para buscar un registro mediante dos campos
	@Override
	public Usuario findUsuarioByLoginAndPass(String login, String pass) {
		return jpa.findByLoginAndPass(login, pass);
	}

	//Metodo Heredado por un Query de JPA (JPQL) "personalizado"
	// Recupera una lista de usuarios con cierto estatus
	@Override
	public List<Usuario> getAllUsuarioByEstatus(String estatus) {
		return jpa.getAllUsuarioByEstatus(estatus);
	}

	@Override
	public List<Usuario> getAllUsuarioByFiltros(Map<String, String> filtros) {		
		try {
			//return jpa.getAllUsuarioByFiltros(porFiltrar);
			
			//AGREGAR CONSULTA CUANDO NO HAY FILTROS
			if (filtros.size() == 0) {
				return this.getAllUsuario();
			}
			
			//AGREGAR CONSULTA CUANDO SOLO HAY DE "estatus"
			if (filtros.size() == 1 && 
					filtros.containsKey(UsuarioConstantes.FILTRO_ESTATUS)) {
				return this.getAllUsuarioByEstatus(filtros.get(UsuarioConstantes.FILTRO_ESTATUS));
			}

			//LinkedHashMap map = (LinkedHashMap<String, String>)filtros;			
			
			// A partir del EntityManager, creamos el EntityManager 
			CriteriaBuilder cb = em.getCriteriaBuilder();			
			// A partir del CriteriaBuilder creamos el CriteriaQuery
			// quién es el encargado de hacer las consultas
			CriteriaQuery<Usuario> query = cb.createQuery(Usuario.class);
			//
			Root<Usuario> root = query.from(Usuario.class);
			
			// Path: representa un SOLO atributo de la entidad.
			Path<Date> fechaAlta = root.get(UsuarioConstantes.FILTRO_FECHA_ALTA);
			
			/* PREDICATE
			 * Para poder buscar las columnas sobre las que queremos 
			 * realizar la consulta necesitaremos un objeto Root, 
			 * que crearemos a partir del anterior objeto CriteriaQuery
			 * 
			 * las condiciones de nuestra query.
			 */
			List<Predicate> predicates = new ArrayList<>();
			
			//ZoneId.getAvailableZoneIds().forEach(z -> System.out.println(z));
		    
		    //Variables para debuggeo y que el codigo no se vea tan largo
		    Predicate pred = null;
		    String valor = "";
	        
		    System.out.println();
		    
		    //Los Predicate son las clausulas en el WHERE
	        if (filtros.containsKey(UsuarioConstantes.FILTRO_NOMBRE)) {
	        	valor = filtros.get(UsuarioConstantes.FILTRO_NOMBRE);
	            pred = cb.like(root.get(UsuarioConstantes.FILTRO_NOMBRE), "%"+valor+"%");
	            predicates.add(pred);
	            System.out.println("NOMBRE = %" + valor + "%");
	        }
	        if (filtros.containsKey(UsuarioConstantes.FILTRO_AP_PATERNO)) {
	        	valor = filtros.get(UsuarioConstantes.FILTRO_AP_PATERNO);
	        	pred = cb.like(root.get(UsuarioConstantes.FILTRO_AP_PATERNO), "%"+valor+"%");
	            predicates.add(pred);
	            System.out.println("AP_PATERNO = %" + valor + "%");
	        }
	        if (filtros.containsKey(UsuarioConstantes.FILTRO_AP_MATERNO)) {
	        	valor = filtros.get(UsuarioConstantes.FILTRO_AP_MATERNO);
	            pred = cb.like(root.get(UsuarioConstantes.FILTRO_AP_MATERNO), "%"+valor+"%");
	            predicates.add(pred);
	            System.out.println("AP_MATERNO = %" + valor + "%");
	        }
	        
	        try {
	        	 // Formateador de Fecha estilo == 2021-03-24 16:48:05
			    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			    /*
			     * Agregado para corregir Bug donde se cambiaban la fecha,
			     * en especifico la cantidad de horas de la fecha por culpa
			     * de la Java-VM, que toma la Zona Horaria del sistema (HOST),
			     * modificando la fecha con una diferencia de horas respecto al GMT. 
			     * 
			     *	Ej: 
			     *		Si la fecha recibida es 	18-Oct 00:00:00 GMT+00:00
			     *		Tras el bug se modifica al 	17-Oct 18:00:00 GMT-06:00
			     *	Por la diferencia de horas respecto a CDMX (America/Mexico_City) 
			     *	que en este caso son -6 hrs. 
			     * 
			     * NOTAS
			     * - Al hacer debug y ver en "pred.rightHandExpression.value"
			     * me di cuenta de la fecha real que se envia a la consulta (Criteria)
			     * y hay se tenia la diferencia de 6 hrs.
			     * 
			     * - En las ultimas pruebas, ahora con el setteo de la Zona Horaria,
			     * "pred.rightHandExpression.value" 
			     * arroja una fecha erronea con 6 hrs de diferencia
			     * pero la consulta ya la hace bien
			     * 
			     * 3 y 6
			     */
			    sdf.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));			   
			    
				//System.out.println(sdf.format(timestamp));
			    Date fecha;			    
			    
	            //FECHA ALTA INICIAL
	            if (filtros.containsKey(UsuarioConstantes.FILTRO_FECHA_ALTA)) {
	                //cb.lessThanOrEqualTo(root.<Date>get(clave), (Date)sdf.parse(valor) );							
	            	valor = filtros.get(UsuarioConstantes.FILTRO_FECHA_ALTA);
	            	
	            	//SEPARAR STRING
	            	
	            	fecha = sdf.parse(valor);
	            	pred = cb.greaterThanOrEqualTo(fechaAlta, fecha);	            	
	            	System.out.println("FECHAALTA >= " + valor);
	                predicates.add(pred);
	                
	                /*System.out.println("\tsdf: GMT String: " + fecha.toGMTString());
	            	System.out.println("\tsdf: LocaleString: " + fecha.toLocaleString());
	            	System.out.println("\tsdf: DefaultTimeZone: " + sdf.getTimeZone().getID());
	            	System.out.println("\tsdf: " + fecha);*/
	            }
	            
	            //FECHA ALTA FINAL
	            if (filtros.containsKey(UsuarioConstantes.FILTRO_FECHA_ALTA_FINAL)) {
	                //cb.lessThanOrEqualTo(root.<Date>get(clave), (Date)sdf.parse(valor) );							
	            	valor = filtros.get(UsuarioConstantes.FILTRO_FECHA_ALTA_FINAL);
	            	fecha = sdf.parse(valor);
	            	
	            	pred = cb.lessThanOrEqualTo(fechaAlta, fecha);
	            	System.out.println("FECHAALTA <= " + valor);
	                predicates.add(pred);
	            }
	        }catch(ParseException e) {
	            e.printStackTrace();
	        }					
	        
	        //ESTATUS
	        if (filtros.containsKey(UsuarioConstantes.FILTRO_ESTATUS)) {
	        	valor = filtros.get(UsuarioConstantes.FILTRO_ESTATUS);
	            pred = cb.equal(root.get(UsuarioConstantes.FILTRO_ESTATUS), valor);
	            predicates.add(pred);
	            System.out.println("ESTATUS = '" + valor + "'");
	            
	            
	            //Pruebas de colecciones
	            ArrayList<String> lista = new ArrayList<String>();
	            String [] array = new String[3];
	            
	            /*NOTA: 
	             * Tanto "inClause" como "root.get().in()" retornan un "Predicate"*/
	            
	            //CriteriaBuilder
	           In<String> inClause = cb.in(root.get(UsuarioConstantes.FILTRO_ESTATUS));	           	            	          
	           for (String elem : lista) {
	        	   inClause.value(elem);
	           } 	           	         
	           
	           //Expressions.in()
	            root.get(UsuarioConstantes.FILTRO_ESTATUS).in(lista);
	        }
	        
	        System.out.println();
	        
	        //Se crea la consulta aqui
	        query
	        	.select(root) //La parte del SELECT
	        	.where(predicates.toArray(new Predicate[predicates.size()] )) // WHERE	        	
	        	.orderBy(cb.desc(fechaAlta)); //ORDER BY
			
	        //A traves del EntityManager se ejecuta el Query (y se devuelve la lista)
			return em.createQuery(query).getResultList();			
		}catch(Throwable t) {
			t.printStackTrace();
			return null;
		}
	}
	
}
