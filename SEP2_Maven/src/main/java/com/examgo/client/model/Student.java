package com.examgo.client.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * A class that stores information about a student
 *
 * @author Cristian Josan && Mihai Mihaila
 * @version 1.0
 */
public class Student implements StudentAccount, Serializable
{
  private static final long serialVersionUID = -2253333648068555358L;
  private final int id;
  private String firstName;
  private String lastName;
  private String password;
  private ClassName className;

  /**
   * Five-argument constructor initializing information for Student object
   *
   * @param firstName first name to initialize
   * @param lastName  last name to initialize
   * @param password  password to initialize
   * @param className  classname object to initialize
   * @param id  id to initialize
   */
  public Student(String firstName, String lastName, String password,
      ClassName className, int id)
  {
    this.firstName = firstName;
    this.lastName = lastName;
    this.className = className;
    this.password = password;
    this.id = id;
  }


  /**
   * Four-argument constructor initializing information for Student object
   *
   * @param firstName first name to initialize
   * @param lastName  last name to initialize
   * @param className  classname object to initialize
   * @param id  id to initialize
   */
  public Student(String firstName, String lastName, ClassName className, int id)
  {
    this.firstName = firstName;
    this.lastName = lastName;
    this.className = className;
    this.id = id;
  }


  public String getPassword()
  {
    return password;
  }

  @Override public String getClassname()
  {
    return className.getClassName();
  }

  @Override public void setClassname(ClassName classname)
  {
    this.className = classname;
  }

  public ClassName getClassNameObj()
  {
    return className;
  }

  public int getID()
  {
    return id;
  }

  public int getId()
  {
    return id;
  }

  public void setPassword(String password)
  {
    this.password = password;
  }

  public void setFirstName(String firstName)
  {
    this.firstName = firstName;
  }

  public void setLastName(String lastName)
  {
    this.lastName = lastName;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public String getLastName()
  {
    return lastName;
  }

  @Override public String toString()
  {
    return getFirstName() + " " + getLastName();
  }

  @Override public boolean equals(Object o)
  {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    Student student = (Student) o;

    if (!Objects.equals(firstName, student.firstName))
      return false;
    if (!Objects.equals(lastName, student.lastName))
      return false;
    if (!Objects.equals(password, student.password))
      return false;
    return Objects.equals(className, student.className);
  }

  public String getType()
  {
    return "student";
  }
}