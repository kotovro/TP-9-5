package logic.persistence;

import java.sql.Connection;

public class InitDatabase {
    public static void main(String[] args) {
        Connection connection = DBManager.getConnection();
    }
}