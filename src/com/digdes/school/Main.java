package com.digdes.school;

public class Main {
    public static void main(String[] args) {
        JavaSchoolStarter starter = new JavaSchoolStarter();
        try {
            System.out.println(starter.execute("INSERT VALUES 'lastName' = 'Петров', 'id' = 1, 'age' = 30, 'active' = true, 'cost' = 5.4"));
            System.out.println(starter.execute("INSERT VALUES 'lastName' = 'Иванов', 'id' = 2, 'age' = 25, 'active' = false, 'cost' = 4.3"));
            System.out.println(starter.execute("INSERT VALUES 'lastName' = 'Федоров', 'id' = 3, 'age' = 40, 'active' = true"));
            System.out.println(starter.execute("UPDATE VALUES ‘active’=true  where ‘active’=false"));
            System.out.println(starter.execute("SELECT WHERE ‘age’>=30 and ‘lastName’ like ‘%п%’"));
            System.out.println(starter.execute("DELETE WHERE 'id' >= 2 AND 'active' = false"));
        } catch (AppException e) {
            DialogUtil.showErrorMessage(e.getMessage());
        }
    }
}