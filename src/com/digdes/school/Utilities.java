package com.digdes.school;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Utilities {
    private Utilities() {
    }

    public static List<String> getWordsFromString(String request) {
        List<String> result = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        char[] chars = request.toCharArray();
        boolean isQuote = false;
        for (int i = 0; i < chars.length; ++i) {
            if (chars[i] != ' ') {
                if (chars[i] == '’') {
                    stringBuilder.append(chars[i]);
                    result.add(stringBuilder.toString());
                    stringBuilder.setLength(0);
                    isQuote = false;
                } else if (chars[i] == '‘') {
                    isQuote = true;
                    stringBuilder.append(chars[i]);
                } else if (chars[i] == ',' || chars[i] == ';' && !isQuote) {
                    if (!stringBuilder.isEmpty()) {
                        result.add(stringBuilder.toString());
                        stringBuilder.setLength(0);
                    }
                    if (chars[i] == ',') result.add(String.valueOf(chars[i]));
                } else if (chars[i] == '>' || chars[i] == '<' && !isQuote) {
                    if (chars[i + 1] == '=') {
                        result.add(chars[i] + "=");
                        ++i;
                    } else result.add(String.valueOf(chars[i]));
                } else if (chars[i] == '=' && !isQuote) {
                    if (chars[i - 1] == '!') {
                        result.add(chars[i - 1] + "=");
                        stringBuilder.setLength(0);
                    } else result.add(String.valueOf(chars[i]));
                } else stringBuilder.append(chars[i]);
            } else {
                if (!isQuote) {
                    if (!stringBuilder.isEmpty()) {
                        result.add(stringBuilder.toString());
                        stringBuilder.setLength(0);
                    }
                } else stringBuilder.append(chars[i]);
            }
        }
        if (!stringBuilder.isEmpty()) result.add(stringBuilder.toString());
        return result;
    }

    public static List<Map<String, Object>> findRows(List<Map<String, Object>> data, List<String> keysConditionValue) {
        return null;
    }

    public static boolean checkCorrectnessKey(String key) {
        return switch (key.toLowerCase()) {
            case "id", "lastname", "age", "cost", "active" -> true;
            default -> false;
        };
    }

    public static boolean checkCorrectnessCondition(String key, String value) {
        return switch (value.toLowerCase()) {
            case "=", "!=" -> true;
            case ">", "<", ">=", "<=" -> "cost".equals(key) || "age".equals(key) || "id".equals(key);
            case "like", "ilike" -> "lastname".equals(key);
            default -> false;
        };
    }

    public static boolean checkCorrectnessValue(String key, String value) {
        return switch (key.toLowerCase()) {
            case "id", "age" -> {
                try {
                    Long.valueOf(value);
                    yield true;
                } catch (Exception e) {
                    yield false;
                }
            }
            case "lastname" -> value.contains("’");
            case "cost" -> {
                try {
                    Double.valueOf(value);
                    yield true;
                } catch (Exception e) {
                    yield false;
                }
            }
            case "active" -> "true".equals(value) || "false".equals(value);
            default -> false;
        };
    }


}
