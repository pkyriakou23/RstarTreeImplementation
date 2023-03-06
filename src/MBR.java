import java.util.ArrayList;
import java.util.Comparator;
import java.io.Serializable;

/** Κλάση που αναπαριστά ένα ορθογώνιο
 */
public class MBR implements Serializable {
    //η μικρότερη τιμή στον άξονα x
    private double x1;
    //η μεγαλύτερη τιμή στον άξονα x
    private double x2=0;
    //η μικρότερη τιμή στον άξονα y
    private double y1;
    //η μεγαλύτερη τιμή στον άξονα y
    private double y2=0;

    /**
     * Κατασκευαστής της κλάσης με 4 ορίσματα
     */
    public MBR(double x1, double x2, double y1, double y2){
        this.x1=x1;
        this.x2=x2;
        this.y1=y1;
        this.y2=y2;
    }

    /**
     * Μέθοδος που επιστρέφει και τις 4 τιμές του ορθογωνίου
     */
    public ArrayList<Double> getAllValues(){
        ArrayList<Double> temp = new ArrayList<>(4);
        temp.add(x1);
        temp.add(x2);
        temp.add(y1);
        temp.add(y2);
        return temp;
    }

    /**
     * Getter για το x1
     */
    public double getX1(){
        return x1;
    }

    /**
     * Getter για το x2
     */
    public double getX2(){
        return x2;
    }

    /**
     * Getter για το y1
     */
    public double getY1(){
        return y1;
    }

    /**
     * Getter για το y2
     */
    public double getY2(){
        return y2;
    }

    /**
     * Μέθοδος για το εμβαδόν του ορθογωνίου
     */
    public double getArea(){
        return (x2-x1)*(y2-y1);
    }

    /**
     * μέθοδος που υπολογίζει την περίμετρο ενός ορθογωνίου
     */
    public double getPerimeter(){
        return 2*(x2-x1)+2*(y2-y1);
    }
    /**
     * Μέθοδος που υπολογίζει το πόσο θα αλλάξει το εμβαδόν εάν προσθέσουμε ένα ακόμα σημείο στο ορθογώνιο
     * @param x η τιμή του άξονα x του σημείου που θέλουμε να προσθέσουμε
     * @param y η τιμή του άξονα y του σημείου που θέλουμε να προσθέσουμε
     */
    public double calculate_new_area(double x, double y){
        if(x1<=x && x<=x2 && y>y2){
            return (x2-x1)*(y-y1);
        }
        else if(y1<=y && y<=y2 && x>x2){
            return (x-x1)*(y2-y1);
        }
        else if(x1<=x && x<=x2 && y<y1){
            return (x2-x1)*(y2-y);

        }
        else if(y1<=y && y<=y2 && x<x1){
            return (x2-x)*(y2-y1);
        }
        else if(x<x1 && y>y2){
            return (x2-x)*(y-y1);
        }
        else if(x>x2 && y>y2){
            return (x-x1)*(y-y1);
        }
        else if(x>x2 && y<y1){
            return (x-x1)*(y2-y);
        }
        else if(x<x1 && y<y1){
            return (x2-x)*(y2-y);
        }
        //το σημείο βρίσκεται ήδη μέσα στο MBR
        else if(x>=x1 && x<=x2 && y>=y1 && y<=y2){
            return (x2-x1)*(y2-y1);
        }
        else {
            System.out.println("Error in calculating area");
            return -1;
        }
    }

    /**
     * Μέθοδος που προσθέτει ένα καινούργιο σημείο σε ένα ορθογώνιο και αλλάζει τις τιμές x1, x2, y1, y2
     * @param x η τιμή του άξονα x του σημείου που θέλουμε να προσθέσουμε
     * @param y η τιμή του άξονα y του σημείου που θέλουμε να προσθέσουμε
     */
    public void setNewDimensions(double x, double y){
        if(x1==Double.MAX_VALUE && x2==Double.MIN_VALUE && y1==Double.MAX_VALUE && y2==Double.MIN_VALUE){
            x1=x; y1=y; x2=x; y2=y;
            return;

        }
        if(x1<=x && x<=x2 && y>y2)
            y2=y;
        else if(y1<=y && y<=y2 && x<x1)
            x1=x;
        else if(x1<=x && x<=x2 && y<y1)
            y1=y;
        else if(y1<=y && y<=y2 && x>x2)
            x2=x;
        else if(x<x1 && y>y2){
            x1=x;
            y2=y;
        }else if(x>x2 && y<y1){
            x2=x;
            y1=y;
        }else if(x>x2 && y>y2){
            x2=x;
            y2=y;
        } else if(x<x1 && y<y1){
            x1=x;
            y1=y;
        }
        else if(x<=x2 && x>=x1 && y>=y1 && y<=y2){
        }
        else System.out.println("Error in setting dimensions");
    }

    /**
     * Μέθοδος που αναπροσαρμόζει τις τιμές ενός ορθογωνίου όταν προστεθεί σε αυτό ένας επιπλέον κόμβος
     * @param x_lower η μικρότερη τιμή στον άξονα των x
     * @param x_upper η μεγαλύτερη τιμή στον άξονα των x
     * @param y_lower η μικρότερη τιμή στον άξονα των y
     * @param y_upper η μεγαλύτερη τιμή στον άξονα των y
     */
    public void setNewDimensions(double x_lower, double x_upper, double y_lower, double y_upper){
        if(x1==Double.MAX_VALUE && x2==Double.MIN_VALUE && y1==Double.MAX_VALUE && y2==Double.MIN_VALUE){
            x1=x_lower;
            x2=x_upper;
            y1=y_lower;
            y2=y_upper;
            return;
        }
        if(x_lower<x1){
            x1=x_lower;
        }
        if(x_upper>x2){
            x2=x_upper;
        }
        if(y_lower<y1){
            y1=y_lower;
        }
        if(y_upper>y2){
            y2=y_upper;
        }
    }

    /**
     * Μέθοδος που υπολογίζει την απόσταση μεταξύ ενός σημείου και ενός ορθογωνίου
     * @param point σημείο που θέλουμε να μετρήσουμε πόσο μακριά είναι από ένα ορθογώνιο
     */
    public double find_distance_between_point_and_MBR(Point point){
        double x=point.getLat();
        double y=point.getLon();
        if(x1<=x && x<=x2 && y>y2){
            return y-y2;
        }
        else if(y1<=y && y<=y2 && x>x2){
            return x-x2;
        }
        else if(x1<=x && x<=x2 && y<y1){
            return y1-y;
        }
        else if(y1<=y && y<=y2 && x<x1){
            return x1-x;
        }
        else if(x<x1 && y>y2){
            return Math.abs(x1-x)+Math.abs(y2-y);
        }
        else if(x>x2 && y>y2){
            return Math.abs(x2-x)+Math.abs(y2-y);
        }
        else if(x>x2 && y<y1){
            return Math.abs(x2-x)+Math.abs(y1-y);
        }
        else if(x<x1 && y<y1){
            return Math.abs(x1-x)+Math.abs(y1-y);
        }
        //σε περίπτωση που το σημείο βρίσκεται ήδη μέσα στο ορθογώνιο τότε η απόσταση είναι μηδενική
        return 0;
    }

    /**
     * Μέθοδος που υπολογίζει την επικάλυψη που υπάρχει μεταξύ δύο ορθογωνίων
     */
    public double calculate_overlap(MBR second_rectangle){
        double xx1=second_rectangle.getX1();
        double xx2=second_rectangle.getX2();
        double yy1=second_rectangle.getY1();
        double yy2=second_rectangle.getY2();
        double overlap_x=Math.max(0,Math.min(x2,xx2)-Math.max(x1,xx1));
        double overlap_y=Math.max(0,Math.min(y2,yy2)-Math.max(y1,yy1));
        if(overlap_x==0){
            return overlap_y;
        }
        if(overlap_y==0){
            return overlap_x;
        }
        return overlap_x*overlap_y;
    }

}
