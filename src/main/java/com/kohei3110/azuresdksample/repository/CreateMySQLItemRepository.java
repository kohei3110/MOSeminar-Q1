package com.kohei3110.azuresdksample.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

import com.kohei3110.azuresdksample.model.Member;

public class CreateMySQLItemRepository {

    Logger logger = Logger.getLogger(CreateMySQLItemRepository.class.getName());

    private static final String CREATE_SQL = "INSERT INTO member (id, name, email) VALUES (?, ?, ?)";

    private Properties properties;
    private Connection connection;

    public CreateMySQLItemRepository() {
        this.properties = new Properties();
        try {
            this.properties.load(this.getClass().getResourceAsStream("/application.properties"));
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
        try {
            this.connection = DriverManager.getConnection(
                    properties.getProperty("url"),
                    properties.getProperty("user"),
                    properties.getProperty("password"));
        } catch (SQLException e) {
            logger.warning(e.getMessage());
        }
    }

    public Member create(Member member) {
        try {
            PreparedStatement pstmt = this.connection.prepareStatement(
                    CREATE_SQL);
            pstmt = prepareStatement(pstmt, member);
            pstmt.executeUpdate();
            this.connection.close();
        } catch (SQLException e) {
            logger.warning(e.getMessage());
        }
        return member;
    }

    private PreparedStatement prepareStatement(PreparedStatement pstmt, Member member) {
        try {
            pstmt.setString(1, member.getId());
            pstmt.setString(2, member.getName());
            pstmt.setString(3, member.getEmail());
        } catch (SQLException e) {
            logger.warning(e.getMessage());
        }
        return pstmt;
    }
}
