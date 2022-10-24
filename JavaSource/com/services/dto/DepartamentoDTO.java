package com.services.dto;

public class DepartamentoDTO {
	private Long id;
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