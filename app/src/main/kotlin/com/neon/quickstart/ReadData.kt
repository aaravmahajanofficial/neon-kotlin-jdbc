package com.neon.quickstart

import io.github.cdimascio.dotenv.dotenv
import java.sql.DriverManager

fun readData() {
    val dotenv = dotenv()
    val connString = dotenv["DATABASE_URL"]

    try {
        DriverManager.getConnection(connString).use { conn ->
            conn.createStatement().use { stmt ->
                println("Connection established")

                val sql = "SELECT * FROM books ORDER BY publication_year;"

                stmt.executeQuery(sql).use { rs ->
                    println("\n--- Book Library ---")

                    while (rs.next()) {
                        println(
                            "ID: ${rs.getInt("id")}, " +
                                    "Title: ${rs.getString("title")}, " +
                                    "Author: ${rs.getString("author")}, " +
                                    "Year: ${rs.getInt("publication_year")}, " +
                                    "In Stock: ${rs.getBoolean("in_stock")}"
                        )
                    }

                    println("--------------------\n")
                }
            }
        }
    } catch (e: Exception) {
        println("Connection failed.")
        e.printStackTrace()
    }
}