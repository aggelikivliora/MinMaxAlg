package ce326.hw2;
import java.io.*;
import java.util.Scanner;
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
    public minMaxTree(String jsonString) throws JSONException,IllegalArgumentException{
        //CREATE TREE
        JSONObject obj;
        //try{
            obj= new JSONObject(jsonString);
            Object j = buildTree(obj);
            if(j instanceof maximizer){
                root = (maximizer)j;
            }
            if(j instanceof minimizer){
                root = (minimizer)j;
            }
        //}catch(JSONException | IllegalArgumentException e){
            //System.out.println("\nInvalid format\n\n");
        ////}
    } 
    
    public minMaxTree(File file) throws FileNotFoundException, JSONException{
        
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
        JSONObject myJsonObject;
        try {
            myJsonObject=new JSONObject(jsonString);
            Object j = buildTree(myJsonObject);
            if(j instanceof maximizer){
                root = (maximizer)j;
            }
            else{
                root = (minimizer)j;
            }
        } catch (IllegalArgumentException e) {
            System.out.println("\nInvalid format\n\n");
        }
    }
    
    public Object buildTree(JSONObject obj) throws JSONException{
        //String type = obj.getString("type");
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
                    //float num = arr.getJSONObject(i).getFloat("value");
                    double num = arr.getJSONObject(i).getDouble("value");
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
                    double num = arr.getJSONObject(i).getDouble("value");
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
            maximizer curr;
            curr = (maximizer)nd;
            if(curr.array!=null){
                for(int i=0; i<curr.getChildrenSize(); i++){
                    minMaxRecursive(curr.getChild(i));
                }
                
            }
            curr.setValue(curr.maxNode());
            return;
        }
        if(nd instanceof minimizer){
            minimizer curr;
            curr = (minimizer)nd;
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
            maximizer curr;
            curr = (maximizer)nd;
            num++;
            for(int i=0; i<curr.getChildrenSize(); i++){
                num = sizeCount(curr.getChild(i),num);
            }         
        }
        else if(nd instanceof minimizer){
            minimizer curr ;
            curr = (minimizer)nd;
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
        Object nd ;
        nd = root;
        while(true){
            if(nd instanceof maximizer){
                maximizer curr;
                curr = (maximizer)nd;
                for(int i=0; i<curr.getChildrenSize(); i++){
                    if(curr.getChild(i).getValue()==root.getValue()){
                        path.add(i);
                        nd = curr.getChild(i);
                        break;
                    }
                }
            }
            else if(nd instanceof minimizer){
                minimizer curr;
                curr = (minimizer)nd;
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
        sb = treeToStringB(root,sb, flag, 2);
        return sb+"";
    }
    
    public int isNotInitialised(Object nd, int flag){
        if(nd instanceof maximizer){
            maximizer curr;
            curr = (maximizer)nd;
            for(int i=0; i<curr.getChildrenSize(); i++){
                if(curr.getValue()!=0){flag=1;}
                flag=isNotInitialised(curr.getChild(i),flag);
            }
        }
        if(nd instanceof minimizer){
            minimizer curr;
            curr = (minimizer)nd;
            for(int i=0; i<curr.getChildrenSize(); i++){
                if(curr.getValue()!=0){flag=1;}
                flag=isNotInitialised(curr.getChild(i),flag);
            }
        }
        return flag;
    }
    
    public StringBuilder treeToStringB(Object nd, StringBuilder sb, int flag, int vathos){
       if(nd instanceof maximizer){
            maximizer curr;
            curr = (maximizer)nd;
            sb.append(spaces(vathos-2));
            sb.append("{\n");
            sb.append(spaces(vathos));
            sb.append('"'+"type"+'"'+":"+'"'+"max"+'"'+",\n");
            if(flag == 1){
                sb.append(spaces(vathos));
                sb.append('"'+"value"+'"'+": ");
                sb.append(curr.getValue());
                sb.append(",");
                sb.append("\n");
            }
            sb.append(spaces(vathos));
            sb.append('"'+"children"+'"'+": [\n");
            
            for(int i=0; i<curr.getChildrenSize(); i++){
                sb = treeToStringB(curr.getChild(i), sb,flag, vathos+2);
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
            curr =(minimizer)nd;
            sb.append(spaces(vathos-2));
            sb.append("{\n");
            sb.append(spaces(vathos));
            sb.append('"'+"type"+'"'+": "+'"'+"min"+'"'+",\n");
            if(flag==1){
                sb.append(spaces(vathos));
                sb.append('"'+"value"+'"'+": ");
                sb.append(curr.getValue());
                sb.append(",");
                sb.append("\n");
            }
            sb.append(spaces(vathos));
            sb.append('"'+"children"+'"'+": [\n");
            
            for(int i=0; i<curr.getChildrenSize(); i++){
                sb = treeToStringB(curr.getChild(i),sb,flag, vathos+2);
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
            InternalNode curr = (InternalNode)nd;
            double num = curr.getValue();
            sb.append(spaces(vathos-2));
            sb.append("{\n");
            sb.append(spaces(vathos));
            sb.append('"'+"type"+'"'+": "+'"'+"leaf"+'"'+",\n");
            sb.append(spaces(vathos));
            sb.append('"'+"value"+'"'+": ");
            sb.append(num);
            sb.append("\n");
            sb.append(spaces(vathos-2));
            sb.append("}");
            return sb;
        } 
       return sb;
    }
    
    public String spaces(int num){
        StringBuilder strb = new StringBuilder("");
        for(int i=0; i<num; i++){
            strb.append(" ");
        }
        return strb.toString();
    }
    
    public void toFile(File file){
        try {
            file.createNewFile();
            try (FileWriter myWriter = new FileWriter(file)) {
                String str = toString();
                System.out.println(str);
                myWriter.write(str);
            }
            System.out.println("\nOK\n\n");
        } catch (IOException e) {
            System.out.println("\nUnable to write '"+file+"'\n\n");
        }
    }
}

