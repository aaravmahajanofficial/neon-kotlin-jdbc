package com.neon.quickstart

import io.github.cdimascio.dotenv.dotenv
import java.sql.DriverManager

fun updateData() {
    val dotenv = dotenv()
    val connString = dotenv["DATABASE_URL"]
    val sql = "UPDATE books SET in_stock = ? WHERE title = ?;"

    try {
        DriverManager.getConnection(connString).use { conn ->
            conn.prepareStatement(sql).use { pstmt ->
                println("Connection established")

                pstmt.setBoolean(1, true)
                pstmt.setString(2, "Dune")

                val rowsAffected = pstmt.executeUpdate()

                if (rowsAffected > 0) {
                    println("Updated stock status for 'Dune'.")
                }
            }
        }
    } catch (e: Exception) {
        println("Connection failed.")
        e.printStackTrace()
    }
}