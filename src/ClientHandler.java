
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;


public class ClientHandler implements Runnable {
    
    private String name;
    private String nname;
    private String mail;
    private String password;
    Socket s;
    final DataInputStream in;
    final DataOutputStream out;
    boolean isLoggedIn;
    boolean isFriend;
    
    Scanner sc = new Scanner(System.in);
    
    public ClientHandler(Socket s, String name, String nname, String mail, String password, DataInputStream in, DataOutputStream out)
    {
        this.out = out;
        this.in = in;
        this.name = name;
        this.nname = nname;
        this.mail = mail;
        this.password = password;
        this.s = s;
        this.isFriend = false;
        this.isLoggedIn = true;
    }

    @Override
    public void run() {
       String receivedMessage;
        while (true) {
            try {
                try {
                    receivedMessage = in.readUTF();
                    StringTokenizer st = new StringTokenizer(receivedMessage, "@");
                    String MessageToSend = st.nextToken();
                    String recipient = st.nextToken();
                    System.out.println(this.name);
                    if(MessageToSend.equals("logout"))
                    {
                        out.writeUTF(MessageToSend);
                        isLoggedIn = false;
                    } 
                    
                    
                    else if(MessageToSend.equals("request")) 
                    {
                        for(ClientHandler mc : Server.activeClient) 
                        {
                            if(mc.nname.equals(recipient) && mc.isLoggedIn == true)
                            {
                                mc.out.writeUTF(this.nname + " has requested to accept  friend request.");
                                break;
                            }
                        }

                    } 
                    else if(MessageToSend.equals("Y"))
                    {
                        this.isFriend = true;
                        System.out.print(this.nname);
                        for(ClientHandler mc : Server.activeClient) 
                        {
                            if(mc.nname.equals(recipient) && mc.isLoggedIn == true)
                            {
                                System.out.println(" is friend's now with " + mc.nname);
                                mc.isFriend = true;
                                break;
                            }
                        }
                    } 
                    else if(MessageToSend.equals("show")) 
                    {
                        for(ClientHandler mc : Server.activeClient) 
                        {
                            if(this.nname.equals(mc.nname) != true)
                            {
                                this.out.writeUTF(mc.nname + "\n");  
                            }
                        }    
                    } 
                    
                    else if (recipient.equals("all"))
                    {
                        for (ClientHandler mc : Server.activeClient) 
                        {
                            if (mc.isLoggedIn == true) 
                            {
                                mc.out.writeUTF(this.nname + " : " + MessageToSend);
                            }
                        }
                    }
                    int k = recipient.split(":").length;
                    StringTokenizer stn = new StringTokenizer(recipient, ":");
                    String clients[] = new String[k];
                    for (int i = 0; i < k; i++) 
                    {
                        clients[i] = stn.nextToken();
                    }

                    for (ClientHandler mc : Server.activeClient) 
                    {
                        for (int i = 0; i < k; i++) {
                            if (mc.nname.equals(clients[i]) && mc.isLoggedIn == true && mc.isFriend == true) 
                            {
                                mc.out.writeUTF(this.nname + " : " + MessageToSend);
                                break;
                            }
                        }

                    }
                }catch (EOFException s){
                    System.out.println(this.nname + "logged out");
                    break;
                }
            }catch (IOException e){
                e.printStackTrace();
            }

        }
    }   
}
