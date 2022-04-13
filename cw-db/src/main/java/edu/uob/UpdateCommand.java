package edu.uob;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


import static java.util.Arrays.copyOfRange;

public class UpdateCommand extends DBCommand{
    private StringBuilder commandArray;
    private Integer mark;

    public UpdateCommand(String command) {
        super.setCommandVal(isUpdateCommand(command));
    }

    private boolean isUpdateCommand(String command){
        String[] commandArr = command.split(" ");
        int length = commandArr.length;
        this.mark = 0;
        if(length < 6){
            super.setParseErr("wrong number of elements in UPDATE");
            return false;
        }
        for(int n = 0;n<length;n++){
            String com = commandArr[n];
            if(com.equalsIgnoreCase("update")){
                super.setDbCommand("update");
            }else if(com.equalsIgnoreCase("where")){
                this.mark = n;
            }
        }
        if(this.mark == 0){
            super.setParseErr("missing 'where' in UPDATE");
            return false;
        }
        DBCommandArr list = new DBCommandArr(copyOfRange(commandArr,3, this.mark));
        if(list.getVal()){
            this.commandArray = list.getCommandArr();
            return true;
        }else{
            super.setParseErr(list.getError());
            return false;
        }
    }

    public void exeCommand(DBServer dbServer) throws IOException{
        if (usefulDatabase(dbServer)) {
            String str = dbServer.getDirectory() + ".txt";
            File tabFile = new File(str);
            if (usefulTable(dbServer, tabFile)) {
                DBTable dbTab = new DBTable(tabFile);
                ArrayList<String[]> newList = new ArrayList<>();
                if (this.commandArray.toString().length()==0) {
                    dbTab.updateTable(newList);
                    dbServer.setOutput("[OK] you have updated");
                }else{
                    dbServer.setError("unexpected values in UPDATE");
                }
            }
        }
    }
}
