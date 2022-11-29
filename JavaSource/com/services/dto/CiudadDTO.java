package com.services.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.entities.Departamento;

import org.hibernate.validator.constraints.Length;

@SuppressWarnings("serial")
public class CiudadDTO implements Serializable{
	
	private Long id;
	
	@NotNull
	@Length(min=3, max=30 ,message = "El nombre debe tener entre 3 y 30 caracteres")
	private String nombre;
	
	@NotNull
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