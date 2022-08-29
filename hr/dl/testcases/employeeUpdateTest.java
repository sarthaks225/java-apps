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
class employeeUpdateTest
{
public static void main(String gg[])
{
String employeeId=gg[0];
String name=gg[1];
String designationCode=gg[2];
SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
Date dateOfBirth=null;
try
{
dateOfBirth=sdf.parse(gg[3]);
}catch(ParseException ioe)
{
System.out.println(ioe.getMessage());
return;
}

char gender=gg[4].charAt(0);
boolean isIndian=Boolean.parseBoolean(gg[5]);
BigDecimal basicSalary=new BigDecimal(gg[6]);
String panNumber=gg[7];
String aadharCardNumber=gg[8];

EmployeeDTOInterface employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId(employeeId);
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
employeeDAO.update(employeeDTO);
System.out.println("Updation done......");
}catch(DAOException daoe)
{
System.out.println(daoe);
}

}


}