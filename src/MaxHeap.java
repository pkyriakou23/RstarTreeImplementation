
/**
 * Η κλάση αυτή υλοποιεί το MaxHeap
 */
public class MaxHeap {
    //πίνακας από Points που περιέχει όλα τα στοιχεία που ανήκουν στη σωρό
    private Point[] Heap;
    //πλήθος σημείων που υπάρχουν στη σωρό
    private int size;
    //μέγιστη χωρητικότητα της σωρού
    private int maximum;

    /**
     * Κατασκευαστής με 1 όρισμα
     * @param maximum_size η μέγιστη χωρητικότητα
     */
    public MaxHeap(int maximum_size) {
        this.maximum = maximum_size;
        this.size = 0;
        Heap = new Point[this.maximum + 1];
        Point point =  new Point(Double.MIN_VALUE, Double.MIN_VALUE);
        point.setDistance_point(Double.MAX_VALUE);
        Heap[0] = point;
    }

    /**
     * Getter για το size
     */
    public int getSize(){
        return size;
    }

    /**
     * Μέθοδος που επιστρέφει true αν ο κόμβος είναι φύλλο ή false αν δεν είναι
     * @param node κόμβος για το οποίο θέλω να μάθω αν είναι φύλλο στο δέντρο μου
     */
    private boolean isLeaf(int node) {
        if(find_left_child(node)>=size) {
            return true;
        }
        return false;
    }

    /**
     * Μέθοδος που επιστρέφει το που βρίσκεται ο γονιός
     * @param node κόμβος του οποίου ψάχνω τον γονιό
     */
    private int find_parent(int node) { //parent
        return node / 2;
    }

    /**
     * Μέθοδος που επιστρέφει το που βρίσκεται το αριστερό παιδί
     * @param node κόμβος του οποίου ψάχνω το αριστερό παιδί
     */
    private int find_left_child(int node) { //leftChild
        return (2 * node);
    }

    /**
     * Μέθοδος που επιστρέφει το που βρίσκεται το δεξί παιδί
     * @param node κόμβος του οποίου ψάχνω το δεξί παιδί
     */
    private int find_right_child(int node) { //rightChild
        return (2 * node) + 1;
    }

    /**
     * Μέθοδος που αντιστρέφει δύο κόμβους node1 και node2 στη σωρό
     * node1, node2 είναι οι κόμβοι που θέλω να αλλάξω μεταξύ τους
     */
    private void swap(int node1, int node2) {
        Point temp;
        temp = Heap[node1];
        Heap[node1] = Heap[node2];
        Heap[node2] = temp;
    }

    /**
     * Μέθοδος που επιστρέφει το σημείο που υπάρχει στην κορυφή της σωρού
     */
    public Point getMax(){
        return Heap[1];
    }

    /**
     * Μέθοδος που αφαιρεί το max σημείο από τη σωρό
     */
    public Point extractMax() {
        Point popped = Heap[1];
        Heap[1] = Heap[size];
        apply_maxHeap(1);
        size--;
        return popped;
    }

    /**
     * Μέθοδος που εφαρμόζει τον αλγόριθμο MaxHeap σε ένα υποδέντρο
     * Με τη μέθοδο αυτή έχουμε ως αποτέλεσμα το δεξί και αριστερό  υποδέντρο του κόμβου node να είναι heaped και το μόνο που μένει είναι να φτιάξουμε τη ρίζα
     */
    private void apply_maxHeap(int node) {
        if (isLeaf(node))
            return;
        if (Heap[node].getDistance_point() < Heap[find_left_child(node)].getDistance_point() ||
                Heap[node].getDistance_point() < Heap[find_right_child(node)].getDistance_point()) {

            if (Heap[find_left_child(node)].getDistance_point() > Heap[find_right_child(node)].getDistance_point()) {
                swap(node, find_left_child(node));
                apply_maxHeap(find_left_child(node));
            } else {
                swap(node, find_right_child(node));
                apply_maxHeap(find_right_child(node));
            }
        }
    }

    /**
     * Μέθοδος που προσθέτει ένα καινούργιο στοιχείο στη σωρό
     */
    public void insert_to_maxHeap(Point new_point) { //insert
        //αν το καινούργιο σημείο είναι πιο κοντά από ότι είναι το πιο μακρινό σημείο
        //και η στοίβα είναι γεμάτη τότε πρέπει να βγάλουμε ένα σημείο να προσθέσουμε
        //το καινούργιο
        if (size== maximum){
            if(new_point.getDistance_point()<=getMax().getDistance_point())
                extractMax();
            else
                return ;
        }
        Heap[++size] = new_point;
        //αναπροσαρμογή και διόρθωση της σειράς των στοιχείων στη σωρό
        int current = size;
        while (Heap[current].getDistance_point() > Heap[find_parent(current)].getDistance_point() ) {
            swap(current, find_parent(current));
            current = find_parent(current);
        }
    }

}