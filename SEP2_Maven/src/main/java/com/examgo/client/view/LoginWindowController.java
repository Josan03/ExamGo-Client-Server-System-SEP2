package com.examgo.client.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import com.examgo.client.model.Account;
import com.examgo.client.model.Administrator;
import com.examgo.client.model.Student;
import com.examgo.client.model.Teacher;
import com.examgo.client.viewmodel.LoginWindowViewModel;
import com.examgo.client.viewmodel.ViewModel;

/**
 * A ViewController for a window where user should log in. It is the first view to appear in the app
 *
 * @author Michael Leo && Mihai Mihaila
 * @version 1.0
 */
public class LoginWindowController implements ViewController
{
  private String name = "l_w";

  @FXML public TextField usernameInput;
  @FXML public TextField passwordInput;
  @FXML public Button loginButton;

  private ViewHandler viewHandler;
  private LoginWindowViewModel loginWindowViewModel;
  private Region root;

  /**
   * FXML method that is run when the "Login" button is pressed.
   */
  @FXML public void onLogin()
  {
    if (!usernameInput.getText().trim().isEmpty() && !passwordInput.getText()
        .trim().isEmpty())
    {
      Account result = loginWindowViewModel.connect(
          Integer.parseInt(usernameInput.getText()), passwordInput.getText());

      System.out.println(result.getType());
      if (result.getType().equals("teacher"))
      {
        viewHandler.openView("t_m_m");
      }
      else if (result.getType().equals("student"))
      {
        viewHandler.openView("s_m_m");
      }
      else if (result.getType().equals("admin"))
      {
        viewHandler.openView("a_m_m");
      }
      else
      {
        AlertControl.showError("Wrong id or password", null,
            "The id or password that you have entered are wrong");
      }
    }
  }

  /**
   * Overridden method that is implemented from ViewController interface, initializes the controller.
   *
   * @param viewHandler ViewHandler of the application
   * @param viewModel   specific ViewModel of the application
   * @param root        root of the FXMLLoader
   */
  @Override public void init(ViewHandler viewHandler, ViewModel viewModel,
      Region root)
  {
    this.viewHandler = viewHandler;
    this.loginWindowViewModel = (LoginWindowViewModel) viewModel;
    this.root = root;

    this.loginWindowViewModel.bindName(usernameInput.textProperty());

  }

  /**
   * Overridden method that is implemented from ViewController interface to get the root.
   *
   * @return the root of this view
   */
  @Override public Region getRoot()
  {
    return root;
  }

  /**
   * Overridden method that is implemented from ViewController interface to reset the view.
   */
  @Override public void reset()
  {

  }

  public void update()
  {
    loginWindowViewModel.update();
  }
}
