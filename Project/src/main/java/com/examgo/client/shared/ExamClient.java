package com.examgo.client.shared;

import com.examgo.client.model.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * An interface that represents the structure of a client
 * All the methods bellow call other methods from sql classes
 *
 * @author Cristian Josan && Dan Turcan && Mihai Mihaila && Michael Leo
 * @version 1.0
 */
public interface ExamClient extends Remote
{
  /**
   * Returns an Account object which connected to the server
   *
   * @param id an int value is used for the username to connect
   * @param password a String value is used for the password to connect
   * @return an Account object which was connected
   * @throws RemoteException
   */
  Account connect(int id, String password) throws RemoteException;

  /**
   * Creates a test
   *
   * @param test a Test object is used to store
   * @throws RemoteException
   */
  void addTest(Test test) throws RemoteException;

  /**
   * Creates a session
   *
   * @param test a Test object is used to create a session
   * @throws RemoteException
   */
  void addSession(Test test) throws RemoteException;

  /**
   * Creates a question
   *
   * @param test a Test object is used to create a question
   * @param question a Question object is used to create a question
   * @throws RemoteException
   */
  void addQuestion(Test test, Question question) throws RemoteException;

  /**
   * Removes the test
   *
   * @param test a Test object is used to remove the test
   * @return a boolean value, if the test was removed - true, else - false
   * @throws RemoteException
   */
  boolean removeTest(Test test) throws RemoteException;

  /**
   * Stops the session
   *
   * @param id an int value is used to stop the session
   * @return a boolean value, if the session was stopped - true, else - false
   * @throws RemoteException
   */
  boolean stopSession(int id) throws RemoteException;

  /**
   * Connects the student to a session
   *
   * @param sessionId an int value is used to connect to a specific session
   * @param studentId an int value is used to connect a specific student to a session
   * @return a Session object to which someone connected
   * @throws RemoteException
   */
  Session connectSession(int sessionId, int studentId) throws RemoteException;

  /**
   * Gets all the sessions for a student
   *
   * @param studentId an int value is used for a specific student
   * @return an ArrayList of all the sessions which the student connected to
   * @throws RemoteException
   */
  ArrayList<StudentSession> getSessionsByStudent(int studentId)
      throws RemoteException;

  /**
   * Sends the answers to the questions for a session
   *
   * @param sessionId an int value is used for a specific session to send the answers to
   * @param answers an ArrayList of all the answers to the questions
   * @throws RemoteException
   */
  void sendAnswers(int sessionId, ArrayList<Answer> answers, int studentID)
      throws RemoteException;

  /**
   * Removes the question
   *
   * @param question a Question object is used to remove the question
   * @return a boolean value, if the question was removed - true, else - false
   * @throws RemoteException
   */
  boolean removeQuestion(Question question) throws RemoteException;

  /**
   * Gets all the tests for a teacher
   *
   * @param teacher a Account object is used for a specific teacher
   * @return an ArrayList of all the tests which a teacher created
   * @throws RemoteException
   */
  ArrayList<Test> getTestsByTeacher(Account teacher) throws RemoteException;

  /**
   * Gets all the tests
   *
   * @return an ArrayList of all the tests created
   * @throws RemoteException
   */
  ArrayList<Test> getAllTests() throws RemoteException;

  /**
   * Gets all the sessions for a teacher
   *
   * @param teacher a Account object is used for a specific teacher
   * @return an ArrayList of all the sessions which a teacher started
   * @throws RemoteException
   */
  ArrayList<Session> getSessionsByTeacher(Account teacher)
      throws RemoteException;

  /**
   * Gets all the sessions
   *
   * @return an ArrayList of all the sessions started
   * @throws RemoteException
   */
  ArrayList<Session> getAllSessions() throws RemoteException;

  /**
   * Gets all the classes for a teacher
   *
   * @param teacher_id an int value is used for a specific teacher
   * @return an ArrayList of all the classes that a teacher has
   * @throws RemoteException
   */
  ArrayList<ClassName> getClassesByTeacher(int teacher_id)
      throws RemoteException;

  /**
   * Gets all the students for a teacher
   *
   * @param teacher_id an int value is used for a specific teacher
   * @return an ArrayList of all the students that a teacher teach
   * @throws RemoteException
   */
  ArrayList<StudentAccount> getStudentsByTeacher(int teacher_id)
      throws RemoteException;

  /**
   * Gets all the students which connected to a session
   *
   * @param session_id an int value is used for a specific session
   * @return an ArrayList of all the students which connected to the specific session
   * @throws RemoteException
   */
  ArrayList<StudentAccount> getStudentsBySession(int session_id)
      throws RemoteException;

  /**
   * Gets all the answers of a student for a session
   *
   * @param student_id an int value is used for a specific student
   * @param session_id an int value is used for a specific session
   * @return an ArrayList of all the answers of a specific student for a specific session
   * @throws RemoteException
   */
  ArrayList<Answer> getAnswersByStudentSession(int student_id,
      int session_id) throws RemoteException;

  /**
   * Sets points for an answers
   *
   * @param answer_id an int value is used for a specific answer
   * @param points an int value is used to set points for an answer
   * @throws RemoteException
   */
  void updatePoints(int answer_id, int points)
      throws RemoteException;

  /**
   * Sets grade for a student which connected to a session
   *
   * @param student_id an int value is used for a specific student
   * @param session_id an int value is used for a specific session
   * @param grade an int value is used to set the grade for a specific student which connected to a specific session
   * @throws RemoteException
   */
  void updateGrade(int student_id, int session_id, int grade)
      throws RemoteException;

  /**
   * Gets the grade of a student for a session
   *
   * @param student_id an int value is used for a specific student
   * @param session_id an int value is used for a specific session
   * @return an int value of the grade of a student which connected to the session
   * @throws RemoteException
   */
  int getGradeByStudentSession(int student_id, int session_id)
      throws RemoteException;

  /**
   * Edits the existing test
   *
   * @param test a Test object is used to update
   * @param question a Question object is used in case of adding a new question to the test or editing the existing one
   * @throws RemoteException
   */
  void updateTest(Test test, Question question) throws RemoteException;

  /**
   * Creates a class
   *
   * @param id a String value is used to create the unique name of a class
   * @param teacher an Account object is used to create a class which a teacher has
   * @throws RemoteException
   */
  void addClassName(String id, Account teacher) throws RemoteException;

  /**
   * Creates a student
   *
   * @param firstName a String value to create the student's first name
   * @param lastName a String value to create the student's last name
   * @param password a String value to create the student's password
   * @param class_id a String value to set the student's class
   * @return a StudentAccount object which was created
   * @throws RemoteException
   */
  StudentAccount addSingleStudent(String firstName, String lastName, String password, String class_id) throws RemoteException;

  /**
   * Creates multiple students
   *
   * @param students an ArrayList of all the students with their properties to create
   * @return an ArrayList of all the students which were created
   * @throws RemoteException
   */
  ArrayList<StudentAccount> addMultipleStudents(ArrayList<StudentAccount> students) throws RemoteException;

  /**
   * Creates a teacher
   *
   * @param firstName a String value to create teacher's first name
   * @param lastName a String value to create teacher's last name
   * @param password a String value to create teacher's password
   * @return a Account object which was created
   * @throws RemoteException
   */
  Account addSingleTeacher(String firstName, String lastName, String password) throws RemoteException;

  /**
   * Creates multiple teachers
   *
   * @param teachers an ArrayList of all the teachers with their properties to create
   * @return an ArrayList of all the teachers which were created
   * @throws RemoteException
   */
  ArrayList<Account> addMultipleTeachers(ArrayList<Account> teachers) throws RemoteException;

  /**
   * Removes the teacher
   *
   * @param teacher a Account object to remove a specific teacher
   * @return a boolean value, if the teacher was removed - true, else - false
   * @throws RemoteException
   */
  boolean removeTeacher(Account teacher) throws RemoteException;

  /**
   * Removes the student
   *
   * @param student a StudentAccount object to remove a specific student
   * @return a boolean value, if the student was removed - true, else - false
   * @throws RemoteException
   */
  boolean removeStudent(StudentAccount student) throws RemoteException;

  /**
   * Removes the class
   *
   * @param class_id a String value to remove a specific class
   * @return a boolean value, if the class was removed - true, else - false
   * @throws RemoteException
   */
  boolean removeClass(String class_id) throws RemoteException;

  /**
   * Sets the student with new properties
   *
   * @param student a StudentAccount object to edit the existing student
   * @param class_id a String value to edit the existing student's class
   * @throws RemoteException
   */
  void updateStudent(StudentAccount student, String class_id) throws RemoteException;

  /**
   * Sets the teacher with new properties
   *
   * @param teacher a Account object to edit the existing teacher
   * @throws RemoteException
   */
  void updateTeacher(Account teacher) throws RemoteException;

  /**
   * Sets the class with new properties
   *
   * @param oldId a String value for the old class name
   * @param id a String value for the new class name
   * @param teacher a Account object to set the teacher's class
   * @throws RemoteException
   */
  void updateClassName(String oldId, String id, Account teacher) throws RemoteException;

  /**
   * Gets all the classes
   *
   * @return an ArrayList of all the classes that exists
   * @throws RemoteException
   */
  ArrayList<ClassName> getAllClassNames() throws RemoteException;

  /**
   * Gets all the teachers
   *
   * @return an ArrayList of all the teachers that exists
   * @throws RemoteException
   */
  ArrayList<Account> getAllTeachers() throws RemoteException;

  /**
   * Gets all the students
   *
   * @return an ArrayList of all the students that exists
   * @throws RemoteException
   */
  ArrayList<StudentAccount> getAllStudents() throws RemoteException;
}
