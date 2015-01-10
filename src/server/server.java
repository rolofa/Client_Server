package server;

import java.io.*;
import java.net.*;
import java.util.Enumeration;
import java.util.Scanner;
public class server
{
	private static ServerSocket serverSocket;
	private static final int PORT = 1414;
	public static void main(String[] args)
			throws IOException
			{
		try
		{
			serverSocket = new ServerSocket(PORT);
			System.out.println("MULTI CLIENT SERVER pada PORT:" + PORT);
		}
		catch (IOException ioEx)
		{
			System.out.println("\nTidak Dapat Membuka Port!");
			System.exit(1);
		}
		do
		{
			//Tunggu Koneksi
			System.out.println("Server Siap...");
			Socket client = serverSocket.accept();
			InetAddress ip = client.getInetAddress();
			System.out.println("\nClient Baru masuk..\n"
							+"Dengan IP : "+ip.toString()+ 
							"\nNama : "+ip.getHostName().toString()+"\n");
			//Create a thread to handle communication with
			//this client and pass the constructor for this
			//thread a reference to the relevant socket...
			ClientHandler handler =
					new ClientHandler(client);
			handler.start();//As usual, method calls run .
		}while (true);
			}
}
class ClientHandler extends Thread
{
	private Socket client;
	private Scanner input;
	private PrintWriter output;
	public ClientHandler(Socket socket)
	{
		//Set up reference to associated socket...
		client = socket;
		try
		{
			input = new Scanner(client.getInputStream());
			output = new PrintWriter(
					client.getOutputStream(),true);
		}
		catch(IOException ioEx){
			ioEx.printStackTrace();
		}
	}
	public void run()
	{
		String received;
		do
		{
			//menerima pesan dari client
			//melalui socket input stream..
			received = input.nextLine();
			//Menampilkan kembali pesan ke client
			//melalui socket output stream...
			output.println("ECHO : " + received);
			System.out.println("Pesan Dari client : "+received);
			//ulangi sampai QUIT
		}while (!received.equalsIgnoreCase("QUIT"));
		try
		{
			if (client!=null)
			{
				System.out.println(
						"Menutup koneksi...sukses..");
				client.close();
			}
		}
		catch(IOException ioEx)
		{
			System.out.println("Gagal menutup koneksi!");
		}
	}
}