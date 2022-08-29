package com.hr.dl.interfaces.dao;
import com.hr.dl.exceptions.*;
import com.hr.dl.interfaces.dto.*;
import java.util.*;

public interface EmployeeDAOInterface
{
public void add(EmployeeDTOInterface employeeDTOInterface) throws DAOException;
public void update(EmployeeDTOInterface employeeDTOInterface) throws DAOException;
public void delete(EmployeeDTOInterface employeeDTOInterface) throws DAOException;
public Set<EmployeeDTOInterface> getAll() throws DAOException;
public Set<EmployeeDTOInterface> getByDesignation(int designationCode) throws DAOException;
public boolean isDesignationAlloted(int designationCode) throws DAOException;
public EmployeeDTOInterface getByEmployeeId(String employeeId) throws DAOException;
public EmployeeDTOInterface getByPANNumber(String panNumber) throws DAOException;
public EmployeeDTOInterface getByAadharCardNumber(String aadharCardNumber) throws DAOException;
public boolean employeeIdExists(String employeeId) throws DAOException;
public boolean panNumberExists(String panNumber) throws DAOException;
public boolean aadharCardNumberExists(String aadharCardNumber) throws DAOException;
public int getCount() throws DAOException;
public int getCountByDesignation(int designationCode) throws DAOException;
}