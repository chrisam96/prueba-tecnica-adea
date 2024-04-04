package csam.pruebatecnica.adea.model;

import java.io.Serializable;

public class UsuarioCredenciales implements Serializable{
	//Propiedades
	private String login;
	private String pass;
	
	public UsuarioCredenciales() {
	}
	
	public UsuarioCredenciales(String login, String pass) {	
		this.login = login;
		this.pass = pass;
	}
	
	//Getters & Setters
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
