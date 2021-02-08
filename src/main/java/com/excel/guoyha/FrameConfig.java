package com.excel.guoyha;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FrameConfig {

    public static void main(String[] args) {
        // ��ʼ��ģ������
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();

        // ��ȡģ���ļ�
        Template template = ve.getTemplate("hello.vm");

        // ���ñ���
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("name", "Velocity");
        List<String> list = new ArrayList<String>();
        list.add("1");
        list.add("2");
        velocityContext.put("list", list);
        List<Book> bookList = new ArrayList<Book>();
        Book book = null;
        book = new Book();
        book.setName("��������1");
        bookList.add(book);
        book = new Book();
        book.setName("��������2");
        bookList.add(book);
        book = new Book();
        book.setName("��������3");
        bookList.add(book);
        velocityContext.put("bookList", bookList);
        Map<String, Book> bookMap = new HashMap<>();
        book = new Book();
        book.setName("��������1");
        bookMap.put("��ж�1",book);
        book = new Book();
        book.setName("��������2");
        bookMap.put("��ж�2",book);
        book = new Book();
        book.setName("��������3");
        bookMap.put("��ж�3",book);
        velocityContext.put("bookMap", bookMap);

        // ���
        StringWriter sw = new StringWriter();
        template.merge(velocityContext,sw);
        System.out.println(sw.toString());
    }
}
