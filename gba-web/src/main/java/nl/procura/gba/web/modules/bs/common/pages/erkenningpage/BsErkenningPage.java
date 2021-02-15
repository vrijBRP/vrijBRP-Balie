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

package nl.procura.gba.web.modules.bs.common.pages.erkenningpage;

import static nl.procura.gba.common.MiscUtils.to;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;

import java.util.List;

import nl.procura.gba.web.components.layouts.page.ButtonPageTemplate;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkte;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.erkenning.DossierErkenning;
import nl.procura.gba.web.services.bs.erkenning.ErkenningsType;
import nl.procura.gba.web.services.bs.erkenning.argumenten.ErkenningArgumenten;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;
import nl.procura.vaadin.functies.VaadinUtils;

public class BsErkenningPage extends ButtonPageTemplate {

  private DossierGeboorte dossierGeboorte;
  private Table1          table1 = null;

  public BsErkenningPage(DossierGeboorte dossierGeboorte) {
    this.dossierGeboorte = dossierGeboorte;
    setMargin(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      setSpacing(true);
      setInfo("Overzicht van de erkenningen");
      table1 = new Table1();
      addComponent(table1);
    }

    super.event(event);
  }

  public DossierGeboorte getDossierGeboorte() {
    return dossierGeboorte;
  }

  public void setDossierGeboorte(DossierGeboorte dossierGeboorte) {
    this.dossierGeboorte = dossierGeboorte;
  }

  @Override
  public void onNew() {
    table1.getRecords().clear();
    table1.reloadRecords();
    super.onNew();
  }

  protected void fillTable(ErkenningArgumenten erkenningArgumenten) {

    table1.getRecords().clear();

    List<Zaak> zaken = getServices().getErkenningService().getOngeborenErkenningen(erkenningArgumenten);
    if (zaken.size() > 0) {
      for (Zaak zaak : zaken) {
        DossierErkenning erkenning = to(to(zaak, Dossier.class).getZaakDossier(), DossierErkenning.class);

        List<DossierAkte> aktes = erkenning.getDossier().getAktes();
        String akte = "";
        if (aktes.size() > 0) {
          akte = astr(aktes.get(0).getAkte());
        }

        Record r = table1.addRecord(erkenning);
        r.addValue(erkenning.getErkenningsType());
        r.addValue(akte);
        r.addValue(zaak.getDatumIngang().getFormatDate());
        r.addValue(getPersoonId(erkenning.getMoeder()));
        r.addValue(getPersoonId(erkenning.getErkenner()));
        r.addValue(ZaakUtils.getStatus(zaak.getStatus()));
      }
    } else {
      infoMessage("Geen erkenningen gevonden.");
    }

    table1.reloadRecords();
  }

  private String getPersoonId(DossierPersoon persoon) {
    StringBuilder id = new StringBuilder();
    id.append(persoon.getNaam().getPred_adel_voorv_gesl_voorn());

    if (persoon.getBurgerServiceNummer().isCorrect()) {
      id.append(" (");
      id.append(persoon.getBurgerServiceNummer());
      id.append(")");
    }

    return id.toString();
  }

  public class Table1 extends BsErkenningTable {

    @Override
    public void onClick(Record record) {

      try {
        DossierErkenning erkenning = record.getObject(DossierErkenning.class);
        if (!ErkenningsType.ERKENNING_ONGEBOREN_VRUCHT.is(erkenning.getErkenningsType())) {
          throw new ProException(WARNING, "Alleen erkenningen ongeboren vrucht zijn te selecteren");
        }

        getDossierGeboorte().setErkenningVoorGeboorte((DossierErkenning) record.getObject());
        VaadinUtils.getParent(this, GbaModalWindow.class).closeWindow();

      } catch (Exception e) {
        getApplication().handleException(e);
      }

      super.onDoubleClick(record);
    }
  }
}
