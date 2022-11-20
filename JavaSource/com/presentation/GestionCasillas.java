package com.presentation;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

import com.exceptions.ServiciosException;
import com.services.CasillasBean;
import com.services.UsuariosBean;
import com.services.dto.CasillaDTO;
import com.services.dto.UsuarioDTO;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.UniqueElements;

@Named(value="gestionCasillas")
@SessionScoped
public class GestionCasillas implements Serializable{	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private CasillasBean beanc;
	private CasillaDTO casilla;
	private List<CasillaDTO> listaCasillas;
	@Inject
	private UsuariosBean userBean;
	private UsuarioDTO user;
	private Long id;
	

	private String nombre;
	
	private String tipo;
	
	private String ubicacion;
	private String obligatoria;
	private String unidadMedida;
	private String descripcion;
	
	private String modalidad;
	
	
	@PostConstruct
	public void init(){
		listaCasillas = listar();
		casilla = new CasillaDTO();
		chequeoUsuario();
	}
	
	private void chequeoUsuario() {
		try {
			HttpSession ses = ( HttpSession ) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
			Long userId = (Long) ses.getAttribute("id");
			
			user = userBean.buscar(userId);
		
			if(user.getTipo().name() == "AFICIONADO") {
				ExternalContext ec = FacesContext.getCurrentInstance()
				        .getExternalContext();
				try {
					ec.redirect("menuPrincipal.xhtml");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (ServiciosException e) {
			e.printStackTrace();
		}
	}
	
	public void preRenderView() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			Map<String,String> params = 
	        fc.getExternalContext().getRequestParameterMap();
			modalidad =  params.get("modalidad");
			modalidad =  params.get("id");
			
			if(id!=null) { //si existe el id, traemos el objeto
				casilla = beanc.buscar(id);
				nombre = casilla.getNombre();
				tipo = casilla.getTipo();
				ubicacion = casilla.getUbicacion();
				if(obligatoria.equalsIgnoreCase("1"))
					obligatoria = "OBLIGATORIA";
				else
				obligatoria = "NO OBLIGATORIA";
				unidadMedida = casilla.getUnidad_de_medida();
				descripcion = casilla.getDescripcion();
				
			} else {
				casilla = new CasillaDTO();
				nombre = "";
				nombre = "";
				tipo = "";
				ubicacion = "";
				obligatoria = "";
				unidadMedida = "";
				descripcion = "";
			}
//			if (modalidad.contentEquals("update")) {
//			}else if (modalidad.contentEquals("insert")) {
//			}else {
//				modalidad="insert";
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//trae listado de usuarios..
	public List<CasillaDTO> listar() {
		try {
			listaCasillas = beanc.obtenerTodos();
			return listaCasillas;
		} catch (ServiciosException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}

	//agrega usuario
	public String insertCasilla() {
		try {		
			if(!beanc.findBooleanCasilla(casilla.getNombre())) {
				CasillaDTO casillaNueva = new CasillaDTO();

				HttpSession ses = ( HttpSession ) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
				nombre = casilla.getNombre();
				
				String userName = (String) ses.getAttribute("username");
					casillaNueva.setNombre(casilla.getNombre());
					casillaNueva.setDescripcion(casilla.getDescripcion());
					casillaNueva.setTipo(casilla.getTipo());
					casillaNueva.setUbicacion(casilla.getUbicacion());
					casillaNueva.setUnidad_de_medida(casilla.getUnidad_de_medida());
					casillaNueva.setObligatoria(casilla.getObligatoria());
					
					casillaNueva.setUsuario(userName);
					
					this.modalidad="view";

					beanc.crear(casillaNueva);
					this.modalidad = "insert";
					
					//mensaje de actualizacion correcta
					FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se ha agregado un nuevo casilla: " + casillaNueva.getNombre(), "");
					FacesContext.getCurrentInstance().addMessage(null, facesMsg);
						
					return "menuCasillas.xhtml?faces-redirect=true";
			}
			
			else {
				FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
				FacesContext.getCurrentInstance().validationFailed();
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Nombre de casilla duplicado"));
				
				return null;
				
				
//				String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
//				return viewId + "?faces-redirect=true";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String eliminarCasilla() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			Map<String,String> params = 
	        fc.getExternalContext().getRequestParameterMap();
			id = Long.parseLong(params.get("id")); 
			
			beanc.eliminar(id.longValue());
			
			Flash flash = fc.getExternalContext().getFlash();
			flash.setKeepMessages(true);
			FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se ha eliminado la casilla", "");
			FacesContext.getCurrentInstance().addMessage(null, facesMsg);
			
			listaCasillas = beanc.obtenerTodos();

			return "menuCasillas.xhtml?faces-redirect=true";
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public CasillaDTO getCasilla() {
		return casilla;
	}

	public void setCasilla(CasillaDTO casilla) {
		this.casilla = casilla;
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

	public String isObligatoria() {
		return obligatoria;
	}

	public void setObligatoria(String obligatoria) {
		this.obligatoria = obligatoria;
	}

	public String getUnidadMedida() {
		return unidadMedida;
	}

	public void setUnidadMedida(String unidadMedida) {
		this.unidadMedida = unidadMedida;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}	
}
