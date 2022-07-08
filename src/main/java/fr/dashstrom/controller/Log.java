package fr.dashstrom.controller;

import java.io.PrintStream;

public class Log {

    public static void info(String message) {
        log(System.out, "INFO", message);
    }

    public static void error(String message) {
        log(System.err, "ERROR", message);
    }

    public static void log(PrintStream out, String level, String message) {
        out.printf("[%s] %s\n", level, message);
    }

}
