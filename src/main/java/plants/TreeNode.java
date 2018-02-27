package plants;

import java.util.ArrayList;

public class TreeNode
{
    //points are relative to base
    //using public to facilitate drawing
    public double [] points; // relative distance from last point
    public ArrayList<TreeNode> branches; //nodes that this one connects to
    
    public TreeNode(double right, double forward, double height)
    {
        //as you're facing the tree, how far "right" is this from the base
        // how far "forward" is it closer to you (relative to the base)
        // and how high is it above the base
        points = new double[3];
        points[0] = right;
        points[1] = forward;
        points[2] = height;
        branches = new ArrayList<TreeNode>();
    }
    
}
