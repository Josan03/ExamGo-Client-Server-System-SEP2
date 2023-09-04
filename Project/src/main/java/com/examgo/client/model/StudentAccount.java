package com.examgo.client.model;

/**
 * An interface that extends the Account interface and represents the structure of a student account
 *
 * @author Mihai Mihaila
 * @version 1.0
 */
public interface StudentAccount extends Account
{

  /**
   * Returns the className characteristic as a String
   *
   * @return a string value which represents the class name
   */
  String getClassname();

  /**
   * Sets the classname characteristic
   *
   * @param classname is used to set the last name
   */
  void setClassname(ClassName classname);

  /**
   * Returns the className characteristic
   *
   * @return a string value which represents the first name
   */
  ClassName getClassNameObj();
}
