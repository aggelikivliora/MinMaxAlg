package ce326.hw2;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import org.json.JSONException;
public class minMaxOptimalTree extends minMaxTree{
    public minMaxOptimalTree(String jstr) throws JSONException,IllegalArgumentException{
        super(jstr);
    }
    
    @Override
    public double minMax(){
        minMaxOptimalRecursive(getRoot(), 0);
        return getRoot().getValue();
    }
    public double simpleMinMax(){
        return super.minMax();
    }
    public void minMaxOptimalRecursive(Object nd, int v){
        if(nd instanceof maximizer){
            maximizer curr ;
            curr = (maximizer)nd;
            for(int i=0; i<curr.getChildrenSize(); i++){
                curr.getChild(i).alpha=curr.alpha;
                curr.getChild(i).beta=curr.beta;
                minMaxOptimalRecursive(curr.getChild(i), v+1);
                if(curr.getChild(i).getValue()>curr.alpha)
                {
                    curr.alpha=curr.getChild(i).getValue();
                    if(curr.beta<=curr.alpha){
                        curr.setValue(curr.alpha);
                        return;
                    }
                }
            }
            double value=curr.maxNode();
            curr.setValue(value);
            return;
        }
        if(nd instanceof minimizer){
            minimizer curr ;
            curr = (minimizer)nd;
            for(int i=0; i<curr.getChildrenSize(); i++){
                curr.getChild(i).beta=curr.beta;
                curr.getChild(i).alpha=curr.alpha;
                minMaxOptimalRecursive(curr.getChild(i), v+1);
                if(curr.getChild(i).getValue()<curr.beta)
                {
                    curr.beta=curr.getChild(i).getValue();
                    if(curr.beta<=curr.alpha){
                        curr.setValue(curr.beta);
                        return;
                    }
                }
            }
            double value=curr.minNode();
            curr.setValue(value);
            //System.out.println("["+value+","+curr.alpha+","+curr.beta+"]\n"); 
            
            return;
        }
        if(nd instanceof InternalNode){
            InternalNode curr; 
            curr = (InternalNode)nd;
            //System.out.println(curr.getValue()); 
            curr.alpha = MAX_VALUE;
            curr.beta = MIN_VALUE;
        }
    }
    
    // epistrefetai to optimal path
    @Override
    public ArrayList<Integer> optimalPath(){
        ArrayList<Integer> path = new ArrayList<>();
        path = findPath(getRoot(), path);
        return path;
    }
    public ArrayList<Integer> findPath(Object nd, ArrayList<Integer> path){
        ArrayList<Integer> temp = new ArrayList<>();
        if(nd instanceof maximizer){
            maximizer curr;
            curr = (maximizer)nd;
            for(int i=0; i<curr.getChildrenSize(); i++){
                if(curr.getChild(i).getValue()==getRoot().getValue()){
                    path.add(i);
                    path=findPath(curr.getChild(i), path);
                }
                if(path.size()>temp.size()){
                    temp = path;
                }
            }
            return temp;
        }
        if(nd instanceof minimizer){
            minimizer curr;
            curr = (minimizer)nd;
            for(int i=0; i<curr.getChildrenSize(); i++){
                if(curr.getChild(i).getValue()==getRoot().getValue()){
                    path.add(i);
                    path=findPath(curr.getChild(i), path);
                }
                if(path.size()>temp.size()){
                    temp = path;
                }
            }
            return path;
        }
        return path;
    }
    public double prunedNodes(){
        double pnodes=0;
        pnodes=countPrunedNodes(getRoot(),pnodes);
        return pnodes;
    } 
    public double countPrunedNodes(Object nd,double pnodes){
        if(nd instanceof maximizer){
            maximizer curr;
            curr =(maximizer)nd;
            for(int i=0; i<curr.getChildrenSize(); i++){
                pnodes=countPrunedNodes(curr.getChild(i),pnodes);
            }
            if((curr.beta==MAX_VALUE)&&(curr.alpha==MIN_VALUE)){
                pnodes++;
            }
            return pnodes;
        }
        else if(nd instanceof minimizer){
            minimizer curr;
            curr = (minimizer)nd;
            for(int i=0; i<curr.getChildrenSize(); i++){
                pnodes=countPrunedNodes(curr.getChild(i), pnodes);          
            //System.out.println(" <MINIMIZED> "+curr.getValue()+" :: ("+curr.alpha+","+curr.beta+")");                
            }
            if((curr.beta==MAX_VALUE)&&(curr.alpha==MIN_VALUE)){
                //System.out.print("PRUNED!!! =");
                pnodes++;
            }
            return pnodes;
        } 
        else if(nd instanceof InternalNode){
            InternalNode curr;
            curr = (InternalNode)nd;
            if((curr.alpha==MIN_VALUE)&&(curr.beta==MAX_VALUE)){
                //System.out.print("PRUNED!!! =");
                pnodes++;
            }
            //System.out.println(" <leaf> "+curr.getValue()+" :: ("+curr.alpha+","+curr.beta+")");
        }
        return pnodes;
    }
    
    @Override
    public String toString(){
        int flag=0;
        if(isNotInitialised(getRoot(),flag)==0){
            return super.toString();
        }
        if((getRoot().alpha==MIN_VALUE)&&(getRoot().beta==MAX_VALUE)){
            //System.out.println(getRoot().alpha+","+getRoot().beta);
            String s = super.toString();
            return s;
        }
        StringBuilder sb = new StringBuilder();
        sb = OpTreeToStringB(getRoot(),sb,2);
        return ""+sb;
    }
    public StringBuilder OpTreeToStringB(Object nd, StringBuilder sb,int vathos){
       if(nd instanceof maximizer){
            maximizer curr;
            curr = (maximizer)nd;
            sb.append(spaces(vathos-2));
            sb.append("{\n");
            sb.append(spaces(vathos));
            sb.append('"'+"type"+'"'+": "+'"'+"max"+'"'+","+"\n");
            //pruned
            if((curr.beta==MAX_VALUE)&&(curr.alpha==MIN_VALUE)){
                sb.append(spaces(vathos));
                sb.append('"'+"pruned"+'"'+": "+"true"+",\n");
            }
            else{
                sb.append(spaces(vathos));
                sb.append('"'+"value"+'"'+": ");
                sb.append(String.format("%.2f",curr.getValue()));
                sb.append(",\n");
            }
            sb.append(spaces(vathos));
            sb.append('"'+"children"+'"'+": [\n");
            for(int i=0; i<curr.getChildrenSize(); i++){
                sb = OpTreeToStringB(curr.getChild(i), sb, vathos+2);
                if(i!=curr.getChildrenSize()-1){
                    sb.append(",\n");
                }
            }    
            sb.append("\n");
            sb.append(spaces(vathos));
            sb.append("]\n");
            sb.append(spaces(vathos-2));
            sb.append("}");
        }
        else if(nd instanceof minimizer){
            minimizer curr;
            curr = (minimizer)nd;
            sb.append(spaces(vathos-2));
            sb.append("{\n");
            sb.append(spaces(vathos));
            sb.append('"'+"type"+'"'+": "+'"'+"min"+'"'+",\n");
            //pruned
            if((curr.beta==MAX_VALUE)&&(curr.alpha==MIN_VALUE)){
                sb.append(spaces(vathos));
                sb.append('"'+"pruned"+'"'+": "+"true"+",\n");
            }
            else{
                sb.append(spaces(vathos));
                sb.append('"'+"value"+'"'+": ");
                sb.append(String.format("%.2f",curr.getValue()));
                sb.append(",\n");
            }
            sb.append(spaces(vathos));
            sb.append('"'+"children"+'"'+": [\n");
            for(int i=0; i<curr.getChildrenSize(); i++){
                sb = OpTreeToStringB(curr.getChild(i),sb, vathos+2);
                if(i!=curr.getChildrenSize()-1){
                    sb.append(",\n");
                }
            }
            sb.append("\n");
            sb.append(spaces(vathos));
            sb.append("]\n");
            sb.append(spaces(vathos-2));
            sb.append("}");
        }
        else{
            InternalNode curr;
            curr = (InternalNode)nd;
            double num = curr.getValue();
            sb.append(spaces(vathos-2));
            sb.append("{\n");
            sb.append(spaces(vathos));
            sb.append('"'+"type"+'"'+": "+'"'+"leaf"+'"'+",\n");
            sb.append(spaces(vathos));
            sb.append('"'+"value"+'"'+": ");
            sb.append(String.format("%.2f",num));
            if((curr.alpha==MIN_VALUE)&&(curr.beta==MAX_VALUE)){
                sb.append(",\n");
                sb.append(spaces(vathos));
                sb.append('"'+"pruned"+'"'+": "+"true");
            }
            sb.append("\n");
            sb.append(spaces(vathos-2));
            sb.append("}");
            return sb;
        } 
       return sb;
    }
    

    @Override
    public void toFile(File file){
        try {
            file.createNewFile();
            try (FileWriter myWriter = new FileWriter(file)) {
                String s = toString();
                myWriter.write(s);
            }
            System.out.print("\nOK\n\n");
        } catch (IOException e) {
            System.out.print("\nUnable to write '"+file+"'\n\n");
            //e.printStackTrace();
        }
        
    }
}

