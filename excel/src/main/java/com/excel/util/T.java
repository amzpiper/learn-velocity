package com.excel.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class T {
    public static void main(String[] args) {
        String text="�ء�\r\n" +
                "���������¾������죬�ص��˺������������������һƬ���ӡ���ǿ���Լ��򾲣�����ˮ��ǰ���˼���ˮ������һ�����������£���Ŀ�����Ѿ��������Եº���������ʱ��������ǰ��������ס�ֳ������˶��Եº��Ļ����С��������ϣ����������Ŵ����ը����ʱ���Ͼɵ�ľ�š�֨ѽ��һ�����ˣ�����������������λ̰�ٵļ����ˡ�\r\n" +
                "����̰��һ�������࣬է����ȥ����̫����ظɲ����������";
        //�������ȡ����ı���
        String result="";
        try {
            //����python�������ַ��������Ӧ����python��python�ļ�·������python���ݵĲ���
            String[] strs=new String[] {"E:\\cc_project\\py36\\Scripts\\python","E:/opt/anos/netcopa/wacapacity_json.py"};
            //Runtime���װ������ʱ�Ļ�����ÿ�� Java Ӧ�ó�����һ�� Runtime ��ʵ����ʹӦ�ó����ܹ��������еĻ��������ӡ�
            //һ�㲻��ʵ����һ��Runtime����Ӧ�ó���Ҳ���ܴ����Լ��� Runtime ��ʵ����������ͨ�� getRuntime ������ȡ��ǰRuntime����ʱ��������á�
            // exec(String[] cmdarray) �ڵ����Ľ�����ִ��ָ������ͱ�����
            Process pr = Runtime.getRuntime().exec(strs);
            //ʹ�û��������ܳ��򷵻صĽ��
            BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream(),"UTF8"));//ע���ʽ
            //����һ������python������ķ��ؽ��
            String line=" ";

            int index = -1;
            while((line=in.readLine())!=null) {
                index++;
                //ѭ����ӡ�����еĽ��
                result+=line+"\n";

                System.out.println(line);
                if(index > 2000) {
                    break;
                }
            }
            //�ر�in��Դ
            in.close();
            //pr.waitFor();
        }catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("python�����Ľ����");
        //��ӡ���ؽ��
        System.out.println(result);
    }
}
