package com.network.client;
import com.network.common.*;
import com.network.common.exceptions.*;
import java.io.*;
import java.net.*;
public class NetworkClient
{
public Response send(Request request) throws NetworkException
{
    try
    {
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        ObjectOutputStream oos=new ObjectOutputStream(baos);
        oos.writeObject(request);
        oos.flush();

        byte serializedObject[];
        serializedObject=baos.toByteArray();

        Socket socket=new Socket("localhost",5500);
        OutputStream os=socket.getOutputStream();
        InputStream is=socket.getInputStream();

        byte header[];
        header=new byte[1024];
        int length_of_serialized_object=serializedObject.length;

        int l=length_of_serialized_object;
        System.out.println("length_of_serialized_object: "+l);
        for(int x=1023; x>=0; --x)
        {
        header[x]=(byte)(l%10);
        l/=10;
        }

        os.write(header,0,1024);
        os.flush();

        byte ack[]=new byte[1];
        int bytesReadCount;
        while(true)
        {
        bytesReadCount=is.read(ack);
        if(bytesReadCount==-1) continue;
        System.out.println("ack: "+ack[0]);
        break;
        }

        int chunkSize=1024;
        int bytesToSend=length_of_serialized_object;

        for(int i=0; i<bytesToSend; )
        {
        if(bytesToSend-i<chunkSize) chunkSize=bytesToSend-i;
        os.write(serializedObject,i,chunkSize);
        os.flush();
        i+=chunkSize;
        }

        byte tmp[]=new byte[1024];
        int bytesToReceive=1024;
        int i,j,k;
        for(i=0,j=0 ; j<bytesToReceive; ++j)
        {
        bytesReadCount=is.read(tmp);
        System.out.println("bytesReadCount : "+bytesReadCount);
        if(bytesReadCount==-1) continue;

        for(k=0; k<bytesReadCount; ++k)
        {
        header[i]=tmp[k];
        i++;
        }

        j+=bytesReadCount;
        }


        int headerLength=0;
        for(j=1,i=1023; i>0; j*=10,--i )
        {
        headerLength=headerLength+header[i]*j;
        }

        System.out.println("HeaderLength: "+headerLength);


        tmp=new byte[headerLength];
        byte response[]=new byte[headerLength];

        for(k=0,i=0; k<headerLength ; )
        {

        bytesReadCount=is.read(tmp);
        if(bytesReadCount==-1) continue;
        for(j=0; j<bytesReadCount; ++j,i++)
        {
        response[i]=tmp[j];
        }

        k+=bytesReadCount;
        }

        ByteArrayInputStream bais=new ByteArrayInputStream(response);
        ObjectInputStream ois=new ObjectInputStream(bais);

        Response response_obj=(Response)ois.readObject();
        System.out.println("Response is received");

        ack[0]=1;
        os.write(ack,0,1);
        os.flush();


        socket.close();
        return response_obj;
    }catch(Exception e)
    {
       throw new NetworkException(e.getMessage());
    }

    }



}