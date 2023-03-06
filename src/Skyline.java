import java.util.ArrayList;

public class Skyline {

    /**
     * Συνάρτηση που υλοποιεί τα ερωτήματα κορυφογραμμής
     * Επιστρέφει μια λίστα με τα στοιχεία που ανήκουν στην κορυφογραμμή
     * Ουσιαστικά ελέγχει κάθε φορά τα σημεία στον χώρο για το αν κυριαρχούνται από κάποια άλλα ή οχι
     * αν δεν κυριαρχούνται μπαίνουν στη λίστα των αποτελεσμάτων αλλιώς αποκλείονται
     **/
    public ArrayList<SkylineNode> skyline(Tree tree)
    {
        //ArrayList για τα στοιχεία που ανήκουν στην κορυφογραμμή
        ArrayList<SkylineNode> skyline_results = new ArrayList<>();
        //δημιουργία minHeap για ταξινόμηση
        MinHeapSkyline heap = new MinHeapSkyline(99999);
        //προσθέτουμε στη σωρό τα στοιχεία της ρίζας,
        //βρίσκει την απόσταση manhattan και ταξινομεί τους κόμβους σύμφωνα με τη μικρότερη απόσταση
        heap.insert_to_minHeap_for_skyline(new SkylineNode(tree.getRoot()));
        //όσο η σωρός δεν είναι άδεια
        while (!heap.isEmpty()) {
            //κόμβος με μικρότερη απόσταση manhattan βγαίνει από σωρό
            SkylineNode popnode = heap.remove_for_skyline();
            boolean flag = false;
            for (SkylineNode n : skyline_results) {
                //ελέγχουμε αν κυριαρχούνται οι επόμενοι κόμβοι μετά από αυτόν που βγήκε από τη σωρό
                if (n.getX() >= n.getX() && n.getY() >= n.getY()) {
                    flag = true;
                    break;
                }
            }
            //αν κυριαρχούνται σταματάς αν όχι συνεχίζεις
            if (flag) continue;
            //αν δεν είναι φύλλο σημαίνει ότι θα πάω να ελέγξω αν το MBR του κυριαρχείται
            if(! popnode.node.isLeaf()) {
                for (TreeNode n : popnode.node.getChildren()) {
                    SkylineNode n1 = new SkylineNode(n);
                    flag = false;
                    for (SkylineNode ans : skyline_results) {
                        if (n1.getX() >= ans.getX() && n1.getY() >= ans.getY()) {
                            flag = true;
                            break;
                        }
                    }
                    //αν το MBR δεν κυριαρχείται τότε το βάζουμε στη σωρό
                    if (!flag)
                        heap.insert_to_minHeap_for_skyline(n1);
                }
            }
            //αν είναι φύλλο
            else
            {
                //παίρνεις τα σημεία του κόμβου και ελέγχεις αν τα στοιχεία του κυριαρχούνται
                for (Point n : popnode.node.getPoints()) {
                    SkylineNode n1 = new SkylineNode(n);
                    flag = false;
                    for (SkylineNode ans : skyline_results) {
                        if (n1.getX() >= ans.getX() && n1.getY() >= ans.getY()) {
                            flag = true;
                            break;
                        }
                    }
                    //αν δεν κυριαρχούνται τότε μπαίνουν στη λίστα των απαντήσεων
                    if (!flag) skyline_results.add(n1);
                }
            }
        }
        return skyline_results;

    }
}
