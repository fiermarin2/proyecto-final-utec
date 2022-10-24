package com.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;


/**
 * The persistent class for the ADMINISTRADORES database table.
 * 
 */
@Entity
@Table(name="ADMINISTRADORES")
@NamedQuery(name="Administrador.findDocument", query="SELECT a FROM Administrador a WHERE a.documento = :documento AND a.borrado = :borrado")
@PrimaryKeyJoinColumn(referencedColumnName = "ID_USUARIO" , name = "ID_ADMINISTRADOR")
public class Administrador extends Usuario {
	private static final long serialVersionUID = 1L;

	@Column(precision=8, unique = true)
	private int documento;

	@Column(length=50)
	private String domicilio;
	
	@Column(precision = 14)
	private int telefono;

	//bi-directional many-to-one association to Ciudad
	@ManyToOne
	@JoinColumn(name="ID_CIUDAD", nullable = true)
	private Ciudad ciudad;

	public Administrador() {
	}

	public int getDocumento() {
		return this.documento;
	}

	public void setDocumento(int documento) {
		this.documento = documento;
	}

	public String getDomicilio() {
		return this.domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public int getTelefono() {
		return this.telefono;
	}

	public void setTelefono(int telefono) {
		this.telefono = telefono;
	}

	public Ciudad getCiudade() {
		return this.ciudad;
	}

	public void setCiudade(Ciudad ciudad) {
		this.ciudad = ciudad;
	}

}