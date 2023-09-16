package com.xuecheng.test.freemarker.controller;

import com.xuecheng.test.freemarker.model.Student;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FreemarkerControllerTest {

    @Autowired
    RestTemplate restTemplate;

    /**
     * 测试静态化，基于ftl文件生成html静态文件
     */
    @Test
    public void test222() throws IOException, TemplateException {
        /**
         * 定义配置类
         */
        Configuration configuration = new Configuration(Configuration.getVersion());
        /**
         * 定义模板，得到classpath的路径
         */
        String path = this.getClass().getResource("/").getPath();
        configuration.setDirectoryForTemplateLoading(new File(path+ "/templates/"));
        /**
         * 获取模板文件的内容
         */
        Template template = configuration.getTemplate("test1.ftl");
        /**
         * 定义数据类型
         */
        Map map = getMap();
        /**
         * 静态化
         */
        String s = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        InputStream inputStream = IOUtils.toInputStream(s);
        FileOutputStream fileOutputStream = new FileOutputStream(new File("d:/test1.hmtl"));
        IOUtils.copy(inputStream,fileOutputStream);
        inputStream.close();
        fileOutputStream.close();
    }

    @Test
    public void testGen() throws IOException, TemplateException {
        /**
         * 定义配置类
         */
        Configuration configuration = new Configuration(Configuration.getVersion());
        /**
         * 定义模板，得到classpath的路径
         */
        String templateString="" +
                "<html>\n" +
                "    <head></head>\n" +
                "    <body>\n" +
                "    名称：${name}\n" +
                "    </body>\n" +
                "</html>";
        StringTemplateLoader s=new StringTemplateLoader();
        s.putTemplate("templates",templateString);
        configuration.setTemplateLoader(s);
        Template templates = configuration.getTemplate("templates", "UTF-8");
        Map map = getMap();
        String intoString = FreeMarkerTemplateUtils.processTemplateIntoString(templates, map);
        InputStream inputStream = IOUtils.toInputStream(intoString);
        FileOutputStream fileOutputStream = new FileOutputStream(new File("d:/test2.html"));
        IOUtils.copy(inputStream,fileOutputStream);
        inputStream.close();
        fileOutputStream.close();

    }



    public Map getMap(){
        Map map=new HashMap();
        map.put("name","freemarker测试");
        Student student=new Student();
        student.setAge(18);
        student.setBirthday(new Date());
        student.setMoney((float) 200);
        student.setName("老李");
        List<Student> list=new ArrayList<>();
        list.add(student);
        Student s2=new Student();
        s2.setName("阿拉团");
        s2.setMoney((float) 5000);
        s2.setBirthday(new Date());
        s2.setAge(20);
        s2.setFriends(list);
        s2.setBestFriend(student);
        List<Student> students=new ArrayList<>();
        students.add(s2);
        students.add(student);
        /**
         * list数组数据
         */
        map.put("stus",students);


        HashMap<String,Student> hashMap=new HashMap();
        hashMap.put("stu1",student);
        hashMap.put("stu2",s2);
        /**
         * map数组数据
         */
        map.put("s2",hashMap);

        map.put("student",student);

        map.put("pointss",102920122);
        return map;

    }

    @Test
    public void RestTest(){
        String id="5a791725dd573c3574ee333f";
        String url="http://localhost:31001/getModel/";
        ResponseEntity<Map> forObject = restTemplate.getForEntity(url + id, Map.class);
        HttpStatus statusCode = forObject.getStatusCode();
        int statusCodeValue = forObject.getStatusCodeValue();
        Map body = forObject.getBody();
        System.out.println(body);
    }
}