package com.hr.dl.dao;
import com.hr.dl.exceptions.*;
import com.hr.dl.interfaces.dao.*;
import com.hr.dl.interfaces.dto.*;
import com.hr.dl.dao.*;
import com.hr.dl.dto.*;
import java.text.*;
import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.zip.DataFormatException;
public class EmployeeDAO implements EmployeeDAOInterface
{
private static String fileName="employee.data";
public void add(EmployeeDTOInterface employeeDTO) throws DAOException
{
    if(employeeDTO==null) throw new DAOException("error: employee is null");
    String employeeId;
    String name=employeeDTO.getName();
    if(name==null || name.length()==0) throw new DAOException("error: employee name is null");
    int designationCode=employeeDTO.getDesignationCode();
    if(designationCode<=0) throw new DAOException("error: designation code is invalid: "+designationCode);
    DesignationDAOInterface designationDAO=new DesignationDAO();
    if(!(designationDAO.codeExists(designationCode))) throw new DAOException("error code does not exist :"+designationCode);
    Date dateOfBirth=employeeDTO.getDateOfBirth();
    if(dateOfBirth==null) throw new DAOException("error: Date is null");
    char gender=employeeDTO.getGender();
    boolean isIndian=employeeDTO.getIsIndian();
    BigDecimal basicSalary=employeeDTO.getBasicSalary();
    if(basicSalary.signum()==-1) throw new DAOException("error: salary can not be negative");
    String panNumber=employeeDTO.getPANNumber().trim();
    if(panNumber==null || panNumber.length()==0) throw new DAOException("error: PAN Number is null");
    String aadharCardNumber=employeeDTO.getAadharCardNumber();
    if(aadharCardNumber==null || aadharCardNumber.length()==0) throw new DAOException("error: Aadhar Card Number is invalid");
    try
    {
        int lastGeneratedEmployeeId=0;
        String lastGeneratedEmployeeIdString="";
        int recordCount=0;
        String recordCountString="";
        File file=new File(fileName);
        RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
        SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
        if(randomAccessFile.length()==0)
        {
            lastGeneratedEmployeeIdString=String.format("%-10s","10000001");
            randomAccessFile.writeBytes(lastGeneratedEmployeeIdString+"\n");
            recordCountString=String.format("%-10s","1");
            randomAccessFile.writeBytes(recordCountString+"\n");
            employeeId="A"+lastGeneratedEmployeeIdString;
            randomAccessFile.writeBytes(employeeId+"\n");
            randomAccessFile.writeBytes(name+"\n");
            randomAccessFile.writeBytes(designationCode+"\n");
            randomAccessFile.writeBytes(sdf.format(dateOfBirth)+"\n");
            randomAccessFile.writeBytes(gender+"\n");
            randomAccessFile.writeBytes(isIndian+"\n");
            randomAccessFile.writeBytes(basicSalary.toPlainString()+"\n");
            randomAccessFile.writeBytes(panNumber+"\n");
            randomAccessFile.writeBytes(aadharCardNumber+"\n");
            employeeDTO.setEmployeeId(employeeId);
        }
        else
        {
            lastGeneratedEmployeeId=Integer.parseInt(randomAccessFile.readLine().trim());
            recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
        

            boolean boolPANNumberFound=false;
            boolean boolAadharCardNumberFound=false;
            String fPANNumber="";
            String fAadharCardNumber="";
            int x;
            while(randomAccessFile.length()>randomAccessFile.getFilePointer())
            {
                for(x=0; x<7; ++x) randomAccessFile.readLine();
                fPANNumber=randomAccessFile.readLine().trim();
                fAadharCardNumber=randomAccessFile.readLine().trim();
                if(fPANNumber.equalsIgnoreCase(panNumber)) boolPANNumberFound=true;
                if(fAadharCardNumber.equalsIgnoreCase(aadharCardNumber)) boolAadharCardNumberFound=true;
                if(boolPANNumberFound && boolAadharCardNumberFound) break;
            }
            if(boolPANNumberFound && boolAadharCardNumberFound) 
            {
                randomAccessFile.close();
                throw new DAOException("can not add employee. PAN Number ("+fPANNumber+") and Aadhar Card Number ("+aadharCardNumber+"). They already Exists");
            }
            else if(boolPANNumberFound)
            {
                randomAccessFile.close();
                throw new DAOException("can not add employee. PAN Number ("+panNumber+") already exists");
            }else if(boolAadharCardNumberFound)
            {
                randomAccessFile.close();
                throw new DAOException("can not add employee. Aadhar Card Number ("+aadharCardNumber+") already exists");
            }
            employeeId="A"+Integer.toString(lastGeneratedEmployeeId+1);
            randomAccessFile.writeBytes(employeeId+"\n");
            randomAccessFile.writeBytes(name+"\n");
            randomAccessFile.writeBytes(designationCode+"\n");
            randomAccessFile.writeBytes(sdf.format(dateOfBirth)+"\n");
            randomAccessFile.writeBytes(gender+"\n");
            randomAccessFile.writeBytes(isIndian+"\n");
            randomAccessFile.writeBytes(basicSalary.toPlainString()+"\n");
            randomAccessFile.writeBytes(panNumber+"\n");
            randomAccessFile.writeBytes(aadharCardNumber+"\n");
            
            randomAccessFile.seek(0);
            lastGeneratedEmployeeIdString=String.format("%-10s",lastGeneratedEmployeeId+1);
            randomAccessFile.writeBytes(lastGeneratedEmployeeIdString+"\n");
            recordCountString=String.format("%-10s",recordCount+1);
            randomAccessFile.writeBytes(recordCountString+"\n");
            employeeDTO.setEmployeeId(employeeId);
        }
        
        randomAccessFile.close();
    }catch(IOException ioe)
    {

        throw new DAOException(ioe.getMessage());

    }


}
public void update(EmployeeDTOInterface employeeDTO) throws DAOException
{

    if(employeeDTO==null) throw new DAOException("error: employee is null");

    String employeeId=employeeDTO.getEmployeeId().trim();
    if(employeeId==null || employeeId.length()==0) throw new DAOException("error: invalid employee ID can not update");
    String name=employeeDTO.getName();
    if(name==null || name.length()==0) throw new DAOException("error: employee name is null can not update");
    int designationCode=employeeDTO.getDesignationCode();
    if(designationCode<=0) throw new DAOException("error: designation code is invalid: "+designationCode);
    DesignationDAOInterface designationDAO=new DesignationDAO();
    if(!(designationDAO.codeExists(designationCode))) throw new DAOException("error code does not exist :"+designationCode);
    Date dateOfBirth=employeeDTO.getDateOfBirth();
    if(dateOfBirth==null) throw new DAOException("error: Date is null");
    char gender=employeeDTO.getGender();
    boolean isIndian=employeeDTO.getIsIndian();
    BigDecimal basicSalary=employeeDTO.getBasicSalary();
    if(basicSalary.signum()==-1) throw new DAOException("error: salary can not be negative");
    String panNumber=employeeDTO.getPANNumber().trim();
    if(panNumber==null || panNumber.length()==0) throw new DAOException("error: PAN Number is null");
    String aadharCardNumber=employeeDTO.getAadharCardNumber();
    if(aadharCardNumber==null || aadharCardNumber.length()==0) throw new DAOException("error: Aadhar Card Number is invalid");
    try
    {
        int lastGeneratedEmployeeId=0;
        String lastGeneratedEmployeeIdString="";
        int recordCount=0;
        String recordCountString="";
        File file=new File(fileName);
        RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
        SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
        if(randomAccessFile.length()==0)
        {
            lastGeneratedEmployeeIdString=String.format("%-10s","10000001");
            randomAccessFile.writeBytes(lastGeneratedEmployeeIdString+"\n");
            recordCountString=String.format("%-10s","1");
            randomAccessFile.writeBytes(recordCountString+"\n");
            employeeId="A"+lastGeneratedEmployeeIdString;
            randomAccessFile.writeBytes(employeeId+"\n");
            randomAccessFile.writeBytes(name+"\n");
            randomAccessFile.writeBytes(designationCode+"\n");
            randomAccessFile.writeBytes(sdf.format(dateOfBirth)+"\n");
            randomAccessFile.writeBytes(gender+"\n");
            randomAccessFile.writeBytes(isIndian+"\n");
            randomAccessFile.writeBytes(basicSalary.toPlainString()+"\n");
            randomAccessFile.writeBytes(panNumber+"\n");
            randomAccessFile.writeBytes(aadharCardNumber+"\n");
            employeeDTO.setEmployeeId(employeeId);
        }
        else
        {
            lastGeneratedEmployeeId=Integer.parseInt(randomAccessFile.readLine().trim());
            recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
        

            boolean boolPANNumberFound=false;
            boolean boolAadharCardNumberFound=false;
            boolean boolEmployeeIdFound=false;
            String fPANNumber="";
            String fAadharCardNumber="";
            String fEmployeeId="";
            long updatePointerPosition=0;
            int x;
            while(randomAccessFile.length()>randomAccessFile.getFilePointer())
            {
                if(!boolEmployeeIdFound)
                {
                    
                    fEmployeeId=randomAccessFile.readLine().trim();
                    if(employeeId.equalsIgnoreCase(fEmployeeId))
                    {
                        updatePointerPosition=randomAccessFile.getFilePointer();
                        boolEmployeeIdFound=true;
                        for(x=0; x<8; ++x) randomAccessFile.readLine();
                        continue;
                    }
                }
                else randomAccessFile.readLine().trim();
                for(x=0; x<6; ++x) randomAccessFile.readLine();
                fPANNumber=randomAccessFile.readLine().trim();
                fAadharCardNumber=randomAccessFile.readLine().trim();
                if(fPANNumber.equalsIgnoreCase(panNumber)) boolPANNumberFound=true;
                if(fAadharCardNumber.equalsIgnoreCase(aadharCardNumber)) boolAadharCardNumberFound=true;
                if(boolPANNumberFound && boolAadharCardNumberFound) break;
            }
            
            if(boolPANNumberFound && boolAadharCardNumberFound)
            {
                randomAccessFile.close();
                throw new DAOException("can not update employee. PAN Number ("+fPANNumber+") and Aadhar Card Number ("+aadharCardNumber+"). They already Exists");
            }
            else if(boolPANNumberFound)
            {
                randomAccessFile.close();
                throw new DAOException("can not update employee. PAN Number ("+panNumber+") already exists");
            }else if(boolAadharCardNumberFound)
            {
                randomAccessFile.close();
                throw new DAOException("can not update employee. Aadhar Card Number ("+aadharCardNumber+") already exists");
            }
            if(boolEmployeeIdFound==false) throw new DAOException("error: employee Id not found");

            File tmpFile=new File("tmpEmployee.data");   //copying to tmpEmployee.data
            if(tmpFile.exists()) tmpFile.delete();
            RandomAccessFile tmpRandomAccessFile=new RandomAccessFile(tmpFile,"rw");
    
            tmpRandomAccessFile.writeBytes(name+"\n");
            tmpRandomAccessFile.writeBytes(designationCode+"\n");
            tmpRandomAccessFile.writeBytes(sdf.format(dateOfBirth)+"\n");
            tmpRandomAccessFile.writeBytes(gender+"\n");
            tmpRandomAccessFile.writeBytes(isIndian+"\n");
            tmpRandomAccessFile.writeBytes(basicSalary.toPlainString()+"\n");
            tmpRandomAccessFile.writeBytes(panNumber+"\n");
            tmpRandomAccessFile.writeBytes(aadharCardNumber+"\n");

            randomAccessFile.seek(updatePointerPosition);
            for(x=0; x<8; ++x) randomAccessFile.readLine();
            while(randomAccessFile.getFilePointer()<randomAccessFile.length())
            {
                tmpRandomAccessFile.writeBytes(randomAccessFile.readLine()+"\n");

            }

            randomAccessFile.setLength(updatePointerPosition);  // updation in original file
            randomAccessFile.seek(updatePointerPosition);
            tmpRandomAccessFile.seek(0);
            while(tmpRandomAccessFile.length()>tmpRandomAccessFile.getFilePointer())
            {
                randomAccessFile.writeBytes(tmpRandomAccessFile.readLine()+"\n");
            }
            tmpFile.delete();
            tmpRandomAccessFile.close();
        }
        
        randomAccessFile.close();
      
    }catch(IOException ioe)
    {

        throw new DAOException(ioe.getMessage());

    }
}


public void delete(EmployeeDTOInterface employeeDTOInterface) throws DAOException
{
throw new DAOException("Not yet implemented");
}

public Set<EmployeeDTOInterface> getAll() throws DAOException
{
 Set<EmployeeDTOInterface> employees=new TreeSet<>();
 try
 {
    File file=new File(fileName);
    if(!file.exists()) return employees;
    RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
    if(randomAccessFile.length()==0)
    {
        randomAccessFile.close();
        return employees;
    }

    randomAccessFile.readLine();
    randomAccessFile.readLine();
    EmployeeDTOInterface employeeDTO;
    SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
    Date date=new Date();
    String test;
    while(randomAccessFile.length()>randomAccessFile.getFilePointer())
    {
        employeeDTO=new EmployeeDTO();
        employeeDTO.setEmployeeId(randomAccessFile.readLine().trim());
        employeeDTO.setName(randomAccessFile.readLine().trim());
        employeeDTO.setDesignationCode(Integer.parseInt(randomAccessFile.readLine().trim()));
        try
        {
            date=sdf.parse(randomAccessFile.readLine());
            employeeDTO.setDateOfBirth(date);
        }catch(ParseException pe)
        {
            randomAccessFile.close();
            throw new DAOException("error: "+pe.getMessage());
        }
        employeeDTO.setGender(randomAccessFile.readLine().charAt(0));
        employeeDTO.setIsIndian(Boolean.parseBoolean(randomAccessFile.readLine()));
        employeeDTO.setBasicSalary(new BigDecimal(randomAccessFile.readLine()));
        employeeDTO.setPANNumber(randomAccessFile.readLine());
        employeeDTO.setAadharCardNumber(randomAccessFile.readLine());
        employees.add(employeeDTO);
    }
    randomAccessFile.close();
    return employees;
    
 }catch(IOException ioe)
 {
    throw new DAOException(ioe.getMessage());
 }
}

public Set<EmployeeDTOInterface> getByDesignation(int designationCode) throws DAOException
{
throw new DAOException("Not yet implemented");
}

public boolean isDesignationAlloted(int designationCode) throws DAOException
{
throw new DAOException("Not yet implemented");
}

public EmployeeDTOInterface getByEmployeeId(String employeeId) throws DAOException
{
throw new DAOException("Not yet implemented");
}

public EmployeeDTOInterface getByPANNumber(String panNumber) throws DAOException
{
throw new DAOException("Not yet implemented");
}

public EmployeeDTOInterface getByAadharCardNumber(String aadharCardNumber) throws DAOException
{
throw new DAOException("Not yet implemented");
}

public boolean employeeIdExists(int employeeId) throws DAOException
{
throw new DAOException("Not yet implemented");
}

public boolean panNumberExists(String panNumber) throws DAOException
{
throw new DAOException("Not yet implemented");
}

public boolean aadharCardNumberExists(int aadharCardNumber) throws DAOException
{
throw new DAOException("Not yet implemented");
}

public int getCount() throws DAOException
{
throw new DAOException("Not yet implemented");
}

public int getCountByDesignation() throws DAOException
{
throw new DAOException("Not yet implemented");
}

}