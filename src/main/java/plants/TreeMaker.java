package plants;

public class TreeMaker
{
    public static TreeNode makeTree(double maxHeight, double minHeight, double branchyness, double avgBranchLength, double minimumFirstSplit)
    {
        // tree will be within 20% of min height or higher,
        // or 20% of max height or lower
        // branchyness of 0% would be a palm tree, 100% would be very brancy
        // avgBranchLength +- 20% how long is a branch
        // minimumFirstSplit - min height for first split to occur (default 0)
        
        // eg: tree between 40 and 100 feet tall, with 30% branchiness and 7 avg branch length
        // expectation: initial trunk roughly 7 feet, 30% chance to split into 2 smaller trunks
        // if not, trunk 14 feet, second 30% chance to split
        // if not, trunk 21 feet, third 30% chance to split
        // by 40 feet, likely would have split once.  maybe not
        // from here, there are roughly 5 more iterations possible
        // ensuring not crossing max height
        
        //returns the root, which has branches, which have branches
        TreeNode root = new TreeNode(0, 0, 0);
        startBranching(root, maxHeight, minHeight, branchyness, avgBranchLength, minimumFirstSplit);
        return root;
    }
    
    private static void startBranching(TreeNode current, double max, double min, double chance, double len, double minSplit)
    {
        double currentRight = current.points[0];
        double currentForward = current.points[1];
        double currentUp = current.points[2];
        
        do {
            double nextUp = currentUp + len/2 + Math.random()*(len/2);
            double nextForward = currentForward - len/8 + Math.random()*(len/4);
            double nextRight = currentRight - len/8 + Math.random()*(len/4);
            if(nextUp <= max) {
                TreeNode nextNode = new TreeNode(nextRight, nextForward, nextUp);
                current.branches.add(nextNode);
                //depth first recursion
                startBranching(nextNode, max, min, chance, len, minSplit);
            }
            //chance to repeat another branch IF we're above min split height
        }while(currentUp > minSplit && Math.random() < chance);
    }
}
