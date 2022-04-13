package edu.uob;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class InsertCommand extends DBCommand{
    private String insert;

    public InsertCommand(String command){
        super.setCommandVal(isInsertCommand(command));
    }

    private boolean isInsertCommand(String command){
        String[] commandArr = command.split(" ");
        if(commandArr.length < 5){
            super.setParseErr("Invalid elements in INSERT");
            return false;
        }
        if (commandArr[0].equalsIgnoreCase("insert")) {
            super.setDbCommand("insert");
            this.insert = Arrays.copyOfRange(commandArr,3,4)[0] ;
            return true;
        } else {
            return false;
        }
    }

    public void exeCommand(DBServer dbServer) throws IOException {
        if (usefulDatabase(dbServer)) {
            String path = dbServer.getDirectory()+ ".txt";
            File tabFile = new File(path);
            if (usefulTable(dbServer, tabFile)) {
                DBTable tab = new DBTable(tabFile);
                boolean equalColumn = (tab.getColumns() == this.insert.toCharArray().length);
                if (!equalColumn) {
                    dbServer.setError("number of insert is not equal to table");
                }else{
                    tab.insertTable(tabFile, this.insert);
                    dbServer.setOutput("[OK] you have inserted a line");
                }
            }
        }
    }
}
