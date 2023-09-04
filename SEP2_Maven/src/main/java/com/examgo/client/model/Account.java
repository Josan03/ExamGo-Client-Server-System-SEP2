package com.examgo.client.model;

/**
 * An interface that represents the basic structure of an account
 *
 * @author Mihai Mihaila
 * @version 1.0
 */
public interface Account
{
  /**
   * Returns the first name characteristic
   *
   * @return a string value which represents the first name
   */
  String getFirstName();

  /**
   * Returns the last name characteristic
   *
   * @return a string value which represents the last name
   */
  String getLastName();

  /**
   * Returns the password characteristic
   *
   * @return a string value which represents the password
   */
  String getPassword();

  /**
   * Returns the id characteristic
   *
   * @return an int value which represents the id
   */
  int getID();

  /**
   * Sets the first name characteristic
   *
   * @param fn is used to set the first name
   */
  void setFirstName(String fn);

  /**
   * Sets the last name characteristic
   *
   * @param ln is used to set the last name
   */
  void setLastName(String ln);

  /**
   * Sets the password characteristic
   *
   * @param pass is used to set the password
   */
  void setPassword(String pass);

  /**
   * Returns the type of the account
   *
   * @return a String value which represents the type of the account
   */
  String getType();

}
