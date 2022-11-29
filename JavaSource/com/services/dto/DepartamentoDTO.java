package com.services.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class DepartamentoDTO {
	private Long id;
	
	@NotNull(message = "El nombre no puede ser nulo")
	@Length(min = 3 , max = 30, message = "El nombre debe tener entre 3 y 30 caracteres")
	private String nombre;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public /*DepartamentoDTO*/ void setNombre(String nombre) {
		this.nombre = nombre;
		//return this;
	}
		
	@Override
	public String toString() {
		return nombre;
	}
}