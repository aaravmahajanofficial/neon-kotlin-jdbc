package com.neon.quickstart

import io.github.cdimascio.dotenv.dotenv
import java.sql.DriverManager

fun deleteData() {
    val dotenv = dotenv()
    val connString = dotenv["DATABASE_URL"]
    val sql = "DELETE FROM books WHERE title = ?;"

    try {
        DriverManager.getConnection(connString).use { conn ->
            conn.prepareStatement(sql).use { pstmt ->
                println("Connection established")

                pstmt.setString(1, "1984")

                val rowsAffected = pstmt.executeUpdate()

                if (rowsAffected > 0) {
                    println("Deleted the book '1984' from the table.")
                }
            }
        }
    } catch (e: Exception) {
        println("Connection failed.")
        e.printStackTrace()
    }
}