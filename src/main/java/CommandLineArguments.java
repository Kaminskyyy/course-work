public class CommandLineArguments {
    private String rootPath = null;
    private int numThreads = 0;

    public void parse(String[] args) {
        for (var argument: args) {
            if (argument.startsWith("--threads=")) {
                numThreads = Integer.getInteger(argument.replace("--threads=", ""));
            }

            if (argument.startsWith("--root=")) {
                rootPath = argument.replace("--root=", "");
            }
        }
    }

    public String getRoot() {
        if (rootPath == null) rootPath = "D:/KPI/4_curs/cursach/data";

        return rootPath;
    }

    public int getNumThreads() {
        if (numThreads == 0) numThreads = 6;

        return numThreads;
    }
}
