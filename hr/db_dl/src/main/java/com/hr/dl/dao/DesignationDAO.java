package com.hr.dl.dao;
import com.hr.dl.interfaces.dao.*;
import com.hr.dl.interfaces.dto.*;
import com.hr.dl.exceptions.*;
import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.hr.dl.dto.*;
public class DesignationDAO implements DesignationDAOInterface
{
   
public void add(DesignationDTOInterface designationDTO) throws DAOException
{
    if(designationDTO==null || designationDTO.getTitle()==null || designationDTO.getTitle().trim().length()==0) throw new DAOException("Designation is null");
    String title=designationDTO.getTitle().trim();
   
    try
    {
        Connection c=ConnectionDAO.getConnection();
        PreparedStatement preparedStatement=c.prepareStatement("select code from designation where title=?");
        preparedStatement.setString(1,title);
        ResultSet r=preparedStatement.executeQuery();
        if(r.next()==true)
        {
            r.close();
            preparedStatement.close();
            c.close();;
            throw new DAOException("Designation : ("+title+") already exists");            
        }

        r.close();
        preparedStatement.close();
        preparedStatement=c.prepareStatement("insert into designation (title) value (?)",Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, title);
        preparedStatement.executeUpdate();
        r=preparedStatement.getGeneratedKeys();
        r.next();
        int code=r.getInt(1);
        r.close();
        preparedStatement.close();
        c.close();
        designationDTO.setCode(code);



    }catch(Exception e)
    {
        throw new DAOException(e.getMessage());
        
    }
    


}
public void update(DesignationDTOInterface designationDTO) throws DAOException
{
    if(designationDTO==null) throw new DAOException("Desigantion is null");
    int code=designationDTO.getCode();
    String title=designationDTO.getTitle().trim();
    if(code<=0 || title.length()==0)  throw new DAOException("invalid Designation");

    try
    {
        Connection c=ConnectionDAO.getConnection();
        PreparedStatement preparedStatement;
        preparedStatement=c.prepareStatement("select code from designation where code=?");
        preparedStatement.setString(1,Integer.toString(code));
        ResultSet r;
        r=preparedStatement.executeQuery();
        if(r.next()==false)
        {
            r.close();
            preparedStatement.close();
            c.close();
            throw new DAOException("Code : ("+code+") does not exist.");
        }

        preparedStatement=c.prepareStatement("select code from designation where title=?");
        preparedStatement.setString(1,title);
        r=preparedStatement.executeQuery();
       
        if(r.next())
        {
            r.close();
            preparedStatement.close();
            c.close();
            throw new DAOException("title : ("+title+") already exists");
        }

        r.close();
        preparedStatement.close();
        
        preparedStatement=c.prepareStatement("update designation set title=? where code=?");
        preparedStatement.setString(1,title);
        preparedStatement.setString(2,Integer.toString(code));
        preparedStatement.executeUpdate();

        preparedStatement.close();
        c.close();
    }catch(Exception e)
    {
        throw new DAOException(e.getMessage());
    }
}
public void delete(int code) throws DAOException
{

    if(code<=0)  throw new DAOException("invalid Designation code: "+code);
    try
    {

        Connection c=ConnectionDAO.getConnection();
        PreparedStatement preparedStatement;
        ResultSet r;
        preparedStatement=c.prepareStatement("select title from designation where code=?");
        preparedStatement.setString(1,Integer.toString(code));
        r=preparedStatement.executeQuery();

        if(r.next()==false)
        {
            r.close();
            preparedStatement.close();
            c.close();
            throw new DAOException("Code ("+code+") does not exists");
        }
        String designationTitle=r.getString("title");
        r.close();
        preparedStatement.close();

        preparedStatement=c.prepareStatement("select employee_id from employee where designation_code=?");
        preparedStatement.setInt(1,code);
        r=preparedStatement.executeQuery();
        if(r.next())
        {
            r.close();
            preparedStatement.close();
            c.close();
            throw new DAOException("Can not remove Designation ("+designationTitle+"),it is alloted to an employee ");
        }
        r.close();
        preparedStatement.close();

        preparedStatement=c.prepareStatement("delete from designation title where code=?");
        preparedStatement.setString(1, Integer.toString(code));
        preparedStatement.executeUpdate();
        preparedStatement.close();
        c.close();
        
    }catch(Exception e)
    {
     
        throw new DAOException(e.getMessage());

    }
}

public Set<DesignationDTOInterface> getAll() throws DAOException
{
    Set<DesignationDTOInterface> designations;
    designations=new TreeSet<DesignationDTOInterface>();
    DesignationDTO designationDTO;

    try
    {
        Connection c=ConnectionDAO.getConnection();
        Statement statement=c.createStatement();
        ResultSet r;
        r=statement.executeQuery("select * from designation");
        while(r.next())
        {
            designationDTO=new DesignationDTO();
            designationDTO.setCode(r.getInt("code"));
            designationDTO.setTitle(r.getString("title").trim());
            designations.add(designationDTO);
        }
        r.close();
        statement.close();
        c.close();
        return designations;
    }catch(SQLException e)
    {
        
        throw new DAOException(e.getMessage());
    }
 
}

public DesignationDTOInterface getByCode(int code) throws DAOException
{
 
    if(code<=0) throw new DAOException("Code : ("+code+") not found");
    try
    {
        Connection c=ConnectionDAO.getConnection();

        PreparedStatement preparedStatement=c.prepareStatement("select code from designation where code=?");
        ResultSet r;
        preparedStatement.setString(1,Integer.toString(code));
        r=preparedStatement.executeQuery();
        if(r.next()==false)
        {
            r.close();
            preparedStatement.close();
            c.close();
            throw new DAOException("Code ("+code+") does not exist");
        }
        r.close();
        preparedStatement.close();
        preparedStatement=c.prepareStatement("select code,title from designation where code=?");
        preparedStatement.setString(1,Integer.toString(code));
        r=preparedStatement.executeQuery();
        DesignationDTOInterface designationDTO=new DesignationDTO();
        r.next();

        designationDTO.setCode(r.getInt("code"));
        designationDTO.setTitle(r.getString("title"));

        r.close();
        preparedStatement.close();
        c.close();
        return designationDTO;

    }catch(Exception e)
    {
       throw new DAOException(e.getMessage());
    }
}

public DesignationDTOInterface getByTitle(String string) throws DAOException
{

    if(string==null) throw new DAOException("Designation : ("+string+") not found");
    String title=string.trim();
    
    if(title.length()==0) throw new DAOException("Designation : ("+string+") not found");
    try
    {
       
        Connection c=ConnectionDAO.getConnection();

        PreparedStatement preparedStatement=c.prepareStatement("select code from designation where title=?");
        ResultSet r;
        preparedStatement.setString(1,title);
        r=preparedStatement.executeQuery();
        
        if(r.next()==false)
        {
            r.close();
            preparedStatement.close();
            c.close();
            throw new DAOException("Designation ("+title+") does not exist");
        }
        
        r.close();
        preparedStatement.close();
        preparedStatement=c.prepareStatement("select code,title from designation where title=?");
        preparedStatement.setString(1,title);
        r=preparedStatement.executeQuery();
        DesignationDTOInterface designationDTO=new DesignationDTO();
       
        r.next();
        designationDTO.setCode(r.getInt("code"));
        designationDTO.setTitle(r.getString("title").trim());

        r.close();
        preparedStatement.close();
        c.close();
        return designationDTO;

    }catch(Exception e)
    {

        throw new DAOException(e.getMessage());
        
    }

}

public boolean codeExists(int code) throws DAOException
{
    if(code<=0) return false;
    try
    {  
        Connection c=ConnectionDAO.getConnection();

        PreparedStatement preparedStatement=c.prepareStatement("select code from designation where code=?");
        ResultSet r;
        preparedStatement.setString(1,Integer.toString(code));
        r=preparedStatement.executeQuery();
        if(r.next()==false)
        {
            r.close();
            preparedStatement.close();
            c.close();
            return false;
        }
        r.close();
        preparedStatement.close();
        c.close();

        return true;
    }catch(Exception e)
    {
        throw new DAOException(e.getMessage());
    }
}

public boolean titleExists(String string) throws DAOException
{
    if(string==null) return false;
    String title=string.trim();
    if(title.length()==0) return false;

    try
    {

        Connection c=ConnectionDAO.getConnection();

        PreparedStatement preparedStatement=c.prepareStatement("select code from designation title=?");
        ResultSet r;
        preparedStatement.setString(1,title);
        r=preparedStatement.executeQuery();
        if(r.next()==false)
        {
            r.close();
            preparedStatement.close();
            c.close();
            return false;
        }
        return true;
    }catch(Exception e)
    {

        throw new DAOException(e.getMessage());
    }




}

public int getCount() throws DAOException
{
   
    try
    {
        Connection c=ConnectionDAO.getConnection();

        Statement statement=c.createStatement();
        ResultSet r;
        r=statement.executeQuery("select count(*) from designation code");
        r.next();
        return r.getInt("count(*)");
       
    }catch(Exception e)
    {
        throw new DAOException(e.getMessage());
    }
}


};