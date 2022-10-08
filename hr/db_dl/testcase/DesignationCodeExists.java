import com.hr.dl.exceptions.*;
import com.hr.dl.dao.*;
class DesignationCodeExists
{
public static void main(String gg[])
{

int code;
code=Integer.parseInt(gg[0]);
try
{
DesignationDAO designationDAO=new DesignationDAO();
boolean bool=designationDAO.codeExists(code);
System.out.println(bool);

}catch(DAOException daoe)
{
System.out.println(daoe.getMessage());
}





}


}