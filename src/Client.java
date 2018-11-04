import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    
    final static int serverPort = 6789;
    
    public static void main(String args[]) throws UnknownHostException, IOException
    {
        Socket clientSocket = new Socket("localhost", 7777);
        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(System.in));
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        InetAddress ipAddress = InetAddress.getByName("localhost");
        Socket socket = new Socket(ipAddress, serverPort);
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Your Name for LOGIN:");
        String name = inFromClient.readLine();
        System.out.println("Enter Your Gmail:");
        String mail = inFromClient.readLine();
        System.out.println("Enter Your Password:");
        String password = inFromClient.readLine();
        outToServer.writeBytes(name + '\n');
        outToServer.writeBytes(mail + '\n');
        outToServer.writeBytes(password + '\n');
        
        Thread MessageSend = new Thread(new Runnable()
        {
            public void run()
            {
                while(true)
                {
                   String message = sc.nextLine();
                   try{
                      out.writeUTF(message);
                   }catch(IOException e){
                       e.printStackTrace();
                   }
                }
            }
        });
        
        Thread MessageRead = new Thread(new Runnable()
        {
            public void run()
            {
                while(true)
                {
                    try{
                        String msg = in.readUTF();
                        System.out.println(msg);
                    }catch(IOException e){
                        e.printStackTrace();
                    }      
                }
            }
        });        
        MessageSend.start();
        MessageRead.start();   
    }  
}
