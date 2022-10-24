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

import com.entities.Departamento;
import com.exceptions.ServiciosException;
import com.services.CiudadesBean;
import com.services.DepartamentosBean;
import com.services.dto.CiudadDTO;
import com.services.dto.DepartamentoDTO;

@Named(value="gestionCiudades")
@SessionScoped
public class GestionCiudades implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Inject
	private CiudadesBean beanc;
	
	@Inject
	private DepartamentosBean beand;
	
	private List<CiudadDTO> listaCiudades;
	
	private CiudadDTO ciudad;
	
	private Long id;
	private String modalidad;
	private String nombre;
	private Long departamento;
	
	private boolean modoEdicion=false;
	
	@PostConstruct
	public void init(){
		listaCiudades = listar();
		ciudad = new CiudadDTO();
	}
	
	public void preRenderView() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			Map<String,String> params = 
	        fc.getExternalContext().getRequestParameterMap();
			modalidad =  params.get("modalidad"); 
			modalidad =  params.get("id");
			
			if(id!=null) {
				ciudad = beanc.buscar(id);
				nombre = ciudad.getNombre();
			} else {
				ciudad = new CiudadDTO();
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
	public List<CiudadDTO> listar() {
		try {
			listaCiudades = beanc.obtenerTodos();
		} catch (ServiciosException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return listaCiudades;
	}

	//agrega ciudad
	public String insertCiudad() {
		try {		
			CiudadDTO ciudadNuevo = new CiudadDTO();

			ciudadNuevo.setNombre(ciudad.getNombre());
			DepartamentoDTO dep = new DepartamentoDTO();
			
			dep = beand.buscar(departamento);
			
			ciudadNuevo.setDepartamento(dep);
			this.modalidad="view";
			
			if(ciudad.getId() == null) {
				beanc.crearCiudad(ciudadNuevo);
				this.modalidad = "insert";
				
				//mensaje de actualizacion correcta
				FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se ha agregado una nueva Ciudad: " + ciudadNuevo.getNombre(), "");
				FacesContext.getCurrentInstance().addMessage(null, facesMsg);
				
				return "menuCiudades.xhtml?faces-redirect=true";
			} else {
				ciudadNuevo.setId(ciudad.getId());
				this.modalidad="update";
				
				FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se ha modificado la Ciudad: " + ciudadNuevo.getNombre(), "");
				FacesContext.getCurrentInstance().addMessage(null, facesMsg);
				
				beanc.mergeCiudad(ciudadNuevo);
				return "menuCiudades.xhtml?faces-redirect=true";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
			
	public String eliminarCiudad() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			Map<String,String> params = 
	        fc.getExternalContext().getRequestParameterMap();
			id = Long.parseLong(params.get("id")); 
			
			beanc.borrarCiudad(id.longValue());
			
			Flash flash = fc.getExternalContext().getFlash();
			flash.setKeepMessages(true);
			FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se ha eliminado la ciudad", "");
			FacesContext.getCurrentInstance().addMessage(null, facesMsg);
			
			listaCiudades = beanc.obtenerTodos();

			return "menuCiudades.xhtml?faces-redirect=true";
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public CiudadDTO getCiudad() {
		return ciudad;
	}

	public void setCiudad(CiudadDTO ciudad) {
		this.ciudad = ciudad;
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
	
	public Long getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Long departamento) {
		this.departamento = departamento;
	}

	public boolean isModoEdicion() {
		return modoEdicion;
	}

	public void setModoEdicion(boolean modoEdicion) {
		this.modoEdicion = modoEdicion;
	}

	public String getModalidad() {
		return modalidad;
	}

	public void setModalidad(String modalidad) {
		this.modalidad = modalidad;
	}
}
