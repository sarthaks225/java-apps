import com.hr.bl.managers.*;
import com.hr.bl.interfaces.managers.*;
import com.hr.bl.pojo.*;
import com.hr.bl.managers.*;
import com.hr.bl.exceptions.*;
import com.hr.bl.interfaces.pojo.*;

import java.util.*;
import java.math.*;
import com.hr.common.enums.*;
import java.text.*;
class employeeDeleteTest
{
public static void main(String gg[])
{


try
{
EmployeeManagerInterface employeeManager=EmployeeManager.getEmployeeManager();
employeeManager.delete("A10000003");

System.out.println("employee deleted");

//...........................now adding
/*
System.out.println("adding ");
String name="kanak";
DesignationInterface designation=new Designation();
designation.setCode(9);


char gender='f';

Boolean isIndian=true;

BigDecimal basicSalary=new BigDecimal("34730");

String panNumber="pan1234342131";
String aadharCardNumber="inr78492302";

try
{
//DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
//DesignationInterface designation=designationManager.getDesignationByCode(designation);


//date=sdf.parse(dateOfBirth);
SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
Date date=new Date();
date=sdf.parse("10/10/1910");
EmployeeInterface employee=new Employee();


employee.setName(name);
employee.setDesignation(designation);

employee.setDateOfBirth(date);

employee.setGender(gender=='m' || gender=='M'?GENDER.MALE:GENDER.FEMALE);
employee.setIsIndian(isIndian);
employee.setBasicSalary(basicSalary);
employee.setPANNumber(panNumber);
employee.setAadharCardNumber(aadharCardNumber);



employeeManager.add(employee);

System.out.println("employee added");

}
catch(BLException ble)
{
if(ble.hasGenericException()) System.out.println("genric exception: "+ble.getGenericException());

System.out.println("property exception		");
List<String> properties=ble.getPropertyExceptions();
for(String property: properties)
{
System.out.println(ble.getException(property));
}

}
catch(ParseException pe)
{
System.out.println(pe.getMessage());
}

*/
}



catch(BLException ble)
{
if(ble.hasGenericException()) System.out.println("genric exception: "+ble.getGenericException());

System.out.println("property exception		");
List<String> properties=ble.getPropertyExceptions();
for(String property: properties)
{
System.out.println(ble.getException(property));
}

}


}



}