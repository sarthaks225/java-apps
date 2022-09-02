package com.hr.dl.interfaces.dao;
import com.hr.dl.interfaces.dto.*;
import com.hr.dl.exceptions.*;
import java.util.*;
public interface DesignationDAOInterface
{
public void add(DesignationDTOInterface designationDTOInterface) throws DAOException;
public void update(DesignationDTOInterface designationDTOInterface) throws DAOException;
public void delete(int code) throws DAOException;
public Set<DesignationDTOInterface> getAll() throws DAOException;
public DesignationDTOInterface getByCode(int code) throws DAOException;
public DesignationDTOInterface getByTitle(String string) throws DAOException;
public boolean codeExists(int code) throws DAOException;
public boolean titleExists(String string) throws DAOException;
public int getCount() throws DAOException;
};