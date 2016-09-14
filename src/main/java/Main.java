import org.apache.commons.cli.*;
import server.Server;

public class Main {
    private static final String SERVER_NAME = "HTTP Server";
    private static final int PORT = 8080;
    private static final String DIRECTORY_INDEX = "index.html";
    private static String directory;
    private static int cpuNumber;

    public static void main(String[] args) throws InterruptedException {
        parseCommandLineArguments(args);

        final Server server = new Server(SERVER_NAME, PORT, directory, DIRECTORY_INDEX, cpuNumber);

        System.out.println("Starting server at port " + PORT);
        server.run();
    }

    private static void parseCommandLineArguments(String[] args) {
        final Options options = new Options();

        final Option directoryOption = new Option("r", true, "directory");
        directoryOption.setRequired(true);
        options.addOption(directoryOption);

        final Option cpuNumberOption = new Option("c", true, "CPU number");
        cpuNumberOption.setRequired(true);
        options.addOption(cpuNumberOption);

        final CommandLineParser parser = new PosixParser();
        final HelpFormatter formatter = new HelpFormatter();
        final CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("", options);
            System.exit(1);
            return;
        }

        directory = cmd.getOptionValue("r");
        if (!directory.endsWith("/")) {
            directory += '/';
        }

        cpuNumber = Integer.parseInt(cmd.getOptionValue("c"));
    }
}
