import java.io.*;
import java.nio.channels.UnsupportedAddressTypeException;
class memberutility
{
private static String Course[]={"c","c++","java","python","J2EE"};
private static String Operations[]={"add","update","getAll","getByCourse","getByContactNumber","getByName","remove"};
private static String fileName="member.data";


public static void main(String gg[])
{

if(gg.length==0)
{
System.out.println("Please specify operation name");
System.out.print("* Usage: java memberutitlity 'operation name'\n\n* operation name={'");
for(int i=0; i<Operations.length; ++i)
{
System.out.printf("'%s' ",Operations[i]);
if(i<Operations.length-1) System.out.print("or ");
}
System.out.printf("}\n");
return;
}
String operation=gg[0];
operation=operation.trim();
if(!isOperationValid(operation))
{
return;
}

if(operation.equalsIgnoreCase("add")) add(gg);
else if(operation.equalsIgnoreCase("update")) update(gg);
else if(operation.equalsIgnoreCase("getAll")) getAll(gg);
else if(operation.equalsIgnoreCase("getByCourse")) getByCourse(gg);
else if(operation.equalsIgnoreCase("getByContactNumber")) getByContactNumber(gg);
else if(operation.equalsIgnoreCase("getByName")) getByName(gg);
else if(operation.equalsIgnoreCase("remove")) remove(gg);


}

// validity checking by boolean
private static boolean isOperationValid(String operation)
{
    int i;
    for(i=0; i<Operations.length; ++i) if(Operations[i].equalsIgnoreCase(operation)) break;

    if(i==Operations.length) 
    {
        System.out.printf("Error : '%s' is invalid operation\n",operation);
        System.out.print("* Valid opertions={ ");
        for(i=0; i<Operations.length; ++i)
        {

            System.out.printf("'%s' ",Operations[i]);
            if(i<Operations.length-1) System.out.print("or ");

        }
        System.out.println("}");
        return false;
    }
    return true;
}

private static boolean isCourseValid(String course)
{
    int i;
    for(i=0; i<Course.length; ++i) if(course.equalsIgnoreCase(Course[i])) break;
    
    if(i==Course.length) 
    {
        System.out.printf("Error : '%s' is invalid Course\n",course);
        System.out.print("* Valid Course={ ");
        for(i=0; i<Course.length; ++i)
        {

            System.out.printf("'%s' ",Course[i]);
            if(i<Course.length-1) System.out.print("or ");

        }
        System.out.println("}");
        return false;
    }
    return true;
}



private static void add(String data[])
{
 if(data.length!=5) 
 {
    System.out.println("Usage: 'contact' 'name' 'course' 'fee'");
    return;
 }
 String contact=data[1];
 String name=data[2];
 String course=data[3];
 if(!isCourseValid(course))
 {
    return;
 }
 int fee=0;
 
 try
 {
 fee=Integer.parseInt(data[4]);
 }catch(NumberFormatException nfe)
 {
    System.out.printf("Invalid fee : '%s' \n* fee should be of integer type value\n",data[4]);
    return;
 }

 try{
 File file=new File("fileName");
 RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
 String fContact;
 while(randomAccessFile.getFilePointer()<randomAccessFile.length())
 {
    fContact=randomAccessFile.readLine();
    if(contact.equalsIgnoreCase(fContact))
    {
        randomAccessFile.close();
        System.out.printf("Error\n*Contact : '%s' is already exists\n",contact);
        return;
    }
    randomAccessFile.readLine();
    randomAccessFile.readLine();
    randomAccessFile.readLine();
 }
randomAccessFile.writeBytes(contact);
randomAccessFile.writeBytes("\n");
randomAccessFile.writeBytes(name);
randomAccessFile.writeBytes("\n");
randomAccessFile.writeBytes(course);
randomAccessFile.writeBytes("\n");
randomAccessFile.writeBytes(String.valueOf(fee));
randomAccessFile.writeBytes("\n");
 }catch(IOException ioe)
 {
    System.out.println(ioe.getMessage());
 }
System.out.println("Member added");
}

private static void update(String data[])
{}

private static void getAll(String data[])
{
    if(data.length!=1)
    {
        System.out.println("Invalid Opearation");
        System.out.println("Usage: 'java memberutility getAll'");
        return;
    }
    try
    {
    String contact;
    String name;
    String course;
    int fee;
    File file=new File("fileName");
    if(!file.exists()) 
    {
        System.out.println("No members added");
        return;
    }
    RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
    if(randomAccessFile.length()==0)
    {
        System.out.println("No members added");
        randomAccessFile.close();
        return;
    }
    int i=1;
    int totalFee=0;
    while(randomAccessFile.getFilePointer()<randomAccessFile.length())
    {
        contact=randomAccessFile.readLine();
        name=randomAccessFile.readLine();
        course=randomAccessFile.readLine();
        fee=Integer.parseInt(randomAccessFile.readLine());
        System.out.printf("%d) Contact: (%s), Name: (%s) Course: (%s) fee: (%d)\n",i,contact,name,course,fee);
        ++i;
        totalFee+=fee;
    }
    System.out.println("Total registrations : "+(i-1));
    System.out.println("Total fee collected : "+totalFee);
    randomAccessFile.close();
    }catch(IOException ioe)
    {
     System.out.println(ioe.getMessage());
    }


    

}

private static void getByCourse(String data[])
{}

private static void getByContactNumber(String data[])
{}

private static void getByName(String data[])
{}

private static void remove(String data[])
{}


}