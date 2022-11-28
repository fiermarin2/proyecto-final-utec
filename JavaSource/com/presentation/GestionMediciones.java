package com.presentation;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.entities.Casilla;
import com.exceptions.ServiciosException;
import com.services.CasillasBean;
import com.services.EstacionesBean;
import com.services.FormulariosBean;
import com.services.RegistrosBean;
import com.services.UsuariosBean;
import com.services.dto.CasillaDTO;
import com.services.dto.EstacionDTO;
import com.services.dto.FormularioDTO;
import com.services.dto.RegistroDTO;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.file.UploadedFile;


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
	
	private UploadedFile uploadedFile;

	private boolean modoEdicion = false;
	private Long userId;
	
	@PostConstruct
	public void init(){
		listaMediciones = listar();
		formulario = new FormularioDTO();

		casillas = new ArrayList<>();
	}
	
	public void preRenderView() {
		try {
			if(formulario.getCasillas().size()>0) {
				for(CasillaDTO c: formulario.getCasillas()) {
					c.setValor("");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<RegistroDTO> listar() {
		try {
			HttpSession ses = ( HttpSession ) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
			userId = (Long) ses.getAttribute("id");
		
			listaMediciones = registroBean.obtenerTodos(userBean.buscar(userId));
		} catch (ServiciosException e) {
			
			e.printStackTrace();
			return null;
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
				valores.put(c.getNombre(), c.getValor());
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
	
	//Importar(FormularioDTO formDTO,EstacionDTO estacion)
	public void importar(){
		
		//tomo el archivo desde el front.
		//uploadedFile = event.getFile();
		
		Map<String,String> campos = new HashMap<>();
		
		try {
			System.out.println("NOMBREEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE:" + uploadedFile.getFileName());
			InputStream inputstream = uploadedFile.getInputStream();
			XSSFWorkbook workbook = new XSSFWorkbook(inputstream);
			XSSFSheet sheet = workbook.getSheetAt(0); //esto elige la hoja 1 del excel.
			
			//PARA EL LOOP
			DataFormatter formatter = new DataFormatter();
			int rows = sheet.getLastRowNum();
			int cols = sheet.getRow(1).getLastCellNum();
			
			int input = 0;
			// 0=yes, 1=no, 2=cancel
			if(input == 0/* && estacion != null*/) {
			for(int r = 0; r<rows ; r++ ) {
				XSSFRow row = sheet.getRow(r+1);
				for(int c = 0; c < cols ; c++) {
					String nombreCasilla = formatter.formatCellValue(sheet.getRow(0).getCell(c));
					XSSFCell cell = row.getCell(c);
					String contenidoCelda = formatter.formatCellValue(cell);
					campos.put(nombreCasilla, contenidoCelda);
					//System.out.println(campos.get(nombreCasilla));
							
				}		
				//despues de cada fila hace el insert,
				
					//crearModificar(null, formDTO, estacion, campos);
				crearModificar(null, null, null, campos);
				
			  }
			}
			else
				System.out.println("Error");
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	//AGREGAR REGISTRO..
		private String crearModificar(Long id, FormularioDTO formulario, EstacionDTO estacion, Map<String,String> valor) {

			RegistroDTO dato = new RegistroDTO();
			Map<String, String> mapCasillas = null;
			for(CasillaDTO c: formulario.getCasillas()) {
				mapCasillas.put(c.getNombre(), null);
			}
			
			boolean valores = false;
			
			/*
			 * Se itera el mapa de casillas del formulario
			 */
//			for(String s: mapCasillas.keySet()) {
//				/*
//				 * COMPARACIÓN CON VALORES INGRESADOS
//				 */
//				if(mapCasillas.get(s).getObligatoria() == true) {
//					/*
//					 * RECORRIDO DE CASILLAS OBLIGATORIAS VACÍAS
//					 */
//					if(valor.get(s).isEmpty()) {
//						valores = false; //Por las dudas
//						return "Error";
//						
//					}else {
//						valores = true;
//						
//					}
//				}
//			}
			
			if (estacion == null) return "Debe seleccionar una ESTACIÓN para poder continuar";
			
			if(estacion != null && valores == true) {
				
					dato.setFormulario(formulario);
					dato.setEstacion(estacion);
					try {
						dato.setUsuario(userBean.buscar(userId));
					} catch (ServiciosException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					dato.setValor(valor);
					
					try {
						dato = (id == null) ? registroBean.crear(dato) : registroBean.merge(dato.setId(id));
					} catch (ServiciosException e) {
						return "Error al cargar el REGISTRO";	
					}
			}
			return null;

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

	public RegistroDTO getRegistro() {
		return registro;
	}

	public void setRegistro(RegistroDTO registro) {
		this.registro = registro;
	}
	
	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}
	
}