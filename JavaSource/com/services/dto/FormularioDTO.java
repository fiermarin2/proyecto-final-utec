package com.services.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@SuppressWarnings("serial")
public class FormularioDTO implements Serializable {
	
	private Long id;
	@NotNull
	@Length(min=3, max=30 ,message = "El nombre debe tener entre 3 y 30 caracteres")
	private String nombre;
	private Date fecha_hora;
	private String comentario;
	private UsuarioDTO usuario;
	private ArrayList<CasillaDTO> casillas;
	//private Map<String, CasillaDTO> casillas = new LinkedHashMap<>();
	
	public Long getId() {
		return id;
	}
	public ArrayList<CasillaDTO> getCasillas() {
		return casillas;
	}
	public void setCasillas(ArrayList<CasillaDTO> casillas) {
		this.casillas = casillas;
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
	public Date getFecha_hora() {
		return fecha_hora;
	}
	public void setFecha_hora(Date fecha_hora) {
		this.fecha_hora = fecha_hora;
	}
	/*
	public String getMetodo_medicion() {
		return metodo_medicion;
	}
	public void setMetodo_medicion(String metodo_medicion) {
		this.metodo_medicion = metodo_medicion;
	}
	*/
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	public UsuarioDTO getUsuario() {
		return usuario;
	}
	public void setUsuario(UsuarioDTO usuario) {
		this.usuario = usuario;
	}
	/*public Map<String, CasillaDTO> getCasillas() {
		return casillas;
	}
	public void setCasillas(Map<String, CasillaDTO> casillas) {
		this.casillas = casillas;
	}*/
	@Override
	public String toString() {
		return nombre;
	}
	
	
	
}
