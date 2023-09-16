/**
 * FileName: Student
 * Author:   admin
 * Date:     2020/6/16 18:10
 * Description: 学生表
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xuecheng.test.freemarker.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈学生表〉
 * @author admin
 * @create 2020/6/16
 * @since 1.0.0
 */
@Data
public class Student {
    /**
     * 姓名
     */
    private String name;
    /**
     * 姓名
     */
    private int age;
    /**
     * 生日
     */
    private Date birthday;
    /**
     * 钱包
     */
    private Float money;
    /**
     * 朋友列表
     */
    private List<Student> friends;
    /**
     * 最好的朋友
     */
    private Student bestFriend;
}
