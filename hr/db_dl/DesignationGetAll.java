import com.hr.dl.exceptions.*;
import com.hr.dl.dao.*;
import com.hr.dl.interfaces.dao.*;
import com.hr.dl.interfaces.dto.*;

import java.util.*;

class DesignationGetAll
{
public static void main(String gg[])
{
DesignationDAO designationDAO=new DesignationDAO();
DesignationDTOInterface designationDTO;
Set<DesignationDTOInterface> designations;

try
{
designations=designationDAO.getAll();

designations.forEach((designation)->{
System.out.println("Code: ("+designation.getCode()+") Title: ("+designation.getTitle()+")");
});

}catch(DAOException daoe)
{
System.out.println(daoe.getMessage());
}


}



}