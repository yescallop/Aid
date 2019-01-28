package cn.yescallop.aid.console;

import org.fusesource.jansi.Ansi;
import org.jline.reader.LineReader;

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

    public static void info(String str) {
        log("INFO", Ansi.Color.GREEN, str);
    }

    public static void warning(String str) {
        log("WARNING", Ansi.Color.YELLOW, str);
    }

    public static void severe(String str) {
        log("SEVERE", Ansi.Color.RED, str);
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
