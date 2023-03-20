package com.digdes.school;

public class Main {
    public static void main(String[] args) {
        JavaSchoolStarter starter = new JavaSchoolStarter();
        try {
            System.out.println(starter.execute("INSERT VALUES 'lastName' = 'Федоров' , 'id' = 3, 'age' = 40, 'active' = true"));
            System.out.println(starter.execute("UPDATE VALUES 'active'=false, 'cost'=10.1 where 'id'=8"));
            System.out.println(starter.execute("SELECT "));
        } catch (AppException e) {
            DialogUtil.showErrorMessage(e.getMessage());
        }
    }
}