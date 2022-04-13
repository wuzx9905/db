package edu.uob;

import java.util.ArrayList;

public class DBAttributes {
    private final boolean val;
    private String stringList = "";
    private String error;
    private String[] attrList;

    public DBAttributes(String[] commandArray){
        this.val = isAttr(commandArray);
    }

    private boolean isAttr(String[] commandArray){
        for (String s : commandArray) {
            if (s.equals("")) {
                this.stringList += s;
            }
        }
        if(!this.stringList.contains("(") && !this.stringList.contains(")")){
            this.attrList = this.stringList.split(",");
            return true;
        }else{
            this.error = "there is not a normal '()'";
            return false;
        }

    }

    public boolean getVal (){
        return this.val;
    }
    public String getError(){
        return this.error;
    }
    public String[] getAttrList(){
        return this.attrList;
    }
    public String getStringList(){
        return this.stringList;
    }
}
