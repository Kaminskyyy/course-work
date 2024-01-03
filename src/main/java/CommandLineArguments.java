public class CommandLineArguments {
    private String rootPath = "D:/KPI/4_curs/cursach/data";
    private int numThreads = 6;
    private int queueSize = 24;
    private int threadPoolSize = 12;

    public void parse(String[] args) {
        for (var argument: args) {
            if (argument.startsWith("--threads=")) {
                var cleared = argument.replace("--threads=", "");
                try {
                    numThreads = Integer.parseInt(cleared);
                } catch (Exception e) {
                    System.out.println("Invalid argument: key=threads value='" + cleared + "'");
                }
            }

            if (argument.startsWith("--root=")) {
                var cleared = argument.replace("--root=", "");
                if (cleared.isEmpty()) {
                    System.out.println("Invalid argument: key=root value='" + cleared + "'");
                }
                else rootPath = argument.replace("--root=", "");
            }

            if (argument.startsWith("--threadPoolSize=")) {
                var cleared = argument.replace("--threadPoolSize=", "");
                try {
                    threadPoolSize = Integer.parseInt(cleared);
                } catch (Exception e) {
                    System.out.println("Invalid argument: key=threadPoolSize value='" + cleared + "'");
                }
            }

            if (argument.startsWith("--queueSize=")) {
                var cleared = argument.replace("--queueSize=", "");
                try {
                    queueSize = Integer.parseInt(cleared);
                } catch (Exception e) {
                    System.out.println("Invalid argument: key=queueSize value='" + cleared + "'");
                }
            }
        }
    }

    public String getRoot() {
        return rootPath;
    }

    public int getNumThreads() {
        return numThreads;
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    public int getQueueSize() {
        return queueSize;
    }
}
