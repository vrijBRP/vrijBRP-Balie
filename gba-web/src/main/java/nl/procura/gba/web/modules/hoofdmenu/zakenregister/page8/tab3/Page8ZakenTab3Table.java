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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page8.tab3;

import static nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakAttribuutType.MIJN_OVERHEID_WEL;

import java.util.List;

import com.vaadin.ui.Component;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.document.DocumentUitvoerContainer;
import nl.procura.gba.web.components.layouts.form.document.PrintRecord;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page8.Page8ZakenTable;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.aanvragen.DocumentZaak;
import nl.procura.gba.web.services.zaken.documenten.printen.PrintModelUtils;
import nl.procura.gba.web.services.zaken.documenten.printopties.PrintOptieType;
import nl.procura.gba.web.theme.GbaWebTheme;

public class Page8ZakenTab3Table extends Page8ZakenTable {

  public Page8ZakenTab3Table() {
  }

  @Override
  public void init() {

    super.init();

    selectAll(true);
  }

  @Override
  public void setColumns() {

    setSelectable(true);
    setMultiSelect(true);
    addStyleName(GbaWebTheme.TABLE.NEWLINE_WRAP);

    addColumn("Nr", 30);
    addColumn("Soort");
    addColumn("Aantekening");
    addColumn("Abonnee");
    addColumn("Personen");
    addColumn("Gebruiker");
    addColumn("Status").setUseHTML(true);
    addColumn("Uitvoer naar", 250).setClassType(Component.class);

    super.setColumns();
  }

  protected void addUittreksel(List<DocumentRecord> documenten, Zaak zaak) {

    DocumentZaak uitt = (DocumentZaak) zaak;

    // Is een mijn-overheid persoon
    if (uitt.getZaakHistorie().getAttribuutHistorie().is(MIJN_OVERHEID_WEL)) {

      for (DocumentRecord document : documenten) {

        if (document.getCDocument().equals(uitt.getDoc().getCDocument())) {

          Zaak modelZaak = getGerelateerdeZaak(uitt);
          Object modelObject = PrintModelUtils.getModel(modelZaak, zaak, document);

          PrintRecord printRecord = new PrintRecord();
          printRecord.setSoort(document.getDocumentSoort());
          printRecord.setDocument(document);
          printRecord.setUitvoer(new UitvoerField());
          printRecord.setZaak(modelZaak);
          printRecord.setModel(modelObject);

          Record r = addRecord(printRecord);

          r.addValue(getRecords().size());
          r.addValue(uitt.getSoort());
          r.addValue(uitt.getZaakHistorie().getAantekeningHistorie().getAantekeningenSamenvatting());
          r.addValue(getAbonnee(uitt));
          r.addValue(uitt.getPersonen().size());
          r.addValue(getIngevoerdDoor(uitt));
          r.addValue(ZaakUtils.getStatus(uitt.getStatus()));
          r.addValue(printRecord.getUitvoer());
        }
      }
    }
  }

  private class UitvoerField extends GbaNativeSelect {

    private UitvoerField() {

      setWidth("100%");
      setNullSelectionAllowed(false);
      setContainerDataSource(new DocumentUitvoerContainer(Page8ZakenTab3Table.this.getApplication(),
          PrintOptieType.MIJN_OVERHEID));
    }
  }
}
