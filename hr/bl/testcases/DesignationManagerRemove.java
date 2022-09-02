import com.hr.bl.exceptions.*;
import com.hr.bl.interfaces.pojo.*;
import com.hr.bl.interfaces.managers.*;
import com.hr.bl.pojo.*;
import com.hr.bl.managers.*;
import java.util.*;

class DesignationManagerRemove
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
code=4;
 
  

designationManager.remove(code);
System.out.println("removed :- code: "+designation.getCode());

//.....2
code=6;
title="master";
 
  

designationManager.remove(code);
System.out.println("removed :- code: "+designation.getCode());

//.......3
code=5;
title="teacher";
 
  

designationManager.remove(code);
System.out.println("removed :- code: "+designation.getCode());



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