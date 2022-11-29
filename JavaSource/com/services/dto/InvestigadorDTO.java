package com.services.dto;

import java.util.HashMap;

import javax.validation.constraints.NotNull;

import com.entities.TipoUsuario;

public class InvestigadorDTO extends UsuarioDTO
{	
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	@NotNull
	private int documento;
	
	private String domicilio;
	private int telefono;
	private CiudadDTO ciudad;

	public InvestigadorDTO() {
		setTipo(TipoUsuario.INVESTIGADOR);
		
		HashMap<String,String> permisos = new HashMap<String,String>();
		permisos.put("Formularios", 	"eCRUD");
		permisos.put("Casillas", 		"eCRD");
		permisos.put("Estaciones", 		"eCRUD");
		permisos.put("Registros CA", 	"ieCRUD");
		setPermisos(permisos);
	}

	public int getDocumento() {
		return documento;
	}

	public InvestigadorDTO setDocumento(int documento) {
		this.documento = documento;
		return this;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public InvestigadorDTO setDomicilio(String domicilio) {
		this.domicilio = domicilio;
		return this;
	}

	public int getTelefono() {
		return telefono;
	}

	public InvestigadorDTO setTelefono(int telefono) {
		this.telefono = telefono;
		return this;
	}

	public CiudadDTO getCiudad() {
		return ciudad;
	}

	public InvestigadorDTO setCiudad(CiudadDTO ciudad) {
		this.ciudad = ciudad;
		return this;
	}

}
