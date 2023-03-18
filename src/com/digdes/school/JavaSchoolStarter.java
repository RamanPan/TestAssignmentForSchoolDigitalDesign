package com.digdes.school;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JavaSchoolStarter {
    private final List<Map<String, Object>> collection = new ArrayList<>();

    private List<Map<String, Object>> executeUpdate(String[] wordsFromRequest) throws AppException {
        return null;
    }

    private List<Map<String, Object>> executeInsert(String[] wordsFromRequest) throws AppException {
        boolean isMistake = true;
        for (int i = 2; i < wordsFromRequest.length; ++i) {

        }
        return null;
    }


    private List<Map<String, Object>> executeDelete(String[] wordsFromRequest) throws AppException {
        return null;
    }


    private List<Map<String, Object>> executeSelect(String[] wordsFromRequest) throws AppException {
        return null;
    }

    public JavaSchoolStarter() {
    }

    public List<Map<String, Object>> execute(String request) throws AppException {
        List<Map<String, Object>> result;
        request = request.replace("=", " = ");
        String[] wordsRequest = request.split(" ");
        switch (wordsRequest[0].toUpperCase()) {
            case "SELECT" -> {
                result = executeSelect(wordsRequest);
            }
            case "INSERT" -> {
                if ("VALUES".equalsIgnoreCase(wordsRequest[1])) result = executeInsert(wordsRequest);
                else throw new AppException("Отсутствует ключевое слово VALUES");
            }
            case "UPDATE" -> {
                if ("VALUES".equalsIgnoreCase(wordsRequest[1])) result = executeUpdate(wordsRequest);
                else throw new AppException("Отсутствует ключевое слово VALUES");
            }
            case "DELETE" -> {
                result = executeDelete(wordsRequest);
            }
            default -> throw new AppException("Отсутсвует ключевое слово для совершения действия\n");
        }
        return result;
    }
}
