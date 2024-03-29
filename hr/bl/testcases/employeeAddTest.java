import com.hr.bl.managers.*;
import com.hr.bl.interfaces.managers.*;
import com.hr.bl.pojo.*;
import com.hr.bl.managers.*;
import com.hr.bl.exceptions.*;
import com.hr.bl.interfaces.pojo.*;

/*
import com.hr.dl.exceptions.*;
import com.hr.dl.interfaces.dto.*;
import com.hr.dl.interfaces.dao.*;
import com.hr.dl.dao.*;
import com.hr.dl.dto.*;
*/

import java.util.*;
import java.math.*;
import com.hr.common.enums.*;
import java.text.*;
class employeeAddTest
{
public static void main(String gg[])
{
/*
String name=gg[0];
int designationCode=Integer.parseInt(gg[1].trim());
String dateOfBirth=gg[2];
char gender=gg[3].charAt(0);
boolean isIndian=Boolean.parseBoolean(gg[4]);
BigDecimal basicSalary=new BigDecimal(gg[5].trim());
String panNumber=gg[6];
String aadharCardNumber=gg[7];
*/

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

EmployeeManagerInterface employeeManager=EmployeeManager.getEmployeeManager();

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


}



}