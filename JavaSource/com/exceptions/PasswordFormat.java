package com.exceptions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("com.exceptions.PasswordFormat")
public class PasswordFormat implements Validator{
	
//	private static final String PASS_PATTERN = "^(?=.*?[a-zA-Z])(?=.*?[0-9])[a-zA-Z0-9]{8,}$";
//			private Pattern pattern;
//			private Matcher matcher;
	
	public PasswordFormat() {
//		pattern = Pattern.compile(PASS_PATTERN);
	}
	
			
	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
//		matcher = pattern.matcher(value.toString());
	    if(value.toString().length()<8){      
	      FacesMessage msg = 
	        new FacesMessage("Formato inválido de contraseña, debe tener más de 8 caracteres en total", 
	        		"Formato inválido de contraseña, debe tener más de 8 caracteres en total");
	      msg.setSeverity(FacesMessage.SEVERITY_ERROR);
	      throw new ValidatorException(msg);
	    }
		
	}

}
