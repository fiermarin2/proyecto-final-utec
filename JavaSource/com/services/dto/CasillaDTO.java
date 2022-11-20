package com.services.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.UniqueElements;

@SuppressWarnings("serial")
public class CasillaDTO implements Serializable{
	
	private Long id;
	
	@NotNull(message = "El campo nombre no puede ser nulo")
	private String nombre;
	
	@NotNull(message = "El campo tipo no puede ser nulo")
	private String tipo;
	
	@NotNull(message = "El campo ubicacion no puede ser nulo")
	private String ubicacion;
	
	private boolean obligatoria;
	
	@NotNull(message = "El campo unidad de medida no puede ser nulo")
	private String unidad_de_medida;
	
	private String usuario;
	private String descripcion;

	
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
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getUbicacion() {
		return ubicacion;
	}
	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}
	public String getUnidad_de_medida() {
		return unidad_de_medida;
	}
	public void setUnidad_de_medida(String unidad_de_medida) {
		this.unidad_de_medida = unidad_de_medida;
	}

	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public boolean getObligatoria() {
		return obligatoria;
	}
	public void setObligatoria(boolean obligatoria) {
		this.obligatoria = obligatoria;
	}
	@Override
	public String toString() {
		return tipo;
	}

}
