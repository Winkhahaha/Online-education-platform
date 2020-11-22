<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Hello World!</title>
</head>
<body>
Hello ${name}!
<br/>
遍历数据模型中的list学生信息
<table>
    <tr>
        <td>序号</td>
        <td>姓名</td>
        <td>年龄</td>
        <td>财富</td>
        <td>出生日期</td>
    </tr>
<#--    判断stus是否存在-->
    <#if stus??>
    <#list stus as stu>
        <tr>
            <td>${stu_index+1}</td>
            <td<#if stu.name == '小明'> style="background-color: aqua;"</#if>> ${stu.name}</td>
            <td>${stu.age}</td>
            <td<#if (stu.money > 300)> style="background-color: purple;"</#if>>${stu.money}</td>
<#--            <td>${stu.birthday?date}</td>-->
            <td>${stu.birthday?string("yyyy年MM月dd日")}</td>
        </tr>
    </#list><br/>
        list的size:${stus?size}
    </#if>
</table>
<br/>
遍历数据模型map<br/>
第一种方法:stuMap[key].属性<br/>
姓名:${stuMap['stu1'].name} --- 年龄:${stuMap['stu1'].age}<br/>
第二种方法:stuMap.key.属性<br/>
姓名:${(stuMap.stu1.name)!''} --- 年龄:${(stuMap.stu1.age)!''}<br/>

遍历map中key<br/>
<#list stuMap?keys as k>
    姓名:${stuMap[k].name} --- 年龄:${stuMap[k].age}<br/>
</#list><br/>

${point?c}<br/>

JSON字符串转java对象<br/>
<#assign text="{'bank':'工商银行','account':'10101920201920212'}" />
<#assign data=text?eval />
开户行：${data.bank}  账号：${data.account}
</body>
</html>