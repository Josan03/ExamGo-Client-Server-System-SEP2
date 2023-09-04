package com.examgo.server.dao;

import com.examgo.client.model.Administrator;
import com.examgo.client.model.ClassName;
import com.examgo.client.model.Student;
import com.examgo.client.model.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

public class SqlAdministratorDaoIntegrationTest
{
  private AdministratorDao dao;
  private Administrator existingAdmin;
  private Teacher existingTeacher;

  @BeforeEach
  void setUp() throws SQLException
  {
    dao = SqlAdministratorDao.getInstance();
    existingAdmin = new Administrator("Cristian", "Josan", "admin", 1003);
    existingTeacher = new Teacher("Mihai", "Mihaila", 4);
  }

  @Test
  void getAdministratorFromDatabase() throws RemoteException
  {
    Administrator admin = dao.getAdministrator(existingAdmin.getID());
    assertEquals(1003, admin.getID());
  }

  @Test
  void getAnInvalidAdministratorFromDatabase() throws RemoteException
  {
    Administrator invalidAdministrator = new Administrator("John", "Doe", "johndoe", 100);
    assertNull(dao.getAdministrator(invalidAdministrator.getID()));
  }

  @Test
  void addingAClassToTheDatabase() throws RemoteException
  {
    String class_id = "TestClass";
    // Initially there are 5 classes in our database, so the size of classes should be 5, adding another class would increase the size

    dao.addClassName(class_id, existingTeacher);
    ArrayList<ClassName> classes = dao.getAllClassNames();
    assertEquals(6, classes.size());
    dao.removeClass(class_id);
  }

  @Test
  void addingAnInvalidClassThrowsException() throws RemoteException
  {
    // The class_id should be less or equals 10 characters, here we have 11 characters for class_id
    String class_id = "MoreThan10C";
    assertThrows(RemoteException.class, () -> dao.addClassName(class_id, existingTeacher));
  }

  @Test
  void addASingleStudentFromAdministratorToTheDatabase() throws RemoteException
  {
    Student student = dao.addSingleStudent("Student", "Test", "testStudent", "9N");
    assertEquals("9N", student.getClassname());

    int id = -1;
    ArrayList<Student> students = dao.getAllStudents();
    for (Student value : students)
    {
      if (value.getFirstName().equals("Student") && value.getLastName()
          .equals("Test"))
      {
        id = value.getID();
      }
    }

    Student studentToRemove = new Student("Student", "Test", new ClassName("9N"), id);
    dao.removeStudent(studentToRemove);
  }

  @Test
  void addAnInvalidStudentFromAdministratorToTheDatabase()
  {
    assertThrows(RemoteException.class, () -> dao.addSingleStudent("Student", "Test", "testStudent", "MoreThan10C"));
  }

  @Test
  void addMultipleStudentsFromAdministratorToTheDatabase()
      throws RemoteException
  {
    // We already have 6 students in our database, so now after adding 2 students we should have 8 students
    // We put the id negative one because we do not need it, the database is automatically generating a unique id
    ArrayList<Student> students = new ArrayList<>();
    students.add(new Student("Student1", "Test", "testStudent", new ClassName("12M", existingTeacher), -5));
    students.add(new Student("Student2", "Test", "testStudent", new ClassName("12M", existingTeacher), -5));
    dao.addMultipleStudents(students);
    ArrayList<Student> databaseStudents = dao.getAllStudents();
    assertEquals(8, databaseStudents.size());

    int id1 = -1;
    int id2 = -1;
    for (Student databaseStudent : databaseStudents)
    {
      if (databaseStudent.getFirstName().equals("Student1")
          && databaseStudent.getLastName().equals("Test"))
      {
        id1 = databaseStudent.getID();
      }
      else if (databaseStudent.getFirstName().equals("Student2")
          && databaseStudent.getLastName().equals("Test"))
      {
        id2 = databaseStudent.getID();
      }
    }

    Student studentToRemove1 = new Student("Student1", "Test", new ClassName("12M"), id1);
    Student studentToRemove2 = new Student("Student2", "Test", new ClassName("12M"), id2);
    dao.removeStudent(studentToRemove1);
    dao.removeStudent(studentToRemove2);
  }

  @Test
  void addMultipleStudentsAndOneIsInvalidFromThrowsException()
  {
    ArrayList<Student> students = new ArrayList<>();
    students.add(new Student("Student", "Test", "testStudent", new ClassName("MoreThan10C", existingTeacher), -5));
    students.add(new Student("Student", "Test", "testStudent", new ClassName("12M", existingTeacher), -5));
    assertThrows(RemoteException.class, () -> dao.addMultipleStudents(students));
  }

  @Test
  void addASingleTeacherFromAdministratorToTheDatabase() throws RemoteException
  {
    Teacher teacher = dao.addSingleTeacher("Teacher", "Test", "testTeacher");
    assertEquals("Teacher", teacher.getFirstName());

    int id = -1;
    ArrayList<Teacher> teachers = dao.getAllTeachers();
    for (Teacher value : teachers)
    {
      if (value.getFirstName().equals("Teacher") && value.getLastName()
          .equals("Test"))
      {
        id = value.getID();
      }
    }

    Teacher teacherToRemove = new Teacher("Teacher", "Test", id);
    dao.removeTeacher(teacherToRemove);
  }

  @Test
  void addAnInvalidTeacherFromAdministratorToTheDatabase()
  {
    assertThrows(RemoteException.class, () -> dao.addSingleTeacher("NameMoreThan30CharactersSixMoreNeeded", "Test", "testTeacher"));
  }

  @Test
  void addMultipleTeachersFromAdministratorToTheDatabase()
      throws RemoteException
  {
    // We already have 10 teachers in our database, so now after adding 2 teachers we should have 12 teachers
    // We put the id negative one because we do not need it, the database is automatically generating a unique id
    ArrayList<Teacher> teachers = new ArrayList<>();
    teachers.add(new Teacher("Teacher1", "Test", "testTeacher", -5));
    teachers.add(new Teacher("Teacher2", "Test", "testTeacher", -5));
    dao.addMultipleTeachers(teachers);
    ArrayList<Teacher> databaseTeachers = dao.getAllTeachers();
    assertEquals(12, databaseTeachers.size());

    int id1 = -1;
    int id2 = -1;
    for (Teacher databaseTeacher : databaseTeachers)
    {
      if (databaseTeacher.getFirstName().equals("Teacher1")
          && databaseTeacher.getLastName().equals("Test"))
      {
        id1 = databaseTeacher.getID();
      }
      else if (databaseTeacher.getFirstName().equals("Teacher2")
          && databaseTeacher.getLastName().equals("Test"))
      {
        id2 = databaseTeacher.getID();
      }
    }

    Teacher teacherToRemove1 = new Teacher("Teacher1", "Test", id1);
    Teacher teacherToRemove2 = new Teacher("Teacher2", "Test", id2);

    dao.removeTeacher(teacherToRemove1);
    dao.removeTeacher(teacherToRemove2);
  }

  @Test
  void addMultipleTeacherAndOneIsInvalidFromThrowsException()
  {
    ArrayList<Teacher> teachers = new ArrayList<>();
    teachers.add(new Teacher("FirstNameMoreThan30CharactersSixMoreNeeded", "Test", "testTeacher", -5));
    teachers.add(new Teacher("Teacher", "Test", "testTeacher", -5));
    assertThrows(RemoteException.class, () -> dao.addMultipleTeachers(teachers));
  }

  @Test
  void removeAnExistingClassWithStudents() throws RemoteException
  {
    // After removing the class the size of students in the database should be the same as initially 6
    dao.addClassName("ClassWStud", existingTeacher);
    dao.addSingleStudent("Student", "Test", "testStudent", "ClassWStud");
    dao.removeClass("ClassWStud");
    assertEquals(6, dao.getAllStudents().size());
  }

  @Test
  void insertANewStudentAndUpdateStudentFromDatabase() throws RemoteException
  {
    dao.addSingleStudent("Student", "Test", "testStudent", "9N");
    ArrayList<Student> students = dao.getAllStudents();

    int id = -1;
    for (Student value : students)
    {
      if (value.getFirstName().equals("Student") && value.getLastName()
          .equals("Test") && value.getClassname().equals("9N"))
      {
        id = value.getID();
      }
    }

    // Here we insert new properties for the same id student so that we can update the student in the database
    dao.updateStudent(new Student("StudentChanged", "TestChanged", "testStudent", new ClassName("9N"), id), "10C");

    ArrayList<Student> updatedStudents = dao.getAllStudents();
    Student updatedStudent = null;

    for (Student student : updatedStudents)
    {
      if (student.getFirstName().equals("StudentChanged")
          && student.getLastName().equals("TestChanged"))
      {
        updatedStudent = new Student(student.getFirstName(),
            student.getLastName(), student.getPassword(),
            student.getClassNameObj(), student.getID());
      }
    }

    assert updatedStudent != null;
    assertEquals("StudentChanged", updatedStudent.getFirstName());
    assertEquals("10C", updatedStudent.getClassname());

    dao.removeStudent(updatedStudent);
  }

  @Test
  void insertANewStudentAndUpdateWithInvalidClassThrowsException() throws RemoteException
  {
    dao.addSingleStudent("Student", "Test", "testStudent", "9N");
    ArrayList<Student> students = dao.getAllStudents();

    int id = -1;
    for (Student student : students)
    {
      if (student.getFirstName().equals("Student") && student.getLastName()
          .equals("Test") && student.getClassname().equals("9N"))
      {
        id = student.getID();
      }
    }

    // Here we insert a class that has more than 10 characters so that we can get an exception
    int finalId = id;
    assertThrows(RemoteException.class, () -> {
      dao.updateStudent(new Student("StudentChanged", "TestChanged", "testStudent", new ClassName("9N"),
          finalId), "MoreThan10C");
    });

    dao.removeStudent(new Student("Student", "Test", "testStudent", new ClassName("9N"), id));
  }

  @Test
  void insertANewTeacherAndUpdateTeacherFromDatabase() throws RemoteException
  {
    dao.addSingleTeacher("Teacher", "Test", "testTeacher");
    ArrayList<Teacher> teachers = dao.getAllTeachers();

    int id = -1;
    for (Teacher value : teachers)
    {
      if (value.getFirstName().equals("Teacher") && value.getLastName()
          .equals("Test"))
      {
        id = value.getID();
      }
    }

    // Here we insert new properties for the same id teacher so that we can update the teacher in the database
    dao.updateTeacher(new Teacher("TeacherChanged", "TestChanged", id));

    ArrayList<Teacher> updatedTeachers = dao.getAllTeachers();
    Teacher updatedTeacher = null;

    for (Teacher teacher : updatedTeachers)
    {
      if (teacher.getFirstName().equals("TeacherChanged")
          && teacher.getLastName().equals("TestChanged"))
      {
        updatedTeacher = new Teacher(teacher.getFirstName(),
            teacher.getLastName(), teacher.getID());
      }
    }

    assert updatedTeacher != null;
    assertEquals("TeacherChanged", updatedTeacher.getFirstName());
    assertEquals("TestChanged", updatedTeacher.getLastName());

    dao.removeTeacher(updatedTeacher);
  }

  @Test
  void insertANewTeacherAndUpdateWithInvalidDataThrowsException() throws RemoteException
  {
    dao.addSingleTeacher("Teacher", "Test", "testTeacher");
    ArrayList<Teacher> teachers = dao.getAllTeachers();

    int id = -1;
    for (Teacher teacher : teachers)
    {
      if (teacher.getFirstName().equals("Teacher") && teacher.getLastName()
          .equals("Test"))
      {
        id = teacher.getID();
      }
    }

    // Here we insert a first name that has more than 30 characters so that we can get an exception
    int finalId = id;
    assertThrows(RemoteException.class, () -> {
      dao.updateTeacher(new Teacher("FirstNameMoreThan30CharactersSixMoreNeeded", "Test", "testTeacher", finalId));
    });

    dao.removeTeacher(new Teacher("Teacher", "Test", id));
  }

  @Test
  void insertANewClassAndUpdateClassFromDatabase() throws RemoteException
  {
    dao.addClassName("ClassUpd", existingTeacher);

    // Here we insert new properties for the same id student so that we can update the student in the database
    dao.updateClassName("ClassUpd", "ToClassUpd", existingTeacher);

    ArrayList<ClassName> classes = dao.getAllClassNames();
    ClassName updatedClass = null;

    for (ClassName aClass : classes)
    {
      if (aClass.getClassName().equals("ToClassUpd"))
      {
        updatedClass = new ClassName(aClass.getClassName());
      }
    }

    assert updatedClass != null;
    assertEquals("ToClassUpd", updatedClass.getClassName());

    dao.removeClass("ToClassUpd");
  }
}
