package com.examgo.server;

import com.examgo.client.model.*;
import com.examgo.client.shared.ExamClient;
import com.examgo.server.dao.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * ExamImplementation is the class that communicates with the client and the server.
 *
 * @author Cristian Josan
 * @version 1.0
 */
public class ExamImplementation extends UnicastRemoteObject
    implements ExamClient
{
  private final AdministratorDao administratorDao;
  private final TeacherDao teacherDao;
  private final StudentDao studentDao;
  private final TestDao testDao;
  private final SessionDao sessionDao;

  /**
   * Five-argument constructor initializing information for ExamImplementation
   *
   * @param administratorDao sql interface to initialize a new sql driver for administrator
   * @param teacherDao       sql interface to initialize a new sql driver for teacher
   * @param studentDao       sql interface to initialize a new sql driver for student
   * @param testDao          sql interface to initialize a new sql driver for test
   * @param sessionDao       sql interface to initialize a new sql driver for session
   * @throws RemoteException
   */
  public ExamImplementation(AdministratorDao administratorDao,
      TeacherDao teacherDao, StudentDao studentDao, TestDao testDao,
      SessionDao sessionDao) throws RemoteException
  {
    this.administratorDao = administratorDao;
    this.teacherDao = teacherDao;
    this.studentDao = studentDao;
    this.testDao = testDao;
    this.sessionDao = sessionDao;
  }

  @Override public Account connect(int id, String password)
      throws RemoteException
  {
    System.out.println(id + " " + password);
    if (administratorDao.getAdministrator(id) != null)
    {
      if (password.equals(administratorDao.getAdministrator(id).getPassword()))
      {
        System.out.println("LOOOOL");
        return administratorDao.getAdministrator(id);
      }
    }

    if (teacherDao.getTeacher(id) != null)
    {
      if (password.equals(teacherDao.getTeacher(id).getPassword()))
      {
        return teacherDao.getTeacher(id);
      }
    }

    if (studentDao.getStudent(id) != null)
    {
      if (password.equals(studentDao.getStudent(id).getPassword()))
      {
        return studentDao.getStudent(id);
      }
    }

    return null;
  }

  @Override public void addTest(Test test) throws RemoteException
  {
    if (test != null)
    {
      testDao.storeTest(test);
    }
  }

  @Override public void addSession(Test test) throws RemoteException
  {
    if (test != null)
    {
      sessionDao.createSession(test);
    }
  }

  @Override public void addQuestion(Test test, Question question)
      throws RemoteException
  {
    try
    {
      testDao.createQuestion(test, question.getQuestion(), question.getTime(),
          question.getPoints());
    }
    catch (SQLException e)
    {
      throw new RuntimeException(e);
    }
  }

  @Override public boolean removeTest(Test test) throws RemoteException
  {
    return testDao.deleteTest(test);
  }

  @Override public boolean stopSession(int id) throws RemoteException
  {
    return sessionDao.stopSession(id);
  }

  @Override public Session connectSession(int sessionId, int studentId)
      throws RemoteException
  {
    return studentDao.connectSession(sessionId, studentId);
  }

  @Override public ArrayList<StudentSession> getSessionsByStudent(int studentId)
      throws RemoteException
  {
    return studentDao.getSessionsByStudent(studentId);
  }

  @Override public void sendAnswers(int sessionId, ArrayList<Answer> answers,
      int studentID) throws RemoteException
  {
    studentDao.sendAnswers(sessionId, answers, studentID);
  }

  @Override public boolean removeQuestion(Question question)
      throws RemoteException
  {
    return testDao.deleteQuestion(question.getId());
  }

  @Override public ArrayList<Test> getTestsByTeacher(Account teacher)
  {
    try
    {
      return testDao.getTestsByTeacher(teacher);
    }
    catch (RemoteException e)
    {
      throw new RuntimeException(e);
    }
  }

  @Override public ArrayList<Test> getAllTests()
  {
    try
    {
      return testDao.getAllTests();
    }
    catch (RemoteException e)
    {
      throw new RuntimeException(e);
    }
  }

  @Override public ArrayList<Session> getSessionsByTeacher(Account teacher)
      throws RemoteException
  {
    return sessionDao.getSessionsByTeacher(teacher);
  }

  @Override public ArrayList<Session> getAllSessions() throws RemoteException
  {
    return sessionDao.getAllSessions();
  }

  @Override public ArrayList<ClassName> getClassesByTeacher(int teacher_id)
      throws RemoteException
  {
    return teacherDao.getClassesByTeacher(teacher_id);
  }

  @Override public ArrayList<StudentAccount> getStudentsByTeacher(
      int teacher_id) throws RemoteException
  {
    return teacherDao.getStudentsByTeacher(teacher_id);
  }

  @Override public ArrayList<StudentAccount> getStudentsBySession(
      int session_id) throws RemoteException
  {
    return teacherDao.getStudentsBySession(session_id);
  }

  @Override public ArrayList<Answer> getAnswersByStudentSession(int student_id,
      int session_id) throws RemoteException
  {
    return teacherDao.getAnswersByStudentSession(student_id, session_id);
  }

  @Override public void updatePoints(int answer_id, int points)
      throws RemoteException
  {
    teacherDao.updatePoints(answer_id, points);
  }

  @Override public void updateGrade(int student_id, int session_id, int grade)
      throws RemoteException
  {
    teacherDao.updateGrade(student_id, session_id, grade);
  }

  @Override public int getGradeByStudentSession(int student_id, int session_id)
      throws RemoteException
  {
    return teacherDao.getGradeByStudentSession(student_id, session_id);
  }

  @Override public void updateTest(Test test, Question question)
      throws RemoteException
  {
    testDao.updateTest(test, question);
  }

  @Override public void addClassName(String id, Account teacher)
      throws RemoteException
  {
    administratorDao.addClassName(id, teacher);
  }

  @Override public StudentAccount addSingleStudent(String firstName,
      String lastName, String password, String class_id) throws RemoteException
  {
    return administratorDao.addSingleStudent(firstName, lastName, password,
        class_id);
  }

  @Override public ArrayList<StudentAccount> addMultipleStudents(
      ArrayList<StudentAccount> students) throws RemoteException
  {
    return administratorDao.addMultipleStudents(students);
  }

  @Override public Account addSingleTeacher(String firstName, String lastName,
      String password) throws RemoteException
  {
    return administratorDao.addSingleTeacher(firstName, lastName, password);
  }

  @Override public ArrayList<Account> addMultipleTeachers(
      ArrayList<Account> teachers) throws RemoteException
  {
    return administratorDao.addMultipleTeachers(teachers);
  }

  @Override public boolean removeTeacher(Account teacher) throws RemoteException
  {
    return administratorDao.removeTeacher(teacher);
  }

  @Override public boolean removeStudent(StudentAccount student)
      throws RemoteException
  {
    return administratorDao.removeStudent(student);
  }

  @Override public boolean removeClass(String class_id) throws RemoteException
  {
    return administratorDao.removeClass(class_id);
  }

  @Override public void updateStudent(StudentAccount student, String class_id)
      throws RemoteException
  {
    administratorDao.updateStudent(student, class_id);
  }

  @Override public void updateTeacher(Account teacher) throws RemoteException
  {
    administratorDao.updateTeacher(teacher);
  }

  @Override public void updateClassName(String oldId, String id,
      Account teacher) throws RemoteException
  {
    administratorDao.updateClassName(oldId, id, teacher);
  }

  @Override public ArrayList<ClassName> getAllClassNames()
      throws RemoteException
  {
    return administratorDao.getAllClassNames();
  }

  @Override public ArrayList<Account> getAllTeachers() throws RemoteException
  {
    return administratorDao.getAllTeachers();
  }

  @Override public ArrayList<StudentAccount> getAllStudents()
      throws RemoteException
  {
    return administratorDao.getAllStudents();
  }
}
