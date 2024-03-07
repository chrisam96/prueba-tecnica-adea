package csam.pruebatecnica.adea.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import csam.pruebatecnica.adea.model.Usuario;
import csam.pruebatecnica.adea.model.UsuarioCredenciales;

public interface UsuarioService {
	
	//Metodo para devolver al usuario mediante Login
	Usuario getUsuarioByLogin(String login);
	
	//Metodo para devolver al usuario mediante Email
	Usuario getUsuarioByEmail(String email);
	
	//Metodo para devolver todos los usuarios
	List<Usuario> getAllUsuario();
	
	//Usuario saveUsuario(Usuario u);
	String saveUsuario(Usuario u);
	
	//Usuario saveOrUpdateUsuario(Usuario u);
	String saveOrUpdateUsuario(Usuario u);
	
	//Metodo para borrar al Usuario
	void deleteUsuario (Usuario u);
	
	//Metodo para recuperar un Usuario en base al Login y Pass
	//Usuario getUsuarioByCredenciales(String login, String pass);
	Usuario getUsuarioByCredenciales(UsuarioCredenciales uc);

	//Metodo para recuperar un Usuario en base al Login y Pass
	Usuario findUsuarioByLoginAndPass(String login, String pass);
	
	//Metodo para recuperar una Lista en base al tipo de estatus
	List<Usuario> getListOfUsuariosByEstatus(String estatus);

	//Metodo para recuperar un Lista en base a una serie de filtros
	List<Usuario> getListOfUsuariosByFiltros(Map<String, String> filtros);	
	
}
