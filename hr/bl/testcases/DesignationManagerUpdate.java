import com.hr.bl.exceptions.*;
import com.hr.bl.interfaces.pojo.*;
import com.hr.bl.interfaces.managers.*;
import com.hr.bl.pojo.*;
import com.hr.bl.managers.*;
import java.util.*;

class DesignationManagerUpdate
{
public static void main(String gg[])
{
int code;
String title;

try
{

DesignationInterface designation=new Designation();
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();

//....1
code=2;
title="clerk";
designation.setTitle(title);
designation.setCode(code);

designationManager.update(designation);
System.out.println("Updated :- code: "+designation.getCode()+"title: "+designation.getTitle());

//.....2
code=1;
title="master";
designation.setTitle(title);
designation.setCode(code);

designationManager.update(designation);
System.out.println("Updated :- code: "+designation.getCode()+"title: "+designation.getTitle());

//.......3
code=1;
title="teacher";
designation.setTitle(title);
designation.setCode(code);

designationManager.update(designation);
System.out.println("Updated :- code: "+designation.getCode()+"title: "+designation.getTitle());



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