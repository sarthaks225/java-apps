package com.hr.dl.interfaces.dto;
public interface DesignationDTOInterface extends Comparable<DesignationDTOInterface>,java.io.Serializable //Serializable is a [marker interface]
{                                                                                                        //No methods have been declared in it
public void setCode(int code);
public int getCode();
public void setTitle(String title);
public String getTitle();
};