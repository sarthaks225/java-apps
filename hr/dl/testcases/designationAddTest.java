import com.hr.dl.dao.*;
import com.hr.dl.dto.*;
import com.hr.dl.interfaces.dao.*;
import com.hr.dl.interfaces.dto.*;
import com.hr.dl.exceptions.*;
import java.util.*;
import java.io.*;


class designationAddTest
{
public static void main(String gg[])
{
String designation=gg[0];
DesignationDAO designationDAO=new DesignationDAO();
DesignationDTO designationDTO=new DesignationDTO();
designationDTO.setTitle(designation);
try
{
designationDAO.add(designationDTO);
System.out.printf("Title: (%s) code:(%d)\n",designationDTO.getTitle(),designationDTO.getCode());
}catch(DAOException daoe)
{
System.out.println("Error: "+daoe.getMessage());
}

}



};