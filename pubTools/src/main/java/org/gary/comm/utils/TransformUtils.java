package org.gary.comm.utils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

/**
 * 
 * <strong>功能:</strong>类型转换
 * <strong>作者：</strong>Gary Huang
 * <strong>日期:</strong> 2014-3-5 
 * <strong>版权：<strong>版权所有(C) 2014，QQ 
 */ 
@SuppressWarnings({"unchecked" , "rawtypes"})
public class TransformUtils {

	public static boolean toBoolean(Object obj) {

		return toBoolean(obj, false);
	}

	public static boolean toBoolean(Object obj, boolean defaultValue) {
		if (obj == null) {
			return defaultValue;
		}
		try {
			return Boolean.parseBoolean(toString(obj));
		} catch (Exception e) {
		}
		return defaultValue ;
	}

	public static byte toByte(Object obj) {
		return toByte(obj, (byte) 0);
	}

	public static byte toByte(Object obj, byte defaultValue) {
		if (obj == null) {
			return defaultValue;
		}

		if (obj instanceof Number) {
			Number number = (Number) obj;
			return number.byteValue();
		}
		String value = toString(obj) ;
		try {
			return Byte.parseByte( value ) ;
		} catch (Exception e) {
		}
		return defaultValue ;
	}

	public static char toCharacter(Object obj) {
		return toCharacter(obj, ' ');
	}

	public static char toCharacter(Object obj, char defaultValue) {
		if (obj == null) {
			return defaultValue;
		}
		String str = obj.toString().trim();
		if (str.length() == 0) {
			return defaultValue;
		}
		return str.toCharArray()[0];
	}

	public static double toDouble(Object obj) {
		return toDouble(obj, 0d);
	}

	public static double toDouble(Object obj, double defaultValue) {
		if (obj == null) {
			return defaultValue;
		}

		if (obj instanceof Number) {
			Number number = (Number) obj;
			return number.doubleValue() ;
		}
		String value = toString(obj) ;
		try {
			return Double.parseDouble(value) ;
		} catch (Exception e) {
		}
		return defaultValue ;
	}

	public static float toFloat(Object obj) {
		return toFloat(obj, 0);
	}

	public static float toFloat(Object obj, float defaultValue) {
		if (obj == null) {
			return defaultValue;
		}

		if (obj instanceof Number) {
			Number number = (Number) obj;
			return number.floatValue();
		}
		String value = toString(obj) ;
		try {
			return Float.parseFloat(value) ;
		} catch (Exception e) {
		}
		return defaultValue ;
	}

	public static int toInt(Object obj) {
		return toInt(obj, 0);
	}

	public static int toInt(Object obj, int defaultValue) {
		if (obj == null) {
			return defaultValue;
		}

		if (obj instanceof Number) {
			Number number = (Number) obj;
			return number.intValue();
		}
		String value = toString(obj) ;
		try {
			return Integer.parseInt(value) ;
		} catch (Exception e) {
		}
		return defaultValue ;
	}

	public static long toLong(Object obj) {
		return toLong(obj, 0L);
	}

	public static long toLong(Object obj, long defaultValue) {
		if (obj == null) {
			return defaultValue;
		}

		if (obj instanceof Number) {
			Number number = (Number) obj;
			return number.longValue();
		}
		String value = toString(obj) ;
		try {
			return Long.parseLong(value) ;
		} catch (Exception e) {
		}
		return defaultValue ;
	}

	public static short toShort(Object obj) {
		return toShort(obj, (byte) 0);
	}
	
	
	public static short toShort(Object obj, short defaultValue) {
		if (obj == null) {
			return defaultValue;
		}

		if (obj instanceof Number) {
			Number number = (Number) obj;
			return number.shortValue();
		}
		String value = toString(obj) ;
		try {
			return Short.parseShort(value) ;
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static String toString(Object value) {
		if (value == null) {
			return "";
		}
		if ("null".equals(value)) {
			return "";
		}
		
		if(value instanceof Number){
			BigDecimal bigDecimal = toBigDecimal(value) ;
			return bigDecimal.toString() ;
		}
		
		return value.toString().trim();
	}
	public static String toString(Object value,String defaultValues) {
		if (value == null) {
			return defaultValues;
		}
		
		if(value instanceof Number){
			BigDecimal bigDecimal = toBigDecimal(value) ;
			return bigDecimal.toString() ;
		}
		
		return value.toString().trim();
	}
	public static BigDecimal toBigDecimal(Object value){
		return toBigDecimal(value , new BigDecimal(0)) ;
	}
	
	public static BigDecimal toBigDecimal(Object value, BigDecimal defaultValue) {
		if(value == null){
			return defaultValue ;
		}
		if(value instanceof BigDecimal){
			BigDecimal decimal = (BigDecimal) value ;
			return decimal; 
		}
		return new BigDecimal( toDouble( value ) );
	}

	public static String dateToString(Object value, String pattern){
		
		if(null == value){
			return "" ; 
		}
		
		java.util.Date date = (java.util.Date) value;  
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime( date ) ; 
        SimpleDateFormat sdf = new SimpleDateFormat( pattern );  
        return sdf.format( date ) ; 
	}
	
	public static Object transform(Object obj,Class<?> clazz){
		if(clazz == null){
			return obj ;  
		}
		
		if(clazz.isEnum()){
			Field[]fields=clazz.getFields() ;
			int tempInt = toInt(obj) ;
			if(fields.length > tempInt){
				try {
					return Enum.valueOf((Class)clazz , fields[tempInt].getName());
				} catch (Exception e) {
				}
			}
		}
		
		if(obj == null){
			return null ;
		}
		
		if(obj.getClass().equals(clazz)){
			return obj ; 
		}
		if(int.class.equals(clazz) || Integer.class.equals(clazz)){
			return toInt(obj) ; 
		}else if( String.class.equals(clazz) ){
			return toString( obj ) ; 
		}else if(float.class.equals(clazz) || Float.class.equals(clazz)){
			return toFloat(obj) ; 
		}else if(double.class.equals(clazz) || Double.class.equals(clazz)){
			return toDouble(obj) ; 
		}else if(byte.class.equals(clazz) || Byte.class.equals(clazz)){
			return toByte(obj) ; 
		}else if(char.class.equals(clazz) || Character.class.equals(clazz)){
			return toCharacter(obj) ; 
		}else if(short.class.equals(clazz) || Short.class.equals(clazz)){
			return toShort(obj) ; 
		}else if(long.class.equals(clazz) || Long.class.equals(clazz)){
			return toLong(obj) ; 
		}else if(boolean.class.equals(clazz) || Boolean.class.equals(clazz)){
			return toBoolean(obj) ; 
		}else if(BigDecimal.class.equals(clazz)){
			return toBigDecimal( obj ) ; 
		}else if(java.util.Date.class.equals(clazz) || 
				java.sql.Date.class.equals(clazz) 
				|| java.sql.Timestamp.class.equals(clazz)){
			try {
				java.util.Date date = DateUtils.parseDate(obj.toString() 
						, "yyyy-MM-dd" , "yyyyMMdd" , 
						"yyyy年MM月dd日" , 
						 "yyyyMMddHHmmss" , 
						 "yyyy-MM-dd HH:mm:ss" , 
						 "yyyy/MM/dd HH:mm:ss" , 
						 "yyyy年MM月dd日HH时mm分ss秒" ) ;  
				if(java.sql.Date.class.equals(clazz)){
					return new java.sql.Date(date.getTime()) ;  
				}else if(java.sql.Timestamp.class.equals(clazz)){
					return new java.sql.Timestamp(date.getTime()) ;
				}
				return date ; 
			} catch (Exception e) {
				return null;
			}
		}
		return obj ; 
	}
	
	
	public static String objToString( Object value ){
		if(Helper.isNull( value )){
			return "" ; 
		}
		if(value instanceof BigDecimal || value instanceof Double ||
				value instanceof Float){
			BigDecimal decimal = toBigDecimal( value ) ;
			DecimalFormat df = new DecimalFormat("0.00");
			return df.format( decimal ) ;
		}else if(value instanceof Date){
			Date date = (Date) value ;
			Calendar calendar = Calendar.getInstance() ;
			calendar.setTime( date ) ; 
			String pattern = "yyyy-MM-dd HH:mm:ss" ;
			if(calendar.get(Calendar.HOUR_OF_DAY) == 0 
					 && calendar.get(Calendar.MINUTE) == 0
					  && calendar.get(Calendar.SECOND) == 0){
				pattern = "yyyy-MM-dd" ; 
			}
			return DateFormatUtils.format(calendar , pattern ) ;
		}
		return value.toString() ; 
	}
	
	public static Double doubleFormat(Object number,Integer b){
		String fm = "0.";
		if(TransformUtils.toInt(b)==0 || b == null){
			fm = fm + "00";
		}else{
			for(int i=0;i<b;i++){
				fm = fm + "0";
			}
		}
		DecimalFormat df = new DecimalFormat(fm);
		return TransformUtils.toDouble(df.format(TransformUtils.toDouble(number)));
	}
	
	public static Double toDouble2(Object number){
		return doubleFormat(number,2);
	}
	
}

