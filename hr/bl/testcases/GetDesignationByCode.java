import com.hr.bl.exceptions.*;
import com.hr.bl.interfaces.pojo.*;
import com.hr.bl.interfaces.managers.*;
import com.hr.bl.pojo.*;
import com.hr.bl.managers.*;
import java.util.*;

class GetDesignationByCode
{
public static void main(String gg[])
{
int code;
String title;

try
{

DesignationInterface designation;
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();

//....1
code=9;
 
designation=designationManager.getDesignationByCode(code);
System.out.println(designation.getCode()+" / "+designation.getTitle());

//.....2
code=9;

designation=designationManager.getDesignationByCode(code);
System.out.println(designation.getCode()+" / "+designation.getTitle());

//.......3
code=17;

designation=designationManager.getDesignationByCode(code);
System.out.println(designation.getCode()+" / "+designation.getTitle());



}
catch(BLException ble)
{

if(ble.hasGenericException()) System.out.println("genric exception: "+ble.getGenericException());
List<String> list=ble.getPropertyExceptions();
System.out.println("property exceptions: ");
for(String property: list)
{
System.out.println(ble.getException(property));
}

}

}


}