import com.hr.dl.interfaces.dao.*;
import com.hr.dl.interfaces.dto.*;
import com.hr.dl.exceptions.*;
import com.hr.dl.dao.*;
import java.text.*;

class employeeGetByAadharCardNumber
{
public static void main(String gg[])
{
String aadharCardNumber=gg[0];

try
{
EmployeeDAOInterface employeeDAO=new EmployeeDAO();
EmployeeDTOInterface employeeDTO;
employeeDTO=employeeDAO.getByAadharCardNumber(aadharCardNumber);
SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
System.out.println("Id: "+employeeDTO.getEmployeeId());
System.out.println("name: "+employeeDTO.getName());
System.out.println("designation code: "+employeeDTO.getDesignationCode());
System.out.println("date of birth : "+sdf.format(employeeDTO.getDateOfBirth()));
System.out.println("gender: "+employeeDTO.getGender());
System.out.println("isIndian: "+employeeDTO.getIsIndian());
System.out.println("basic salary: "+employeeDTO.getBasicSalary());
System.out.println("PAN number: "+employeeDTO.getPANNumber());
System.out.println("Aadhar card number: "+employeeDTO.getAadharCardNumber());

}catch(DAOException daoe)
{
System.out.println(daoe.getMessage());
}

}


}