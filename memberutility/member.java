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

// .....

private static void add(String data[])
{
 if(data.length!=5) 
 {
    System.out.println("* Usage: 'contact' 'name' 'course' 'fee'");
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
 File file=new File(fileName);
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
randomAccessFile.close();
}catch(IOException ioe)
{
    System.out.println(ioe.getMessage());
}
System.out.println("Member added");
}

private static void update(String data[])
{

    if(data.length!=5)
    {
        System.out.println("* Usage: 'java memberutility update contact newName newCourseName fee'");
        return;
    }
    String contact;
    String name;
    String course;
    String fee;
    contact=data[1];
    name=data[2];
    course=data[3];
    fee=data[4];
    if(!isCourseValid(course))
    {
        return;
    }

    try
    {
        File file=new File(fileName);
        RandomAccessFile randomAccessFile= new RandomAccessFile(file,"rw");
        boolean flag=false;
        String fContact="";
        String fName="";
        String fCourse="";
        int fFee=0;
        while(randomAccessFile.getFilePointer()<randomAccessFile.length())
        {
            fContact=randomAccessFile.readLine();
            if(fContact.equalsIgnoreCase(contact))
            {
                flag=true;
                fName=randomAccessFile.readLine();
                fCourse=randomAccessFile.readLine();
                fFee=Integer.parseInt(randomAccessFile.readLine());
                break;
            }
            randomAccessFile.readLine();
            randomAccessFile.readLine();
            randomAccessFile.readLine();
        }
        if(flag==false)
        {
         System.out.printf("Member with contact: (%s) is not added, So cannot be updated\n",contact);
         randomAccessFile.close();
         return;
        }

        System.out.printf("Member with Contact: (%s)  Name: (%s) Course: (%s) Fee: (%d)\nupdated to\n",fContact,fName,fCourse,fFee);
        System.out.printf("Name: (%s) Course: (%s) Fee: (%s)\n",name,course,fee);

        randomAccessFile.seek(0);

        File tmpFile=new File("tmpMember.data");
        tmpFile.delete();
        RandomAccessFile tmpRandomAccessFile=new RandomAccessFile(tmpFile,"rw");
        
        String fFees="";
        while(randomAccessFile.getFilePointer()<randomAccessFile.length())
        {
            fContact=randomAccessFile.readLine();
            fName=randomAccessFile.readLine();
            fCourse=randomAccessFile.readLine();
            fFees=randomAccessFile.readLine();
            
            if(!fContact.equalsIgnoreCase(contact))
            {
            tmpRandomAccessFile.writeBytes(fContact+"\n");
            tmpRandomAccessFile.writeBytes(fName+"\n");
            tmpRandomAccessFile.writeBytes(fCourse+"\n");
            tmpRandomAccessFile.writeBytes(fFees+"\n");
            }
            else
            {
                tmpRandomAccessFile.writeBytes(contact+"\n");
                tmpRandomAccessFile.writeBytes(name+"\n");
                tmpRandomAccessFile.writeBytes(course+"\n");
                tmpRandomAccessFile.writeBytes(fee+"\n");

            }
           
        }
        randomAccessFile.close();
        tmpRandomAccessFile.close();
        file.delete();
        tmpFile.renameTo(new File(fileName));
       
       
    }catch(IOException ioe)
    {
        System.out.println(ioe.getMessage());

    }    

}

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
    File file=new File(fileName);
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
{
    if(data.length!=2)
    {
        System.out.println("Invalid Opearation");
        System.out.println("Usage: java memberutility getByCourse 'course name'");
        return;
    }
    String course;
    course=data[1];
    course=course.trim();
    if(!isCourseValid(course))
    {
        return;
    }
    try
    {
        File file=new File(fileName);
        if(!file.exists()) 
        {
            System.out.println("No members added");
            return;
        }

        RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
        String fContact;
        String fName;
        String fCourse;
        int fFee;
        int totalFee=0;
        int totalRegistration=0;

 
        while(randomAccessFile.getFilePointer()<randomAccessFile.length())
        {
            fContact=randomAccessFile.readLine();
            fName=randomAccessFile.readLine();
            fCourse=randomAccessFile.readLine();
            fFee=Integer.parseInt(randomAccessFile.readLine());
            if(course.equalsIgnoreCase(fCourse))
            {
                System.out.printf("Contact: (%s), Name: (%s) Course: (%s) fee: (%d)\n",fContact,fName,fCourse,fFee);
                ++totalRegistration;
                totalFee+=fFee;
            }

        }

        if(totalRegistration==0)
        {
            System.out.printf("No member registered for course : '%s'\n",course);
            randomAccessFile.close();
            return;
        }

        System.out.printf("Total registration in '%s': '%d'\n",course,totalRegistration);
        System.out.println("Total fee collected for "+course+": "+totalFee);
        randomAccessFile.close();
    }catch(IOException ioe)
    {
        System.out.println(ioe.getMessage());
        return;
    }
    

}

private static void getByContactNumber(String data[])
{
    if(data.length!=2)
    {
        System.out.println("Invalid Opearation");
        System.out.println("Usage: 'java memberutility gtByContactNumber contact'");
        return;
    }
    String contact=data[1];
    String fContact="";
    String fName="";
    String fCourse="";
    int fFee=0;
    int flag=0;
    try
    {
       
        File file=new File(fileName);
        RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
        if(randomAccessFile.length()==0)
        {
            randomAccessFile.close();
            System.out.println("No members added");
            return;
        }
        while(randomAccessFile.getFilePointer()<randomAccessFile.length())
        {
            fContact=randomAccessFile.readLine();
            if(!contact.equalsIgnoreCase(fContact))
            {
                randomAccessFile.readLine();
                randomAccessFile.readLine();
                randomAccessFile.readLine();
                continue;
            }
            fName=randomAccessFile.readLine();
                  
            fCourse=randomAccessFile.readLine();
            fFee=Integer.parseInt(randomAccessFile.readLine());
            flag=1;
            break;
        }
        
        if(flag==0)
        {
            System.out.printf("Invalid contact number: '%s'\n",contact);
            randomAccessFile.close();
            return;
        }

        System.out.printf("Contact: (%s), Name: (%s) Course: (%s) fee: (%d)\n",fContact,fName,fCourse,fFee);
        randomAccessFile.close();
    }catch(IOException ioe)
    {
        System.out.println(ioe.getMessage());
        return;
    }




}

private static void getByName(String data[])
{
    if(data.length!=2)
    {
        System.out.println("Usage: 'java memberutility getByName name'");
        return;
    }
    String name=data[1];
    name=name.trim();
    try
    {
        File file=new File(fileName);
        RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
        if(randomAccessFile.length()==0)
        {
            randomAccessFile.close();
            System.out.println("No members added");
            return;
        }
        String fContact;
        String fName;
        String fCourse;
        int fFee;
        boolean flag=false;
        while(randomAccessFile.getFilePointer()<randomAccessFile.length())
        {
            fContact=randomAccessFile.readLine();
            fName=randomAccessFile.readLine();
            fCourse=randomAccessFile.readLine();
            fFee=Integer.parseInt(randomAccessFile.readLine());
            if(fName.equalsIgnoreCase(name))
            {
                System.out.printf("Contact: (%s), Name: (%s) Course: (%s) fee: (%d)\n",fContact,fName,fCourse,fFee);
                flag=true;
            }
        }
        if(flag==false)
        {
            System.out.printf("Member with name: '%s' not exists\n",name);

        }

        randomAccessFile.close();
    }catch(IOException ioe)
    {
        System.out.println(ioe.getMessage());
        return;
    }



}

private static void remove(String data[])
{
    if(data.length!=2)
    {
        System.out.println("Usage: 'java memberutility remove contact'");
        return;
    }
    String contact;
    contact=data[1];
    try
    {
        File file=new File(fileName);
        RandomAccessFile randomAccessFile= new RandomAccessFile(file,"rw");
        boolean flag=false;
        String fContact="";
        String fName="";
        String fCourse="";
        int fFee=0;
        while(randomAccessFile.getFilePointer()<randomAccessFile.length())
        {
            fContact=randomAccessFile.readLine();
            if(fContact.equalsIgnoreCase(contact))
            {
                flag=true;
                fName=randomAccessFile.readLine();
                fCourse=randomAccessFile.readLine();
                fFee=Integer.parseInt(randomAccessFile.readLine());
                break;
            }
            randomAccessFile.readLine();
            randomAccessFile.readLine();
            randomAccessFile.readLine();
        }
        if(flag==false)
        {
         System.out.printf("Member with contact: (%s) is not added\n",contact);
         randomAccessFile.close();
         return;
        }

        System.out.printf("Member with Contact: (%s)  Name: (%s) Course: (%s) Fee: (%d)\nremoved\n",fContact,fName,fCourse,fFee);

        randomAccessFile.seek(0);

        File tmpFile=new File("tmpMember.data");
        tmpFile.delete();
        RandomAccessFile tmpRandomAccessFile=new RandomAccessFile(tmpFile,"rw");

        String fFees="";
        while(randomAccessFile.getFilePointer()<randomAccessFile.length())
        {
            fContact=randomAccessFile.readLine();
            fName=randomAccessFile.readLine();
            fCourse=randomAccessFile.readLine();
            fFees=randomAccessFile.readLine();
            
            if(!fContact.equalsIgnoreCase(contact))
            {
            tmpRandomAccessFile.writeBytes(fContact+"\n");
            tmpRandomAccessFile.writeBytes(fName+"\n");
            tmpRandomAccessFile.writeBytes(fCourse+"\n");
            tmpRandomAccessFile.writeBytes(fFees+"\n");
            }
           
        }
        randomAccessFile.close();
        tmpRandomAccessFile.close();
        file.delete();
        tmpFile.renameTo(new File(fileName));
       
       
    }catch(IOException ioe)
    {
        System.out.println(ioe.getMessage());

    }    
}


}