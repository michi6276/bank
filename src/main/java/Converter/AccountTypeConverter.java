/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Converter;

import de.oth.gmeiner.swgmeiner.entity.AccountType;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import de.oth.gmeiner.swgmeiner.service.customerService;

/**
 *
 * @author Michael
 */
@FacesConverter("de.oth.sw.entity.converter.StudiengangConverter")
public class AccountTypeConverter implements Converter{
@Inject
private customerService customerService;
@Override
public Object getAsObject(FacesContext context, UIComponent component, String value) {
if (value==null)
return "";
AccountType stdgang = customerService.getTypebyName(value);
if(stdgang==null)
return "";
return stdgang;
}
@Override
public String getAsString(FacesContext context, UIComponent component, Object value) {
if(value==null)
return null;
if(!value.getClass().equals(AccountType.class))
return null;
return ((AccountType)value).getName();
}



}