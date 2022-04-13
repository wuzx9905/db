package edu.uob;

public class DBCommandArr {
    private final boolean val;
    private StringBuilder nameList;
    private String error;

    public DBCommandArr(String[] commandArr){
        int count = 0;
        StringBuilder commandList = new StringBuilder();
        for (String command: commandArr){
            commandList.append(command);
        }
        for(int i=0;i<commandList.length();i++){
            char symbol = commandList.charAt(i);
            switch (symbol) {
                case '=' -> count++;
                case ',' -> count--;
            }
        }
        if(count == 1){
            this.nameList = commandList;
            this.val = true;
        }else{
            this.error = "wrong number of comma and equal";
            this.val = false;
        }
    }

    public boolean getVal (){
        return this.val;
    }
    public String getError(){
        return this.error;
    }
    public StringBuilder getCommandArr(){
        return this.nameList;
    }
}
