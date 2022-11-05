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

import com.entities.TipoUsuario;
import com.exceptions.ServiciosException;
import com.services.CiudadesBean;
import com.services.UsuariosBean;
import com.services.dto.AdministradorDTO;
import com.services.dto.AficionadoDTO;
import com.services.dto.CiudadDTO;
import com.services.dto.InvestigadorDTO;
import com.services.dto.UsuarioDTO;


@Named(value="gestionUsuarios")
@SessionScoped
public class GestionUsuarios implements Serializable{
	private static final long serialVersionUID = 1L;
	@Inject
	private UsuariosBean beanu;
	@Inject
	private CiudadesBean beanc;
	private List<UsuarioDTO> listaUsuarios;
	private UsuarioDTO usuario;
	private String contrasena;
	private Long id;
	private UsuarioDTO user;
	private String modalidad;
	private String tipoUsuario;
	private boolean showOrHide;
	private String documento;
	private String domicilio;
	private String telefono;
	private String departamento;
	private String ciudad;
	private String ocupacion;
	
	private boolean modoEdicion=false;
	
	public boolean showOrHide() {
		try {
			if(tipoUsuario.equalsIgnoreCase("AFICIONADO")) {
				return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	@PostConstruct
	public void init(){
		listaUsuarios = listar();
		usuario = new UsuarioDTO();
		chequeoUsuario();
	}
	
	private void chequeoUsuario() {
		try {
			HttpSession ses = ( HttpSession ) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
			Long userId = (Long) ses.getAttribute("id");
			
			user = beanu.buscar(userId);
		
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
				usuario = beanu.buscar(id);
				if(usuario.getTipo() == TipoUsuario.ADMINISTRADOR) {
					documento = String.valueOf(((AdministradorDTO) usuario).getDocumento());
					domicilio = String.valueOf(((AdministradorDTO) usuario).getDomicilio());					
					telefono = String.valueOf(((AdministradorDTO) usuario).getTelefono());
				}else if (usuario.getTipo() == TipoUsuario.INVESTIGADOR) {
					documento = String.valueOf(((InvestigadorDTO) usuario).getDocumento());
					domicilio = String.valueOf(((InvestigadorDTO) usuario).getDomicilio());					
					telefono = String.valueOf(((InvestigadorDTO) usuario).getTelefono());
				}else {
					ocupacion = String.valueOf(((AficionadoDTO) usuario).getOcupacion());
				}
			} else {
				usuario = new UsuarioDTO();
				documento = "";
				domicilio = "";
				telefono = "";
				ciudad = "";
				departamento = "";
				ocupacion = "";
				contrasena = ""; 
				tipoUsuario = "";
			}
			if (modalidad.contentEquals("update")) {
				modoEdicion = false;
			}else if (modalidad.contentEquals("insert")) {
				modoEdicion = false;
			}else {
				modoEdicion = true;
				modalidad="insert";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public GestionUsuarios() {
		super();
	}	
	
	//agrega usuario
	public String insertUsuario() {
		try {				
			
			UsuarioDTO usuarioNuevo = new UsuarioDTO();

			if(this.tipoUsuario.equals("ADMINISTRADOR")) {
				usuarioNuevo = new AdministradorDTO();
				
				usuarioNuevo.setApellido(usuario.getApellido());
				usuarioNuevo.setContrasena(contrasena.toCharArray());
				usuarioNuevo.setMail(usuario.getMail());
				usuarioNuevo.setNombre(usuario.getNombre());
				//falta tipo usuario.setTipo(TipoUsuario.AFICIONADO);
				usuarioNuevo.setUsuario(usuario.getUsuario());
				
				
				usuarioNuevo.setTipo(TipoUsuario.ADMINISTRADOR);
				((AdministradorDTO) usuarioNuevo).setDocumento(Integer.parseInt(documento));
				((AdministradorDTO) usuarioNuevo).setDomicilio(domicilio);
				((AdministradorDTO) usuarioNuevo).setTelefono(Integer.parseInt(telefono));
				((AdministradorDTO) usuarioNuevo).setCiudad(beanc.obtenerDTO(1L));
			}else if(this.tipoUsuario.equals("INVESTIGADOR")) {
				usuarioNuevo = new InvestigadorDTO();
				
				usuarioNuevo.setApellido(usuario.getApellido());
				usuarioNuevo.setContrasena(contrasena.toCharArray());
				usuarioNuevo.setMail(usuario.getMail());
				usuarioNuevo.setNombre(usuario.getNombre());
				usuarioNuevo.setUsuario(usuario.getUsuario());
				
				usuarioNuevo.setTipo(TipoUsuario.INVESTIGADOR);
				((InvestigadorDTO) usuarioNuevo).setDocumento(Integer.parseInt(documento));
				((InvestigadorDTO) usuarioNuevo).setDomicilio(domicilio);
				((InvestigadorDTO) usuarioNuevo).setTelefono(Integer.parseInt(telefono));
				((InvestigadorDTO) usuarioNuevo).setCiudad(beanc.obtenerDTO(1L));
			}else {
				usuarioNuevo = new AficionadoDTO();
				
				usuarioNuevo.setApellido(usuario.getApellido());
				usuarioNuevo.setContrasena(contrasena.toCharArray());
				usuarioNuevo.setMail(usuario.getMail());
				usuarioNuevo.setNombre(usuario.getNombre());
				usuarioNuevo.setUsuario(usuario.getUsuario());
				
				
				usuarioNuevo.setTipo(TipoUsuario.AFICIONADO);
				((AficionadoDTO) usuarioNuevo).setOcupacion(ocupacion);
				
				this.modalidad="view";
			}
			
			if(usuario.getId() == null) {
				beanu.crear(usuarioNuevo);
				this.modalidad = "insert";
				
				//mensaje de actualizacion correcta
				FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se ha agregado un nuevo Usuario de tipo " + usuarioNuevo.getTipo().toString(), "");
				FacesContext.getCurrentInstance().addMessage(null, facesMsg);
				
				return "menuPrincipal";
			} else {
				usuarioNuevo.setId(usuario.getId());
				this.modalidad="update";
				
				FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se ha modificado el Usuario " + usuarioNuevo.getUsuario(), "");
				FacesContext.getCurrentInstance().addMessage(null, facesMsg);
				
				beanu.modificar(usuarioNuevo);
				return "menuPrincipal";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
		
	public String eliminarUsuario() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			Map<String,String> params = 
	        fc.getExternalContext().getRequestParameterMap();
			id = Long.parseLong(params.get("id")); 
			
			beanu.eliminar(id.longValue());
			
			Flash flash = fc.getExternalContext().getFlash();
			flash.setKeepMessages(true);
			FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se ha eliminado el Usuario", "");
			FacesContext.getCurrentInstance().addMessage(null, facesMsg);
			
			listaUsuarios = beanu.encontrarTodos();

			//return "menuPrincipal.xhtml";
			return "menuPrincipal.xhtml?faces-redirect=true";
			//FacesContext.getCurrentInstance().getExternalContext().redirect("/Grupo10_Semana4/views/menuPrincipal.xhtml");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
		
	public static boolean chequearTelefono(String telefono) {
	    return telefono.matches("[0-9]+");
	}		

	//trae listado de usuarios..
	public List<UsuarioDTO> listar() {
		try {
			listaUsuarios = beanu.encontrarTodos();
		} catch (ServiciosException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return listaUsuarios;

	}
	
	public List<CiudadDTO> filtrarCiudades() throws ServiciosException{
		
		if(departamento != null)
		return beanc.obtenerTodosDepto(departamento);
		
		return null;
	}
	
	public UsuarioDTO getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioDTO usuario) {
		this.usuario = usuario;
	}

	public List<UsuarioDTO> getListaUsuarios() {
		return listaUsuarios;
	}

	public void setListaUsuarios(List<UsuarioDTO> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(String tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}
	
	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}
	
	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getOcupacion() {
		return ocupacion;
	}

	public void setOcupacion(String ocupacion) {
		this.ocupacion = ocupacion;
	}

	public String getModalidad() {
		return modalidad;
		
	}

	public void setModalidad(String modalidad) {
		this.modalidad = modalidad;
		
	}
	
	public boolean isShowOrHide() {
		return showOrHide;
		
	}

	public void setShowOrHide(boolean showOrHide) {
		this.showOrHide = showOrHide;
		
	}

	public CiudadesBean getBeanc() {
		return beanc;
	}

	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	
	
	public boolean isModoEdicion() {
		return modoEdicion;
	}

	public void setModoEdicion(boolean modoEdicion) {
		this.modoEdicion = modoEdicion;
	}	
}