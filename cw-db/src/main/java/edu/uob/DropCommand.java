package edu.uob;

import java.io.File;

public class DropCommand extends DBCommand{
    private String drop;

    public DropCommand(String command){
        super.setCommandVal(isDropCommand(command));
    }

    private boolean isDropCommand(String command){
        String[] commandArr = command.split(" ");
        if(commandArr[0].equalsIgnoreCase("drop")){
            super.setDbCommand("drop");
            boolean table = commandArr[1].equalsIgnoreCase("table");
            boolean database = commandArr[1].equalsIgnoreCase("database");
            if(!table && !database){
                super.setParseErr( "should be 'table' or 'database'");
                return false;
            }else{
                this.drop = commandArr[1];
                return true;
            }
        }else{
            return false;
        }
    }

    public void exeCommand(DBServer dbServer){
        if(this.drop.equalsIgnoreCase("table")){
            if (usefulDatabase(dbServer)) {
                File dropTab = new File(dbServer.getCurDatabase());
                if (usefulTable(dbServer, dropTab)) {
                    if(!dropTab.delete()){
                      dbServer.setError("could not drop it");
                    }
                }
            }
        }else{
            File dropDb = new File(String.valueOf(dbServer.getDirectory()));
            if(dropDb.exists() && dropDb.isDirectory()){
                File[] files = dropDb.listFiles();
                assert files != null;
                for (File file : files) {
                    if(!file.delete()){
                        dbServer.setError("could not drop it");
                    }
                }
                if(!dropDb.delete()){
                    dbServer.setError("could not drop it");
                }
            }
        }
        dbServer.setOutput("[OK] you have dropped it");
    }
}
