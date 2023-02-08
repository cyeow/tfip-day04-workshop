package cookie.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerApp {
    public static void main(String[] args) {
        if(args.length <= 2) {
            System.out.println("Usage: java ... .ServerApp <port no> <cookie file> <cookie replacement file>");
            return;
        }

        System.out.println(args[0]);
        int serverPort = Integer.parseInt(args[0]);
        String COOKIE_FILE = args[1];
        String COOKIE_REPLACEMENT_FILE = args[2];
        String COOKIE_RESULT_FILE = "cookie_result.txt";

        Socket socket = null;
        InputStream is = null;
        OutputStream os = null;

        Cookie.loadCookies(COOKIE_FILE);
        Cookie.chooseRandomCookie();

        boolean acceptClient = true;
    
        try {
            while(acceptClient) {
                ServerSocket server = new ServerSocket(serverPort);
                System.out.printf("Cookie server started at port %d...\n", serverPort);

                socket = server.accept(); 
                is = socket.getInputStream();
                DataInputStream dis = new DataInputStream(is);
                os = socket.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);

                boolean acceptInput = true;

                while(acceptInput) {
                    String dataFromClient = dis.readUTF().toLowerCase();

                    if(dataFromClient.equals("get-cookie")) {
                        String randCookie = Cookie.chooseRandomCookie();
                        System.out.println("Random cookie: " + randCookie);
                        
                        dos.writeUTF("cookie-text " + randCookie);
                        dos.flush();

                        Cookie.saveNewCookie(COOKIE_RESULT_FILE, randCookie);
                        Cookie.replaceText(COOKIE_REPLACEMENT_FILE, randCookie);
                    
                    } else if(dataFromClient.equals("close")){
                        acceptInput = false;
                    
                    } else if(dataFromClient.equals("exit")) {
                        acceptInput = false;
                        acceptClient = false;
                    
                    } else {
                        dos.writeUTF("Invalid input!\n");
                    }
                }

                dis.close();
                dos.close();
                is.close();
                os.close();
                server.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
}
