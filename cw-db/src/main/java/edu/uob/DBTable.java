package edu.uob;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class DBTable {
    private int columns;
    private ArrayList<String[]> table = new ArrayList<>();
    private final ArrayList<String> tableString = new ArrayList<>();

    public DBTable(File tableFile) throws IOException{
        FileReader reader = new FileReader(tableFile);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String firstLine = bufferedReader.readLine();
        if(firstLine!=null){
            String next;
            while((next = bufferedReader.readLine()) != null){
                this.tableString.add(next);
                this.table.add(next.split(","));
            }
        }
    }

    public DBTable (String name,String line) throws IOException{
        File tab = new File(name);
        try {
            tab.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }
        FileWriter writer = new FileWriter(tab);
        writer.write("id " + "name "+ line + "\n");
        writer.flush();
        writer.close();
    }

    public void deleteTable(ArrayList<String[]> list){
        for(int i=1;i<list.size();i++){
            table.remove(table.get(i));
        }
    }

    public void save(File tableFile) throws IOException{
        FileWriter writer = new FileWriter(tableFile);
        for (String[] strings : table) {
            int j = 0;
            while (j < this.columns) {
                writer.write(strings[j]);
                j++;
            }
        }
        writer.flush();
        writer.close();
    }

    public void updateTable(ArrayList<String[]> list){
        for (String[] strings : list) {
            int j = 0;
            int mark = 0;
            while (j < table.get(0).length) {
                if (strings[j].equals(table.get(j)[0])) {
                    mark = j;
                }
                j++;
            }
            for (String[] value : table) {
                if (Objects.equals(strings[0], value[0])) {
                    value[mark] = strings[0];
                }
            }
        }
    }

    public void alterTable(String column){
        ArrayList<String[]> newTab = new ArrayList<>();
        String message = this.tableString.get(0) + column;
        newTab.add(message.split(""));
        this.table = newTab;
    }

    public void alterDrop(int column){
        ArrayList<String[]> newTab = new ArrayList<>();
        StringBuilder firstL = new StringBuilder();
        for(int i=0;i<this.columns;i++){
            firstL.append(table.get(0)[i]);
        }
        newTab.add(firstL.toString().split(","));
        this.columns = column;
        this.table = newTab;
    }


    public void insertTable(File tabFile, String insert) {
        try {
            FileWriter writer = new FileWriter(tabFile,true);
            writer.write(  missSpace(insert) + "\n");
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public String missSpace(String str){
        StringBuilder newString = new StringBuilder();
        for(int i=0;i<str.length();i++){
            newString.append(str.charAt(i) == ' '? "": str.charAt(i));
        }
        return newString.toString();
    }

    public ArrayList<String[]> getTable(){
        return this.table;
    }
    public int getColumns(){
        return this.columns;
    }
}
