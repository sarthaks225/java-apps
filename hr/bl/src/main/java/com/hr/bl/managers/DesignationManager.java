package com.hr.bl.managers;

import com.hr.bl.exceptions.*;
import com.hr.bl.interfaces.managers.*;
import com.hr.bl.interfaces.pojo.*;
import com.hr.bl.pojo.*;

import com.hr.dl.interfaces.dao.*;
import com.hr.dl.interfaces.dto.*;
import com.hr.dl.exceptions.*;
import com.hr.dl.dao.*;
import com.hr.dl.dto.*;

import java.util.*;

import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument.HTMLReader.BlockAction;

public class DesignationManager implements DesignationManagerInterface
{
    private Map<Integer,DesignationInterface> codeWiseDesignationsMap;
    private Map<String,DesignationInterface> titleWiseDesignationsMap;
    private Set<DesignationInterface> designationsSet;

    private static DesignationManagerInterface designationManager=null;

    private DesignationManager() throws BLException
    {
        codeWiseDesignationsMap=new HashMap<>();
        titleWiseDesignationsMap=new HashMap<>();
        designationsSet=new TreeSet<>();
        populateDataStructure();
    }

    public static DesignationManagerInterface getDesignationManager() throws BLException
    {
        if(designationManager==null) designationManager=new DesignationManager();
        return designationManager;
    }

    private void populateDataStructure() throws BLException
    {
        try
        {
            DesignationDAOInterface designationDAO=new DesignationDAO();
            Set<DesignationDTOInterface> designations=designationDAO.getAll();
            DesignationInterface designation;
            for(DesignationDTOInterface designationDTO: designations)
            {
                designation=new Designation();
                designation.setCode(designationDTO.getCode());
                designation.setTitle(designationDTO.getTitle());

                this.codeWiseDesignationsMap.put(designationDTO.getCode(),designation);
                this.titleWiseDesignationsMap.put(designationDTO.getTitle().toUpperCase(),designation);
                this.designationsSet.add(designation);
            }
        }catch(DAOException daoe)
        {
            BLException blException=new BLException();
            blException.setGenericException(daoe.getMessage());
            throw blException;
        }

    }






public void add(DesignationInterface designation) throws BLException
{
BLException blException=new BLException();
blException.setGenericException("Not Yet Implemented");
throw blException;
}
public void update(DesignationInterface designation) throws BLException
{
    BLException blException=new BLException();
    blException.setGenericException("Not Yet Implemented");
    throw blException;

}
public void remove(DesignationInterface designation) throws BLException
{
    BLException blException=new BLException();
    blException.setGenericException("Not Yet Implemented");
    throw blException;

}
public DesignationInterface getDesignationByCode(int code)  throws BLException
{
    BLException blException=new BLException();
    blException.setGenericException("Not Yet Implemented");
    throw blException;
}

public DesignationInterface getDesignationByTitle(String title)  throws BLException
{
    BLException blException=new BLException();
    blException.setGenericException("Not Yet Implemented");
    throw blException;
}

public Set<DesignationInterface> getDesignations()  throws BLException
{
    BLException blException=new BLException();
    blException.setGenericException("Not Yet Implemented");
    throw blException;

}

public int getDesignationCount() throws BLException
{
    BLException blException=new BLException();
    blException.setGenericException("Not Yet Implemented");
    throw blException;

}

public boolean designationCodeExists(int code) throws BLException
{
    BLException blException=new BLException();
    blException.setGenericException("Not Yet Implemented");
    throw blException;

}

public boolean designationTitleExists(String title)  throws BLException
{
    BLException blException=new BLException();
    blException.setGenericException("Not Yet Implemented");
    throw blException;

}

};