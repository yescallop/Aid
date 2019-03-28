package cn.yescallop.aid.console;

import org.fusesource.jansi.Ansi;
import org.jline.reader.LineReader;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import static java.time.temporal.ChronoField.*;

/**
 * @author Scallop Ye
 */
public final class Logger {

    protected static LineReader lineReader = null;

    private Logger() {
        //no instance
    }

    public static void logException(Throwable cause, String msg) {
        Logger.severe(msg);
        CharArrayWriter caw = new CharArrayWriter();
        cause.printStackTrace(new PrintWriter(caw));
        lineReader.printAbove(Ansi.ansi().fgRed().a(caw.toCharArray()).fgDefault().toString());
    }

    public static void logException(Throwable cause) {
        logException(cause, "An exception occurred");
    }

    public static void info(String str) {
        log("INFO", Ansi.Color.GREEN, str);
    }

    public static void info(Object obj) {
        info(String.valueOf(obj));
    }

    public static void warning(String str) {
        log("WARNING", Ansi.Color.YELLOW, str);
    }

    public static void warning(Object obj) {
        warning(String.valueOf(obj));
    }

    public static void severe(String str) {
        log("SEVERE", Ansi.Color.RED, str);
    }

    public static void severe(Object obj) {
        severe(String.valueOf(obj));
    }

    private static DateTimeFormatter FORMATTER_NORMAL; //HH:mm:ss

    static {
        FORMATTER_NORMAL = new DateTimeFormatterBuilder()
                .appendValue(HOUR_OF_DAY, 2)
                .appendLiteral(':')
                .appendValue(MINUTE_OF_HOUR, 2)
                .appendLiteral(':')
                .appendValue(SECOND_OF_MINUTE, 2)
                .toFormatter();
    }

    public static void log(String level, Ansi.Color color, String str) {
        String time = LocalTime.now().format(FORMATTER_NORMAL);
        if (lineReader != null) {
            lineReader.printAbove(Ansi.ansi().a(time).a(" [").fg(color).a(level).fgDefault().a("] " + str).toString());
        } else {
            System.out.println(time + " [" + level + "] " + str);
        }
    }
}
