package com.digdes.school;

import java.util.*;

public class JavaSchoolStarter {
    private final List<Map<String, Object>> collection = new ArrayList<>();

    public JavaSchoolStarter() {
    }

    private List<Map<String, Object>> executeUpdate(List<String> wordsFromRequest) throws AppException {
        List<String> keys = new LinkedList<>(List.of("id", "lastname", "age", "cost", "active"));
        List<String> keysConditionValue = new ArrayList<>();
        Map<String, Object> row = new HashMap<>();
        Status status = Status.GET_KEY;
        String key = "", condition = "";
        boolean existsWhere = false, isMistake = true;
        for (int i = 2; i < wordsFromRequest.size(); ++i) {
            String word = wordsFromRequest.get(i);
            if ("WHERE".equalsIgnoreCase(word)) {
                existsWhere = true;
                status = Status.GET_KEY;
                continue;
            }
            switch (status) {
                case GET_KEY -> {
                    isMistake = true;
                    word = word.substring(1, word.length() - 1);
                    if (!Utilities.checkCorrectnessKey(word)) throw new AppException("Ключ " + word + " неверен");
                    key = word.toLowerCase();
                    status = existsWhere ? Status.GET_CONDITION : Status.ONLY_EQUAL;
                }
                case ONLY_EQUAL -> {
                    if (!"=".equals(word))
                        throw new AppException("Отсутствует = между " + wordsFromRequest.get(i - 1) + " и " + wordsFromRequest.get(i + 1));
                    status = Status.GET_VALUE;
                }
                case GET_CONDITION -> {
                    if (!Utilities.checkCorrectnessCondition(key, word))
                        throw new AppException("Ошибка в условии для " + word);
                    condition = word;
                    status = Status.GET_VALUE;
                }
                case GET_VALUE -> {
                    isMistake = false;
                    if (existsWhere) {
                        if (!Utilities.checkCorrectnessValue(key, word))
                            throw new AppException("Ошибка в значении для нахождения строк с помощью ключа " + key);
                        if (word.contains("’")) word = word.substring(1, word.length() - 1);
                        keysConditionValue.add(key);
                        keysConditionValue.add(condition);
                        keysConditionValue.add(word);
                        status = Status.GET_LOGIC_OPERATOR;
                    } else {
                        if (!Utilities.checkCorrectnessValue(key, word))
                            throw new AppException("Ошибка в присваевом значении для " + key);
                        if (word.contains("’")) word = word.substring(1, word.length() - 1);
                        if (!keys.isEmpty()) {
                            if (!keys.remove(key)) throw new AppException("Повторное использование " + key);
                        } else throw new AppException("Запрос составлен неверно");
                        row.put(key, word);
                        status = Status.CHECK_SEPARATOR;
                    }
                }
                case GET_LOGIC_OPERATOR -> {
                    if ("OR".equalsIgnoreCase(word) || "AND".equalsIgnoreCase(word)) {
                        if (i == wordsFromRequest.size() - 1) throw new AppException("Запрос не закончен");
                        keysConditionValue.add(word);
                        status = Status.GET_KEY;
                    } else
                        throw new AppException("Отсутствует логический оператор между " + wordsFromRequest.get(i - 1) + " и " + wordsFromRequest.get(i + 1));
                }
                case CHECK_SEPARATOR -> {
                    if (",".equals(word)) {
                        if (i == wordsFromRequest.size() - 1) throw new AppException("Запрос не закончен");
                        status = Status.GET_KEY;
                    } else
                        throw new AppException("Отсутствует , между " + wordsFromRequest.get(i - 1) + " и " + wordsFromRequest.get(i + 1));
                }
            }
        }
        if (isMistake) throw new AppException("Запрос составлен неверно");
        return collection;
    }

    private List<Map<String, Object>> executeInsert(List<String> wordsFromRequest) throws AppException {
        List<String> keys = new LinkedList<>(List.of("id", "lastname", "age", "cost", "active"));
        Map<String, Object> row = new HashMap<>();
        Status status = Status.GET_KEY;
        String key = "";
        boolean isMistake = true;
        for (int i = 2; i < wordsFromRequest.size(); ++i) {
            String word = wordsFromRequest.get(i);
            switch (status) {
                case GET_KEY -> {
                    word = word.substring(1, word.length() - 1);
                    if (!Utilities.checkCorrectnessKey(word)) throw new AppException("Ключ " + word + " неверен");
                    key = word.toLowerCase();
                    status = Status.ONLY_EQUAL;
                    isMistake = true;
                }
                case ONLY_EQUAL -> {
                    if (!"=".equals(word))
                        throw new AppException("Отсутствует = между " + wordsFromRequest.get(i - 1) + " и " + wordsFromRequest.get(i + 1));
                    status = Status.GET_VALUE;
                }
                case GET_VALUE -> {
                    if (!Utilities.checkCorrectnessValue(key, word))
                        throw new AppException("Ошибка в присваевом значении для " + key);
                    if (word.contains("’")) word = word.substring(1, word.length() - 1);
                    if (!keys.isEmpty()) {
                        if (!keys.remove(key)) throw new AppException("Повторное использование " + key);
                    } else throw new AppException("Запрос составлен неверно");
                    row.put(key, word);
                    status = Status.CHECK_SEPARATOR;
                    isMistake = false;
                }
                case CHECK_SEPARATOR -> {
                    if (",".equals(word)) {
                        if (i == wordsFromRequest.size() - 1) throw new AppException("Запрос не закончен");
                        status = Status.GET_KEY;
                    } else
                        throw new AppException("Отсутствует , между " + wordsFromRequest.get(i - 1) + " и " + wordsFromRequest.get(i + 1));
                }
                default -> isMistake = true;
            }
        }
        if (isMistake) throw new AppException("Запрос составлен неверно");
        for (String k : keys) row.put(k, null);
        collection.add(row);
        return collection;
    }


    private List<Map<String, Object>> executeDelete(List<String> wordsFromRequest) throws AppException {
        if (wordsFromRequest.size() > 1 && !"WHERE".equalsIgnoreCase(wordsFromRequest.get(1)))
            throw new AppException("Запрос составлен неверно");
        else if (wordsFromRequest.size() == 1) collection.clear();
        else {
            List<String> keysConditionValue = new ArrayList<>();
            Status status = Status.GET_KEY;
            String key = "", condition = "";
            boolean isMistake = true;
            for (int i = 2; i < wordsFromRequest.size(); ++i) {
                String word = wordsFromRequest.get(i);
                switch (status) {
                    case GET_KEY -> {
                        isMistake = true;
                        word = word.substring(1, word.length() - 1);
                        if (!Utilities.checkCorrectnessKey(word)) throw new AppException("Ключ " + word + " неверен");
                        key = word.toLowerCase();
                        status = Status.GET_CONDITION;
                    }
                    case GET_CONDITION -> {
                        if (!Utilities.checkCorrectnessCondition(key, word))
                            throw new AppException("Ошибка в условии для " + word);
                        condition = word;
                        status = Status.GET_VALUE;
                    }
                    case GET_VALUE -> {
                        isMistake = false;
                        if (!Utilities.checkCorrectnessValue(key, word))
                            throw new AppException("Ошибка в значении для нахождения строк с помощью ключа " + key);
                        if (word.contains("’")) word = word.substring(1, word.length() - 1);
                        keysConditionValue.add(key);
                        keysConditionValue.add(condition);
                        keysConditionValue.add(word);
                        status = Status.GET_LOGIC_OPERATOR;
                    }
                    case GET_LOGIC_OPERATOR -> {
                        if ("OR".equalsIgnoreCase(word) || "AND".equalsIgnoreCase(word)) {
                            if (i == wordsFromRequest.size() - 1) throw new AppException("Запрос не закончен");
                            keysConditionValue.add(word);
                            status = Status.GET_KEY;
                        } else
                            throw new AppException("Отсутствует логический оператор между " + wordsFromRequest.get(i - 1) + " и " + wordsFromRequest.get(i + 1));
                    }
                    default -> throw new AppException("Запрос составлен неверно");
                }
            }
            if (isMistake) throw new AppException("Запрос составлен неверно");
        }
        return collection;
    }


    private List<Map<String, Object>> executeSelect(List<String> wordsFromRequest) throws AppException {
        return null;
    }

    public List<Map<String, Object>> execute(String request) throws AppException {
        List<Map<String, Object>> result;
        List<String> wordsRequest = Utilities.getWordsFromString(request);
        System.out.println(wordsRequest);
        switch (wordsRequest.get(0).toUpperCase()) {
            case "SELECT" -> {
                result = executeSelect(wordsRequest);
            }
            case "INSERT" -> {
                if ("VALUES".equalsIgnoreCase(wordsRequest.get(1))) result = executeInsert(wordsRequest);
                else throw new AppException("Отсутствует ключевое слово VALUES");
            }
            case "UPDATE" -> {
                if ("VALUES".equalsIgnoreCase(wordsRequest.get(1))) result = executeUpdate(wordsRequest);
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
