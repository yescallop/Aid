package cn.yescallop.aid.console;

import org.fusesource.jansi.Ansi;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;

/**
 * @author Scallop Ye
 */
public class CommandReader extends Thread {

    private final LineReader reader;
    private final CommandHandler handler;
    private final String prompt;
    private static CommandReader instance;

    public static CommandReader instance() {
        return instance;
    }

    public CommandReader(CommandHandler handler, String prompt) throws IOException {
        super("CommandReader");
        Terminal terminal = TerminalBuilder.builder()
                .system(true)
                .jansi(true)
                .build();
        reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .build();
        this.handler = handler;
        this.prompt = prompt;
        instance = this;
    }

    @Override
    public void run() {
        while (true) {
            String line;
            try {
                line = reader.readLine(prompt).trim();
            } catch (UserInterruptException e) {
                handler.userInterrupt();
                return;
            } catch (EndOfFileException e) {
                e.printStackTrace();
                return;
            }
            if (line.isEmpty())
                continue;
            int index = line.indexOf(' ');
            if (index < 0) {
                handler.handle(line, new String[0]);
            } else {
                String cmd = line.substring(0, index);
                String[] args = line.substring(index).split(" +");
                handler.handle(cmd, args);
            }
        }
    }

    public static void info(String str) {
        log("INFO", Ansi.Color.GREEN, str);
    }

    public static void warning(String str) {
        log("WARNING", Ansi.Color.RED, str);
    }

    public static void log(String level, Ansi.Color color, String str) {
        if (instance != null) {
            instance.reader.printAbove(Ansi.ansi().a('[').fg(color).a(level).fgDefault().a("] " + str).toString());
        } else {
            System.out.println('[' + level + "] " + str);
        }
    }
}
