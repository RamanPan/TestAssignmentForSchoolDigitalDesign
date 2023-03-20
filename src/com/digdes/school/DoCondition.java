package com.digdes.school;

import java.util.Map;

public class DoCondition {
    private DoCondition() {
    }

    public static boolean equalCondition(String key, String value, Map<String, Object> row) throws NullPointerException {
        return switch (key) {
            case "id", "age" -> Integer.parseInt(value) == Integer.parseInt(row.get(key).toString());
            case "lastname" -> value.equals(row.get(key));
            case "cost" -> Double.parseDouble(value) == Double.parseDouble(row.get(key).toString());
            case "active" -> Boolean.parseBoolean(value) == Boolean.parseBoolean(row.get(key).toString());
            default -> false;
        };
    }

    public static boolean notEqualCondition(String key, String value, Map<String, Object> row) throws NullPointerException {
        return switch (key) {
            case "id", "age" -> Integer.parseInt(value) != Integer.parseInt(row.get(key).toString());
            case "lastname" -> !value.equals(row.get(key));
            case "cost" -> Double.parseDouble(value) != Double.parseDouble(row.get(key).toString());
            case "active" -> Boolean.parseBoolean(value) != Boolean.parseBoolean(row.get(key).toString());
            default -> false;
        };
    }

    public static boolean moreCondition(String key, String value, Map<String, Object> row) throws NullPointerException {
        return switch (key) {
            case "id", "age" -> Integer.parseInt(value) > Integer.parseInt(row.get(key).toString());
            case "cost" -> Double.parseDouble(value) > Double.parseDouble(row.get(key).toString());
            default -> false;
        };
    }

    public static boolean lessCondition(String key, String value, Map<String, Object> row) throws NullPointerException {
        return switch (key) {
            case "id", "age" -> Integer.parseInt(value) < Integer.parseInt(row.get(key).toString());
            case "cost" -> Double.parseDouble(value) < Double.parseDouble(row.get(key).toString());
            default -> false;
        };
    }

    public static boolean moreOrEqualCondition(String key, String value, Map<String, Object> row) throws NullPointerException {
        return switch (key) {
            case "id", "age" -> Integer.parseInt(value) >= Integer.parseInt(row.get(key).toString());
            case "cost" -> Double.parseDouble(value) >= Double.parseDouble(row.get(key).toString());
            default -> false;
        };
    }

    public static boolean lessOrEqualCondition(String key, String value, Map<String, Object> row) throws NullPointerException {
        return switch (key) {
            case "id", "age" -> Integer.parseInt(value) <= Integer.parseInt(row.get(key).toString());
            case "cost" -> Double.parseDouble(value) <= Double.parseDouble(row.get(key).toString());
            default -> false;
        };
    }

    public static boolean likeCondition(String key, String value, Map<String, Object> row) throws NullPointerException {
        String rowValue = row.get(key).toString();
        if (value.charAt(0) == '%' && value.charAt(value.length() - 1) == '%') {
            return rowValue.contains(value.substring(1, value.length() - 1));
        } else if (value.charAt(0) == '%') {
            return rowValue.endsWith(value.substring(1));
        } else if (value.charAt(value.length() - 1) == '%') {
            return rowValue.startsWith(value.substring(0, value.length() - 1));
        } else {
            return value.equals(rowValue);
        }
    }

    public static boolean ilikeCondition(String key, String value, Map<String, Object> row) throws NullPointerException {
        String rowValue = row.get(key).toString().toLowerCase();
        if (value.charAt(0) == '%' && value.charAt(value.length() - 1) == '%') {
            return rowValue.contains(value.substring(1, value.length() - 1).toLowerCase());
        } else if (value.charAt(0) == '%') {
            return rowValue.endsWith(value.substring(1).toLowerCase());
        } else if (value.charAt(value.length() - 1) == '%') {
            return rowValue.startsWith(value.substring(0, value.length() - 1).toLowerCase());
        } else {
            return value.equalsIgnoreCase(rowValue);
        }
    }
}
