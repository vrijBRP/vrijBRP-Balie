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

package nl.procura.gba.web.modules.hoofdmenu.klapper.windows;

import static nl.procura.standard.Globalfunctions.astr;

import java.util.List;

import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkte;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.vaadin.component.layout.info.InfoLayout;

public class KlapperOverzichtWindow extends GbaModalWindow {

  private final List<DossierAkte> aktes;

  public KlapperOverzichtWindow(List<DossierAkte> aktes) {

    super(true, "Gevonden klappers", "70%");

    this.aktes = aktes;

    VerticalLayout layout = new VerticalLayout();
    layout.setSizeFull();
    layout.setSpacing(true);

    layout.addComponent(BUTTON_CLOSE);
    layout.addComponent(new InfoLayout("", "Deze klappers zijn gevonden bij deze zaak."));
    layout.addComponent(new OverzichtTable());

    addComponent(layout);
  }

  public class OverzichtTable extends GbaTable {

    @Override
    public void onClick(final Record record) {

      getApplication().getParentWindow().addWindow(new KlapperInzageWindow(record.getObject(DossierAkte.class)));

      super.onClick(record);
    }

    @Override
    public void setColumns() {

      setSelectable(true);
      addStyleName(GbaWebTheme.TABLE.NEWLINE_WRAP);

      addColumn("Nr", 30);
      addColumn("Datum", 100);
      addColumn("Akte", 100);
      addColumn("Invoertype", 110);
      addColumn("Soort");
      addColumn("Persoon");
      addColumn("Eventuele 2e persoon");

      super.setColumns();
    }

    @Override
    public void setRecords() {

      int i = 0;
      for (DossierAkte akte : aktes) {

        i++;
        Record row = addRecord(akte);
        row.addValue(astr(i));
        row.addValue(akte.getDatumIngang());
        row.addValue(akte.getAkte());
        row.addValue(akte.getInvoerType());
        row.addValue(akte.getAkteRegistersoort());
        row.addValue(akte.getAktePersoon().getNaam().getNaam_naamgebruik_eerste_voornaam());
        row.addValue(akte.getAktePartner().getNaam().getNaam_naamgebruik_eerste_voornaam());
      }

      super.setRecords();
    }
  }
}
