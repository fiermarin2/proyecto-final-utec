package com.exceptions;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;


@FacesValidator("com.exceptions.CedulaFormat")
public class CedulaFormat implements Validator{

	public CedulaFormat() {
		
	}
	
	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		int multiplicador[] = { 2, 9, 8, 7, 6, 3, 4 };
		int indiceMultiplicador = multiplicador.length - 1;
		int suma = 0;

		for (int d = value.toString().length() - 2; d >= 0; d--) {

			int digito = Integer.parseInt(String.valueOf(value.toString().charAt(d)));

			digito = digito * multiplicador[indiceMultiplicador];
			indiceMultiplicador--;

			suma += digito;

		}
		int dControl = (10 - suma % 10) % 10; // se vuelve a calcular el módulo para los casos de cédulas terminadas en
												// cero

		if (dControl != Integer.parseInt(String.valueOf(value.toString().charAt(value.toString().length() - 1)))) {
			FacesMessage msg = 
				new FacesMessage("Cedula formato incorrecto", "Cedula formato incorrecto");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(msg);
		}
		
	}

	
	
}
