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

package nl.procura.gba.web.modules.bs.geboorte.page80;

import static nl.procura.gba.web.services.bs.erkenning.ErkenningsType.ERKENNING_BIJ_AANGIFTE;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.INFO;
import static nl.procura.commons.core.exceptions.ProExceptionType.ENTRY;

import nl.procura.diensten.gba.ple.openoffice.formats.Naamformats;
import nl.procura.gba.web.modules.bs.common.pages.BsPage;
import nl.procura.gba.web.modules.bs.common.pages.BsProcesStatus;
import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsStatusForm;
import nl.procura.gba.web.modules.bs.geboorte.page81.Page81Geboorte;
import nl.procura.gba.web.modules.bs.geboorte.page82.Page82Geboorte;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterBackwardReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Geboorte
 */

public class Page80Geboorte<T extends DossierGeboorte> extends BsPage<T> {

  private final Table1 table1 = new Table1();

  public Page80Geboorte() {
    this("Geboorte - kind");
  }

  public Page80Geboorte(String caption) {

    super(caption);
    addButton(buttonPrev);
    addButton(buttonNext);
  }

  @Override
  public boolean checkPage() {

    for (DossierPersoon kind : getZaakDossier().getKinderen()) {
      if (!kind.isVolledig()) {
        throw new ProException(ENTRY, INFO, "Niet alle gegevens zijn volledig ingevuld.");
      }
    }

    getApplication().getServices().getGeboorteService().save(getDossier());
    return true;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addComponent(new BsStatusForm(getDossier()));

      setInfo("Selecteer de kinderen en vul de gegevens in.");

      addComponent(table1);

      table1.focus();
    } else if (event.isEvent(AfterBackwardReturn.class)) {

      table1.init();
    }

    super.event(event);
  }

  @Override
  public void onNextPage() {

    for (DossierPersoon kind : getZaakDossier().getKinderen()) {

      if (!kind.isVolledig()) {

        selectKind(kind);

        return;
      }
    }

    if (getZaakDossier().getErkenningsType().is(
        ERKENNING_BIJ_AANGIFTE)) { // Overslaan als er geen sprake is van erkenning bij geboorte

      getProcessen().getProces(Page82Geboorte.class).forceStatus(BsProcesStatus.EMPTY);
    } else {
      getProcessen().getProces(Page82Geboorte.class).forceStatus(BsProcesStatus.DISABLED);
    }

    goToNextProces();

    super.onNextPage();
  }

  @Override
  public void onPreviousPage() {

    goToPreviousProces();

    super.onPreviousPage();
  }

  private void selectKind(DossierPersoon kind) {

    getNavigation().goToPage(new Page81Geboorte(kind));
  }

  class Table1 extends Page80GeboorteTable {

    @Override
    public void onClick(Record record) {

      selectKind((DossierPersoon) record.getObject());

      super.onDoubleClick(record);
    }

    @Override
    public void setRecords() {

      int nr = 0;

      for (DossierPersoon kind : getZaakDossier().getKinderen()) {

        nr++;

        Record r = addRecord(kind);

        Naamformats n = kind.getNaam();

        String naam = n.getPred_adel_voorv_gesl_voorn();

        if (!kind.isVolledig()) {
          naam = "< Klik om de gegevens in te voeren >";
        }

        r.addValue(nr);
        r.addValue(naam);
        r.addValue(kind.getDatumGeboorte().getFormatDate() + " om " + kind.getTijdGeboorte().getFormatTime(
            "HH:mm"));
        r.addValue(kind.getGeslacht());
        r.addValue(kind.getNaamskeuzeType().getType());
      }

      super.setRecords();
    }
  }
}
