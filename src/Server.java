import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;

public class Server {
    static Vector <ClientHandler> activeClient = new Vector<>();
    static int count = 1;
    
    public static void main(String args[]) throws IOException
    {
        ServerSocket serverSocket = new ServerSocket(7777);
        ServerSocket sc = new ServerSocket(6789);
        Socket s;
        
        while(true)
        {
            Socket cs = serverSocket.accept();
            s = sc.accept();
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(cs.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(cs.getOutputStream());
            String name = inFromClient.readLine();
            String mail = inFromClient.readLine();
            String password = inFromClient.readLine();
            System.out.println(name);
            System.out.println(mail);
            System.out.println(password);
            System.out.println("Client Request: " + s);
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            DataInputStream in = new DataInputStream(s.getInputStream());
            ClientHandler ch = new ClientHandler(s, "Client" + count, name, mail, password, in, out); 
            Thread th = new Thread(ch);
            activeClient.add(ch);
            System.out.println(count);
            th.start();
            count++;
        }   
    }
}
