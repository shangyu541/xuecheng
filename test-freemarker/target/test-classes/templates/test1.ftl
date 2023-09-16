<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<#--Hello,${name}-->
<table>
    <tr>
        <td>序号</td>
        <td>名字</td>
        <td>年龄</td>
        <td>金额</td>
        <td>出生日期</td>
        <td>自定义日期展示格式</td>
    </tr>
    <#--判断空值 判断变量是否存在用??,如果改变量存在返回true，否则返回false-->
<#--    内建函数语法格式：变量+？+函数名称-->
<#--    size 集合大小-->
<#--    point是数字型，使用${point}会显示这个数字的值，并且每三位用逗号分隔-->
<#--如果不想显示为每三位分隔的数字，可以使用c函数将数字型转成字符输出-->

<#--    将字符串转换成对象  assign标签，他的作用是定义一个变量-->

    <#if stus??>
    <#--list数据-->
        <#list stus as stu>
            <tr>
                <td>序号</td>
                <td <#if stu.name=='老李'>style="background-color: blue" </#if>>${stu.name}</td>
                <td>${stu.age}</td>
                <td <#if stu.money lt 300>style="background-color: blue"</#if>>
                ${stu.money}
                </td>
                <td>${stu.birthday?date}</td>
                <td>${stu.birthday?string("yyyy年MM月dd日")}</td>
            </tr>
        </#list>
    </#if>
</table>

<br/>
遍历数据模型中的map数据,点状和放key都可以
<br/>
姓名:${(s2['stu1'].name)!''}<br/>
姓名:${(s2['stu2'].name)!''}<br/>
姓名:${(s2.stu1.name)!''}<br/>
姓名:${(s2.stu2.name)!''}<br/>

<br/>
遍历s2里面的keys赋值给stu
<br/>
<#if s2??>
<#list s2?keys as stu>
    遍历出来的key：${(stu)!''}
    ${(s2[stu].name)!''}<br/>
    ${(s2[stu].age)!''}<br/>
</#list>
</#if>
<br/>
${stus?size}
<br/>
<br/>
${pointss}
${pointss?c}
<br/>

<#assign text="{'blank':'傻逼建行','account':'1020392193'}"/>
<#assign data=text?eval/>
开户行：${data.blank}  账号：${data.account}
</body>
</html>