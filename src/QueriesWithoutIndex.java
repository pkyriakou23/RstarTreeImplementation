import java.util.ArrayList;
import java.util.Collections;

/**
 * Η κλάση αυτή υλοποιεί ερωτήματα περιοχής και κοντινότερων γειτόνων χωρίς index
 */
public class QueriesWithoutIndex {

    /**
     * Συνάρτηση που υλοποιεί σειριακά το ερώτημα κοντινότερων γειτόνων
     * Επιστρέφει τους k κοντινότερους γείτονες από το στοιχείο middle
     * @param locations όλοι οι πιθανοί γείτονες
     * @param middle τοποθεσία για την οποία ψάχνουμε τους κοντινότερους γείτονες
     * @param k αριθμός επιθυμητών γειτόνων
     */
    public ArrayList<Location> knn_without_index(ArrayList<Location> locations, Location middle, int k) {
        //ArrayList για τις τοποθεσίες που βρίσκονται οι k κοντινότεροι γείτονες
        ArrayList<Location> k_neighbors = new ArrayList<>();
        //υπολογισμός απόστασης μεταξύ της middle τοποθεσίας και όλων των υπόλοιπων τοποθεσιών (brute force)
        for (Location location : locations)
            location.find_manhattan_distance_between_two_points(middle.getLat(), middle.getLon());
        //ταξινόμηση των locations σύμφωνα με την απόσταση που απέχουν από τη middle
        Collections.sort(locations);
        //αποθήκευση εμφάνιση των k κοντινότερων γειτόνων στο k_neighbors
        for (int i = 0; i < k; i++) {
            k_neighbors.add(locations.get(i));
            Point k_distance = new Point(locations.get(i).getLat(), locations.get(i).getLon());
            k_distance.setDistance_point(k_distance.find_distance_from_point(new Point(middle.getLat(), middle.getLon())));
        }
        return k_neighbors;
    }

    /**
     * Συνάρτηση που υλοποιεί σειριακά το ερώτημα περιοχής
     * Επιστρέφει τους γείτονες που βρίσκονται μέσα στον "κύκλο" γύρω από το στοιχείο middle
     * @param middle η τοποθεσία από την οποία μετράμε την ακτίνα
     * @param radius η ακτίνα εντός της οποίας ψάχνουμε τις τοποθεσίες
     * @param locations όλες οι τοποθεσίες
     */
    public static ArrayList<Location> range_query_without_index(Location middle, double radius, ArrayList<Location> locations) {
        //ArrayList για τις τοποθεσίες που βρίσκονται οι γείτονες εντός της ακτίνας
        ArrayList<Location> locations_in_range = new ArrayList<>();
        //αναζήτηση τοποθεσιών που είναι εντός της ακτίνας
        for (int i = 0; i < locations.size(); i++) {
            //όσες αποστάσεις manhattan μεταξύ του middle και των υπόλοιπων τοποθεσιών βρίσκονται εντός της ακτίνας
            // τότε πρόσθεσε τις τοποθεσίες αυτές στο locations_in_range (brute force)
            if (locations.get(i).find_manhattan_distance_between_two_points(middle.getLat(), middle.getLon()) <= radius)
                locations_in_range.add(locations.get(i));
        }
        return locations_in_range;
    }
}
