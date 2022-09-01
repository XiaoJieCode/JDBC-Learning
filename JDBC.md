# 一、JDBC是什么

- JDBC：Java DataBase Connectivity (Java语言连接数据库)
- JDBC本质:
    - JDBC是SUN公司定制的一套接口
    - 接口都有调用者和实现者
    - 面向接口调用、面向接口写实现类，这都属于面向接口编程
- 为什么要面向接口编程
    - 解耦合：降低程序的耦合度，提高程序的扩展力
- 多态机制就是非常典型的面向对象编程



# 二、JDBC开发前的准备工作

- 配置SQL数据库的驱动

## 

# 三、JDBC编程6步

- **演示程序为JDBCTest01.java**

1. 注册驱动

    ```java
    // 在MySQL8.0版本以后调用com.mysql.cj.jdbc.Driver的静态代码块就能完成驱动的注册
    Class.forName("com.mysql.cj.jdbc.Driver");
    ```

2. 获取连接

    ```java
    connection = DriverManager.getConnection(url, user, password);
    ```

    - url:sql访问地址  jdbc:mysql://127.0.0.1:3306/mydatabase
        - jdbc:mysql://  表示mysql协议
        - 127.0.0.1为本地ip  也可用localhost代替
        - 3306为数据库端口
        - mydatabase为需要访问的数据库
    - user：数据库用户名
    - passwor：数据库密码

3. 获取数据库操作对象

    ```java
    statement = connection.createStatement();
    ```

    - 所有的sql语句将在statement对象中执行

4. 执行SQL语句

    1. 执行增删改sql语句

        ```java
        String sql = "insert into students values(\"2022010105\", \"老六\", \" 男\" , 18, 220101)";
        int count = statement.executeUpdate(sql)
        // 检查返回值 返回值为1则说明执行成功, 反之异常
        System.out.println("执行情况: " + (count == 1 ? "保存成功" : "保存失败"));
        ```

    

    

5. 处理查询结果集

   1. 执行查询sql语句

    ```java
    ResultSet resultSet = null;
    String sql = "select stu_num from students";  // 查询students的所有字段
    resultSet = statement.executeQuery("select * from classes");
    
    // 算法
    int column = 1;
    while (true) {
        try {
            System.out.print(
                resultSet.getString(column)  // 获取字段对应的内容
                             + "\t"   // 格式化输出
            );
            column++;
        } catch (SQLException e) {
            column = 1;
            System.out.println();  // 换行
            if (!resultSet.next()) 
            {
                break;
            }
        }
    }
    ```

    

6. 释放资源

    - 执行sql相关对象的close()方法即可释放资源

    - 执行前一定要判空

    - 推荐释放资源写在finally{}中以确保一定执行释放资源语句

    - 释放资源顺序为从小到大

        - 例如上个例子中

            我们应该先关闭结果集 ResultSet

            再关闭 Statement

            最后关闭 Connect

        ```java
        try {
            if (resultSet != null) resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        try {
            if (statement != null) statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ```



# 四、创建一个简单的用户登录业务

## 1. 数据准备

- 创建表格users

    ```mysql
    create table users(
    	id varchar(20) primary key,
        password varchar(20) not null
    );
    ```

    

- 创建两个用户:  jack  和  wenjie

    ```mysql
    insert into users values(
    	'jack', 123456
    );
    insert into users values(
    	'wenjie', 666666
    );
    ```


## 2. 业务逻辑

- 用户输入分两次输入账号和密码登录
- 获取用户账号密码对数据库进行查询
- 验证成功显示登录成功，反之

## 3. 代码实现

```mysql

```

