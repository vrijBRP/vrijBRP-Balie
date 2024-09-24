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

package nl.procura.gba.web.modules.account.meldingen.pages.page.tab1;

import static nl.procura.gba.web.services.applicatie.meldingen.ServiceMeldingCategory.WORK;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Embedded;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.account.meldingen.pages.page.AbstractMeldingenTab;
import nl.procura.gba.web.services.applicatie.meldingen.ServiceMelding;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.vaadin.component.layout.HLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable;

public class MeldingenTab1 extends AbstractMeldingenTab {

  public MeldingenTab1() {

    addButton(buttonDel, 1f);
    addButton(buttonClose);
    setInfo("Dubbelklik op een regel voor meer informatie.");
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {

      table = new GbaTable() {

        @Override
        public void onDoubleClick(IndexedTable.Record record) {
          selectRecord(record);
        }

        @Override
        public void setColumns() {

          setSelectable(true);
          setMultiSelect(true);
          addStyleName(GbaWebTheme.TABLE.NEWLINE_WRAP);

          addColumn("Nr", 30);
          addColumn("&nbsp;", 20).setClassType(Embedded.class);
          addColumn("Type", 90);
          addColumn("Tijdstip", 140);
          addColumn("Melding");

          super.setColumns();
        }

        @Override
        public void setRecords() {
          getServices().getSelectieService().check();
          List<ServiceMelding> messages = getServices().getMeldingService().getMeldingen(WORK);

          int nr = messages.size();
          if (!messages.isEmpty()) {
            for (ServiceMelding m : messages) {
              IndexedTable.Record r = addRecord(m);
              r.addValue(nr);
              r.addValue(getMeldingIcon(m));
              r.addValue(m.getSeverity());
              r.addValue(m.getDatumtijdString());
              r.addValue(StringUtils.abbreviate(m.getMelding(), 0, 120));
              nr--;
            }
          }
        }
      };
      addExpandComponent(table);
      CheckBox userMessageCheckbox = getOnlyUserMessageCheckbox();
      userMessageCheckbox.addListener((Button.ClickListener) e -> table.init());

      addComponent(new HLayout(getNotificationsCheckbox(), userMessageCheckbox)
          .align(userMessageCheckbox, Alignment.MIDDLE_RIGHT)
          .widthFull());
    }
    super.event(event);
  }
}
