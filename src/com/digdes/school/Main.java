package com.digdes.school;

import java.util.List;
import java.util.Map;

public class Main {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static void main(String[] args) {
        JavaSchoolStarter javaSchoolStarter = new JavaSchoolStarter();
        try {
            List<Map<String, Object>> result = javaSchoolStarter.execute("INSERT VALUES ‘lastName’ = ‘Федоров’,‘id’=3, ‘age’=40, ‘active’=true");
            System.out.println(result);
            System.out.println(javaSchoolStarter.execute("UPDATE VALUES ‘active’=false, ‘active’=false, ‘cost’=10.1 where ‘id’=3"));
        } catch (AppException e) {
            System.out.println(ANSI_RED + e.getMessage() + ANSI_RESET);
        }
    }
}