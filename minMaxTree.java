package ce326.hw2;
import java.io.*;
//import static java.lang.Integer.MAX_VALUE;
//import static java.lang.Integer.MIN_VALUE;
import java.util.Scanner;
import org.json.JSONObject;
import java.util.*;
import org.json.*;
public class minMaxTree {
    private InternalNode root;
    
    public InternalNode getRoot(){
        return root;
    }
    public minMaxTree(){
        root=null;
    }
    public minMaxTree(String jsonString){
        try {
            JSONObject myJsonObject=new JSONObject(jsonString);
        } catch (IllegalArgumentException e) {
            System.out.println("\nInvalid format\n\n");
        }
        
        //CREATE TREE
        JSONObject obj = new JSONObject(jsonString);
        Object j = buildTree(obj);
        if(j instanceof maximizer){
            root = (maximizer)j;
        }
        if(j instanceof minimizer){
            root = (minimizer)j;
        }
        //System.out.println("\n minmax="+minMax());
        //System.out.println(" numNodes="+size());
        //ArrayList<Integer> path = new ArrayList<Integer>();
        //path = optimalPath();
        //System.out.println(path);
    } 
    
    public minMaxTree(File file) throws FileNotFoundException{
        
        StringBuilder strBuilder = new StringBuilder();
        try(Scanner sc = new Scanner(file)) {
            while( sc.hasNextLine() ) {
                String string = sc.nextLine();
                strBuilder.append(string);
                strBuilder.append("\n");
            }
        } catch(FileNotFoundException ex){ 
            System.out.println("\nUnable to open '"+file.getName()+"'\n\n");
        }
        /*} catch(IllegalArgumentException ex) {
            System.out.println("\nInvalid format\n\n");
            //ex.printStackTrace();
        }*/
        String jsonString = strBuilder.toString();
        
        try {
            JSONObject myJsonObject=new JSONObject(jsonString);
        } catch (IllegalArgumentException e) {
            System.out.println("\nInvalid format\n\n");
        }
        //CREATE TREE
        JSONObject obj = new JSONObject(jsonString);
        Object j = buildTree(obj);
        if(j instanceof maximizer){
            root = (maximizer)j;
        }
        
    }
    
    public Object buildTree(JSONObject obj){
        String type = obj.getString("type");
        //System.out.println(type);
        
        if("max".equals(type)){
            maximizer max = new maximizer();
            JSONArray arr = obj.getJSONArray("children");
            max.setChildrenSize(arr.length());
               // System.out.println("     "+ max.getParent().getValue());
            if(root == null){
                root = max;
            }
            for (int i = 0; i < arr.length(); i++){
                String value = arr.getJSONObject(i).getString("type");
                if("min".equals(value)){
                    minimizer curr = (minimizer)buildTree(arr.getJSONObject(i));
                    curr.setParent(max);
                    max.insertChild(i, curr);
                }
                else if("leaf".equals(value)){
                    InternalNode curr = new InternalNode();
                    curr.setParent(max);
                    float num = arr.getJSONObject(i).getFloat("value");
                    curr.setValue(num);
                    max.insertChild(i, curr);
                }
            }
            return max;
        }
        if("min".equals(type)){
            minimizer min = new minimizer();
            JSONArray arr = obj.getJSONArray("children");
            min.setChildrenSize(arr.length());
            if(root == null){
                root = min;
            }
            for (int i = 0; i < arr.length(); i++){
                String value = arr.getJSONObject(i).getString("type");
                
                if("max".equals(value)){
                    //RECURSIVE
                    maximizer curr = (maximizer)buildTree(arr.getJSONObject(i));
                    curr.setParent(min);
                    min.insertChild(i, curr);
                }
                if("leaf".equals(value)){
                    InternalNode curr = new InternalNode();
                    curr.setParent(min);
                    float num = arr.getJSONObject(i).getFloat("value");
                    curr.setValue(num);
                    min.insertChild(i, curr);
                }
            }
            return min;
        }
        return root;
    }
    
    //YPOLOGISMOS ALGORITHM MINMAX
    public double minMax(){
        minMaxRecursive(root);
        return (double)root.getValue();
    }
    public void minMaxRecursive(Object nd){
        if(nd instanceof maximizer){
            maximizer curr =(maximizer)nd;
            if(curr.array!=null){
                for(int i=0; i<curr.getChildrenSize(); i++){
                    minMaxRecursive(curr.getChild(i));
                }
                
            }
            curr.setValue(curr.maxNode());
            return;
        }
        if(nd instanceof minimizer){
            minimizer curr = (minimizer)nd;
            if(curr.array!=null){
                for(int i=0; i<curr.getChildrenSize(); i++){
                    minMaxRecursive(curr.getChild(i));
                }
                
            }
            curr.setValue(curr.minNode());
        }
        
    }
    //ARI8MOS KOMVWN
    public int size(){
        int nodes=0;
        nodes = sizeCount(root,nodes);
        return nodes;
    }
    public int sizeCount(Object nd, int num){
        if(nd instanceof maximizer){
            maximizer curr = (maximizer)nd;
            num++;
            for(int i=0; i<curr.getChildrenSize(); i++){
                num = sizeCount(curr.getChild(i),num);
            }         
        }
        else if(nd instanceof minimizer){
            minimizer curr =(minimizer)nd;
            num++;
            for(int i=0; i<curr.getChildrenSize(); i++){
                num = sizeCount(curr.getChild(i),num);
            }
        }
        else{
            num++;
        }
        return num;
    }
    
    public ArrayList<Integer> optimalPath(){
        ArrayList<Integer> path = new ArrayList<>();
        Object nd = new Object();
        nd = root;
        while(true){
            if(nd instanceof maximizer){
                maximizer curr = (maximizer)nd;
                for(int i=0; i<curr.getChildrenSize(); i++){
                    if(curr.getChild(i).getValue()==root.getValue()){
                        path.add(i);
                        nd = curr.getChild(i);
                        break;
                    }
                }
            }
            else if(nd instanceof minimizer){
                minimizer curr = (minimizer)nd;
                for(int i=0; i<curr.getChildrenSize(); i++){
                    if(curr.getChild(i).getValue()==root.getValue()){
                        path.add(i);
                        nd = curr.getChild(i);
                        break;
                    }
                }
            }
            else{
               return path;
            }
        }
    }
    

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        int flag = 0;
        flag = isNotInitialised(root,flag);
        sb = treeToStringB(root,sb, flag);
        return sb+"";
    }
    public int isNotInitialised(Object nd, int flag){
        if(nd instanceof maximizer){
            maximizer curr = (maximizer)nd;
            for(int i=0; i<curr.getChildrenSize(); i++){
                if(curr.getValue()!=0){flag=1;}
                flag=isNotInitialised(curr.getChild(i),flag);
            }
        }
        if(nd instanceof minimizer){
            minimizer curr = (minimizer)nd;
            for(int i=0; i<curr.getChildrenSize(); i++){
                if(curr.getValue()!=0){flag=1;}
                flag=isNotInitialised(curr.getChild(i),flag);
            }
        }
        return flag;
    }
    public StringBuilder treeToStringB(Object nd, StringBuilder sb, int flag){
       if(nd instanceof maximizer){
            maximizer curr = (maximizer)nd;
            sb.append("{"+'"'+"type"+'"'+":"+'"'+"max"+'"'+',');
            if(flag == 1){
                sb.append('"'+"value"+'"'+"=");
                sb.append(curr.getValue());
                sb.append(",");
            }
            sb.append('"'+"children"+'"'+":[");
            for(int i=0; i<curr.getChildrenSize(); i++){
                sb = treeToStringB(curr.getChild(i), sb,flag);
                if(i!=curr.getChildrenSize()-1){
                    sb.append(",");
                }
            }    
            sb.append("]}");
        }
        else if(nd instanceof minimizer){
            minimizer curr =(minimizer)nd;
            sb.append("{"+'"'+"type"+'"'+":"+'"'+"min"+'"'+",");
            if(flag==1){
                sb.append('"'+"value"+'"'+"=");
                sb.append(curr.getValue());
                sb.append(",");
            }
            sb.append('"'+"children"+'"'+":[");
            for(int i=0; i<curr.getChildrenSize(); i++){
                sb = treeToStringB(curr.getChild(i),sb,flag);
                if(i!=curr.getChildrenSize()-1){
                    sb.append(",");
                }
            }
            sb.append("]}");
        }
        else{
            InternalNode curr = (InternalNode)nd;
            double num = curr.getValue();
            sb.append("{"+'"'+"type"+'"'+":"+'"'+"leaf"+'"'+","+'"'+"value"+":");
            sb.append(num);
            sb.append("}");
            return sb;
        } 
       return sb;
    }
    
    public void toFile(File file){
        try {
            file.createNewFile();
            FileWriter myWriter = new FileWriter(file);
            String str = toString();
            System.out.println(str);
            myWriter.write(str);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("\nUnable to write '"+file+"'\n\n");
            e.printStackTrace();
        }
        
    }
}
