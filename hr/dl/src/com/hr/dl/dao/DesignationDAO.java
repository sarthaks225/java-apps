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
            }
            while(recordCountString.length()<10)  
            {
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
    }

     while(recordCountString.length()<10)
    {
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
public void update(DesignationDTOInterface designationDTO) throws DAOException
{
    if(designationDTO==null) throw new DAOException("Desigantion is null");
    int code=designationDTO.getCode();
    String title=designationDTO.getTitle().trim();
    if(code<=0 || title.length()==0)  throw new DAOException("invalid Designation");

    RandomAccessFile randomAccessFile=null;
     RandomAccessFile tempRandomAccessFile=null;
    try
    {
        File file=new File(fileName);
        if(!file.exists()) throw new DAOException("Desgnation does not exist");
        randomAccessFile=new RandomAccessFile(file,"rw");
        if(randomAccessFile.length()==0) throw new DAOException("Designation does not exist");
        randomAccessFile.readLine();
        if(Integer.parseInt(randomAccessFile.readLine().trim())==0) throw new DAOException("Designation does not exist");

        int fCode;
        String fTitle;
        boolean booleanCodeFound=false;
        int filePointerPosition=0;
        while(randomAccessFile.getFilePointer()<randomAccessFile.length())
        {
            fCode=Integer.parseInt(randomAccessFile.readLine().trim());
          
            if(fCode==code) 
            {
                booleanCodeFound=true;
                filePointerPosition=(int)randomAccessFile.getFilePointer();
            }
            fTitle=randomAccessFile.readLine().trim();
            if(fTitle.equalsIgnoreCase(title)) throw new DAOException("Title already exists");
        }

        if(booleanCodeFound==false) throw new DAOException("code does not exsist");

        File tempFile=new File("temp.data");
        if(tempFile.exists()) tempFile.delete();
        tempRandomAccessFile=new RandomAccessFile(tempFile,"rw");

        randomAccessFile.seek(filePointerPosition);
        randomAccessFile.readLine();

        tempRandomAccessFile.writeBytes(title);
        tempRandomAccessFile.writeBytes("\n");
        while(randomAccessFile.getFilePointer()<randomAccessFile.length())
        {
            tempRandomAccessFile.writeBytes(randomAccessFile.readLine());
            tempRandomAccessFile.writeBytes("\n");
        }

        randomAccessFile.setLength(filePointerPosition);
        randomAccessFile.seek(filePointerPosition);

        tempRandomAccessFile.seek(0);
        while(tempRandomAccessFile.length()>tempRandomAccessFile.getFilePointer())
        {
            randomAccessFile.writeBytes(tempRandomAccessFile.readLine());
            randomAccessFile.writeBytes("\n");

        }
        randomAccessFile.close();
        tempRandomAccessFile.close();
    }catch(IOException ioe)
    {
        try
        {
            randomAccessFile.close();
            tempRandomAccessFile.close();
        }catch(IOException io)
        {
        }

        throw new DAOException(ioe.getMessage());

    }
}
public void delete(int code) throws DAOException
{

    if(code<=0)  throw new DAOException("invalid Designation code: "+code);

    RandomAccessFile randomAccessFile=null;
    RandomAccessFile tempRandomAccessFile=null;
    try
    {
        File file=new File(fileName);
        if(!file.exists()) throw new DAOException("Desgnation does not exist");
        randomAccessFile=new RandomAccessFile(file,"rw");
        if(randomAccessFile.length()==0) throw new DAOException("Designation does not exist");
        int lastGeneratedCode=Integer.parseInt(randomAccessFile.readLine().trim());
        if(lastGeneratedCode<code) throw new DAOException("invalid Designation code: "+code);
        int count=Integer.parseInt(randomAccessFile.readLine().trim());
        if(count==0) throw new DAOException("Designation does not exist");

        int fCode;
        boolean booleanCodeFound=false;
        int filePointerPosition=0;
        while(randomAccessFile.getFilePointer()<randomAccessFile.length())
        {
            
            filePointerPosition=(int)randomAccessFile.getFilePointer();
            fCode=Integer.parseInt(randomAccessFile.readLine().trim());
            if(fCode==code) 
            {
                booleanCodeFound=true;
                break;
            }
            randomAccessFile.readLine().trim();
        }
        
        if(booleanCodeFound==false) throw new DAOException("code ("+code+") does not exsist");
        randomAccessFile.readLine().trim();

        File tempFile=new File("temp.data");
        if(tempFile.exists()) tempFile.delete();
        tempRandomAccessFile=new RandomAccessFile(tempFile,"rw");

        randomAccessFile.seek(filePointerPosition);
        randomAccessFile.readLine();
        randomAccessFile.readLine();

        while(randomAccessFile.getFilePointer()<randomAccessFile.length())
        {
            tempRandomAccessFile.writeBytes(randomAccessFile.readLine());
            tempRandomAccessFile.writeBytes("\n");
            tempRandomAccessFile.writeBytes(randomAccessFile.readLine());
            tempRandomAccessFile.writeBytes("\n");
        }

        randomAccessFile.setLength(filePointerPosition);
        randomAccessFile.seek(filePointerPosition);

        tempRandomAccessFile.seek(0);
        while(tempRandomAccessFile.length()>tempRandomAccessFile.getFilePointer())
        {
            randomAccessFile.writeBytes(tempRandomAccessFile.readLine());
            randomAccessFile.writeBytes("\n");

        }

        randomAccessFile.seek(0);
        randomAccessFile.readLine();
        String countStr=Integer.toString(--count);
        while(countStr.length()<10) countStr+=" ";
        randomAccessFile.writeBytes(countStr);
        randomAccessFile.writeBytes("\n");

        randomAccessFile.close();
        tempRandomAccessFile.close();
    }catch(IOException ioe)
    {
        try
        {
            randomAccessFile.close();
            tempRandomAccessFile.close();
        }catch(IOException io)
        {
        }

        throw new DAOException(ioe.getMessage());

    }
}

public Set<DesignationDTOInterface> getAll() throws DAOException
{
    Set<DesignationDTOInterface> designations;
    designations=new TreeSet<DesignationDTOInterface>();
    RandomAccessFile randomAccessFile=null;

    try
    {
        File file=new File(fileName);
        if(!file.exists()) return designations;
        randomAccessFile=new RandomAccessFile(file,"rw");
        if(randomAccessFile.length()==0) return designations;

        randomAccessFile.readLine();
        int recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
        if(recordCount==0) return designations;
        int fCode;
        String fTitle;
        DesignationDTO designation;
        while(randomAccessFile.length()>randomAccessFile.getFilePointer())
        {
            fCode=Integer.parseInt(randomAccessFile.readLine().trim());
            fTitle=randomAccessFile.readLine().trim();
            designation=new DesignationDTO();
            designation.setCode(fCode);
            designation.setTitle(fTitle);
            designations.add(designation);
        }
     
        randomAccessFile.close();
    }catch(IOException ioe)
    {
        if(randomAccessFile!=null)
        {
            try
            {
            randomAccessFile.close();
            }catch(IOException io)
            {}
        }
        
        throw new DAOException(ioe.getMessage());
    }



   return designations;
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
       throw new DAOException(ioe.getMessage());

    }

    



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

        throw new DAOException(ioe.getMessage());
        
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
        throw new DAOException(ioe.getMessage());

    }
    
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

        throw new DAOException(ioe.getMessage());
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
       throw new DAOException(ioe.getMessage());
    }
}


};