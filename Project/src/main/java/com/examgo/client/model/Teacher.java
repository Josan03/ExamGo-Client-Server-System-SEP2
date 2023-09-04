package com.examgo.client.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A class that stores information about a teacher
 *
 * @author Michael Leo && Mihai Mihaila
 * @version 1.0
 */
public class Teacher implements Account, Serializable
{
  private final long serialVersionUID = 6921918539894186185L;
  private String firstName;
  private String lastName;
  private String password;
  private int id;
  private ArrayList<ClassName> classes;

  /**
   * Four-argument constructor initializing information for Teacher object
   *
   * @param fn   first name to initialize
   * @param ln   last name to initialize
   * @param pass password to initialize
   * @param id   id to initialize
   */
  public Teacher(String fn, String ln, String pass, int id)
  {
    firstName = fn;
    lastName = ln;
    password = pass;
    this.id = id;
    classes = new ArrayList<>();
  }

  /**
   * Three-argument constructor initializing information for Teacher object
   *
   * @param fn   first name to initialize
   * @param ln   last name to initialize
   * @param id   id to initialize
   */
  public Teacher(String fn, String ln, int id)
  {
    firstName = fn;
    lastName = ln;
    password = "";
    this.id = id;
    classes = new ArrayList<>();
  }

  public String getFirstName()
  {
    return firstName;
  }

  public String getLastName()
  {
    return lastName;
  }

  @Override public String getPassword()
  {
    return password;
  }

  @Override public int getID()
  {
    return id;
  }

  @Override public void setFirstName(String fn)
  {
    this.firstName = fn;
  }

  @Override public void setLastName(String ln)
  {
    this.lastName = ln;
  }

  @Override public void setPassword(String pass)
  {
    password = pass;
  }

  @Override public String toString()
  {
    return id + " | " + firstName + " " + lastName;
  }

  public String getType()
  {
    return "teacher";
  }
}
