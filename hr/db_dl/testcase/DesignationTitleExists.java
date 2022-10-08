import com.hr.dl.exceptions.*;
import com.hr.dl.dao.*;
class DesignationTitleExists
{
public static void main(String gg[])
{

String title;
title=gg[0];
try
{
DesignationDAO designationDAO=new DesignationDAO();
boolean bool=designationDAO.titleExists(title);
System.out.println(bool);

}catch(DAOException daoe)
{
System.out.println(daoe.getMessage());
}





}


}