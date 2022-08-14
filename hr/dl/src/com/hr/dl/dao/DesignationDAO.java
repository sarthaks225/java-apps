package com.hr.dl.dao;
import com.hr.dl.interfaces.dao.*;
import com.hr.dl.interfaces.dto.*;
import com.hr.dl.exceptions.*;
import java.util.*;
import java.io.*;
public class DesignationDAO implements DesignationDAOInterface
{

public void add(DesignationDTOInterface designationDTOInterface) throws DAOException
{
    if(designationDTOInterface==null || designationDTOInterface.getTitle()==null || designationDTOInterface.getTitle().trim().length()==0) throw new DAOException("Designation is null");
    String title=designationDTOInterface.getTitle().trim();
    String fileName="designation.data";
    int lastGeneratedCode=0;
    int recordCount=0;
    RandomAccessFile randomAccessFile=null;
    String lastGeneratedCodeString="0";
    String recordCountString="0";
    try
    {
        File f=new File(fileName);
        randomAccessFile=new RandomAccessFile(f,"rw");
        if(randomAccessFile.length()==0)
        {
            while(lastGeneratedCodeString.length()<10)  
            {
                lastGeneratedCodeString+=" ";
                recordCountString+=" ";
            }

            randomAccessFile.writeBytes(lastGeneratedCodeString);
            randomAccessFile.writeBytes("\n");
            randomAccessFile.writeBytes(recordCountString);
            randomAccessFile.writeBytes("\n");
        }
        else
        {
        lastGeneratedCodeString=randomAccessFile.readLine().trim();
        recordCountString=randomAccessFile.readLine().trim();
        lastGeneratedCode=Integer.parseInt(lastGeneratedCodeString);
        recordCount=Integer.parseInt(recordCountString);
        }

    String fTitle;
    String fCode;
    while(randomAccessFile.getFilePointer()<randomAccessFile.length())
    {
        randomAccessFile.readLine().trim();
        fTitle=randomAccessFile.readLine().trim();
        if(fTitle.equalsIgnoreCase(title))
        {
            throw new DAOException("Designation : ("+title+") exists");
        }
    }   

    lastGeneratedCode++;
    recordCount++;
    lastGeneratedCodeString=String.valueOf(lastGeneratedCode);
    recordCountString=String.valueOf(recordCount);
    randomAccessFile.writeBytes(lastGeneratedCodeString);
    randomAccessFile.writeBytes("\n");
    randomAccessFile.writeBytes(title);
    randomAccessFile.writeBytes("\n");
    
    randomAccessFile.seek(0);
    while(lastGeneratedCodeString.length()<10)
    {
        lastGeneratedCodeString+=" ";
        recordCountString+=" ";
    }
    randomAccessFile.writeBytes(lastGeneratedCodeString);
    randomAccessFile.writeBytes("\n");
    randomAccessFile.writeBytes(recordCountString);
    randomAccessFile.writeBytes("\n");
    randomAccessFile.close();
    designationDTOInterface.setCode(lastGeneratedCode);
    

    }catch(IOException ioe)
    {
        try{
            if(randomAccessFile!=null)  randomAccessFile.close();
        }catch(IOException io)
        {
            
        }
        System.out.println(ioe.getMessage());
    }
    


}
public void update(DesignationDTOInterface designationDTOInterface) throws DAOException
{
throw new DAOException("NOT Yet Implemented");
}
public DesignationDTOInterface delete(DesignationDTOInterface designationDTOInterface) throws DAOException
{
throw new DAOException("NOT Yet Implemented");
}

public TreeSet<DesignationDTOInterface> getAll() throws DAOException
{
throw new DAOException("NOT Yet Implemented");
}

public DesignationDTOInterface getByCode(int code) throws DAOException
{
throw new DAOException("NOT Yet Implemented");
}

public DesignationDTOInterface getByTitle(String string) throws DAOException
{
throw new DAOException("NOT Yet Implemented");
}

public boolean codeExists(int code) throws DAOException
{
throw new DAOException("NOT Yet Implemented");
}

public boolean titleExists(String string) throws DAOException
{
throw new DAOException("NOT Yet Implemented");
}

public int getCount() throws DAOException
{
throw new DAOException("NOT Yet Implemented");
}


};