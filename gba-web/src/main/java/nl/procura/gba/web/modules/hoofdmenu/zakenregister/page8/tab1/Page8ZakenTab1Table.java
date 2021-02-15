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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page8.tab1;

import static nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakAttribuutType.MIJN_OVERHEID_NIET;
import static nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakAttribuutType.MIJN_OVERHEID_WEL;

import java.util.List;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.document.PrintRecord;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page8.Page8ZakenTable;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;
import nl.procura.gba.web.services.zaken.algemeen.attribuut.AttribuutHistorie;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.DocumentSoort;
import nl.procura.gba.web.services.zaken.documenten.aanvragen.DocumentZaak;
import nl.procura.gba.web.services.zaken.documenten.printen.PrintModelUtils;
import nl.procura.vaadin.component.container.ArrayListContainer;

public class Page8ZakenTab1Table extends Page8ZakenTable {

  public Page8ZakenTab1Table() {
  }

  @Override
  public void setColumns() {

    setClickable(true);

    addColumn("Nr", 30);
    addColumn("Soort");
    addColumn("Aantekening");
    addColumn("Abonnee");
    addColumn("Personen");
    addColumn("Gebruiker");
    addColumn("Status").setUseHTML(true);

    super.setColumns();
  }

  protected void addUittreksel(List<DocumentSoort> soorten, Zaak zaak) {

    DocumentZaak uitt = (DocumentZaak) zaak;

    AttribuutHistorie historie = uitt.getZaakHistorie().getAttribuutHistorie();

    if (historie.isNot(MIJN_OVERHEID_WEL) && historie.isNot(MIJN_OVERHEID_NIET)) {

      for (DocumentSoort soort : soorten) {

        for (DocumentRecord doc : soort.getDocumenten()) {

          if (doc.getCDocument().equals(uitt.getDoc().getCDocument())) {

            Zaak modelZaak = getGerelateerdeZaak(uitt);
            Object modelObject = PrintModelUtils.getModel(modelZaak, zaak, doc);

            PrintRecord printRecord = new PrintRecord();
            printRecord.setSoort(soort);
            printRecord.setDocument(doc);
            printRecord.setUitvoer(new UitvoerField(doc));
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
          }
        }
      }
    }
  }

  private class UitvoerContainer extends ArrayListContainer {

    private UitvoerContainer(DocumentRecord document) {

      try {
        addItems(document.getPrintOpties());
      } catch (Exception e) {
        getApplication().handleException(getWindow(), e);
      }
    }
  }

  private class UitvoerField extends GbaNativeSelect {

    private UitvoerField(DocumentRecord document) {

      setWidth("100%");
      setNullSelectionAllowed(false);
      setContainerDataSource(new UitvoerContainer(document));
      setValue(document.getStandaardPrintOptie());
    }
  }
}
