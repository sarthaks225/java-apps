import java.text.*;
import com.hr.dl.exceptions.*;
import com.hr.dl.interfaces.dao.*;
import com.hr.dl.interfaces.dto.*;
import com.hr.dl.dao.*;
import com.hr.dl.dto.*;
import java.util.*;
import java.io.*;
import java.math.*;
class employeeGetByDesignationCode
{
public static void main(String gg[])
{
int designationCode=Integer.parseInt(gg[0].trim());
SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
Set<EmployeeDTOInterface> employees;
EmployeeDAOInterface employeeDAO=new EmployeeDAO();
try
{
employees=employeeDAO.getByDesignation(designationCode);

for(EmployeeDTOInterface employeeDTO: employees)
{
System.out.println("Employee ID: ("+employeeDTO.getEmployeeId()+")");
System.out.println("Employee Name : ("+employeeDTO.getName()+")");
System.out.println("Employee designation code: ("+employeeDTO.getDesignationCode()+")");
System.out.println("Employee date of birth: ("+sdf.format(employeeDTO.getDateOfBirth())+")");
System.out.println("Employee gender: ("+employeeDTO.getGender()+")");
System.out.println("Employee isIndian: ("+employeeDTO.getIsIndian()+")");
System.out.println("Employee basic Salary: ("+employeeDTO.getBasicSalary()+")");
System.out.println("Employee pan number: ("+employeeDTO.getPANNumber()+")");
System.out.println("Employee aadhar card number: ("+employeeDTO.getAadharCardNumber()+")");
System.out.println("******************************");
}

}catch(DAOException daoe)
{
System.out.println(daoe.getMessage());
}

}


}