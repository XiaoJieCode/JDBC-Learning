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

- 详情请看JDBCTest02.java

    ```java
    import java.io.FileInputStream;
    import java.io.FileNotFoundException;
    import java.io.IOException;
    import java.sql.*;
    import java.util.*;
    
    public class JDBCTest02 {
        public static void main(String[] args) {
            // 首先调用getAccount()获取用户账号密码
            HashMap<String, String> accMap = getAccount();
    
            // 接下来调用login()方法进行数据库对比操作
            boolean isLogin = login(accMap);
    
            // 检查是否登录成功
            System.out.println(isLogin ? "登录成功！" : "登录失败，请检查用户名或密码");
    
        }
    
        private static boolean login(HashMap<String, String> accMap) {
            boolean isLogin = false;
            
            // 初始化数据库连接
            Connection connection = null;
            Statement statement = null;
            ResultSet resultSet = null;
            String driver = "";
            String url = "";
            String username = "";
            String password = "";
            
            try {
                Properties properties = new Properties();
                properties.load(new FileInputStream("Resource\\mysql.properties"));
                driver = properties.getProperty("driver");
                url = properties.getProperty("url");
                username = properties.getProperty("username");
                password = properties.getProperty("password");
            } catch (IOException e) {
                e.printStackTrace();
            }
    
            try {
                Class.forName(driver);
                connection = DriverManager.getConnection(url,username,password);
                statement = connection.createStatement();
    
                // 查询数据库是否有账户名，且密码是否正确
                String sql = "select * from users where id='"+accMap.get("id")+"' and password='"+accMap.get("password")+"'";
                resultSet = statement.executeQuery(sql);
                
                // 如果查询到数据则说明数据库中有该账户
                if (resultSet.next()) isLogin = true;
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            } finally {
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
            }
       
            return isLogin;
        }
    
        private static HashMap<String,String> getAccount() {
            HashMap<String, String> hashMap = new HashMap<>();
            Scanner scanner = new  java.util.Scanner(System.in);
            System.out.print("请输入你的id：");
            hashMap.put("id", scanner.next());
            System.out.print("请输入你的密码：");
            hashMap.put("password",scanner.next());
            return hashMap;
        }
    }
    
    ```



# 五、SQL注入

## 1. 什么是SQL注入

- 在java源码中，我们是利用字符串拼接SQL指令的。

- 但当用户的信息中恰好包含一些SQL语句，那么业务的SQL语句可能会收到“污染”

- 例如第四章的例子中，我们输入用户名 admin   密码  admin or 1 = 1  

    一定会登录成功(貌似mysql8.0已修复)



# 六、解决SQL注入

- 示例在JDBCTest03.java

- 使用预编译SQL

- 方法

    ```java
    String sql = "select * from users where id = ? and password = ?";  // 不需要加入单引号，会自动引入
    PreparedStatment ps = connect.prepareStatment(sql);
    ps.setString(1, accMap.get("id"));
    ps.setString(2, accMap.get("password"));
    ```

    

# 七、JDBC事务提交

- JDBC的事务提交是执行一条sql语句就提交
- 但当有些情况是需要两条或多条sql同时执行完成
- 例如转账,A账户加100的同时B账户必须减100。这样才是一个正常的转账流程

- 解决方法：在执行Sql语句前设置不自动提交

    ```java
    connect.setAutoCommit(false);
    ```

- 但在sql语句全部执行完成后需要手动提交

    ```java
    connect.commit();
    ```

- 如果程序出异常需要手动回滚

    ```java
    connect.rollBack();
    ```

    

