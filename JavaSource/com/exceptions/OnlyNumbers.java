package com.exceptions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("com.exceptions.OnlyNumbers")
public class OnlyNumbers implements Validator{
	
	private static final String CHARACTERS = "[0-9]+"; 
			private Pattern pattern;
			private Matcher matcher;
			
			public OnlyNumbers() {
				pattern = Pattern.compile(CHARACTERS);
			}
			
			public void validate(FacesContext context, UIComponent component,
				      Object value) throws ValidatorException {    
				
				    matcher = pattern.matcher(value.toString());
				    if(!matcher.matches()){      
				      FacesMessage msg = 
				        new FacesMessage("Solo puede ingresar caracteres numericos", 
				            "Solo puede ingresar caracteres numericos");
				      msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				      throw new ValidatorException(msg);
				    }
				  }
	
}
