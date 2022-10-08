import com.hr.dl.interfaces.dao.*;
import com.hr.dl.interfaces.dto.*;
import com.hr.dl.exceptions.*;
import com.hr.dl.dao.*;
import java.text.*;

class employeePANNumberExists
{
public static void main(String gg[])
{
String panNumber=gg[0].trim();

try
{
EmployeeDAOInterface employeeDAO=new EmployeeDAO();

System.out.println(employeeDAO.panNumberExists(panNumber));

}catch(DAOException daoe)
{
System.out.println(daoe.getMessage());
}

}


}