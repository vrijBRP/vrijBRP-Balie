package nl.procura.gba.web.modules.zaken.personmutations.page2;

import nl.procura.gba.web.components.layouts.GbaVerticalLayout;
import nl.procura.vaadin.component.layout.Fieldset;

public class Page2BCMCheckResultLayout extends GbaVerticalLayout {

  private String                         header = "Controle niet uitgevoerd";
  private final Page2BCMCheckResultTable table;

  public Page2BCMCheckResultLayout() {
    setSpacing(true);
    table = new Page2BCMCheckResultTable();
  }

  public void addComponentsWithHeader(String header) {
    this.header = header;
    addComponent(new Fieldset("BCM Controle resultaat - " + header));
    addComponent(table);
  }

  public Page2BCMCheckResultTable getTable() {
    return table;
  }

  public String getHeader() {
    return header;
  }
}
