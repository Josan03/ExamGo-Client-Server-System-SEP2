package com.examgo.client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * A class that stores information about a session
 *
 * @author Mihai Mihaila && Michael Leo
 * @version 1.0
 */
public class Session implements Serializable
{
  private int id;
  private Test test;
  private boolean status;

  /**
   * Two-argument constructor initializing information for Session object
   *
   * @param test   test to initialize
   * @param status status to initialize
   */
  public Session(Test test, boolean status)
  {
    this.id = -1;
    this.test = test;
    this.status = status;
  }

  /**
   * Three-argument constructor initializing information for Session object
   *
   * @param id     id to initialize
   * @param test   test to initialize
   * @param status status to initialize
   */
  public Session(int id, Test test, boolean status)
  {
    this.id = id;
    this.test = test;
    this.status = status;
  }

  /**
   * Returns the id characteristic
   *
   * @return an int value which represents the id
   */
  public int getId()
  {
    return id;

  }

  /**
   * Returns the test characteristic
   *
   * @return a Test value which represents the test
   */
  public Test getTest()
  {
    return test;
  }

  /**
   * Sets the id characteristic
   *
   * @param id used to set the id characteristic
   */
  public void setId(int id)
  {
    this.id = id;
  }

  /**
   * Sets the test characteristic
   *
   * @param test used to set the test characteristic
   */
  public void setTest(Test test)
  {
    this.test = test;
  }

  /**
   * Returns the status of the Session, which can be true / false
   *
   * @return a boolean value which represents the status of the Session
   */
  public boolean getStatus()
  {
    return status;
  }

  @Override public String toString()
  {
    return "Session{" + "id=" + id + '\'' + ", test=" + test + '}';
  }

  @Override public boolean equals(Object o)
  {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Session session = (Session) o;
    return id == session.id && Objects.equals(test, session.test);
  }

  @Override public int hashCode()
  {
    return Objects.hash(id, test);
  }
}
