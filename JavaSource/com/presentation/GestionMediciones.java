package com.presentation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import com.entities.Casilla;
import com.exceptions.ServiciosException;
import com.services.CasillasBean;
import com.services.EstacionesBean;
import com.services.FormulariosBean;
import com.services.RegistrosBean;
import com.services.UsuariosBean;
import com.services.dto.CasillaDTO;
import com.services.dto.FormularioDTO;
import com.services.dto.RegistroDTO;

import org.primefaces.event.SelectEvent;

@Named(value="gestionMediciones")
@SessionScoped
public class GestionMediciones implements Serializable{
	private static final long serialVersionUID = 1L;

	@Inject
	private UsuariosBean userBean;
	@Inject
	private FormulariosBean formularioBean;
	@Inject
	private RegistrosBean registroBean;
	@Inject
	private EstacionesBean estacionBean;
	private RegistroDTO registro;
	private List<RegistroDTO> listaMediciones;
	private FormularioDTO formulario;
	private String form;
	private Long estacionID;
	private Long id;
	private String modalidad;
	private ArrayList<CasillaDTO> casillas;
	private boolean modoEdicion = false;
	
	@PostConstruct
	public void init(){
		listaMediciones = listar();
		formulario = new FormularioDTO();
		casillas = new ArrayList<>();
	}
	
	public List<RegistroDTO> listar() {
		try {
			HttpSession ses = ( HttpSession ) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
			Long userId = (Long) ses.getAttribute("id");
		
			listaMediciones = registroBean.obtenerTodos(userBean.buscar(userId));
		} catch (ServiciosException e) {
			e.printStackTrace();
		}

		return listaMediciones;
	}

	public void onFormularioChange() {
		try {
	        if (formulario != null && !"".equals(formulario)) {
	        	formulario = formularioBean.obtenerFormulario(form);
	        	casillas = formulario.getCasillas();
	        } else {
	        	casillas = new ArrayList<>();
	        }
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    }
	
	public String insertFormulario() {
		try {
			RegistroDTO newRegistro = new RegistroDTO();
			HttpSession ses = ( HttpSession ) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
			Long userId = (Long) ses.getAttribute("id");
			
			newRegistro.setDepartamento(estacionBean.obtenerPorID(estacionID).getDepartamento());
			newRegistro.setEstacion(estacionBean.obtenerPorID(estacionID));
			newRegistro.setFormulario(formulario);
			newRegistro.setUsuario(userBean.buscar(userId));
			
			Map<String,String> valores = new HashMap<>();
			
			for(CasillaDTO c: casillas) {
				valores.put(c.getNombre(), c.getUsuario());
			}
			
			newRegistro.setValor(valores);
			
			registroBean.crear(newRegistro);

			return "menuMediciones.xhtml?faces-redirect=true";
		}catch(Exception e) {
			e.getLocalizedMessage();
			return null;
		}
	}
	
	public String eliminarMedicion() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			Map<String,String> params = 
	        fc.getExternalContext().getRequestParameterMap();
			id = Long.parseLong(params.get("id")); 
			
			registroBean.borrarMedicion(id.longValue());
			
			Flash flash = fc.getExternalContext().getFlash();
			flash.setKeepMessages(true);
			FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se ha eliminado la medicion", "");
			FacesContext.getCurrentInstance().addMessage(null, facesMsg);

			return "menuMediciones.xhtml?faces-redirect=true";
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public FormularioDTO getFormulario() {
		return formulario;
	}

	public void setFormulario(FormularioDTO formulario) {
		this.formulario = formulario;
	}

	public ArrayList<CasillaDTO> getCasillas() {
		return casillas;
	}

	public void setCasillas(ArrayList<CasillaDTO> casillas) {
		this.casillas = casillas;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEstacionID() {
		return estacionID;
	}
	
	public void setEstacionID(Long estacionID) {
		this.estacionID = estacionID;
	}

	public List<RegistroDTO> getListaMediciones() {
		return listaMediciones;
	}

	public void setListaMediciones(List<RegistroDTO> listaMediciones) {
		this.listaMediciones = listaMediciones;
	}
}