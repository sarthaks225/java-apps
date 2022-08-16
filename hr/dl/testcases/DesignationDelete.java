import com.hr.dl.exceptions.*;
import com.hr.dl.dao.*;
import com.hr.dl.dto.*;
import com.hr.dl.interfaces.dao.*;
import com.hr.dl.interfaces.dto.*;

import java.util.*;

class DesignationDelete
{
public static void main(String gg[])
{
DesignationDAO designationDAO=new DesignationDAO();
DesignationDTO designationDTO=new DesignationDTO();

designationDTO.setCode(Integer.parseInt(gg[0].trim()));
designationDTO.setTitle(gg[1].trim());

try
{
designationDAO.delete(designationDTO);
System.out.println("Deletion done.....");
}catch(DAOException daoe)
{
System.out.println(daoe.getMessage());
}


}



}