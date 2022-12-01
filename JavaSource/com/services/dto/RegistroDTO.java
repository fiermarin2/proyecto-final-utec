package com.services.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@SuppressWarnings("serial")
public class RegistroDTO implements Serializable{
	
	private Long id;	
	private Map<String, String> valor;     //esto.
	private Date fecha_Hora;
	private EstacionDTO estacion; //despues lo pedimos 
	private FormularioDTO formulario;  // 1
	@SuppressWarnings("unused")
	private DepartamentoDTO departamento;      //1
	private UsuarioDTO usuario;
	private String fecha;
	
	public Long getId() {
		return id;
	}

	public RegistroDTO setId(Long id) {
		this.id = id;
		return this;
	}

	public Date getFecha_Hora() {
		return fecha_Hora;
	}

	public void setFecha_Hora(Date fecha_Hora) {
		this.fecha_Hora = fecha_Hora;
	}

	public FormularioDTO getFormulario() {
		return formulario;
	}

	public void setFormulario(FormularioDTO formulario) {
		this.formulario = formulario;
	}
/*
	public CasillaDTO getCasilla() {
		return casilla;
	}

	public void setCasilla(CasillaDTO casilla) {
		this.casilla = casilla;
	}
*/
	public Map<String, String> getValor() {
		return valor;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public void setValor(Map<String, String> valor) {
		this.valor = valor;
	}

	public EstacionDTO getEstacion() {
		return estacion;
	}

	public void setEstacion(EstacionDTO estacion) {
		this.estacion = estacion;
	}

	public UsuarioDTO getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioDTO usuario) {
		this.usuario = usuario;
	}

	public DepartamentoDTO getDepartamento() {
		return estacion.getDepartamento();
	}

	public void setDepartamento(DepartamentoDTO departamento) {
		this.departamento = departamento;
	}	
	
}

