package org.gary.comm.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

import javax.crypto.Cipher;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.gary.logs.LogManager;
 
@SuppressWarnings({"unchecked" , "restriction"})
public class Helper {

	public static final String YYYY_MM_DD_HH_MM_SS_WORD = "yyyy年MM月dd日 HH时mm分ss秒";
	public static final String YYYY_MM_DD_WORD = "yyyy年MM月dd日";
	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static final String YYYY_MM_DD = "yyyy-MM-dd";

	public static int getArrayLength(Object obj) {
		if (Helper.isNull(obj)) {
			return 0;
		}
		if (obj.getClass().isArray()) {
			return Array.getLength(obj);
		}
		if (obj instanceof Collection<?>) {
			Collection<?> coll = (Collection<?>) obj;
			return coll.size();
		}
		return 0;
	}
	
	public static Object getArray(Object obj, int index) {
		if (Helper.isNull(obj)) {
			return null;
		}
		if (obj.getClass().isArray()) {

			if (getArrayLength(obj) <= index) {
				return null;
			}

			return Array.get(obj, index);
		}
		if (obj instanceof Collection<?>) {
			Collection<?> coll = (Collection<?>) obj;
			int tempIndex = 0;
			for (Object tempObj : coll) {
				if (tempIndex == index) {
					return tempObj;
				}
				tempIndex++;
			}
		}
		return null;
	}
	
	public static String toCommaStrong(Object value){
		StringBuffer buffer = new StringBuffer();
		if(value == null){
			return "" ;
		}
		if(value.getClass().isArray()){
			int length = Array.getLength(value) ;
			for(int x=0;x<length;x++){
				if(x != 0){
					buffer.append(",") ;
				}
				buffer.append( Array.get(value , x ) ) ;
			}
		} else if(value instanceof Collection<?>){
			Collection<?> coll = (Collection<?>)value ; 
			int index = 0 ;
			for(Object ob : coll){
				if(index != 0){
					buffer.append(",") ;
				}
				index ++ ;
				buffer.append( ob ) ; 
			}
		}
		
		return buffer.toString() ;
	}
	
	public static String numberFormat(Object ob,int...lengths){
		Double d = TransformUtils.toDouble( ob);
		int length = lengths.length > 0 ? lengths[0] : 2 ;
		String f = "0.";
		while(length-->0){
			f += "0" ;
		}
		DecimalFormat df = new DecimalFormat(f);
		return df.format( d ) ;
	}
	
	
	public static List<Object> toList(List<?> maps, String keyName) {
		List<Object> list = new ArrayList<Object>();
		if (isNull(maps)) {
			return null;
		}
		try {
			for (Object vo : maps) {
				if (vo instanceof Map<?, ?>) {
					Map<?, ?> map = (Map<?, ?>) vo;
					list.add(map.get(keyName));
				} else {
					list.add(FieldUtils.readDeclaredField(vo, keyName, true));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static <T> T[] toDistinctArray(List<?> maps , String keyName , Class<T> targetClass){
		if (isNull(maps)) {
			return null;
		}
		T[] ts = null;
		try {
			for (Object vo : maps) {
				T t = null ;
				if (vo instanceof Map<?, ?>) {
					Map<?, ?> map = (Map<?, ?>) vo;
					t = (T)TransformUtils.transform(map.get(keyName),
							targetClass )  ;
				} else { 
					t =  (T) TransformUtils.transform(FieldUtils
							.readDeclaredField(vo, keyName, true), targetClass) ; 
				}
				if(null == t){
					continue ;
				}
				if( null != ts){
					boolean isHave = false ;
					for(T tmpT:ts){
						if(tmpT.equals(t)){
							isHave=true;
							break;
						}
					} 
					if(!isHave){
						ts=ArrayUtils.add(ts, t);
					}
				}else{
					ts=ArrayUtils.add(ts, t);
				}
			}
		} catch (Exception e) {
		}
		return ts ; 
	}
	
	public static <T> T[] toArray(List<?> maps, String keyName,
			Class<T> targetClass) {
		if (isNull(maps)) {
			return null;
		}
		T[] ts = null;
		try {
			int size = maps.size();
			ts = (T[]) Array.newInstance(targetClass, size);
			int index = 0;
			for (Object vo : maps) {
				if (vo instanceof Map<?, ?>) {
					Map<?, ?> map = (Map<?, ?>) vo;
					ts[index] = (T) TransformUtils.transform(map.get(keyName),
							targetClass);
				} else {
					ts[index] = (T) TransformUtils.transform(FieldUtils
							.readDeclaredField(vo, keyName, true), targetClass);
				}
				index++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ts;
	}
	

	
	public static <T> T[] asArrayClass(String[]source,
			Class<T> targetClass) {
		if (isNull(source)) {
			return null ; 
		}
		T[] ts = null;
		try {
			int size = source.length;
			ts = (T[]) Array.newInstance(targetClass, size);
			int index = 0;
			for (String vo : source){
				ts[index] = (T) TransformUtils.transform( vo , targetClass);
				index++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ts;
	}

	/**
	 * get Target Field 如果该对象没有该属性 就去器父类找
	 */
	public static Field getTargetField(Class<?> targetClass, String fieldName) {
		Field field = null;

		try {
			if (targetClass == null) {
				return field;
			}

			if (Object.class.equals(targetClass)) {
				return field;
			}

			field = FieldUtils.getDeclaredField(targetClass, fieldName, true);
			if (field == null) {
				field = getTargetField(targetClass.getSuperclass(), fieldName);
			}
		} catch (Exception e) {
		}

		return field;
	}


	/**
	 * 判断String类型是否为空
	 */
	public static boolean isNull(String value) {
		if (value == null) {
			return true;
		}
		if (value.trim().length() == 0) {
			return true;
		}
		return false;
	}

	public static boolean isNull(Object value) {
		if (value == null) {
			return true;
		}
		if (value.getClass().isArray()) {
			if (Array.getLength(value) == 0) {
				return true;
			}
		}
		if (value instanceof Collection<?>) {
			Collection<?> collection = (Collection<?>) value;
			if (collection.isEmpty()) {
				return true;
			}
		} else if (value instanceof Map<?, ?>) {
			Map<?, ?> map = (Map<?, ?>) value;
			if (map.isEmpty()) {
				return true;
			}
		} else if (value instanceof String) {
			String string = (String) value;
			return isNull(string);
		}
		return false;
	}

	public static String checkNull(Object val) {
		if (val == null) {
			return "";
		}
		return val.toString().trim();
	}

	public static int toInt(Object value) {
		if (value == null) {
			return 0;
		}
		if (value instanceof Number) {
			Number number = (Number) value;
			return number.intValue();
		}
		return new Double(NumberUtils.toDouble(value.toString().trim()))
				.intValue();
	}

	public static String format(int num, int length) {
		StringBuffer buffer = new StringBuffer(Integer.toString(num));
		while (buffer.length() < length) {
			buffer.insert(0, "0");
		}
		return buffer.toString();
	}

	public static float toFloat(Object value) {
		if (value == null) {
			return 0;
		}
		if (value instanceof Number) {
			Number number = (Number) value;
			return number.floatValue();
		}
		return NumberUtils.toFloat(value.toString().trim());
	}

	public static double toDouble(Object value) {
		if (value == null) {
			return 0;
		}
		if (value instanceof Number) {
			Number number = (Number) value;
			return number.doubleValue();
		}
		return NumberUtils.toDouble(value.toString().trim());
	}


	
	
	public static String getValue(Object o, String field) {
		if (o == null)
			return null;
		Object value = null;
		if (o instanceof Map<?, ?>) {
			Map<?, ?> map = (Map<?, ?>) o;
			value = map.get(field);
		} else {
			try {
				value = FieldUtils.readField(Helper.getTargetField(
						o.getClass(), field), o, true);
			} catch (Exception e) {
			}
		}
		return value != null ? value.toString() : "";
	}

	public static Map<?, ?> toMap(List<?> data, String key, String value) {
		Map<String, Object> result = new KeyValue<Object>();
		if (Helper.isNull(data)) {
			return result;
		}
		for (Object obj : data) {
			result.put(getValue(obj, key), getValue(obj, value));
		}
		return result;
	}
	
	public static String replaceBRbyLine(String str) {
		return StringUtils.replace(str, "<br/>", "\n");
	}

	public static java.sql.Timestamp geTimestamp() {
		return new java.sql.Timestamp(new Date().getTime());
	}


	/**
	 * 车牌专用
	 * 
	 * @param licensecodes
	 * @return
	 */
	public static String handlerLicensecodes(String licensecodes) {
		Set<String> set = new HashSet<String>();
		String s1[] = StringUtils.split(licensecodes, ",");
		for (String s : s1) {
			if (!StringUtils.isEmpty(s)) {
				set.add(s);
			}
		}
		StringBuffer sb = new StringBuffer();
		int i = 0;
		for (String s : set) {
			if (i == set.size() - 1) {
				sb.append(s + "");
			} else {
				sb.append(s + ",");
			}
		}
		return sb.toString();
	}

	

	public static String generateScript(String js) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<script>");
		buffer.append(js);
		buffer.append("</script>");
		return buffer.toString();
	}

	public static String generateAlert(String msg, String link) {
		StringBuffer buffer = new StringBuffer();
		if(!Helper.isNull(msg)){
			buffer.append(String.format("alert('%s');", msg));
		}
		if(!Helper.isNull(link)){
			buffer.append(String.format("window.location='%s';", link));
		}
		return generateScript( buffer.toString() ) ;
	}
	

	public static String[] splitProject(String text) {
		int index = text.indexOf("〓");
		if (index == -1) {
			return null;
		}
		String[] splits = new String[3];
		splits[0] = text.substring(0, index);

		splits[1] = text.substring(index + 1);
		index = splits[1].indexOf("〓");
		if (index == -1) {
			return null;
		}
		String str = splits[1];
		splits[1] = str.substring(0, index);
		splits[2] = str.substring(index + 1);
		return splits;
	}

	public static String toChinese(String value) {
		if (value == null) {
			return "";
		}
		try {
			return new String(value.getBytes("ISO8859-1"), "UTF-8");
		} catch (Exception e) {

		}
		return "";
	}

	
	protected static DecimalFormat df = new DecimalFormat("0.00");

	public static String formatMoney(Object obj) {
		double d = TransformUtils.toDouble(obj);
		return df.format(d);
	}

	public static <T> T[] toTs(String[] values, Class<T> cls) {
		if (isNull(values)) {
			return null;
		}
		T[] ts = null;
		try {
			ts = (T[]) Array.newInstance(cls, values.length);

			for (int x = 0; x < values.length; x++) {
				ts[x] = (T) TransformUtils.transform(values[x], cls);
			}
		} catch (Exception e) {
			LogManager.err("Helper.ts", e);
		}
		return ts;
	}

	

	public static String mergeString(Object obj) {
		if (null == obj) {
			return "";
		}
		StringBuffer buffer = new StringBuffer();
		if (obj.getClass().isArray()) {
			int length = Array.getLength(obj);
			for (int x = 0; x < length; x++) {
				Object item = Array.get(obj, x);
				if (x > 0) {
					buffer.append(",");
				}
				if (item instanceof String) {
					buffer.append(String.format("'%s'", TransformUtils
							.toString(item)));
				} else {
					buffer.append(item);
				}
			}
		} else if (obj instanceof Collection<?>) {
			Collection<?> coll = (Collection<?>) obj;
			int index = 0;
			for (Iterator<?> iterator = coll.iterator(); iterator.hasNext();) {
				Object item = iterator.next();
				if (index > 0) {
					buffer.append(",");
				}
				if (item instanceof String) {
					buffer.append(String.format("'%s'", TransformUtils
							.toString(item)));
				} else {
					buffer.append(item);
				}
				index++;
			}
		}
		return buffer.toString();
	}
	
	
	public static String mergeString(Object obj , String pattern) {
		if (null == obj) {
			return "";
		} 
		StringBuffer buffer = new StringBuffer();
		if (obj.getClass().isArray()) {
			int length = Array.getLength(obj);
			for (int x = 0; x < length; x++) {
				Object item = Array.get(obj, x);
				if (x > 0) {
					buffer.append( pattern );
				}
				buffer.append( TransformUtils.toString(item) ) ;
			}
		} else if (obj instanceof Collection<?>) {
			Collection<?> coll = (Collection<?>) obj;
			int index = 0;
			for (Iterator<?> iterator = coll.iterator(); iterator.hasNext();) {
				Object item = iterator.next();
				if (index > 0) {
					buffer.append( pattern );
				}
				buffer.append( TransformUtils.toString(item) ) ;
				index++;
			}
		}
		return buffer.toString();
	}
	
	public static String replaceStr(Object obj){
		return StringEscapeUtils.escapeSql(TransformUtils.toString(obj)); 
	}

	public static String getRandomStr() {
		List<String> strList = new Vector<String>();
		/*
		 for (int x = 65; x <= 90; x++) {
			strList.add(String.valueOf((char) x));
		} 
		 * */
		for (int x = 0; x < 10; x++) {
			strList.add(String.valueOf(x));
		}
		Random random = new Random();
		String sRand = "";
		for (int i = 0; i < 6 ; i++) {
			String rand = strList.remove(random.nextInt(strList.size() - 1));
			sRand += rand;
		}
		return sRand;
	}
	
	public static String getCurrentDate(){
		Date date = new Date();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format( date );
	}

	
	public static String formatDoubel(double d,int count){
		String f="0.";
		for(int i=0;i<count;i++)
			f=f+"0";
		DecimalFormat df = new DecimalFormat(f);
		return df.format(d);
	}
	
	
	
	
	public static int randNext(int bigInt){
		return new SecureRandom().nextInt(bigInt) ;
	}
	
	public static int randNext(){
		return new SecureRandom().nextInt() ;
	}
	
	
	/***
	 * 加密方法
	 * */
	public static String desEncode(String strKey, String strIn) {
		try {
			byte[]encodeStrByte = strIn.getBytes();
			Security.addProvider(new com.sun.crypto.provider.SunJCE());
			byte[]keyByte = strKey.getBytes();
			byte[] arrB = new byte[8] ; 
			for (int i = 0; i < keyByte.length && i < arrB.length; i++) {
				arrB[i] = keyByte[i];
			}
			Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");
			Cipher encryptCipher = Cipher.getInstance("DES");
			encryptCipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] afterByter = encryptCipher.doFinal( encodeStrByte ) ;
			int iLen = afterByter.length;
			// 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
			StringBuffer sb = new StringBuffer(iLen * 2) ;  
			for (int i = 0; i < iLen; i++) {
				int intTmp = afterByter[i];
				// 把负数转换为正数
				while (intTmp < 0) {
					intTmp = intTmp + 256;
				}
				// 小于0F的数需要在前面补0
				if (intTmp < 16) {
					sb.append("0");
				}
				sb.append(Integer.toString(intTmp, 16));
			}
			return sb.toString(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/***
	 * 加密方法
	 * */
	public static String desEncode( String strIn) {
		return desEncode( "national" , strIn) ;
	}
	/***
	 * 解密
	 * */
	public static String desDecode(String strKey, String strIn) {
		try {
			byte[]encodeStrByte = strIn.getBytes();
			Security.addProvider(new com.sun.crypto.provider.SunJCE());
			byte[]keyByte = strKey.getBytes();
			byte[] arrB = new byte[8] ;
			for (int i = 0; i < keyByte.length && i < arrB.length; i++) {
				arrB[i] = keyByte[i];
			}
			Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");
			Cipher decryptCipher = Cipher.getInstance("DES");
			decryptCipher.init(Cipher.DECRYPT_MODE, key);
			int iLen = encodeStrByte.length;
			byte[] arrOut = new byte[iLen / 2];
			for (int i = 0; i < iLen; i = i + 2) {
				String strTmp = new String(encodeStrByte, i, 2);
				arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
			}
			byte[] doFinal = decryptCipher.doFinal(arrOut) ; 
			return new String( doFinal );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/***
	 * 解密
	 * */
	public static String desDecode(String strIn) {
		return desDecode( "national" , strIn);
	}
	
	public static Map<String,String> parseToMap(String parseStr){
		String[]parseStrs = parseStr.split(",");
		KeyValue<String> values = new KeyValue<String>();
		if(!Helper.isNull(parseStrs)){
			for(String str:parseStrs){
				String[] split = str.split("=");
				values.put(split[0], split[1]);
			}
		}
		return values;
	}
}
