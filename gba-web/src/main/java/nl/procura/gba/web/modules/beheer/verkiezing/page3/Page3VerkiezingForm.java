/*
 * Copyright 2022 - 2023 Procura B.V.
 *
 * In licentie gegeven krachtens de EUPL, versie 1.2
 * U mag dit werk niet gebruiken, behalve onder de voorwaarden van de licentie.
 * U kunt een kopie van de licentie vinden op:
 *
 *   https://github.com/vrijBRP/vrijBRP/blob/master/LICENSE.md
 *
 * Deze bevat zowel de Nederlandse als de Engelse tekst
 *
 * Tenzij dit op grond van toepasselijk recht vereist is of schriftelijk
 * is overeengekomen, wordt software krachtens deze licentie verspreid
 * "zoals deze is", ZONDER ENIGE GARANTIES OF VOORWAARDEN, noch expliciet
 * noch impliciet.
 * Zie de licentie voor de specifieke bepalingen voor toestemmingen en
 * beperkingen op grond van de licentie.
 */

package nl.procura.gba.web.modules.beheer.verkiezing.page3;

import static nl.procura.gba.web.modules.beheer.verkiezing.page3.Page3VerkiezingBean.*;

import java.util.function.Consumer;

import com.vaadin.ui.Button;
import com.vaadin.ui.Field;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.person.QuickSearchPersonWindow;
import nl.procura.vaadin.component.layout.table.TableLayout;
import nl.procura.validation.Anummer;

public class Page3VerkiezingForm extends GbaForm<Page3VerkiezingBean> {

  private final Button selectButton1 = new Button("Zoek");
  private final Button selectButton2 = new Button("Zoek");

  private Anummer                 anrKiesgerechtigde;
  private Anummer                 anrGemachtigde;
  private QuickSearchPersonWindow snelZoekWindow;

  public Page3VerkiezingForm() {
    setCaption("Selectie kiezersregister");
    setOrder(F_ROS, F_VNR, F_AAND, F_KIESGERECHTIGDE, F_DATUM_VAN, F_GEMACHTIGDE, F_DATUM_TM, F_AANTAL);
    setColumnWidths("200px", "200px", "100px", "");
    setBean(new Page3VerkiezingBean());
    Button.ClickListener listener1 = event -> onZoek(F_KIESGERECHTIGDE, anr -> anrKiesgerechtigde = anr);
    Button.ClickListener listener2 = event -> onZoek(F_GEMACHTIGDE, anr -> anrGemachtigde = anr);
    selectButton1.setWidth("70px");
    selectButton2.setWidth("70px");
    selectButton1.addListener(listener1);
    selectButton2.addListener(listener2);
  }

  @Override
  public void reset() {
    anrKiesgerechtigde = null;
    anrGemachtigde = null;
    super.reset();
  }

  private void onZoek(String fieldName, Consumer<Anummer> anummerConsumer) {
    if (snelZoekWindow == null) {
      snelZoekWindow = new QuickSearchPersonWindow(pl -> {
        Anummer anr = new Anummer(pl.getPersoon().getAnr().getVal());
        anummerConsumer.accept(anr);
        getField(fieldName).setValue(pl.getPersoon().getNaam().getNaamNaamgebruikGeslachtsnaamVoornaamAanschrijf());
        repaint();
      });
    }
    snelZoekWindow.setParent(null);
    getApplication().getParentWindow().addWindow(snelZoekWindow);
  }

  @Override
  public void setColumn(TableLayout.Column column, Field field, Property property) {
    if (property.is(F_AANTAL)) {
      getLayout().addFieldset("Resultaat selectie");
    }
    if (property.is(F_DATUM_TM, F_AANTAL)) {
      column.setColspan(3);
    }
    super.setColumn(column, field, property);
  }

  @Override
  public void afterSetColumn(TableLayout.Column column, Field field, Property property) {
    if (property.is(F_KIESGERECHTIGDE)) {
      selectButton1.setParent(null);
      column.addComponent(selectButton1);
    }
    if (property.is(F_GEMACHTIGDE)) {
      selectButton2.setParent(null);
      column.addComponent(selectButton2);
    }
    super.afterSetColumn(column, field, property);
  }

  public Anummer getAnrKiesgerechtigde() {
    return anrKiesgerechtigde;
  }

  public Anummer getAnrGemachtigde() {
    return anrGemachtigde;
  }
}
