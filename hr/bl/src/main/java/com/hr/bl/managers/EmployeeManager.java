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
    private Map<Integer,Set<EmployeeInterface>> designationCodeWiseEmployeeMap;

    private static EmployeeManagerInterface employeeManager=null;

    private EmployeeManager() throws BLException
    {
        employeeIdWiseEmployeesMap=new HashMap<>();
        employeePANNumberWiseEmployeesMap=new HashMap<>();
        employeeAadharCardNumberWiseEmployeesMap=new HashMap<>();
        employeesSet=new TreeSet<>();
        designationCodeWiseEmployeeMap=new HashMap<>();
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
            //Map<Integer,Set<EmployeeInterface>> codeWiseMap;
            Set<EmployeeInterface> designationWiseEmployeesSet;
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

                this.employeeIdWiseEmployeesMap.put(employee.getEmployeeId().toUpperCase(),employee);
                this.employeePANNumberWiseEmployeesMap.put(employee.getPANNumber().toUpperCase(),employee);
                this.employeeAadharCardNumberWiseEmployeesMap.put(employee.getAadharCardNumber().toUpperCase(),employee);
                this.employeesSet.add(employee);
                designationWiseEmployeesSet=this.designationCodeWiseEmployeeMap.get(employeeDTO.getDesignationCode());
                if(designationWiseEmployeesSet==null)
                {
                    designationWiseEmployeesSet=new TreeSet<>();
                    designationWiseEmployeesSet.add(employee);
                    this.designationCodeWiseEmployeeMap.put(employeeDTO.getDesignationCode(),designationWiseEmployeesSet);
                }
                else
                {
                    designationWiseEmployeesSet.add(employee);
                }

            }
        }catch(DAOException daoe)
        {
            BLException blException=new BLException();
            blException.setGenericException(daoe.getMessage());
            throw blException;
        }

    }

    //...............................
    
	public void add(EmployeeInterface employee) throws BLException
	{
        BLException blException=new BLException();
        if(employee==null)
        {
            blException.setGenericException("employee is not instantiated");
            throw blException;
        }
        if(employee.getEmployeeId()!=null && employee.getEmployeeId().trim().length()!=0) blException.addException("employeeId","employee Id should be empty");
        if(employee.getName()==null || employee.getName().trim().length()==0) blException.addException("empoyee name","employee name should not be empty");
        if(employee.getDesignation()==null) blException.addException("designation","designation required");
        else
        {
            DesignationInterface designation=employee.getDesignation();
            DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
            if(designation.getCode()<=0) blException.addException("designationCode", "code should be greater then zero");
            else if(designationManager.designationCodeExists(designation.getCode())==false)
            {
             blException.addException("designation", "Designation ("+designation.getCode()+") code invalid");  
            }
        }
        if(employee.getDateOfBirth()==null) blException.addException("date of birth", "date of birth should not empty");
        if(employee.getGender()!='M' && employee.getGender()!='F') blException.addException("gender", "invalid gender, Valid (M/F)");
        if(employee.getBasicSalary()==null) blException.addException("basicSalary","salary should be written");
        else if(employee.getBasicSalary().signum()==-1) blException.addException("basicSalary","salary should be positive");
        if(employee.getPANNumber()==null || employee.getPANNumber().trim().length()==0)
        {
            blException.addException("panNumber", "pan number should not be empty");
        }
        else if(this.employeePANNumberWiseEmployeesMap.containsKey(employee.getPANNumber().toUpperCase()))
        {
            blException.addException("panNumber","pan number("+employee.getPANNumber()+") already exists");
        }

        if(employee.getAadharCardNumber()==null || employee.getAadharCardNumber().trim().length()==0)
        {
            blException.addException("aadharCardNumber","aadharCardNumber should not be empty");
        }
        else if(this.employeeAadharCardNumberWiseEmployeesMap.containsKey(employee.getAadharCardNumber().toUpperCase()))
        {
            blException.addException("aadharCardNumber","aadharCardNumber ("+employee.getAadharCardNumber()+") already exists");
        }

        if(blException.hasExceptions()) throw blException;
         
        try
        {
                // now sending employee data to dataLayer......
            EmployeeDAOInterface employeeDAO=new EmployeeDAO();
            EmployeeDTOInterface employeeDTO=new EmployeeDTO();
            employeeDTO.setName(employee.getName());
            employeeDTO.setDesignationCode(employee.getDesignation().getCode());
            employeeDTO.setDateOfBirth((Date)employee.getDateOfBirth().clone());
            employeeDTO.setGender(employee.getGender()=='m' || employee.getGender()=='M'?GENDER.MALE:GENDER.FEMALE);
            employeeDTO.setIsIndian(employee.getIsIndian());
            employeeDTO.setBasicSalary(employee.getBasicSalary());
            employeeDTO.setPANNumber(employee.getPANNumber());
            employeeDTO.setAadharCardNumber(employee.getAadharCardNumber());

            employeeDAO.add(employeeDTO);

            employee.setEmployeeId(employeeDTO.getEmployeeId());
                //data layer work done

                // now cloning employee object
            Employee dsEmployee=new Employee();
            dsEmployee.setEmployeeId(employee.getEmployeeId());
            dsEmployee.setName(employee.getName());

            DesignationManagerInterface designationManager=DesignationManager.getDesignationManager(); // original Designation object from DesignationManager
            dsEmployee.setDesignation(((DesignationManager) designationManager).getDSDesignationByCode(employee.getDesignation().getCode()));

            dsEmployee.setDateOfBirth((Date)employee.getDateOfBirth().clone());
            dsEmployee.setGender(employee.getGender()=='m' || employee.getGender()=='M'?GENDER.MALE:GENDER.FEMALE);
            dsEmployee.setIsIndian(employee.getIsIndian());
            dsEmployee.setBasicSalary(employee.getBasicSalary());
            dsEmployee.setPANNumber(employee.getPANNumber());
            dsEmployee.setAadharCardNumber(employee.getAadharCardNumber());
                // cloning done....

                // upadating data structure
            this.employeeIdWiseEmployeesMap.put(dsEmployee.getEmployeeId().toUpperCase(),dsEmployee);
            this.employeePANNumberWiseEmployeesMap.put(dsEmployee.getPANNumber().toUpperCase(),dsEmployee);
            this.employeeAadharCardNumberWiseEmployeesMap.put(dsEmployee.getAadharCardNumber().toUpperCase(),dsEmployee);
            this.employeesSet.add(dsEmployee);

            Set<EmployeeInterface> designationWiseEmployeesSet;
            designationWiseEmployeesSet=this.designationCodeWiseEmployeeMap.get(employeeDTO.getDesignationCode());
            if(designationWiseEmployeesSet==null)
            {
                designationWiseEmployeesSet=new TreeSet<>();
                designationWiseEmployeesSet.add(employee);
                this.designationCodeWiseEmployeeMap.put(employeeDTO.getDesignationCode(),designationWiseEmployeesSet);
            }
            else
            {
                designationWiseEmployeesSet.add(employee);
            }   
        }
        catch(DAOException daoe)
        {
            blException.setGenericException(daoe.getMessage());
            throw blException;
        }
   	}

    public void update(EmployeeInterface employee) throws BLException
    {


        BLException blException=new BLException();
        DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
        DesignationInterface designation=employee.getDesignation();
        EmployeeInterface dsEmployee;
        if(employee==null)
        {
            blException.setGenericException("employee is not instantiated");
            throw blException;
        }
        if(employee.getEmployeeId()==null && employee.getEmployeeId().trim().length()!=0) blException.addException("employeeId","employee Id should be empty");
        else
        {
            if(this.employeeIdWiseEmployeesMap.containsKey(employee.getEmployeeId().toUpperCase())==false)
            {
                blException.addException("employeeId", "employee id ("+employee.getEmployeeId()+") not exists, can not update");
                throw blException;
            }
        }

        
        if(employee.getName()==null || employee.getName().trim().length()==0) blException.addException("empoyee name","employee name should not be empty");
        if(designation==null) blException.addException("designation","designation required");
        else
        {
            
            if(designation.getCode()<=0) blException.addException("designationCode", "code should be greater then zero");
            else if(designationManager.designationCodeExists(designation.getCode())==false)
            {
             blException.addException("designation", "Designation ("+designation.getCode()+") code invalid");  
            }
        }
        if(employee.getDateOfBirth()==null) blException.addException("date of birth", "date of birth should not empty");
        if(employee.getGender()!='M' && employee.getGender()!='F') blException.addException("gender", "invalid gender, Valid (M/F)");
        if(employee.getBasicSalary()==null) blException.addException("basicSalary","salary should be written");
        else if(employee.getBasicSalary().signum()==-1) blException.addException("basicSalary","salary should be positive");
        if(employee.getPANNumber()==null || employee.getPANNumber().trim().length()==0)
        {
            blException.addException("panNumber", "pan number should not be empty");
        }
        else if(this.employeePANNumberWiseEmployeesMap.containsKey(employee.getPANNumber().toUpperCase()))
        {
            dsEmployee=this.employeePANNumberWiseEmployeesMap.get(employee.getPANNumber().toUpperCase());
            if(!(dsEmployee.getPANNumber().equalsIgnoreCase(employee.getPANNumber())))
            {
                blException.addException("panNumber","pan number("+employee.getPANNumber()+") already exists");
            }
            
        }

        if(employee.getAadharCardNumber()==null || employee.getAadharCardNumber().trim().length()==0)
        {
            blException.addException("aadharCardNumber", "aadhar card number should not be empty");
        }
        else if(this.employeeAadharCardNumberWiseEmployeesMap.containsKey(employee.getAadharCardNumber().toUpperCase()))
        {
            dsEmployee=this.employeeAadharCardNumberWiseEmployeesMap.get(employee.getAadharCardNumber().toUpperCase());
            if(!(dsEmployee.getAadharCardNumber().equalsIgnoreCase(employee.getAadharCardNumber())))
            {
                blException.addException("aadharCardNumber","aadhar card number("+employee.getAadharCardNumber()+") already exists");
            }
            
        }
    

        if(blException.hasExceptions()) throw blException;
         
        try
        {
                // now updating employee data to dataLayer......
            EmployeeDAOInterface employeeDAO=new EmployeeDAO();
            EmployeeDTOInterface employeeDTO=new EmployeeDTO();
            employeeDTO.setEmployeeId(employee.getEmployeeId());
            employeeDTO.setName(employee.getName());
            employeeDTO.setDesignationCode(employee.getDesignation().getCode());
            employeeDTO.setDateOfBirth((Date)employee.getDateOfBirth().clone());
            employeeDTO.setGender(employee.getGender()=='m' || employee.getGender()=='M'?GENDER.MALE:GENDER.FEMALE);
            employeeDTO.setIsIndian(employee.getIsIndian());
            employeeDTO.setBasicSalary(employee.getBasicSalary());
            employeeDTO.setPANNumber(employee.getPANNumber());
            employeeDTO.setAadharCardNumber(employee.getAadharCardNumber());

            employeeDAO.update(employeeDTO);

                //data layer work done

                //removing old employee from data structure
            dsEmployee=employeeIdWiseEmployeesMap.get(employee.getEmployeeId().toUpperCase());
            this.employeesSet.remove(dsEmployee);
            this.employeeIdWiseEmployeesMap.remove(dsEmployee.getEmployeeId().toUpperCase());
            this.employeePANNumberWiseEmployeesMap.remove(dsEmployee.getPANNumber().toUpperCase());
            this.employeeAadharCardNumberWiseEmployeesMap.remove(dsEmployee.getAadharCardNumber().toUpperCase());

            Set<EmployeeInterface> designationWiseEmployeesSet;
            designationWiseEmployeesSet=this.designationCodeWiseEmployeeMap.get(dsEmployee.getDesignation().getCode());
            designationWiseEmployeesSet.remove(employee);
          
            
                // now cloning employee object
            dsEmployee=new Employee();
            dsEmployee.setEmployeeId(employee.getEmployeeId());
            dsEmployee.setName(employee.getName());

            designationManager=DesignationManager.getDesignationManager(); // original Designation object from DesignationManager
            dsEmployee.setDesignation(((DesignationManager) designationManager).getDSDesignationByCode(employee.getDesignation().getCode()));

            dsEmployee.setDateOfBirth((Date)employee.getDateOfBirth().clone());
            dsEmployee.setGender(employee.getGender()=='m' || employee.getGender()=='M'?GENDER.MALE:GENDER.FEMALE);
            dsEmployee.setIsIndian(employee.getIsIndian());
            dsEmployee.setBasicSalary(employee.getBasicSalary());
            dsEmployee.setPANNumber(employee.getPANNumber());
            dsEmployee.setAadharCardNumber(employee.getAadharCardNumber());
                // cloning done....

                

                // upadating data structure
            this.employeeIdWiseEmployeesMap.put(dsEmployee.getEmployeeId().toUpperCase(),dsEmployee);
            this.employeePANNumberWiseEmployeesMap.put(dsEmployee.getPANNumber().toUpperCase(),dsEmployee);
            this.employeeAadharCardNumberWiseEmployeesMap.put(dsEmployee.getAadharCardNumber().toUpperCase(),dsEmployee);
            this.employeesSet.add(dsEmployee);

            designationWiseEmployeesSet=this.designationCodeWiseEmployeeMap.get(employeeDTO.getDesignationCode());
            if(designationWiseEmployeesSet==null)
            {
                designationWiseEmployeesSet=new TreeSet<>();
                designationWiseEmployeesSet.add(employee);
                this.designationCodeWiseEmployeeMap.put(employeeDTO.getDesignationCode(),designationWiseEmployeesSet);
            }
            else
            {
                designationWiseEmployeesSet.add(employee);
            }   
            
        }
        catch(DAOException daoe)
        {
            blException.setGenericException(daoe.getMessage());
            throw blException;
        }
   	}
    public void delete(String employeeId) throws BLException
    {
        BLException blException=new BLException();
        if(employeeId==null || employeeId.trim().length()==0)
        {
            blException.addException("employeeId","employee id should not be empty");
        }
        if(this.employeeIdWiseEmployeesMap.containsKey(employeeId.toUpperCase())==false)
        {
            blException.addException("employeeId","employee id ("+employeeId+") not exists");
            throw blException;
        }

        try
        {
            EmployeeDAOInterface employeeDAO=new EmployeeDAO();
            employeeDAO.delete(employeeId);

            EmployeeInterface employee=this.employeeIdWiseEmployeesMap.get(employeeId.toUpperCase());
            this.employeeIdWiseEmployeesMap.remove(employee.getEmployeeId().toUpperCase());
            this.employeePANNumberWiseEmployeesMap.remove(employee.getPANNumber().toUpperCase());
            this.employeeAadharCardNumberWiseEmployeesMap.remove(employee.getAadharCardNumber().toUpperCase());
            this.employeesSet.remove(employee);

            Set<EmployeeInterface> designationWiseEmployeesSet;
            designationWiseEmployeesSet=this.designationCodeWiseEmployeeMap.get(employee.getDesignation().getCode());
            designationWiseEmployeesSet.remove(employee);

        }
        catch(DAOException daoe)
        {
            blException.setGenericException(daoe.getMessage());
            throw blException;
        }


   	}
    public Set<EmployeeInterface> getAll()
    {
        Set<EmployeeInterface> employees=new TreeSet<>();
        EmployeeInterface employee;
        DesignationInterface designation;
        for(EmployeeInterface dsEmployee: this.employeesSet)
        {
            employee=new Employee();
            employee.setEmployeeId(dsEmployee.getEmployeeId());
            employee.setName(dsEmployee.getName());
            designation=new Designation();
            designation.setCode(dsEmployee.getDesignation().getCode());
            designation.setTitle(dsEmployee.getDesignation().getTitle());
            employee.setDesignation(designation);
            employee.setDateOfBirth((Date)dsEmployee.getDateOfBirth().clone());
            employee.setGender(dsEmployee.getGender()=='M' || dsEmployee.getGender()=='m'?GENDER.MALE:GENDER.FEMALE);
            employee.setBasicSalary(dsEmployee.getBasicSalary());
            employee.setIsIndian(dsEmployee.getIsIndian());
            employee.setPANNumber(dsEmployee.getPANNumber());
            employee.setAadharCardNumber(dsEmployee.getAadharCardNumber());
            
            employees.add(employee);
        }
        return employees;
   	}
    public Set<EmployeeInterface> getByDesignation(int designationCode)
    {
        Set<EmployeeInterface> designationWiseEmployeesSet;
        designationWiseEmployeesSet=this.designationCodeWiseEmployeeMap.get(designationCode);
        if(designationWiseEmployeesSet==null) return null;

        Set<EmployeeInterface> newDesignationWiseEmpoyeeSet=new TreeSet<>();
        EmployeeInterface employee;
        DesignationInterface designation;
        for(EmployeeInterface dsEmployee:designationWiseEmployeesSet)
        {
            employee=new Employee();

            employee.setEmployeeId(dsEmployee.getEmployeeId());
            employee.setName(dsEmployee.getName());
            designation=new Designation();
            designation.setCode(dsEmployee.getDesignation().getCode());
            designation.setTitle(dsEmployee.getDesignation().getTitle());
            employee.setDesignation(designation);
            employee.setDateOfBirth((Date)dsEmployee.getDateOfBirth().clone());
            employee.setGender(dsEmployee.getGender()=='M' || dsEmployee.getGender()=='m'?GENDER.MALE:GENDER.FEMALE);
            employee.setBasicSalary(dsEmployee.getBasicSalary());
            employee.setIsIndian(dsEmployee.getIsIndian());
            employee.setPANNumber(dsEmployee.getPANNumber());
            employee.setAadharCardNumber(dsEmployee.getAadharCardNumber());

            newDesignationWiseEmpoyeeSet.add(employee);
        }

        return newDesignationWiseEmpoyeeSet;
   	}
    public boolean isDesignationAlloted(int designationCode)
    {
        return this.designationCodeWiseEmployeeMap.containsKey(designationCode);
   	}
    public EmployeeInterface getByEmployeeId(String employeeId) throws BLException
    {
        EmployeeInterface dsEmployee=this.employeeIdWiseEmployeesMap.get(employeeId.toUpperCase());
        if(dsEmployee==null)
        {
            BLException blException=new BLException();
            blException.setGenericException("employee Id ("+employeeId+") not found");
            throw blException;
        }

        EmployeeInterface employee=new Employee();

        employee.setEmployeeId(dsEmployee.getEmployeeId());
        employee.setName(dsEmployee.getName());
        DesignationInterface designation=new Designation();
        designation.setCode(dsEmployee.getDesignation().getCode());
        designation.setTitle(dsEmployee.getDesignation().getTitle());
        employee.setDesignation(designation);
        employee.setDateOfBirth((Date)dsEmployee.getDateOfBirth().clone());
        employee.setGender(dsEmployee.getGender()=='M' || dsEmployee.getGender()=='m'?GENDER.MALE:GENDER.FEMALE);
        employee.setBasicSalary(dsEmployee.getBasicSalary());
        employee.setIsIndian(dsEmployee.getIsIndian());
        employee.setPANNumber(dsEmployee.getPANNumber());
        employee.setAadharCardNumber(dsEmployee.getAadharCardNumber());
        
        return employee;

   	}
    public EmployeeInterface getByPANNumber(String panNumber) throws BLException
    {
        EmployeeInterface dsEmployee=this.employeePANNumberWiseEmployeesMap.get(panNumber.toUpperCase());
        if(dsEmployee==null)
        {
            BLException blException=new BLException();
            blException.setGenericException("pan number ("+panNumber+") not found");
            throw blException;
        }

        EmployeeInterface employee=new Employee();

        employee.setEmployeeId(dsEmployee.getEmployeeId());
        employee.setName(dsEmployee.getName());
        DesignationInterface designation=new Designation();
        designation.setCode(dsEmployee.getDesignation().getCode());
        designation.setTitle(dsEmployee.getDesignation().getTitle());
        employee.setDesignation(designation);
        employee.setDateOfBirth((Date)dsEmployee.getDateOfBirth().clone());
        employee.setGender(dsEmployee.getGender()=='M' || dsEmployee.getGender()=='m'?GENDER.MALE:GENDER.FEMALE);
        employee.setBasicSalary(dsEmployee.getBasicSalary());
        employee.setIsIndian(dsEmployee.getIsIndian());
        employee.setPANNumber(dsEmployee.getPANNumber());
        employee.setAadharCardNumber(dsEmployee.getAadharCardNumber());
        
        return employee;
   	}
    public EmployeeInterface getByAadharCardNumber(String aadharCardNumber) throws BLException
    {
        
        EmployeeInterface dsEmployee=this.employeeAadharCardNumberWiseEmployeesMap.get(aadharCardNumber.toUpperCase());
        if(dsEmployee==null)
        {
            BLException blException=new BLException();
            blException.setGenericException("aadhar card number ("+aadharCardNumber+") not found");
            throw blException;
        }

        EmployeeInterface employee=new Employee();

        employee.setEmployeeId(dsEmployee.getEmployeeId());
        employee.setName(dsEmployee.getName());
        DesignationInterface designation=new Designation();
        designation.setCode(dsEmployee.getDesignation().getCode());
        designation.setTitle(dsEmployee.getDesignation().getTitle());
        employee.setDesignation(designation);
        employee.setDateOfBirth((Date)dsEmployee.getDateOfBirth().clone());
        employee.setGender(dsEmployee.getGender()=='M' || dsEmployee.getGender()=='m'?GENDER.MALE:GENDER.FEMALE);
        employee.setBasicSalary(dsEmployee.getBasicSalary());
        employee.setIsIndian(dsEmployee.getIsIndian());
        employee.setPANNumber(dsEmployee.getPANNumber());
        employee.setAadharCardNumber(dsEmployee.getAadharCardNumber());
        
        return employee;


   	}
    public boolean employeeIdExists(String employeeId) 
    {
        return this.employeeIdWiseEmployeesMap.containsKey(employeeId.toUpperCase());
   	}
    public boolean panNumberExists(String panNumber)
    {
        return this.employeePANNumberWiseEmployeesMap.containsKey(panNumber.toUpperCase());
   	}
    public boolean aadharCardNumberExists(String aadharCardNumber)
    {
        return this.employeeAadharCardNumberWiseEmployeesMap.containsKey(aadharCardNumber.toUpperCase());
   	}
    public int getCount()
    {
        return this.employeeIdWiseEmployeesMap.size();
   	}
    public int getCountByDesignation(int designationCode)
    {
        Set<EmployeeInterface> designationWiseEmployeesSet=this.designationCodeWiseEmployeeMap.get(designationCode);
        if(designationWiseEmployeesSet==null) return 0;
        return designationWiseEmployeesSet.size();
   	}
     


}