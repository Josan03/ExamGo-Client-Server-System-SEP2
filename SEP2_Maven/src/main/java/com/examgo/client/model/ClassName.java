package com.examgo.client.model;

import java.io.Serializable;

/**
 * A class that stores information about a classname
 *
 * @author Mihai Mihaila && Cristian Josan
 * @version 1.0
 */
public class ClassName implements Serializable
{
  private String className;

  private Account teacher;

  /**
   * One-argument constructor initializing information for ClassName object
   *
   * @param className class name to initialize
   */
  public ClassName(String className)
  {
    this.className = className;
  }

  /**
   * Two-argument constructor initializing information for ClassName object
   *
   * @param className class name to initialize
   * @param teacher   teacher to initialize
   */
  public ClassName(String className, Account teacher)
  {
    this.className = className;
    this.teacher = teacher;
  }

  /**
   * Sets the class name characteristic
   *
   * @param className used to set the class name characteristic
   */
  public void setClassName(String className)
  {
    this.className = className;
  }

  /**
   * Sets the teacher characteristic
   *
   * @param teacher used to set the teacher characteristic
   */
  public void setTeacher(Account teacher)
  {
    this.teacher = teacher;
  }

  /**
   * Returns the teacher characteristic
   *
   * @return a Account value which represents the teacher
   */
  public Account getTeacher()
  {
    return teacher;
  }

  /**
   * Returns the className characteristic
   *
   * @return a String value which represents the class name
   */
  public String getClassName()
  {
    return className;
  }

  @Override public String toString()
  {
    return getClassName();
  }
}
