
package ce326.hw2;
import java.util.*;
import org.json.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
public class MinMax {
    String Jstr;
    minMaxOptimalTree tree;
    public MinMax(String Jstr){
        this.Jstr = Jstr; 
        this.tree = null;
    }
    public MinMax(){
        Jstr=null;
        tree=null;
    }
    public void setStr(String str){
        this.Jstr = str;
    }
    public static void main(String[] args) {
        MinMax mainMinMax = new MinMax();
        Scanner sc = new Scanner(System.in);
        while(true)
        {
            System.out.print("\n");
            System.out.print(String.format("%-15s %s","-i <filename>", ":  insert tree from file\n"));
            System.out.print(String.format("%-15s %s","-j [<filename>]", ":  print tree in the specified filename using JSON format\n"));
            System.out.print(String.format("%-15s %s","-d [<filename>]", ":  print tree in the specified filename using DOT format\n"));
            System.out.print(String.format("%-15s %s","-c",":  calculate tree using min-max algorithm\n"));
            System.out.print(String.format("%-15s %s","-p",":  calculate tree using min-max and alpha-beta pruning optimization\n"));
            System.out.print(String.format("%-15s %s","-q",":  quit this program"));
            System.out.print("\n\n$>  ");
            try{
                String strr = sc.nextLine();
                if(strr.length()<2){
                    continue;
                }
                String str = strr.replaceAll("\\s","");
                String ch = str.substring(0,2);
                int start=2, end=str.length();
            
                if("-i".equals(ch)){
                    for(int i=0; i<str.length(); i++){
                        if(str.charAt(i)=='<'){
                            start = i;
                            break;
                        }
                    }
                    String fileName = str.substring(start,end);
                    File file = new File(fileName);
                    if(!file.exists()){
                        System.out.print("\nUnable to find '"+fileName+"'\n\n");
                        continue;
                    }
                    if(!file.canRead()){
                        System.out.print("\nUnable to open '"+fileName+"'\n\n");
                        continue;
                    }
                    StringBuilder strBuilder = new StringBuilder();
                    try(Scanner sc2 = new Scanner(new File(fileName))) {
                        while( sc2.hasNextLine() ) {
                        String string = sc2.nextLine();
                        strBuilder.append(string);
                        strBuilder.append("\n");
                        }
                    } catch(IOException ex) {
                        continue;
                    } catch(IllegalArgumentException ex) {
                        System.out.print("\nInvalid format\n\n");
                        continue;
                    }
                    // // // //CHECK IF THE STRING IS JSON  
                    String jsonString = strBuilder.toString();
                    mainMinMax = new MinMax(jsonString);
                    try {
                        mainMinMax.tree = new minMaxOptimalTree(mainMinMax.Jstr);
                    } catch (JSONException | IllegalArgumentException ex) {
                        System.out.print("\nInvalid format\n\n");
                        continue;
                    }
                    System.out.print("\nOK\n\n");
                }//#################################################################
                else if("-j".equals(ch))
                {   ////if we re having a file
                    if(mainMinMax.tree==null){
                        System.out.print("\nNot OK\n\n");
                        continue;
                    }
                    if(str.length()>2){
                        for(int i=0; i<str.length(); i++){
                            if(str.charAt(i)=='<'){
                                start = i;
                                break;
                            }
                        }
                        String fileName = str.substring(start,end);
                        File file = new File(fileName);
                        if(file.exists()){
                            System.out.print("\nFile '"+fileName+"' already exists\n\n");
                            continue;
                        }
                        if(mainMinMax.tree==null){
                            if(mainMinMax.Jstr!=null){
                                System.out.print("\n"+mainMinMax.Jstr+"\n\n");
                            }
                            continue;
                        }
                        mainMinMax.tree.toFile(file);
                        //System.out.println("\nOK\n\n");
                    }////if we re not having a file
                    else{
                        System.out.print("\n"+mainMinMax.tree.toString()+"\n\n");
                    }
                }
                else if("-d".equals(ch))
                {
           
                }
                else if("-c".equals(ch))
                {// SIMPLE MINMAX
                    if((mainMinMax.tree!=null) && (mainMinMax.Jstr!=null) ){
                        mainMinMax.tree.simpleMinMax();
                        ArrayList<Integer> path = mainMinMax.tree.optimalPath();
                        System.out.print("\n");
                        for(int i=0; i<path.size(); i++){
                            System.out.print(path.get(i));
                            if(i!=path.size()-1){System.out.print(", ");}
                        }
                        System.out.print("\n\n");
                    }
                    else{
                        System.out.print("\nNot OK\n\n");
                    }
                }
                else if("-p".equals(ch))
                {// ALPHA BETA PRUNING
                    if(mainMinMax.tree!=null){
                        mainMinMax.tree.minMax();
                        System.out.print("\n["+mainMinMax.tree.size()+","+(int)mainMinMax.tree.prunedNodes()+']'+" ");
                        ArrayList<Integer> path = mainMinMax.tree.optimalPath();
                        for(int i=0; i<path.size(); i++){
                            System.out.print(path.get(i));
                            if(i!=path.size()-1){
                                System.out.print(", ");
                            }
                        }
                        System.out.print("\n\n");
                    }  
                    else{
                        System.out.print("\nNot OK\n\n");
                    }
                }
                else if("-q".equals(ch))
                {
                    sc.close();
                    //System.out.print("\n");
                    return;
                }
            }catch(NoSuchElementException ex) {
                //System.out.print("\n");
                //sc.next();
                //continue;
            }
        }
    }
}
/*    public boolean isJson(String strBuilder){
        this.Jstr = strBuilder;
        try {
            JSONObject myJsonObject=new JSONObject(strBuilder);
        } catch (JSONException | IllegalArgumentException e) {
            System.out.print("\nInvalid format\n\n");
            return false;
        }
        return true;
    }
    
}
*/
