package util;

// aslo good article https://www.javaworld.com/article/2071275/core-java/when-runtime-exec---won-t.html?page=1
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class ExecuteCommand {

	private static PrintStream out = System.out;


	public static void execRuntime(String cmd) {
		out.println("Starting to execute - " + cmd);

		Process process = null;
		OS os = new OS();

		try {
			if (os.isWindows()) {
				process = Runtime.getRuntime().exec("cmd /C " + cmd);
			} else {
				process = Runtime.getRuntime().exec(cmd);
			}
		} catch (IOException e) {
			out.println("Problems in running " + cmd + "\n" + e.toString());
			return;
		}

		int exitCode;
		// Starts a thread to read error stream
		ByteArrayOutputStream errorOutputStream = new ByteArrayOutputStream();
		StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), errorOutputStream);
		errorGobbler.start();

		// Starts a thread to read output stream
		ByteArrayOutputStream standardOutputStream = new ByteArrayOutputStream();
		StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream(), standardOutputStream);
		outputGobbler.start();

		try {
			out.println("Waiting to finish");
			exitCode = process.waitFor();
			out.println("Exit code " + exitCode);
			out.println("Command Finished");

			// Waits for threads to die
			errorGobbler.join();
			outputGobbler.join();
		} catch (InterruptedException e) {
		}

		// Get output and error messages
		String error = errorOutputStream.toString();
		String output = standardOutputStream.toString();
		output = output.replace("\r", "").replace("\u0000", "");
		if (!error.equals("")) out.println("Errors are : " + error);
	}


	public static void main(String[] args) {
		String app = "notepad.exe";
		String command = "taskkill -F -T -IM " +  app;
		ExecuteCommand.execRuntime(command);
	}
}
