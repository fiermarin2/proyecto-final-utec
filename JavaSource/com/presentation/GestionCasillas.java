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

import com.exceptions.ServiciosException;
import com.services.CasillasBean;
import com.services.dto.CasillaDTO;

@Named(value="gestionCasillas")
@SessionScoped
public class GestionCasillas implements Serializable{
	
	
private static final long serialVersionUID = 1L;
	
	@Inject
	private CasillasBean beanc;
	
	private CasillaDTO casilla;
	private List<CasillaDTO> listaCasillas;
	
	private Long id;
	private String nombre;
	private String tipo;
	private String ubicacion;
	private String obligatoria;
	private String unidadMedida;
	private String descripcion;
	//completa el casilla un usuario asignado a creacion de casilla.
	
	private String modalidad;
	
	@PostConstruct
	public void init(){
		listaCasillas = listar();
		casilla = new CasillaDTO();
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
				
			} else { //sino vaciamos los campos para crear una casilla nueva.
				//casilla = new CasillaDTO();
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
			CasillaDTO casillaNueva = new CasillaDTO();

			HttpSession ses = ( HttpSession ) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
			//Long userId = (Long) ses.getAttribute("id");
			
			String userName = (String) ses.getAttribute("username");

			casillaNueva.setNombre(casilla.getNombre());
			casillaNueva.setDescripcion(casilla.getDescripcion());
			casillaNueva.setTipo(casilla.getUbicacion());
			casillaNueva.setUbicacion(casilla.getUbicacion());
			casillaNueva.setUnidad_de_medida(casilla.getUnidad_de_medida());
			
			if(obligatoria.equalsIgnoreCase("1"))
				casillaNueva.setObligatoria(true);
			else
				casillaNueva.setObligatoria(false);
			
			casillaNueva.setUsuario(userName);
			
			this.modalidad="view";

			beanc.crear(casillaNueva);
			this.modalidad = "insert";
			
			//mensaje de actualizacion correcta
			FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se ha agregado un nuevo casilla: " + casillaNueva.getNombre(), "");
			FacesContext.getCurrentInstance().addMessage(null, facesMsg);
				
			return "menuCasillas.xhtml?faces-redirect=true";
			
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
			
			System.out.println(id);
			
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
	
//	public void onCasillaChange() {
//
//        if(tipo !=null && ubicacion != null && obligatoria != null
//        && !tipo.equals("") && !ubicacion.equals("") && !obligatoria.equals(""))
//        {
//            listOfSubDepartment = staffSession.getAllSubDepartments(department);
//        }
//        else
//        {
//            listOfSubDepartment = new ArrayList<String>();
//        }
//    }
	
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
