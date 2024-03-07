package csam.pruebatecnica.adea.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import csam.pruebatecnica.adea.model.Usuario;
import java.util.List;

/* NOTAS:
 * Metodos que serán heredados por la implementacion de Spring Data JPA
 * 
 */
public interface UsuarioJPA extends JpaRepository<Usuario, String> {
	
	///Metodos heredados de JPA
	Usuario findByLogin(String login);
	
	Usuario findByEmail(String email);
	
	//Eliminado por duplicidad con JpaRepository
	//List<Usuario> findAll(); 
	
	// Busca al usuario por login (ID) y contraseña
	Usuario findByLoginAndPass(String login, String pass);
	
	// Comprobar que exista el usuario con las credenciales enviadas desde el front
		// Sintaxis por Posicion
	//@Query(value="SELECT * FROM Usuario u WHERE u.login=?1 AND u.pass=?2", nativeQuery = true)
		// Sintaxis por Notacion
	@Query(value="SELECT * FROM Usuario u WHERE u.login=:login AND u.pass=:pass", nativeQuery = true, name = "getUsuarioByLoginAndPass") 	
	Usuario getByLoginAndPass(String login, String pass);
	
	// Busca una lista de usuarios con cierto estatus
	@Query(value="SELECT * FROM Usuario u WHERE u.estatus =?1", nativeQuery = true)
	List<Usuario> getAllUsuarioByEstatus(String estatus);
	
	// Busca una lista de usuarios con cierto filtros
	@Query(value="SELECT * FROM Usuario u WHERE :filtros", nativeQuery = true)
	List<Usuario> getAllUsuarioByFiltros(String filtros);
}
