
/**
 * Η κλάση αυτή υλοποιεί το MinHeap
 */
public class MinHeap {
    //πίνακας από κόμβους που περιέχει όλα τα στοιχεία που ανήκουν στη σωρό
    private TreeNode[] Heap;
    //πλήθος σημείων που είναι μέσα στη σωρό
    private int size;
    //μέγιστη χωρητικότητα της σωρού
    private int maximum_size;

    /**
     * Κατασκευαστής με 1 όρισμα
     * @param maximum η μέγιστη χωρητικότητα
     */
    public MinHeap(int maximum)
    {
        this.maximum_size = maximum;
        this.size = 0;
        Heap = new TreeNode[this.maximum_size + 1];
        TreeNode aNode = new TreeNode(Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE);
        aNode.setDistance_from_point(Double.MIN_VALUE);
        Heap[0] = aNode;
    }

    /**
     * Μέθοδος που ελέγχει αν το μέγεθος της σωρός είναι μηδενικό
     * Δηλαδή ελέγχει αν η σωρός είναι άδεια
     */
    public boolean isEmpty(){
        if(size==0)
            return true;
        else
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
     * Μέθοδος που επιστρέφει true αν ο κόμβος είναι φύλλο ή false αν δεν είναι
     * @param node κόμβος για το οποίο θέλω να μάθω αν είναι φύλλο στο δέντρο μου
     */
    private boolean isLeaf(int node) {
        if(find_left_child(node)>=size)
            return true;
        return false;
    }

    /**
     * Μέθοδος που αντιστρέφει δύο κόμβους node1 και node2 στη σωρό
     */
    private void swap(int node1, int node2)
    {
        TreeNode temp_node;
        temp_node = Heap[node1];
        Heap[node1] = Heap[node2];
        Heap[node2] = temp_node;
    }

    /**
     * Μέθοδος που αφαιρεί και επιστρέφει τον κόμβο που βρίσκεται πιο ψηλά στη σωρό
     */
    public TreeNode remove()
    {
        TreeNode exit = Heap[1];
        Heap[1] = Heap[size--];
        apply_minHeap(1);
        return exit;
    }

    /**
     * Μέθοδος που εφαρμόζει τον αλγόριθμο MinHeap σε ένα υποδέντρο
     * Με τη μέθοδο αυτή έχουμε ως αποτέλεσμα το δεξί και αριστερό υποδέντρο να είναι heaped και το μόνο που μένει είναι να φτιάξουμε τη ρίζα
     */
    private void apply_minHeap(int node) {
        //αν ο κόμβος δεν είναι φύλλο και είναι μεγαλύτερο από οποιοδήποτε από τα παιδιά του
        if (!isLeaf(node)) {
            if (Heap[node].getDistance_from_point() > Heap[find_left_child(node)].getDistance_from_point()
                    || Heap[node].getDistance_from_point() > Heap[find_right_child(node)].getDistance_from_point()) {
                //αντάλλαξε το με το αριστερό παιδί και προσάρμοσε στη σωρό το αριστερό παιδί
                if (Heap[find_left_child(node)].getDistance_from_point() < Heap[find_right_child(node)].getDistance_from_point()) {
                    swap(node, find_left_child(node));
                    apply_minHeap(find_left_child(node));
                }
                //αντάλλαξε το με το δεξί παιδί και προσάρμοσε στη σωρό το δεξί παιδί
                else {
                    swap(node, find_right_child(node));
                    apply_minHeap(find_right_child(node));
                }
            }
        }
    }

    /**
     * Μέθοδος που προσθέτει έναν καινούργιο κόμβο στη σωρό
     * @param new_node κόμβος που θα προστεθεί στη σωρό
     */
    public void insert_to_minHeap(TreeNode new_node) {
        //αν το πλήθος των στοιχείων που βρίσκονται στη σωρό είναι μεγαλύτερο ή ίσο από τη μέγιστη χωρητικότητα της σωρού τότε
        // δε μπορούμε να βάλουμε άλλο στοιχείο
        if (size >= maximum_size) {
            return;
        }
        //αν δεν ισχύει αυτό τότε αύξηση το πλήθος των στοιχείων της σωρού κατά 1
        //και αναπροσάρμοσε τη σειρά των στοιχείων μέσα σε αυτή
        Heap[++size] = new_node;
        int temp = size;
        while (Heap[temp].getDistance_from_point() < Heap[find_parent(temp)].getDistance_from_point()) {
            swap(temp, find_parent(temp));
            temp = find_parent(temp);
        }
    }

}
