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
    String title=designation.getTitle();
    if(code!=0) blException.addException("code","code should be zero" );
    if(title==null) blException.addException("title","designation should not be empty");
    else if(title.length()==0) blException.addException("title","designation should not be empty");
    else if(titleWiseDesignationsMap.containsKey(title.toUpperCase())) blException.addException("title","designation ("+title+") already exists");
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

        titleWiseDesignationsMap.put(title.toUpperCase(),blDesignation);
        codeWiseDesignationsMap.put(code,blDesignation);
        designationsSet.add(blDesignation);
    }catch(DAOException daoe)
    {
        blException.setGenericException(daoe.getMessage());
        throw blException;
    }

}
public void update(DesignationInterface designation) throws BLException
{
    BLException blException=new BLException();
    if(designation==null) 
    {
        blException.setGenericException("designation required");
        throw blException;
    }
    int code=designation.getCode();
    if(code<=0) 
    {
        blException.addException("code","code ("+code+") should be greater then zero" );
        throw blException;
    }
    if(codeWiseDesignationsMap.containsKey(code)==false)
    {
        blException.addException("code", "code ("+code+") not exists, can not upadate");
        throw blException;
    }
    String title=designation.getTitle();
    DesignationInterface blDesignation;

    if(title==null) blException.addException("title","designation should not be empty");
    else if(title.length()==0) blException.addException("title","designation should not be empty");
    else
    {
        blDesignation=titleWiseDesignationsMap.get(title.toUpperCase());
        if(blDesignation!=null && blDesignation.getCode()!=code) blException.addException("title","designation ("+title+") already exists");
        else if(blDesignation!=null && blDesignation.getCode()==code) return;
    }
    if(blException.hasExceptions()) throw blException;

    try
    {
        DesignationDTOInterface designationDTO=new DesignationDTO();
        designationDTO.setTitle(title);
        designationDTO.setCode(code);
        DesignationDAOInterface designationDAO=new DesignationDAO();
        designationDAO.update(designationDTO);
        blDesignation=new Designation();
        blDesignation.setCode(code);
        blDesignation.setTitle(title);

         // removing old key (DesignationTitle) from DS 
        titleWiseDesignationsMap.remove(codeWiseDesignationsMap.get(code).getTitle().toUpperCase());
        designationsSet.remove(codeWiseDesignationsMap.get(code));

        //update DS
        titleWiseDesignationsMap.put(title.toUpperCase(),blDesignation);
        codeWiseDesignationsMap.put(code,blDesignation);
        designationsSet.add(blDesignation);
    }catch(DAOException daoe)
    {
        blException.setGenericException(daoe.getMessage());
        throw blException;
    }

}
public void remove(int code) throws BLException
{
    
    BLException blException=new BLException();
    
    if(code<=0) 
    {
        blException.addException("code","code ("+code+") should be greater then zero" );
        throw blException;
    }
    if(codeWiseDesignationsMap.containsKey(code)==false)
    {
        blException.addException("code", "code ("+code+") not exists, can not upadate");
        throw blException;
    }

    try
    {
        DesignationInterface designation=codeWiseDesignationsMap.get(code);
        DesignationDTOInterface designationDTO=new DesignationDTO();
        DesignationDAOInterface designationDAO=new DesignationDAO();
        designationDAO.delete(code);
        
        // removing old key (DesignationTitle) from DS 
        titleWiseDesignationsMap.remove(designation.getTitle().toUpperCase());
        codeWiseDesignationsMap.remove(code);
        designationsSet.remove(designation);

    }catch(DAOException daoe)
    {
        blException.setGenericException(daoe.getMessage());
        throw blException;
    }



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