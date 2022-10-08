import com.hr.dl.exceptions.*;
import com.hr.dl.interfaces.dao.*;
import com.hr.dl.dao.*;

class employeeGetCountTest
{
public static void main(String gg[])
{

try
{
EmployeeDAOInterface employeeDAO=new EmployeeDAO();
System.out.println("number of employes : "+employeeDAO.getCount());
}
catch(DAOException daoe)
{
System.out.println(daoe.getMessage());
}


}




}