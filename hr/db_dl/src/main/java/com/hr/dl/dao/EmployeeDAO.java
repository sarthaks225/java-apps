package com.hr.dl.dao;
import com.hr.dl.exceptions.*;
import com.hr.dl.interfaces.dao.*;
import com.hr.dl.interfaces.dto.*;
import com.hr.common.enums.GENDER;
import com.hr.dl.dao.*;
import com.hr.dl.dto.*;
import java.text.*;
import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.zip.DataFormatException;
import java.sql.*;
public class EmployeeDAO implements EmployeeDAOInterface
{
private static String fileName="employee.data";
public void add(EmployeeDTOInterface employeeDTO) throws DAOException
{
    Connection c=ConnectionDAO.getConnection();
    PreparedStatement preparedStatement;
    ResultSet r;

    if(employeeDTO==null) throw new DAOException("error: employee is null");
    String employeeId;
    String name=employeeDTO.getName();
    if(name==null || name.length()==0) throw new DAOException("error: employee name is null");
    int designationCode=employeeDTO.getDesignationCode();
    if(designationCode<=0) throw new DAOException("error: designation code is invalid: "+designationCode);
    
    DesignationDAOInterface designationDAO=new DesignationDAO();
    
    if(!(designationDAO.codeExists(designationCode))) throw new DAOException("error code does not exist :"+designationCode);
    
    java.util.Date dateOfBirth=employeeDTO.getDateOfBirth();
    if(dateOfBirth==null) throw new DAOException("error: Date is null");



    char gender=employeeDTO.getGender();
    if(gender==' ') throw new DAOException("Gender not set Male/Female");
    boolean isIndian=employeeDTO.getIsIndian();
    BigDecimal basicSalary=employeeDTO.getBasicSalary();
    if(basicSalary.signum()==-1) throw new DAOException("error: salary can not be negative");
    String panNumber=employeeDTO.getPANNumber().trim();
    if(panNumber==null || panNumber.length()==0) throw new DAOException("error: PAN Number is null");
    String aadharCardNumber=employeeDTO.getAadharCardNumber();
    if(aadharCardNumber==null || aadharCardNumber.length()==0) throw new DAOException("error: Aadhar Card Number is invalid");
   
    try
    {
        boolean panNumberExists=false;
       
        preparedStatement=c.prepareStatement("select employee_id from employee where pan_number=?");
       
        preparedStatement.setString(1,panNumber);
        r=preparedStatement.executeQuery();
        if(r.next()==true) panNumberExists=true;
        r.close();
        preparedStatement.close();
    
        boolean aadharCardNumberExists=false;
        preparedStatement=c.prepareStatement("select employee_id from employee where aadhar_card_number=?");
        preparedStatement.setString(1,aadharCardNumber);
        r=preparedStatement.executeQuery();
        if(r.next()==true) aadharCardNumberExists=true;
        r.close();
        preparedStatement.close();

        if(panNumberExists && aadharCardNumberExists)
        {
            throw new DAOException("PAN Number and Aadhar Card Number already exists");
        }
        else if(panNumberExists)
        {
            throw new DAOException("PAN Number already exists");
        }
        else if(aadharCardNumberExists)
        {
            throw new DAOException("Aadhar Card Number already exists");
        }

        preparedStatement=c.prepareStatement("insert into employee (name,designation_code,date_of_birth,gender,is_indian,basic_salary,pan_number,aadhar_card_number) values(?,?,?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1,name);
        preparedStatement.setInt(2,designationCode);
        java.sql.Date sqlDate=new java.sql.Date(dateOfBirth.getYear(),dateOfBirth.getMonth(),dateOfBirth.getDate());
        preparedStatement.setDate(3,sqlDate);
        preparedStatement.setString(4,Character.toString(gender));        
        preparedStatement.setBoolean(5, isIndian);
        preparedStatement.setBigDecimal(6, basicSalary);
        preparedStatement.setString(7,panNumber);
        preparedStatement.setString(8,aadharCardNumber);

        preparedStatement.executeUpdate();

        r=preparedStatement.getGeneratedKeys();
        r.next();
        employeeDTO.setEmployeeId("A"+Integer.toString(100000+r.getInt(1)));

        r.close();
        preparedStatement.close();
        c.close();
    }catch(Exception e)
    {
        throw new DAOException(e.getMessage());

    }

}
public void update(EmployeeDTOInterface employeeDTO) throws DAOException
{

    Connection c=ConnectionDAO.getConnection();
    PreparedStatement preparedStatement=null;
    ResultSet r=null;

    if(employeeDTO==null) throw new DAOException("error: employee is null");

    String employeeId=employeeDTO.getEmployeeId().trim();
    if(employeeId==null || employeeId.length()==0) throw new DAOException("error: invalid employee ID can not update");
    int sqlEmployeeId=Integer.parseInt(employeeId.substring(1,employeeId.length()))-100000;
    try
    {
        preparedStatement=c.prepareStatement("select employee_id from employee where employee_id=?");
        preparedStatement.setInt(1,sqlEmployeeId);

        r=preparedStatement.executeQuery();
        if(r.next()==false)
        {
            r.close();
            preparedStatement.close();
            c.close();
            throw new DAOException("employeeId does not exist");
        }
        r.close();
        preparedStatement.close();

    }catch(SQLException e)
    {
        throw new DAOException(e.getMessage());
    }


    String name=employeeDTO.getName();
    if(name==null || name.length()==0) throw new DAOException("error: employee name is null can not update");
    int designationCode=employeeDTO.getDesignationCode();
    if(designationCode<=0) throw new DAOException("error: designation code is invalid: "+designationCode);
    DesignationDAOInterface designationDAO=new DesignationDAO();
    if(!(designationDAO.codeExists(designationCode))) throw new DAOException("error code does not exist :"+designationCode);
    java.util.Date dateOfBirth=employeeDTO.getDateOfBirth();
    if(dateOfBirth==null) throw new DAOException("error: Date is null");
    char gender=employeeDTO.getGender();
    if(gender==' ') throw new DAOException("Gender is not set to Male/Female");
    boolean isIndian=employeeDTO.getIsIndian();
    BigDecimal basicSalary=employeeDTO.getBasicSalary();
    if(basicSalary.signum()==-1) throw new DAOException("error: salary can not be negative");
    String panNumber=employeeDTO.getPANNumber().trim();
    if(panNumber==null || panNumber.length()==0) throw new DAOException("error: PAN Number is null");
    String aadharCardNumber=employeeDTO.getAadharCardNumber().trim();
    if(aadharCardNumber==null || aadharCardNumber.length()==0) throw new DAOException("error: Aadhar Card Number is invalid");

    try
    {

        boolean panNumberExists=false;
       
        preparedStatement=c.prepareStatement("select employee_id from employee where pan_number=? and employee_id<>?");
       
        preparedStatement.setString(1,panNumber);
        preparedStatement.setInt(2,sqlEmployeeId);
        r=preparedStatement.executeQuery();
        if(r.next()==true) panNumberExists=true;
        r.close();
        preparedStatement.close();
    
        boolean aadharCardNumberExists=false;
        preparedStatement=c.prepareStatement("select employee_id from employee where aadhar_card_number=? and employee_id<>?");
        preparedStatement.setString(1,aadharCardNumber);
        preparedStatement.setInt(2,sqlEmployeeId);
        r=preparedStatement.executeQuery();
        if(r.next()==true) aadharCardNumberExists=true;
        r.close();
        preparedStatement.close();

        if(panNumberExists && aadharCardNumberExists)
        {
            throw new DAOException("PAN Number and Aadhar Card Number already exists");
        }
        else if(panNumberExists)
        {
            throw new DAOException("PAN Number already exists");
        }
        else if(aadharCardNumberExists)
        {
            throw new DAOException("Aadhar Card Number already exists");
        }
       
        preparedStatement=c.prepareStatement("update employee set name=?,designation_code=?,date_of_birth=?,gender=?,is_indian=?,basic_salary=?,pan_number=?,aadhar_card_number=? where employee_id=?");
       
        preparedStatement.setString(1,name);
        preparedStatement.setInt(2,designationCode);

        java.sql.Date sqlDate=new java.sql.Date(dateOfBirth.getYear(),dateOfBirth.getMonth(),dateOfBirth.getDate());
        preparedStatement.setDate(3,sqlDate);

        preparedStatement.setString(4,Character.toString(gender));        
        preparedStatement.setBoolean(5, isIndian);
        preparedStatement.setBigDecimal(6, basicSalary);
        preparedStatement.setString(7,panNumber);
        preparedStatement.setString(8,aadharCardNumber);
        preparedStatement.setInt(9,sqlEmployeeId);
       
        preparedStatement.executeUpdate();
     

        preparedStatement.close();
        c.close();
      
    }catch(Exception e)
    {
        throw new DAOException(e.getMessage());

    }

   
}


public void delete(String employeeId) throws DAOException
{
    if(employeeId==null) throw new DAOException("error: employee Id is null");

    employeeId=employeeId.trim();
    if(employeeId.length()==0) throw new DAOException("error: invalid employee ID can not delete");

    Connection c=ConnectionDAO.getConnection();
    PreparedStatement preparedStatement=null;
    ResultSet r=null;
    int sqlEmployeeId=Integer.parseInt(employeeId.substring(1,employeeId.length()))-100000;
    try
    {
        preparedStatement=c.prepareStatement("select employee_id from employee where employee_id=?");
        preparedStatement.setInt(1,sqlEmployeeId);
        r=preparedStatement.executeQuery();
        if(r.next()==false)
        {
            r.close();
            preparedStatement.close();
            c.close();
            throw new DAOException("Employee id : ("+employeeId+") not exist");
        }

        r.close();
        preparedStatement.close();

        preparedStatement=c.prepareStatement("delete from employee where employee_id=?");
        preparedStatement.setInt(1,sqlEmployeeId);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        c.close();
    }catch(Exception e)
    {
        throw new DAOException(e.getMessage());
    }
}

public Set<EmployeeDTOInterface> getAll() throws DAOException
{
    Set<EmployeeDTOInterface> employees=new TreeSet<>();
    EmployeeDTOInterface employeeDTO=null;
    Connection c=null;
    Statement statement=null;
    ResultSet r=null;

    try
    {
        c=ConnectionDAO.getConnection();
        statement=c.createStatement();
        r=statement.executeQuery("Select * from employee");

        GENDER gender;
        String male="m";
        while(r.next())
        {
            employeeDTO=new EmployeeDTO();
            employeeDTO.setEmployeeId("A"+Integer.toString(100000+Integer.parseInt(r.getString("employee_id").trim())));
            employeeDTO.setName(r.getString("name").trim());

            employeeDTO.setDesignationCode(r.getInt("designation_code"));

            employeeDTO.setDateOfBirth(new java.util.Date((r.getDate("date_of_birth")).getTime()));

            gender=male.equalsIgnoreCase(r.getString("gender"))?GENDER.MALE:GENDER.FEMALE;
            employeeDTO.setGender(gender);

            employeeDTO.setIsIndian(r.getBoolean("is_Indian"));
            employeeDTO.setBasicSalary(r.getBigDecimal("basic_salary"));
            employeeDTO.setPANNumber(r.getString("pan_number").trim());
            employeeDTO.setAadharCardNumber(r.getString("aadhar_card_number").trim());
            employees.add(employeeDTO);

        }

        r.close();
        statement.close();
        c.close();
        return employees;

    }catch(Exception e)
    {
        throw new DAOException(e.getMessage());
    }
}

public Set<EmployeeDTOInterface> getByDesignation(int designationCode) throws DAOException
{

    DesignationDAOInterface designationDAO=new DesignationDAO();
    if(!designationDAO.codeExists(designationCode)) throw new DAOException("error designation ("+designationCode+") not exist");
    Set<EmployeeDTOInterface> employees=new TreeSet<>();

    Connection c=ConnectionDAO.getConnection();
    PreparedStatement preparedStatement=null;
    ResultSet r=null;

    try
    {
        preparedStatement=c.prepareStatement("select code from designation where code=?");
        preparedStatement.setInt(1,designationCode);
        r=preparedStatement.executeQuery();
        if(r.next()==false)
        {
            r.close();
            preparedStatement.close();
            c.close();
            throw new DAOException("designation does not exists");
        }
        r.close();
        preparedStatement.close();

        preparedStatement=c.prepareStatement("select * from employee where designation_code=?");
        preparedStatement.setInt(1,designationCode);
        r=preparedStatement.executeQuery();

        GENDER gender;
        String male="m";
        EmployeeDTOInterface employeeDTO;
        while(r.next())
        {
            employeeDTO=new EmployeeDTO();
            employeeDTO.setEmployeeId("A"+Integer.toString(100000+Integer.parseInt(r.getString("employee_id").trim())));
            employeeDTO.setName(r.getString("name").trim());

            employeeDTO.setDesignationCode(r.getInt("designation_code"));

            employeeDTO.setDateOfBirth(new java.util.Date((r.getDate("date_of_birth")).getTime()));

            gender=male.equalsIgnoreCase(r.getString("gender"))?GENDER.MALE:GENDER.FEMALE;
            employeeDTO.setGender(gender);

            employeeDTO.setIsIndian(r.getBoolean("is_Indian"));
            employeeDTO.setBasicSalary(r.getBigDecimal("basic_salary"));
            employeeDTO.setPANNumber(r.getString("pan_number").trim());
            employeeDTO.setAadharCardNumber(r.getString("aadhar_card_number").trim());
            employees.add(employeeDTO);

        }

        r.close();
        preparedStatement.close();
        c.close();
        return employees;

    }catch(Exception e)
    {
        throw new DAOException(e.getMessage());
    }

}

public boolean isDesignationAlloted(int designationCode) throws DAOException
{
    DesignationDAOInterface designationDAO=new DesignationDAO();
    if(!designationDAO.codeExists(designationCode)) return false;
    Connection c=ConnectionDAO.getConnection();
    Statement statement;
    ResultSet r=null;
    boolean bool;
    try
    {
        statement=c.createStatement();
        r=statement.executeQuery("select gender from employee where designation_code="+designationCode);
        bool=r.next();
        r.close();
        statement.close();
        c.close();
    }catch(Exception e)
    {
        throw new DAOException(e.getMessage());
    }
   
    return bool;
   
}

public EmployeeDTOInterface getByEmployeeId(String employeeId) throws DAOException
{
    if(employeeId==null) throw new DAOException("error: invalid employee id");
    employeeId=employeeId.trim();
    if(employeeId.length()==0) throw new DAOException("error: employee id length is zero");

    Connection c=ConnectionDAO.getConnection();
    PreparedStatement preparedStatement=null;
    ResultSet r=null;

    int sqlEmployeeId=Integer.parseInt(employeeId.substring(1,employeeId.length()))-100000;
    try
    {
        preparedStatement=c.prepareStatement("select * from employee where employee_id=?");
        preparedStatement.setInt(1,sqlEmployeeId);
        r=preparedStatement.executeQuery();
        if(r.next()==false)
        {
            r.close();
            preparedStatement.close();
            c.close();
            throw new DAOException("employee ID does not exists");
        }

        GENDER gender;
        String male="m";
        EmployeeDTOInterface employeeDTO;
       
        employeeDTO=new EmployeeDTO();
        employeeDTO.setEmployeeId(employeeId);
        employeeDTO.setName(r.getString("name").trim());

        employeeDTO.setDesignationCode(r.getInt("designation_code"));

        employeeDTO.setDateOfBirth(new java.util.Date((r.getDate("date_of_birth")).getTime()));

        gender=male.equalsIgnoreCase(r.getString("gender"))?GENDER.MALE:GENDER.FEMALE;
        employeeDTO.setGender(gender);

        employeeDTO.setIsIndian(r.getBoolean("is_Indian"));
        employeeDTO.setBasicSalary(r.getBigDecimal("basic_salary"));
        employeeDTO.setPANNumber(r.getString("pan_number").trim());
        employeeDTO.setAadharCardNumber(r.getString("aadhar_card_number").trim());

        r.close();
        preparedStatement.close();
        c.close();
        return employeeDTO;

    }catch(Exception e)
    {
        throw new DAOException(e.getMessage());
    }
   
}

public EmployeeDTOInterface getByPANNumber(String panNumber) throws DAOException
{
    if(panNumber==null) throw new DAOException("error: invalid pan number");
    panNumber=panNumber.trim();
    if(panNumber.length()==0) throw new DAOException("error: pan number length is zero");

    Connection c=ConnectionDAO.getConnection();
    PreparedStatement preparedStatement=null;
    ResultSet r=null;
    
    try
    {
        preparedStatement=c.prepareStatement("select * from employee where pan_number=?");
        preparedStatement.setString(1,panNumber);
        r=preparedStatement.executeQuery();
        if(r.next()==false)
        {
            r.close();
            preparedStatement.close();
            c.close();
            throw new DAOException("panNumber does not exists");
        }

        GENDER gender;
        String male="m";
        EmployeeDTOInterface employeeDTO;
       
        employeeDTO=new EmployeeDTO();
        employeeDTO.setEmployeeId("A"+Integer.toString(100000+r.getInt("employee_id")));
        employeeDTO.setName(r.getString("name").trim());

        employeeDTO.setDesignationCode(r.getInt("designation_code"));

        employeeDTO.setDateOfBirth(new java.util.Date((r.getDate("date_of_birth")).getTime()));

        gender=male.equalsIgnoreCase(r.getString("gender"))?GENDER.MALE:GENDER.FEMALE;
        employeeDTO.setGender(gender);

        employeeDTO.setIsIndian(r.getBoolean("is_Indian"));
        employeeDTO.setBasicSalary(r.getBigDecimal("basic_salary"));
        employeeDTO.setPANNumber(panNumber);
        employeeDTO.setAadharCardNumber(r.getString("aadhar_card_number").trim());

        r.close();
        preparedStatement.close();
        c.close();
        return employeeDTO;

    }catch(Exception e)
    {
        throw new DAOException(e.getMessage());
    }
   
}

public EmployeeDTOInterface getByAadharCardNumber(String aadharCardNumber) throws DAOException
{

    if(aadharCardNumber==null) throw new DAOException("error: invalid aadharCardNumber");
    aadharCardNumber=aadharCardNumber.trim();
    if(aadharCardNumber.length()==0) throw new DAOException("error: aadharCardNumber length is zero");

    Connection c=ConnectionDAO.getConnection();
    PreparedStatement preparedStatement=null;
    ResultSet r=null;
    
    try
    {
        preparedStatement=c.prepareStatement("select * from employee where aadhar_card_number=?");
        preparedStatement.setString(1,aadharCardNumber);
        r=preparedStatement.executeQuery();
        if(r.next()==false)
        {
            r.close();
            preparedStatement.close();
            c.close();
            throw new DAOException("aadhar Card Number does not exists");
        }

        GENDER gender;
        String male="m";
        EmployeeDTOInterface employeeDTO;
       
        employeeDTO=new EmployeeDTO();
        employeeDTO.setEmployeeId("A"+Integer.toString(100000+r.getInt("employee_id")));
        employeeDTO.setName(r.getString("name").trim());

        employeeDTO.setDesignationCode(r.getInt("designation_code"));

        employeeDTO.setDateOfBirth(new java.util.Date((r.getDate("date_of_birth")).getTime()));

        gender=male.equalsIgnoreCase(r.getString("gender"))?GENDER.MALE:GENDER.FEMALE;
        employeeDTO.setGender(gender);

        employeeDTO.setIsIndian(r.getBoolean("is_Indian"));
        employeeDTO.setBasicSalary(r.getBigDecimal("basic_salary"));
        employeeDTO.setPANNumber(r.getString("pan_number"));
        employeeDTO.setAadharCardNumber(aadharCardNumber);

        r.close();
        preparedStatement.close();
        c.close();
        return employeeDTO;

    }catch(Exception e)
    {
        throw new DAOException(e.getMessage());
    }
}

public boolean employeeIdExists(String employeeId) throws DAOException
{

    if(employeeId==null) return false;
    employeeId=employeeId.trim();
    if(employeeId.length()==0) return false;
    
    Connection c=ConnectionDAO.getConnection();
    PreparedStatement preparedStatement=null;
    ResultSet r=null;
    boolean bool=false;
    int sqlEmployeeId=Integer.parseInt(employeeId.substring(1,employeeId.length()))-100000;
    try
    {
        preparedStatement=c.prepareStatement("select gender from employee where employee_id=?");
        preparedStatement.setInt(1,sqlEmployeeId);
        r=preparedStatement.executeQuery();
        bool=r.next();
        r.close();
        preparedStatement.close();
        c.close();
    }catch(Exception e)
    {
        throw new DAOException(e.getMessage());
    }
    return bool;
}

public boolean panNumberExists(String panNumber) throws DAOException
{

    if(panNumber==null) return false;
    panNumber=panNumber.trim();
    if(panNumber.length()==0) return false;
   
    Connection c=ConnectionDAO.getConnection();
    PreparedStatement preparedStatement=null;
    ResultSet r=null;
    boolean bool;
    try
    {
        preparedStatement=c.prepareStatement("select * from employee where pan_number=?");
        preparedStatement.setString(1,panNumber);
        r=preparedStatement.executeQuery();
        bool=r.next();
        r.close();
        preparedStatement.close();
        c.close();
      
    }catch(Exception e)
    {
        throw new DAOException(e.getMessage());
    }
    return bool;

}

public boolean aadharCardNumberExists(String aadharCardNumber) throws DAOException
{

    
    if(aadharCardNumber==null) return false;
    aadharCardNumber=aadharCardNumber.trim();
    if(aadharCardNumber.length()==0) return false;

    Connection c=ConnectionDAO.getConnection();
    PreparedStatement preparedStatement=null;
    ResultSet r=null;
    boolean bool;
    try
    {
        preparedStatement=c.prepareStatement("select * from employee where aadhar_card_number=?");
        preparedStatement.setString(1,aadharCardNumber);
        r=preparedStatement.executeQuery();
        bool=r.next();
        r.close();
        preparedStatement.close();
        c.close();
      
    }catch(Exception e)
    {
        throw new DAOException(e.getMessage());
    }
    return bool;

}

public int getCount() throws DAOException
{
    Connection c=ConnectionDAO.getConnection();
    Statement statement=null;
    ResultSet r=null;
    int count=0;
    try
    {
        statement=c.createStatement();
        r=statement.executeQuery("select count(employee_id) from employee");
        
        r.next();
        count=r.getInt("count(employee_id)");
        r.close();
        statement.close();
        c.close();
    }catch(Exception e)
    {
        throw new DAOException(e.getMessage());
    }
    return count;
}

public int getCountByDesignation(int designationCode) throws DAOException
{

    Connection c=ConnectionDAO.getConnection();
    Statement statement=null;
    ResultSet r=null;
    int count=0;
    try
    {
        statement=c.createStatement();
        r=statement.executeQuery("select code from designation where code="+designationCode);
        if(r.next()==false) 
        {
            r.close();
            statement.close();
            c.close();
            return 0;
        }
        r.close();
        statement.close();
        statement=c.createStatement();
        r=statement.executeQuery("select count(employee_id) from employee where designation_code="+designationCode);
        
        r.next();
        count=r.getInt("count(employee_id)");
        r.close();
        statement.close();
        c.close();
    }catch(Exception e)
    {
        throw new DAOException(e.getMessage());
    }
    return count;
}

}