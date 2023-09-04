package com.examgo.client.viewmodel;

import java.beans.PropertyChangeListener;

/**
 * A ViewModel interface is implemented by all the view model classes. Used for a more solid code design.
 *
 * @author Michael Leo
 * @version 1.0
 */
public interface ViewModel
{
  /**
   * Add a PropertyChangeListener for a specific property.
   * The listener will be invoked only when a call on firePropertyChange names that specific property.
   * @param propertyName The name of the property to listen on.
   * @param listener The PropertyChangeListener to be added
   */
  void addPropertyChangeListener(String propertyName,
      PropertyChangeListener listener);

  /**
   * Remove a PropertyChangeListener for a specific property.
   * @param propertyName The name of the property to listen on.
   * @param listener The PropertyChangeListener to be removed.
   */
  void removePropertyChangeListener(String propertyName,
      PropertyChangeListener listener);
}
