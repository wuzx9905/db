package edu.uob;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Arrays.copyOfRange;

public class SelectCommand extends DBCommand{
    private boolean dbConValue;
    private String[] attrList;


    public SelectCommand(String command){
        super.setCommandVal(isSelectCommand(command));
    }

    private boolean isSelectCommand(String command){
        this.dbConValue = false;
        String[] commandArr = command.split(" ");
        int length = commandArr.length;
        if(commandArr[0].equalsIgnoreCase("select") && length >= 4){
            super.setDbCommand("select");
        }else{
            super.setParseErr("wrong numbers of elements in SELECT");
            return false;
        }
        return isValidFrom(commandArr);
    }

    private boolean isValidFrom(String[] commandArr){
        AtomicInteger mark = new AtomicInteger();
        for(int i=0;i<commandArr.length;i++){
            if(commandArr[i].equalsIgnoreCase("from")){
                mark.set(i);
            }
        }
        if(mark.get() == 0){
            super.setParseErr("missing 'from' in SELECT");
            return false;
        }
        if(commandArr[mark.get()+commandArr.length-2].equalsIgnoreCase("where")){
            super.setParseErr("missing 'where' in SELECT");
            return false;
        }
        DBAttributes list = new DBAttributes(copyOfRange(commandArr,1, mark.get()));
        if(list.getVal()){
            this.attrList = list.getAttrList();
            return this.attrList.length == 1;
        }else {
            super.setParseErr(list.getError());
            return false;
        }
    }

    public void exeCommand (DBServer dbServer) throws IOException{
        if (usefulDatabase(dbServer)) {
            String path = dbServer.getDirectory() + ".txt";
            File tabFile = new File(path);
            if (usefulTable(dbServer, tabFile)) {
                ArrayList<String[]> finalTab = null;
                DBTable dbTab = new DBTable(tabFile);
                StringBuilder finalTable = new StringBuilder();
                if (!this.dbConValue) {
                    finalTab = dbTab.getTable();
                }
                if (finalTab != null) {
                    if (Objects.equals(this.attrList[1], "*")) {
                        for (String[] strings : finalTab) {
                            finalTable.append(Arrays.toString(strings));
                            finalTable.append("\n");
                        }
                    } else {
                        dbServer.setError("wrong attributes in SELECT");
                        return;
                    }
                    System.out.println(finalTable);
                    dbServer.setOutput(finalTable.toString());
                }

            }
        }
    }
}
