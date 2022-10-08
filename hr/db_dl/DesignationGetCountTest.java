import com.hr.dl.exceptions.*;
import com.hr.dl.dao.*;
class DesignationGetCountTest
{
public static void main(String gg[])
{
try
{
DesignationDAO designationDAO=new DesignationDAO();
int recordCount=designationDAO.getCount();
System.out.println("No. of records: "+recordCount);
}catch(DAOException daoe)
{
System.out.println(daoe.getMessage());
}
}

};