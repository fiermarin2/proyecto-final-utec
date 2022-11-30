package com.presentation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
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
import javax.servlet.http.Part;
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
	private Part file;
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
	public void importar() throws IOException{
		Map<String,String> campos = new HashMap<>();
		String archivito = getFilename(file);
		file.write(archivito);
		
		try {
			InputStream inputstream = new FileInputStream(new File("C:\\wildfly-20.0.1\\standalone\\tmp\\PIP.war\\"+archivito));
			XSSFWorkbook workbook = new XSSFWorkbook(inputstream);
			XSSFSheet sheet = workbook.getSheetAt(0); //esto elige la hoja 1 del excel.
			
			//PARA EL LOOP
			DataFormatter formatter = new DataFormatter();
			int rows = sheet.getLastRowNum();
			int cols = sheet.getRow(1).getLastCellNum();
			
			int input = 0;
			// 0=yes, 1=no, 2=cancel
			String nombreFormulario = "";
			String nombreEstacion = "";
			if(input == 0/* && estacion != null*/) {
				for(int r = 0; r < rows; r++ ) {
					XSSFRow row = sheet.getRow(r + 1);
					for(int c = 0; c < cols - 1 ; c++) {
						if(r == 0 && c == 0) {
							XSSFCell cell = row.getCell(c);
							nombreFormulario = formatter.formatCellValue(cell);
						}else if(r == 0 && c == 2) {
							XSSFCell cell = row.getCell(c);
							nombreEstacion= formatter.formatCellValue(cell);
						}else if(c > 2){
							//String nombreCasilla = formatter.formatCellValue(sheet.getRow(r+1).getCell(c));
							XSSFCell cell = row.getCell(c);
							String nombreCasilla = formatter.formatCellValue(cell);
							
							XSSFCell cell2 = row.getCell(c+1);
							String contenidoCelda = formatter.formatCellValue(cell2);
							
							campos.put(nombreCasilla, contenidoCelda);
						}
					}		
					//despues de cada fila hace el insert,
				}
				crearModificar(nombreFormulario, nombreEstacion, campos);
			}else
				System.out.println("Error");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	private static String getFilename(Part part) {
		for(String cd : part.getHeader("content-disposition").split(";")) {
			if(cd.trim().startsWith("filename")) {
				String filename = cd.substring(cd.indexOf("=") + 1).trim().replace("\"", "");
				return filename.substring(filename.lastIndexOf("/") + 1).substring(filename.lastIndexOf("\\") + 1);
			}
		}
		return null;
	}
	
	//AGREGAR REGISTRO..
		private String crearModificar(String formulario, String estacion, Map<String,String> valor) {
			HttpSession ses = ( HttpSession ) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
			Long userId = (Long) ses.getAttribute("id");
			
			RegistroDTO newRegistro = new RegistroDTO();
			
			Map<String, String> mapCasillas = new HashMap<>();
			
			if (estacion == null) return "Debe seleccionar una ESTACIÓN para poder continuar";
			
			if(estacion != null && formulario != null) {
				try {
					FormularioDTO formularioDTO = formularioBean.obtenerFormulario(formulario);
					
					newRegistro.setDepartamento(estacionBean.obtenerPorNombre(estacion).getDepartamento());
					newRegistro.setEstacion(estacionBean.obtenerPorNombre(estacion));
					newRegistro.setFormulario(formularioDTO);
					newRegistro.setUsuario(userBean.buscar(userId));
					
					Map<String,String> valores = new HashMap<>();
					
					/*for(CasillaDTO c: formularioDTO.getCasillas()) {
						valores.put(c.getNombre(), c.getValor());
					}
					
					for(CasillaDTO c: formularioDTO.getCasillas()) {
						mapCasillas.put(c.getNombre(), null);
					}*/
				
					newRegistro.setValor(valor);
					
					registroBean.crear(newRegistro);
					
					return "menuMediciones.xhtml?faces-redirect=true";
				}catch(Exception error) {
					System.out.println("ERROR");
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

	public Part getFile() {
		return file;
	}

	public void setFile(Part file) {
		this.file = file;
	}
	
}