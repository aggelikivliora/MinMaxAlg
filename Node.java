package ce326.hw2;

public class Node {
    private double value;
    InternalNode parent;
    
    public Node()
    {
        value = 0;
        parent= null;
    }
    public Node(double value)
    {
        this.value = value;
        parent=null;
    }
    
    public double getValue(){
        return value;
    }
    public void setValue(double value){
        this.value = value;
    }
    public void setParent(InternalNode nd){
        parent=nd;
    }
    public InternalNode getParent(){
        return parent;
    }
}
