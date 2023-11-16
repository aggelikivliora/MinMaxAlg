package ce326.hw2;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
public class InternalNode extends Node{
    InternalNode []array; /// POSES 8ESEIS???
    double alpha;
    double beta;
    //String type;
    
    public InternalNode() // DEFAULT
    {
        super(0);
        array = null;
        alpha = MIN_VALUE;
        beta = MAX_VALUE;
      //  type=null;
    }
    
    public InternalNode(InternalNode []array)
    {
        super(0);
        this.array = array;
        alpha = Double.MIN_VALUE;
        beta = Double.MAX_VALUE;
        //type=null;
    }
    
    public void setChildrenSize(int size)
    {
        this.array = new InternalNode[size];
    }
    
    public int getChildrenSize()
    {
        return array.length;
    }
    
    public void insertChild(int pos, InternalNode X)
    {
        array[pos] = X;
    }
    
    public InternalNode getChild(int pos)
    {
        return array[pos];
    }
}


class maximizer extends InternalNode {
    public double maxNode(){
        double num = array[0].getValue();
        if(array.length>1){
            for(int i=1; i<array.length; i++){
                if(array[i].getValue()>num){    /// PRIVATE???
                    num = array[i].getValue();
                }
            }
        }
        else{
            return num;
        }
        return num;
    }
}

class minimizer extends InternalNode{
    public double minNode(){
        double num = array[0].getValue();
        if(array.length>1){
            for(int i=1; i<array.length; i++){
                if(array[i].getValue()<num){    /// PRIVATE???
                    num = array[i].getValue();
                }
            }
        }
        else{
            return num;
        }
        return num;
    }
}