package com.services.dto;

import java.io.Serializable;
import java.util.Map;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import com.entities.TipoUsuario;

import org.hibernate.validator.constraints.Length;

public class UsuarioDTO  implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Long id;

	@NotNull
	@Length(min=3, max=50 ,message = "El nombre debe tener entre 3 y 50 caracteres")
	private String nombre;
	
	@NotNull
	@Length(min=3, max=50 ,message = "El apellido debe tener entre 3 y 50 caracteres")	
	private String apellido;
	
	@NotNull
	@Email(message = "Dirección de e-mail no es valida")	
	private String mail;
	
	@NotNull
	@Length(min=3, max=50 ,message = "El nombre de usuario debe tener entre 3 y 50 caracteres")	
	private String usuario;
	
	private char[] contrasena;
	
	private TipoUsuario tipo;
	private Map<String,String> permisos;

	public UsuarioDTO() {
		
	}

	public Long getId() {
		return id;
	}

	public UsuarioDTO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String nombreUsuario) {
		this.usuario = nombreUsuario;
	}

	public char[] getContrasena(){
		return contrasena;
	}

	public void setContrasena (char[] contrasena) {
		this.contrasena = contrasena;
	}
	
	public TipoUsuario getTipo() {
		return tipo;
	}

	public void setTipo(TipoUsuario tipo) {
		this.tipo = tipo;
	}
	
	public String getIniciales() {
		return String.valueOf(Character.toUpperCase(getNombre().toCharArray()[0]))
		     + String.valueOf(Character.toUpperCase(getApellido().toCharArray()[0]));
	}
	
	protected void setPermisos(Map<String, String> permisos) {
		this.permisos = permisos;
	}
	
	public Map<String, String> getPermisos(){
		return permisos;
	}

	@Override
	public String toString() {
		return usuario;
	}
	
}
