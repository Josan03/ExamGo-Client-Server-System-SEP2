package com.examgo.client.model;

import java.io.Serializable;

/**
 * A class that stores information about an administrator
 *
 * @author Mihai Mihaila && Dan Turcan
 * @version 1.0
 */
public class Administrator implements Account, Serializable
{
  private String firstName;
  private String lastName;
  private String password;
  private final int id;

  /**
   * Four-argument constructor initializing information for Administrator object
   *
   * @param firstName first name to initialize
   * @param lastName  last name to initialize
   * @param password  password to initialize
   * @param id        id to initialize
   */
  public Administrator(String firstName, String lastName, String password,
      int id)
  {
    this.firstName = firstName;
    this.lastName = lastName;
    this.password = password;
    this.id = id;
  }

  @Override public String getFirstName()
  {
    return firstName;
  }

  @Override public String getLastName()
  {
    return lastName;
  }

  public String getPassword()
  {
    return password;
  }

  public int getID()
  {
    return id;
  }

  public void setFirstName(String firstName)
  {
    this.firstName = firstName;
  }

  public void setLastName(String lastName)
  {
    this.lastName = lastName;
  }

  public void setPassword(String password)
  {
    this.password = password;
  }

  public String getType()
  {
    return "admin";
  }



}