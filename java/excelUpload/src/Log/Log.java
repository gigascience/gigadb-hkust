/**
 * 
 */
package Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @author 王森洪
 *
 * @date 2012-3-8
 */
public class Log {
	public String outputFileName;
	public BufferedWriter outFile ;
	public OutputStream outStream;
	public PrintWriter printWriter;
	/**
	 * 
	 */
	public Log() {
		// TODO Auto-generated constructor stub
	}
	//写错误日志到文件里
	public Log(String fileName,Boolean append){
		outputFileName=fileName;
		try{
		outStream = new FileOutputStream(outputFileName, append); // true: append
		printWriter = new PrintWriter(outStream); 
//		printWriter.append("sdfa");
//		printWriter.flush();
	    OutputStreamWriter outStreamWriter = new OutputStreamWriter(outStream, "utf8");
	    outFile = new BufferedWriter(outStreamWriter);   
		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
  
	}
	public void  write(LinkedList<String> list) throws IOException{
		for(String s:list)
		   writeLine(s, outFile);
  }
	public void  write(ArrayList<String> list) throws IOException{
		for(String s:list)
		   writeLine(s, outFile);
  }
	 public   void writeLine(String line, BufferedWriter writer) throws IOException
	  {
	    writer.write(line, 0, line.length());
	    writer.newLine();
	    writer.flush();
	  }
	 public void writeLine() throws IOException{
		 outFile.newLine();
	 }
	 public void writeLine(String line) throws IOException{
		 writeLine(line, outFile);
	 }
	/**
	 * @param args
	 */
	// 复制文件 
	 public static void copyFile(File sourceFile,File targetFile)  
	 throws IOException{ 
	         // 新建文件输入流并对它进行缓冲 
	         FileInputStream input = new FileInputStream(sourceFile); 
	         BufferedInputStream inBuff=new BufferedInputStream(input); 
	  
	         // 新建文件输出流并对它进行缓冲 
	         FileOutputStream output = new FileOutputStream(targetFile); 
	         BufferedOutputStream outBuff=new BufferedOutputStream(output); 
	         
	         // 缓冲数组 
	         byte[] b = new byte[1024 * 5]; 
	         int len; 
	         while ((len =inBuff.read(b)) != -1) { 
	             outBuff.write(b, 0, len); 
	         } 
	         // 刷新此缓冲的输出流 
	         outBuff.flush(); 
	          
	         //关闭流 
	         inBuff.close(); 
	         outBuff.close(); 
	         output.close(); 
	         input.close(); 
	     } 
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
