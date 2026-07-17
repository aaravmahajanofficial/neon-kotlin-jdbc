package com.neon.quickstart

import io.github.cdimascio.dotenv.dotenv
import java.sql.DriverManager
import java.sql.SQLException

fun transactionExample() {
    val dotenv = dotenv()
    val connString = dotenv["DATABASE_URL"]

    DriverManager.getConnection(connString).use { conn ->
        try {
            conn.autoCommit = false // Begin Transaction

            // Insert
            conn.prepareStatement(
                "INSERT INTO books (title, author, publication_year) VALUES (?, ?, ?)"
            ).use { pstmt ->
                pstmt.setString(1, "Brave New World")
                pstmt.setString(2, "Aldous Huxley")
                pstmt.setInt(3, 1932)
                pstmt.executeUpdate()

                println("Inserted book.")
            }

            // Update
            conn.prepareStatement(
                "UPDATE books SET in_stock = ? WHERE title = ?"
            ).use { pstmt ->
                pstmt.setBoolean(1, false)
                pstmt.setString(2, "Brave New World")
                pstmt.executeUpdate()

                println("Updated stock status.")
            }

            conn.commit() // Commit transaction
            println("Transaction committed successfully.")

        } catch (e: SQLException) {
            println("Transaction failed. Rolling back.")

            conn.rollback()
            e.printStackTrace()
        }
    }
}