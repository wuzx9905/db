package edu.uob;

import java.io.File;

public class UseCommand extends DBCommand {
    private String useDb;

    public UseCommand(String command){
        super.setCommandVal(isUseCommand(command));
    }

    private boolean isUseCommand(String command){
        String[] commandArr = command.split(" ");
        if(commandArr.length == 2 && commandArr[0].equalsIgnoreCase("use")){
            super.setDbCommand("use");
            this.useDb = commandArr[1];
            return true;
        }else{
            super.setParseErr("False command in USE");
            return false;
        }
    }
    public void exeCommand(DBServer dbServer){
        String path = dbServer.getDirectory() + this.useDb;
        File databaseFolder = new File(path);
        if(databaseFolder.exists() && databaseFolder.isDirectory()){
            dbServer.setCurDatabase(this.useDb);
            dbServer.setOutput("[OK] you now using " + dbServer.getCurDatabase());
        }else{
            dbServer.setError("It needs to create a database before you use it !");
        }
    }
}
