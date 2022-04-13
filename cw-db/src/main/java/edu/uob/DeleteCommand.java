package edu.uob;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static java.util.Arrays.copyOfRange;

public class DeleteCommand extends DBCommand{
    private DBCondition dbCondition;

    public DeleteCommand(String command){
        super.setCommandVal(isDeleteCommand(command));
    }

    private boolean isDeleteCommand(String command){
        String[] commandArr = command.split(" ");
        if(commandArr.length < 5){
            super.setParseErr("false number of elements in DELETE");
            return false;
        }
        if(commandArr[0].equalsIgnoreCase("delete")){
            super.setDbCommand("delete");
            String[] condition = copyOfRange(commandArr,4,commandArr.length);
            DBCondition dbCondition = new DBCondition(condition);
            if(dbCondition.getVal()){
                this.dbCondition = dbCondition;
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    public void exeCommand(DBServer dbServer) throws IOException{
        if (usefulDatabase(dbServer)) {
            String str = dbServer.getDirectory() + ".txt";
            File tabFile = new File(str);
            DBTable dbTable = new DBTable(tabFile);
            ArrayList<String[]> finalTab = dbCondition.getResultTab(dbTable);
            if (finalTab != null) {
                dbTable.deleteTable(finalTab);
                dbServer.setOutput("[OK] you have deleted a table");
            }
        }
    }
}
