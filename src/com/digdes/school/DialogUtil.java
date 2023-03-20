package com.digdes.school;

public class DialogUtil {
    private static final String ANSI_RESET = "\u001B[0m";

    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";

    private static final String ANSI_GREEN = "\u001B[32m";
    private DialogUtil() {
    }

    public static void showErrorMessage(String message) {
        System.out.println(ANSI_RED + "Ошибка: " + message + ANSI_RESET);
    }

    public static void showSuccessMessage(String message) {
        System.out.println(ANSI_GREEN + message + ANSI_RESET);
    }
    public static void showInfoMessage(String message) {
        System.out.println(ANSI_YELLOW + message + ANSI_RESET);
    }
}
