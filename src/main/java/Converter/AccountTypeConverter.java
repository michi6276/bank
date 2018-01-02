/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Converter;

import de.oth.gmeiner.swgmeiner.entity.AccountType;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import javax.inject.Inject;
import de.oth.gmeiner.swgmeiner.service.customerService;
import java.lang.annotation.Annotation;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.convert.Converter;

import javax.inject.Named;


/**
 *
 * @author Michael
 */
@Named
@ApplicationScoped
public class AccountTypeConverter implements Converter {
@Inject
private customerService customerService;

@Override
public Object getAsObject(FacesContext context, UIComponent component, String value) {
if (value==null)
return "";
AccountType type = customerService.getTypebyId(Long.parseLong(value));
if(type==null)
return "";
return type;
}
@Override
public String getAsString(FacesContext context, UIComponent component, Object value) {
if(value==null)
return null;
if(!value.getClass().equals(AccountType.class))
return null;
return String.valueOf(((AccountType)value).getId());
}





}