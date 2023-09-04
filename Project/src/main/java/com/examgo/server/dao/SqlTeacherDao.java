package com.examgo.server.dao;

import com.examgo.client.model.*;

import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;

/**
 * A class that connects to the database and access the information for teacher's needs
 *
 * @author Cristian Josan && Mihai Mihaila
 * @version 1.0
 */
public class SqlTeacherDao implements TeacherDao
{
  private static TeacherDao instance;

  /**
   * No-argument private constructor to initialize the postgreSQL driver
   *
   * @throws SQLException
   */
  private SqlTeacherDao() throws SQLException
  {
    DriverManager.registerDriver(new org.postgresql.Driver());
  }

  /**
   * This method is created to control object creation, limiting the number to one.
   *
   * @return an instance of the class SqlTeacherDao
   * @throws SQLException
   */
  public synchronized static TeacherDao getInstance() throws SQLException
  {
    if (instance == null)
    {
      instance = new SqlTeacherDao();
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
   * Reads the specified username of a teacher from the database if it exists in the database
   *
   * @param id an int value is used to search if the username of a teacher exists
   * @return a Teacher object which was found, else null
   * @throws SQLException
   */
  private Account read(int id) throws SQLException
  {
    try(Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "SELECT * FROM Teacher T " +
                "JOIN Login L ON T.username = L.username " +
                "WHERE L.username = ? AND T.status IS TRUE"
        ))
    {
      statement.setInt(1, id);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next())
      {
        int teacher_id = resultSet.getInt("teacher_id");
        String firstName = resultSet.getString("firstName");
        String lastName = resultSet.getString("lastName");
        String password = resultSet.getString("password");
        return new Teacher(firstName, lastName, password, teacher_id);
      }
      else
      {
        return null;
      }
    }
  }

  @Override public Account getTeacher(int id) throws RemoteException
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

  @Override public ArrayList<ClassName> getClassesByTeacher(int teacher_id)
      throws RemoteException
  {
    try(Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "SELECT * FROM Class C " +
                "JOIN Teacher T ON C.teacher_id = T.teacher_id " +
                "WHERE T.teacher_id = ?"
        ))
    {
      statement.setInt(1, teacher_id);
      ResultSet resultSet = statement.executeQuery();
      ArrayList<ClassName> classNames = new ArrayList<>();
      ClassName currentClassName = null;
      while (resultSet.next())
      {
        String className = resultSet.getString("class_id");
        if (currentClassName == null || !className.equals(currentClassName.getClassName()))
        {
          currentClassName = new ClassName(className);
          classNames.add(currentClassName);
        }
      }
      System.out.println(classNames);
      return classNames;
    }
    catch (SQLException e)
    {
      throw new RemoteException(e.getMessage(), e);
    }
  }

  @Override public ArrayList<StudentAccount> getStudentsByTeacher(int teacher_id)
      throws RemoteException
  {
    try(Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "SELECT * FROM Class C " +
                "JOIN Student S ON C.class_id = S.class_id " +
                "JOIN Teacher T ON C.teacher_id = T.teacher_id " +
                "WHERE T.teacher_id = ? AND S.status IS TRUE"
        ))
    {
      statement.setInt(1, teacher_id);
      ResultSet resultSet = statement.executeQuery();
      ArrayList<StudentAccount> students = new ArrayList<>();
      StudentAccount currentStudent = null;
      while (resultSet.next())
      {
        int student_id = resultSet.getInt("student_id");
        if (currentStudent == null || student_id != currentStudent.getID())
        {
          String firstName = resultSet.getString("firstName");
          String lastName = resultSet.getString("lastName");
          String className = resultSet.getString("class_id");
          ClassName classNameObj = new ClassName(className);
          currentStudent = new Student(firstName, lastName, classNameObj, student_id);
          students.add(currentStudent);
        }
      }
      return students;
    }
    catch (SQLException e)
    {
      throw new RemoteException(e.getMessage(), e);
    }
  }

  @Override public ArrayList<StudentAccount> getStudentsBySession(int session_id)
      throws RemoteException
  {
    try(Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "SELECT * FROM StudentSession SS " +
                "JOIN Student S on SS.student_id = S.student_id " +
                "WHERE session_id = ?"
        ))
    {
      statement.setInt(1, session_id);
      ResultSet resultSet = statement.executeQuery();
      ArrayList<StudentAccount> students = new ArrayList<>();
      StudentAccount currentStudent = null;
      while (resultSet.next())
      {
        int student_id = resultSet.getInt("student_id");
        if (currentStudent == null || student_id != currentStudent.getID())
        {
          String firstName = resultSet.getString("firstName");
          String lastName = resultSet.getString("lastName");
          String className = resultSet.getString("class_id");
          ClassName classNameObj = new ClassName(className);
          currentStudent = new Student(firstName, lastName, classNameObj, student_id);
          students.add(currentStudent);
        }
      }
      return students;
    }
    catch (SQLException e)
    {
      throw new RemoteException(e.getMessage(), e);
    }
  }

  @Override public ArrayList<Answer> getAnswersByStudentSession(int student_id,
      int session_id) throws RemoteException
  {
    try(Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "SELECT * FROM SessionAnswer " +
                "WHERE student_id = ? AND session_id = ?"
        ))
    {
      statement.setInt(1, student_id);
      statement.setInt(2, session_id);
      ResultSet resultSet = statement.executeQuery();
      ArrayList<Answer> answers = new ArrayList<>();
      Answer currentAnswer = null;
      while (resultSet.next())
      {
        int session_answer_id = resultSet.getInt("session_answer_id");
        if (currentAnswer == null || session_answer_id != currentAnswer.getAnswer_id())
        {
          int question_id = resultSet.getInt("question_id");
          String answer = resultSet.getString("answer");
          int points = resultSet.getInt("points");
          currentAnswer = new Answer(session_answer_id, question_id, answer, points);
          answers.add(currentAnswer);
        }
      }
      return answers;
    }
    catch (SQLException e)
    {
      throw new RemoteException(e.getMessage(), e);
    }
  }

  @Override public void updatePoints(int answer_id, int points)
      throws RemoteException
  {
    try(Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "UPDATE SessionAnswer " +
                "SET points = ? " +
                "WHERE session_answer_id = ?"
        ))
    {
      statement.setInt(1, points);
      statement.setInt(2, answer_id);
      statement.executeUpdate();
    }
    catch (SQLException e)
    {
      throw new RemoteException(e.getMessage(), e);
    }
  }

  @Override public void updateGrade(int student_id, int session_id, int grade)
      throws RemoteException
  {
    try(Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "UPDATE StudentSession " +
                "SET grade = ? " +
                "WHERE student_id = ? AND session_id = ?"
        ))
    {
      statement.setInt(1, grade);
      statement.setInt(2, student_id);
      statement.setInt(3, session_id);
      statement.executeUpdate();
    }
    catch (SQLException e)
    {
      throw new RemoteException(e.getMessage(), e);
    }
  }

  @Override public int getGradeByStudentSession(int student_id, int session_id)
      throws RemoteException
  {
    try(Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "SELECT grade FROM StudentSession " +
                "WHERE student_id = ? AND session_id = ?"
        ))
    {
      statement.setInt(1, student_id);
      statement.setInt(2, session_id);
      ResultSet resultSet = statement.executeQuery();
      if (!resultSet.next())
      {
        throw new SQLException("No grades for such session and student");
      }
      return resultSet.getInt("grade");
    }
    catch (SQLException e)
    {
      throw new RemoteException(e.getMessage(), e);
    }
  }
}
