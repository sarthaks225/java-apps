import com.hr.bl.exceptions.*;
import com.hr.bl.interfaces.pojo.*;
import com.hr.bl.interfaces.managers.*;
import com.hr.bl.pojo.*;
import com.hr.bl.managers.*;
import java.util.*;
class getDesignations
{
public static void main(String gg[])
{

try
{

DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
Set<DesignationInterface> designations=designationManager.getDesignations();

for(DesignationInterface designation: designations)
{
System.out.println("code: "+designation.getCode()+"  title: "+designation.getTitle());

}






}catch(BLException ble)
{
if(ble.hasGenericException()) System.out.println("generic exception: "+ble.getGenericException());
List<String>properties=ble.getPropertyExceptions();
System.out.println("Property exceptions: ");
for(String property:properties)
{
System.out.println(ble.getException(property));

}
}




}


}