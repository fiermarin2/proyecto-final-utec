package com.exceptions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("com.exceptions.OnlyRealNumbers")
public class OnlyRealNumbers implements Validator{
	
	// /^-?\d+(\.\d+)?$/
	private static final String REALNUMBERS = "/^-?\\d+(\\.\\d+)?$/";
	private Pattern pattern;
	private Matcher matcher;
	
	public OnlyRealNumbers() {
		pattern = Pattern.compile(REALNUMBERS);
	}
	
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {    
		    matcher = pattern.matcher(value.toString());
		    if(!matcher.matches()){      
		    	FacesMessage msg = new FacesMessage("Solo puede ingresar numeros racionales", "Solo puede ingresar numeros racionales");
		    	msg.setSeverity(FacesMessage.SEVERITY_ERROR);
		    	throw new ValidatorException(msg);
	   		}
	}
	

}
