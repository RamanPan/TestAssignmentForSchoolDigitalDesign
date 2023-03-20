package com.digdes.school;

import java.util.*;

public class JavaSchoolStarter {
    private final List<Map<String, Object>> collection = new ArrayList<>();

    public JavaSchoolStarter() {
    }

    public List<Integer> findRows(List<String> keysConditionsValuesAndLogicOperators) {
        boolean andOr = true;
        String key, condition, value, logicOperator;
        List<Integer> indexesRightRows = new LinkedList<>();
        Map<String, Object> row;
        int size = keysConditionsValuesAndLogicOperators.size();
        for (int i = 0; i < size; i += 4) {
            key = keysConditionsValuesAndLogicOperators.get(i);
            condition = keysConditionsValuesAndLogicOperators.get(i + 1);
            value = keysConditionsValuesAndLogicOperators.get(i + 2);
            if (andOr) {
                for (int j = 0; j < collection.size(); ++j) {
                    row = collection.get(j);
                    if (Utilities.determineCondition(key, condition, value, row)) {
                        indexesRightRows.add(j);
                    }
                }
            } else {
                for (Integer index : indexesRightRows) {
                    row = collection.get(index);
                    if (!Utilities.determineCondition(key, condition, value, row)) {
                        indexesRightRows.remove(index);
                    }
                }
            }
            if (i + 4 < size) {
                logicOperator = keysConditionsValuesAndLogicOperators.get(i + 4);
                andOr = "OR".equalsIgnoreCase(logicOperator);
            }
        }
        return indexesRightRows;
    }

    private List<String> getKeysConditionsValuesAndLogicOperators(List<String> wordsFromRequest) throws AppException {
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
                    if (!Utilities.checkCorrectnessKey(word)) throw new AppException(Messages.WRONG_KEY + word);
                    key = word.toLowerCase();
                    status = Status.GET_CONDITION;
                }
                case GET_CONDITION -> {
                    if (!Utilities.checkCorrectnessCondition(key, word))
                        throw new AppException(Messages.WRONG_CONDITION + key);
                    condition = word;
                    status = Status.GET_VALUE;
                }
                case GET_VALUE -> {
                    isMistake = false;
                    if (word.equalsIgnoreCase("null"))
                        throw new AppException(Messages.FIND_NULL + key);
                    if (!Utilities.checkCorrectnessValue(key, word))
                        throw new AppException(Messages.WRONG_VALUE_FOR_COMPARE + key);
                    if (word.contains("’") || word.contains("'")) word = word.substring(1, word.length() - 1);
                    keysConditionValue.add(key);
                    keysConditionValue.add(condition);
                    keysConditionValue.add(word);
                    status = Status.GET_LOGIC_OPERATOR;
                }
                case GET_LOGIC_OPERATOR -> {
                    if ("OR".equalsIgnoreCase(word) || "AND".equalsIgnoreCase(word)) {
                        if (i == wordsFromRequest.size() - 1) throw new AppException(Messages.REQUEST_NOT_DONE);
                        keysConditionValue.add(word);
                        status = Status.GET_KEY;
                    } else
                        throw new AppException(Messages.MISS_LOGIC_OPERATOR + wordsFromRequest.get(i - 1) + " и " + wordsFromRequest.get(i + 1));
                }
                default -> throw new AppException(Messages.WRONG_REQUEST);
            }
        }
        if (isMistake) throw new AppException(Messages.WRONG_REQUEST);
        return keysConditionValue;
    }

    private List<Map<String, Object>> executeUpdate(List<String> wordsFromRequest) throws AppException {
        List<String> keys = new LinkedList<>(List.of("id", "lastname", "age", "cost", "active"));
        List<String> keysConditionValue = new ArrayList<>();
        List<Map<String, Object>> updatedValues = new ArrayList<>();
        Map<String, Object> row = new HashMap<>(), collectionRow;
        Status status = Status.GET_KEY;
        String key = "", condition = "";
        boolean existsWhere = false, isMistake = true;
        for (int i = 2; i < wordsFromRequest.size(); ++i) {
            String word = wordsFromRequest.get(i);
            if (Utilities.isWhere(word)) {
                existsWhere = true;
                status = Status.GET_KEY;
                continue;
            }
            switch (status) {
                case GET_KEY -> {
                    isMistake = true;
                    word = word.substring(1, word.length() - 1);
                    if (!Utilities.checkCorrectnessKey(word)) throw new AppException(Messages.WRONG_KEY + word);
                    key = word.toLowerCase();
                    status = existsWhere ? Status.GET_CONDITION : Status.ONLY_EQUAL;
                }
                case ONLY_EQUAL -> {
                    if (!"=".equals(word))
                        throw new AppException(Messages.MISS_EQUAL + wordsFromRequest.get(i - 1) + " и " + wordsFromRequest.get(i + 1));
                    status = Status.GET_VALUE;
                }
                case GET_CONDITION -> {
                    if (!Utilities.checkCorrectnessCondition(key, word))
                        throw new AppException(Messages.WRONG_CONDITION + key);
                    condition = word;
                    status = Status.GET_VALUE;
                }
                case GET_VALUE -> {
                    isMistake = false;
                    if (existsWhere) {
                        if (word.equalsIgnoreCase("null"))
                            throw new AppException(Messages.FIND_NULL + key);
                        if (!Utilities.checkCorrectnessValue(key, word))
                            throw new AppException(Messages.WRONG_VALUE_FOR_COMPARE + key);
                        if (word.contains("’") || word.contains("'")) word = word.substring(1, word.length() - 1);
                        keysConditionValue.add(key);
                        keysConditionValue.add(condition);
                        keysConditionValue.add(word);
                        status = Status.GET_LOGIC_OPERATOR;
                    } else {
                        if (!Utilities.checkCorrectnessValue(key, word))
                            throw new AppException(Messages.WRONG_VALUE + key);
                        if (word.contains("’") || word.contains("'")) word = word.substring(1, word.length() - 1);
                        if (!keys.isEmpty()) {
                            if (!keys.remove(key)) throw new AppException(Messages.REPEAT_KEY + key);
                        } else throw new AppException(Messages.WRONG_REQUEST);
                        row.put(key, word);
                        status = Status.CHECK_SEPARATOR;
                    }
                }
                case GET_LOGIC_OPERATOR -> {
                    if ("OR".equalsIgnoreCase(word) || "AND".equalsIgnoreCase(word)) {
                        if (i == wordsFromRequest.size() - 1) throw new AppException(Messages.REQUEST_NOT_DONE);
                        keysConditionValue.add(word);
                        status = Status.GET_KEY;
                    } else
                        throw new AppException(Messages.MISS_LOGIC_OPERATOR + wordsFromRequest.get(i - 1) + " и " + wordsFromRequest.get(i + 1));
                }
                case CHECK_SEPARATOR -> {
                    if (",".equals(word)) {
                        if (i == wordsFromRequest.size() - 1) throw new AppException(Messages.REQUEST_NOT_DONE);
                        status = Status.GET_KEY;
                    } else
                        throw new AppException(Messages.MISS_COMMA + wordsFromRequest.get(i - 1) + " и " + wordsFromRequest.get(i + 1));
                }
            }
        }
        if (isMistake) throw new AppException(Messages.WRONG_REQUEST);
        List<Integer> indexesRightRows = findRows(keysConditionValue);
        if (indexesRightRows.isEmpty()) DialogUtil.showInfoMessage(Messages.UPDATE_NOT_FOUND);
        else DialogUtil.showSuccessMessage(Messages.UPDATE_SUCCESS);
        for (Integer index : indexesRightRows) {
            collectionRow = collection.get(index);
            for (String k : row.keySet())
                collectionRow.replace(k, row.get(k));
            updatedValues.add(collectionRow);
        }
        return updatedValues;
    }

    private List<Map<String, Object>> executeInsert(List<String> wordsFromRequest) throws AppException {
        List<String> keys = new LinkedList<>(List.of("id", "lastname", "age", "cost", "active"));
        List<Map<String, Object>> insertedValues = new ArrayList<>();
        Map<String, Object> row = new HashMap<>();
        Status status = Status.GET_KEY;
        String key = "";
        boolean isMistake = true;
        for (int i = 2; i < wordsFromRequest.size(); ++i) {
            String word = wordsFromRequest.get(i);
            switch (status) {
                case GET_KEY -> {
                    word = word.substring(1, word.length() - 1);
                    if (!Utilities.checkCorrectnessKey(word)) throw new AppException(Messages.WRONG_KEY + word);
                    key = word.toLowerCase();
                    status = Status.ONLY_EQUAL;
                    isMistake = true;
                }
                case ONLY_EQUAL -> {
                    if (!"=".equals(word))
                        throw new AppException(Messages.MISS_EQUAL + wordsFromRequest.get(i - 1) + " и " + wordsFromRequest.get(i + 1));
                    status = Status.GET_VALUE;
                }
                case GET_VALUE -> {
                    if (!Utilities.checkCorrectnessValue(key, word))
                        throw new AppException(Messages.WRONG_VALUE + key);
                    if (word.contains("’") || word.contains("'")) word = word.substring(1, word.length() - 1);
                    if (!keys.isEmpty()) {
                        if (!keys.remove(key)) throw new AppException(Messages.REPEAT_KEY + key);
                    } else throw new AppException(Messages.WRONG_REQUEST);
                    row.put(key, word);
                    status = Status.CHECK_SEPARATOR;
                    isMistake = false;
                }
                case CHECK_SEPARATOR -> {
                    if (",".equals(word)) {
                        if (i == wordsFromRequest.size() - 1) throw new AppException(Messages.REQUEST_NOT_DONE);
                        status = Status.GET_KEY;
                    } else
                        throw new AppException(Messages.MISS_COMMA + wordsFromRequest.get(i - 1) + " и " + wordsFromRequest.get(i + 1));
                }
                default -> isMistake = true;
            }
        }
        if (isMistake) throw new AppException(Messages.WRONG_REQUEST);
        for (String k : keys) row.put(k, null);
        collection.add(row);
        insertedValues.add(row);
        DialogUtil.showSuccessMessage(Messages.INSERT_SUCCESS);
        return insertedValues;
    }


    private List<Map<String, Object>> executeDelete(List<String> wordsFromRequest) throws AppException {
        List<Map<String, Object>> deletedValues = new ArrayList<>();
        if (wordsFromRequest.size() > 1 && !Utilities.isWhere(wordsFromRequest.get(1)))
            throw new AppException(Messages.WRONG_REQUEST);
        else if (wordsFromRequest.size() == 1) collection.clear();
        else {
            List<String> keysConditionsValuesLogicOperators = getKeysConditionsValuesAndLogicOperators(wordsFromRequest);
            List<Integer> indexesRightRows = findRows(keysConditionsValuesLogicOperators);
            for (int index : indexesRightRows) {
                deletedValues.add(collection.get(index));
                collection.remove(index);
            }
        }
        DialogUtil.showSuccessMessage(Messages.DELETE_SUCCESS);
        return deletedValues;
    }


    private List<Map<String, Object>> executeSelect(List<String> wordsFromRequest) throws AppException {
        List<Map<String, Object>> selectedValues = new ArrayList<>();
        if (wordsFromRequest.size() > 1 && !Utilities.isWhere(wordsFromRequest.get(1)))
            throw new AppException(Messages.WRONG_REQUEST);
        else if (wordsFromRequest.size() == 1) selectedValues = collection;
        else {
            List<String> keysConditionsValuesLogicOperators = getKeysConditionsValuesAndLogicOperators(wordsFromRequest);
            List<Integer> indexesRightRows = findRows(keysConditionsValuesLogicOperators);
            for (int index : indexesRightRows) selectedValues.add(collection.get(index));
        }
        DialogUtil.showSuccessMessage(Messages.SELECT_SUCCESS);
        return selectedValues;
    }

    public List<Map<String, Object>> execute(String request) throws AppException {
        List<Map<String, Object>> result;
        List<String> wordsRequest = Utilities.getWordsFromString(request);
        switch (wordsRequest.get(0).toUpperCase()) {
            case "SELECT" -> result = executeSelect(wordsRequest);
            case "INSERT" -> {
                if ("VALUES".equalsIgnoreCase(wordsRequest.get(1))) result = executeInsert(wordsRequest);
                else throw new AppException(Messages.MISS_VALUES);
            }
            case "UPDATE" -> {
                if ("VALUES".equalsIgnoreCase(wordsRequest.get(1))) result = executeUpdate(wordsRequest);
                else throw new AppException(Messages.MISS_VALUES);
            }
            case "DELETE" -> result = executeDelete(wordsRequest);

            default -> throw new AppException(Messages.MISS_KEYWORD);
        }
        return result;
    }
}
