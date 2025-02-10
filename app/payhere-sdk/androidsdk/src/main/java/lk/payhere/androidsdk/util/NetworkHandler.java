package lk.payhere.androidsdk.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by chamika on 9/26/16.
 */

public class NetworkHandler {

    public static String sendPost(String postUrl, Map<String,String> params) throws IOException {


        //Your server URL
        URL obj = new URL(postUrl);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        //Request Parameters you want to send
        String urlParameters = "";
        if(params != null ){
            StringBuilder sb = new StringBuilder();

            Set<Map.Entry<String, String>> entries = params.entrySet();
            boolean isFirst = true;
            for (Map.Entry<String, String> entry : entries) {
                if(!isFirst){
                   sb.append("&");
                }
                sb.append(entry.getKey());
                sb.append("=");
                sb.append(entry.getValue());
            }

            urlParameters = sb.toString();
        }

        // Send post request
        con.setDoOutput(true);// Should be part of code only for .Net web-services else no need for PHP
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + postUrl);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());
        return response.toString();

    }
}
