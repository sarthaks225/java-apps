package com.hr.bl.managers;

import com.hr.bl.managers.*;
import com.hr.bl.exceptions.*;
import com.hr.bl.interfaces.managers.*;
import com.hr.bl.interfaces.pojo.*;
import com.hr.bl.pojo.*;

import com.network.common.*;
import com.network.common.exceptions.*;
import com.network.client.*;

import java.util.*;

import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument.HTMLReader.BlockAction;


public class DesignationManager implements DesignationManagerInterface
{

    private static DesignationManagerInterface designationManager=null;

    private DesignationManager() throws BLException
    {
        
    }

    public static DesignationManagerInterface getDesignationManager() throws BLException
    {
        if(designationManager==null) designationManager=new DesignationManager();
        return designationManager;
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
    else 
    {
        title=title.trim();
        if(title.length()==0) blException.addException("title","designation should not be empty");
    }
    if(blException.hasExceptions()) throw blException;

    Request request=new Request();

    request.setManager(Managers.getManagerType(Managers.DESIGNATION));
    request.setAction(Managers.Designation.getAction(Managers.Designation.ADD_DESIGNATION));
    request.setArguments(designation);

    NetworkClient networkClient=new NetworkClient();

    try
    {
        Response response=networkClient.send(request);
        if(response.hasException())
        {
            blException=(BLException)response.getException();
            throw blException;

        }
        DesignationInterface d=(DesignationInterface)response.getResult();
        designation.setCode(d.getCode());
    }catch(NetworkException ne)
    {

        blException.setGenericException(ne.getMessage());
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
    
    String title=designation.getTitle();
    DesignationInterface blDesignation;

    if(title==null) blException.addException("title","designation should not be empty");
    else 
    {
        title=title.trim();
        if(title.length()==0) blException.addException("title","designation should not be empty");
    }
    if(blException.hasExceptions()) throw blException;

    Request request=new Request();

    request.setManager(Managers.getManagerType(Managers.DESIGNATION));
    request.setAction(Managers.Designation.getAction(Managers.Designation.UPDATE_DESIGNATION));
    request.setArguments(designation);

    NetworkClient networkClient=new NetworkClient();

    try
    {
        Response response=networkClient.send(request);
        if(response.hasException())
        {
            blException=(BLException)response.getException();
            throw blException;
        }
        
    }catch(NetworkException ne)
    {
        blException.setGenericException(ne.getMessage());
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

    if(blException.hasExceptions()) throw blException;

    Request request=new Request();

    request.setManager(Managers.getManagerType(Managers.DESIGNATION));
    request.setAction(Managers.Designation.getAction(Managers.Designation.REMOVE_DESIGNATION));
    request.setArguments(new Integer(code));

    NetworkClient networkClient=new NetworkClient();

    try
    {
        Response response=networkClient.send(request);
        if(response.hasException())
        {
            blException=(BLException)response.getException();
            throw blException;
        }
    }catch(NetworkException ne)
    {
        blException.setGenericException(ne.getMessage());
        throw blException;
    }
   

}
public DesignationInterface getDesignationByCode(int code)  throws BLException
{
    BLException blException=new BLException();
    if(code<=0) blException.addException("code", "code ("+code+")should be greater then zero");
   
    if(blException.hasExceptions()) throw blException;
    
    Request request=new Request();

    request.setManager(Managers.getManagerType(Managers.DESIGNATION));
    request.setAction(Managers.Designation.getAction(Managers.Designation.GET_DESIGNATION_BY_CODE));
    request.setArguments(new Integer(code));

    NetworkClient networkClient=new NetworkClient();

    try
    {
        Response response=networkClient.send(request);
        if(response.hasException())
        {
            blException=(BLException)response.getException();
            throw blException;

        }
        DesignationInterface d=(DesignationInterface)response.getResult();
        return d;
    }catch(NetworkException ne)
    {

        blException.setGenericException(ne.getMessage());
        throw blException;
    }

   
}


public DesignationInterface getDesignationByTitle(String title)  throws BLException
{
    BLException blException=new BLException();
    if(title==null) blException.addException("title", "Designation title can not be empty");
    if(title.trim().length()==0) blException.addException("title", "Designation title can not be empty");
    if(blException.hasExceptions()) throw blException;
    
    Request request=new Request();

    request.setManager(Managers.getManagerType(Managers.DESIGNATION));
    request.setAction(Managers.Designation.getAction(Managers.Designation.GET_DESIGNATION_BY_TITLE));
    request.setArguments(title);

    NetworkClient networkClient=new NetworkClient();

    try
    {
        Response response=networkClient.send(request);
        if(response.hasException())
        {
            blException=(BLException)response.getException();
            throw blException;

        }
        DesignationInterface d=(DesignationInterface)response.getResult();
        return d;
    }catch(NetworkException ne)
    {

        blException.setGenericException(ne.getMessage());
        throw blException;
    }

 
}

public int getDesignationCount() throws BLException
{
    BLException blException=new BLException();

    Request request=new Request();

    request.setManager(Managers.getManagerType(Managers.DESIGNATION));
    request.setAction(Managers.Designation.getAction(Managers.Designation.GET_DESIGNATION_COUNT));

    NetworkClient networkClient=new NetworkClient();

    try
    {
        Response response=networkClient.send(request);
        if(response.hasException())
        {
            blException=(BLException)response.getException();
            throw blException;

        }
        Integer count=(Integer)response.getResult();
        return count;
    }catch(NetworkException ne)
    {

        blException.setGenericException(ne.getMessage());
        throw blException;
    }

}

public boolean designationCodeExists(int code) throws BLException
{
    if(code<=0) return false;
    BLException blException=new BLException();

    Request request=new Request();

    request.setManager(Managers.getManagerType(Managers.DESIGNATION));
    request.setAction(Managers.Designation.getAction(Managers.Designation.DESIGNATION_CODE_EXISTS));
    request.setArguments(new Integer(code));
    NetworkClient networkClient=new NetworkClient();

    try
    {
        Response response=networkClient.send(request);
        if(response.hasException())
        {
            blException=(BLException)response.getException();
            throw blException;

        }
        Boolean bool=(Boolean)response.getResult();
        return bool;
    }catch(NetworkException ne)
    {

        blException.setGenericException(ne.getMessage());
        throw blException;
    }
}

public boolean designationTitleExists(String title)  throws BLException
{
    title=title.trim();
    if(title.length()==0) return false;
    BLException blException=new BLException();

    Request request=new Request();

    request.setManager(Managers.getManagerType(Managers.DESIGNATION));
    request.setAction(Managers.Designation.getAction(Managers.Designation.DESIGNATION_TITLE_EXISTS));
    request.setArguments(title);
    NetworkClient networkClient=new NetworkClient();

    try
    {
        Response response=networkClient.send(request);
        if(response.hasException())
        {
            blException=(BLException)response.getException();
            throw blException;

        }
        Boolean bool=(Boolean)response.getResult();
        return bool;
    }catch(NetworkException ne)
    {

        blException.setGenericException(ne.getMessage());
        throw blException;
    }
}

public Set<DesignationInterface> getDesignations()  throws BLException
{

    BLException blException=new BLException();
    Request request=new Request();

    request.setManager(Managers.getManagerType(Managers.DESIGNATION));
    request.setAction(Managers.Designation.getAction(Managers.Designation.GET_DESIGNATIONS));
    NetworkClient networkClient=new NetworkClient();

    try
    {
        Response response=networkClient.send(request);
        if(response.hasException())
        {
            blException=(BLException)response.getException();
            throw blException;
        }

        Set<DesignationInterface> designations=new TreeSet<>();
        designations=( Set<DesignationInterface>)response.getResult();
        return designations;
    }catch(NetworkException ne)
    {

        blException.setGenericException(ne.getMessage());
        throw blException;
    }

}

};