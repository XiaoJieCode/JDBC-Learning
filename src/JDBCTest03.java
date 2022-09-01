import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class JDBCTest03 {
    public static void main(String[] args) {
        // ���ȵ���getAccount()��ȡ�û��˺�����
        HashMap<String, String> accMap = getAccount();

        // ����������login()�����������ݿ�ԱȲ���
        boolean isLogin = login(accMap);

        // ����Ƿ��¼�ɹ�
        System.out.println(isLogin ? "��¼�ɹ���" : "��¼ʧ�ܣ������û���������");

    }

    private static boolean login(HashMap<String, String> accMap) {
        boolean isLogin = false;
        // ��ʼ�����ݿ�����
        Connection connection = null;
        PreparedStatement ps = null;
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
            // ���ò��Զ��ύ
            connection.setAutoCommit(false);

            // ��ǰд��sql���ģ��
            String sql = "select * from users where id = ? and password = ?";
            ps = connection.prepareStatement(sql);
            // ��ѯ���ݿ��Ƿ����˻������������Ƿ���ȷ
            ps.setString(1, accMap.get("id"));
            ps.setString(2, accMap.get("password"));

            resultSet = ps.executeQuery(sql);

            // �����ѯ��������˵�����ݿ����и��˻�
            if (resultSet.next()) isLogin = true;

            // �ύ
            connection.commit();
        } catch (Exception e) {
            try {
                if (connection != null) connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (ps != null) ps.close();
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
        System.out.print("���������id��");
        hashMap.put("id", scanner.next());
        System.out.print("������������룺");
        hashMap.put("password",scanner.next());
        return hashMap;
    }
}
