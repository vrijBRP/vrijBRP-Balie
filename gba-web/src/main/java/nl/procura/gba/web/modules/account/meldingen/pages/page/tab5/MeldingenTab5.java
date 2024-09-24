/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.account.meldingen.pages.page.tab5;

import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakregisterNavigator.navigatoTo;

import java.util.List;
import java.util.Optional;

import com.vaadin.terminal.ExternalResource;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.account.meldingen.pages.page.AbstractMeldingenTab;
import nl.procura.gba.web.services.beheer.inwonerapp.InwonerSignaal;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

public class MeldingenTab5 extends AbstractMeldingenTab {

  public MeldingenTab5() {
    addButton(buttonSearch);
    addButton(buttonNext, 1f);
    addButton(buttonClose);
    setInfo("Dubbelklik op een regel voor meer informatie.");
    buttonSearch.setCaption("Verversen (F7)");
    buttonNext.setCaption("Toon in Inwoner.app (F2)");
  }

  @Override
  protected void selectRecord(Record record) {
    InwonerSignaal signaal = record.getObject(InwonerSignaal.class);
    Optional.ofNullable(signaal.getZaak())
        .ifPresent(zaak -> navigatoTo(zaak, MeldingenTab5.this, true));
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      table = new GbaTable() {

        @Override
        public void onDoubleClick(Record record) {
          selectRecord(record);
        }

        @Override
        public void setColumns() {
          setSelectable(true);
          setMultiSelect(true);
          addStyleName(GbaWebTheme.TABLE.NEWLINE_WRAP);

          addColumn("Nr", 30);
          addColumn("Zaak");
          addColumn("Aantal signalen", 120);

          super.setColumns();
        }

        @Override
        public void setRecords() {
          List<InwonerSignaal> signalen = getServices().getInwonerAppService().getSignalen();
          getServices().getInwonerAppService().check(signalen);
          int nr = 1;
          if (!signalen.isEmpty()) {
            for (InwonerSignaal signaal : signalen) {
              Record r = addRecord(signaal);
              r.addValue(nr);
              r.addValue(signaal.getOmschrijving());
              r.addValue(signaal.getAantal());
              nr++;
            }
          }
        }
      };

      addExpandComponent(table);
      addComponent(getNotificationsCheckbox());
    }
  }

  @Override
  public void onSearch() {
    table.init();
    super.onSearch();
  }

  @Override
  public void onNextPage() {
    getServices().getInwonerAppService().getExternalURL().ifPresent(this::openExternalURL);
    super.onNextPage();
  }

  private void openExternalURL(String url) {
    getApplication().getParentWindow().open(new ExternalResource(url), "Inwoner.app");
  }
}
