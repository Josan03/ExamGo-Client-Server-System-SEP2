package com.examgo.client.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Region;
import com.examgo.client.viewmodel.ViewModelFactory;

import java.io.IOError;
import java.io.IOException;

/**
 * A class that loads the views
 *
 * @author Michael Leo && Mihai Mihaila
 * @version 1.0
 */
public class ViewFactory
{
  public static final String A_EDIT_TEACHER = "a_e_t";
  public static final String A_ADD_TEACHER = "a_a_t";
  public static final String A_ADD_STUDENT = "a_a_s";
  public static final String A_EDIT_CLASS = "a_e_c";
  public static final String A_EDIT_STUDENT = "a_e_s";
  public static final String A_ADD_CLASS = "a_a_c";
  public static final String A_MAIN_MENU = "a_m_m";
  public static final String LOGIN_WINDOW = "l_w";
  public static final String S_MAIN_MENU = "s_m_m";
  public static final String S_TEST_WINDOW = "s_t_w";
  public static final String T_CREATE_TEST = "t_c_t";
  public static final String T_EDIT_TEST = "t_e_t";
  public static final String T_MAIN_MENU = "t_m_m";
  public static final String T_CHECK_SESSION = "t_c_s";

  private AAddClassViewController aAddClassViewController = null;
  private AMainMenuViewController aMainMenuViewController = null;
  private LoginWindowController loginWindowController = null;
  private SMainMenuViewController sMainMenuViewController = null;
  private STestWindowViewController sTestWindowViewController = null;
  private TCreateTestViewController tCreateTestViewController = null;
  private TEditTestViewController tEditTestViewController = null;
  private TMainMenuViewController tMainMenuViewController = null;
  private TCheckSessionViewController tCheckSessionViewController = null;

  private AAddStudentViewController aAddStudentViewController = null;
  private AEditClassViewController aEditClassViewController = null;
  private AEditStudentViewController aEditStudentViewController = null;

  private AAddTeacherViewController aAddTeacherViewController = null;
  private AEditTeacherViewController aEditTeacherViewController = null;

  private ViewHandler viewHandler;
  private ViewModelFactory viewModelFactory;

  /**
   * Two-argument constructor initializing ViewFactory.
   *
   * @param viewHandler ViewHandler of the application
   * @param viewModelFactory ViewModelFactory of the application
   */
  public ViewFactory(ViewHandler viewHandler, ViewModelFactory viewModelFactory)
  {
    this.viewHandler = viewHandler;
    this.viewModelFactory = viewModelFactory;
  }

  /**
   * A method that passes the necessary controller to the loadView method.
   * @param id shortened unique name of view controller to identify it
   */
  public Region load(String id)
  {
    return switch (id)
        {
          case A_MAIN_MENU ->
              loadView(aMainMenuViewController, "/com/examgo/AMainMenu.fxml", id);
          case A_ADD_CLASS ->
              loadView(aAddClassViewController, "/com/examgo/AAddClass.fxml", id);
          case LOGIN_WINDOW ->
              loadView(loginWindowController, "/com/examgo/LoginWindow.fxml", id);
          case S_MAIN_MENU ->
              loadView(sMainMenuViewController, "/com/examgo/SMainMenu.fxml", id);
          case S_TEST_WINDOW ->
              loadView(sTestWindowViewController, "/com/examgo/STestWindow.fxml", id);
          case T_CREATE_TEST ->
              loadView(tCreateTestViewController, "/com/examgo/TCreateTest.fxml", id);
          case T_EDIT_TEST ->
              loadView(tEditTestViewController, "/com/examgo/TEditTest.fxml", id);
          case T_MAIN_MENU ->
              loadView(tMainMenuViewController, "/com/examgo/TMainMenu.fxml", id);
          case T_CHECK_SESSION ->
              loadView(tCheckSessionViewController, "/com/examgo/TCheckSession.fxml", id);
          case A_EDIT_CLASS ->
              loadView(aEditClassViewController, "/com/examgo/AEditClass.fxml", id);
          case A_ADD_STUDENT ->
              loadView(aAddStudentViewController, "/com/examgo/AAddStudent.fxml", id);
          case A_EDIT_STUDENT ->
              loadView(aEditStudentViewController, "/com/examgo/AEditStudent.fxml", id);
          case A_ADD_TEACHER ->
              loadView(aAddTeacherViewController, "/com/examgo/AAddTeacher.fxml", id);
          case A_EDIT_TEACHER ->
              loadView(aEditTeacherViewController, "/com/examgo/AEditTeacher.fxml", id);
          default -> throw new IllegalArgumentException("Unknown view: " + id);
        };
  }

  /**
   * A method to load a chosen view
   *
   * @param controller the controller class to load is passed
   * @param fxml a path to the .fxml file
   * @param id shortened unique name of view controller to identify it
   */
  private Region loadView(ViewController controller, String fxml, String id)
  {
    if (controller == null)
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource(fxml));
      try
      {
        Region root = loader.load();
        controller = loader.getController();
        controller.init(viewHandler, viewModelFactory.getViewModel(id), root);
      }
      catch (IOException e)
      {
        throw new IOError(e);
      }
    }
    controller.reset();
    return controller.getRoot();
  }
}
