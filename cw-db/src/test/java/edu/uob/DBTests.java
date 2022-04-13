package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

// PLEASE READ:
// The tests in this file will fail by default for a template skeleton, your job is to pass them
// and maybe write some more, read up on how to write tests at
// https://junit.org/junit5/docs/current/user-guide/#writing-tests
final class DBTests {

  private DBServer server;

  // we make a new server for every @Test (i.e. this method runs before every @Test test case)
  @BeforeEach
  void setup(@TempDir File dbDir) {
    // Notice the @TempDir annotation, this instructs JUnit to create a new temp directory somewhere
    // and proceeds to *delete* that directory when the test finishes.
    // You can read the specifics of this at
    // https://junit.org/junit5/docs/5.4.2/api/org/junit/jupiter/api/io/TempDir.html

    // If you want to inspect the content of the directory during/after a test run for debugging,
    // simply replace `dbDir` here with your own File instance that points to somewhere you know.
    // IMPORTANT: If you do this, make sure you rerun the tests using `dbDir` again to make sure it
    // still works and keep it that way for the submission.

    server = new DBServer(dbDir);
  }

  // Here's a basic test for spawning a new server and sending an invalid command,
  // the spec dictates that the server respond with something that starts with `[ERROR]`
  @Test
  void testInvalidCommandIsAnError() {
    assertTrue(server.handleCommand("CREATE DATABASE markbook").startsWith("[ERROR]")); //missing ';'
    assertTrue(server.handleCommand("CREATE DATABASE markbook;").startsWith("[OK]"));
    assertTrue(server.handleCommand("USE markbook;").startsWith("[OK]"));
    assertTrue(server.handleCommand("USE DATABASE markbook;").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("CREATE TABLE (name, mark, pass);").startsWith("[OK]"));
//
    assertTrue(server.handleCommand("CREATE TABLE marks (name, mark, pass);").startsWith("[OK]"));
    assertTrue(server.handleCommand("CREATE TABLE marks name, mark, pass);").startsWith("[ERROR]")); //missing '('
    assertTrue(server.handleCommand("CREATE TABLE marks ();").startsWith("[OK]"));


    assertTrue(server.handleCommand("INSERT INTO marks VALUES ('Steve', 65, TRUE);").startsWith("[OK]"));
    assertTrue(server.handleCommand("INSERT INTO marks VALUES ('Dave', 55, TRUE);").startsWith("[OK]"));
    assertTrue(server.handleCommand("INSERT INTO marks VALUES ('Bob', 35, FALSE);").startsWith("[OK]"));
    assertTrue(server.handleCommand("INSERT INTO marks VALUES ('Clive', 20, FALSE);").startsWith("[OK]"));
    assertTrue(server.handleCommand("SELECT * FROM marks;").startsWith("[OK]"));
    assertTrue(server.handleCommand("foo").startsWith("[ERROR]"));
  }
  @Test
  void testIntegration(){
    server.handleCommand("CREATE DATABASE markbook;");
    server.handleCommand("USE markbook;");
    server.handleCommand("CREATE TABLE marks (name, mark, pass);");
    server.handleCommand("INSERT INTO marks VALUES ('Steve', 65, TRUE);");
    server.handleCommand("INSERT INTO marks VALUES ('Dave', 55, TRUE);");
    server.handleCommand("INSERT INTO marks VALUES ('Bob', 35, FALSE);");
    server.handleCommand("INSERT INTO marks VALUES ('Clive', 20, FALSE);");
    assertTrue(server.handleCommand("JOIN coursework AND marks ON grade AND id;").startsWith("[OK]"));
    assertTrue(server.handleCommand("UPDATE marks SET mark = 38 WHERE name == 'Clive';").startsWith("[OK]"));
    assertTrue(server.handleCommand("DELETE FROM marks WHERE name == 'Dave';").startsWith("[OK]"));
    assertTrue(server.handleCommand("SELECT * FROM marks;").startsWith("[OK]"));
    assertTrue(server.handleCommand("SELECT * FROM marks pass == TRUE;").startsWith("[ERROR]"));
  }

  // Add more unit tests or integration tests here.
  // Unit tests would test individual methods or classes whereas integration tests are geared
  // towards a specific usecase (i.e. creating a table and inserting rows and asserting whether the
  // rows are actually inserted)

}
