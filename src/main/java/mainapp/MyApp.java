package mainapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class MyApp {

    // Method to check if the directory is initialized with Git
    public static String isGitInitialized() {
        String gitDirectoryExists;
        try {
            System.out.println("Checking if the directory is initialized with git...");
            gitDirectoryExists = executeCommand("git rev-parse --is-inside-work-tree").trim();
            System.out.println("Git Initialized: " + gitDirectoryExists);
            if (gitDirectoryExists.equals("true")) {
                return "true";
            } else {
                return "false";
            }
        } catch (Exception e) {
            System.out.println("Error in isGitInitialized method: " + e.getMessage());
            return "";
        }
    }

    public static String isCommitPresent() {
        String commitMessage;
        try {
            System.out.println("Checking if commit with message 'add untracked.txt' exists...");
            
            // Execute the command to get the log with the most recent commits
            commitMessage = executeCommand("git log --oneline").trim();
            System.out.println("Commit Messages: \n" + commitMessage);
            
            // Split the commit log by lines
            String[] commits = commitMessage.split("\n");
            
            // Loop through each commit and check if the message contains 'add untracked.txt'
            for (String commit : commits) {
                if (commit.toLowerCase().contains("add untracked.txt")) {
                    return "true";
                }
            }
            // If no matching commit message is found
            return "false"; // Commit with the required message not found
        } catch (Exception e) {
            System.out.println("Error in isCommitPresent method: " + e.getMessage());
            return "";
        }
    }

    public static String wasUntrackedFileRemovedOrNot() {
        try {
            // First check if 'untracked.txt' file exists in the current directory
            String fileStatus = executeCommand("git ls-files untracked.txt").trim();
            
            // If the file is present in the tracked files list, return false (file still present)
            if (!fileStatus.isEmpty()) {
                System.out.println("File 'untracked.txt' is not removed");
                return "false"; // File is still present in the tracked files
            }

            // If the file is not found in tracked files, proceed to check the commit details
            System.out.println("File 'untracked.txt' is no longer in the tracked files, checking commit history...");

            // Execute the command to get the log with the most recent commits
            String commitMessage = executeCommand("git log --oneline").trim();
            
            // Split the commit log by lines
            String[] commits = commitMessage.split("\n");

            // Loop through each commit and check if the message contains 'add untracked.txt'
            for (String commit : commits) {
                if (commit.toLowerCase().contains("add untracked.txt")) {
                    // Extract the commit hash from the log (the first part before the space)
                    String commitHash = commit.split(" ")[0];

                    System.out.println("Going through all logs");

                    // Show the full commit details using the commit hash
                    String commitDetails = executeCommand("git show " + commitHash).trim();

                    // Check if the commit details contain the string "untracked.txt"
                    if (commitDetails.toLowerCase().contains("untracked.txt")) {
                        // Print the detailed commit information
                        System.out.println("Commit Found: \n" + commitDetails);
                        return "true"; // Commit with the required message and untracked.txt found
                    }
                }
            }

            // If no matching commit message is found
            return "false"; // Commit with the required message not found
        } catch (Exception e) {
            System.out.println("Error in wasUntrackedFileRemovedOrNot method: " + e.getMessage());
            return "";
        }
    }

    private static String executeCommand(String command) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(".")); // Ensure this is the correct directory where Git repo is located
        processBuilder.command("bash", "-c", command);
        // System.out.println("Executing command: " + command);
        Process process = processBuilder.start();

        StringBuilder output = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        int exitVal = process.waitFor();
        System.out.println("Command executed with exit code: " + exitVal);
        if (exitVal == 0) {
            return output.toString();
        } else {
            System.out.println("Command failed with exit code: " + exitVal);
            throw new RuntimeException("Failed to execute command: " + command);
        }
    }

    // Main method to run the methods manually
    public static void main(String[] args) {
        try {
            // Check if .git directory exists
            String gitDirectoryExists = isGitInitialized();
            if (gitDirectoryExists.equals("true")) {
                System.out.println("Git repository initialized successfully.");
            } else {
                System.out.println("Git repository not initialized.");
                return;
            }

            // Check for at least one commit with the specific message
            String commitMessageExists = isCommitPresent();
            if (commitMessageExists.equals("true")) {
                System.out.println("Commit with message 'add untracked.txt' found.");
            } else {
                System.out.println("No commit with message 'add untracked.txt' found.");
            }

            // Check for untracked.txt file removed or not
            String untrackedFileRemoved = wasUntrackedFileRemovedOrNot();
            if (untrackedFileRemoved.equals("true")) {
                System.out.println("untracked.txt file was removed");
            } else {
                System.out.println("untracked.txt file was not found");
            }

        } catch (Exception e) {
            System.out.println("Error in main method: " + e.getMessage());
        }
    }
}
