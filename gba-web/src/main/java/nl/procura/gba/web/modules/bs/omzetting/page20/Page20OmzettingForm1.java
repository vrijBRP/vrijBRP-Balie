/*
 * Copyright 2021 - 2022 Procura B.V.
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

package nl.procura.gba.web.modules.bs.omzetting.page20;

import static nl.procura.gba.web.modules.bs.omzetting.page20.Page20OmzettingBean1.LOCATIEVERBINTENIS;
import static nl.procura.gba.web.modules.bs.omzetting.page20.Page20OmzettingBean1.TIJDVERBINTENIS;
import static nl.procura.standard.Globalfunctions.fil;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.bs.omzetting.page20.locaties.OmzettingLocatiesWindow;
import nl.procura.gba.web.services.gba.basistabellen.huwelijkslocatie.HuwelijksLocatie;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page20OmzettingForm1 extends GbaForm<Page20OmzettingBean1> {

  private HuwelijksLocatie huwelijksLocatie = null;

  public Page20OmzettingForm1() {
    setColumnWidths("100px", "");
    setReadThrough(true);
    init();
  }

  public HuwelijksLocatie getHuwelijksLocatie() {
    return huwelijksLocatie;
  }

  public void setHuwelijksLocatie(HuwelijksLocatie huwelijksLocatie) {

    this.huwelijksLocatie = huwelijksLocatie;

    if (huwelijksLocatie != null && fil(huwelijksLocatie.getHuwelijksLocatie())) {
      getBean().setLocatieVerbintenis(
          huwelijksLocatie.getHuwelijksLocatie() + " (" + huwelijksLocatie.getLocatieSoort().getOms() + ")");
    } else {
      getBean().setLocatieVerbintenis("(Geen locatie geselecteerd)");
    }
  }

  @Override
  public Page20OmzettingBean1 getNewBean() {
    return new Page20OmzettingBean1();
  }

  @Override
  public void setColumn(Column column, com.vaadin.ui.Field field, final Property property) {

    if (property.is(TIJDVERBINTENIS)) {
      column.setAppend(true);
    }

    if (property.is(LOCATIEVERBINTENIS)) {
      column.addComponent(new Button("Selecteer",
          (ClickListener) event -> getWindow().addWindow(new OmzettingLocatiesWindow(Page20OmzettingForm1.this))));
    }

    super.setColumn(column, field, property);
  }

  protected void init() {
  }
}
