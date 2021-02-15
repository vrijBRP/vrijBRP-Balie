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

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.layout.info.InfoLayout;

public abstract class KlapperZoekWindow extends GbaModalWindow {

  private final List<BasePLExt> persoonslijsten;

  public KlapperZoekWindow(List<BasePLExt> persoonslijsten) {

    super("Gevonden personen", "70%");

    this.persoonslijsten = persoonslijsten;

    VerticalLayout layout = new VerticalLayout();
    layout.setSizeFull();
    layout.setSpacing(true);
    layout.setMargin(true);

    layout.addComponent(BUTTON_CLOSE);
    layout.addComponent(new InfoLayout("Deze personen zijn gevonden op basis van de naam.",
        "Selecteer een persoon om het burgerservicenummer toe te kennen aan de klapper."));
    layout.addComponent(new ZoekTable());

    addComponent(layout);
  }

  public abstract void update(BasePLExt pl);

  private void closeKlapperZoekWindow() {
    closeWindow();
  }

  public class ZoekTable extends GbaTable {

    @Override
    public void onClick(final Record record) {

      getApplication().getParentWindow().addWindow(
          new ConfirmDialog("Wilt u het burgerservicenummer toekennen?") {

            @Override
            public void buttonYes() {

              update(record.getObject(BasePLExt.class));

              closeKlapperZoekWindow();

              super.buttonYes();
            }

          });

      super.onClick(record);
    }

    @Override
    public void setColumns() {

      setSelectable(true);
      addStyleName(GbaWebTheme.TABLE.NEWLINE_WRAP);

      addColumn("Nr", 30);
      addColumn("BSN", 80);
      addColumn("Naam");
      addColumn("Geslacht", 60);
      addColumn("Geboortedatum", 100);
      addColumn("Adres");

      super.setColumns();
    }

    @Override
    public void setRecords() {

      int i = 0;
      for (BasePLExt pl : persoonslijsten) {

        i++;
        Record row = addRecord(pl);
        row.addValue(astr(i));
        row.addValue(pl.getPersoon().getBsn().getDescr());
        row.addValue(pl.getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam());
        row.addValue(pl.getPersoon().getGeslacht().getDescr());
        row.addValue(pl.getPersoon().getGeboorte().getDatumLeeftijd());
        row.addValue(pl.getVerblijfplaats().getAdres().getAdresPcWplGem());
      }

      super.setRecords();
    }
  }
}
