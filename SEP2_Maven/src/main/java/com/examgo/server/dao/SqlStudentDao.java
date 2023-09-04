package com.examgo.server.dao;

import com.examgo.client.model.*;

import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;

/**
 * A class that connects to the database and access the information for student's needs
 *
 * @author Cristian Josan && Mihai Mihaila
 * @version 1.0
 */
public class SqlStudentDao implements StudentDao
{
  private static StudentDao instance;

  /**
   * No-argument private constructor to initialize the postgreSQL driver
   *
   * @throws SQLException
   */
  private SqlStudentDao() throws SQLException
  {
    DriverManager.registerDriver(new org.postgresql.Driver());
  }

  /**
   * This method is created to control object creation, limiting the number to one.
   *
   * @return an instance of the class SqlStudentDao
   * @throws SQLException
   */
  public synchronized static StudentDao getInstance() throws SQLException
  {
    if (instance == null) {
      instance = new SqlStudentDao();
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
    return DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres?currentSchema=examgo", "postgres", "1111");
  }

  /**
   * Reads the specified username of a student from the database if it exists in the database
   *
   * @param id an int value is used to search if the username of a student exists
   * @return a Student object which was found, else null
   * @throws SQLException
   */
  private StudentAccount read(int id) throws SQLException
  {
    try(Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "SELECT * FROM Student S " +
                "JOIN Login L ON S.username = L.username " +
                "WHERE L.username = ? AND S.status IS TRUE"
        ))
    {
      statement.setInt(1, id);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next())
      {
        return createStudent(resultSet);
      }
      else
      {
        return null;
      }
    }
  }

  /**
   * It is a private method that creates a Student object to return for the {@link #read} method
   *
   * @param resultSet a ResultSet interface which gets all the rows from a query
   * @return a Student object if the resultSet variable is not empty
   * @throws SQLException
   */
  private static StudentAccount createStudent(ResultSet resultSet) throws SQLException
  {
    int student_id = resultSet.getInt("student_id");
    String firstName = resultSet.getString("firstName");
    String lastName = resultSet.getString("lastName");
    String password = resultSet.getString("password");
    String className = resultSet.getString("class_id");
    ClassName classNameObj = new ClassName(className);
    return new Student(firstName, lastName, password, classNameObj, student_id);
  }

  @Override public StudentAccount getStudent(int id) throws RemoteException
  {
    try
    {
      return read(id);
    }
    catch (SQLException e)
    {
      throw new RemoteException(e.getMessage(), e);
    }
  }

  @Override public Session connectSession(int sessionId, int studentId)
      throws RemoteException
  {
    try(Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "SELECT * FROM Session " +
                "JOIN Test T ON Session.test_id = T.test_id " +
                "JOIN Teacher T2 ON T.teacher_id = T2.teacher_id " +
                "WHERE session_id = ?"
        ))
    {
      statement.setInt(1, sessionId);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next())
      {
        String firstName = resultSet.getString("firstName");
        String lastName = resultSet.getString("lastName");
        int teacher_id = resultSet.getInt("teacher_id");
        int test_id = resultSet.getInt("test_id");
        String title = resultSet.getString("title");
        Account teacher = new Teacher(firstName, lastName, teacher_id);
        Test test = new Test(test_id, title, teacher);

        PreparedStatement questionStatement = connection.prepareStatement(
            "SELECT * FROM QuestionTest QT " +
                "JOIN Question Q ON QT.question_id = Q.question_id " +
                "WHERE QT.test_id = ?"
        );
        questionStatement.setInt(1, test_id);
        ResultSet rsQuestion = questionStatement.executeQuery();
        ArrayList<Question> questions = new ArrayList<>();
        Question currentQuestion = null;
        while (rsQuestion.next())
        {
          int question_id = rsQuestion.getInt("question_id");
          if (currentQuestion == null || question_id != currentQuestion.getId())
          {
            double time = rsQuestion.getDouble("time");
            String questionContent = rsQuestion.getString("question");
            int points = rsQuestion.getInt("points");
            currentQuestion = new Question(question_id, time, questionContent, points);
          }
          questions.add(currentQuestion);


        }
        test.setQuestions(questions);

        PreparedStatement checkConnection = connection.prepareStatement(
            "SELECT * FROM StudentSession SS " +
                "JOIN Session S ON SS.session_id = S.session_id " +
                "JOIN Test T ON S.test_id = T.test_id " +
                "JOIN Teacher T2 ON T.teacher_id = T2.teacher_id " +
                "WHERE S.session_id = ? AND SS.student_id = ? AND S.status IS TRUE"
        );

        checkConnection.setInt(1, sessionId);
        checkConnection.setInt(2, studentId);
        ResultSet rs = checkConnection.executeQuery();
        if (rs.next())
        {
          return null;
        }


        PreparedStatement studentSessionStatement = connection.prepareStatement(
            "INSERT INTO StudentSession(student_id, session_id) VALUES (?, ?)"
        );
        studentSessionStatement.setInt(1, studentId);
        studentSessionStatement.setInt(2, sessionId);
        studentSessionStatement.executeUpdate();

        return new Session(sessionId, test, true);
      }
      else
      {
        return null;
      }
    }
    catch (SQLException e)
    {
      throw new RemoteException(e.getMessage(), e);
    }
  }

  @Override public void sendAnswers(int sessionId, ArrayList<Answer> answers, int studentID)
      throws RemoteException
  {
    try(Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "INSERT INTO SessionAnswer(session_id, question_id, answer, student_id) VALUES (?, ?, ?, ?)"
        ))
    {
      for(Answer answer : answers)
      {
        statement.setInt(1, sessionId);
        statement.setInt(2, answer.getQuestion_id());
        statement.setString(3, answer.getAnswer());
        statement.setInt(4, studentID);
        statement.executeUpdate();
      }
    }
    catch (SQLException e)
    {
      throw new RemoteException(e.getMessage(), e);
    }
  }

  @Override public ArrayList<StudentSession> getSessionsByStudent(int studentId)
      throws RemoteException
  {
    try(Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "SELECT SS.session_id, title, grade FROM StudentSession SS " +
                "JOIN Session S ON SS.session_id = S.session_id " +
                "JOIN Test T ON S.test_id = T.test_id " +
                "WHERE SS.student_id = ?"
        ))
    {
      statement.setInt(1, studentId);
      ResultSet resultSet = statement.executeQuery();
      ArrayList<StudentSession> studentSessions = new ArrayList<>();
      StudentSession currentSession;
      while (resultSet.next())
      {
        int session_id = resultSet.getInt("session_id");
        int grade = resultSet.getInt("grade");
        String title = resultSet.getString("title");
        currentSession = new StudentSession(session_id, title, grade);
        studentSessions.add(currentSession);
      }
      return studentSessions;
    }
    catch (SQLException e)
    {
      throw new RemoteException(e.getMessage(), e);
    }
  }
}
