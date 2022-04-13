package edu.uob;

import java.io.File;
import java.io.IOException;

public class CreateCommand extends DBCommand{
    private String[] createArr;
    private String tableAttr = "";

    public CreateCommand(String command){
        super.setCommandVal(isCreateCommand(command));
    }

    private boolean isCreateCommand(String command){
        String[] commandArr = command.split(" ");
        if(commandArr[0].equalsIgnoreCase("create")){
            super.setDbCommand("create");
            boolean table = commandArr[1].equalsIgnoreCase("table");
            boolean database = commandArr[1].equalsIgnoreCase("database");
            if(!table && !database){
                super.setParseErr("expected 'table' or 'database' in " + commandArr[1]);
                return false;
            }else{
                this.createArr = commandArr;
                if (commandArr.length > 3){
                    String[] attrList = command.substring(1,4).split(" ");
                    DBAttributes list = new DBAttributes(attrList);
                    if(list.getVal()){
                        this.tableAttr = list.getStringList();
                    }
                }
                return true;
            }
        }else{
            return false;
        }
    }

    public void exeCommand(DBServer dbServer) throws IOException {
        if(createArr[1].equalsIgnoreCase("database")){
            String path = dbServer.getDirectory()+ this.createArr[2] + ".txt";
            File databaseFolder = new File(path);
            if(databaseFolder.exists()){
                dbServer.setError("there is already a database been CREATE: " + this.createArr[2]);
                return;
            }
            dbServer.setOutput("[OK] you have created a database");
            dbServer.setOutput("[OK] you have created a database");
        }else{
            if (usefulDatabase(dbServer)) {
                String database = dbServer.getCurDatabase();
                String path = dbServer.getDirectory() + database + ".txt";
                File tableF = new File(path);
                if (tableF.exists()) {
                    dbServer.setError("this table has been existed");
                    return;
                }
                DBTable newTab;
                newTab = new DBTable(path, this.tableAttr);
                if(newTab.getTable()!=null){
                    newTab.save(tableF);
                    dbServer.setOutput("[OK] you have create a table");
                }
            }
            dbServer.setOutput("[OK] you have created a table");
        }
    }
}
