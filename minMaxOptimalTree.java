package ce326.hw2;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
public class minMaxOptimalTree extends minMaxTree{
    //kataskeyasths : ftiaxnw dentro
    public minMaxOptimalTree(String jstr){
        super(jstr);
    }// 86 PRUNED
    //epilyetai o algori8mos minmax me alpha beta pruning
    public double minMax(){
        //System.out.println("AAAAAAAAAAAAAAAAA");
        minMaxOptimalRecursive(getRoot());
        return getRoot().getValue();
    }
    public double simpleMinMax(){
        return super.minMax();
    }
    public void minMaxOptimalRecursive(Object nd){
        if(nd instanceof maximizer){
            maximizer curr = new maximizer();
            curr = (maximizer)nd;
            for(int i=0; i<curr.getChildrenSize(); i++){
                curr.getChild(i).alpha=curr.alpha;
                curr.getChild(i).beta=curr.beta;
                minMaxOptimalRecursive(curr.getChild(i));
                if(curr.getChild(i).getValue()>curr.alpha)
                {
                    curr.alpha=curr.getChild(i).getValue();
                    //System.out.println("(("+i+"))MAX:");
                    //System.out.println("now alpha:<<"+curr.alpha+">>");
                    //System.out.println("now beta=<<"+curr.beta+">>");
                    if(curr.beta<=curr.alpha){
                        curr.setValue(curr.alpha);
                        //System.out.println(curr.alpha);
                        return;
                    }
                }
            }
            double value=curr.maxNode();
            curr.setValue(value);
            //System.out.println("max(("+value+"))");
            return;
        }
        if(nd instanceof minimizer){
            minimizer curr = new minimizer();
            curr = (minimizer)nd;
            for(int i=0; i<curr.getChildrenSize(); i++){
                curr.getChild(i).beta=curr.beta;
                curr.getChild(i).alpha=curr.alpha;
                minMaxOptimalRecursive(curr.getChild(i));
                if(curr.getChild(i).getValue()<curr.beta)
                {
                    curr.beta=curr.getChild(i).getValue();
                    //System.out.println(i + ") MINIMIZER:");
                    //System.out.println("now alpha=<<" + curr.alpha + ">>");
                    //System.out.println("now beta=<<" + curr.beta + ">>");
                    if(curr.beta<=curr.alpha){
                        curr.setValue(curr.beta);
                        //System.out.print("exit:"+curr.beta);
                        /*if(curr!=getRoot()){
                            System.out.println(", parent_alpha:(("+curr.getParent().alpha+"))");
                        }*/
                        return;
                    }
                }
            }
            double value=curr.minNode();
            curr.setValue(value);
            //System.out.print("min(("+value+"))");
            return;
        }
        if(nd instanceof InternalNode){
            InternalNode curr = (InternalNode)nd;
            curr.alpha = MAX_VALUE;
            curr.beta = MIN_VALUE;
        }
    }/*
    public double minMaxOptimalRecursive(Object nd){
        if(nd instanceof InternalNode){
            InternalNode curr = (InternalNode)nd;
            curr.alpha = MAX_VALUE;
            curr.beta = MIN_VALUE;
            return curr.getValue();
        }
        if(nd instanceof maximizer){
            maximizer curr = new maximizer();
            curr = (maximizer)nd;
            for(int i=0; i<curr.getChildrenSize(); i++){
            
            }
        }
    }*/
    // epistrefetai to optimal path
    @Override
    public ArrayList<Integer> optimalPath(){
        ArrayList<Integer> path = new ArrayList<>();
        path = findPath(getRoot(), path);
        return path;
        /*while(true){
            if(nd instanceof maximizer){
                //System.out.println(";-) ");
                maximizer curr = new maximizer();
                curr = (maximizer)nd;
                for(int i=0; i<curr.getChildrenSize(); i++){
                    if(curr.getChild(i).getValue()==getRoot().getValue()){
                        path.add(i);
                        nd = curr.getChild(i);
                        break;
                    }
                }
            }
            else if(nd instanceof minimizer){
                minimizer curr = new minimizer();
                curr = (minimizer)nd;
                for(int i=0; i<curr.getChildrenSize(); i++){
                    if(curr.getChild(i).getValue()==getRoot().getValue()){
                        path.add(i);
                        nd = curr.getChild(i);
                        break;
                    }
                }
            }
            else{
               return path;
            }
        }*/
    }
    public ArrayList<Integer> findPath(Object nd, ArrayList<Integer> path){
        ArrayList<Integer> temp = new ArrayList<>();
        if(nd instanceof maximizer){
            maximizer curr = (maximizer)nd;
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
            minimizer curr = (minimizer)nd;
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
            maximizer curr =(maximizer)nd;
            for(int i=0; i<curr.getChildrenSize(); i++){
                pnodes=countPrunedNodes(curr.getChild(i),pnodes);
            //System.out.println(" <MAX> "+curr.getValue()+" :: ("+curr.alpha+","+curr.beta+")");                
            }
            if((curr.beta==MAX_VALUE)&&(curr.alpha==MIN_VALUE)){
               // System.out.print("PRUNED!!! =");
                pnodes++;
            }
            return pnodes;
        }
        else if(nd instanceof minimizer){
            minimizer curr = (minimizer)nd;
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
            InternalNode curr  = (InternalNode)nd;
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
        sb = OpTreeToStringB(getRoot(),sb);
        return ""+sb;
    }
    public StringBuilder OpTreeToStringB(Object nd, StringBuilder sb){
       if(nd instanceof maximizer){
            maximizer curr = new maximizer();
            curr = (maximizer)nd;
            sb.append("{\n"+'"'+"type"+'"'+":"+'"'+"max"+'"'+","+"\n");
            sb.append('"'+"value"+'"'+"=");
            sb.append(curr.getValue());
            sb.append(",\n");
            //pruned
            if((curr.beta==MAX_VALUE)&&(curr.alpha==MIN_VALUE)){
                sb.append(","+'"'+"pruned"+'"'+"="+"true"+",\n");
            }
            sb.append('"'+"children"+'"'+":[\n");
            for(int i=0; i<curr.getChildrenSize(); i++){
                sb = OpTreeToStringB(curr.getChild(i), sb);
                if(i!=curr.getChildrenSize()-1){
                    sb.append(",");
                }
            }    
            sb.append("]\n}\n");
        }
        else if(nd instanceof minimizer){
            minimizer curr = new minimizer();
            curr = (minimizer)nd;
            sb.append("{\n"+'"'+"type"+'"'+":"+'"'+"min"+'"'+",\n");
            sb.append('"'+"value"+'"'+"=");
            sb.append(curr.getValue());
            sb.append(",");
            //pruned
            if((curr.beta==MAX_VALUE)&&(curr.alpha==MIN_VALUE)){
                sb.append(","+'"'+"pruned"+'"'+"="+"true"+",\n");
            }
            sb.append('"'+"children"+'"'+":[");
            for(int i=0; i<curr.getChildrenSize(); i++){
                sb = OpTreeToStringB(curr.getChild(i),sb);
                if(i!=curr.getChildrenSize()-1){
                    sb.append(",");
                }
            }
            sb.append("]}");
        }
        else{
            InternalNode curr = new InternalNode();
            curr = (InternalNode)nd;
            double num = curr.getValue();
            sb.append("{"+'"'+"type"+'"'+":"+'"'+"leaf"+'"'+","+'"'+"value"+":");
            sb.append(num);
            //pruned
            //System.out.println(curr.alpha+","+curr.beta);
            if((curr.alpha==MIN_VALUE)&&(curr.beta==MAX_VALUE)){
                sb.append(","+'"'+"pruned"+'"'+"="+"true");
            }
            sb.append("}");
            return sb;
        } 
       return sb;
    }
    

    public void toFile(File file){
        try {
            file.createNewFile();
            FileWriter myWriter = new FileWriter(file);
            /*if((getRoot().alpha==MIN_VALUE)&&(getRoot().beta==MAX_VALUE)){
                //System.out.println(getRoot().alpha+","+getRoot().beta);
                String s = super.toString();
                myWriter.write(s);
                myWriter.close();
                return;
            }*/
            //System.out.println(getRoot().alpha+","+getRoot().beta);
            String s = toString();
            myWriter.write(s);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("\n\nUnable to write '"+file+"'\n");
            //e.printStackTrace();
        }
        
    }
}
