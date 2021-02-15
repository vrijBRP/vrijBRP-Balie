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

package nl.procura.gba.web.modules.bs.erkenning.page5;

import java.util.ArrayList;
import java.util.List;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.Cat2OuderExt;
import nl.procura.diensten.gba.ple.extensions.Cat2OudersExt;
import nl.procura.gba.web.components.layouts.page.ButtonPageTemplate;
import nl.procura.gba.web.modules.bs.common.utils.BsKindUtils;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.erkenning.DossierErkenning;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.vaadin.component.label.H2;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Geboorte
 */

public class Page5ErkenningWindowPage extends ButtonPageTemplate {

  private final Page5Erkenning page;
  private final DossierPersoon ouder;
  private KindTable            table1 = null;

  public Page5ErkenningWindowPage(Page5Erkenning page, DossierPersoon ouder) {

    this.page = page;
    this.ouder = ouder;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      H2 h2 = new H2("Kinderen");

      addButton(buttonClose);

      setSpacing(true);

      setInfo("Dubbelklik om een kind te selecteren.");

      getButtonLayout().addComponent(h2, getButtonLayout().getComponentIndex(buttonClose));
      getButtonLayout().setExpandRatio(h2, 1f);
      getButtonLayout().setWidth("100%");

      table1 = new KindTable(page.getZaakDossier());

      addComponent(table1);
    }

    super.event(event);
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  private void addToList(DossierPersoon dossierPersoon) {
    page.toevoegenKind(dossierPersoon);
  }

  /**
   * Haal de kinderen van de moeder op
   */
  private List<DossierPersoon> getKinderen(DossierPersoon moeder) {

    List<DossierPersoon> personen = new ArrayList<>();

    for (BasePLExt pl : BsKindUtils.getKinderenMetPersoonslijst(getServices(), moeder)) {
      if (komtInAanmerkingErkenning(pl)) {
        DossierPersoon dossierPersoon = new DossierPersoon();
        BsPersoonUtils.kopieDossierPersoon(pl, dossierPersoon);
        personen.add(dossierPersoon);
      }
    }

    personen.addAll(BsKindUtils.getKinderenZonderPersoonslijst(getServices(), moeder));

    return personen;
  }

  private boolean komtInAanmerkingErkenning(BasePLExt pl) {
    Cat2OudersExt ouders = pl.getOuders();
    if (ouders.getAantalOuders() == 1) {
      for (Cat2OuderExt ouder : ouders.getOuders()) {
        if (ouders.heeftOuder(ouder)) {
          if (Geslacht.VROUW.getAfkorting().equalsIgnoreCase(ouder.getGeslacht().getCode())) {
            return true;
          }
        }
      }
    }
    return false;
  }

  class KindTable extends Page5ErkenningTable1 {

    public KindTable(DossierErkenning erkenning) {
      super(erkenning);
    }

    @Override
    public void onDoubleClick(Record record) {
      addToList(record.getObject(DossierPersoon.class));
      addDossierPersonen(getKinderen(ouder));
      super.onDoubleClick(record);
    }

    @Override
    public void setRecords() {
      addDossierPersonen(getKinderen(ouder));
      super.setRecords();
    }

    @Override
    protected boolean isMetCheckboxes() {
      return true;
    }
  }
}
