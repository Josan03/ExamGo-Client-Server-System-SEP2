package com.examgo.server.dao;

import com.examgo.client.model.*;

import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;

/**
 * A class that connects to the database and access the information for session's needs
 *
 * @author Cristian Josan && Michael Leo
 * @version 1.0
 */
public class SqlSessionDao implements SessionDao
{
  private static SessionDao instance;

  /**
   * No-argument private constructor to initialize the postgreSQL driver
   *
   * @throws SQLException
   */
  private SqlSessionDao() throws SQLException
  {
    DriverManager.registerDriver(new org.postgresql.Driver());
  }

  /**
   * This method is created to control object creation, limiting the number to one.
   *
   * @return an instance of the class SqlSessionDao
   * @throws SQLException
   */
  public synchronized static SessionDao getInstance() throws SQLException
  {
    if (instance == null)
    {
      instance = new SqlSessionDao();
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

  @Override public Session createSession(Test test) throws RemoteException
  {
    try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "INSERT INTO Session(test_id, status) VALUES (?, ?)",
            PreparedStatement.RETURN_GENERATED_KEYS))
    {
      statement.setInt(1, test.getId());
      statement.setBoolean(2, true);
      statement.executeUpdate();
      ResultSet keys = statement.getGeneratedKeys();
      if (keys.next())
      {
        int session_id = keys.getInt("session_id");
        return new Session(session_id, test, true);
      }
      else
      {
        throw new SQLException("No keys generated");
      }
    }
    catch (SQLException e)
    {
      throw new RemoteException(e.getMessage(), e);
    }
  }

  @Override public boolean stopSession(int id) throws RemoteException
  {
    try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "UPDATE Session SET status = ? WHERE session_id = ?"))
    {
      statement.setBoolean(1, false);
      statement.setInt(2, id);
      int count = statement.executeUpdate();

      if (count > 0)
        return true;
      else
        return false;
    }
    catch (SQLException e)
    {
      throw new RemoteException(e.getMessage(), e);
    }
  }

  @Override public ArrayList<Session> getAllSessions() throws RemoteException
  {
    try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "SELECT * FROM Session " +
                "JOIN Test T ON Session.test_id = T.test_id " +
                "JOIN Teacher T2 ON T.teacher_id = T2.teacher_id "
        ))
    {
      ResultSet resultSet = statement.executeQuery();
      ArrayList<Session> sessions = new ArrayList<>();
      Session currentSession = null;
      while (resultSet.next())
      {
        int session_id = resultSet.getInt("session_id");
        if (currentSession == null || session_id != currentSession.getId())
        {
          boolean status = resultSet.getBoolean("status");
          int test_id = resultSet.getInt("test_id");
          String test_title = resultSet.getString("title");
          int teacher_id = resultSet.getInt("teacher_id");
          String firstName = resultSet.getString("firstName");
          String lastName = resultSet.getString("lastName");
          Teacher teacher = new Teacher(firstName, lastName, teacher_id);
          Test test = new Test(test_id, test_title, teacher);
          currentSession = new Session(session_id, test, status);

          sessions.add(currentSession);
        }
      }
      return sessions;
    }
    catch (SQLException e)
    {
      throw new RemoteException(e.getMessage(), e);
    }
  }

  @Override public ArrayList<Session> getSessionsByTeacher(Account teacher)
      throws RemoteException
  {
    try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "SELECT * FROM Session " +
                "JOIN Test T ON Session.test_id = T.test_id " +
                "JOIN Teacher T2 ON T.teacher_id = T2.teacher_id " +
                "WHERE T2.teacher_id = ?"
        ))
    {
      statement.setInt(1, teacher.getID());
      ResultSet resultSet = statement.executeQuery();
      ArrayList<Session> sessions = new ArrayList<>();
      Session currentSession = null;
      while (resultSet.next())
      {
        int session_id = resultSet.getInt("session_id");
        if (currentSession == null || session_id != currentSession.getId())
        {
          PreparedStatement questionStatement = connection.prepareStatement(
              "SELECT * FROM Session S " +
                  "JOIN Test T ON S.test_id = T.test_id " +
                  "JOIN QuestionTest QT ON QT.test_id = T.test_id " +
                  "JOIN Question Q on QT.question_id = Q.question_id " +
                  "WHERE S.session_id = ?"
          );

          int test_id = resultSet.getInt("test_id");
          boolean status = resultSet.getBoolean("status");
          String test_title = resultSet.getString("title");

          questionStatement.setInt(1, session_id);
          ResultSet rsQuestion = questionStatement.executeQuery();
          Question currentQuestion = null;
          Test test = new Test(test_id, test_title, teacher);
          while (rsQuestion.next())
          {
            int question_id = rsQuestion.getInt("question_id");
            if (currentQuestion == null
                || question_id != currentQuestion.getId())
            {
              double time = rsQuestion.getDouble("time");
              String question = rsQuestion.getString("question");
              int points = rsQuestion.getInt("points");
              currentQuestion = new Question(question_id, time, question,
                  points);
              test.addQuestion(currentQuestion);
            }
          }
          System.out.println(test.getQuestions());
          currentSession = new Session(session_id, test, status);
          sessions.add(currentSession);
        }
      }
      return sessions;
    }
    catch (SQLException e)
    {
      throw new RemoteException(e.getMessage(), e);
    }
  }
}
