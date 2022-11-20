package com.presentation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.swing.JOptionPane;

import com.exceptions.ServiciosException;
import com.services.CasillasBean;
import com.services.FormulariosBean;
import com.services.UsuariosBean;

public class ControladorException {
	
	private static CasillasBean beanCasillas;
	@Inject
	private FormulariosBean beanForm;
	@Inject
	private UsuariosBean beanUsuarios;
	

	public boolean existeForm(String nombre, String nombreCampo) {

		if (beanForm.findBooleanName(nombre)) {
			JOptionPane.showMessageDialog(null, "Ya existe un formulario con mismo " + nombreCampo);
			return true;
		}
		return false;
	}

	public static boolean existeCasilla(String nombre, String nombreCampo) throws ServiciosException {
		System.out.println(beanCasillas.findBooleanCasilla(nombre));
		if (beanCasillas.findBooleanCasilla(nombre)) {
			System.out.println("ENTRO ACA");
			FacesContext context = FacesContext.getCurrentInstance();
	        FacesMessage message = new FacesMessage("Ya existe un registro con ese: " + nombreCampo);
	        message.setSeverity(FacesMessage.SEVERITY_ERROR);
	        context.addMessage("statusMessages", message);
			return true;
		}
		return false;
	}

	public boolean existeUserName(String nombreUsuario, String nombreCampo) /* throws ServiciosException */ {
		if (beanUsuarios.buscarUserName(nombreUsuario)) {
			JOptionPane.showMessageDialog(null, "Ya existe un Usuario con mismo " + nombreCampo);
			return true;
		}
		return false;
	}

	public boolean existeMail(String mail, String nombreCampo) /* throws ServiciosException */ {
		if (beanUsuarios.buscarMail(mail)) {
			JOptionPane.showMessageDialog(null, "Ya existe un Usuario con mismo " + nombreCampo);
			return true;
		}
		return false;
	}

	public boolean existeDocumento(int documento) {
		//return 0 no existe documento
		//return 1 existe documento y borrado = 0;
		//return 2 existe documento y borrado = 1;
		boolean option = beanUsuarios.buscarDocument(documento);
		if(option == true) {
			JOptionPane.showMessageDialog(null, "Existe documento");
			return true;
		}
		return false;
	}

	public boolean isEmpty(String valor, String nombreCampo) {
		if (valor==null || valor.equals("") || valor.equals("null")) {
			JOptionPane.showMessageDialog(null, "Error campo " + nombreCampo + " Vacío");
			return true;
		}
		return false;
	}

	public boolean comprobarEmail(String mail) {
		// String regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" +
		// "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.com";
		// String regex = "^([\\w-\\.]+){1,64}@([\\w&&[^_]]+){2,255}.[a-z]{2,}$";
		String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		// String regex = "^[A-Za-z0-9+_.-]+@estudiantes.utec.edu.uy";
		Pattern pattern = Pattern.compile(regex);

		Matcher matcher = pattern.matcher(mail);

		if (matcher.matches())
			return true;
		else
			JOptionPane.showMessageDialog(null, "Formato inválido de E-Mail, formato: xxx@xxx.com");
		return false;
	}

	public boolean comprobarContrasenia(String contrasenia) {
		//isEmpty(contrasenia, "Contrasña");
		// Solo letras y numeros. minimo 8 caracteres y al menos un digito y una letra
		String regex = "^(?=.*?[a-zA-Z])(?=.*?[0-9])[a-zA-Z0-9]{8,}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(contrasenia);

		if (matcher.matches())
			return true;
		else
			JOptionPane.showMessageDialog(null, "Formato inválido de contraseña, debe tener al menos un digito y una letra, y más de 8 caracteres en total");
		return false;

	}
	
	public boolean comprobarNombreUsuario(String usuario) {

		if (usuario.length() >= 8)
			return true;
		else
			JOptionPane.showMessageDialog(null, "El nombre de usuario debe contener al menos 8 caracteres");
		return false;

	}

	
	public boolean comprobarCedula(String documento) {
		int multiplicador[] = { 2, 9, 8, 7, 6, 3, 4 };
		int indiceMultiplicador = multiplicador.length - 1;
		int suma = 0;

		for (int d = documento.length() - 2; d >= 0; d--) {

			int digito = Integer.parseInt(String.valueOf(documento.charAt(d)));

			digito = digito * multiplicador[indiceMultiplicador];
			indiceMultiplicador--;

			suma += digito;

		}
		int dControl = (10 - suma % 10) % 10; // se vuelve a calcular el módulo para los casos de cédulas terminadas en
												// cero

		if (dControl != Integer.parseInt(String.valueOf(documento.charAt(documento.length() - 1)))) {
			JOptionPane.showMessageDialog(null, "El documento ingresado no coincide con el formato válido");
			return false;
		} else {
			return true;
		}
	}
}
