package com.exceptions;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.inject.Inject;
import javax.inject.Named;
import javax.faces.validator.ValidatorException;

import com.dao.CasillasDAO;
import com.entities.Casilla;
import com.services.CasillasBean;
import com.services.dto.CasillaDTO; 

@FacesValidator("com.exceptions.CasillaUnique")
public class CasillaUnique implements Validator, Serializable{
	
	@Inject
	private CasillasBean beanCasillas;
	
	public CasillaUnique(){
	
	}
	
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

		try {
			ArrayList<CasillaDTO> casillas = (ArrayList<CasillaDTO>) beanCasillas.obtenerTodos();
			
			String sola = "";
			if(casillas.size() > 0){
				for(CasillaDTO c: casillas) {
					sola = c.getNombre();
					if(c.getNombre().equalsIgnoreCase(value.toString())) {
						System.out.println(c.getNombre().equalsIgnoreCase(value.toString()));
						FacesMessage msg = 	new FacesMessage("Nombre de casilla duplicado",
								"Nombre de casilla duplicado");
								msg.setSeverity(FacesMessage.SEVERITY_ERROR);
								throw new ValidatorException(msg);
					}
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
	

