package com.examgo.client.viewmodel;

import com.examgo.client.model.ModelManager;

/**
 * A class to set up all the ViewModel classes. Used for a more solid code design.
 *
 * @author Crisitan Josan && Michael Leo && Mihai Mihaila
 * @version 1.0
 */
public class ViewModelFactory
{
  private AMainMenuViewModel aMainMenuViewModel;
  private LoginWindowViewModel loginWindowViewModel;
  private SMainMenuViewModel sMainMenuViewModel;
  private STestWindowViewModel sTestWindowViewModel;
  private TCreateTestViewModel tCreateTestViewModel;
  private TEditTestViewModel tEditTestViewModel;
  private TMainMenuViewModel tMainMenuViewModel;
  private TCheckSessionViewModel tCheckSessionViewModel;
  private AEditClassViewModel aEditClassViewModel;
  private AAddClassViewModel aAddClassViewModel;

  private AEditStudentViewModel aEditStudentViewModel;
  private AAddStudentViewModel aAddStudentViewModel;
  private AAddTeacherViewModel aAddTeacherViewModel;

  private AEditTeacherViewModel aEditTeacherViewModel;

  /**
   * One-argument constructor creating instances of all the ViewModel classes.
   *
   * @param modelManager ModelManager of the application
   */
  public ViewModelFactory(ModelManager modelManager)
  {
    this.aMainMenuViewModel = new AMainMenuViewModel(modelManager);
    this.loginWindowViewModel = new LoginWindowViewModel(modelManager);
    this.sMainMenuViewModel = new SMainMenuViewModel(modelManager);
    this.sTestWindowViewModel = new STestWindowViewModel(modelManager);
    this.tCreateTestViewModel = new TCreateTestViewModel(modelManager);
    this.tEditTestViewModel = new TEditTestViewModel(modelManager);
    this.tMainMenuViewModel = new TMainMenuViewModel(modelManager);
    this.tCheckSessionViewModel = new TCheckSessionViewModel(modelManager);
    this.tCheckSessionViewModel = new TCheckSessionViewModel(modelManager);
    this.aAddClassViewModel = new AAddClassViewModel(modelManager);
    this.aEditClassViewModel = new AEditClassViewModel(modelManager);
    this.aAddStudentViewModel = new AAddStudentViewModel(modelManager);
    this.aEditStudentViewModel = new AEditStudentViewModel(modelManager);
    this.aAddTeacherViewModel = new AAddTeacherViewModel(modelManager);
    this.aEditTeacherViewModel = new AEditTeacherViewModel(modelManager);
  }

  /**
   * A method to get an instance of a particular ViewModel class by its id.
   *
   * @param name shortened unique name of view controller to identify it
   */
  public ViewModel getViewModel(String name)
  {
    ViewModel viewModel = null;
    switch (name)
    {
      case "t_c_t" -> viewModel = tCreateTestViewModel;
      case "t_m_m" -> viewModel = tMainMenuViewModel;
      case "t_e_t" -> viewModel = tEditTestViewModel;
      case "s_t_w" -> viewModel = sTestWindowViewModel;
      case "s_m_m" -> viewModel = sMainMenuViewModel;
      case "l_w" -> viewModel = loginWindowViewModel;
      case "a_m_m" -> viewModel = aMainMenuViewModel;
      case "t_c_s" -> viewModel = tCheckSessionViewModel;
      case "a_a_c" -> viewModel = aAddClassViewModel;
      case "a_e_c" -> viewModel = aEditClassViewModel;
      case "a_a_s" -> viewModel = aAddStudentViewModel;
      case "a_e_s" -> viewModel = aEditStudentViewModel;
      case "a_a_t" -> viewModel = aAddTeacherViewModel;
      case "a_e_t" -> viewModel = aEditTeacherViewModel;
    }
    return viewModel;
  }
}