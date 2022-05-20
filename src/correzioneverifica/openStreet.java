/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package correzioneverifica;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author raimondi_riccardo
 */
public class openStreet {

    public openStreet() {

    }

    
    //returna una lista di Places
    public static List<Place> getPlaces(String ricerca) throws UnsupportedEncodingException, MalformedURLException, ParserConfigurationException, SAXException, IOException {
        String url = "https://nominatim.openstreetmap.org/search?q=";
        String path = URLEncoder.encode(ricerca, "UTF-8");
        url += path + "&format=xml&addressdetails=1";

        URL fileUrl = new URL(url);

        Element root = getRoot(fileUrl);

        List<Place> tmp = new ArrayList<>();
        NodeList listaDiPlace = root.getElementsByTagName("place");
        int numEl = listaDiPlace.getLength();

        for (int i = 0; i < numEl; i++) {
            Element place = (Element) listaDiPlace.item(i);

            //parsing...
            Place p = parseXMLPlace(place);
            //mi salvo un oggetto Place con tutte le info un una lista
            tmp.add(p);
        }

        return tmp;
    }

    
    public static Element getRoot(URL url) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory factory;
        DocumentBuilder builder;
        Element root, element;
        NodeList nodelist;

        Document document;

        factory = DocumentBuilderFactory.newDefaultInstance();
        builder = factory.newDocumentBuilder();
        document = builder.parse(url.openStream());
        root = document.getDocumentElement();
        return root;
    }

    
    //parse di un place in XML
    public static Place parseXMLPlace(Element place) {

        double lat = Double.parseDouble(place.getAttribute("lat"));
        double lon = Double.parseDouble(place.getAttribute("lon"));

        Place p = new Place(lat, lon);

        p.setCity(contenuto(place, "city"));
        p.setTown(contenuto(place, "town"));
        return p;

    }

    private static String contenuto(Element place, String tag) {
        //restituire il contenuto di place con il mio tag
        NodeList listaDiTown = place.getElementsByTagName(tag);
        if (listaDiTown.getLength() != 0) {
            return listaDiTown.item(0).getTextContent();
        }
        return "";
    }

    //resituisce la distanza tra due città
    public Double distanceBetweenTwoCity(String cityA, String cityB) {
        Double distanceKM = 0.0;
        Double[] coordinateA = getCoordinates(cityA), coordinateB = getCoordinates(cityB);
        Double latA, lonA, latB, lonB;
        latA = Math.toRadians(coordinateA[0]);
        lonA = Math.toRadians(coordinateA[1]);
        latB = Math.toRadians(coordinateB[0]);
        lonB = Math.toRadians(coordinateB[1]);
        double dlon = lonB - lonA;
        double dlat = latB - latA;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(latA) * Math.cos(latB)
                * Math.pow(Math.sin(dlon / 2), 2);
        double ca = 2 * Math.asin(Math.sqrt(a));
        distanceKM = ca * 6371;
        return distanceKM;
    }

    //restituisce le coordinate di una città
    public Double[] getCoordinates(String city) {
        Double[] coordinates = new Double[2]; //posizione 0 -> latitudine | posizione 1 -> longitudine
        try {
            URL fileUrl = new URL("https://nominatim.openstreetmap.org/search?q=" + URLEncoder.encode(city, StandardCharsets.UTF_8) + "&format=xml&addressdetails=1");
            Scanner inRemote = new Scanner(fileUrl.openStream());
            inRemote.useDelimiter("\u001a");
            String content = inRemote.next();
            Document doc = convertStringToXMLDocument(content);
            NodeList nodelist = doc.getElementsByTagName("place");
            Element element = (Element) nodelist.item(0);
            coordinates[0] = Double.parseDouble(element.getAttribute("lat"));
            coordinates[1] = Double.parseDouble(element.getAttribute("lon"));
        } catch (IOException ex) {
            Logger.getLogger(openStreet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return coordinates;
    }

    
    //parsa una stringa in file XML
    private Document convertStringToXMLDocument(String xmlString) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
