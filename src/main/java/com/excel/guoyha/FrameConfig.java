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
        // 初始化模板引擎
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();

        // 获取模板文件
        Template template = ve.getTemplate("hello.vm");

        // 设置变量
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("name", "Velocity");
        List<String> list = new ArrayList<String>();
        list.add("1");
        list.add("2");
        velocityContext.put("list", list);
        List<Book> bookList = new ArrayList<Book>();
        Book book = null;
        book = new Book();
        book.setName("三国演义1");
        bookList.add(book);
        book = new Book();
        book.setName("三国演义2");
        bookList.add(book);
        book = new Book();
        book.setName("三国演义3");
        bookList.add(book);
        velocityContext.put("bookList", bookList);
        Map<String, Book> bookMap = new HashMap<>();
        book = new Book();
        book.setName("三国演义1");
        bookMap.put("吴承恩1",book);
        book = new Book();
        book.setName("三国演义2");
        bookMap.put("吴承恩2",book);
        book = new Book();
        book.setName("三国演义3");
        bookMap.put("吴承恩3",book);
        velocityContext.put("bookMap", bookMap);

        // 输出
        StringWriter sw = new StringWriter();
        template.merge(velocityContext,sw);
        System.out.println(sw.toString());
    }
}
