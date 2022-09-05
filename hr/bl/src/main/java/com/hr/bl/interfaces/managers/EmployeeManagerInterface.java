package com.hr.bl.interfaces.managers;
import com.hr.bl.exceptions.*;
import com.hr.bl.interfaces.pojo.*;
import java.util.*;


public interface EmployeeManagerInterface
{
    
    public void add(EmployeeInterface employeeInterface) throws BLException;
    public void update(EmployeeInterface employeeInterface) throws BLException;
    public void delete(String enployeeId) throws BLException;
    public Set<EmployeeInterface> getAll();
    public Set<EmployeeInterface> getByDesignation(int designationCode) throws BLException;
    public boolean isDesignationAlloted(int designationCode) throws BLException;
    public EmployeeInterface getByEmployeeId(String employeeId) throws BLException;
    public EmployeeInterface getByPANNumber(String panNumber) throws BLException;
    public EmployeeInterface getByAadharCardNumber(String aadharCardNumber) throws BLException;
    public boolean employeeIdExists(String employeeId);
    public boolean panNumberExists(String panNumber);
    public boolean aadharCardNumberExists(String aadharCardNumber);
    public int getCount();
    public int getCountByDesignation(int designationCode) throws BLException;
    

}

