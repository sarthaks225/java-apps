import com.hr.bl.exceptions.*;
import com.hr.bl.interfaces.pojo.*;
import com.hr.bl.interfaces.managers.*;
import com.hr.bl.pojo.*;
import com.hr.bl.managers.*;
import java.util.*;

class designationTitleExist
{
public static void main(String gg[])
{

String title=gg[0];

try
{

DesignationInterface designation;
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();

System.out.println(designationManager.designationTitleExists(title));
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