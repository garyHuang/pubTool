package org.gary.io.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.gary.logs.LogManager;

/**
 * * * *
 */
public class StreamUtils {

	final static int BUFFER_SIZE = 4096;

	/**
	 * 将InputStream转换成String
	 * 
	 * @param in
	 *            InputStream
	 * @return String
	 * @throws Exception
	 * 
	 */
	public static String inputStreamTOString(InputStream in) {

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[BUFFER_SIZE];
		String string = null;
		int count = 0;
		try {
			while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
				outStream.write(data, 0, count);
		} catch (IOException e) {
			e.printStackTrace();
		}

		data = null;
		try {
			string = new String(outStream.toByteArray(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LogManager.err("inputStreamTOString," , e);
		}finally{
			closeInput(in);
			closeOutput(outStream);
		}
		return string;
	}

	/**
	 * 将InputStream转换成某种字符编码的String
	 * 
	 * @param in
	 * @param encoding
	 * @return
	 * @throws Exception
	 */
	public static String inputStreamTOString(InputStream in, String encoding) {
		String string = null;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[BUFFER_SIZE];
		int count = -1;
		try {
			while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
				outStream.write(data, 0, count);
		} catch (IOException e) {
			LogManager.err("inputStreamTOString," , e);
		}finally{
			closeInput(in);
			closeOutput(outStream);
		}

		data = null;
		try {
			string = new String(outStream.toByteArray(), encoding);
		} catch (UnsupportedEncodingException e) {
			LogManager.err("inputStreamTOString," , e);
		}
		return string;
	}

	public static void closeInput(InputStream in){
		if(in != null){
			try {
				in.close();
			} catch (Exception e) {
			}
		}
	}
	
	public	static void closeOutput(OutputStream out){
		if(out!=null){
			try {
				out.close();
			} catch (Exception e) {
			}
		}
	}
	/**
	 * 将String转换成InputStream
	 * 
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static InputStream stringTOInputStream(String in) throws Exception {
		ByteArrayInputStream is = new ByteArrayInputStream(in.getBytes("UTF-8"));
		return is;
	}

	/**
	 * 将String转换成InputStream
	 * 
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static byte[] StringTObyte(String in) {
		byte[] bytes = null;
		try {
			bytes = inputStreamTOByte(stringTOInputStream(in));
		}catch (Exception e) {
		}
		
		return bytes;
	}

	/**
	 * 将InputStream转换成byte数组
	 * 
	 * @param in
	 *            InputStream
	 * @return byte[]
	 * @throws IOException
	 */
	public static byte[] inputStreamTOByte(InputStream in) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		try {
			byte[] data = new byte[BUFFER_SIZE];
			int count = -1;
			while ((count = in.read(data, 0, BUFFER_SIZE)) != -1){
				outStream.write(data, 0, count);
			}

			data = null;
			return outStream.toByteArray();
		}finally{
			closeInput(in);
			closeOutput( outStream );
		}
	}

	/**
	 * 将byte数组转换成InputStream
	 * 
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static InputStream byteTOInputStream(byte[] in) throws Exception {
		ByteArrayInputStream is = new ByteArrayInputStream(in);
		return is;
	}

	/**
	 * 将byte数组转换成String
	 * 
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static String byteTOString(byte[] in) {

		InputStream is = null;
		try {
			is = byteTOInputStream(in);
		} catch (Exception e) {
			LogManager.err("byteTOString", e);
		}
		return inputStreamTOString(is, "UTF-8");
	}

	/**
	 * 将byte数组转换成String
	 * 
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static String getString(String in) {

		String is = null;
		try {
			is = byteTOString(StringTObyte(in));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return is;
	}

	// InputStream 转换成byte[]
	public byte[] getBytes(InputStream is) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			byte[] b = new byte[BUFFER_SIZE];
			int len = 0;

			while ((len = is.read(b, 0, BUFFER_SIZE)) != -1) {
				baos.write(b, 0, len);
			}

			baos.flush();

			byte[] bytes = baos.toByteArray();

			return bytes;
		}finally{
			closeInput(is);
			closeOutput( baos );
		}
	}

	/**
	 * 根据文件路径创建文件输入流处理 以字节为单位（非 unicode ）
	 * 
	 * @param path
	 * @return
	 */
	public static FileInputStream getFileInputStream(String filepath) {
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(filepath);
		} catch (FileNotFoundException e) {
			System.out.print("错误信息:文件不存在");
			e.printStackTrace();
		}
		return fileInputStream;
	}

	/**
	 * 根据文件对象创建文件输入流处理 以字节为单位（非 unicode ）
	 * 
	 * @param path
	 * @return
	 */
	public static FileInputStream getFileInputStream(File file) {
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			System.out.print("错误信息:文件不存在");
			e.printStackTrace();
		}
		return fileInputStream;
	}

	/**
	 * 根据文件对象创建文件输出流处理 以字节为单位（非 unicode ）
	 * 
	 * @param file
	 * @param append
	 *            true:文件以追加方式打开,false:则覆盖原文件的内容
	 * @return
	 */
	public static FileOutputStream getFileOutputStream(File file, boolean append) {
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(file, append);
		} catch (FileNotFoundException e) {  
			e.printStackTrace();
		}
		return fileOutputStream;
	}

	/**
	 * 根据文件路径创建文件输出流处理 以字节为单位（非 unicode ）
	 * 
	 * @param path
	 * @param append
	 *            true:文件以追加方式打开,false:则覆盖原文件的内容
	 * @return
	 */
	public static FileOutputStream getFileOutputStream(String filepath,
			boolean append) {
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(filepath, append);
		} catch (FileNotFoundException e) {
			System.out.print("错误信息:文件不存在");
			e.printStackTrace();
		}
		return fileOutputStream;
	}

	public static File getFile(String filepath) {
		return new File(filepath);
	}

	public static ByteArrayOutputStream getByteArrayOutputStream() {
		return new ByteArrayOutputStream();
	}
	
	
	public static boolean writeFile(InputStream is , String path) throws Exception{
		FileOutputStream fos =  null ; 
		try {
			byte[]bytes = inputStreamTOByte( is ) ;
			fos = new FileOutputStream(path);
			fos.write( bytes ) ;
			fos.close() ; 
			return true ; 
		}finally{
			closeInput(is);
			closeOutput(fos);
		}
	}
}