import com.hr.dl.exceptions.*;
import com.hr.dl.interfaces.dao.*;
import com.hr.dl.dao.*;
class EmployeeIsDesigantionAlloted
{
public static void main(String gg[])
{
int designationCode=Integer.parseInt(gg[0].trim());

try
{
EmployeeDAOInterface employeeDAO=new EmployeeDAO();
boolean bool=employeeDAO.isDesignationAlloted(designationCode);
System.out.println("designation code exists: "+bool);


}catch(DAOException daoe)
{
System.out.println(daoe.getMessage());
}


}

}