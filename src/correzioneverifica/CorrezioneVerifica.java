/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package correzioneverifica;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author raimondi_riccardo
 */
public class CorrezioneVerifica {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws MalformedURLException, ParserConfigurationException, SAXException, IOException {
        
        Scanner s = new Scanner(System.in);
        

        JSonParser j = new JSonParser();
        openStreet o = new openStreet();
        String token = "";
        String username = "";
        String pass = "";
        String tappa = "";

        Boolean bool = true;
        do {
            s = new Scanner(System.in);

            System.out.println("Vuoi registrarti o accedere? 1-Registrati 2-Accedi");
            switch (s.nextInt()) {
                case 1:
                    System.out.println("inserisci username: ");
                    username = s.next();
                    System.out.println("inserisci password: ");
                    pass = s.next();
                    j.register(username, pass);
                    token = j.getToken(username, pass);
                    bool = false;
                    break;
                case 2:
                    System.out.println("inserisci username: ");
                    username = s.next();
                    System.out.println("inserisci password: ");
                    pass = s.next();
                    j.getToken(username, pass); //vedere accedi
                    token = j.getToken(username, pass);
                    bool = false;
                    break;
                //default:
                // break;
            }
        } while (bool);

        Boolean finito = false;
        do {
            System.out.println("Scegli cosa fare: 1-inserisci tappa; 2-visualizza tappa; 3-Cancella tappa; 4-swap; 5-distanza; 6-cancella itinerario; 0-esci");
            
            s = new Scanner(System.in);
            int val = s.nextInt();
            s.nextLine();
            switch (val) {
                case 1:
                    ArrayList<String> list = new ArrayList();//lista per keys
                    list = j.getKeys(token);
                    int max = Integer.parseInt(list.get(0));
                    for (int i = 0; i < list.size(); i++) {
                        if (Integer.parseInt(list.get(i)) > max) {
                            max = Integer.parseInt(list.get(i));
                        }
                    }

                    System.out.println("inserisci tappa: ");
                    tappa = s.next();
                    j.setString(token, (max + 1) + "", tappa);
                    break;
                case 2:
                    ArrayList<String> tappe = new ArrayList();
                    ArrayList<String> list2 = new ArrayList(); //liste per tappe e per keys
                    list2 = j.getKeys(token);
                    for (int i = 0; i < list2.size(); i++) {
                        tappe.add(j.getString(token, list2.get(i)));
                    }
                    System.out.println("Visualizza tappe: ");
                    for (int i = 0; i < tappe.size(); i++) {
                        System.out.println(tappe.get(i));
                    }
                    break;
                case 3:
                    System.out.println("Cancella tappa");
                    String tElimina = s.next();
                    j.deleteString(token, tElimina);
                    break;
                case 4:
                    List<String> keys = j.getKeys(token);
                    for (int i = 0; i < keys.size(); i++) {
                        String str = j.getString(token, keys.get(i));
                        System.out.println(keys.get(i) + " - " + str);
                    }

                    String key1 = s.nextLine();
                    String key2 = s.nextLine();

                    String val1 = j.getString(token, key1);
                    String val2 = j.getString(token, key2);

                    j.setString(token, key1, val2);
                    j.setString(token, key2, val1);

                    break;

                case 5:
                    //prendiamo tutte le keys
                    List<String> keys2 = j.getKeys(token);
                    //per ogni citta calcoliamo la distanza
                    Double distance = 0.0;
                    for (int i = 0; i < keys2.size() - 1; i++) {
                        String city1 = j.getString(token, keys2.get(i));
                        String city2 = j.getString(token, keys2.get(i + 1));
                        distance += o.distanceBetweenTwoCity(city1, city2);
                    }
                    System.out.println(distance);
                    break;

                case 6:
                    List<String> key = j.getKeys(token);
                    for (int i = 0; i < key.size(); i++) {
                        j.deleteString(token, key.get(i));
                    }
                    break;

                case 0:
                    finito = true;
                    break;
            }
        } while (!finito);

    }

}
