package edu.uob;

import java.util.ArrayList;

public class DBCondition {
    private final boolean val;
    private String error;
    private final String[] condition;

    public DBCondition(String[] commandArr){
        this.val = isDBCondition(commandArr);
        this.condition = commandArr;
    }

    private boolean isDBCondition(String[] commandArr){
        for (int i=0;i<commandArr.length;i++){
            String com = commandArr[i];
            if(i% 4 == 1&& isOperator(com)){
                this.error = "false OPERATOR in CONDITION";
                return false;
            }
        }
        return true;
    }


    private boolean  isOperator(String str){
        return switch (str) {
            case "==", "!=", "<=", ">=", "<", ">","like" -> true;
            default -> false;
        };
    }

    private boolean isInTable(DBTable dbTab){
        for(int i=0;i<this.condition.length;i++){
            String con = this.condition[i];
            if(i%4 == 0){
                boolean flag = false;
                for (String str : dbTab.getTable().get(0)){
                    if(str.equals(missSpace(con))){
                        flag = true;
                        break;
                    }
                }
                if(!flag){
                    this.error = missSpace(con + "is not in this table");
                    return false;
                }
            }
        }
        return true;
    }

    private ArrayList<String> getOrderArr(String[] dbConditionArr){
        ArrayList<String> orderArr = new ArrayList<>();
        for(String str: dbConditionArr){
            if(str.contains("(")){
                orderArr.add(str.substring(str.length()+1));
            }else if(str.contains(")")){
                orderArr.add(str);
            }
        }
        return orderArr;
    }



    private String missSpace(String str){
        StringBuilder newStr = new StringBuilder();
        for(int i=0;i<str.length();i++){
            if(str.charAt(i) !='(' && str.charAt(i)!=')'){
                newStr.append(str.charAt(i));
            }
        }
        return newStr.toString();
    }

    public boolean getVal (){
        return this.val;
    }

    public ArrayList<String[]> getResultTab(DBTable dbTab){
        if (isInTable(dbTab)) {
            String orderArr = String.valueOf(getOrderArr(this.condition));
            ArrayList<String[]> newTab = new ArrayList<>();
            newTab.add(orderArr.split(","));
            return newTab;
        }else{
            return null;
        }
    }

}
