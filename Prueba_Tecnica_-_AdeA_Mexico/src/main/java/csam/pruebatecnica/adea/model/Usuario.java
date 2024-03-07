package csam.pruebatecnica.adea.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;


/**
 * The persistent class for the usuario database table.
 * 
 */
@Entity
//@Table(name = "USUARIO")
@NamedQuery(name="Usuario.findAll", query="SELECT u FROM Usuario u")
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String login;

	private String pass;
	
	private String nombre;
	
	@Column(name="APELLIDO_PATERNO")
	private String apellidoPaterno;
	
	@Column(name="APELLIDO_MATERNO")
	private String apellidoMaterno;

	private String estatus;

	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaalta;
	
	private BigDecimal area;

	private float cliente;

	private String email;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FECHA_VIGENCIA")
	private Date fechaVigencia;


	@Temporal(TemporalType.TIMESTAMP)
	private Date fechabaja;

	@Temporal(TemporalType.TIMESTAMP)
	private Date fechamodificacion;

	@Temporal(TemporalType.TIMESTAMP)
	private Date fecharevocado;

	private float intentos;

	@Column(name="NO_ACCESO")
	private int noAcceso;

	

	

	public Usuario() {
	}
	
	public Usuario(String login) {
		this.login = login;
	}

	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getApellidoMaterno() {
		return this.apellidoMaterno;
	}

	public void setApellidoMaterno(String apellidoMaterno) {
		this.apellidoMaterno = apellidoMaterno;
	}

	public String getApellidoPaterno() {
		return this.apellidoPaterno;
	}

	public void setApellidoPaterno(String apellidoPaterno) {
		this.apellidoPaterno = apellidoPaterno;
	}

	public BigDecimal getArea() {
		return this.area;
	}

	public void setArea(BigDecimal area) {
		this.area = area;
	}

	public float getCliente() {
		return this.cliente;
	}

	public void setCliente(float cliente) {
		this.cliente = cliente;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEstatus() {
		return this.estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public Date getFechaVigencia() {
		return this.fechaVigencia;
	}

	public void setFechaVigencia(Date fechaVigencia) {
		this.fechaVigencia = fechaVigencia;
	}

	public Date getFechaalta() {
		return this.fechaalta;
	}

	public void setFechaalta(Date fechaalta) {
		this.fechaalta = fechaalta;
	}

	public Date getFechabaja() {
		return this.fechabaja;
	}

	public void setFechabaja(Date fechabaja) {
		this.fechabaja = fechabaja;
	}

	public Date getFechamodificacion() {
		return this.fechamodificacion;
	}

	public void setFechamodificacion(Date fechamodificacion) {
		this.fechamodificacion = fechamodificacion;
	}

	public Date getFecharevocado() {
		return this.fecharevocado;
	}

	public void setFecharevocado(Date fecharevocado) {
		this.fecharevocado = fecharevocado;
	}

	public float getIntentos() {
		return this.intentos;
	}

	public void setIntentos(float intentos) {
		this.intentos = intentos;
	}

	public int getNoAcceso() {
		return this.noAcceso;
	}

	public void setNoAcceso(int noAcceso) {
		this.noAcceso = noAcceso;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPass() {
		return this.pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

}