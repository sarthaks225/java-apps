import com.hr.dl.interfaces.dao.*;
import com.hr.dl.interfaces.dto.*;
import com.hr.dl.exceptions.*;
import com.hr.dl.dao.*;
import java.text.*;

class employeeIdExists
{
public static void main(String gg[])
{
String employeeId=gg[0].trim();

try
{
EmployeeDAOInterface employeeDAO=new EmployeeDAO();

System.out.println(employeeDAO.employeeIdExists(employeeId));

}catch(DAOException daoe)
{
System.out.println(daoe.getMessage());
}

}


}