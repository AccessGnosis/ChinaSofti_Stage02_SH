/**
* Describe: 
* Keyword: 
* Hint: 
* Filename: Server.java
* Copyright 2017-08-18 By Gnosis. Allright reserved.
* Time: 上午9:45:19
*/
package com.chinasofti.day27.socket.copy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

//服务端程序
public class ServerDemo {
	private ServerSocket server;

	// 无参构造，初始化服务端对象
	public ServerDemo() {
		try {
			System.out.println("初始化服务器");
			server = new ServerSocket(8088);
			System.out.println("服务端初始化完毕");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ServerDemo server = new ServerDemo();
		server.start();
	}

	// 服务端开始工作的方法
	private void start() {
		try {
			System.out.println("等待客户端链接");
			/*
			 * ServerSocket的accept()方法，用于监听8088端口，等待客户端的链接，
			 * 该方法是一个阻塞方法，直到一个客户端的链接，
			 * 否则一致阻塞，若客户端连接了返回的是客户端的Socket
			 */
			while (true) {
				Socket clientSocket = server.accept();
				// System.out.println("一个客户端链接了");
				/*
				 * 通过socket获取客户端的地址信息
				 */

				/*
				 * 当一个客户端连接后，启动一个线程ClientHandler，然后将
				 */
				Runnable handler = new ClientHandler(clientSocket);

				Thread t = new Thread(handler);
				t.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

/*
 * 服务端中一个线程，用于和某个客户端交互
 * 使用线程的目的就是使得服务端可以处理多个客户端
 */
class ClientHandler implements Runnable {
	// 当前线程处理的客户端的Socket
	private Socket clientSocket;
	private InetAddress address;

	public ClientHandler(Socket socket) {
		this.clientSocket = socket;
		// 通过clientSocket获取客户端的地址信息
		address = clientSocket.getInetAddress();
		// 获取客户端的IP
		String ip = address.getHostAddress();
		// 获取客户端的端口
		int port = clientSocket.getPort();
		System.out.println(ip + ":" + port + " 客户端链接了");
	}

	@Override
	public void run() {
		try {
			/*
			 * 通过刚刚连接上的客户端的clientSocket获取输入流，
			 * 	来读取客户端发送过来的信息。
			 */
			InputStream in = clientSocket.getInputStream();
			/*
			 * 将字节流转换成字符流并指定编码集
			 */
			InputStreamReader isr = new InputStreamReader(in, "utf-8");
			/*
			 * 将字符流转换成缓冲流，这样就可以以行为单位读取字符串
			 */
			BufferedReader br = new BufferedReader(isr);
			String message = null;
			while ((message = br.readLine()) != null) {
				System.out.println(address.getHostAddress()+":"+clientSocket.getPort() + " 说：" + message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				clientSocket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}