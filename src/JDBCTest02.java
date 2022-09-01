import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class JDBCTest02 {
    public static void main(String[] args) {
        // 首先获取用户账号密码
        HashMap<String, String> accMap = getAccount();

        // 接下来进行数据库对比操作
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
