import com.hr.dl.exceptions.*;
import com.hr.dl.dao.*;
import com.hr.dl.interfaces.dto.*;
					 
class DesignationGetByTitle
{
public static void main(String gg[])
{
String title=gg[0];

try
{

DesignationDAO designationDAO=new DesignationDAO();
DesignationDTOInterface designationDTO;
designationDTO=designationDAO.getByTitle(title);
System.out.println("Code : "+designationDTO.getCode()+" Title: "+designationDTO.getTitle());

}catch(DAOException daoe)
{
System.out.println(daoe.getMessage());
}

}

};