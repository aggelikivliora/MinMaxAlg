
package ce326.hw2;
import java.util.*;
import org.json.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
public class MinMax {
    String Jstr;
    minMaxOptimalTree tree;
    //minMaxTree tree;
    public MinMax(String Jstr){
        this.Jstr = Jstr; 
        this.tree = null;
        //this.opTree=null;
    }
    public MinMax(){
        Jstr=null;
        tree=null;
        //opTree=null;
    }
    public void setStr(String str){
        this.Jstr = str;
    }
    /**
     * @param args the command line arguments
     */
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
            System.out.print("\n\n$> ");
            try/*(Scanner sc = new Scanner(System.in))*/{
            //Scanner sc = new Scanner(System.in);
            String strr = sc.nextLine();
            //sc.close();
            //sc.next();
            //System.out.println(strr.length());
            if(strr.length()<2){
                //sc.next();
                //sc.close();
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
                    System.out.println("\nUnable to find '"+fileName+"'\n\n");
                    continue;
                }
                if(!file.canRead()){
                    System.out.println("\nUnable to open '"+fileName+"'\n\n");
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
                    //ex.printStackTrace();
                    continue;
                } catch(IllegalArgumentException ex) {
                    System.out.println("\nInvalid format\n\n");
                    //ex.printStackTrace();
                    continue;
                    //ex.printStackTrace();
                }
                // // // //CHECK IF THE STRING IS JSON  
                String jsonString = strBuilder.toString();
                MinMax myMinMax = new MinMax(jsonString);
                if(!(myMinMax.isJson(jsonString))){
                    continue;
                }
                mainMinMax = myMinMax;
                mainMinMax.tree = new minMaxOptimalTree(mainMinMax.Jstr);
                System.out.println("\nOK\n\n");
            }//#################################################################
            if("-j".equals(ch))
            {   ////if we re having a file
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
                        System.out.println("\nFile '"+fileName+"' already exists\n\n");
                        continue;
                    }
                    if(mainMinMax.tree==null){
                        if(mainMinMax.Jstr!=null){
                            System.out.println("\n"+mainMinMax.Jstr+"\n\n");
                        }
                        continue;
                    }
                    mainMinMax.tree.toFile(file);
                    System.out.println("\nOK\n\n");
                }////if we re not having a file
                else{
                    System.out.println("\n"+mainMinMax.tree.toString()+"\n\n");
                }
            }
            if("-d".equals(ch))
            {
           
            }
            if("-c".equals(ch))
            {// SIMPLE MINMAX
                if(mainMinMax.tree!=null){
                    double rt=mainMinMax.tree.simpleMinMax();
                    ArrayList<Integer> path = mainMinMax.tree.optimalPath();
                    System.out.print("\n");
                    for(int i=0; i<path.size(); i++){
                        System.out.print(path.get(i));
                        if(i!=path.size()-1){System.out.print(", ");}
                    }
                    System.out.print("\n\n\n");
                }
                else{
                    System.out.print("\nNot OK\n\n");
                }
            }
            if("-p".equals(ch))
            {// ALPHA BETA PRUNING
                if(mainMinMax.tree!=null){
                    mainMinMax.tree.minMax();
                    System.out.print("\n["+mainMinMax.tree.size()+","+(int)mainMinMax.tree.prunedNodes()+']'+" ");
                    double rt=mainMinMax.tree.minMax();
                    ArrayList<Integer> path = mainMinMax.tree.optimalPath();
                    for(int i=0; i<path.size(); i++){
                        System.out.print(path.get(i)+"");
                        if(i!=path.size()-1){System.out.print(", ");}
                    }
                    System.out.print("\n\n");
                }  
                else{
                    System.out.print("\nNot OK\n\n");
                }
            }
            if("-q".equals(ch))
            {
                sc.close();
                System.out.print("\n");
                return;
            }
            }catch(NoSuchElementException ex) {
                sc.next();
            }
        }
    }
    
    public boolean isJson(String strBuilder){
        this.Jstr = strBuilder;
        try {
            JSONObject myJsonObject=new JSONObject(strBuilder);
        } catch (JSONException | IllegalArgumentException e) {
            System.out.println("\nInvalid format\n\n");
            return false;
        }
        return true;
    }
    
}
