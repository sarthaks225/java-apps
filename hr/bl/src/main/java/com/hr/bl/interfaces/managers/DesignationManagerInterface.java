package com.hr.bl.interfaces.managers;

import com.hr.bl.interfaces.pojo.*;
import com.hr.bl.pojo.*;
import com.hr.bl.exceptions.*;

import java.util.*;

public interface DesignationManagerInterface
{
public void add(DesignationInterface designation) throws BLException;
public void update(DesignationInterface designation) throws BLException;
public void remove(DesignationInterface designation) throws BLException;
public DesignationInterface getDesignationByCode(int code) throws BLException;
public DesignationInterface getDesignationByTitle(String title) throws BLException;
public Set<DesignationInterface> getDesignations() throws BLException;
public int getDesignationCount() throws BLException;
public boolean designationCodeExists(int code) throws BLException;
public boolean designationTitleExists(String title) throws BLException;
}