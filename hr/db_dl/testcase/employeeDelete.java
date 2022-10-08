import com.hr.dl.interfaces.dao.*;
import com.hr.dl.interfaces.dto.*;
import com.hr.dl.exceptions.*;
import com.hr.dl.dao.*;


class employeeDelete
{
public static void main(String gg[])
{
String employeeId=gg[0];

try
{
EmployeeDAOInterface employeeDAO=new EmployeeDAO();
employeeDAO.delete(employeeId);
System.out.println("employee with Id: "+employeeId+") deleted....");
}catch(DAOException daoe)
{
System.out.println(daoe.getMessage());
}

}


}