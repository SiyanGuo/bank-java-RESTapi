package com.revature.main;

import com.revature.utility.ConnectionUtility;

import java.sql.SQLException;

public class Driver {
    public static void main(String[] args) {

        try {
            System.out.println(ConnectionUtility.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
