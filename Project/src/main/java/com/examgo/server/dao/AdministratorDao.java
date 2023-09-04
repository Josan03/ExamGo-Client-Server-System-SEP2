package com.examgo.server.dao;

import com.examgo.client.model.Account;
import com.examgo.client.model.ClassName;
import com.examgo.client.model.StudentAccount;
import com.examgo.client.model.Account;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * An interface that represents the structure of an administrator data object access
 *
 * @author Cristian Josan
 * @version 1.0
 */
public interface AdministratorDao
{
  /**
   * Gets the administrator from the database
   *
   * @param id an int value is used to check if there is such administrator
   * @return an Account object which connected to the server
   * @throws RemoteException
   */
  Account getAdministrator(int id) throws RemoteException;

  /**
   * Stores a class to the database
   *
   * @param id a String value for the class name
   * @param teacher a Account object for the class to store the connection between teacher and class
   * @throws RemoteException
   */
  void addClassName(String id, Account teacher) throws RemoteException;

  /**
   * Stores a student to the database
   *
   * @param firstName a String value for the student's first name
   * @param lastName a String value for the student's last name
   * @param password a String value for the student's password
   * @param class_id a String value for the student's class
   * @return a StudentAccount object which was created
   * @throws RemoteException
   */
  StudentAccount addSingleStudent(String firstName, String lastName, String password, String class_id) throws RemoteException;

  /**
   * Stores multiple students to the database
   *
   * @param students an ArrayList of students with necessary properties as in {@link #addSingleStudent} method
   * @return an ArrayList with all the students that were created
   * @throws RemoteException
   */
  ArrayList<StudentAccount> addMultipleStudents(ArrayList<StudentAccount> students) throws RemoteException;

  /**
   * Stores a teacher to the database
   *
   * @param firstName a String value for the teacher's first name
   * @param lastName a String value for the teacher's last name
   * @param password a String value for the teacher's password
   * @return a Account object which was created
   * @throws RemoteException
   */
  Account addSingleTeacher(String firstName, String lastName, String password) throws RemoteException;

  /**
   * Stores multiple teachers to the database
   *
   * @param teachers an ArrayList of teachers with necessary properties as in {@link #addSingleTeacher} method
   * @return an ArrayList with all the teachers that were created
   * @throws RemoteException
   */
  ArrayList<Account> addMultipleTeachers(ArrayList<Account> teachers) throws RemoteException;

  /**
   * Sets a flag, status to false if the teacher is removed (it does not remove it from the database)
   *
   * @param teacher a Account object to remove the specific teacher
   * @return a boolean value, if the status was updated to false - true, else - false
   * @throws RemoteException
   */
  boolean removeTeacher(Account teacher) throws RemoteException;

  /**
   * Sets a flag, status to false if the student is removed (it does not remove it from the database)
   *
   * @param student a StudentAccount object to remove the specific student
   * @return a boolean value, if the status was updated to false - true, else - false
   * @throws RemoteException
   */
  boolean removeStudent(StudentAccount student) throws RemoteException;

  /**
   * Sets a flag, status to false if the class is removed (it does not remove it from the database)
   *
   * @param class_id a String value to remove the specific class
   * @return a boolean value, if the status was updated to false - true, else - false
   * @throws RemoteException
   */
  boolean removeClass(String class_id) throws RemoteException;

  /**
   * Updates the student in the database with the new properties
   *
   * @param student a StudentAccount object with all the new properties to update
   * @param class_id a String value for the new class from the existing ones
   * @throws RemoteException
   */
  void updateStudent(StudentAccount student, String class_id) throws RemoteException;

  /**
   * Updates the teacher in the database with the new properties
   *
   * @param teacher a Account object with all the new properties to update
   * @throws RemoteException
   */
  void updateTeacher(Account teacher) throws RemoteException;

  /**
   * Updates (the class name / the teacher for the class / both) in the database
   *
   * @param oldId a String value for the old name of the class
   * @param id a String value for the new name of the class to update with
   * @param teacher a Account object from the existing teachers to update with
   * @throws RemoteException
   */
  void updateClassName(String oldId, String id, Account teacher) throws RemoteException;

  /**
   * Gets all the classes from the database
   *
   * @return an ArrayList of all the classes that exists in the database
   * @throws RemoteException
   */
  ArrayList<ClassName> getAllClassNames() throws RemoteException;

  /**
   * Gets all the teachers from the database
   *
   * @return an ArrayList of all the teachers that exists in the database
   * @throws RemoteException
   */
  ArrayList<Account> getAllTeachers() throws RemoteException;

  /**
   * Gets all the students from the database
   *
   * @return an ArrayList of all the students that exists in the database
   * @throws RemoteException
   */
  ArrayList<StudentAccount> getAllStudents() throws RemoteException;
}
