package com.examgo.server.dao;

import com.examgo.client.model.Account;
import com.examgo.client.model.Question;
import com.examgo.client.model.Teacher;
import com.examgo.client.model.Test;

import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;

/**
 * A class that connects to the database and access the information for test's needs
 *
 * @author Cristian Josan && Dan Turcan
 * @version 1.0
 */
public class SqlTestDao implements TestDao
{
  private static TestDao instance;

  /**
   * No-argument private constructor to initialize the postgreSQL driver
   *
   * @throws SQLException
   */
  private SqlTestDao() throws SQLException
  {
    DriverManager.registerDriver(new org.postgresql.Driver());
  }

  /**
   * This method is created to control object creation, limiting the number to one.
   *
   * @return an instance of the class SqlTestDao
   * @throws SQLException
   */
  public synchronized static TestDao getInstance() throws SQLException
  {
    if (instance == null)
    {
      instance = new SqlTestDao();
    }
    return instance;
  }

  /**
   * Returns the connection to the specified schema with specified user and password from the database
   *
   * @return the connection to the database
   * @throws SQLException
   */
  private Connection getConnection() throws SQLException
  {
    return DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/postgres?currentSchema=examgo",
        "postgres", "1111");
  }

  /**
   * Reads the specified id of a test from the database if it exists in the database
   *
   * @param id an int value is used to search if the id of a test exists
   * @return a Test object which was found, else null
   * @throws SQLException
   */
  private Test read(int id) throws SQLException
  {
    try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "SELECT * FROM Test T JOIN Teacher TT ON T.teacher_id = TT.teacher_id WHERE T.test_id = ? AND T.status IS TRUE"
        ))
    {
      statement.setInt(1, id);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next())
      {
        return createTest(resultSet);
      }
      else
      {
        return null;
      }
    }
  }

  /**
   * It is a private method that creates a Test object to return for the {@link #read} method
   *
   * @param resultSet a ResultSet interface which gets all the rows from a query
   * @return a Test object if the resultSet variable is not empty
   * @throws SQLException
   */
  private static Test createTest(ResultSet resultSet) throws SQLException
  {
    int test_id = resultSet.getInt("test_id");
    String title = resultSet.getString("title");
    int teacher_id = resultSet.getInt("teacher_id");
    String firstName = resultSet.getString("firstName");
    String lastName = resultSet.getString("lastName");
    Account teacher = new Teacher(firstName, lastName, teacher_id);
    return new Test(test_id, title, teacher);
  }

  @Override public Question createQuestion(Test test, String question,
      double time, int points) throws SQLException
  {
    try (Connection connection = getConnection())
    {
      PreparedStatement questionStatement = connection.prepareStatement(
          "INSERT INTO Question(question, time, points) VALUES (?, ?, ?)",
          PreparedStatement.RETURN_GENERATED_KEYS);
      questionStatement.setString(1, question);
      questionStatement.setDouble(2, time);
      questionStatement.setInt(3, points);
      questionStatement.executeUpdate();
      ResultSet keys = questionStatement.getGeneratedKeys();
      if (!keys.next())
      {
        throw new SQLException("No keys generated for question");
      }
      int question_id = keys.getInt("question_id");
      Question q = new Question(question_id, time, question, points);
      test.addQuestion(q);
      PreparedStatement questionTestStatement = connection.prepareStatement(
          "INSERT INTO QuestionTest(question_id, test_id) VALUES (?, ?)");
      questionTestStatement.setInt(1, question_id);
      questionTestStatement.setInt(2, test.getId());
      questionTestStatement.executeUpdate();
      return q;
    }
  }

  private Test createTest(String title, Account teacher,
      ArrayList<Question> questions) throws SQLException
  {
    Connection connection = getConnection();
    try
    {
      connection.setAutoCommit(false);
      PreparedStatement testStatement = connection.prepareStatement(
          "INSERT INTO Test(title, teacher_id, status) VALUES (?, ?, true)",
          PreparedStatement.RETURN_GENERATED_KEYS);
      testStatement.setString(1, title);
      testStatement.setInt(2, teacher.getID());
      testStatement.executeUpdate();
      ResultSet keys = testStatement.getGeneratedKeys();
      if (!keys.next())
      {
        throw new SQLException("No keys generated for test");
      }
      int test_id = keys.getInt("test_id");
      Test test = new Test(test_id, title, teacher);
      PreparedStatement questionStatement = connection.prepareStatement(
          "INSERT INTO Question(question, time, points) VALUES (?, ?, ?)",
          PreparedStatement.RETURN_GENERATED_KEYS);
      for (Question question : questions)
      {
        questionStatement.setString(1, question.getQuestion());
        questionStatement.setDouble(2, question.getTime());
        questionStatement.setInt(3, question.getPoints());
        questionStatement.executeUpdate();
        ResultSet questionKeys = questionStatement.getGeneratedKeys();
        if (!questionKeys.next())
        {
          throw new SQLException("No keys generated for question");
        }
        int question_id = questionKeys.getInt("question_id");
        question.setId(question_id);
        test.addQuestion(question);
      }
      PreparedStatement questionTestStatement = connection.prepareStatement(
          "INSERT INTO QuestionTest(question_id, test_id) VALUES (?, ?)");
      for (Question question : questions)
      {
        questionTestStatement.setInt(1, question.getId());
        questionTestStatement.setInt(2, test_id);
        questionTestStatement.executeUpdate();
      }
      connection.commit();
      return test;
    }
    catch (SQLException e)
    {
      connection.rollback();
      throw e;
    }
    finally
    {
      connection.close();
    }
  }

  @Override public ArrayList<Test> getTestsByTeacher(Account teacher)
      throws RemoteException
  {
    try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "SELECT * FROM Test " + "WHERE teacher_id = ? AND status IS TRUE"))
    {
      statement.setInt(1, teacher.getID());
      ResultSet resultSet = statement.executeQuery();
      ArrayList<Test> tests = new ArrayList<>();
      Test currentTest = null;
      while (resultSet.next())
      {
        int test_id = resultSet.getInt("test_id");
        if (currentTest == null || test_id != currentTest.getId())
        {
          PreparedStatement questionStatement = connection.prepareStatement(
              "SELECT * FROM QuestionTest QT "
                  + "JOIN Question Q on Q.question_id = QT.question_id "
                  + "JOIN Test T on T.test_id = QT.test_id "
                  + "WHERE T.test_id = ?");
          questionStatement.setInt(1, test_id);
          ResultSet rsQuestions = questionStatement.executeQuery();
          String title = resultSet.getString("title");
          currentTest = new Test(test_id, title, teacher);
          while (rsQuestions.next())
          {
            int question_id = rsQuestions.getInt("question_id");
            double time = rsQuestions.getDouble("time");
            String questionContent = rsQuestions.getString("question");
            int points = rsQuestions.getInt("points");
            Question question = new Question(question_id, time, questionContent,
                points);
            currentTest.addQuestion(question);
          }
          tests.add(currentTest);
        }
      }
      return tests;
    }
    catch (SQLException e)
    {
      throw new RemoteException(e.getMessage(), e);
    }
  }

  @Override public ArrayList<Test> getAllTests() throws RemoteException
  {
    try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "SELECT * FROM Test T " +
                "JOIN Teacher T2 ON T.teacher_id = T2.teacher_id " +
                "WHERE T.status IS TRUE"
        ))
    {
      ResultSet resultSet = statement.executeQuery();
      ArrayList<Test> tests = new ArrayList<>();
      Test currentTest = null;
      while (resultSet.next())
      {
        int test_id = resultSet.getInt("test_id");

        if (currentTest == null || test_id != currentTest.getId())
        {
          String title = resultSet.getString("title");
          int teacher_id = resultSet.getInt("teacher_id");
          String firstName = resultSet.getString("firstName");
          String lastName = resultSet.getString("lastName");
          Account teacher = new Teacher(firstName, lastName, teacher_id);
          currentTest = new Test(test_id, title, teacher);

          PreparedStatement questionStatement = connection.prepareStatement(
              "SELECT * FROM QuestionTest QT " +
                  "JOIN Question Q on Q.question_id = QT.question_id " +
                  "JOIN Test T on T.test_id = QT.test_id " +
                  "WHERE T.test_id = ?"
          );
          questionStatement.setInt(1, test_id);
          ResultSet rsQuestions = questionStatement.executeQuery();
          while (rsQuestions.next())
          {
            int question_id = rsQuestions.getInt("question_id");
            double time = rsQuestions.getDouble("time");
            String questionContent = rsQuestions.getString("question");
            int points = rsQuestions.getInt("points");
            Question question = new Question(question_id, time, questionContent,
                points);
            currentTest.addQuestion(question);
          }
          tests.add(currentTest);
        }
      }
      return tests;
    }
    catch (SQLException e)
    {
      throw new RemoteException(e.getMessage(), e);
    }
  }

  @Override
  public void updateTest(Test test, Question question) throws RemoteException
  {
    try (Connection connection = getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "UPDATE Test SET title = ? WHERE test_id = ?");
      statement.setString(1, test.getTitle());
      statement.setInt(2, test.getId());

      PreparedStatement questionStatement = connection.prepareStatement(
          "UPDATE Question SET question = ?, time = ?, points = ? WHERE question_id = ?");
      questionStatement.setString(1, question.getQuestion());
      questionStatement.setDouble(2, question.getTime());
      questionStatement.setInt(3, question.getPoints());
      questionStatement.setInt(4, question.getId());
      questionStatement.executeUpdate();
      statement.executeUpdate();

    }
    catch (SQLException e)
    {
      throw new RemoteException(e.getMessage(), e);
    }
  }

  @Override public Test storeTest(Test test) throws RemoteException
  {
    try
    {
      if (read(test.getId()) == null)
      {
        return createTest(test.getTitle(), test.getTeacher(), test.getQuestions());
      }
      else
      {
        return read(test.getId());
      }
    }
    catch (SQLException e)
    {
      throw new RemoteException(e.getMessage(), e);
    }
  }

  @Override public boolean deleteTest(Test test) throws RemoteException
  {
    try (Connection connection = getConnection())
    {
      PreparedStatement checkTestInSession = connection.prepareStatement(
          "UPDATE Test " +
              "SET status = false " +
              "WHERE test_id = ?"
      );
      checkTestInSession.setInt(1, test.getId());
      int count1 = checkTestInSession.executeUpdate();

      return count1 > 0;
    }
    catch (SQLException e)
    {
      throw new RemoteException(e.getMessage(), e);
    }
  }

  @Override public boolean deleteQuestion(int questionId) throws RemoteException
  {
    try (Connection connection = getConnection())
    {
      PreparedStatement questionTestStatement = connection.prepareStatement(
          "DELETE FROM QuestionTest WHERE question_id = ?");
      questionTestStatement.setInt(1, questionId);
      int updateCount1 = questionTestStatement.executeUpdate();

      PreparedStatement questionStatement = connection.prepareStatement(
          "DELETE FROM Question WHERE question_id = ?");
      questionStatement.setInt(1, questionId);
      int updateCount2 = questionStatement.executeUpdate();

      return updateCount1 > 0 && updateCount2 > 0;
    }
    catch (SQLException e)
    {
      throw new RemoteException(e.getMessage(), e);
    }
  }
}
