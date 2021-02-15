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

package nl.procura.gba.web.modules.bs.common.pages.aktepage.page1;

import nl.procura.gba.web.modules.bs.common.pages.aktepage.BsAktePage;
import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsStatusForm;
import nl.procura.gba.web.services.bs.algemeen.ZaakDossier;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkte;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterBackwardReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Akte pagina
 */
public class BsAktePage1<T extends ZaakDossier> extends BsAktePage<T> {

  private Table table = null;

  public BsAktePage1(String caption) {

    super(caption);

    addButton(buttonPrev);
    addButton(buttonNext);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      table = new Table();

      addComponent(new BsStatusForm(getDossier()));

      setInfo("Controleer de aktegegevens. Als het aktenummer moet worden aangepast, "
          + "klik dan op de regel. Druk op Volgend (F2) om verder te gaan.");

      addComponent(table);

    } else if (event.isEvent(AfterBackwardReturn.class)) {

      table.init();
    }

    super.event(event);

    if (event.isEvent(InitPage.class)) {

      if (!isAktesCorrect()) {

        naarVolgendeAkte();
      } else {

        getServices().getAkteService().updateAkteEigenschappen(getDossier());
      }
    }
  }

  @Override
  public void onNextPage() {

    if (!isAktesCorrect()) {

      naarVolgendeAkte();
    } else {

      goToNextProces();
    }

    super.onNextPage();
  }

  class Table extends BsAkteTable {

    public Table() {
      super(getZaakDossier().getDossier());
    }

    @Override
    public void onClick(Record record) {

      selectAkte((DossierAkte) record.getObject());

      super.onClick(record);
    }
  }
}
