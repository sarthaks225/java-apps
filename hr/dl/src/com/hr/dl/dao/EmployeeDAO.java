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
            employeeId="A"+"10000001";
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
                if(boolPANNumberFound==false && fPANNumber.equalsIgnoreCase(panNumber)) boolPANNumberFound=true;
                if(boolAadharCardNumberFound==false && fAadharCardNumber.equalsIgnoreCase(aadharCardNumber)) boolAadharCardNumberFound=true;
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
                throw new DAOException("...1111.... can not add employee. Aadhar Card Number ("+aadharCardNumber+") already exists");
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
                if(boolPANNumberFound==false && fPANNumber.equalsIgnoreCase(panNumber)) boolPANNumberFound=true;
                if(boolAadharCardNumberFound==false && fAadharCardNumber.equalsIgnoreCase(aadharCardNumber)) boolAadharCardNumberFound=true;
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
            if(tmpFile.exists()) tmpFile.delete();
            tmpFile.delete();
            tmpRandomAccessFile.close();
        }
        
        randomAccessFile.close();
      
    }catch(IOException ioe)
    {

        throw new DAOException(ioe.getMessage());

    }
}


public void delete(String employeeId) throws DAOException
{
    if(employeeId==null) throw new DAOException("error: employee Id is null");

    employeeId=employeeId.trim();
    if(employeeId.length()==0) throw new DAOException("error: invalid employee ID can not delete");
    try
    {
        int recordCount=0;
        File file=new File(fileName);
        RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
        if(randomAccessFile.length()==0)
        {
            randomAccessFile.close();
            throw new DAOException("errror : can not delete employee Id ("+employeeId+")");
        }
        else
        {
            randomAccessFile.readLine();

            recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
            long updatePointerPosition=0;
            Boolean boolEmployeeIdFound=false;
            String fEmployeeId;
            int x;
            while(randomAccessFile.length()>randomAccessFile.getFilePointer())
            {
                    updatePointerPosition=randomAccessFile.getFilePointer();
                    fEmployeeId=randomAccessFile.readLine().trim();
                    if(employeeId.equalsIgnoreCase(fEmployeeId))
                    {    
                        boolEmployeeIdFound=true;
                        break;
                    }
                for(x=0; x<8; ++x) randomAccessFile.readLine();
            }
            
            if(boolEmployeeIdFound==false) throw new DAOException("error: employee Id not found: ("+employeeId+")");

            File tmpFile=new File("tmpEmployee.data");   //copying to tmpEmployee.data
            if(tmpFile.exists()) tmpFile.delete();
            RandomAccessFile tmpRandomAccessFile=new RandomAccessFile(tmpFile,"rw");
    
            randomAccessFile.seek(updatePointerPosition);
            for(x=0; x<9; ++x) randomAccessFile.readLine();
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
            randomAccessFile.seek(0);
            randomAccessFile.readLine();
            randomAccessFile.writeBytes(String.format("%-10s",Integer.toString(recordCount-=1))+"\n");
            tmpFile.delete();
            tmpRandomAccessFile.close();
        }
        
        randomAccessFile.close();
      
    }catch(IOException ioe)
    {

        throw new DAOException(ioe.getMessage());

    }



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
        char gender=randomAccessFile.readLine().charAt(0);
        if(gender=='M' || gender=='m') employeeDTO.setGender(GENDER.MALE);
        else employeeDTO.setGender(GENDER.FEMALE);
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

 DesignationDAOInterface designationDAO=new DesignationDAO();
 if(!designationDAO.codeExists(designationCode)) throw new DAOException("error designation ("+designationCode+") not exist");
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
    String fEmployeeId;
    String fName;
    int fDesignationCode,x;
    while(randomAccessFile.length()>randomAccessFile.getFilePointer())
    {

        fEmployeeId=randomAccessFile.readLine().trim();
        
        fName=randomAccessFile.readLine().trim();
        fDesignationCode=Integer.parseInt(randomAccessFile.readLine().trim());
        
        if(designationCode!=fDesignationCode) 
        {
            for(x=0; x<6; ++x) randomAccessFile.readLine();
            continue;
        }
        employeeDTO=new EmployeeDTO();
        employeeDTO.setEmployeeId(fEmployeeId.trim());
        employeeDTO.setName(fName);
        employeeDTO.setDesignationCode(fDesignationCode);
        try
        {
            date=sdf.parse(randomAccessFile.readLine());
            employeeDTO.setDateOfBirth(date);
        }catch(ParseException pe)
        {
            randomAccessFile.close();
            throw new DAOException("error: "+pe.getMessage());
        }

        char gender=randomAccessFile.readLine().charAt(0);
        if(gender=='M' || gender=='m') employeeDTO.setGender(GENDER.MALE);
        else employeeDTO.setGender(GENDER.FEMALE);
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

public boolean isDesignationAlloted(int designationCode) throws DAOException
{
    DesignationDAOInterface designationDAO=new DesignationDAO();
    if(!designationDAO.codeExists(designationCode)) return false;
    try
    {
        File file=new File(fileName);
        if(file.exists()==false) return false;
        RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
        if(randomAccessFile.length()==0) return false;
        randomAccessFile.readLine();
        randomAccessFile.readLine();
        int x;
        while(randomAccessFile.length()>randomAccessFile.getFilePointer())
        {
            randomAccessFile.readLine();
            randomAccessFile.readLine();
            if(designationCode==Integer.parseInt(randomAccessFile.readLine().trim())) 
            {
                randomAccessFile.close();
                return true;
            }
            for(x=0; x<6; ++x)  randomAccessFile.readLine();

        }
        randomAccessFile.close();
        return false;
    }catch(IOException ioe)
    {
        throw new DAOException(ioe.getMessage());
    }


}

public EmployeeDTOInterface getByEmployeeId(String employeeId) throws DAOException
{
    if(employeeId==null) throw new DAOException("error: invalid employee id");
    employeeId=employeeId.trim();
    if(employeeId.length()==0) throw new DAOException("error: employee id length is zero");

    try
    {
        File file=new File(fileName);
        if(file.exists()==false) throw new DAOException("error: not single employee added");
        RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
        if(randomAccessFile.length()==0) 
        {
            randomAccessFile.close();
            throw new DAOException("error: not single employee added");
        }
        randomAccessFile.readLine();
        if(Integer.parseInt(randomAccessFile.readLine().trim())==0)
        {
            randomAccessFile.close();
            throw new DAOException("error: not single employee added");
        }

        EmployeeDTOInterface employeeDTO;
        String fEmployeeId;
        SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
        while(randomAccessFile.length()>randomAccessFile.getFilePointer())
        {
            fEmployeeId=randomAccessFile.readLine().trim();
            if(fEmployeeId.equalsIgnoreCase(employeeId))
            {
                employeeDTO=new EmployeeDTO();
                employeeDTO.setEmployeeId(fEmployeeId);
                employeeDTO.setName(randomAccessFile.readLine());
                employeeDTO.setDesignationCode(Integer.parseInt(randomAccessFile.readLine().trim()));
                employeeDTO.setDateOfBirth(sdf.parse(randomAccessFile.readLine()));
                employeeDTO.setGender(randomAccessFile.readLine().charAt(0)=='M'?GENDER.MALE:GENDER.FEMALE);
                employeeDTO.setIsIndian(Boolean.parseBoolean(randomAccessFile.readLine()));
                employeeDTO.setBasicSalary(new BigDecimal(randomAccessFile.readLine().trim()));
                employeeDTO.setPANNumber(randomAccessFile.readLine());
                employeeDTO.setAadharCardNumber(randomAccessFile.readLine());
                randomAccessFile.close();
                return employeeDTO;
            }
            for(int x=0; x<8; ++x) randomAccessFile.readLine();
        }


        randomAccessFile.close();
        throw new DAOException("error: invalid emoloyee id ("+employeeId+")");

    }catch(IOException ioe)
    {
        throw new DAOException(ioe.getMessage());
    }catch(ParseException pe)
    {
        throw new DAOException(pe.getMessage());
    }
}

public EmployeeDTOInterface getByPANNumber(String panNumber) throws DAOException
{
    if(panNumber==null) throw new DAOException("error: invalid pan number");
    panNumber=panNumber.trim();
    if(panNumber.length()==0) throw new DAOException("error: pan number length is zero");

    try
    {
        File file=new File(fileName);
        if(file.exists()==false) throw new DAOException("error: not single employee added");
        RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
        if(randomAccessFile.length()==0) 
        {
            randomAccessFile.close();
            throw new DAOException("error: not single employee added");
        }
        randomAccessFile.readLine();
        if(Integer.parseInt(randomAccessFile.readLine().trim())==0)
        {
            randomAccessFile.close();
            throw new DAOException("error: not single employee added");
        }

        EmployeeDTOInterface employeeDTO;
        String fPANNumber;
        long fPointerPosition;
        SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
        while(randomAccessFile.length()>randomAccessFile.getFilePointer())
        {
            fPointerPosition=randomAccessFile.getFilePointer();
            for(int x=0; x<7; ++x) randomAccessFile.readLine();
            fPANNumber=randomAccessFile.readLine().trim();
            if(fPANNumber.equalsIgnoreCase(panNumber))
            {
                randomAccessFile.seek(fPointerPosition);
                employeeDTO=new EmployeeDTO();
                employeeDTO.setEmployeeId(randomAccessFile.readLine().trim());
                employeeDTO.setName(randomAccessFile.readLine());
                employeeDTO.setDesignationCode(Integer.parseInt(randomAccessFile.readLine().trim()));
                employeeDTO.setDateOfBirth(sdf.parse(randomAccessFile.readLine()));
                employeeDTO.setGender(randomAccessFile.readLine().charAt(0)=='M'?GENDER.MALE:GENDER.FEMALE);
                employeeDTO.setIsIndian(Boolean.parseBoolean(randomAccessFile.readLine()));
                employeeDTO.setBasicSalary(new BigDecimal(randomAccessFile.readLine().trim()));
                employeeDTO.setPANNumber(randomAccessFile.readLine());
                employeeDTO.setAadharCardNumber(randomAccessFile.readLine());
                randomAccessFile.close();
                return employeeDTO;
            }
            randomAccessFile.readLine();
        }


        randomAccessFile.close();
        throw new DAOException("error: invalid aadharCardNumber ("+panNumber+")");

    }catch(IOException ioe)
    {
        throw new DAOException(ioe.getMessage());
    }catch(ParseException pe)
    {
        throw new DAOException(pe.getMessage());
    }

}

public EmployeeDTOInterface getByAadharCardNumber(String aadharCardNumber) throws DAOException
{

    
    if(aadharCardNumber==null) throw new DAOException("error: invalid aadharCardNumber");
    aadharCardNumber=aadharCardNumber.trim();
    if(aadharCardNumber.length()==0) throw new DAOException("error: aadharCardNumber length is zero");

    try
    {
        File file=new File(fileName);
        if(file.exists()==false) throw new DAOException("error: not single employee added");
        RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
        if(randomAccessFile.length()==0) 
        {
            randomAccessFile.close();
            throw new DAOException("error: not single employee added");
        }
        randomAccessFile.readLine();
        if(Integer.parseInt(randomAccessFile.readLine().trim())==0)
        {
            randomAccessFile.close();
            throw new DAOException("error: not single employee added");
        }

        EmployeeDTOInterface employeeDTO;
        String fAadharCardNumber;
        long fPointerPosition;
        SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
        while(randomAccessFile.length()>randomAccessFile.getFilePointer())
        {
            fPointerPosition=randomAccessFile.getFilePointer();
            for(int x=0; x<8; ++x) randomAccessFile.readLine();
            fAadharCardNumber=randomAccessFile.readLine().trim();
            if(fAadharCardNumber.equalsIgnoreCase(aadharCardNumber))
            {
                randomAccessFile.seek(fPointerPosition);
                employeeDTO=new EmployeeDTO();
                employeeDTO.setEmployeeId(randomAccessFile.readLine().trim());
                employeeDTO.setName(randomAccessFile.readLine());
                employeeDTO.setDesignationCode(Integer.parseInt(randomAccessFile.readLine().trim()));
                employeeDTO.setDateOfBirth(sdf.parse(randomAccessFile.readLine()));
                employeeDTO.setGender(randomAccessFile.readLine().charAt(0)=='M'?GENDER.MALE:GENDER.FEMALE);
                employeeDTO.setIsIndian(Boolean.parseBoolean(randomAccessFile.readLine()));
                employeeDTO.setBasicSalary(new BigDecimal(randomAccessFile.readLine().trim()));
                employeeDTO.setPANNumber(randomAccessFile.readLine());
                employeeDTO.setAadharCardNumber(randomAccessFile.readLine());
                randomAccessFile.close();
                return employeeDTO;
            }
        }


        randomAccessFile.close();
        throw new DAOException("error: invalid aadharCardNumber ("+aadharCardNumber+")");

    }catch(IOException ioe)
    {
        throw new DAOException(ioe.getMessage());
    }catch(ParseException pe)
    {
        throw new DAOException(pe.getMessage());
    }




}

public boolean employeeIdExists(String employeeId) throws DAOException
{

    if(employeeId==null) return false;
    employeeId=employeeId.trim();
    if(employeeId.length()==0) return false;
    try
    {
        File file=new File(fileName);
        if(file.exists()==false) return false;
        RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
        if(randomAccessFile.length()==0) 
        {
            randomAccessFile.close();
            return false;
        }
        randomAccessFile.readLine();
        if(Integer.parseInt(randomAccessFile.readLine().trim())==0)
        {
            randomAccessFile.close();
            return false;
        }

        while(randomAccessFile.length()>randomAccessFile.getFilePointer())
        {            
            if(employeeId.equalsIgnoreCase(randomAccessFile.readLine().trim()))
            {
                randomAccessFile.close();
                return true;
            }
            for(int x=0; x<8; ++x) randomAccessFile.readLine();
        }

        randomAccessFile.close();
        return false;

    }catch(IOException ioe)
    {
        throw new DAOException(ioe.getMessage());
    }

}

public boolean panNumberExists(String panNumber) throws DAOException
{

    if(panNumber==null) return false;
    panNumber=panNumber.trim();
    if(panNumber.length()==0) return false;
    try
    {
        File file=new File(fileName);
        if(file.exists()==false) return false;
        RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
        if(randomAccessFile.length()==0) 
        {
            randomAccessFile.close();
            return false;
        }
        randomAccessFile.readLine();
        if(Integer.parseInt(randomAccessFile.readLine().trim())==0)
        {
            randomAccessFile.close();
            return false;
        }

        while(randomAccessFile.length()>randomAccessFile.getFilePointer())
        {            
            for(int x=0; x<7; ++x) randomAccessFile.readLine();
            if(panNumber.equalsIgnoreCase(randomAccessFile.readLine().trim()))
            {
                randomAccessFile.close();
                return true;
            }
            randomAccessFile.readLine();
        }

        randomAccessFile.close();
        return false;

    }catch(IOException ioe)
    {
        throw new DAOException(ioe.getMessage());
    }




}

public boolean aadharCardNumberExists(String aadharCardNumber) throws DAOException
{

    
    if(aadharCardNumber==null) return false;
    aadharCardNumber=aadharCardNumber.trim();
    if(aadharCardNumber.length()==0) return false;
    try
    {
        File file=new File(fileName);
        if(file.exists()==false) return false;
        RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
        if(randomAccessFile.length()==0) 
        {
            randomAccessFile.close();
            return false;
        }
        randomAccessFile.readLine();
        if(Integer.parseInt(randomAccessFile.readLine().trim())==0)
        {
            randomAccessFile.close();
            return false;
        }

        while(randomAccessFile.length()>randomAccessFile.getFilePointer())
        {            
            for(int x=0; x<8; ++x) randomAccessFile.readLine();
            if(aadharCardNumber.equalsIgnoreCase(randomAccessFile.readLine().trim()))
            {
                randomAccessFile.close();
                return true;
            }
        }

        randomAccessFile.close();
        return false;

    }catch(IOException ioe)
    {
        throw new DAOException(ioe.getMessage());
    }

}

public int getCount() throws DAOException
{
    try
    {
        int recordCount=0;
        File file=new File(fileName);
        if(!file.exists()) return recordCount;
        RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
        if(randomAccessFile.length()==0)
        {
          randomAccessFile.close();
          return recordCount;
        }
        randomAccessFile.readLine();
        recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
        randomAccessFile.close();
        return recordCount;
    }catch(IOException ioe)
    {
        throw new DAOException(ioe.getMessage());
    }
}

public int getCountByDesignation(int designationCode) throws DAOException
{

    DesignationDAOInterface designationDAO=new DesignationDAO();
    if(!designationDAO.codeExists(designationCode)) return 0;
    try
    {
        File file=new File(fileName);
        if(file.exists()==false) return 0;
        RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
        if(randomAccessFile.length()==0) return 0;
        randomAccessFile.readLine();
        randomAccessFile.readLine();
        int x,count;
        count=0;
        while(randomAccessFile.length()>randomAccessFile.getFilePointer())
        {
            randomAccessFile.readLine();
            randomAccessFile.readLine();
            if(designationCode==Integer.parseInt(randomAccessFile.readLine().trim())) 
            {
                count+=1;
            }
            for(x=0; x<6; ++x)  randomAccessFile.readLine();

        }
        randomAccessFile.close();
        return count;
    }catch(IOException ioe)
    {
        throw new DAOException(ioe.getMessage());
    }



}

}