package com.dotool.util;


import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.lang.reflect.Array;

/**
 *	rem: ���ù���
 *  aut: ��ȸ��
 */

public class PublicTools
{

    /**
     * ���ڸ�ʽ��yyyy-MM-dd
     */
    public static final String DATA_FORMAT = "yyyy-MM-dd";

    /**
     * ʱ���ʽ��yyyy-MM-dd HH:mm:ss
     */
    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";


    public static String DB_DIALECT = "mysql";
    

    /* fn-hd
	 * rem: �������ַ���ת����Ϊ����SQL��ʽ���ַ���
	 * rem: �� / ' " ǰ����ת����� /
	 * par: str -- Ҫת�����ַ���
	 * ret: ת����ϵ��ַ���
	 * sep:
	 * pub:
	 * exp:
	 * aut: ���
	 * log: 2004-08-25 ����
	 */
	public static String toSql(String str)
	/* fn-tl*/
	{
		if (str == null)
		{
			return null;
		}
		char c;
		int length = str.length();
        StringBuffer strBuffer = new StringBuffer(length*11/10+5);
		for(int i = 0; i < length; i++)
		{
			c = str.charAt(i);
			if (c == '\\' || c == '\'' || c == '\"')
			{
				strBuffer.append('\\');
			}
			strBuffer.append(str.charAt(i));
		}
		return strBuffer.toString();
	}

    public static String toRegular(String str)
        /* fn-tl*/
    {
        if (str == null)
        {
            return "";
        }
        char c;
        int length = str.length();
        StringBuffer strBuffer = new StringBuffer();
        for(int i = 0; i < length; i++)
        {
            c = str.charAt(i);
            if (c == '.' || c == '*' || c == '(' || c == ')' || c == '/')
            {
                strBuffer.append('\\');
            }
            strBuffer.append(str.charAt(i));
        }
        return strBuffer.toString();
    }

	/* fn-hd
	 * rem: �������ַ���ת����Ϊ������ҳ���html��ʽ���ַ���
	 * rem: �޸������ַ� "&", "<", ">" �������з� "\n"
	 * par: str -- Ҫת�����ַ���
	 * ret: ת����ϵ��ַ���
	 * sep:
	 * pub:
	 * exp:
	 * aut: ���
	 * log: 2004-08-25 ����
	 */
    public static String toHtml(String str)
	/* fn-tl*/
	{
        if (str == null)    return null;

        String html = new String(str);
        html = Replace(html, "&", "&amp;");
        html = Replace(html, "<", "&lt;");
        html = Replace(html, ">", "&gt;");
        html = Replace(html, "\n", "<br>"); //�����з�

        return html;
    }

    /**
     * ���ܣ��ַ����滻���� source �е� oldString ȫ������ newString
	 * ������@param source Դ�ַ���
     *       @param oldString �ϵ��ַ���
     *       @param newString �µ��ַ���
     * ����ֵ���滻����ַ���
     * �������ڣ�2004-7-12
     * ���ߣ��̻���
     * ����޸ģ�
     * �޸��ˣ�
     */
    public static String Replace(String source, String oldString, String newString)
	{
        StringBuffer output = new StringBuffer();

        if (source.equals("") || source == null) return "";
        int lengthOfSource = source.length();   // Դ�ַ�������
        int lengthOfOld = oldString.length();   // ���ַ�������

        int posStart = 0;   // ��ʼ����λ��
        int pos;            // ���������ַ�����λ��

        while ((pos = source.indexOf(oldString, posStart)) >= 0)
		{
            output.append(source.substring(posStart, pos));

            output.append(newString);
            posStart = pos + lengthOfOld;
        }

        if (posStart < lengthOfSource)
		{
            output.append(source.substring(posStart));
        }

        return output.toString();
    }
    
    
    /* fn-hd
	 * rem: ��encode��ת��Ϊ�����ַ�	 
	 * par: str -- Ҫת�����ַ���
	 * ret: ת����ϵ��ַ���
	 * sep:
	 * pub:
	 * exp:
	 * aut: ��ȸ��
	 * log: 2004-08-25 ����
	 */
    public static String Dencoder(String str)
	/* fn-tl*/
	{
    		
    		str = str.replaceAll("%23","#");
    		
    		//str = str.replaceAll("%25","%");
    		
    		str = str.replaceAll("%26","&");
    		
    		str = str.replaceAll("%2B","+");
    		
    		str = str.replaceAll("%2F","\\");
    		
    		str = str.replaceAll("%3D","=");
    		
    		str = str.replaceAll("%3F","?");
    		
    		return str;
    }
    
    
    /* fn-hd
	 * rem: �������ַ�ת��Ϊencode�� 
	 * par: str -- Ҫת�����ַ���
	 * ret: ת����ϵ��ַ���
	 * sep:
	 * pub:
	 * exp:
	 * aut: ��ȸ��
	 * log: 2004-09-28 ����
	 */
    public static String Encoder(String str)
	/* fn-tl*/
	{
    	
    		str = PublicTools.replaceAll(str,"#","%23");
    		
    		str = PublicTools.replaceAll(str,"&","%26");
    		
    		str = PublicTools.replaceAll(str,"+","%2B");
    		
    		str = PublicTools.replaceAll(str,"\\","%2F");
    		
    		str = PublicTools.replaceAll(str,"=","%3D");
    		
    		str = PublicTools.replaceAll(str,"?","%3F");	
    		return str;
    }
    
    
    /* fn-hd
	 * rem: ���������ַ�#+*�Ȳ�����String��replaceAll����.
	 * ȡ��String���replaceAll
	 * 
	 * par: newStr -- Ҫ�滻���ַ���
	 * ret: �滻��ϵ��ַ���
	 * sep:
	 * pub:
	 * exp:
	 * aut: ��ȸ��
	 * log: 2004-09-29 ����
	 */
    public static String replaceAll(String str,String oldStr,String newStr)
    {
    	int length = str.indexOf(oldStr);
    	int j=0;    	
    	String temp = "";
    	while( length != -1)
    	{
    		j++;
    		if(j ==10)
    			break;
    		temp = str.substring(length+1);    		
    		str = str.substring(0,length);
    		str = str + newStr + temp;     		
    		length = str.indexOf(oldStr); 
    	}
    	return str;
    }
    
    
    /**
     * 
     * @param str
     * @param f
     */
    public static void cat(String str,File f)
	{
		try
		{
			String filePath = f.getPath();
			int length = filePath.lastIndexOf("/");
			if(length != -1 )
			{
			    filePath = filePath.substring(0,length);
			    File tmpFile = new File(filePath);
			    if(!tmpFile.exists())
			    {
			        tmpFile.mkdirs();
			    }
			}
			if(!f.exists())
			{			   
			    
				f.createNewFile();
			}
			else
			{
			    f.delete();
			}
			
			
			
			ByteArrayInputStream bis = new ByteArrayInputStream(str
                    .getBytes());
			
			
			
			 DataInputStream dataInputStream = new DataInputStream(
	                    new BufferedInputStream(bis));
			
			FileOutputStream wf = new FileOutputStream(f);
			DataOutputStream dos = new DataOutputStream(wf);
			
			
			
			int onebyte;
            while ((onebyte = dataInputStream.read()) != -1)
            {
                dos.write(onebyte);
            }
			
			dos.close();
			wf.close();
			
		}
		catch(Exception ce)
		{
			ce.printStackTrace();
		}
	}


    public static void cat(String str,String dir , String file )
	{
		try
		{
            File f = new File(dir + "/" + file);

			    File tmpFile = new File(dir);
			    if(!tmpFile.exists())
			    {
			        tmpFile.mkdirs();
			    }

			if(!f.exists())
			{

				f.createNewFile();
			}
			else
			{
			    f.delete();
			}



			ByteArrayInputStream bis = new ByteArrayInputStream(str
                    .getBytes());



			 DataInputStream dataInputStream = new DataInputStream(
	                    new BufferedInputStream(bis));

			FileOutputStream wf = new FileOutputStream(f);
			DataOutputStream dos = new DataOutputStream(wf);



			int onebyte;
            while ((onebyte = dataInputStream.read()) != -1)
            {
                dos.write(onebyte);
            }

			dos.close();
			wf.close();

		}
		catch(Exception ce)
		{
			ce.printStackTrace();
		}
	}


    /**
     *
     * @param str
     * @param f
     */
    public static void cat(String str , File f  ,  String encoding)
	{
		try
		{
			String filePath = f.getPath();
			int length = filePath.lastIndexOf("/");
			if(length != -1 )
			{
			    filePath = filePath.substring(0,length);
			    File tmpFile = new File(filePath);
			    if(!tmpFile.exists())
			    {
			        tmpFile.mkdirs();
			    }
			}
			if(!f.exists())
			{

				f.createNewFile();
			}
			else
			{
			    f.delete();
			}



			ByteArrayInputStream bis = new ByteArrayInputStream(str
                    .getBytes(encoding));



			 DataInputStream dataInputStream = new DataInputStream(
	                    new BufferedInputStream(bis));

			FileOutputStream wf = new FileOutputStream(f);
			DataOutputStream dos = new DataOutputStream(wf);



			int onebyte;
            while ((onebyte = dataInputStream.read()) != -1)
            {
                dos.write(onebyte);
            }

			dos.close();
			wf.close();

		}
		catch(Exception ce)
		{
			ce.printStackTrace();
		}
	}
    
    
    public static void main(String argv[])
    {
    	/*String test = "j#+&";
    	test = PublicTools.Encoder(test);
    	System.out.print("5555555555%2B=" + test);*/
    	
    	//String key ="";
    	Map data = new HashMap();
    	
    	Map data1 = new HashMap();
    	Map data2 = new HashMap();
    	Map data3 = new HashMap();
    	
    	
    	data.put("key1", data1);
    	data1.put("key2", data2);
    	data2.put("key3", data3);
    	
    	List list = new ArrayList<>();
    	data3.put("key4", list);
    	
    	list.add(1);
    	
    	//String val = getValueFromMap("key1.key2.key3.key4.1", data);
    	
    	//System.out.println(val);
    	
    	List map = getListValueFromMap("key1.key2.key3.key4", data);
    	
    	System.out.println(map);
    	
    }


    /**
     * author: michael.yang
     * time: 2006-2-13
     *
     * @param value
     * @param defaultValue
     * @return the value parsed, defaultValue returned if error happens
     */
    public static int parseInt(Object value, int defaultValue)
    {
        try {
            return Integer.parseInt(String.valueOf(value));
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    /**
     * author: michael.yang
     * time: 2006-2-14
     */
    public static String parseString(Object value, String defaultValue)
    {
        if (null == value ||
            "null".equals(String.valueOf(value).toLowerCase())) {
            return defaultValue;
        }

        return String.valueOf(value);

        /* another implementation
           I am not sure if 'value' is null, will "null" be returned,
           which need testing

           String v = String.valueOf(value);
           if ("null".equals(v.toLowerCase())) return defaultValue;
           return v; */
    }

    public static String getCurrentTime()
    {
        return getCurrentTime("yyyy-MM-dd HH:mm:ss");
    }


    /**
     * Return current time in the pattern specified by
     * arguement 'format'.
     *
     * @param format
     * @return the formatted current time
     */
    public static String getCurrentTime(String format)
    {
        Date nowTime = new Date();
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        return fmt.format(nowTime);
    }

    public static String getMySQLDateTimeFormat() {
        return "yyyy-MM-dd HH:mm:ss";
    }

    public static String fmtDate(Date date, String fmt)
    {
        if (null == date) return "";
        SimpleDateFormat f = new SimpleDateFormat(fmt);
        return f.format(date);
    }

    public static boolean isNotNull(Object str){

        if(str == null || "".equals(String.valueOf(str).trim())){
            return false;
        }else{
            return true;
        }
    }

    public static boolean isNull(Object str){
        if(str != null && str instanceof String){
            str = ((String) str).trim();
        }
        if(str == null || "".equals(String.valueOf(str))){
            return true;
        }else{
            return false;
        }
    }


     /**
     * ��ȡϵͳΨһID
     *
     * @return
     */
    public static String getUUID() {
         String uid = "0";
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String tempId = sf.format(new Date());
        if (Long.parseLong(uid) >= Long.parseLong(tempId))
            uid = (Long.parseLong(uid) + 1) + "";
        else
            uid = tempId;

        return uid +  getRandomString(10);
    }


    public static String getRandomString(int size) {
        char[] c = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o',
                'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n', 'm' };
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < size; ++i)
            sb.append(c[(Math.abs(random.nextInt()) % c.length)]);

        return sb.toString();
    }


   
    
   

     /**
     * �ж�ĳ�������Ƿ�Ϊ�� �����ࡢ���������⴦��
     *
     * @param obj
     * @return ��Ϊ�գ�����true,����false
     * @author yehailong
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null)
            return true;

        // �����Ϊnull����Ҫ�����������������
        if (obj instanceof String) {
            return obj.equals("");
        } else if (obj instanceof Collection) {
            // ����Ϊ����
            Collection coll = (Collection) obj;
            return coll.size() == 0;
        } else if (obj instanceof Map) {
            // ����ΪMap
            Map map = (Map) obj;
            return map.size() == 0;
        } else if (obj.getClass().isArray()) {
            // ����Ϊ����
            return Array.getLength(obj) == 0;
        } else {
            // �������ͣ�ֻҪ��Ϊnull������Ϊempty
            return false;
        }
    }


    /**
     * �ַ����Ƿ�Ϊ����
     *
     * @param str
     * @return
     */
    public static boolean isDigitalString(String str) {
        if (isEmpty(str)) {
            return false;
        } else {
            try {
                Double.parseDouble(str);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    /**
     * �ַ����Ƿ�Ϊ����
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
        try {
            if(str != null && str.matches("\\d*.?\\d*")){
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * ������String.indexOf��������ȡ�����������е�λ�ã����ޣ�����-1
     *
     * @param array
     * @param value
     * @return
     */
    public static int arrayIndexOf(Object array, Object value) {
        if (array == null || value == null) {
            return -1;
        }
        int len = Array.getLength(array);
        for (int i = 0; i < len; i++) {
            if (value.equals(Array.get(array, i))) {
                return i;
            }
        }
        return -1;
    }

    

    public static String getPageSql(String sql, int startPos, int endPos) {
        if (DB_DIALECT.equals("ORACLE") || DB_DIALECT.equals("oracle")) {
            // �Ȱ���ѯ������ѯ����0��ҳδ�ļ�¼.Ȼ����ȡ����ҳ��ʼ��ҳδ�ļ�¼
            StringBuffer sqlBuffer = new StringBuffer();
            sqlBuffer.append("select * from ( select row_.*, rownum rownum_ from ( ");
            sqlBuffer.append(sql);
            sqlBuffer.append(" ) row_ where rownum <= " + endPos + ") where rownum_ >= " + startPos);

            return sqlBuffer.toString();
        } else {
            // TODO �������ݿ�ķ�ҳ�㷨
            return sql;
        }
    }
    public static long getFileSize(String filePath){
        long size = 0;
        try {
            File f = new File(filePath);

            if (f.exists()) {

                FileInputStream fis = null;

                fis = new FileInputStream(f);

                size = (long)((double)fis.available()/(double)1024);
            }
        }catch(Exception ex){

        }

        return size;
    }

    public static String toUnicodeString(String str) {
        str = (str == null ? "" : str);
        String tmp;
        StringBuffer sb = new StringBuffer();
        char c;
        int i, j;
        sb.setLength(0);
        for (i = 0; i < str.length(); i++) {
            c = str.charAt(i);
            sb.append("\\u");
            j = (c >>>8); //ȡ����8λ
            tmp = Integer.toHexString(j);
            if (tmp.length() == 1)
                sb.append("0");
            sb.append(tmp);
            j = (c & 0xFF); //ȡ����8λ
            tmp = Integer.toHexString(j);
            if (tmp.length() == 1)
                sb.append("0");
            sb.append(tmp);
        }

        return sb.toString();

    }

    public static String toUtf8String(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i=0;i<s.length();i++) {
	    char c = s.charAt(i);
	    if (c >= 0 && c <= 255) {
				sb.append(c);
	    } else {
				byte[] b;
				try {
		    	b = Character.toString(c).getBytes("utf-8");
				} catch (Exception ex) {
		    	System.out.println(ex);
		    	b = new byte[0];
				}
				for (int j = 0; j < b.length; j++) {
		    	int k = b[j];
		    	if (k < 0) k += 256;
		    	sb.append("%" + Integer.toHexString(k).
		    	toUpperCase());
			}
	   }
		}
		return sb.toString();
  }
    public static String sicenToComm(Object value) {
		 if(value == null){
			 return null;
		 }else if(value instanceof Double){
			 return sicenToCommDouble((Double)value);
		 }else{
			 return String.valueOf(value);
		 }
	 }

	// �����ѧ�����������⣬�������뱣����λС��

    public static String sicenToCommDouble(double value) {

        String retValue = null;

        DecimalFormat df = new DecimalFormat();  //DecimalFormat df = new DecimalFormat("0.00");  //�������Ļ�����ʡ���±�����

        df.setMinimumFractionDigits(0);

        df.setMaximumFractionDigits(5);

        retValue = df.format(value);

        //System.out.println(retValue);

        retValue = retValue.replaceAll(",","");

        return retValue;

    }


    public static String replaceResult(String result , String replaceRule){
        String str = result;

        if(str != null && PublicTools.isNotNull(replaceRule)){
            String[]  matchingRuleArray =  replaceRule.split("\r\n");
            if(matchingRuleArray != null){
                int i = -1;
                for(String tmpRule : matchingRuleArray){
                    try {
                        if(tmpRule != null && tmpRule.indexOf("~,~") == -1){
                            str = str.replaceAll(tmpRule , "");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if(str != null){
            str = str.trim();
        }
        return str;
    }


    /**
     * ��Է������� ��ֹ��Χ �Ľ������ �޳�
     * ������޳��ؼ����б�����~,~�ͱ�ʾ��������ֹ��Χ�� ǰ������ֱ�ʾ�޳��ڼ����Ľ��
     * @param result
     * @param replaceRule
     * @param ruleIndex   ��ʼƥ����ʽ:���к�
     * @return
     */
    public static String replaceResultAndStartEnd(String result , String replaceRule , int ruleIndex){

        ruleIndex += 1;

        String str = result;

        if(str != null && PublicTools.isNotNull(replaceRule) && replaceRule.indexOf("~,~") != -1){
            String[]  matchingRuleArray =  replaceRule.split("\r\n");
            if(matchingRuleArray != null){
                int i = -1;
                for(String tmpRule : matchingRuleArray){
                    try {
                        if(tmpRule != null && tmpRule.indexOf("~,~") != -1){
                            String[] t = tmpRule.split("~,~");
                            if(Integer.parseInt(t[0]) == ruleIndex){
                                str = str.replaceAll(t[1] , "");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if(str != null){
            str = str.trim();
        }
        return str;
    }




    public static String copyFirstToEnd(String tmp){
        if(tmp != null){
            int len =  tmp.indexOf("\r\n") ;
            if(len != -1){
                String str = tmp.substring(0 , len);
                tmp = tmp + "\r\n" + str;
            }
        }

        return tmp;
    }

    public static String getClassNameOfMethod(Object obj , String methodName){
        try {
           return obj.getClass().getMethod(methodName).getDeclaringClass().getName();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return "";
    }


    public static Double parseDouble(String value){
        Double r = null;
        if(value != null){
            value = value.trim();

            if(value.endsWith("%")){
                value = value.substring(0 , value.length() - 1);
            }

            r = Double.parseDouble(value);
        }
        return r;

    }

  

    public static String toXML(String str)
        /* fn-tl*/
    {
        if (str == null)    return null;

        String html = new String(str);
        html = Replace(html, "&", "&amp;");

        return html;
    }



	/**
	 * ���б�ƴ���ַ�������
	 * @param objList
	 * @return
	 */
	public static String getStr(List objList) {
		return getStr(objList , ",");
	}

	/**
	 * ���б�ƴ���ַ�������
	 * @param objList
	 * @param split
	 * @return
	 */
	public static String getStr(List objList , String split) {
		StringBuffer stringBuffer = new StringBuffer("");
		if(objList != null) {
			int i = 0;
			int length = objList.size();
			for(Object cmd : objList) {
				i++;
				stringBuffer.append(cmd);
				if(i < length) {
					stringBuffer.append(split);
				}
			}
		}
		return stringBuffer.toString();

    }
	
	public static Object getValue(String key , Map data){
		if(null != data){
			if(key.indexOf(".") != -1){
				String k = key.substring(0,key.indexOf("."));
				key = key.substring(key.indexOf(".")+1);
				
				Object value = data.get(k);
				
				if(value instanceof Map){
					return getValue(key,(Map)value);
				}else{
					return null;
				}
				
			}else{
				return data.get(key);
			}
		}
		
		return null;
	}
	

    public static String getValueFromMap(String key , Map data) {
    	Object val = getValue(key,data);
		if(val != null){

			return String.valueOf(val);
		}
    	return "";
    }

	public static Map getMapValueFromMap(String key , Map data) {
		Object val = getValue(key,data);
    	if(val instanceof Map){
    		return (Map)val;
    	}
    	return null;
	}

	public static List getListValueFromMap(String key , Map data) {
		Object val = getValue(key,data);
    	if(val instanceof List){
    		if(PublicTools.isNull(val)){
    			return  new ArrayList();
    		}
    		return (List)val;
    	}
    	return new ArrayList();
	}

	public static String getModelVal(String key , Map<String , String> data) {
		String val = "";
		if(PublicTools.isNotNull(key)) {
		    key = key.toLowerCase();
			if (data.containsKey(key)) {
				val = data.get(key);
			} else {
				//val = "";
				val = key;
			}
		}
		return val;
	}

    public static String getModelValByArray(String key , Map<String , String> data) {

        StringBuffer sb = new StringBuffer();
        String res = "";
        if(PublicTools.isNotNull(key)) {
            String[] roleArray = key.split(",");
            if (roleArray != null) {
                for(String str : roleArray) {
                    String val = "";
                    str = str.toLowerCase();
                    if (data.containsKey(str)) {
                        val = data.get(str);
                    } else {
                        val = str;
                    }
                    sb.append(val + ",");
                }
                if(sb.length() > 1) {
                    res = sb.substring(0 , sb.length() -1);
                }
            }

        }
        return res;
    }

	public static String getData(String key , Map m) {
	    String data = "";
        if(m.containsKey(key)) {
            data = (String)m.get(key);
        }

        if(data == null) {
            data = "";
        }
        return data;
    }
  
	

	public static String generateId(Integer size) {
		String pattern = "#000";
		NumberFormat nf = new DecimalFormat(pattern);

		String str = PublicTools.getCurrentTime("yyMMdd") + nf.format(size.intValue());
		return str;
	}

	public static String generateId(String pattern , Integer size) {
		try {

			NumberFormat nf = new DecimalFormat(pattern);
			return nf.format(size.intValue());

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return "";
	}


}// class end
