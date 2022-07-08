package fr.dashstrom;

public class Launcher {

    public static void main(String[] args) {
        try {
            Main.main(args);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

}