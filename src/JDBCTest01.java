import java.sql.*;

public class JDBCTest01 {
    public static void main(String[] args) {
        // 创建连接所需变量
        Statement statement = null;
        Connection connection = null;

        try {
            /*
                1、注册驱动
            */
            Driver driver = new com.mysql.jdbc.Driver(); // 使用多态
            DriverManager.registerDriver(driver);

            /*
               2、获取连接
               URL:统一资源定位符（网络中某个资源的绝对路径)https://www.baidu.com/这就是URL。
               URL包括几部分
                 1.协议
                 2.IP
                 3.PORT
                 4.资源名
               - 例如: http://182.61.200.7:80/index.html
               http://          通信协议
               182.61.200.7     服务器IP地址
               80               服务器上软件的端口
               index.html       服务器上某个资源名

               - 再例如：jdbc:mysql://127.0.0.1:3306/mydatabase
               jdbc:mysql://        协议
               127.0.0.1            本机IP
               3306                 数据库端口号
               mydatabase           MySQl数据库示例名
            */
            String url = "jdbc:mysql://127.0.0.1:3306/mydatabase"; // 需要访问的数据库为mydatabase
            String user = "root";   // MySQL账户
            String password = "123456";  // MySQL密码
            // 传入参数来建立与MySQL的连接
            connection = DriverManager.getConnection(url, user, password);

            // 测试连接
            System.out.println("数据库连接对象=" + connection);
            /*
                3. 获取数据库操作对象  Statement SQL
            */
            statement = connection.createStatement();

            /*
                4. 执行SQL语句
                 执行以下SQL插入语句
                 insert into students values("2022010105", "老六", " 男" , 18, 220101)
            */
            String sql = "insert into students values(\"2022010105\", \"老六\", \" 男\" , 18, 220101)";
            int count = statement.executeUpdate(sql);

            // 检查返回值 返回值为1则说明执行成功, 反之异常
            System.out.println("执行情况: "+ (count==1?"保存成功":"保存失败"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        /*
            5. 释放资源
            为了保证资源一定释放，在finally语句块中进行关闭操作
            并且要遵循从小到大以此关闭
            分别对其try catch 环绕 确保一定关闭所有资源
        */ finally {
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
    }
}
