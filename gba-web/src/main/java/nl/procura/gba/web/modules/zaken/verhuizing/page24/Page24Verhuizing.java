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

package nl.procura.gba.web.modules.zaken.verhuizing.page24;

import static ch.lambdaj.Lambda.*;
import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.standard.exceptions.ProExceptionSeverity.INFO;
import static org.hamcrest.Matchers.equalTo;

import java.util.List;

import nl.procura.gba.web.modules.zaken.common.ZakenPage;
import nl.procura.gba.web.modules.zaken.verhuizing.VerhuisRelatie;
import nl.procura.gba.web.modules.zaken.verhuizing.page25.Page25Verhuizing;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraag;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page24Verhuizing extends ZakenPage {

  private final Table1               table1 = new Table1();
  private final List<VerhuisRelatie> verhuisRelaties;
  private final VerhuisAanvraag      verhuisAanvraag;

  public Page24Verhuizing(VerhuisAanvraag verhuisAanvraag, List<VerhuisRelatie> verhuisRelaties) {

    super("Presentievraag");

    this.verhuisAanvraag = verhuisAanvraag;
    this.verhuisRelaties = verhuisRelaties;

    addButton(buttonPrev);
    addButton(buttonNext);

    setInfo("Overzicht uit te voeren presentievragen. Druk op volgende (F2).");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addComponent(table1);
    } else if (event.isEvent(AfterReturn.class)) {

      table1.init();
    }

    super.event(event);
  }

  @Override
  public void onNextPage() {

    if (zijnAllePresentievragenUitgevoerd()) {
      throw new ProException(INFO, "Alle benodigde presentievragen zijn uitgevoerd.");
    }

    for (VerhuisRelatie verhuisRelatie : verhuisRelaties) {
      if (!verhuisRelatie.isPresentievraag()) {
        getNavigation().goToPage(new Page25Verhuizing(verhuisAanvraag, verhuisRelatie));
      }
    }

    super.onNextPage();
  }

  public boolean zijnAllePresentievragenUitgevoerd() {

    return !exists(table1.getAllValues(VerhuisRelatie.class),
        having(on(VerhuisRelatie.class).isPresentievraag(), equalTo(false)));
  }

  public class Table1 extends Page24VerhuizingTable1 {

    @Override
    public void onClick(Record record) {
      getNavigation().goToPage(new Page25Verhuizing(verhuisAanvraag, record.getObject(VerhuisRelatie.class)));
    }

    @Override
    public void setRecords() {

      for (VerhuisRelatie relatie : verhuisRelaties) {

        String naam = relatie.getRelatie().getPl().getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam();
        Record record = addRecord(relatie);
        record.addValue(setClass(relatie.isPresentievraag(), relatie.getPresentievraagType().toString()));
        record.addValue(naam);
      }

      super.setRecords();
    }
  }
}
