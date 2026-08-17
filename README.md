# Neon Kotlin JDBC

A simple Kotlin example project demonstrating how to connect to a [Neon](https://neon.com/) Postgres database using the PostgreSQL JDBC driver and Gradle.

This project uses **raw JDBC without an ORM** and demonstrates common database operations including:

* Connecting to Neon Postgres
* Creating tables
* Inserting individual records
* Batch inserts with `PreparedStatement`
* Reading data with SQL queries
* Updating records
* Deleting records
* Managing transactions with `commit()` and `rollback()`
* Loading database credentials from an `.env` file using `dotenv-kotlin`

## Prerequisites

Before getting started, make sure you have:

* A [Neon](https://neon.com/) account
* [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) or later
* [Gradle](https://gradle.org/install/)

## Getting Started

### 1. Create a Neon project

Create a project in the [Neon Console](https://console.neon.tech/).

Neon creates a default database named `neondb` that you can use with this example.

### 2. Clone the repository

```bash
git clone https://github.com/aaravmahajanofficial/neon-kotlin-jdbc.git
cd neon-kotlin-jdbc
```

### 3. Configure dependencies

The project uses:

* PostgreSQL JDBC Driver
* dotenv-kotlin

The dependencies are managed through Gradle's version catalog.

The PostgreSQL JDBC driver and `dotenv-kotlin` are declared in `gradle/libs.versions.toml` and added to the application module through `app/build.gradle.kts`.

### 4. Configure your database connection

In the Neon Console, open your project and select **Connect**.

Choose **Java** as the language and copy the provided connection string. Kotlin uses the same JDBC connection string format.

Create an `.env` file inside the `app` directory:

```text
DATABASE_URL="jdbc:postgresql://[neon_hostname]/[dbname]?user=[user]&password=[password]&sslmode=require&channelBinding=require"
```

Replace the placeholders with the values from your Neon connection string.

> Do not commit `.env` or database credentials to source control.

### 5. Build the project

Run:

```bash
./gradlew build
```

On Windows:

```bash
gradlew.bat build
```

## Running the Examples

The examples are package-level Kotlin functions. `App.kt` is used to select which example to run.

For example:

```kotlin
fun main() {
    createTable()
    // readData()
    // updateData()
    // deleteData()
    // transactionExample()
}
```

Run the selected example with:

```bash
./gradlew run
```

On Windows:

```bash
gradlew.bat run
```

## Examples

### Create a table and insert data

`CreateTable.kt` demonstrates how to:

1. Connect to Neon using JDBC.
2. Drop the `books` table if it already exists.
3. Create a new `books` table.
4. Insert a single book using `PreparedStatement`.
5. Insert multiple books using a batch operation.

The table contains:

| Column             | Type           | Description                            |
| ------------------ | -------------- | -------------------------------------- |
| `id`               | `SERIAL`       | Primary key                            |
| `title`            | `VARCHAR(255)` | Book title                             |
| `author`           | `VARCHAR(255)` | Book author                            |
| `publication_year` | `INT`          | Publication year                       |
| `in_stock`         | `BOOLEAN`      | Whether the book is currently in stock |

Run it by enabling `createTable()` in `App.kt`:

```kotlin
createTable()
```

Then:

```bash
./gradlew run
```

Expected output:

```text
Connection established
Finished dropping table (if it existed).
Finished creating table.
Inserted a single book.
Inserted 3 rows of data.
```

### Read data

`ReadData.kt` retrieves all books from the database and orders them by publication year.

Enable:

```kotlin
readData()
```

Then run:

```bash
./gradlew run
```

The example prints each book's ID, title, author, publication year, and stock status.

### Update data

`UpdateData.kt` demonstrates updating an existing record using a parameterized SQL statement.

The example changes the `in_stock` value for `Dune` to `true`.

Enable:

```kotlin
updateData()
```

Then run:

```bash
./gradlew run
```

Afterward, run `readData()` to verify the updated value.

### Delete data

`DeleteData.kt` demonstrates deleting a record with a parameterized SQL statement.

The example removes the book `1984` from the `books` table.

Enable:

```kotlin
deleteData()
```

Then:

```bash
./gradlew run
```

You can run `readData()` afterward to verify that the record was removed.

### Transactions

`TransactionExample.kt` demonstrates how to execute multiple database operations as a single transaction.

The example:

1. Disables automatic commits with `autoCommit = false`.
2. Inserts a new book.
3. Updates its stock status.
4. Commits the transaction when both operations succeed.
5. Rolls back the transaction if an exception occurs.

The relevant transaction flow is:

```kotlin
conn.autoCommit = false

// Database operations...

conn.commit()
```

If an operation fails:

```kotlin
conn.rollback()
```

Enable:

```kotlin
transactionExample()
```

Then run:

```bash
./gradlew run
```

## Project Concepts

This project intentionally uses raw JDBC to demonstrate the fundamentals of interacting with PostgreSQL from Kotlin.

### Prepared statements

`PreparedStatement` is used for parameterized `INSERT`, `UPDATE`, and `DELETE` operations:

```kotlin
val sql = "UPDATE books SET in_stock = ? WHERE title = ?;"

conn.prepareStatement(sql).use { pstmt ->
    pstmt.setBoolean(1, true)
    pstmt.setString(2, "Dune")
    pstmt.executeUpdate()
}
```

Using parameters rather than constructing SQL with user-provided values helps prevent SQL injection.

### Resource management

JDBC connections and statements are managed with Kotlin's `.use { }` construct so resources are closed automatically.

```kotlin
DriverManager.getConnection(connString).use { conn ->
    // Database operations
}
```

### Environment variables

Database credentials are loaded through `dotenv-kotlin` rather than being hardcoded in Kotlin source files.

```kotlin
val dotenv = dotenv()
val connString = dotenv["DATABASE_URL"]
```

## Dependencies

The project uses the following libraries:

| Dependency             | Purpose                                               |
| ---------------------- | ----------------------------------------------------- |
| PostgreSQL JDBC Driver | Connects the Kotlin application to PostgreSQL         |
| dotenv-kotlin          | Loads the Neon database connection string from `.env` |

## Project Structure

The application code lives under:

```text
app/
└── src/
    └── main/
        └── kotlin/
            └── com/
                └── neon/
                    └── quickstart/
```

The examples are separated into individual Kotlin files:

```text
CreateTable.kt
ReadData.kt
UpdateData.kt
DeleteData.kt
TransactionExample.kt
App.kt
```

## Security

Keep your Neon connection string private. It contains database credentials.

Do not commit `.env` files containing real credentials to Git.

For production applications, use an appropriate secrets or environment-variable management solution rather than storing credentials directly in source code.

## Learn More

* [Neon Documentation](https://neon.com/docs)
* [Neon Kotlin JDBC Guide](https://neon.com/docs/guides/kotlin)
* [PostgreSQL JDBC Driver Documentation](https://jdbc.postgresql.org/documentation/use/)
* [Gradle User Manual](https://docs.gradle.org/current/userguide/userguide.html)
* [dotenv-kotlin](https://github.com/cdimascio/dotenv-kotlin)

## Related Neon Guides

If you're building a larger Kotlin application, you may also want to explore Neon integrations with frameworks and ORMs such as Micronaut and Hibernate.

* [Connect a Micronaut Kotlin application to Neon](https://neon.com/docs/guides/micronaut-kotlin)
* [Database Schema Changes with Hibernate, Spring Boot, and Neon](https://neon.com/guides/spring-boot-hibernate)

## License

This project is an example application demonstrating Kotlin, JDBC, and Neon Postgres.
