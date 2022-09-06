import com.hr.bl.exceptions.*;
import com.hr.bl.interfaces.managers.*;
import com.hr.bl.interfaces.pojo.*;
import com.hr.bl.managers.*;
import com.hr.bl.pojo.*;
import java.util.*;


class employeesGetByDesigantionCode
{
public static void main(String gg[])
{
try
{
EmployeeManagerInterface employeeManager=EmployeeManager.getEmployeeManager();

Set<EmployeeInterface> employees=employeeManager.getByDesignation(9);

for(EmployeeInterface employee: employees)
{

System.out.println("employee id: "+employee.getEmployeeId());
System.out.println("employee name: "+employee.getName());
System.out.println("employee desigantion : code ("+employee.getDesignation().getCode()+") title ("+employee.getDesignation().getTitle()+")");
System.out.println("employee gender : "+employee.getGender());
System.out.println("employee isIndian: "+employee.getIsIndian());
System.out.println("employee basic Salary"+employee.getBasicSalary());
System.out.println("employee date of Birth"+employee.getDateOfBirth());
System.out.println("employee pan number: "+employee.getPANNumber());
System.out.println("employee aadhar card number: "+employee.getAadharCardNumber());
System.out.println("..................................");


}



employeeManager.delete("A10000007");
System.out.println("after deletrion of A10000007");


employees=employeeManager.getByDesignation(9);

for(EmployeeInterface employee: employees)
{

System.out.println("employee id: "+employee.getEmployeeId());
System.out.println("employee name: "+employee.getName());
System.out.println("employee desigantion : code ("+employee.getDesignation().getCode()+") title ("+employee.getDesignation().getTitle()+")");
System.out.println("employee gender : "+employee.getGender());
System.out.println("employee isIndian: "+employee.getIsIndian());
System.out.println("employee basic Salary"+employee.getBasicSalary());
System.out.println("employee date of Birth"+employee.getDateOfBirth());
System.out.println("employee pan number: "+employee.getPANNumber());
System.out.println("employee aadhar card number: "+employee.getAadharCardNumber());
System.out.println("..................................");


}








}catch(BLException ble)
{
System.out.println("Generic Exception : "+ble.getGenericException());
}


}


}