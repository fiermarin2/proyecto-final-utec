package com.services.dto;

import java.io.Serializable;

import com.entities.Departamento;

@SuppressWarnings("serial")
public class CiudadDTO implements Serializable{
	
	private Long id;
	private String nombre;
	private DepartamentoDTO departamento;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
		//return this;
	}
	
	public DepartamentoDTO getDepartamento() {
		return departamento;
	}
	
	public void setDepartamento(DepartamentoDTO departamento) {
		this.departamento = departamento;
		//return this;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return nombre;
	}
}