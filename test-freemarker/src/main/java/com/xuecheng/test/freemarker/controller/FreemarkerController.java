/**
 * FileName: FreemarkerController
 * Author:   admin
 * Date:     2020/6/16 18:19
 * Description: freemarker测试类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.test.freemarker.controller;

import com.xuecheng.test.freemarker.model.Student;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 〈一句话功能简述〉<br> 
 * 〈freemarker测试类〉
 * @author admin
 * @create 2020/6/16
 * @since 1.0.0
 */
@Controller
//@RequestMapping("/freemarker")
public class FreemarkerController {

    @RequestMapping("/test1")
    public String Test1(Map map){
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

        return "test1";
    }

    @RequestMapping("/banner")
    public String index_banner(Map<String,Object> map){
        return "index_banner";
    }

}
