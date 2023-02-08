package cookie.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Cookie {
    private static List<String> cookies = new ArrayList<>();
    private static boolean areCookiesLoaded = false;

    public static void loadCookies(String path) {
        if(areCookiesLoaded) {
            return;
        }
        try {
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            String input = "";
    
            while((input = br.readLine()) != null) {
                cookies.add(input);
            }    

            br.close();
            fr.close();

            areCookiesLoaded = true;

        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveNewCookie(String path, String cookieName) {
        try {
            File f = new File(path);
            if(!f.exists()) {
                f.createNewFile();
                FileWriter fw = new FileWriter(path);
                BufferedWriter bw = new BufferedWriter(fw);

                for(int i = 0; i < cookies.size(); i++) {
                    bw.write("\n");
                }
                bw.close();
                fw.close();
            }

            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            String input = "";
            List<String> result = new ArrayList<>();
            
            // for(int i = 0; i < cookies.size(); i++) {
            //     result.add("");
            // }

            while((input = br.readLine()) != null) {
                result.add(input);
            }

            br.close();
            fr.close();

            result.set(cookies.indexOf(cookieName), cookieName);
            
            path = "cookie_result.txt";

            FileWriter fw = new FileWriter(path);
            BufferedWriter bw = new BufferedWriter(fw);

            for(String r : result) {
                bw.write(r + "\n");
            }

            bw.close();
            fw.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static String chooseRandomCookie() {
        if(!areCookiesLoaded) {
            System.err.println("Cookie list is empty!");
            return "";
        }
        Random rand = new Random();
        return cookies.get(rand.nextInt(cookies.size()));
    }

    public static List<String> getCookieList() {
        return cookies;
    } 

    public static void replaceText(String path, String cookieName) {
        try {
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            List<String> content = new ArrayList<>();
            String input;

            while((input = br.readLine()) != null) {
                content.add(input);
            }
            
            br.close();
            fr.close();

            path = "copy of " + path;

            FileWriter fw = new FileWriter(path);
            BufferedWriter bw = new BufferedWriter(fw);

            for(String c : content) {
                bw.write(c.replaceAll("\\$count\\b", cookieName.length() + ""));
            }

            bw.flush();

            bw.close();
            fw.close();
            
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
