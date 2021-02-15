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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.zoeken;

import com.vaadin.ui.Embedded;

import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page2.Page2ZakenTable;

public abstract class Page4ZakenTable extends Page2ZakenTable {

  @Override
  public void setColumns() {

    setSelectable(true);
    setMultiSelect(true);

    addColumn("Nr", 50);
    addColumn("", 20).setClassType(Embedded.class);
    addColumn("Zaaktype", 250);
    addColumn("Omschrijving");
    addColumn("Gebruiker / profielen").setCollapsed(false);
    addColumn("Gebruiker", 170).setCollapsed(true);
    addColumn("Profielen").setCollapsed(true);
    addColumn("Bron", 130).setCollapsed(true);
    addColumn("Leverancier", 130).setCollapsed(true);
    addColumn("Status", 120).setUseHTML(true);
    addColumn("Datum ingang", 100);
    addColumn("Ingevoerd op", 140);
  }

  @Override
  public void setRecords() {
  }
}
