package com.project42.iplanner.DBConn;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SQL_Post{
    public static void main(String r[]){
        HttpURLConnection conn=null;
        try{
            URL url = new URL("mywebsite.com/postdb.php");
            String agent="Applet";
            String query="query=" + r[0];
            String type="application/x-www-form-urlencoded";
            conn=(HttpURLConnection)url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty( "User-Agent", agent );
            conn.setRequestProperty( "Content-Type", type );
            conn.setRequestProperty( "Content-Length", ""+query.length());

            OutputStream out=conn.getOutputStream();
            out.write(query.getBytes());
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            while((inputLine=in.readLine())!=null){
                System.out.print(inputLine+"\n");
            };
            in.close();
            int rc = conn.getResponseCode();
            System.out.print("Response Code = "+rc+"\n");
            String rm=conn.getResponseMessage();
            System.out.print("Response Message = "+rm+"\n");
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            conn.disconnect();
        }
    }

}
