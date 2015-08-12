

import java.io.*;
import java.net.*;

public class HanServer {
	public static void main(String[] args) {
		ServerSocket serversocket = null;
		Socket socket = null;
		try {
			serversocket = new ServerSocket(9999);
			System.out.println("서버가 시작되었습니다.");

			while (true) {
				socket = serversocket.accept();
				System.out.println("클라이언트가 연결 되었습니다.");

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
			System.out.println("파일 수신 작업을 시작합니다.");
			dis = new DataInputStream(socket.getInputStream());
			// // 파일명을 전송 받고 파일명 수정.
			// String fName = dis.readUTF();
			// System.out.println("파일명 " + fName + "을 전송받았습니다.");

			// 파일을 생성하고 파일에 대한 출력 스트림 생성
			File f = new File("test.txt");
			fos = new FileOutputStream(f);
			//bos = new BufferedOutputStream(fos);
			System.out.println("파일을 생성하였습니다.");

			// 바이트 데이터를 전송받으면서 기록
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
			System.out.println("파일 수신 작업을 완료하였습니다.");
			System.out.println("받은 파일의 사이즈 : " + f.length());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void sendFile() {
		try {
			System.out.println("파일 전송 작업을 시작합니다.");
			dos = new DataOutputStream(socket.getOutputStream());
			pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream())), true);
			pw.println("receive");
			String fName = "test.txt";

			// 파일 내용을 읽으면서 전송
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
			System.out.println("파일 전송 작업을 완료하였습니다.");
			System.out.println("보낸 파일의 사이즈 : " + f.length());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
