import com.hr.bl.exceptions.*;
import com.hr.bl.interfaces.pojo.*;
import com.hr.bl.interfaces.managers.*;
import com.hr.bl.pojo.*;
import com.hr.bl.managers.*;
import java.util.*;

class getDesignationCount
{
public static void main(String gg[])
{

try
{

DesignationInterface designation;
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();

System.out.println("Number of designations: "+designationManager.getDesignationCount());



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