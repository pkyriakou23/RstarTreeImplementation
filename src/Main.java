import java.util.ArrayList;

public class Main {

    private static Tree tree = new Tree(new TreeNode(Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE));

    public static void main(String[] args) throws Exception {
        ArrayList<Location> locations = null;
        QueriesWithoutIndex sKNN = new QueriesWithoutIndex();

        //διάβασμα του αρχείου osm
        LoadOSM osm = new LoadOSM();
        osm.load_osmlife();
        //διάβασμα του αρχείου datafile
        LoadData data = new LoadData();
        locations = data.read_data();
        //δημιουργία καταλόγου
        System.out.println("\nMaking index");
        long beginning= System.currentTimeMillis();
        tree.read_from_file();
        tree.setTotal_records(data.getTotal_entries());
        tree.read_from_file();
        long ending= System.currentTimeMillis() - beginning ;
        System.out.println("Time needed: " + ending + "ms");

        //αρχικοποίηση τιμών για να τρέξουν τα ερωτήματα
        int k = 5; //κοντινότεροι γείτονες
        double lat = 38.444235; //x
        double lon = 23.853263; //y
        double range = 10; //ακτίνα
        long id =35836525; //id τοποθεσίας

        //προσθήκη καινούργιου στοιχείου
        Location newLocation = new Location(id, lat, lon);
        System.out.println("\nInsert new element " + newLocation.toString());
        long startTime = System.currentTimeMillis();
        int size = locations.size();
        LoadOSM osm2 = new LoadOSM();
        osm2.add_location(newLocation, locations);
        LoadData data2 = new LoadData();
        locations = data2.read_data();
        if(size !=locations.size())
            System.out.println("Location added.");
        else
            System.out.println("Location not added.");
        long endTime = System.currentTimeMillis() - startTime;
        System.out.println("Time needed: " + endTime + "ms");

        //διαγραφή στοιχείου
        System.out.println("\nDelete element " + newLocation.toString());
        startTime = System.currentTimeMillis();
        size = locations.size();
        tree.delete_from_tree(id,locations);
        if(size !=locations.size())
            System.out.println("Location deleted.");
        else
            System.out.println("Location not deleted.");
        endTime = System.currentTimeMillis() - startTime;
        System.out.println("Time needed: " + endTime + "ms");

        //ερώτημα περιοχής χωρίς τη βοήθεια του καταλόγου
        System.out.println("\nRange query without index for range=" + range + " lat: " + lat + " lon: " + lon );
        startTime = System.currentTimeMillis();
        sKNN = new QueriesWithoutIndex();
        ArrayList<Location> locations_in_range = sKNN.range_query_without_index(new Location(-1, lat, lon), range, locations);
        for (Location neighbor : locations_in_range)
            System.out.println(neighbor.toString());
        endTime = System.currentTimeMillis() - startTime;
        System.out.println("Time needed: " + endTime + "ms");

        //ερώτημα περιοχής με τη βοήθεια του καταλόγου
        System.out.println();
        System.out.println("\nRange query with index for range=" + range + " lat: " + lat + " lon: " + lon );
        startTime = System.currentTimeMillis();
        ArrayList<Location> CloseAreasA = tree.range_query_with_index(new Point(lat, lon), range);
        for (Location neighbor : CloseAreasA)
            System.out.println(neighbor.toString());
        endTime = System.currentTimeMillis() - startTime;
        System.out.println("Time needed: " + endTime + "ms");

        //ερώτημα k κοντινότερων γειτόνων χωρίς τη βοήθεια καταλόγου
        System.out.println("\nKNN without index for k=" + k + " lat: " + lat + " lon: " + lon );
        Location middle = new Location(-1, lat, lon);
        startTime = System.currentTimeMillis();
        ArrayList<Location> distances = sKNN.knn_without_index(locations, middle, k);
        for (Location neighbor : distances)
            System.out.println(neighbor.toString());
        endTime = System.currentTimeMillis() - startTime;
        System.out.println("Time needed: " + endTime + "ms");

        //ερώτημα k κοντινότερων γειτόνων με τη βοήθεια καταλόγου
        System.out.println("\nKNN with index for k=" + k + " lat: " + lat + " lon: " + lon );
        Point point = new Point(lat, lon);
        startTime = System.currentTimeMillis();
        ArrayList<Location> Neighbors = tree.knn_with_index(point, k);
        for (Location neighbor : Neighbors)
            System.out.println(neighbor.toString());
        endTime = System.currentTimeMillis() - startTime;
        System.out.println("Time needed: " + endTime + "ms");

        //ερώτημα κορυφογραμμής
        System.out.println("\nSkyline");
        startTime = System.currentTimeMillis();
        Skyline skl = new Skyline();
        ArrayList<SkylineNode> results = skl.skyline(tree);
        System.out.println("Skyline result: \n" + results);
        endTime = System.currentTimeMillis() - startTime;
        System.out.println("Time needed: " + endTime + "ms");

        //μαζική κατασκευή του δέντρου
        System.out.println("\nMaking bottom up");
        beginning= System.currentTimeMillis();
        BottomUp b = new BottomUp();
        b.build();
        ending= System.currentTimeMillis() - beginning ;
        System.out.println("Time needed: " + ending + "ms");
    }



}





