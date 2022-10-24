package com.services.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class EstacionDTO implements Serializable {
	
	private Long id;
	private String nombre;
	private String calidad_agua;
	private String humedad_relativa;
	private DepartamentoDTO departamento;
	private Double latitud;
	private Double longitud;
	//esto seria javier.dominguez.
	private String nombre_de_usuario;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCalidad_agua() {
		return calidad_agua;
	}
	public void setCalidad_agua(String calidadAgua) {
		this.calidad_agua = calidadAgua;
	}
	public String getHumedad_relativa() {
		return humedad_relativa;
	}
	public void setHumedad_relativa(String humedadRelativa) {
		this.humedad_relativa = humedadRelativa;
	}

	public Double getLatitud() {
		return latitud;
	}
	public void setLatitud(Double latitud) {
		this.latitud = latitud;
	}
	public Double getLongitud() {
		return longitud;
	}
	public void setLongitud(Double longitud) {
		this.longitud = longitud;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombreEstacion) {
		this.nombre = nombreEstacion;
	}
	public DepartamentoDTO getDepartamento() {
		return departamento;
	}
	public void setDepartamento(DepartamentoDTO departamento) {
		this.departamento = departamento;
	}
	public String getNombre_de_usuario() {
		return nombre_de_usuario;
	}
	public void setNombre_de_usuario(String nombreUsuario) {
		this.nombre_de_usuario = nombreUsuario;
	}
	
	@Override
	public String toString() {
		return nombre;
	}
	
	
	
}
