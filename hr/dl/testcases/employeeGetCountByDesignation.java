import com.hr.dl.interfaces.dao.*;
import com.hr.dl.exceptions.*;
import com.hr.dl.dao.*;
class employeeGetCountByDesignation
{
public static void main(String gg[])
{
int designationCode=Integer.parseInt(gg[0].trim());

try
{
EmployeeDAOInterface employeeDAO=new EmployeeDAO();
int count=employeeDAO.getCountByDesignation(designationCode);
System.out.println(count);


}catch(DAOException daoe)
{
System.out.println(daoe.getMessage());
}


}


}