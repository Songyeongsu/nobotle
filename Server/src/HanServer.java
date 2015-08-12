

import java.io.*;
import java.net.*;

public class HanServer {
	public static void main(String[] args) {
		ServerSocket serversocket = null;
		Socket socket = null;
		try {
			serversocket = new ServerSocket(9999);
			System.out.println("������ ���۵Ǿ����ϴ�.");

			while (true) {
				socket = serversocket.accept();
				System.out.println("Ŭ���̾�Ʈ�� ���� �Ǿ����ϴ�.");

				Handler hd = new Handler(socket);
				hd.start();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class Handler extends Thread {
	Socket socket = null;
	BufferedReader buffer;
	FileOutputStream fos;
	DataInputStream dis;
	BufferedOutputStream bos;
	DataOutputStream dos;
	FileInputStream fis;
	BufferedInputStream bis;
	PrintWriter pw;

	public Handler(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		String command = null;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			while (true) {
				do {
					command = in.readLine();
				} while (command == null);

				System.out.println(command);

				switch (command) {
				case "receive":
					receiveFile();
					break;
				case "send":
					sendFile();
					break;
				}
				command = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void receiveFile() {
		try {
			System.out.println("���� ���� �۾��� �����մϴ�.");
			dis = new DataInputStream(socket.getInputStream());
			// // ���ϸ��� ���� �ް� ���ϸ� ����.
			// String fName = dis.readUTF();
			// System.out.println("���ϸ� " + fName + "�� ���۹޾ҽ��ϴ�.");

			// ������ �����ϰ� ���Ͽ� ���� ��� ��Ʈ�� ����
			File f = new File("test.txt");
			fos = new FileOutputStream(f);
			//bos = new BufferedOutputStream(fos);
			System.out.println("������ �����Ͽ����ϴ�.");

			// ����Ʈ �����͸� ���۹����鼭 ���
			int len;
			int size = 4096;
			byte[] data = new byte[size];
			while ((len = dis.read(data)) != -1) {
				fos.write(data, 0, len);
			}

			//bos.flush();
			///bos.close();
			fos.close();
			dis.close();
			System.out.println("���� ���� �۾��� �Ϸ��Ͽ����ϴ�.");
			System.out.println("���� ������ ������ : " + f.length());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void sendFile() {
		try {
			System.out.println("���� ���� �۾��� �����մϴ�.");
			dos = new DataOutputStream(socket.getOutputStream());
			pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream())), true);
			pw.println("receive");
			String fName = "test.txt";

			// ���� ������ �����鼭 ����
			File f = new File(fName);
			fis = new FileInputStream(f);
			bis = new BufferedInputStream(fis);

			int len;
			int size = 4096;
			byte[] data = new byte[size];
			while ((len = bis.read(data)) != -1) {
				dos.write(data, 0, len);
			}

			dos.flush();
			dos.close();
			bis.close();
			fis.close();
			pw.close();
			System.out.println("���� ���� �۾��� �Ϸ��Ͽ����ϴ�.");
			System.out.println("���� ������ ������ : " + f.length());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
