
package com.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the USUARIOS database table.
 * 
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="USUARIOS")
@NamedQueries({
	@NamedQuery(name="Usuario.findAll", query="SELECT u FROM Usuario u WHERE u.borrado = 0 ORDER BY u.id ASC"),
	@NamedQuery(name="Usuario.findByID", query="SELECT u FROM Usuario u WHERE u.id = :id AND u.borrado = 0"),
	@NamedQuery(name="Usuario.findbyUserName", query="SELECT u FROM Usuario u WHERE u.nombreUsuario = :nombreUsuario AND u.borrado = :borrado"),
	@NamedQuery(name="Usuario.findByMail", query="SELECT u FROM Usuario u WHERE u.mail = :mail AND u.borrado = 0")
})
public abstract class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_USUARIO")
	private long id;

	@Column(length=50, nullable=false, unique = true)
	private String mail;

	@Column(nullable=false, length=50)
	private String nombre;

	@Column(nullable=false, length=50)
	private String apellido;

	//Este dato en el MER esta solo a nivel de administrador e investigador. En UML esta a nivel de usuario
	//	@Column(length=14)
	//	private String telefono;

	@Column(nullable = false, length = 50, unique = true)
	private String nombreUsuario;

	@Column(nullable=false, length=64)
	private char[] contrasena;

	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private TipoUsuario tipo;

	//Esto es porque se menciona borrado logico
	@Column(precision=1)
	private int borrado;

	public Usuario() {
	}

	public long getId() {
		return this.id;
	}

	public Usuario setId(long idUsuario) {
		this.id = idUsuario;
		return this;
	}

	public String getApellido() {
		return this.apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public char[] getContrasena() {
		return this.contrasena;
	}

	public void setContrasena(char[] contrasena) {
		this.contrasena = contrasena;
	}

	public String getMail() {
		return this.mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public TipoUsuario getTipo() {
		return tipo;
	}

	public void setTipo(TipoUsuario tipo) {
		this.tipo = tipo;
	}

	public int getBorrado() {
		return borrado;
	}

	public void setBorrado(int borrado) {
		this.borrado = borrado;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}
	
}