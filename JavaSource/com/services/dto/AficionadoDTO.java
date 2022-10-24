package com.services.dto;

import java.util.HashMap;

import com.entities.TipoUsuario;

public class AficionadoDTO extends UsuarioDTO
{	
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String ocupacion;

	public AficionadoDTO() {
		setTipo(TipoUsuario.AFICIONADO);
		
		HashMap<String,String> permisos = new HashMap<String, String>();
		permisos.put("Registros CA", 	"CRU");
		setPermisos(permisos);
	}

	public String getOcupacion() {
		return ocupacion;
	}

	public AficionadoDTO setOcupacion(String ocupacion) {
		this.ocupacion = ocupacion;
		return this;
	}

}
