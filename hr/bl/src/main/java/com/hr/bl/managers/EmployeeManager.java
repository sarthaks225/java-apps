package com.hr.bl.managers;
import com.hr.bl.interfaces.managers.*;
import com.hr.bl.pojo.*;
import com.hr.bl.managers.*;
import com.hr.bl.exceptions.*;
import com.hr.bl.interfaces.pojo.*;

import com.hr.dl.exceptions.*;
import com.hr.dl.interfaces.dto.*;
import com.hr.dl.interfaces.dao.*;
import com.hr.dl.dao.*;
import com.hr.dl.dto.*;


import java.util.*;
import java.math.*;
import com.hr.common.enums.*;

public class EmployeeManager implements EmployeeManagerInterface
{
    //....................................

    private Map<String,EmployeeInterface> employeeIdWiseEmployeesMap;
    private Map<String,EmployeeInterface> employeePANNumberWiseEmployeesMap;
    private Map<String,EmployeeInterface> employeeAadharCardNumberWiseEmployeesMap;
    private Set<EmployeeInterface> employeesSet;

    private static EmployeeManagerInterface employeeManager=null;

    private EmployeeManager() throws BLException
    {
        employeeIdWiseEmployeesMap=new HashMap<>();
        employeePANNumberWiseEmployeesMap=new HashMap<>();
        employeeAadharCardNumberWiseEmployeesMap=new HashMap<>();
        employeesSet=new TreeSet<>();
        populateDataStructure();
    }

    public static EmployeeManagerInterface getEmployeeManager() throws BLException
    {
        if(employeeManager==null) employeeManager=new EmployeeManager();
        return employeeManager;
    }

    private void populateDataStructure() throws BLException
    {
        try
        {
            EmployeeDAOInterface employeeDAO=new EmployeeDAO();
            Set<EmployeeDTOInterface> employees=employeeDAO.getAll();
            EmployeeInterface employee;

            DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();

            for(EmployeeDTOInterface employeeDTO: employees)
            {
                employee=new Employee();
                employee.setEmployeeId(employeeDTO.getEmployeeId());
                employee.setName(employeeDTO.getName());
                employee.setDesignation(designationManager.getDesignationByCode(employeeDTO.getDesignationCode()));
                employee.setDateOfBirth(employeeDTO.getDateOfBirth());
                employee.setGender((employeeDTO.getGender()=='m'||employeeDTO.getGender()=='M')?GENDER.MALE:GENDER.FEMALE);
                employee.setIsIndian(employeeDTO.getIsIndian());
                employee.setBasicSalary(employeeDTO.getBasicSalary());
                employee.setPANNumber(employeeDTO.getPANNumber());
                employee.setAadharCardNumber(employeeDTO.getAadharCardNumber());

                this.employeeIdWiseEmployeesMap.put(employee.getEmployeeId(),employee);
                this.employeePANNumberWiseEmployeesMap.put(employee.getPANNumber(),employee);
                this.employeeAadharCardNumberWiseEmployeesMap.put(employee.getAadharCardNumber(),employee);
                this.employeesSet.add(employee);
            }
        }catch(DAOException daoe)
        {
            BLException blException=new BLException();
            blException.setGenericException(daoe.getMessage());
            throw blException;
        }

    }

    //...............................
    
	public void add(EmployeeInterface employeeInterface) throws BLException
	{
        BLException blException=new BLException();
        blException.setGenericException("Yet to implement");
		throw blException;
   	}
    public void update(EmployeeInterface employeeInterface) throws BLException
    {
        BLException blException=new BLException();
        blException.setGenericException("Yet to implement");
		throw blException;
   	}
    public void delete(String enployeeId) throws BLException
    {
        BLException blException=new BLException();
        blException.setGenericException("Yet to implement");
		throw blException;
   	}
    public Set<EmployeeInterface> getAll() throws BLException
    {
        BLException blException=new BLException();
        blException.setGenericException("Yet to implement");
		throw blException;
   	}
    public Set<EmployeeInterface> getByDesignation(int designationCode) throws BLException
    {
        BLException blException=new BLException();
        blException.setGenericException("Yet to implement");
		throw blException;
   	}
    public boolean isDesignationAlloted(int designationCode) throws BLException
    {
        BLException blException=new BLException();
        blException.setGenericException("Yet to implement");
		throw blException;
   	}
    public EmployeeInterface getByEmployeeId(String employeeId) throws BLException
    {
        BLException blException=new BLException();
        blException.setGenericException("Yet to implement");
		throw blException;
   	}
    public EmployeeInterface getByPANNumber(String panNumber) throws BLException
    {
        BLException blException=new BLException();
        blException.setGenericException("Yet to implement");
		throw blException;
   	}
    public EmployeeInterface getByAadharCardNumber(String aadharCardNumber) throws BLException
    {
        BLException blException=new BLException();
        blException.setGenericException("Yet to implement");
		throw blException;
   	}
    public boolean employeeIdExists(String employeeId) throws BLException
    {
        BLException blException=new BLException();
        blException.setGenericException("Yet to implement");
		throw blException;
   	}
    public boolean panNumberExists(String panNumber) throws BLException
    {
        BLException blException=new BLException();
        blException.setGenericException("Yet to implement");
		throw blException;
   	}
    public boolean aadharCardNumberExists(String aadharCardNumber) throws BLException
    {
        BLException blException=new BLException();
        blException.setGenericException("Yet to implement");
		throw blException;
   	}
    public int getCount() throws BLException
    {
        BLException blException=new BLException();
        blException.setGenericException("Yet to implement");
		throw blException;
   	}
    public int getCountByDesignation(int designationCode) throws BLException
    {
        BLException blException=new BLException();
        blException.setGenericException("Yet to implement");
		throw blException;
   	}
     


}