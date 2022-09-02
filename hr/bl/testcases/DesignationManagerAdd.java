import com.hr.bl.exceptions.*;
import com.hr.bl.interfaces.pojo.*;
import com.hr.bl.interfaces.managers.*;
import com.hr.bl.pojo.*;
import com.hr.bl.managers.*;
import java.util.*;

class DesignationManagerAdd
{
public static void main(String gg[])
{
String title=gg[0];
int code=0;

try
{

DesignationInterface designation=new Designation();
designation.setTitle(title);
designation.setCode(code);

DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
designationManager.add(designation);
code=designation.getCode();
System.out.println("code: "+designation.getCode()+"title: "+designation.getTitle());
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