import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Αυτή η κλάση απεικονίζει έναν κόμβο στο R*
 */
public class TreeNode implements Serializable {
    //παιδιά του κόμβου όταν αυτός δεν είναι φύλλο (αποθήκευση κόμβων)
    private ArrayList<TreeNode> children;
    //παιδιά του κόμβου όταν αυτός είναι φύλλο (αποθήκευση σημείων)
    private ArrayList<Point> points;
    //ορθογώνιο που σχηματίζεται από τον κόμβο
    private MBR mbr;
    //μέγιστος αριθμός θέσεων σε κάθε κόμβο
    private int upper_limit =5;
    //απόσταση κόμβου από ένα σημείο
    private double distance_from_point = Double.MAX_VALUE;


    /**
     * Κατασκευαστής με 4 ορίσματα
     * @param x1 : η μικρότερη τιμή στον άξονα x
     * @param x2 : η μεγαλύτερη τιμή στον άξονα x
     * @param y1 : η μικρότερη τιμή στον άξονα y
     * @param y2 : η μεγαλύτερη τιμή στον άξονα y
     */
    public TreeNode(double x1, double x2, double y1, double y2){
        mbr = new MBR(x1,x2,y1,y2);
        children = new ArrayList<>(upper_limit);
        points = new ArrayList<>(upper_limit);
    }

    /**
     * Getter για την απόσταση μεταξύ ενός κόμβου και ένός σημείου
     */
    public double getDistance_from_point(){
        return distance_from_point;
    }

    /**
     * Setter για την απόσταση μεταξύ ενός κόμβου και ένός σημείου
     */
    public void setDistance_from_point(double other_distance){
        distance_from_point =other_distance;
    }

    /**
     * Getter για τα σημεία του κόμβου αν αυτός είναι φύλλο
     */
    public ArrayList<Point> getPoints(){
        return points;
    }

    /**
     * Setter για τα σημεία του κόμβου
     */
    public void setPoints(ArrayList<Point> new_points){
        points.clear();
        points=new_points;
    }

    /**
     * Getter για τα παιδιά του κόμβου
     */
    public ArrayList<TreeNode> getChildren(){
        return children;
    }

    /**
     * Setter για τα παιδιά του κόμβου
     */
    public void setChildren(ArrayList<TreeNode> new_children){
        children.clear();
        children =new_children;
    }

    /**
     * Getter για το mbr
     */
    public MBR getMbr(){
        return mbr;
    }

    /**
     * Μέθοδος που επιστρέφει αν ένας κόμβος είναι φύλλο ή όχι
     */
    public boolean isLeaf(){
        return children.isEmpty() ;
    }

    /**
     * Μέθοδος που προσθέτει έναν καινούργιο κόμβο ως παιδί αν ο κόμβος-γονιός δεν είναι φύλλο
     * @param new_node ο κόμβος που θέλουμε να προσθέσουμε ως παιδί
     */
    public void add_new_child_node(TreeNode new_node){
        //προσθήκη νέου κόμβου στη λίστα με τα παιδιά
        children.add(new_node);
        //αλλαγή στις τιμές των x1, x2, y1, y2 του MBR
        mbr.setNewDimensions(new_node.getMbr().getAllValues().get(0),new_node.getMbr().getAllValues().get(1),new_node.getMbr().getAllValues().get(2),new_node.getMbr().getAllValues().get(3));
    }

    /**
     * Μέθοδος που προσθέτει ένα σημείο ως παιδί αν ο κόμβος-γονιός είναι φύλλο
     * @param new_point το σημείο που θέλουμε να προσθέσουμε
     */
    public void add_new_point(Point new_point){
        //
        points.add(new_point);
        mbr.setNewDimensions(new_point.getLat(),new_point.getLon());
    }

    /**
     * Συνάρτηση που προσθέτει καινούργια παιδιά σε έναν κόμβο
     * @param points λίστα από σημεία που θέλουμε να προσθέσουμε
     */
    public void add_children_nodes(ArrayList<Point> points){
        this.points = points;
    }

    /**
     * Συνάρτηση που συγκρίνει δύο κόμβους σύμφωνα με τις τιμές των ορθογωνίων τους
     * πρώτα γίνεται ταξινόμηση ως προς τη μικρότερη τιμή του x και μετά ως προς τη μεγαλύτερη τιμή του x
     */
    static class RectangleComparatorX implements Comparator<TreeNode> {
        @Override
        public int compare(TreeNode o1, TreeNode o2) {
            if(o1.getMbr().getAllValues().get(0) > o2.getMbr().getAllValues().get(0)){
                return -1;
            }
            else if(o1.getMbr().getAllValues().get(0) < o2.getMbr().getAllValues().get(0)){
                return 1;
            }
            else if(o1.getMbr().getAllValues().get(0).equals(o2.getMbr().getAllValues().get(0))){
                if(o1.getMbr().getAllValues().get(1) > o2.getMbr().getAllValues().get(1)){
                    return -1;
                }
                else if(o1.getMbr().getAllValues().get(1) < o2.getMbr().getAllValues().get(1)){
                    return 1;
                }
                else {
                    return 0;
                }
            }
            else{
                return 0;
            }
        }
    }


    /**
     * Συνάρτηση που συγκρίνει δύο κόμβους σύμφωνα με τις τιμές των ορθογωνίων τους
     * πρώτα γίνεται ταξινόμηση ως προς τη μικρότερη τιμή του y και μετά ως προς τη μεγαλύτερη τιμή του y
     */
    static class RectangleComparatorY implements Comparator<TreeNode>{
        @Override
        public int compare(TreeNode o1, TreeNode o2) {
            if(o1.getMbr().getAllValues().get(2) > o2.getMbr().getAllValues().get(2)){
                return -1;
            }
            else if(o1.getMbr().getAllValues().get(2) < o2.getMbr().getAllValues().get(2)){
                return 1;
            }
            else if(o1.getMbr().getAllValues().get(2).equals(o2.getMbr().getAllValues().get(2))){
                if(o1.getMbr().getAllValues().get(3) > o2.getMbr().getAllValues().get(3)){
                    return -1;
                }
                else if(o1.getMbr().getAllValues().get(3) < o2.getMbr().getAllValues().get(3)){
                    return 1;
                }
                else {
                    return 0;
                }
            }
            else{
                return 0;
            }
        }
    }
}

