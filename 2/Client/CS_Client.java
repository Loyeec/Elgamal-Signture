package Client;
import java.net.*;
import java.io.*;
import java.util.Scanner;
public class CS_Client 
{
	public static void main(String[] args) 
	{
		try{
		Socket sk =new Socket("127.0.0.1",38380);
		System.out.println("客户端已经开启----");
		PrintStream ps = new PrintStream(sk.getOutputStream());//将客户端套接字的输出流用printStream包装起来，类似于C语言中的fprintf类型转换
		System.out.print("请输入需要发送到服务器的内容：");
		Scanner sn = new Scanner(System.in);
		String str = sn.nextLine();
		ps.println(str);//把控制台输入的内容送入被printstream类包装的输出流里面	
		ps.close();//关闭输出流包装
		sk.close();//关闭socket套接字，已经传完数据，才能关闭
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
