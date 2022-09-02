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
    if(designation==null) 
    {
        blException.setGenericException("designation required");
        throw blException;
    }
    int code=designation.getCode();
    String title=designation.getTitle().trim().toUpperCase();
    if(code!=0) blException.addException("code","code should be zero" );;
    if(title==null) blException.addException("title","designation should not be empty");
    else if(title.length()==0) blException.addException("title","designation should not be empty");
    else if(titleWiseDesignationsMap.containsKey(title)) blException.addException("title","designation ("+title+") already exists");
    if(blException.hasExceptions()) throw blException;

    try
    {
        DesignationDTOInterface designationDTO=new DesignationDTO();
        designationDTO.setTitle(title);
        DesignationDAOInterface designationDAO=new DesignationDAO();
        designationDAO.add(designationDTO);
        code=designationDTO.getCode();
        designation.setCode(code);
        DesignationInterface blDesignation=new Designation();
        blDesignation.setCode(code);
        blDesignation.setTitle(title);

        titleWiseDesignationsMap.put(title,blDesignation);
        codeWiseDesignationsMap.put(code,blDesignation);
        designationsSet.add(designation);
    }catch(DAOException daoe)
    {
        blException.setGenericException(daoe.getMessage());
        throw blException;
    }

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