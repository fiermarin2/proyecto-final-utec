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
import javax.servlet.http.HttpSession;

import com.services.CasillasBean;
import com.services.FormulariosBean;
import com.services.UsuariosBean;
import com.services.dto.FormularioDTO;
import com.services.dto.CasillaDTO;

@Named(value="gestionFormularios")
@SessionScoped
public class GestionFormulario implements Serializable{
	private static final long serialVersionUID = 1L;
	@Inject
	private FormulariosBean beanf;
	@Inject
	private UsuariosBean userBean;
	@Inject
	private CasillasBean casillaBean;
	private List<FormularioDTO> listaFormularios;
	private FormularioDTO formulario;
	private Long id;
	private String nombre;
	private String comentario;
	private String modalidad;
	private boolean modoEdicion = false;
	
	@PostConstruct
	public void init(){
		listaFormularios = listar();
		formulario = new FormularioDTO();
	}
	
	public void preRenderView() {
		try {
			id = null;
			FacesContext fc = FacesContext.getCurrentInstance();
			Map<String,String> params = 
	        fc.getExternalContext().getRequestParameterMap();
			modalidad =  params.get("modalidad");
			if(params.get("id") != null) {
				id =  Long.parseLong(params.get("id")); 
			}
			if(id!=null) {
				formulario = beanf.buscar(id);
				nombre = formulario.getNombre();
				comentario = formulario.getComentario();
			} else {
				formulario = new FormularioDTO();
				nombre = "";
				comentario = "";
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
	
	//trae listado de formularios..
	public List<FormularioDTO> listar() {
		listaFormularios = beanf.obtenerTodos();

		return listaFormularios;
	}

	//trae listado de formularios y casillas..
	public List<FormularioDTO> listarCasillas() {
		listaFormularios = beanf.obtenerTodos();

		return listaFormularios;
	}
	
	//agrega usuario
	public String insertFormulario() {
		try {		
			FormularioDTO formularioNuevo = new FormularioDTO();

			HttpSession ses = ( HttpSession ) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
			Long userId = (Long) ses.getAttribute("id");
			
			formularioNuevo.setNombre(formulario.getNombre());
			formularioNuevo.setComentario(formulario.getComentario());
			formularioNuevo.setUsuario(userBean.buscar(userId));
			
			this.modalidad="view";

			if(formulario.getId() == null) {
				beanf.crearFormulario(formularioNuevo);
				this.modalidad = "insert";
				
				//mensaje de actualizacion correcta
				FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se ha agregado un nuevo Formulario: " + formularioNuevo.getNombre(), "");
				FacesContext.getCurrentInstance().addMessage(null, facesMsg);
				
				return "menuFormularios.xhtml?faces-redirect=true";
			} else {
				formularioNuevo.setId(formulario.getId());
				this.modalidad="update";
				
				FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se ha modificado el Formulario: " + formularioNuevo.getNombre(), "");
				FacesContext.getCurrentInstance().addMessage(null, facesMsg);
				
				beanf.mergeFormulario(formularioNuevo);
				return "menuFormularios.xhtml?faces-redirect=true";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
			
	public String eliminarFormulario() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			Map<String,String> params = 
	        fc.getExternalContext().getRequestParameterMap();
			id = Long.parseLong(params.get("id")); 
			
			beanf.borrarFormulario(id.longValue());
			
			Flash flash = fc.getExternalContext().getFlash();
			flash.setKeepMessages(true);
			FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se ha eliminado el Formulario", "");
			FacesContext.getCurrentInstance().addMessage(null, facesMsg);
			
			listaFormularios = beanf.obtenerTodos();

			return "menuFormularios.xhtml?faces-redirect=true";
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public String eliminarCasilla() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			Map<String,String> params = 
	        fc.getExternalContext().getRequestParameterMap();
			Long idFormulario = Long.parseLong(params.get("idFormulario"));
			Long idCasilla = Long.parseLong(params.get("idCasilla"));
			
			FormularioDTO formularioDTO = beanf.buscar(idFormulario);
			
			CasillaDTO casillaDTO = casillaBean.buscar(idCasilla);
			
			formularioDTO.getCasillas().remove(casillaDTO.getNombre());
			
			beanf.mergeFormulario(formularioDTO);

			Flash flash = fc.getExternalContext().getFlash();
			flash.setKeepMessages(true);
			FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se ha eliminado la casilla del formulario", "");
			FacesContext.getCurrentInstance().addMessage(null, facesMsg);
			
			listaFormularios = beanf.obtenerTodos();

			return "menuFormularioCasilla.xhtml?faces-redirect=true";
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
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

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public FormularioDTO getFormulario() {
		return formulario;
	}

	public void setFormulario(FormularioDTO formulario) {
		this.formulario = formulario;
	}

	public boolean isModoEdicion() {
		return modoEdicion;
	}

	public void setModoEdicion(boolean modoEdicion) {
		this.modoEdicion = modoEdicion;
	}
}