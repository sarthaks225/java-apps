import java.text.*;
import com.hr.common.enums.*;
import com.hr.dl.exceptions.*;
import com.hr.dl.interfaces.dao.*;
import com.hr.dl.interfaces.dto.*;
import com.hr.dl.dao.*;
import com.hr.dl.dto.*;
import java.util.*;
import java.io.*;
import java.math.*;
class employeeAddTest
{
public static void main(String gg[])
{
String name=gg[0];
String designationCode=gg[1];
SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
Date dateOfBirth=null;
try
{
dateOfBirth=sdf.parse(gg[2]);
}catch(ParseException ioe)
{
System.out.println(ioe.getMessage());
return;
}

char gender=gg[3].charAt(0);
boolean isIndian=Boolean.parseBoolean(gg[4]);
BigDecimal basicSalary=new BigDecimal(gg[5]);
String panNumber=gg[6];
String aadharCardNumber=gg[7];

EmployeeDTOInterface employeeDTO=new EmployeeDTO();
employeeDTO.setName(name);
employeeDTO.setDesignationCode(Integer.parseInt(designationCode));
employeeDTO.setDateOfBirth(dateOfBirth); 
if(gender=='m' || gender=='M') employeeDTO.setGender(GENDER.MALE);
else employeeDTO.setGender(GENDER.FEMALE);
employeeDTO.setIsIndian(isIndian);
employeeDTO.setBasicSalary(basicSalary);
employeeDTO.setPANNumber(panNumber);
employeeDTO.setAadharCardNumber(aadharCardNumber);
EmployeeDAOInterface employeeDAO=new EmployeeDAO();
try
{
employeeDAO.add(employeeDTO);
}catch(DAOException daoe)
{
System.out.println(daoe);
}

}


}