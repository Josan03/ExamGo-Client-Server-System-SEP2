package com.examgo.server.dao;

import com.examgo.client.model.Answer;
import com.examgo.client.model.ClassName;
import com.examgo.client.model.StudentAccount;
import com.examgo.client.model.Account;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * An interface that represents the structure of a teacher data object access
 *
 * @author Cristian Josan
 * @version 1.0
 */
public interface TeacherDao
{
  /**
   * Gets the teacher from the database
   *
   * @param id an int value is used to check if there is such teacher
   * @return a Account object which connected to the server
   * @throws RemoteException
   */
  Account getTeacher(int id) throws RemoteException;

  /**
   * Gets all the classes for a specified teacher from the database
   *
   * @param teacher_id an int value is used for a specific teacher to search by
   * @return an ArrayList of all the classes which were found in the database by a specific teacher
   * @throws RemoteException
   */
  ArrayList<ClassName> getClassesByTeacher(int teacher_id) throws RemoteException;

  /**
   * Gets all the students for a specified teacher from the database
   *
   * @param teacher_id an int value is used for a specific teacher to search by
   * @return an ArrayList of all the students which were found in the database by a specific teacher
   * @throws RemoteException
   */
  ArrayList<StudentAccount> getStudentsByTeacher(int teacher_id) throws RemoteException;

  /**
   * Gets all the students for a specified session from the database
   *
   * @param session_id an int value is used for a specific session to search by
   * @return an ArrayList of all the students which were found in the database by a specific session
   * @throws RemoteException
   */
  ArrayList<StudentAccount> getStudentsBySession(int session_id) throws RemoteException;

  /**
   * Gets all the answers for a specified student and session from the database
   *
   * @param student_id an int value is used for a specific student to search by
   * @param session_id an int value is used for a specific session to search by
   * @return an ArrayList of all the answers which were found in the database by a specific student and session
   * @throws RemoteException
   */
  ArrayList<Answer> getAnswersByStudentSession(int student_id, int session_id) throws RemoteException;

  /**
   * Updates the points for an answer in the database
   *
   * @param answer_id an int value is used for a specific answers to update
   * @param points an int value is used for points to update for an answer
   * @throws RemoteException
   */
  void updatePoints(int answer_id, int points) throws RemoteException;

  /**
   * Updates the grade for a student on a specified session in the database
   *
   * @param student_id an int value is used for a specific student to search by
   * @param session_id an int value is used for a specific session to search by
   * @param grade an int value is used for a grade to update for a student
   * @throws RemoteException
   */
  void updateGrade(int student_id, int session_id, int grade) throws RemoteException;

  /**
   * Gets the grade for a student on a specified session from the database
   *
   * @param student_id an int value is used for a specific student to search by
   * @param session_id an int value is used for a specific session to search by
   * @return an int value of the grade for a student
   * @throws RemoteException
   */
  int getGradeByStudentSession(int student_id, int session_id) throws RemoteException;
}
