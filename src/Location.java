import static java.lang.Math.abs;

/**
 * Η κλάση αυτή αναπαριστά μια τοποθεσία με lat (x) και lon (y) στο osm αρχείο
 */
public class Location implements Comparable<Location>{
    //id του κάθε κόμβου στο αρχείο .osm
    private long locationid;
    //lat και lon κάιθε κόμβου στο αρχείο .osm
    private double lat, lon;
    //απόσταση που απέχει μια τοποθεσία από μια άλλη
    private double distance;

    /**
     * Κατασκευαστής της κλάσης Location με 3 ορίσματα
     * @param locationid το id του κάθε node στο αρχείο .osm
     * @param lat η παράμετρος x του σημείου
     * @param lon η παράμετρος y του σημείου
     */
    public Location(long locationid, double lat, double lon) {
        this.locationid = locationid;
        this.lat = lat;
        this.lon = lon;
        //αρχικοποίηση του distance
        distance = Double.MAX_VALUE;
    }

    /**
     * Getter για το lan
     */
    public double getLat() {
        return lat;
    }

    /**
     * Getter για το lon
     */
    public double getLon() {
        return lon;
    }

    /**
     * Getter για το locationid
     */
    public long getLocationid() {
        return locationid;
    }

    /**
     * Συνάρτηση που υπολογίζει την απόσταση Manhattan μεταξύ 2 τοποθεσιών
     * @param lat η παράμετρος x του άλλου σημείου
     * @param lon η παράμετρος y του άλλου σημείου
     */
    public double find_manhattan_distance_between_two_points(double lat, double lon) {
        distance = abs((lat - this.lat)) + abs(lon - this.lon);
        return distance;
    }

    /**
     * Setter για το distance
     */
    public void setDistance(double new_distance){
        distance=new_distance;
    }

    /**
     * Getter για το distance
     */
    public double getDistance() {
        //εάν η τιμή του distance είναι η μεγαλύτερη δυνατή (έτσι όπως την αρχικοποιήσαμε)
        //εμφάνισε μήνυμα λάθους
        if (distance == Double.MAX_VALUE) {
            System.out.println("Error with distance value");
            System.exit(-1);
        }
        return distance;
    }

    /**
     * Κάνουμε override τη συνάρτηση toString
     * Συνάρτηση που μετατρέπει τα στοιχεία της τοποθεσίας σε συμβολοσειρά έτσι ώστε να μπορεί να εκτυπωθεί στην οθόνη
     */
    @Override
    public String toString() {
        return "id:" + locationid + "   latitude= " + lat + "   longitude= " + lon+ "  distance= "+distance;
    }

    /**
     * Κάνουμε override τη συνάρτηση compareTo
     * Συνάρτηση που συγκρίνει 2 τοποθεσίες μεταξύ τους σύμφωνα με τις τιμές των distances
     * Custom comparator to compare 2 locations according to their distances values
     * @param second_location η δεύτερη τοποθεσία
     * @return :
     * 1 αν το distance της πρώτης τοποθεσίας είναι μεγαλύτερη από αυτή της δεύτερης
     *-1 αν ισχύει το αντίθετο,
     * 0 αν και οι δυο τοποθεσίες έχουν ίδια distances
     */
    @Override
    public int compareTo(Location second_location) {
        if (distance > second_location.getDistance()) {
            return 1;
        } else if (distance < second_location.getDistance()) {
            return -1;
        }
        return 0;
    }
}
