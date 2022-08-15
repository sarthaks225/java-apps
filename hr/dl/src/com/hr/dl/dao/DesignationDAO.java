package com.hr.dl.dao;
import com.hr.dl.interfaces.dao.*;
import com.hr.dl.interfaces.dto.*;
import com.hr.dl.exceptions.*;
import java.util.*;
import java.io.*;
import com.hr.dl.dto.*;
public class DesignationDAO implements DesignationDAOInterface
{
   private static String fileName="designation.data";
public void add(DesignationDTOInterface designationDTOInterface) throws DAOException
{
    if(designationDTOInterface==null || designationDTOInterface.getTitle()==null || designationDTOInterface.getTitle().trim().length()==0) throw new DAOException("Designation is null");
    String title=designationDTOInterface.getTitle().trim();
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
 

    if(code<=0) throw new DAOException("Code : ("+code+") not found");
    RandomAccessFile randomAccessFile=null;
    try
    {
        File file=new File(fileName);
        if(!file.exists()) throw new DAOException("Code : ("+code+") not found");
        
        randomAccessFile=new RandomAccessFile(file,"rw");
        if(randomAccessFile.length()==0) 
        {   
            randomAccessFile.close();
            throw new DAOException("Code : ("+code+") not found");
        }
        randomAccessFile.readLine();
        int fCount=Integer.parseInt(randomAccessFile.readLine().trim());
        if(fCount==0)
        {
            randomAccessFile.close();
            throw new DAOException("Code : ("+code+") not found");
        }

        int fCode;
        String fTitle;
        while(randomAccessFile.length()>randomAccessFile.getFilePointer())
        {
            fCode=Integer.parseInt(randomAccessFile.readLine().trim());
            fTitle=randomAccessFile.readLine();
            
            if(fCode==code)
            {
                randomAccessFile.close();
                DesignationDTOInterface designationDTO=new DesignationDTO();
                designationDTO.setCode(fCode);
                designationDTO.setTitle(fTitle.trim());
                return designationDTO;
            }

        }
        randomAccessFile.close();
        throw new DAOException("Code : ("+code+") not found");
    }catch(IOException ioe)
    {
        System.out.println(ioe.getMessage());

    }

    throw new DAOException("Code : ("+code+") not found");



}

public DesignationDTOInterface getByTitle(String string) throws DAOException
{

    if(string==null) throw new DAOException("Designation : ("+string+") not found");
    String title=string.trim();
    
    if(title.length()==0) throw new DAOException("Designation : ("+string+") not found");


    DesignationDTOInterface designationDTO=new DesignationDTO();
    RandomAccessFile randomAccessFile=null;
    try
    {
        File file=new File(fileName);
        if(!file.exists()) throw new DAOException("Designation : ("+string+") not found");

        randomAccessFile=new RandomAccessFile(file,"rw");
        if(randomAccessFile.length()==0)
        {
            randomAccessFile.close();
            throw new DAOException("Designation : ("+string+") not found");
        }
        randomAccessFile.readLine();
        int recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
        if(recordCount==0) throw new DAOException("Designation : ("+string+") not found");
        String fTitle;
        String fCode;
        while(randomAccessFile.length()>randomAccessFile.getFilePointer())
        {
            fCode=randomAccessFile.readLine();
            fTitle=randomAccessFile.readLine().trim();
            if(fTitle.equalsIgnoreCase(title)) 
            {
                randomAccessFile.close();
                designationDTO.setCode(Integer.parseInt(fCode.trim()));
                designationDTO.setTitle(fTitle);
                return designationDTO;
            }
        }
        randomAccessFile.close();
        throw new DAOException("Designation : ("+string+") not found");
    }catch(IOException ioe)
    {

        System.out.println(ioe.getMessage());
        throw new  DAOException("Designation : ("+string+") not found");
    }

}

public boolean codeExists(int code) throws DAOException
{
    if(code<=0) return false;
    RandomAccessFile randomAccessFile=null;
    try
    {
        File file=new File(fileName);
        if(!file.exists()) return false;
        
        randomAccessFile=new RandomAccessFile(file,"rw");
        if(randomAccessFile.length()==0) 
        {   
            randomAccessFile.close();
            return false;
        }
        randomAccessFile.readLine();
        int fCount=Integer.parseInt(randomAccessFile.readLine().trim());
        if(fCount==0)
        {
            randomAccessFile.close();
            return false;
        }

        int fCode;
        while(randomAccessFile.length()>randomAccessFile.getFilePointer())
        {
            fCode=Integer.parseInt(randomAccessFile.readLine().trim());
            randomAccessFile.readLine();
            
            if(fCode==code)
            {
                randomAccessFile.close();
                return true;
            }

        }
        randomAccessFile.close();
        return false;
    }catch(IOException ioe)
    {
        System.out.println(ioe.getMessage());

    }

    return false;
}

public boolean titleExists(String string) throws DAOException
{
    if(string==null) return false;
    String title=string.trim();
    if(title.length()==0) return false;

    RandomAccessFile randomAccessFile=null;
    try
    {
        File file=new File(fileName);
        if(!file.exists()) return false;

        randomAccessFile=new RandomAccessFile(file,"rw");
        if(randomAccessFile.length()==0)
        {
            randomAccessFile.close();
            return false;
        }
        randomAccessFile.readLine();
        int recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
        if(recordCount==0) return false;
        String fTitle;

        while(randomAccessFile.length()>randomAccessFile.getFilePointer())
        {
            randomAccessFile.readLine();
            fTitle=randomAccessFile.readLine().trim();
            if(fTitle.equalsIgnoreCase(title)) 
            {
                randomAccessFile.close();
                return true;
            }
        }
        randomAccessFile.close();
        return false;
    }catch(IOException ioe)
    {

        System.out.println(ioe.getMessage());
        return false;
    }




}

public int getCount() throws DAOException
{
    RandomAccessFile randomAccessFile=null;
    try
    {
        File file=new File(fileName);
        if(!file.exists()) return 0;

        randomAccessFile=new RandomAccessFile(file,"rw");
        if(randomAccessFile.length()==0)
        {
        randomAccessFile.close();    
        return 0;
        }
        randomAccessFile.readLine();
        int getCount=Integer.parseInt(randomAccessFile.readLine().trim());

        randomAccessFile.close();
        return getCount;
    }catch(IOException ioe)
    {
        try
        {
        if(randomAccessFile!=null) randomAccessFile.close();
        }catch(IOException iioe)
        {
        }
        System.out.println(ioe.getMessage());
        return 0;
    }
}


};