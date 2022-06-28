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

package nl.procura.gba.web.modules.locatiekeuze.locatie.pages.page1;

import static nl.procura.gba.web.services.beheer.locatie.LocatieType.NORMALE_LOCATIE;

import nl.procura.gba.web.common.misc.GbaApplicationCookie;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.gba.web.services.beheer.locatie.LocatieSelectie;

public class Page1LocatieKeuze extends NormalPageTemplate {

  private final GbaTable table;

  public Page1LocatieKeuze() {

    super("Selecteer uw locatie");

    setInfo("");

    table = new GbaTable() {

      @Override
      public void onClick(Record record) {
        selectRecord((Locatie) record.getObject());
      }

      @Override
      public void setColumns() {

        setSelectable(true);

        addColumn("Locatie", 200);
        addColumn("Omschrijving");

        super.setColumns();
      }

      @Override
      public void setRecords() {

        try {

          LocatieSelectie s = getServices().getLocatieService().getGebruikerLocaties(
              getApplication().getServices().getGebruiker(), NORMALE_LOCATIE);

          addInfo(s.getMelding());

          for (Locatie l : s.getLocaties()) {

            Record r = addRecord(l);
            r.addValue(l.getLocatie());
            r.addValue(l.getOmschrijving());
          }
        } catch (Exception e) {
          getApplication().handleException(getWindow(), e);
        }
      }
    };

    addExpandComponent(table);
  }

  @Override
  public void onEnter() {
    if (table.getRecord() != null) {
      selectRecord((Locatie) table.getRecord().getObject());
    }

    super.onEnter();
  }

  private void selectRecord(Locatie locatie) {

    if (locatie != null) {
      getServices().getLocatieService().setLocatie(locatie);
      new GbaApplicationCookie(getApplication()).setDefaultLocation(locatie);
      getApplication().onHome();
    }
  }
}
