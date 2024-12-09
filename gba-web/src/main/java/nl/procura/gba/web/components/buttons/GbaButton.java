package nl.procura.gba.web.components.buttons;

import com.vaadin.ui.Button;
import nl.procura.gba.web.application.GbaApplication;

public class GbaButton extends Button {

  public GbaButton(String caption) {
    super(caption);
  }

  @Override
  public GbaApplication getApplication() {
    return (GbaApplication) super.getApplication();
  }
}
