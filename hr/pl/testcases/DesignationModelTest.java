import java.awt.*;
import javax.swing.*;
import com.hr.pl.model.*;


class DesignationModelTest extends JFrame
{
private JTable table;
private DesignationModel designationModel;

private JScrollPane jsp;
private Container container;

DesignationModelTest()
{

designationModel=new DesignationModel();

table=new JTable(designationModel);

jsp=new JScrollPane(table,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

container=getContentPane();
container.setLayout(new BorderLayout());
container.add(jsp);

setLocation(400,300);
setSize(600,600);
setVisible(true);


}





public static void main(String gg[])
{
DesignationModelTest dmt=new DesignationModelTest();
}
}