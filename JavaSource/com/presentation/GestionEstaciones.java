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

import com.entities.Usuario;
import com.exceptions.ServiciosException;
import com.services.DepartamentosBean;
import com.services.EstacionesBean;
import com.services.UsuariosBean;
import com.services.dto.DepartamentoDTO;
import com.services.dto.EstacionDTO;
import com.services.dto.UsuarioDTO;

@Named(value="gestionEstaciones")
@SessionScoped
public class GestionEstaciones implements Serializable{
	private static final long serialVersionUID = 1L;
	@Inject
	private EstacionesBean beanf;
	@Inject
	private EstacionesBean estacionBean;
	@Inject
	private DepartamentosBean departamentoBean;
	@Inject
	private UsuariosBean userBean;
	private UsuarioDTO user;
	private List<EstacionDTO> listaEstaciones;
	private EstacionDTO estacion;
	private Long id;
	private String calidadAgua;
	private String humedadRelativa;
	private Double latitud;
	private Double longitud;
	private Long departamento;
	private Usuario usuario;
	private String nombre;
	private String modalidad;
	private boolean modoEdicion = false;
	
	@PostConstruct
	public void init() throws ServiciosException{
		listaEstaciones = listar();
		estacion = new EstacionDTO();
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
			id = null;
			FacesContext fc = FacesContext.getCurrentInstance();
			Map<String,String> params = 
	        fc.getExternalContext().getRequestParameterMap();
			modalidad =  params.get("modalidad");
			
			if(params.get("id") != null) {
				id =  Long.parseLong(params.get("id")); 
			}
			
			if(id!=null) {
				estacion = beanf.obtenerPorID(id);
				nombre = estacion.getNombre();
				calidadAgua = estacion.getCalidad_agua();
				latitud = estacion.getLatitud();
				longitud =  estacion.getLatitud();
				departamento = estacion.getDepartamento().getId();
			} else {
				estacion = new EstacionDTO();
				nombre = "";
				calidadAgua = "";
				latitud = null;
				longitud =  null;
				departamento = null;
			}
			if (modalidad != null && modalidad.contentEquals("update")) {
				modoEdicion = true;
			}else if (modalidad != null && modalidad.contentEquals("insert")) {
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
	public List<EstacionDTO> listar() throws ServiciosException {
		listaEstaciones = beanf.obtenerTodos();

		return listaEstaciones;
	}
	
	//agrega usuario
	public String insertEstacion() {
		try {		
			EstacionDTO estacionNuevo = new EstacionDTO();

			HttpSession ses = ( HttpSession ) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
			
			String userName = (String) ses.getAttribute("username");	
			Long idDepartamento = Long.parseLong(departamento.toString());
			
			estacionNuevo.setNombre(estacion.getNombre());
			estacionNuevo.setCalidad_agua(estacion.getCalidad_agua());
			estacionNuevo.setHumedad_relativa(estacion.getHumedad_relativa());
			estacionNuevo.setLatitud(estacion.getLatitud());
			estacionNuevo.setLongitud(estacion.getLongitud());
			estacionNuevo.setNombre_de_usuario(userName);
			estacionNuevo.setDepartamento(departamentoBean.buscar(idDepartamento));
			
			//this.modalidad="view";

			if(estacion.getId() == null) {
				this.modalidad = "insert";
				beanf.crearEstacion(estacionNuevo);
				
				//mensaje de actualizacion correcta
				FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se ha agregado un nueva estacion: " + estacionNuevo.getNombre(), "");
				FacesContext.getCurrentInstance().addMessage(null, facesMsg);
				
				return "menuEstaciones.xhtml?faces-redirect=true";
			} else {
				estacionNuevo.setId(estacion.getId());
				this.modalidad="update";
				
				FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se ha modificado la estación: " + estacionNuevo.getNombre(), "");
				FacesContext.getCurrentInstance().addMessage(null, facesMsg);
				
				beanf.mergeEstacion(estacionNuevo);
				
				return "menuEstaciones.xhtml?faces-redirect=true";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
			
	public String eliminarEstacion() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			Map<String,String> params = 
	        fc.getExternalContext().getRequestParameterMap();
			id = Long.parseLong(params.get("id")); 
			
			beanf.borrarEstacion(id.longValue());
			
			Flash flash = fc.getExternalContext().getFlash();
			flash.setKeepMessages(true);
			FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se ha eliminado la estación", "");
			FacesContext.getCurrentInstance().addMessage(null, facesMsg);
			
			listaEstaciones = beanf.obtenerTodos();

			return "menuEstaciones.xhtml?faces-redirect=true";
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
	

	public String getCalidadAgua() {
		return calidadAgua;
	}
	

	public void setCalidadAgua(String calidadAgua) {
		this.calidadAgua = calidadAgua;
	}
	

	public String getHumedadRelativa() {
		return humedadRelativa;
	}
	

	public void setHumedadRelativa(String humedadRelativa) {
		this.humedadRelativa = humedadRelativa;
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

	public EstacionDTO getEstacion() {
		return estacion;
	}

	public void setEstacion(EstacionDTO estacion) {
		this.estacion = estacion;
	}
}