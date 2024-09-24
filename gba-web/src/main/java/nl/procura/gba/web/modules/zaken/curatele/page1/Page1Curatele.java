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

package nl.procura.gba.web.modules.zaken.curatele.page1;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.gba.common.MiscUtils.to;
import static nl.procura.standard.Globalfunctions.fil;

import java.util.List;

import com.vaadin.event.ItemClickEvent;

import nl.procura.gba.web.components.layouts.page.ButtonPageTemplate;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.zaken.curatele.page2.Page2Curatele;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.commons.core.exceptions.ProExceptionSeverity;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;
import nl.rechtspraak.namespaces.ccr.CCRWS;

public class Page1Curatele extends ButtonPageTemplate {

  private final List<DossierPersoon> personen;

  private Page1CurateleForm   form   = null;
  private Page1CurateleTable1 table1 = null;
  private Page1CurateleTable2 table2 = null;

  public Page1Curatele(List<DossierPersoon> personen) {
    this.personen = personen;
    setSpacing(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonReset);
      addButton(buttonSearch, 1f);
      addButton(buttonClose);

      table1 = new Page1CurateleTable1() {

        @Override
        public void itemClick(ItemClickEvent event) {
          super.onClick(event);
        }

        @Override
        public void select(Record record) {
          form.update(record.getObject(DossierPersoon.class));
        }

        @Override
        public void setRecords() {

          for (DossierPersoon persoon : personen) {

            if (persoon.isVolledig()) {

              Record r = addRecord(persoon);
              r.addValue(persoon.getNaam().getNaam_naamgebruik_eerste_voornaam());
              r.addValue(persoon.getDatumGeboorteStandaard());
            }
          }

          super.setRecords();
        }
      };

      form = new Page1CurateleForm();

      table2 = new Page1CurateleTable2() {

        @Override
        public void onClick(Record record) {
          getNavigation().goToPage(new Page2Curatele((CuratelePersoon) record.getObject()));
        }
      };

      addComponent(new Fieldset("Personen", table1));
      addComponent(new Fieldset("Zoekargumenten", form));
      addComponent(new Fieldset("Resultaat", table2));

      table1.select(table1.firstItemId());
    }

    super.event(event);
  }

  @Override
  public void onClose() {
    to(getWindow(), GbaModalWindow.class).closeWindow();
  }

  @Override
  public void onEnter() {
    onSearch();
    super.onEnter();
  }

  @Override
  public void onSearch() {

    form.commit();

    table2.getRecords().clear();

    try {

      String beanDatum = form.getBean().getGeboorteDatum().getStringValue();
      String beanVoorv = form.getBean().getVoorv();
      String beanNaam = form.getBean().getNaam();

      List<CCRWS> resultaten = getApplication().getServices().getCurateleService().verstuur(beanVoorv, beanNaam,
          beanDatum);

      if (resultaten.isEmpty()) {
        throw new ProException(ProExceptionSeverity.INFO, "Geen resultaten gevonden.");
      }

      for (CCRWS persoon : resultaten) {

        CuratelePersoon cPersoon = new CuratelePersoon(persoon);

        Record r = table2.addRecord(cPersoon);

        r.addValue(cPersoon.getNaam().getNaamNaamgebruikEersteVoornaam());
        r.addValue(cPersoon.getGeboorte().getDatumLeeftijdPlaatsLand());

        if (fil(cPersoon.getOverlijdensDatum())) {
          r.addValue(setClass(false, "Ja, op " + cPersoon.getOverlijdensDatum() + ""));
        } else {
          r.addValue("Nee");
        }
      }
    } finally {
      table2.reloadRecords();
    }

    super.onSearch();
  }
}
