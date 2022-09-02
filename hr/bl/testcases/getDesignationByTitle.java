import com.hr.bl.exceptions.*;
import com.hr.bl.interfaces.pojo.*;
import com.hr.bl.interfaces.managers.*;
import com.hr.bl.pojo.*;
import com.hr.bl.managers.*;
import java.util.*;

class getDesignationByTitle
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
title="pilot";
 
designation=designationManager.getDesignationByTitle(title);
System.out.println(designation.getCode()+" / "+designation.getTitle());

//.....2
title="nurse";

designation=designationManager.getDesignationByTitle(title);
System.out.println(designation.getCode()+" / "+designation.getTitle());

//.......3
title="clerk";

designation=designationManager.getDesignationByTitle(title);
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