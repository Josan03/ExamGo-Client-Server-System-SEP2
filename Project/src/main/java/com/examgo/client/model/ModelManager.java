package com.examgo.client.model;

import com.examgo.client.shared.ExamClient;
import com.examgo.client.utils.MyFileHandler;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;

/**
 * ModelManager is the class manages the state of the application, communicates with the server and the viewmodel.
 *
 * @author Mihai Mihaila && Dan Turcan && Cristian Josan && Michael Leo
 * @version 1.0
 */

public class ModelManager
{
  private static ModelManager instance;
  private ArrayList<Test> tests;
  private ArrayList<Session> sessions;
  private ArrayList<Question> questions;
  private ArrayList<ClassName> classNames;
  private ArrayList<Account> accounts;

  private ArrayList<Answer> answers;

  private PropertyChangeSupport support;

  private ExamClient server;

  private Test selectedTest;

  private Session selectedSession;
  private Account currentAccount;

  private StudentAccount selectedStudent;

  private ClassName selectedClass;

  private Account selectedTeacher;

  private int totalPoints;
  private int currentPoints;

  /**
   * One-argument constructor for ModelManager.
   *
   * @param server - ExamClient object that communicates with the server.
   */
  private ModelManager(ExamClient server)
  {
    this.tests = new ArrayList<>();
    this.sessions = new ArrayList<>();
    this.questions = new ArrayList<>();
    this.classNames = new ArrayList<>();
    this.accounts = new ArrayList<>();
    this.answers = new ArrayList<>();
    this.server = server;
    totalPoints = 0;
    currentPoints = 0;
    currentAccount = null;
    selectedTest = null;
    selectedSession = null;
    selectedStudent = null;
    selectedTeacher = null;
    support = new PropertyChangeSupport(this);
  }

  public static ModelManager getInstance(ExamClient server)
  {
    if (instance == null)
    {
      instance = new ModelManager(server);
    }
    return instance;
  }

  /**
   * Adds a new Test object
   *
   * @param title - the title of the new Test object
   */
  public void addTest(String title)
  {
    Test test = new Test(title, currentAccount);

    test.setQuestions(questions);
    try
    {
      server.addTest(test);
      support.firePropertyChange("tests", null, tests);
    }
    catch (RemoteException e)
    {
      throw new RuntimeException(e);
    }
    questions.clear();
  }

  /**
   * Sets the selected Test object.
   *
   * @param test - the Test object that has been selected.
   */
  public void setTest(Test test)
  {
    this.selectedTest = test;

    support.firePropertyChange("test", null, selectedTest);

  }

  /**
   * Sets the selected ClassName object.
   *
   * @param className - the ClassName object that has been selected.
   */
  public void setSelectedClass(ClassName className)
  {
    this.selectedClass = className;
    support.firePropertyChange("classname2", null, selectedClass);
  }

  /**
   * Clears the list of questions.
   */
  public void clearQuestions()
  {
    questions.clear();
  }

  /**
   * Creates a new Session object for the Test object that has been selected.
   */
  public void createSession()
  {
    try
    {
      server.addSession(selectedTest);
      if (currentAccount.getType().equals("teacher"))
      {
        support.firePropertyChange("sessions", null, null);
      }
      else
      {
        support.firePropertyChange("sessionsA", null, null);
      }
    }
    catch (RemoteException e)
    {
      throw new RuntimeException(e);
    }

  }

  /**
   * Stops the current session.
   *
   * @throws RuntimeException if a RemoteException is caught
   */
  public void stopSession()
  {
    try
    {
      server.stopSession(selectedSession.getId());
      if (currentAccount.getType().equals("teacher"))
      {
        support.firePropertyChange("sessions", null, null);
      }
      else
      {
        support.firePropertyChange("sessionsA", null, null);
      }
    }
    catch (RemoteException e)
    {
      throw new RuntimeException(e);
    }

  }

  /**
   * Returns an ArrayList that contains all sessions.
   *
   * @return an ArrayList of all the sessions
   * @throws RuntimeException if a RemoteException is caught
   */
  public ArrayList<Session> getAllSessions()
  {
    try
    {
      return server.getAllSessions();
    }
    catch (RemoteException e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Returns an ArrayList that contains all sessions for the current teacher that has logged in.
   *
   * @return an ArrayList of all the sessions for the current teacher that has logged in
   * @throws RuntimeException if a RemoteException is caught
   */
  public ArrayList<Session> getAllSessionsByTeacher()
  {
    try
    {
      return server.getSessionsByTeacher(currentAccount);
    }
    catch (RemoteException e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Sets the session that has been selected and fires a property change event called "cSession".
   *
   * @param session represents the selected session
   */
  public void setSelectedSession(Session session)
  {
    selectedSession = session;
    if (currentAccount.getType().equals("teacher"))
    {
      support.firePropertyChange("cSession", null, selectedSession);
      totalPoints = 0;
      for (int i = 0; i < session.getTest().getQuestions().size(); i++)
      {
        totalPoints += session.getTest().getQuestions().get(i).getPoints();
      }
    }
  }

  /**
   * Adds a new Question to the ArrayList of Question.
   *
   * @param time     the time
   * @param question the text of the question
   * @param points   the number of points
   */
  public void addQuestion(double time, String question, int points)
  {
    questions.add(new Question(time, question, points));
  }

  /**
   * Adds a new question to the current test and fires 2 property change events: "tests" and "test".
   *
   * @param time     the time
   * @param question the text of the question
   * @param points   the number of points
   * @throws RuntimeException if a RemoteException is caught
   */
  public void addQuestionSingle(double time, String question, int points)
  {
    try
    {
      server.addQuestion(selectedTest, new Question(time, question, points));
      support.firePropertyChange("tests", null, tests);
      ArrayList<Test> aux = getTestsT();
      for (int i = 0; i < aux.size(); i++)
      {
        if (aux.get(i).getId() == selectedTest.getId())
        {
          selectedTest = aux.get(i);
          break;
        }
      }
      support.firePropertyChange("test", null, selectedTest);
    }
    catch (RemoteException e)
    {
      throw new RuntimeException(e);
    }

  }

  /**
   * Removes the selected test and fires a property change event based on the type of the currentAccount.
   * IF currentAccount instanceOf Teacher -> tests
   * IF currentAccount instanceOf Administrator -> testsA
   *
   * @return true if the test was removed, if not -> false
   * @throws RuntimeException if a RemoteException is caught
   */
  public boolean removeTest()
  {
    try
    {
      boolean ok = server.removeTest(selectedTest);
      if (currentAccount.getType().equals("teacher"))
      {
        support.firePropertyChange("tests", null, tests);
      }
      else
        support.firePropertyChange("testsA", null, tests);
      return ok;
    }
    catch (RemoteException e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Removes a question and fires 2 property changes: "tests" and "test".
   *
   * @param question the question that has to be removed
   * @return true if the question was removed, if not -> false
   * @throws RuntimeException if there is a RemoteException
   */
  public boolean removeQuestion(Question question)
  {
    try
    {
      boolean ok = server.removeQuestion(question);
      support.firePropertyChange("tests", null, tests);
      ArrayList<Test> aux = getTestsT();
      for (int i = 0; i < aux.size(); i++)
      {
        if (aux.get(i).getId() == selectedTest.getId())
        {
          selectedTest = aux.get(i);
          break;
        }
      }
      support.firePropertyChange("test", null, selectedTest);
      return ok;
    }
    catch (RemoteException e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Returns the tests for the teacher that has logged in.
   *
   * @return ArrayList of Test for the current teacher
   * @throws RuntimeException if there is a RemoteException
   */
  public ArrayList<Test> getTestsT()
  {
    try
    {
      return server.getTestsByTeacher(currentAccount);
    }
    catch (RemoteException e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Returns the sessions.
   *
   * @return ArrayList of Session
   */
  public ArrayList<Session> getSessions()
  {
    return sessions;
  }

  /**
   * Sets the sessions.
   *
   * @param sessions the sessions to be set
   */
  public void setSessions(ArrayList<Session> sessions)
  {
    this.sessions = sessions;
  }

  /**
   * Returns the class names.
   *
   * @return ArrayList of ClassName
   */
  public ArrayList<ClassName> getClassNames()
  {
    return classNames;
  }

  /**
   * Sets the class names.
   *
   * @param classNames the class names to be set
   */
  public void setClassNames(ArrayList<ClassName> classNames)
  {
    this.classNames = classNames;
  }

  /**
   * Log in using the ID and password.
   *
   * @param id       the ID of the account
   * @param password the password of the account
   * @return the Account that has been connected to
   * @throws RuntimeException if there is a RemoteException
   */
  public Account connect(int id, String password)
  {
    try
    {
      currentAccount = server.connect(id, password);
      System.out.println(currentAccount.getType());
      if (currentAccount != null)
        if (currentAccount.getType().equals("teacher"))
          support.firePropertyChange("teacher", null, currentAccount);
        else if (currentAccount.getType().equals("student"))
          support.firePropertyChange("student", null, currentAccount);
        else if (currentAccount.getType().equals("admin"))
          support.firePropertyChange("admin", null, currentAccount);
      return currentAccount;
    }
    catch (RemoteException e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Adds a property change listener.
   *
   * @param propertyName the name of the property to listen for changes
   * @param listener     the listener
   */
  public void addPropertyChangeListener(String propertyName,
      PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(propertyName, listener);
  }

  /**
   * Removes a property change listener.
   *
   * @param propertyName the name of the property to stop listening for changes
   * @param listener     the listener
   */
  public void removePropertyChangeListener(String propertyName,
      PropertyChangeListener listener)
  {
    support.removePropertyChangeListener(propertyName, listener);
  }

  /**
   * Returns the current account.
   *
   * @return the current account
   */
  public Account getCurrentAccount()
  {
    return currentAccount;
  }

  /**
   * Edits a test and fires 2 property changes: "tests" and "test".
   *
   * @param index    the index of the question
   * @param time     the new time
   * @param question the new question text
   * @param points   the new points value
   * @param subject  the new subject for the test
   * @throws RuntimeException if there is a RemoteException
   */
  public void editTest(int index, double time, String question, int points,
      String subject)
  {
    Test test = new Test(selectedTest.getId(), subject,
        selectedTest.getTeacher());
    try
    {
      server.updateTest(test, new Question(index, time, question, points));
      support.firePropertyChange("tests", null, tests);

      ArrayList<Test> aux = getTestsT();

      for (int i = 0; i < aux.size(); i++)
      {
        if (aux.get(i).getId() == selectedTest.getId())
        {
          selectedTest = aux.get(i);
          break;
        }
      }

      support.firePropertyChange("test", null, selectedTest);
    }
    catch (RemoteException e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Connects to a session with the specified session ID and the account ID.
   *
   * @param session_id the ID of the session
   * @return true if it has connected, if not -> false
   * @throws RuntimeException if a RemoteException is caught
   */
  public boolean connectSession(int session_id)
  {
    try
    {
      selectedSession = server.connectSession(session_id,
          currentAccount.getID());
      if (selectedSession != null)
      {
        support.firePropertyChange("session", null, selectedSession);
        return true;
      }
      else
      {
        return false;
      }
    }
    catch (RemoteException e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Adds an answer to the ArrayList of Answer.
   *
   * @param question_id the ID of the question
   * @param text        the answer text
   */
  public void addAnswer(int question_id, String text)
  {
    Answer answer = new Answer(question_id, text);
    answers.add(answer);
  }

  /**
   * Sends the answers, based on the session and clears the ArrayList of answer
   *
   * @throws RuntimeException if a RemoteException is caught
   */
  public void sendAnswers()
  {
    try
    {
      server.sendAnswers(selectedSession.getId(), answers,
          currentAccount.getID());
      support.firePropertyChange("student-sessions", null, null);
      answers.clear();
    }
    catch (RemoteException e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Returns the student sessions based on the logged account.
   *
   * @return an ArrayList of StudentSession
   * @throws RuntimeException if a RemoteException is caught
   */
  public ArrayList<StudentSession> getStudentSessions()
  {
    try
    {
      return server.getSessionsByStudent(currentAccount.getID());

    }
    catch (RemoteException e)
    {
      throw new RuntimeException(e);
    }

  }

  /**
   * Returns the classes based on the teacher account that has logged in.
   *
   * @return an ArrayList of ClassName
   * @throws RuntimeException if a RemoteException is caught
   */
  public ArrayList<ClassName> getClassesByTeacher()
  {
    try
    {
      return server.getClassesByTeacher(currentAccount.getID());
    }
    catch (RemoteException e)
    {
      throw new RuntimeException(e);
    }

  }

  /**
   * Returns the students based on the teacher account that has logged in.
   *
   * @return an ArrayList of Student
   * @throws RuntimeException if a RemoteException occurs
   */
  public ArrayList<StudentAccount> getStudentsByTeacher()
  {
    try
    {
      return server.getStudentsByTeacher(currentAccount.getID());
    }
    catch (RemoteException e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Returns the students associated with the currently selected session.
   *
   * @return an ArrayList of Student
   * @throws RuntimeException if a RemoteException occurs
   */
  public ArrayList<StudentAccount> getStudentsBySession()
  {
    try
    {
      return server.getStudentsBySession(selectedSession.getId());
    }
    catch (RemoteException e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Returns the answers based on the Student and the Session, calculates the current points of the student for the session.
   *
   * @return an ArrayList of Answer
   * @throws RuntimeException if a RemoteException occurs
   */
  public ArrayList<Answer> getAnswersByStudentSession()
  {
    try
    {
      ArrayList<Answer> result = server.getAnswersByStudentSession(
          selectedStudent.getID(), selectedSession.getId());
      support.firePropertyChange("grade", null, null);
      currentPoints = 0;
      for (int i = 0; i < result.size(); i++)
      {
        currentPoints += result.get(i).getPoints();
      }
      return result;
    }
    catch (RemoteException e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Updates the points of an answer and updates the selected session.
   *
   * @param answer_id the ID of the answer
   * @param points    the number of points
   */
  public void updatePoints(int answer_id, int points)
  {
    try
    {
      server.updatePoints(answer_id, points);
      ArrayList<Session> result = getAllSessionsByTeacher();

      for (int i = 0; i < result.size(); i++)
      {
        if (result.get(i).getId() == selectedSession.getId())
        {
          selectedSession = result.get(i);
          break;
        }
      }
      // support.firePropertyChange("points", null, selectedSession);
    }
    catch (RemoteException e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Updates the grade for the selected student and session.
   *
   * @param grade the new grade for the selected session and student
   */
  public void updateGrade(int grade)
  {
    try
    {
      server.updateGrade(selectedStudent.getID(), selectedSession.getId(),
          grade);

      int result = server.getGradeByStudentSession(selectedStudent.getID(),
          selectedSession.getId());
      support.firePropertyChange("grade2", null, result);
    }
    catch (RemoteException e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Returns the grade for the selected student and session.
   *
   * @return the grade
   */
  public int getGradeByStudentSession()
  {
    try
    {
      return server.getGradeByStudentSession(selectedStudent.getID(),
          selectedSession.getId());
    }
    catch (RemoteException e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Sets the selected student.
   *
   * @param student the student
   */
  public void setSelectedStudent(StudentAccount student)
  {
    this.selectedStudent = student;
    if (currentAccount.getType().equals("teacher"))
    {
      support.firePropertyChange("changedStudent", null, null);
    }
    else
    {
      support.firePropertyChange("changedStudentA", null, selectedStudent);
    }
  }

  /**
   * Returns the answer for a question ID.
   *
   * @param question_id the ID of the question
   * @return the answer, or an empty string if no answer is found
   */
  public String getAnswerByQuestion(int question_id)
  {
    ArrayList<Answer> result = getAnswersByStudentSession();
    for (int i = 0; i < result.size(); i++)
    {
      if (result.get(i).getQuestion_id() == question_id)
      {
        return result.get(i).getAnswer();

      }
    }
    return "";
  }

  /**
   * Returns the number of points for a question ID.
   *
   * @param question_id the ID of the question
   * @return the number of points for question, or -1 if no points are found
   */
  public int getPointsByQuestion(int question_id)
  {
    ArrayList<Answer> result = getAnswersByStudentSession();
    for (int i = 0; i < result.size(); i++)
    {
      if (result.get(i).getQuestion_id() == question_id)
      {
        return result.get(i).getPoints();

      }
    }
    return -1;
  }

  /**
   * Returns the answer ID for a question ID.
   *
   * @param question_id the ID of the question
   * @return the answer ID, or -1 if no answer ID is found
   */
  public int getAnswerIDByQuestion(int question_id)
  {
    ArrayList<Answer> result = getAnswersByStudentSession();
    for (int i = 0; i < result.size(); i++)
    {
      if (result.get(i).getQuestion_id() == question_id)
      {
        return result.get(i).getAnswer_id();
      }
    }
    return -1;
  }

  /**
   * Returns the total points
   *
   * @return the total points
   */
  public int getTotalPoints()
  {
    return totalPoints;
  }

  /**
   * Returns the current points
   *
   * @return the current points
   */
  public int getCurrentPoints()
  {
    return currentPoints;
  }

  /**
   * Returns all tests
   *
   * @return an ArrayList of Test
   */
  public ArrayList<Test> getAllTests()
  {
    try
    {
      return server.getAllTests();
    }
    catch (RemoteException e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Adds a class name.
   *
   * @param id      the ID of the class
   * @param teacher the teacher
   */
  public void addClassName(String id, Account teacher)
  {
    try
    {
      server.addClassName(id, teacher);
      support.firePropertyChange("classname", null, null);
    }
    catch (RemoteException e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * An algorithm that generates a random password.
   *
   * @return the password
   */
  public String generatePassword()
  {
    Random random = new Random();
    String pass = "";

    for (int i = 0; i < 3; i++)
    {
      pass += Character.toString((char) random.nextInt(48, 58));
      pass += Character.toString((char) random.nextInt(65, 90));
      pass += Character.toString((char) random.nextInt(97, 123));
    }

    return pass;
  }

  /**
   * Adds a single student
   *
   * @param firstName the first name
   * @param lastName  the last name
   * @param class_id  the ID of the class
   */
  public void addSingleStudent(String firstName, String lastName,
      String class_id)
  {
    try
    {
      String pass = generatePassword();
      StudentAccount result = server.addSingleStudent(firstName, lastName, pass,
          class_id);
      String txt =
          result.getID() + " " + pass + "  ( " + firstName + " " + lastName
              + " )";
      MyFileHandler.writeToTextFile(firstName + "_" + lastName + "_result.txt",
          txt);
      support.firePropertyChange("classname", null, null);
    }
    catch (RemoteException e)
    {
      throw new RuntimeException(e);
    }
    catch (FileNotFoundException e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Adds multiple students from a file.
   *
   * @param fileName the name of the file containing student data
   */
  public void addMultipleStudents(String fileName)
  {
    ArrayList<StudentAccount> students = new ArrayList<>();

    try
    {
      String[] data = MyFileHandler.readArrayFromTextFile(fileName);
      MyFileHandler.writeToTextFile(fileName + "_results.txt",
          "--- RESULTS: ---");
      for (int i = 1; i < data.length; i++)
      {
        String[] data2 = data[i].split("\t");
        String pass = generatePassword();

        StudentAccount student = new Student(data2[0], data2[1], pass,
            new ClassName(data2[2]), -5);

        students.add(student);
      }

    }
    catch (FileNotFoundException e)
    {
      throw new RuntimeException(e);
    }
    try
    {
      ArrayList<StudentAccount> result = server.addMultipleStudents(students);

      MyFileHandler.writeToTextFile(fileName + "_results.txt", "Results:");
      for (int i = 0; i < result.size(); i++)
      {
        MyFileHandler.appendToTextFile(fileName + "_results.txt",
            result.get(i).getID() + " " + result.get(i).getPassword() + " ( "
                + result.get(i).toString() + " ) ");
      }

    }
    catch (RemoteException e)
    {
      throw new RuntimeException(e);
    }
    catch (FileNotFoundException e)
    {
      throw new RuntimeException(e);
    }
    support.firePropertyChange("classname", null, null);

  }

  /**
   * Adds a single teacher
   *
   * @param firstName The first name of the teacher.
   * @param lastName  The last name of the teacher.
   */
  public void addSingleTeacher(String firstName, String lastName)
  {

    try
    {
      String pass = generatePassword();
      Account result = server.addSingleTeacher(firstName, lastName, pass);
      String txt =
          result.getID() + " " + pass + "  ( " + firstName + " " + lastName
              + " )";
      MyFileHandler.writeToTextFile(firstName + "_" + lastName + "_result.txt",
          txt);
      support.firePropertyChange("classname", null, null);

    }
    catch (RemoteException e)
    {
      throw new RuntimeException(e);
    }
    catch (FileNotFoundException e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Adds multiple teachers to the server from a text file.
   *
   * @param fileName The name of the text file containing the teacher data.
   */
  public void addMultipleTeachers(String fileName)
  {
    ArrayList<Account> teachers = new ArrayList<>();
    try
    {
      String[] data = MyFileHandler.readArrayFromTextFile(fileName);
      MyFileHandler.writeToTextFile(fileName + "_results.txt",
          "--- RESULTS: ---");
      for (int i = 1; i < data.length; i++)
      {
        String[] data2 = data[i].split("\t");
        String pass = generatePassword();
        Account teacher = new Teacher(data2[0], data2[1], pass, -5);
        teachers.add(teacher);
      }

    }
    catch (FileNotFoundException e)
    {
      throw new RuntimeException(e);
    }

    try
    {
      ArrayList<Account> result = server.addMultipleTeachers(teachers);

      MyFileHandler.writeToTextFile(fileName + "_results.txt", "Results:");
      for (int i = 0; i < result.size(); i++)
      {
        MyFileHandler.appendToTextFile(fileName + "_results.txt",
            result.get(i).getID() + " " + result.get(i).getPassword() + " ( "
                + result.get(i).toString() + " ) ");
      }
      support.firePropertyChange("classname", null, null);
    }
    catch (RemoteException e)
    {
      throw new RuntimeException(e);
    }
    catch (FileNotFoundException e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Removes the selected teacher
   */
  public void removeTeacher()
  {
    try
    {
      server.removeTeacher(selectedTeacher);
      support.firePropertyChange("classname", null, null);
    }
    catch (RemoteException e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Removes the selected student
   */
  public void removeStudent()
  {
    try
    {
      server.removeStudent(selectedStudent);
      support.firePropertyChange("changedStudentA", null, selectedStudent);
      support.firePropertyChange("classname", null, null);
    }
    catch (RemoteException e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Removes the selected class
   */
  public void removeClass()
  {
    try
    {
      server.removeClass(selectedClass.getClassName());
      support.firePropertyChange("classname", null, null);
    }
    catch (RemoteException e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Updates a student.
   *
   * @param id        The ID of the student
   * @param firstName The first name
   * @param lastName  The last name
   * @param password  The new password
   * @param class_id  The ID of the class
   */
  public void updateStudent(int id, String firstName, String lastName,
      String password, String class_id)
  {
    StudentAccount student = new Student(firstName, lastName, password, null, id);
    try
    {
      server.updateStudent(student, class_id);
      support.firePropertyChange("classname", null, null);
    }
    catch (RemoteException e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Updates a teacher with the given information
   *
   * @param id        The id of the teacher
   * @param firstName The first name of the teacher
   * @param lastName  The last name of the teacher
   * @param password  The password of the teacher
   */
  public void updateTeacher(int id, String firstName, String lastName,
      String password)
  {
    Account teacher = new Teacher(firstName, lastName, password, id);
    {
      try
      {
        server.updateTeacher(teacher);
        support.firePropertyChange("classname", null, null);
      }
      catch (RemoteException e)
      {
        throw new RuntimeException(e);
      }
    }
  }

  /**
   * Updates a class name with the given information
   *
   * @param oldId   The old id of the class name
   * @param id      The new id of the class name
   * @param teacher The teacher of the class name
   */
  public void updateClassName(String oldId, String id, Account teacher)
  {
    try
    {
      server.updateClassName(oldId, id, teacher);
      support.firePropertyChange("classname", null, null);
    }
    catch (RemoteException e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Returns am arraylist of all class names
   *
   * @return An arraylist of all class names
   */
  public ArrayList<ClassName> getAllClassNames()
  {
    try
    {
      return server.getAllClassNames();
    }
    catch (RemoteException e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Returns am arraylist of all teachers
   *
   * @return An arraylist of all teachers
   */
  public ArrayList<Account> getAllTeachers()
  {
    try
    {
      return server.getAllTeachers();
    }
    catch (RemoteException e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Returns am arraylist of all students
   *
   * @return An arraylist of all students
   */
  public ArrayList<StudentAccount> getAllStudents()
  {
    try
    {
      return server.getAllStudents();
    }
    catch (RemoteException e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Sets the selected teacher
   *
   * @param teacher The selected teacher
   */
  public void setSelectedTeacher(Account teacher)
  {
    selectedTeacher = teacher;
    support.firePropertyChange("steacher", null, selectedTeacher);
  }

  /**
   * Returns an array list of tests of a teacher based on a query
   *
   * @param query The query to search tests
   * @return An array list of tests
   */
  public ArrayList<Test> getTestsBySearchT(String query)
  {
    ArrayList<Test> testsLocal = getTestsT();
    return searchTests(query, testsLocal);
  }

  /**
   * Returns an array list of sessions of a teacher based on a query
   *
   * @param query The query to search sessions
   * @return An array list of sessions
   */
  public ArrayList<Session> getSessionsBySearchT(String query)
  {
    ArrayList<Session> sessionsLocal = getAllSessionsByTeacher();
    return searchSession(query, sessionsLocal);
  }

  /**
   * Returns an array list of classnames of a teacher based on a query
   *
   * @param query The query to search classnames
   * @return An array list of classnames
   */
  public ArrayList<ClassName> getClassesBySearchT(String query)
  {
    ArrayList<ClassName> classesLocal = getClassesByTeacher();
    return searchClass(query, classesLocal);
  }

  /**
   * Returns an array list of students of a teacher based on a query
   *
   * @param query The query to search students
   * @return An array list of students
   */
  public ArrayList<StudentAccount> getStudentsBySearchT(String query)
  {
    ArrayList<StudentAccount> studentLocal = getStudentsByTeacher();
    return searchStudent(query, studentLocal);
  }

  /**
   * Returns an array list of tests based on a query
   *
   * @param query The query to search tests
   * @return An array list of tests
   */
  public ArrayList<Test> getTestsBySearchA(String query)
  {
    ArrayList<Test> testsLocal = getAllTests();
    return searchTests(query, testsLocal);
  }

  /**
   * Searches for tests based on the query in the ArrayList of tests.
   *
   * @param query      the query
   * @param testsLocal the ArrayList of tests
   * @return an ArrayList of Test objects that match the query
   */
  public ArrayList<Test> searchTests(String query, ArrayList<Test> testsLocal)
  {
    ArrayList<Test> result = new ArrayList<>();
    if (!query.isEmpty())
    {
      boolean isDigits = query.matches("\\d+");
      if (!isDigits)
      {
        for (int i = 0; i < testsLocal.size(); i++)
        {
          if (testsLocal.get(i).getTitle().equals(query))
          {
            result.add(testsLocal.get(i));
          }
        }
      }
      else
      {
        for (int i = 0; i < testsLocal.size(); i++)
        {
          if (Integer.toString(testsLocal.get(i).getId()).equals(query))
          {
            result.add(testsLocal.get(i));
          }
        }
      }
    }
    else
    {
      result = testsLocal;
    }
    return result;
  }

  /**
   * Searches for sessions based on the query in the ArrayList of sessions.
   *
   * @param query         the query
   * @param sessionsLocal the ArrayList
   * @return an ArrayList of Session objects that match the query
   */
  public ArrayList<Session> searchSession(String query,
      ArrayList<Session> sessionsLocal)
  {
    ArrayList<Session> result = new ArrayList<>();
    boolean isDigits = query.matches("\\d+");

    if (query.isEmpty() || !isDigits)
    {
      result = sessionsLocal;
    }
    else
    {
      for (int i = 0; i < sessionsLocal.size(); i++)
      {
        if (Integer.toString(sessionsLocal.get(i).getId()).equals(query))
        {
          result.add(sessionsLocal.get(i));
        }
      }
    }
    return result;
  }

  /**
   * Searches for class names based on the query in the ArrayList of class names.
   *
   * @param query      the query
   * @param classLocal the ArrayList
   * @return an ArrayList of ClassName objects that match the query
   */
  public ArrayList<ClassName> searchClass(String query,
      ArrayList<ClassName> classLocal)
  {
    ArrayList<ClassName> result = new ArrayList<>();
    boolean isDigits = query.matches("\\d+");

    if (query.isEmpty())
    {
      result = classLocal;
    }
    else
    {
      for (int i = 0; i < classLocal.size(); i++)
      {
        if (classLocal.get(i).getClassName().equals(query))
        {
          result.add(classLocal.get(i));
        }
      }
    }
    return result;
  }

  /**
   * Searches for students based on the query in the ArrayList of students.
   *
   * @param query        the query
   * @param studentLocal the ArrayList
   * @return an ArrayList of Student objects that match the query
   */
  public ArrayList<StudentAccount> searchStudent(String query,
      ArrayList<StudentAccount> studentLocal)
  {
    ArrayList<StudentAccount> result = new ArrayList<>();
    if (!query.isEmpty())
    {
      boolean isDigits = query.matches("\\d+");
      if (!isDigits)
      {
        for (int i = 0; i < studentLocal.size(); i++)
        {
          if (studentLocal.get(i).getFirstName().equals(query)
              || studentLocal.get(i).getLastName().equals(query))
          {
            result.add(studentLocal.get(i));
          }
        }
      }
      else
      {
        for (int i = 0; i < studentLocal.size(); i++)
        {
          if (Integer.toString(studentLocal.get(i).getID()).equals(query))
          {
            result.add(studentLocal.get(i));
          }
        }
      }
    }
    else
    {
      result = studentLocal;
    }
    return result;
  }

  /**
   * Returns sessions that match the query
   *
   * @param query the query
   * @return an ArrayList of Session objects that match the query
   */
  public ArrayList<Session> getSessionsBySearchA(String query)
  {
    ArrayList<Session> sessionsLocal = getAllSessions();
    return searchSession(query, sessionsLocal);
  }

  /**
   * Returns classes that match the query
   *
   * @param query the query
   * @return an ArrayList of ClassName objects that match the query
   */
  public ArrayList<ClassName> getClassBySearchA(String query)
  {
    ArrayList<ClassName> classNamesLocal = getAllClassNames();
    return searchClass(query, classNamesLocal);
  }

  /**
   * Returns students that match the query
   *
   * @param query the query
   * @return an ArrayList of Student objects that match the query
   */
  public ArrayList<StudentAccount> getStudentBySearchA(String query)
  {
    ArrayList<StudentAccount> studentLocal = getAllStudents();
    return searchStudent(query, studentLocal);
  }

  /**
   * Returns teachers that match the query
   *
   * @param query the query
   * @return an ArrayList of Teacher objects that match the query
   */
  public ArrayList<Account> getTeacherBySearchA(String query)
  {
    ArrayList<Account> teacherLocal = getAllTeachers();
    ArrayList<Account> result = new ArrayList<>();
    if (!query.isEmpty())
    {
      boolean isDigits = query.matches("\\d+");
      if (!isDigits)
      {
        for (int i = 0; i < teacherLocal.size(); i++)
        {
          if (teacherLocal.get(i).getFirstName().equals(query)
              || teacherLocal.get(i).getLastName().equals(query))
          {
            result.add(teacherLocal.get(i));
          }
        }
      }
      else
      {
        for (int i = 0; i < teacherLocal.size(); i++)
        {
          if (Integer.toString(teacherLocal.get(i).getID()).equals(query))
          {
            result.add(teacherLocal.get(i));
          }
        }
      }
    }
    else
    {
      result = teacherLocal;
    }
    return result;
  }
}



