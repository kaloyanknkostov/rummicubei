package com.gameEngine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PythonConnector {
    // Call with
    // System.out.println(PythonConnector.runPythonFile("python", "models\\java_connection_test.py", ""));
    public static String runPythonFile(String pythonExe, String filePath, String boardString){
        try {

            // Create command and execute it
            ProcessBuilder processBuilder = new ProcessBuilder(pythonExe, filePath, boardString);
            Process process = processBuilder.start();

            // Read current output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) { // Account for multiple lines
                output.append(line).append("\n");
            }

            // Get results on how the process finished
            int exitCode = process.waitFor();
            if (exitCode == 0) { // The Python script ran successfully
                return output.toString();
            } else { // Handle errors
                return null;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
