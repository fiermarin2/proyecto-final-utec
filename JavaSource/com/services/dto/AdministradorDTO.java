package com.services.dto;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.entities.TipoUsuario;

public class AdministradorDTO extends UsuarioDTO {	
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull
	private int documento;
	
	private String domicilio;
	private int telefono;
	private CiudadDTO ciudad;

	public AdministradorDTO() {
		setTipo(TipoUsuario.ADMINISTRADOR);
	
		Map<String,String> permisos = new LinkedHashMap<String,String>();
		permisos.put("Usuarios", 		"eCRUD");
		permisos.put("Formularios", 	"eCRUD");
		permisos.put("Casillas", 		"eCRUD");
		permisos.put("Estaciones", 		"eCRUD");
		permisos.put("Registros CA",   "ieCRUD");
		setPermisos(permisos);

	}

	public int getDocumento() {
		return documento;
	}

	public AdministradorDTO setDocumento(int documento) {
		this.documento = documento;
		return this;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public AdministradorDTO setDomicilio(String domicilio) {
		this.domicilio = domicilio;
		return this;
	}

	public int getTelefono() {
		return telefono;
	}

	public AdministradorDTO setTelefono(int telefono) {
		this.telefono = telefono;
		return this;
	}

	public CiudadDTO getCiudad() {
		return ciudad;
	}

	public AdministradorDTO setCiudad(CiudadDTO ciudad) {
		this.ciudad = ciudad;
		return this;
	}

}
