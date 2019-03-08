package cn.yescallop.aid.console;

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
                String[] args = line.substring(index + 1).split(" +");
                handler.handle(cmd, args);
            }
        }
    }
}
