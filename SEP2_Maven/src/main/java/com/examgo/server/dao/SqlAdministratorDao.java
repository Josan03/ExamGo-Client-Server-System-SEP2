package com.examgo.server.dao;

import com.examgo.client.model.*;

import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;

/**
 * A class that connects to the database and access the information for administrator's needs
 *
 * @author Cristian Josan && Dan Turcan
 * @version 1.0
 */
public class SqlAdministratorDao implements AdministratorDao
{
  private static AdministratorDao instance;

  /**
   * No-argument private constructor to initialize the postgreSQL driver
   *
   * @throws SQLException
   */
  private SqlAdministratorDao() throws SQLException
  {
    DriverManager.registerDriver(new org.postgresql.Driver());
  }

  /**
   * This method is created to control object creation, limiting the number to one.
   *
   * @return an instance of the class SqlAdministratorDao
   * @throws SQLException
   */
  public synchronized static AdministratorDao getInstance() throws SQLException
  {
    if (instance == null)
    {
      instance = new SqlAdministratorDao();
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
   * Reads the specified username of an administrator from the database if it exists in the database
   *
   * @param id an int value is used to search if the username of an administrator exists
   * @return an Administrator object which was found, else null
   * @throws SQLException
   */
  private Account read(int id) throws SQLException
  {
    try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "SELECT * FROM Administrator A "
                + "JOIN Login L ON A.username = L.username "
                + "WHERE L.username = ?"))
    {
      statement.setInt(1, id);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next())
      {
        return createAdministrator(resultSet);
      }
      else
      {
        return null;
      }
    }
  }

  /**
   * It is a private method that creates an Administrator object to return for the {@link #read} method
   *
   * @param resultSet a ResultSet interface which gets all the rows from a query
   * @return an Administrator object if the resultSet variable is not empty
   * @throws SQLException
   */
  private static Account createAdministrator(ResultSet resultSet)
      throws SQLException
  {
    int username = resultSet.getInt("username");
    String firstName = resultSet.getString("firstName");
    String lastName = resultSet.getString("lastName");
    String password = resultSet.getString("password");
    System.out.println("LOL");
    System.out.println(
        firstName + " " + lastName + " " + password + " " + username);
    return new Administrator(firstName, lastName, password, username);
  }

  @Override public Account getAdministrator(int id) throws RemoteException
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

  @Override public void addClassName(String id, Account teacher)
      throws RemoteException
  {
    try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "INSERT INTO Class(class_id, teacher_id, status) VALUES (?, ?, true)"))
    {
      statement.setString(1, id);
      statement.setInt(2, teacher.getID());
      statement.executeUpdate();
    }
    catch (SQLException e)
    {
      throw new RemoteException(e.getMessage(), e);
    }
  }

  @Override public StudentAccount addSingleStudent(String firstName,
      String lastName, String password, String class_id) throws RemoteException
  {
    try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "INSERT INTO Login(username, password) VALUES (nextval('login_id_sequence'), ?)",
            PreparedStatement.RETURN_GENERATED_KEYS))
    {
      statement.setString(1, password);
      statement.executeUpdate();
      ResultSet keys = statement.getGeneratedKeys();
      if (keys.next())
      {
        int username = keys.getInt("username");
        PreparedStatement studentStatement = connection.prepareStatement(
            "INSERT INTO Student(firstName, lastName, class_id, username, status) VALUES (?, ?, ?, ?, true)",
            PreparedStatement.RETURN_GENERATED_KEYS);
        studentStatement.setString(1, firstName);
        studentStatement.setString(2, lastName);
        studentStatement.setString(3, class_id);
        studentStatement.setInt(4, username);
        studentStatement.executeUpdate();
        ResultSet studentKeys = studentStatement.getGeneratedKeys();
        if (studentKeys.next())
        {
          ClassName className = new ClassName(class_id);
          return new Student(firstName, lastName, password, className,
              username);
        }
        else
        {
          throw new SQLException("No keys generated for student");
        }
      }
      else
      {
        throw new SQLException("No keys generated for login");
      }
    }
    catch (SQLException e)
    {
      throw new RemoteException(e.getMessage(), e);
    }
  }

  @Override public ArrayList<StudentAccount> addMultipleStudents(
      ArrayList<StudentAccount> students) throws RemoteException
  {
    try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "INSERT INTO Login(username, password) VALUES (nextval('login_id_sequence'), ?)",
            PreparedStatement.RETURN_GENERATED_KEYS))
    {
      ArrayList<StudentAccount> returnStudents = new ArrayList<>();
      for (StudentAccount student : students)
      {
        statement.setString(1, student.getPassword());
        statement.executeUpdate();
        ResultSet keys = statement.getGeneratedKeys();
        if (keys.next())
        {
          PreparedStatement studentStatement = connection.prepareStatement(
              "INSERT INTO Student(firstName, lastName, class_id, username, status) VALUES (?, ?, ?, ?, true)",
              PreparedStatement.RETURN_GENERATED_KEYS);
          int username = keys.getInt("username");
          studentStatement.setString(1, student.getFirstName());
          studentStatement.setString(2, student.getLastName());
          studentStatement.setString(3, student.getClassname());
          studentStatement.setInt(4, username);
          studentStatement.executeUpdate();
          ResultSet studentKeys = studentStatement.getGeneratedKeys();
          if (studentKeys.next())
          {
            returnStudents.add(
                new Student(student.getFirstName(), student.getLastName(),
                    student.getPassword(), student.getClassNameObj(),
                    username));
          }
          else
          {
            throw new SQLException("No keys generated for student");
          }
        }
        else
        {
          throw new SQLException("No keys generated for login");
        }
      }
      return returnStudents;
    }
    catch (SQLException e)
    {
      throw new RemoteException(e.getMessage(), e);
    }
  }

  @Override public Account addSingleTeacher(String firstName, String lastName,
      String password) throws RemoteException
  {
    try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "INSERT INTO Login(username, password) VALUES (nextval('login_id_sequence'), ?)",
            PreparedStatement.RETURN_GENERATED_KEYS))
    {
      statement.setString(1, password);
      statement.executeUpdate();
      ResultSet keys = statement.getGeneratedKeys();
      if (keys.next())
      {
        PreparedStatement teacherStatement = connection.prepareStatement(
            "INSERT INTO Teacher(firstName, lastName, username, status) VALUES (?, ?, ?,  true)",
            PreparedStatement.RETURN_GENERATED_KEYS);
        int username = keys.getInt("username");
        teacherStatement.setString(1, firstName);
        teacherStatement.setString(2, lastName);
        teacherStatement.setInt(3, username);
        teacherStatement.executeUpdate();
        ResultSet teacherKeys = teacherStatement.getGeneratedKeys();
        if (teacherKeys.next())
        {
          return new Teacher(firstName, lastName, password, username);
        }
        else
        {
          throw new SQLException("No keys generated for teacher");
        }
      }
      else
      {
        throw new SQLException("No keys generated for login");
      }
    }
    catch (SQLException e)
    {
      throw new RemoteException(e.getMessage(), e);
    }
  }

  @Override public ArrayList<Account> addMultipleTeachers(
      ArrayList<Account> teachers) throws RemoteException
  {
    try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "INSERT INTO Login(username, password) VALUES (nextval('login_id_sequence'), ?)",
            PreparedStatement.RETURN_GENERATED_KEYS))
    {
      ArrayList<Account> returnTeachers = new ArrayList<>();
      for (Account teacher : teachers)
      {
        statement.setString(1, teacher.getPassword());
        statement.executeUpdate();
        ResultSet keys = statement.getGeneratedKeys();
        if (keys.next())
        {
          PreparedStatement teacherStatement = connection.prepareStatement(
              "INSERT INTO Teacher(firstName, lastName, username, status) VALUES (?, ?, ?, true)",
              PreparedStatement.RETURN_GENERATED_KEYS);
          int username = keys.getInt("username");
          teacherStatement.setString(1, teacher.getFirstName());
          teacherStatement.setString(2, teacher.getLastName());
          teacherStatement.setInt(3, username);
          teacherStatement.executeUpdate();
          ResultSet teacherKeys = teacherStatement.getGeneratedKeys();
          if (teacherKeys.next())
          {
            returnTeachers.add(
                new Teacher(teacher.getFirstName(), teacher.getLastName(),
                    teacher.getPassword(), username));
          }
          else
          {
            throw new SQLException("No keys generated for teacher");
          }
        }
        else
        {
          throw new SQLException("No keys generated for login");
        }
      }
      return returnTeachers;
    }
    catch (SQLException e)
    {
      throw new RemoteException(e.getMessage(), e);
    }
  }

  @Override public boolean removeTeacher(Account teacher) throws RemoteException
  {
    try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "UPDATE Teacher " + "SET status = false " + "WHERE teacher_id = ?"))
    {
      statement.setInt(1, teacher.getID());
      int count1 = statement.executeUpdate();

      return count1 > 0;
    }
    catch (SQLException e)
    {
      throw new RemoteException(e.getMessage(), e);
    }
  }

  @Override public boolean removeStudent(StudentAccount student)
      throws RemoteException
  {
    try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "UPDATE Student " + "SET status = false " + "WHERE student_id = ?"))
    {
      statement.setInt(1, student.getID());
      int count1 = statement.executeUpdate();

      return count1 > 0;
    }
    catch (SQLException e)
    {
      throw new RemoteException(e.getMessage(), e);
    }
  }

  @Override public boolean removeClass(String class_id) throws RemoteException
  {
    try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "SELECT * FROM Class C "
                + "JOIN Student S ON C.class_id = S.class_id "
                + "WHERE C.class_id = ?"))
    {
      statement.setString(1, class_id);
      ResultSet resultSet = statement.executeQuery();
      int count1 = 0;
      while (resultSet.next())
      {
        int student_id = resultSet.getInt("student_id");

        PreparedStatement studentStatement = connection.prepareStatement(
            "UPDATE Student " + "SET status = false " + "WHERE student_id = ?");
        studentStatement.setInt(1, student_id);
        count1 = studentStatement.executeUpdate();
      }

      PreparedStatement classStatement = connection.prepareStatement(
          "UPDATE Class " + "SET status = false " + "WHERE class_id = ?");
      classStatement.setString(1, class_id);
      int count2 = classStatement.executeUpdate();

      return count1 > 0 && count2 > 0;
    }
    catch (SQLException e)
    {
      throw new RemoteException(e.getMessage(), e);
    }
  }

  @Override public void updateStudent(StudentAccount student, String class_id)
      throws RemoteException
  {
    try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "SELECT * FROM Student " + "WHERE student_id = ?"))
    {
      statement.setInt(1, student.getID());
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next())
      {
        int username = resultSet.getInt("username");

        PreparedStatement studentStatement = connection.prepareStatement(
            "UPDATE Student " + "SET firstName = ?, lastName = ?, class_id = ? "
                + "WHERE student_id = ?");
        studentStatement.setString(1, student.getFirstName());
        studentStatement.setString(2, student.getLastName());
        studentStatement.setString(3, class_id);
        studentStatement.setInt(4, student.getID());
        studentStatement.executeUpdate();

        PreparedStatement loginStatement = connection.prepareStatement(
            "UPDATE Login " + "SET password = ? " + "WHERE username = ?");
        loginStatement.setString(1, student.getPassword());
        loginStatement.setInt(2, username);
        loginStatement.executeUpdate();
      }
      else
      {
        throw new SQLException("No such key for student");
      }
    }
    catch (SQLException e)
    {
      throw new RemoteException(e.getMessage(), e);
    }
  }

  @Override public void updateTeacher(Account teacher) throws RemoteException
  {
    try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "SELECT * FROM Teacher " + "WHERE teacher_id = ?"))
    {
      statement.setInt(1, teacher.getID());
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next())
      {
        int username = resultSet.getInt("username");

        PreparedStatement studentStatement = connection.prepareStatement(
            "UPDATE Teacher " + "SET firstName = ?, lastName = ? "
                + "WHERE teacher_id = ?");
        studentStatement.setString(1, teacher.getFirstName());
        studentStatement.setString(2, teacher.getLastName());
        studentStatement.setInt(3, teacher.getID());
        studentStatement.executeUpdate();

        PreparedStatement loginStatement = connection.prepareStatement(
            "UPDATE Login " + "SET password = ? " + "WHERE username = ?");
        loginStatement.setString(1, teacher.getPassword());
        loginStatement.setInt(2, username);
        loginStatement.executeUpdate();
      }
      else
      {
        throw new SQLException("No such key for teacher");
      }
    }
    catch (SQLException e)
    {
      throw new RemoteException(e.getMessage(), e);
    }
  }

  @Override public void updateClassName(String oldId, String id,
      Account teacher) throws RemoteException
  {
    try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "UPDATE Class " + "SET class_id = ?, teacher_id = ? "
                + "WHERE class_id = ?"))
    {
      statement.setString(1, id);
      statement.setInt(2, teacher.getID());
      statement.setString(3, oldId);
      statement.executeUpdate();
    }
    catch (SQLException e)
    {
      throw new RemoteException(e.getMessage(), e);
    }
  }

  @Override public ArrayList<ClassName> getAllClassNames()
      throws RemoteException
  {
    try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "SELECT * FROM Class " + "WHERE status IS TRUE"))
    {
      ResultSet resultSet = statement.executeQuery();
      ArrayList<ClassName> classNames = new ArrayList<>();
      ClassName currentClass = null;
      while (resultSet.next())
      {
        String class_id = resultSet.getString("class_id");
        int teacher_id = resultSet.getInt("teacher_id");
        if (currentClass == null || !class_id.equals(
            currentClass.getClassName()))
        {
          PreparedStatement teacherStatement = connection.prepareStatement(
              "SELECT * FROM Teacher " + "WHERE teacher_id = ?");
          teacherStatement.setInt(1, teacher_id);
          ResultSet rsTeacher = teacherStatement.executeQuery();
          if (!rsTeacher.next())
          {
            throw new SQLException("No such key for teacher");
          }
          String firstName = rsTeacher.getString("firstName");
          String lastName = rsTeacher.getString("lastName");
          Teacher teacher = new Teacher(firstName, lastName, teacher_id);
          currentClass = new ClassName(class_id, teacher);
          classNames.add(currentClass);
        }
      }
      return classNames;
    }
    catch (SQLException e)
    {
      throw new RemoteException(e.getMessage(), e);
    }
  }

  @Override public ArrayList<Account> getAllTeachers() throws RemoteException
  {
    try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "SELECT * FROM Teacher " + "WHERE status IS TRUE"))
    {
      ResultSet resultSet = statement.executeQuery();
      ArrayList<Account> teachers = new ArrayList<>();
      Teacher currentTeacher = null;
      while (resultSet.next())
      {
        int username = resultSet.getInt("username");
        int teacher_id = resultSet.getInt("teacher_id");
        if (currentTeacher == null || teacher_id != currentTeacher.getID())
        {
          PreparedStatement teacherStatement = connection.prepareStatement(
              "SELECT * FROM Login " + "WHERE username = ?");
          teacherStatement.setInt(1, username);
          ResultSet rsLogin = teacherStatement.executeQuery();
          if (!rsLogin.next())
          {
            throw new SQLException("No such key for login");
          }
          String password = rsLogin.getString("password");
          String firstName = resultSet.getString("firstName");
          String lastName = resultSet.getString("lastName");
          currentTeacher = new Teacher(firstName, lastName, password,
              teacher_id);
          teachers.add(currentTeacher);
        }
      }
      return teachers;
    }
    catch (SQLException e)
    {
      throw new RemoteException(e.getMessage(), e);
    }
  }

  @Override public ArrayList<StudentAccount> getAllStudents()
      throws RemoteException
  {
    try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "SELECT * FROM Student " + "WHERE status IS TRUE"))
    {
      ResultSet resultSet = statement.executeQuery();
      ArrayList<StudentAccount> students = new ArrayList<>();
      StudentAccount currentStudent = null;
      while (resultSet.next())
      {
        int username = resultSet.getInt("username");
        String class_id = resultSet.getString("class_id");
        int student_id = resultSet.getInt("student_id");
        if (currentStudent == null || student_id != currentStudent.getID())
        {
          PreparedStatement classStatement = connection.prepareStatement(
              "SELECT * FROM Class "
                  + "WHERE class_id = ? AND status IS NOT FALSE");
          classStatement.setString(1, class_id);
          ResultSet rsClass = classStatement.executeQuery();
          if (!rsClass.next())
          {
            throw new SQLException("No such key for class");
          }

          PreparedStatement loginStatement = connection.prepareStatement(
              "SELECT * FROM Login " + "WHERE username = ?");
          loginStatement.setInt(1, username);
          ResultSet rsLogin = loginStatement.executeQuery();
          if (!rsLogin.next())
          {
            throw new SQLException("No such key for login");
          }
          String password = rsLogin.getString("password");
          String firstName = resultSet.getString("firstName");
          String lastName = resultSet.getString("lastName");
          ClassName classNameObj = new ClassName(class_id);
          currentStudent = new Student(firstName, lastName, password,
              classNameObj, student_id);
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
}
