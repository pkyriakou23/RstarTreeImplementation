import java.io.*;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.ArrayList;

/**
 * Αυτή η κλάση χρησιμοποιείται για να φορτώσουμε και να διαβάσουμε το ήδη υπάρχον αρχείο datafile
 */
public class LoadData {
    //συνολικός αριθμός από εισαγωγές
    private int total_entries;

    /**
     * Getter για το total_entries
     */
    public int getTotal_entries() {
        return total_entries;
    }

    /**
     * Συνάρτηση που διαβάζει ένα αρχείο με χωρητικότητα 32ΚΒ ανά μπλοκ
     * @return μια Arraylist με εισαγωγές
     */
    public ArrayList<Location> read_data() {
        //λίστα για να βάλω τα στοιχεία που θα διαβάσω από το datafile
        ArrayList<Location> locationsArrayList = new ArrayList<>();
        try {

            DataInputStream stream = new DataInputStream(new FileInputStream("datafile.txt"));
            long id = 0;
            double lat = 0, lon = 0;
            //το πρώτο μπλοκ με τα μεταδεδομένα είναι 20 Bytes
            byte[] metadata = new byte[20];
            //παίρνουμε το πρώτο μπλοκ το οποίο περιέχει 3 μεταβλητές:
            //α) τον συνολικό αριθμό των μπλοκς
            //β) τον συνολικό αριθμό από εισαγωγές
            //γ) τον αριθμό εισαγωγών μέσα στο μπλοκ
            //να σημειωθεί ότι και οι τρεις μεταβλητές αποτελούνται από 4 bytes
            stream.read(metadata, 0, 20);
            byte[] number_of_blocks = new byte[4];
            byte[] number_of_entries = new byte[4];
            byte[] records_per_block = new byte[4];
            int bytes = 0; //μετρητής των bytes
            //να σημειωθεί ότι παραλείπουμε τα 8 πρώτα bytes σε κάθε μπλοκ
            //γιατί τα bytes αυτά είναι τα id του κάθε μπλοκ τα οποία δεν χρειαζόμαστε
            for(int counter=8;counter<20;counter++){
                //ανά 4 bytes αλλάζουν οι μεταβλητές
                if(bytes==4){
                    bytes=0;
                }
                //αποθήκευση του συνολικού αριθμού απο μπλοκς
                if(counter<12){
                    number_of_blocks[bytes]=metadata[counter];
                }
                //αποθήκευση του συνολικού αριθμού εισαγωγών
                else if(counter<16){
                    number_of_entries[bytes]=metadata[counter];
                }
                //αποθήκευση αριθμού εισαγωγών ανα μπλοκ
                else{
                    records_per_block[bytes]=metadata[counter];
                }
                bytes++;
            }
            //Τα δεδομένα έχουν διαβαστεί ως μια σειρά από bytes
            ByteBuffer bfMeta = ByteBuffer.wrap(number_of_blocks);
            IntBuffer lgMeta = bfMeta.asIntBuffer();
            int Blocks_Num = lgMeta.get();
            bfMeta = ByteBuffer.wrap(number_of_entries);
            lgMeta = bfMeta.asIntBuffer();
            total_entries = lgMeta.get();
            bfMeta = ByteBuffer.wrap(records_per_block);
            lgMeta = bfMeta.asIntBuffer();
            int Records_Block = lgMeta.get();
            //η ανάγνωση γίνεται ανα μπλοκ
            for (int o = 0; o < Blocks_Num; o++) {
                //διαβάζονται 32ΚΒ μπλοκ
                byte[] buffer = new byte[32768];
                stream.read(buffer, 0, 32768);
                //κάθε εισαγωγή περιλαμβάνει 3 μεταβλητές από 8 byte η κάθε μία
                byte[][] ids = new byte[Records_Block][8];
                int k = 0;
                int l = 0;
                byte[][] lats = new byte[Records_Block][8];
                byte[][] lots = new byte[Records_Block][8];
                int i = 0;
                int count = 0;
                //παραλείπονται τα πρώτα 8 bytes από κάθε μπλοκ γιατί εκεί δεσμεύεται το id κάθε μπλοκ
                for (int m = 8; m < 32768; m++) {
                    if (i == 24) {
                        count++;
                        i = 0;
                        l = 0;
                        k = 0;
                    }
                    if (i < 8) {
                        ids[count][i] = buffer[m];
                    } else if (i < 16) {
                        lats[count][k] = buffer[m];
                        k++;

                    } else {
                        lots[count][l] = buffer[m];
                        l++;
                    }
                    i++;
                }
                for (int c = 0; c < Records_Block; c++) {
                    ByteBuffer bf = ByteBuffer.wrap(ids[c]);
                    LongBuffer lg = bf.asLongBuffer();
                    long tempId = lg.get();
                    bf = ByteBuffer.wrap(lats[c]);
                    DoubleBuffer bs = bf.asDoubleBuffer();
                    Double tempLot = bs.get();
                    bf = ByteBuffer.wrap(lots[c]);
                    DoubleBuffer bl = bf.asDoubleBuffer();
                    double tempLon = bl.get();
                    if (tempId == 0 && tempLot == 0 && tempLon == 0) {
                        break;
                    }
                    locationsArrayList.add(new Location(tempId, tempLot, tempLon));
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return locationsArrayList;
    }

    /**
     * Συνάρτηση που παίρνει ως ορίσματα ένα συγκεκριμένο blockid και slotid
     * και επιστρέφει την τοποθεσία στην οποία δείχνουν
     * @param blockid το id ενός μπλοκ μέσα στο οποίο αναζητούμε μια τοποθεσία
     * @param slotid το slot στο οποίο έχει αποθηκευτεί η πληροφορία που ψάχνουμε
     * */
    public Location find_block(int blockid, int slotid) {
        RandomAccessFile accessFile = null;
        try {
            accessFile = new RandomAccessFile("datafile.txt", "rw");
            accessFile.skipBytes(20);
            accessFile.skipBytes(32768 * --blockid);
            accessFile.skipBytes(8);
            accessFile.skipBytes(24*slotid);
            long id= accessFile.readLong();
            double lat=accessFile.readDouble();
            double lon=accessFile.readDouble();
            Location location = new Location(id,lat,lon);
            return location;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
