import com.hr.dl.exceptions.*;
import com.hr.dl.dao.*;
import com.hr.dl.interfaces.dto.*;

class DesignationGetByCode
{
public static void main(String gg[])
{
int code=Integer.parseInt(gg[0].trim());

try
{

DesignationDAO designationDAO=new DesignationDAO();
DesignationDTOInterface designationDTO;
designationDTO=designationDAO.getByCode(code);
System.out.println("Code : "+designationDTO.getCode()+" Title: "+designationDTO.getTitle());

}catch(DAOException daoe)
{
System.out.println(daoe.getMessage());
}

}

};