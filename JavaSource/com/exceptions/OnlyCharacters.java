package com.exceptions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("com.exceptions.OnlyCharacters")
public class OnlyCharacters implements Validator{
	
	//private static final String CHARACTERS = "[1-9]/[a-zA-Z](-[1-9]){3,4}";  [a-zA-Z]+
	private static final String CHARACTERS = "[a-zA-Z ]+";
			private Pattern pattern;
			private Matcher matcher;
			
			public OnlyCharacters() {
				pattern = Pattern.compile(CHARACTERS);
			}
			
			public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {    
				
				    matcher = pattern.matcher(value.toString());
				    if(!matcher.matches()){      
				    	FacesMessage msg = new FacesMessage("Solo puede ingresar caracteres alfabeticos", "Solo puede ingresar caracteres alfabeticos");
				    	msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				    	throw new ValidatorException(msg);
			   		}
			}
	
}
