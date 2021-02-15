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

package nl.procura.gba.web.modules.beheer.voorraad.page1;

import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.Globalfunctions.pos;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.zaken.voorraad.VoorraadStatus;
import nl.procura.gba.web.services.zaken.voorraad.VoorraadType;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page1Voorraad extends NormalPageTemplate {

  private Table table;

  public Page1Voorraad() {

    super("Voorraad");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      int aantal = aval(getServices().getParameterService().getParm(ParameterConstant.MIN_VOORRAAD));

      setInfo("Overzicht van het aantal nummers dat kan worden uitgegeven.");

      if (pos(aantal)) {
        addInfo("<hr/>" + "Bij een aantal van minder dan " + aantal
            + " nummers ziet de gebruiker een melding op het scherm.");
      }

      table = new Table();

      addComponent(table);
    }

    super.event(event);
  }

  class Table extends Page1VoorraadTable {

    @Override
    public void setRecords() {

      Record bsnRecord = addRecord(VoorraadType.BSN);

      bsnRecord.addValue(VoorraadType.BSN);
      bsnRecord.addValue(
          getServices().getVoorraadService().getAantallen(VoorraadType.BSN, VoorraadStatus.BESCHIKBAAR));
      bsnRecord.addValue(
          getServices().getVoorraadService().getAantallen(VoorraadType.BSN, VoorraadStatus.DEFINITIEF));

      Record anrRecord = addRecord(VoorraadType.ANR);

      anrRecord.addValue(VoorraadType.ANR);
      anrRecord.addValue(
          getServices().getVoorraadService().getAantallen(VoorraadType.ANR, VoorraadStatus.BESCHIKBAAR));
      anrRecord.addValue(
          getServices().getVoorraadService().getAantallen(VoorraadType.ANR, VoorraadStatus.DEFINITIEF));

      super.setRecords();
    }
  }
}
