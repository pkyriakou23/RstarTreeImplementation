import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;

/**
 * Κλάση η οποία διαβάζει ένα αρχείο OSM και δημιουργεί καινούργια βάση δεδομένων
 */
public class LoadOSM {
    //μετράει πάντα τον αριθμό των μπλοκς που έχουμε
    public int blockID;

    public LoadOSM()  {
        blockID = 0;
    }

    /**
     * Συνάρτηση που φορτώνει δεδομένα από ένα OSM αρχείο και τα αποθηκεύει στο αρχείο datafile ως μια σειρά από bytes
     */
    public void load_osmlife() throws IOException, SAXException, ParserConfigurationException {

        Tree tree = new Tree(new TreeNode(Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE));
            FileOutputStream fos = new FileOutputStream("datafile.txt");
            DataOutputStream dos = new DataOutputStream(fos);
            //τα πρώτα 4 ψηφία είναι το blockid
            dos.writeChar('b');
            dos.writeChar('0');
            dos.writeChar('0');
            dos.writeChar('0');
            dos.writeInt(1234);   //συνολικός αριθμός από μπλοκς
            dos.writeInt(1234);   //συνολικός αριθμός από εισαγωγές
            dos.writeInt(1365);
            //Κάθε εισαγωγή περιλαμβάνει 24 bytes
            //άρα για να δημιουργήσουμε μπλοκς με χωρητικότητα 32ΚΒ πρέπει να έχουμε 1365 εισαγωγές
            //επιπλέον τα 8 πρώτα bytes του κάθε μπλοκ αντιπροσωπεύουν το blockid
        //εμφάνιση μπλοκ id++
        int blocks_Number = 1;
            dos.writeChar('b');
            dos.writeChar('0');
            dos.writeChar('0');
            dos.writeChar((char) blocks_Number);
            int count = 0;

        //δημιουργία document builder
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File("map3.osm"));
        //Normalize the XML Structure
        document.getDocumentElement().normalize();
        //αρχικοποίηση ρίζας
        Element root = document.getDocumentElement();

        //δημιουργία node list
        NodeList nodeList = document.getElementsByTagName("node");

        for (int temp = 0; temp < nodeList.getLength(); temp++) {
            Node node = nodeList.item(temp);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                //ο αριθμός εισαγωγών σε κάθε μπλοκ είναι 1365
                //αρα το μέγεθος του κάθε μπλοκ είναι 32ΚΒ
                if (count == 1365) {
                    count = 0;
                    blocks_Number++;
                    //εμφάνιση του μπλοκ id
                    dos.writeChar('b');
                    //όταν αλλάζουμε μπλοκ, εμφανίζεται το id του καινούργιου μπλοκ στην αρχή
                    if (blocks_Number < 10) {
                        dos.writeChar('0');
                        dos.writeChar('0');
                        dos.writeChar((char) blocks_Number);
                    } else {
                        String bns = String.valueOf(blocks_Number);
                        char[] tmp = bns.toCharArray();
                        if (blocks_Number < 99) {
                            dos.writeChar('0');
                            dos.writeChar(tmp[0]);
                            dos.writeChar((char) tmp[1]);
                        } else {
                            dos.writeChar((char) tmp[0]);
                            dos.writeChar((char) tmp[1]);
                            dos.writeChar((char) tmp[2]);
                        }
                    }

                }
                //εμφάνιση κάθε σημείου
                Element eElement = (Element) node;
                long id = Long.parseLong(eElement.getAttribute("id"));
                double lat = Double.parseDouble(eElement.getAttribute("lat"));
                double lon = Double.parseDouble(eElement.getAttribute("lon"));
                dos.writeLong(id);
                dos.writeDouble(lat);
                dos.writeDouble(lon);
                //πρσθήκη του σημείου στο δέντρο
                tree.insert_in_Rtree(new Point(lat,lon,blocks_Number,count));
                count++;
            }
        }
        //αποθήκευση του δέντρου στο Indexfile
        tree.write_in_file();
    }

    /**
     * Συνάρτηση που προσθέτει μια καινούργια τοποθεσία στις ήδη υπάρχουσες στο δέντρο
     * @param location η τοποθεσία που θέλουμε να προσθέσουμε
     * @param locations όλες οι τοποθεσίες που υπάρχουν
     */
    public void add_location(Location location, ArrayList<Location> locations) {
        //προσθέτουμε την τοποθεσία στη λίστα με τις υπόλοιπες τοποθεσίες
        //επαναλαμβάνουμε την ίδια διαδικασία με πριν
        locations.add(location);
        int size = locations.size();
        try {
            FileOutputStream fos = new FileOutputStream("datafile.txt");
            DataOutputStream dos = new DataOutputStream(fos);
            //τα πρώτα 4 ψηφία αντιπροσωπεύουν το blockid
            dos.writeChar('b');
            dos.writeChar('0');
            dos.writeChar('0');
            dos.writeChar('0');
            //Κάθε εισαγωγή περιλαμβάνει 24 bytes
            //άρα για να δημιουργήσουμε μπλοκς με χωρητικότητα 32ΚΒ πρέπει να έχουμε 1365 εισαγωγές
            //επιπλέον τα 8 πρώτα bytes του κάθε μπλοκ αντιπροσωπεύουν το blockid
            dos.writeInt(size/1365+1);
            dos.writeInt(size);
            dos.writeInt(1365);
            int blocks_Number = 1;
            //εμφάνιση μπλοκ id
            dos.writeChar('b');
            dos.writeChar('0');
            dos.writeChar('0');
            dos.writeChar((char) blocks_Number);
            int count = 0;
            for (int x = 0; x < locations.size(); x++) {
                if (count == 1365) {
                    //ο αριθμός εισαγωγών σε κάθε μπλοκ είναι 1365
                    //αρα το μέγεθος του κάθε μπλοκ είναι 32ΚΒ
                    count = 0;
                    blocks_Number++;
                    //εμφάνιση μπλοκ id
                    dos.writeChar('b');
                    if (blocks_Number < 10) {
                        dos.writeChar('0');
                        dos.writeChar('0');
                        dos.writeChar((char) blocks_Number);
                    } else {
                        String bns = String.valueOf(blocks_Number);
                        char[] tmp = bns.toCharArray();
                        if (blocks_Number < 99) {
                            dos.writeChar('0');
                            dos.writeChar((char) tmp[0]);
                            dos.writeChar((char) tmp[1]);
                        } else {
                            dos.writeChar((char) tmp[0]);
                            dos.writeChar((char) tmp[1]);
                            dos.writeChar((char) tmp[2]);
                        }
                    }

                }
                //γράφουμε τα αποτελέσματα στο datafile
                dos.writeLong(locations.get(x).getLocationid());
                dos.writeDouble(locations.get(x).getLat());
                dos.writeDouble(locations.get(x).getLon());
                count++;
            }
            dos.close();
        } catch (
                IOException e) {
            System.out.println("IOException : " + e);


        }

    }
}