package clientserver;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.imageio.ImageIO;

public class CaptureClient {


	public void makeSrceenshot() throws IOException{
		int serverPort = 4567;
		String serverHost = "localhost";

		Socket socket = new Socket(serverHost, serverPort);
		System.out.println("Connected to server");

		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		out.flush();
		ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

		out.writeObject("takeScreenshot");
		out.flush();
		BufferedImage screenshot = ImageIO.read(in);
		System.out.println("Will try to save image");

		ImageIO.write(screenshot, "png", new File("screenshot.png"));
		System.out.println("Closing: " + System.currentTimeMillis());
		socket.close();
	}


	public void killApplication(String app) throws IOException{
		int serverPort = 4567;
		String serverHost = "localhost";

		Socket socket = new Socket(serverHost, serverPort);
		System.out.println("Connected to server");

		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		out.flush();
		ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

		out.writeObject("kill " + app);
		out.flush();
		socket.close();
	}

	public static void main(String[] args) throws IOException {
		CaptureClient captureClient = new CaptureClient();
		//captureClient.killApplication("calc.exe");
		captureClient.makeSrceenshot();
		
	}
}