package cn.yescallop.aid.console;

import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author Scallop Ye
 */
public class CommandReader extends Thread {

    private final LineReader lineReader;
    private final CommandHandler handler;
    private final String prompt;

    public CommandReader(CommandHandler handler, String prompt) throws IOException {
        super("CommandReader");
        Terminal terminal = TerminalBuilder.builder()
                .system(true)
                .jansi(true)
                .build();
        lineReader = LineReaderBuilder.builder()
                .terminal(terminal)
                .build();
        Logger.lineReader = lineReader;

        this.handler = handler;
        this.prompt = prompt;
    }

    @Override
    public void run() {
        while (true) {
            String line;
            try {
                line = lineReader.readLine(prompt).trim();
            } catch (UserInterruptException e) {
                handler.userInterrupt();
                return;
            } catch (EndOfFileException e) {
                //ignore
                return;
            }
            if (line.isEmpty())
                continue;
            String[] args = line.split(" +");
            if (args.length == 1) {
                handler.handle(args[0], new String[0]);
            } else {
                handler.handle(args[0], Arrays.copyOfRange(args, 1, args.length));
            }
        }
    }
}
