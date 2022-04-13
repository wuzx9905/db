package edu.uob;

import java.io.File;
import java.io.IOException;

public class AlterCommand extends DBCommand{
    private String alter;
    private String attrName;

    public AlterCommand(String command){
        super.setCommandVal(isAlterCommand(command));
    }

    private boolean isAlterCommand(String command){
        String[] commandArr = command.split(" ");
        int length = commandArr.length;
        if(length != 5){
            super.setParseErr("unexpected elements in ALTER");
            return false;
        }
        if(commandArr[0].equalsIgnoreCase("alter")){
            super.setDbCommand("alter");
            if(!commandArr[1].equalsIgnoreCase("table")){
                super.setParseErr("expecting a tableName in "+commandArr[1]);
                return false;
            }
            boolean add = commandArr[3].equalsIgnoreCase("add");
            boolean drop = commandArr[3].equalsIgnoreCase("drop");
            if(!add && !drop){
                super.setParseErr("expecting 'add' or 'drop' in" + commandArr[3]);
            }else{
                this.alter = commandArr[3];
                this.attrName = commandArr[4];
                return true;
            }
        }
        return false;
    }
    public void exeCommand(DBServer dbServer) throws IOException {
        if (usefulDatabase(dbServer)) {
            String path = dbServer.getDirectory() + dbServer.getCurDatabase();
            File tabFile = new File(path);
            if (usefulTable(dbServer, tabFile)) {
                DBTable dbtable = new DBTable(tabFile);
                if (this.alter.equalsIgnoreCase("add")) {
                    dbtable.alterTable(this.attrName);
                } else {
                    for (int i = 0; i < dbtable.getColumns(); i++) {
                        if (this.attrName.equals(dbtable.getTable().toString())) {
                            dbtable.alterDrop(i);
                        }else{
                            dbServer.setError("false attribute in ALTER");
                        }
                    }
                }
                dbtable.save(tabFile);
                dbServer.setOutput("[OK] you have altered it");
            }
        }
    }
}
