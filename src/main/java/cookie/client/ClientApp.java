package cookie.client;

import java.io.Console;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientApp {
    public static void main(String[] args) {
        if(args.length <= 0) {
            System.out.println("Usage: java ... .ClientApp <host>:<port>\n");
            return;
        } 

        String host = args[0].split(":")[0];
        int serverPort = Integer.parseInt(args[0].split(":")[1]);

        InputStream is = null;
        OutputStream os = null;
        Socket socket = null;

        try {
            socket = new Socket(host, serverPort);
            is = socket.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            os = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);

            Console cons = System.console();

            boolean isConnected = true;

            while(isConnected) {
                String input = cons.readLine("Send a command to the cookie server. Available commands:\n(1) get-cookie\n(2) close\n(3) exit\n> ");

                if(input.contains("get-cookie")) {
                    dos.writeUTF("get-cookie");
                    dos.flush();
                    System.out.println(dis.readUTF().replace("cookie-text ", ""));
                    
                } else if(input.contains("close")) {
                    dos.writeUTF("close");
                    dos.flush();
                    isConnected = false;
                } else if(input.contains("exit")) {
                    dos.writeUTF("exit");
                    dos.flush();
                    isConnected = false;
                } else {
                    System.out.println("Invalid input!");
                }
            }

            dos.close();
            dis.close();
            is.close();
            os.close();
            socket.close();
    
        } catch(IOException e) {
            e.printStackTrace();
        }

    }
}
