import com.hr.dl.interfaces.dao.*;
import com.hr.dl.interfaces.dto.*;
import com.hr.dl.exceptions.*;
import com.hr.dl.dao.*;
import java.text.*;

class employeeAadharCardNumberExists
{
public static void main(String gg[])
{
String aadharCardNumber=gg[0].trim();

try
{
EmployeeDAOInterface employeeDAO=new EmployeeDAO();

System.out.println(employeeDAO.aadharCardNumberExists(aadharCardNumber));

}catch(DAOException daoe)
{
System.out.println(daoe.getMessage());
}

}


}