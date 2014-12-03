package com.canigenus.common.bean;

//Mobilefish.com Online Java Class Decompiler
//Author: Robert Lie
//Version: 0.1 (pre-alpha)
//http://www.mobilefish.com/services/java_decompiler/java_decompiler.php
//JVM version: 50.0
//File: GenericConstants.java
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;

public abstract class AbstractConverter
    implements Converter, Serializable
{ 

	private static final long serialVersionUID = 1L;
	public AbstractConverter()
    {
        setRbKeyPrefix();
    }

    public static SelectItem getFirstItemSelect()
    {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle rb = context.getApplication().getResourceBundle(context, "bundle");
        return new SelectItem("", rb.getString(SELECT_ONE_RB_KEY));
    }

    public List<SelectItem> getSelectItemList()
    {
        return getSelectItemList("", SELECT_ONE_RB_KEY);
    }
    
  
    public List<SelectItem> getSearchSelectItemList()
    {
        return getSelectItemList("", SELECT_ALL_RB_KEY);
    }

    public List<SelectItem> getItemList()
    {
        return getSelectItemList(null, null);
    }

    public List<SelectItem> getSearchRadioItemList()
    {
        return getSelectItemList("", ALL_RB_KEY);
    }

    protected List<SelectItem> getSelectItemList(String firstItemValue, String firstItemLabel)
    {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle rb = context.getApplication().getResourceBundle(context, "bundle");
        Field fields[] = getClass().getFields();
        ArrayList<SelectItem> selectItemList = new ArrayList<SelectItem>();
        if(firstItemValue != null || firstItemLabel != null)
            selectItemList.add(new SelectItem(firstItemValue, rb.getString(firstItemLabel)));
        Field arr$[] = fields;
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            Field field = arr$[i$];
            try
            {
                Object fieldValue = null;
                if(field.getType().equals(java.lang.Boolean.class))
                    fieldValue = field.get(new Boolean(true));
                else
                    fieldValue = field.get(new String());
                SelectItem selectItem = new SelectItem(fieldValue, rb.getString((new StringBuilder()).append(rbKeyPrefix).append(fieldValue.toString().trim()).toString()));
                selectItemList.add(selectItem);
            }
            catch(IllegalAccessException iae)
            {
                iae.printStackTrace();
            }
        }

        return selectItemList;
    }

    protected List<SelectItem> getSelectItemListForConstants(String constants[])
    {
        return getSelectItemListForConstants(constants, "bundle");
    }

    protected List<SelectItem> getSelectItemListForConstants(String constants[], String bundleKey)
    {
        ArrayList<SelectItem> selectItemList = new ArrayList<SelectItem>();
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle rb = context.getApplication().getResourceBundle(context, bundleKey);
        String arr$[] = constants;
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            String constant = arr$[i$];
            SelectItem selectItem = new SelectItem(constant, rb.getString((new StringBuilder()).append(rbKeyPrefix).append(constant.toString().trim()).toString()));
            selectItemList.add(selectItem);
        }

        return selectItemList;
    }

    public List<SelectItem> getSelectItemListForConstants(List<String> constants)
    {
        ArrayList<SelectItem> selectItemList = new ArrayList<SelectItem>();
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle rb = context.getApplication().getResourceBundle(context, "bundle");
        SelectItem selectItem;
        for(Iterator<String> i$ = constants.iterator(); i$.hasNext(); selectItemList.add(selectItem))
        {
            String constant = (String)i$.next();
            selectItem = new SelectItem(constant, rb.getString((new StringBuilder()).append(rbKeyPrefix).append(constant.toString().trim()).toString()));
        }

        return selectItemList;
    }

    public Object getAsObject(FacesContext context, UIComponent component, String value)
    {
        return value;
    }

    public String getAsString(FacesContext context, UIComponent component, Object value)
    {
        ResourceBundle rb = context.getApplication().getResourceBundle(context, "bundle");
        try
        {
            return rb.getString((new StringBuilder()).append(rbKeyPrefix).append(value.toString().trim()).toString());
        }
        catch(MissingResourceException e)
        {
            return "";
        }
    }

    public String getRbKeyPrefix()
    {
        return rbKeyPrefix;
    }

    public void setRbKeyPrefix(String rbKeyPrefix)
    {
        this.rbKeyPrefix = rbKeyPrefix;
    }

    public abstract void setRbKeyPrefix();

    protected static String SELECT_ONE_RB_KEY = "customs.common.selectone";
    protected static String SELECT_ALL_RB_KEY = "customs.common.selectall";
    protected static String ALL_RB_KEY = "customs.common.all";
    protected String rbKeyPrefix;

}
