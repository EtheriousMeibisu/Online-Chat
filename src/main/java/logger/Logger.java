package logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    private static Logger instance;
    private static final String PATH = new File("src/main/java/logger/log.txt").getAbsolutePath();
    private static DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm:ss");

    private Logger(){}

    public static Logger getInstance(){
        if (instance == null){
            instance = new Logger();
        }
        return instance;
    }
    public boolean log(String mess){
        try(PrintWriter writer = new PrintWriter(new FileWriter(PATH, true))) {
            writer.write( mess + " ("+time.format(LocalTime.now()) + ")"+ "\n");
            writer.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
