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

package nl.procura.gba.web.modules.beheer.verkiezing.page4;

import static nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource.STANDAARD;
import static nl.procura.gba.web.modules.beheer.verkiezing.page4.Page4VerkiezingBean.*;

import java.util.List;

import nl.procura.gba.jpa.personen.db.KiesrStem;
import nl.procura.gba.jpa.personen.db.KiesrVerk;
import nl.procura.gba.jpa.personen.db.KiesrWijz;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.HLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.validation.Anummer;

public class Page4Verkiezing extends NormalPageTemplate {

  private final KiesrVerk verk;
  private final KiesrStem stem;

  public Page4Verkiezing(KiesrVerk verk, KiesrStem stem) {
    super("Toevoegen / muteren kiezergegevens");
    this.verk = verk;
    this.stem = stem;
    addButton(buttonPrev, buttonSearch);
    buttonSearch.setCaption("Zoek persoon (F3)");
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      List<KiesrWijz> wijzigingen = getServices()
          .getKiezersregisterService()
          .getWijzigingen(verk, new Anummer(stem.getAnr()));

      Page4VerkiezingForm form1 = new Page4VerkiezingForm("Verkiezing", "200px",
          verk, stem, F_VERK);
      Page4VerkiezingForm form2 = new Page4VerkiezingForm("Stempas", "200px",
          verk, stem, F_CODE, F_PASNR, F_TOEGEVOEGD, F_AAND);
      Page4VerkiezingForm form3 = new Page4VerkiezingForm("Stemgerechtigde", "120px",
          verk, stem, F_ANR, F_GEBOORTEDATUM, F_GESLACHT, F_NAAM, F_ADRES);
      addComponent(form1);
      addComponent(new HLayout(form2, form3).widthFull());
      addComponent(new Fieldset("Uitgevoerde handelingen"));
      addExpandComponent(new Page4VerkiezingTable(wijzigingen));
    }

    super.event(event);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @Override
  public void onSearch() {
    getApplication().goToPl(getWindow(), "zaken.kiezersregister", STANDAARD, stem.getAnr().toString());
    super.onSearch();
  }
}
