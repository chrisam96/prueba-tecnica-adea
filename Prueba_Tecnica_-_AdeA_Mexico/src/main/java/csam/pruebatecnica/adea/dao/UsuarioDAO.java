package csam.pruebatecnica.adea.dao;

import java.util.List;
import java.util.Map;

import csam.pruebatecnica.adea.model.Usuario;

public interface UsuarioDAO {

	//Metodo para recuperar usuario mediante su Login (ID)
	Usuario getUsuarioByLogin(String login);
	
	//Metodo para recuperar usuario mediante Email
	Usuario getUsuarioByEmail(String email);
	
	//Metodo para recuperar todos los usuarios
	List<Usuario> getAllUsuario();
	
	//Metodo para registrar usuario
	Usuario saveUsuario(Usuario u);
	
	//Metodo para actualizar usuario
	Usuario updateUsuario(Usuario u);
	
	//Metodo para eliminar usuario
	void deleteUsuario(Usuario u);
	

	Usuario getUsuarioByCredenciales(String login, String pass);
	
	Usuario findUsuarioByLoginAndPass(String login, String pass);
	
	List<Usuario> getAllUsuarioByEstatus(String estatus);
	
	List<Usuario> getAllUsuarioByFiltros(Map<String, String> filtros);
}
