package edu.uob;

import java.io.File;
import java.io.IOException;

public class DBCommand {
    private String dbCommand;
    private boolean validCommand;
    private String parseErr;

    public DBCommand(){
        parseErr = "";
        dbCommand = "";
        validCommand = false;
    }

    public void exeCommand(DBServer dbserver) throws IOException {
    }

    public boolean usefulDatabase(DBServer DBserver) {
        boolean result = true;
        if (DBserver.getCurDatabase().equals("")) {
            DBserver.setError("Please USE database firstly");
            result = false;
        }
        return result;
    }

    public boolean usefulTable(DBServer DBServer, File tableFile) {
        boolean result = true;
        if (!tableFile.exists()) {
            DBServer.setError("This table does not exist");
            result = false;
        }
        return result;
    }
    public boolean getValidCommand(){
        return this.validCommand;
    }
    public String getParseErr(){
        return this.parseErr;
    }
    public void setDbCommand(String dbCommand){
        this.dbCommand = dbCommand;
    }
    public void setCommandVal(boolean validCommand){
        this.validCommand = validCommand;
    }
    public void setParseErr(String parseErr){
        this.parseErr = parseErr;
    }
}
