package com.ane56.engine.jdbc.mock;

import com.ane56.engine.jdbc.entity.ClickLog;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Slf4j
public class MockQuickStart {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        Class<?> aClass = Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://rm-uf67xpwhzp9xvuciv2o.mysql.rds.aliyuncs.com:3306";
        String username = "root";
        String password = "Luxin@19980516";
        Connection connection = DriverManager.getConnection(url, username, password);
        String sql = "insert into engine.t_click_logs(name, url, create_time) values(?, ?, ?)";
        Faker faker = new Faker(Locale.CHINA);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        while (true) {
            ClickLog clickLog = ClickLog.builder()
                    .url(faker.internet().url())
                    .user(faker.name().fullName())
                    .cTime(sdf.format(new Date()))
                    .build();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, clickLog.getUser());
            preparedStatement.setString(2, clickLog.getUrl());
            preparedStatement.setString(3, clickLog.getCTime());
            preparedStatement.execute();
            log.info("insert data successfully, data: " + clickLog.toString());
        }
    }
}
