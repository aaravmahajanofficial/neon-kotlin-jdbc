package com.neon.quickstart

import io.github.cdimascio.dotenv.dotenv
import java.sql.DriverManager

fun main() {
    val dotEnv = dotenv()
    val connString = dotEnv["DATABASE_URL"]

    try {
        DriverManager.getConnection(connString).use { conn ->
            println("Connection established")

            conn.createStatement().use { stmt ->
                // Drop the table if it already exists
                stmt.execute("DROP TABLE IF EXISTS books;")
                println("Finished dropping table (if it existed).")

                // Create a new table
                stmt.execute(
                    """
                    CREATE TABLE books (
                        id SERIAL PRIMARY KEY,
                        title VARCHAR(255) NOT NULL,
                        author VARCHAR(255),
                        publication_year INT,
                        in_stock BOOLEAN DEFAULT TRUE
                    );
                    """.trimIndent()
                )
                println("Finished creating table.")

                // Insert a single book record
                val insertOneSql = """
                    INSERT INTO books (title, author, publication_year, in_stock)
                    VALUES (?, ?, ?, ?);
                """.trimIndent()

                conn.prepareStatement(insertOneSql).use { pstmt ->
                    pstmt.setString(1, "The Catcher in the Rye")
                    pstmt.setString(2, "J.D. Salinger")
                    pstmt.setInt(3, 1951)
                    pstmt.setBoolean(4, true)
                    pstmt.executeUpdate()

                    println("Inserted a single book.")
                }

                // Insert multiple books
                val insertManySql = """
                    INSERT INTO books (title, author, publication_year, in_stock)
                    VALUES (?, ?, ?, ?);
                """.trimIndent()

                conn.prepareStatement(insertManySql).use { pstmt ->
                    val booksToInsert = listOf(
                        listOf("The Hobbit", "J.R.R. Tolkien", 1937, true),
                        listOf("1984", "George Orwell", 1949, true),
                        listOf("Dune", "Frank Herbert", 1965, false)
                    )

                    for (book in booksToInsert) {
                        pstmt.setString(1, book[0] as String)
                        pstmt.setString(2, book[1] as String)
                        pstmt.setInt(3, book[2] as Int)
                        pstmt.setBoolean(4, book[3] as Boolean)
                        pstmt.addBatch()
                    }

                    pstmt.executeBatch()
                    println("Inserted 3 rows of data.")
                }
            }
        }
    } catch (e: Exception) {
        println("Connection failed.")
        e.printStackTrace()
    }
}