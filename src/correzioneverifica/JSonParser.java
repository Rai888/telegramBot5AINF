/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package correzioneverifica;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;
import static javax.swing.UIManager.getString;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author raimondi_riccardo
 */
public class JSonParser {

    public JSonParser() {

    }

    public String getToken(String username, String password) throws MalformedURLException, IOException {
        URL fileUrl = new URL("https://savestrings.netsons.org//SaveStrings/getToken.php?username=" + URLEncoder.encode(username, StandardCharsets.UTF_8) + "&password=" + URLEncoder.encode(password, StandardCharsets.UTF_8));
        Scanner inRemote = new Scanner(fileUrl.openStream());
        inRemote.useDelimiter("\u001a");
        String content = inRemote.next();
        String jsonString = content;
        JSONObject obj = new JSONObject(jsonString);
        JSONObject result = obj.getJSONObject("result");
        return result.getString("token");
        //return obj;

        //System.out.println(obj.getJSONObject("result"));
    }

    public void register(String username, String password) throws MalformedURLException, IOException {
        URL fileUrl = new URL("https://savestrings.netsons.org//SaveStrings/register.php?username=" + URLEncoder.encode(username, StandardCharsets.UTF_8) + "&password=" + URLEncoder.encode(password, StandardCharsets.UTF_8));
        Scanner inRemote = new Scanner(fileUrl.openStream());
        inRemote.useDelimiter("\u001a");
        String content = inRemote.next();
        String jsonString = content;
        JSONObject obj = new JSONObject(jsonString);

        //System.out.println(obj.getJSONObject("status"));
        String temp = obj.getString("status");
        if ("ok".equals(temp)) {
            System.out.println("Token:" + getToken(username, password));
        } else if ("error".equals(temp)) {
            System.out.println("Utente gia registrato");
        }
    }

    public void setString(String token , String key, String citta) throws MalformedURLException, IOException {
        URL fileUrl = new URL("https://savestrings.netsons.org//SaveStrings/setString.php?token=" + token + "&key="+key+"&string=" + URLEncoder.encode(citta, StandardCharsets.UTF_8));
        Scanner inRemote = new Scanner(fileUrl.openStream());
        inRemote.useDelimiter("\u001a");
        String content = inRemote.next();
        String jsonString = content;
        JSONObject obj = new JSONObject(jsonString);
    }

    public String getString(String token, String key) throws MalformedURLException, IOException {

        URL fileUrl = new URL("https://savestrings.netsons.org//SaveStrings/getString.php?token=" + token + "&key=" + key);
        Scanner inRemote = new Scanner(fileUrl.openStream());
        inRemote.useDelimiter("\u001a");
        String content = inRemote.next();
        String jsonString = content;
        JSONObject obj = new JSONObject(jsonString);
        JSONObject result = obj.getJSONObject("result");
        //System.out.println(result.getString("string"));
        return result.getString("string");
    }

    public void deleteString(String token , String key) throws MalformedURLException, IOException {
        URL fileUrl = new URL("https://savestrings.netsons.org//SaveStrings/deleteString.php?token=" + token + "&key=" + key);
        Scanner inRemote = new Scanner(fileUrl.openStream());
        inRemote.useDelimiter("\u001a");
        String content = inRemote.next();
        String jsonString = content;
        JSONObject obj = new JSONObject(jsonString);
        System.out.println("stringa eliminata");
    }

    public ArrayList<String> getKeys(String token) throws MalformedURLException, IOException {
        ArrayList<String> list = new ArrayList();
        URL fileUrl = new URL("https://savestrings.netsons.org//SaveStrings/getKeys.php?token=" + token);
        Scanner inRemote = new Scanner(fileUrl.openStream());
        String content = inRemote.next();
        String jsonString = content;
        JSONObject obj = new JSONObject(jsonString);
        JSONArray result = obj.getJSONArray("result");
        for (int i = 0; i < result.length(); i++) {
            list.add(result.getString(i));
        }
        return list;
    }

}
