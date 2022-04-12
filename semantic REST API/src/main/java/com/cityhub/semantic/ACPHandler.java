package com.cityhub.semantic;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ACPHandler {

    boolean postAccessDecision(String userName, String accessToken, String operation) throws IOException {

        URL host_URL = new URL(ServerConfiguration.ACP_BASE_URL + "security/accessDecision/" + userName );

        HttpURLConnection con = (HttpURLConnection) host_URL.openConnection();

        con.setDoOutput(true);
        con.setRequestMethod("POST");
        con.setRequestProperty("Authorization","Bearer "+ accessToken );
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");

        String bodyString = "{ \"operation\": \"" + operation + "\" }";

        OutputStream os = con.getOutputStream();
        byte[] bodyByte = bodyString.getBytes("utf-8");
        os.write(bodyByte, 0, bodyByte.length);

        JSONObject json_response = parseResponse( con );

        String grant_value = json_response.get("result").toString();

        // printing result from response
        System.out.println("Response:-" + grant_value);

        boolean granted = false;

        if(grant_value.equals("grant")){
            granted = true;
        }

        return granted;
    }



    JSONObject parseResponse( HttpURLConnection urlConnection  ) throws IOException {

        String json_response = "";
        BufferedReader reader = new BufferedReader( new InputStreamReader(urlConnection.getInputStream()) );
        String text = "";
        while ((text = reader.readLine()) != null) {
            json_response += text;
        }
        reader.close();

        JSONParser parser = new JSONParser();
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse( json_response );

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return json;
    }

}
