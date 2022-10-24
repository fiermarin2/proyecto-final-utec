package com.presentation;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.inject.Named;

import com.entities.TipoUsuario;
import com.exceptions.ServiciosException;
import com.services.DepartamentosBean;
import com.services.dto.AdministradorDTO;
import com.services.dto.AficionadoDTO;
import com.services.dto.DepartamentoDTO;
import com.services.dto.InvestigadorDTO;
import com.services.dto.UsuarioDTO;

@Named(value="gestionDepartamentos")
@SessionScoped
public class GestionDepartamentos implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DepartamentosBean beanc;
	
	private List<DepartamentoDTO> listaDepartamentos;
	
	private DepartamentoDTO departamento;
	
	private Long id;
	private String modalidad;
	private String nombre;
	
	private boolean modoEdicion=false;
	
	@PostConstruct
	public void init(){
		listaDepartamentos = listar();
		departamento = new DepartamentoDTO();
	}
	
	public void preRenderView() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			Map<String,String> params = 
	        fc.getExternalContext().getRequestParameterMap();
			modalidad =  params.get("modalidad"); 
			modalidad =  params.get("id"); 
			
			if(id!=null) {
				departamento = beanc.buscar(id);
				nombre = departamento.getNombre();
			} else {
				departamento = new DepartamentoDTO();
				nombre = "";
			}
			if (modalidad.contentEquals("update")) {
				modoEdicion = true;
			}else if (modalidad.contentEquals("insert")) {
				modoEdicion = false;
			}else {
				modoEdicion = false;
				modalidad="insert";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//trae listado de usuarios..
	public List<DepartamentoDTO> listar() {
		try {
			listaDepartamentos = beanc.obtenerTodos();
		} catch (ServiciosException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return listaDepartamentos;
	}

	//agrega usuario
	public String insertDepartamento() {
		try {		
			DepartamentoDTO departamentoNuevo = new DepartamentoDTO();

			departamentoNuevo.setNombre(departamento.getNombre());
			
			this.modalidad="view";
			
			if(departamento.getId() == null) {
				beanc.crearDepartamento(departamentoNuevo);
				this.modalidad = "insert";
				
				//mensaje de actualizacion correcta
				FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se ha agregado un nuevo Departamento: " + departamentoNuevo.getNombre(), "");
				FacesContext.getCurrentInstance().addMessage(null, facesMsg);
				
				return "menuDepartamentos.xhtml?faces-redirect=true";
			} else {
				departamentoNuevo.setId(departamento.getId());
				this.modalidad="update";
				
				FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se ha modificado el Departamento: " + departamentoNuevo.getNombre(), "");
				FacesContext.getCurrentInstance().addMessage(null, facesMsg);
				
				beanc.mergeDepartamento(departamentoNuevo);
				return "menuDepartamentos.xhtml?faces-redirect=true";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
			
	public String eliminarDepartamento() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			Map<String,String> params = 
	        fc.getExternalContext().getRequestParameterMap();
			id = Long.parseLong(params.get("id")); 
			
			beanc.borrarDepartamento(id.longValue());
			
			Flash flash = fc.getExternalContext().getFlash();
			flash.setKeepMessages(true);
			FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se ha eliminado el Departamento", "");
			FacesContext.getCurrentInstance().addMessage(null, facesMsg);
			
			listaDepartamentos = beanc.obtenerTodos();

			return "menuDepartamentos.xhtml?faces-redirect=true";
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public DepartamentoDTO getDepartamento() {
		return departamento;
	}

	public void setDepartamento(DepartamentoDTO departamento) {
		this.departamento = departamento;
	}

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
	
	public boolean isModoEdicion() {
		return modoEdicion;
	}

	public void setModoEdicion(boolean modoEdicion) {
		this.modoEdicion = modoEdicion;
	}	
}
