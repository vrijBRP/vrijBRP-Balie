/*
 * Copyright 2023 - 2024 Procura B.V.
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

package nl.procura.gba.web.components.layouts.form.document;

import static nl.procura.standard.Globalfunctions.isTru;

import java.util.List;

import com.vaadin.ui.Component;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.DocumentSoort;
import nl.procura.vaadin.component.container.ArrayListContainer;

public class PrintTableTemplate extends GbaTable {

  private List<DocumentSoort>          soorten = null;
  private final PrintMultiLayoutConfig config;

  public PrintTableTemplate(PrintMultiLayoutConfig config) {
    this.config = config;
  }

  @Override
  public void init() {
    super.init();
    checkPreSelectie();
  }

  @Override
  public void setColumns() {
    setSizeFull();
    setSelectable(true);
    setMultiSelect(true);

    addColumn("Nr", 30);
    addColumn("Document");
    addColumn("Soort", 250);
    addColumn("Uitvoer naar", 250).setClassType(Component.class);
  }

  @Override
  public void setRecords() {
    int nr = 0;
    if (soorten != null) {
      for (DocumentSoort soort : soorten) {
        for (DocumentRecord documentRecord : soort.getDocumenten()) {
          PrintRecord printRecord = new PrintRecord();
          printRecord.setSoort(soort);
          printRecord.setDocument(documentRecord);
          printRecord.setUitvoer(new UitvoerField(documentRecord));

          Record r = addRecord(printRecord);
          DocumentRecord document = printRecord.getDocument();
          String naam = document.toString();

          if (isTru(getApplication().getParmValue(ParameterConstant.DOC_TOON_BESTAND))) {
            naam += String.format(" (%d - %s)", document.getCDocument(), document.getBestand());
          }

          r.addValue(++nr);
          r.addValue(naam);
          r.addValue(printRecord.getSoort());
          r.addValue(printRecord.getUitvoer());
        }
      }
    }

    super.setRecords();
  }

  public void updateSoort(List<DocumentSoort> soorten) {
    if (getParent() != null) {
      this.soorten = soorten;
      init();
    }
  }

  /**
   * Controleer of er documenten standaard geselecteerd moet worden
   */
  private void checkPreSelectie() {
    for (Record record : getRecords()) {
      PrintRecord printRecord = (PrintRecord) record.getObject();
      boolean isPreSelect = printRecord.getDocument().isStandaardDocument();

      if (config.getSelectRecordFilter() != null) {
        isPreSelect = config.getSelectRecordFilter().select(printRecord.getDocument(), isPreSelect);
      }

      if (isPreSelect) {
        select(record.getItemId());
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
