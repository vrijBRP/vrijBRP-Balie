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

package nl.procura.gbaws.web.vaadin.module.tables.page2;

import static nl.procura.standard.Globalfunctions.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

import nl.procura.gbaws.db.handlers.LandTabDao;
import nl.procura.gbaws.db.handlers.LandTabDao.LandTable;
import nl.procura.gbaws.db.handlers.LandTabDao.LandTableRecord;
import nl.procura.gbaws.web.vaadin.layouts.tables.PersonenWsTable;
import nl.procura.commons.core.exceptions.ProException;

@SuppressWarnings("serial")
public class Page2TablesTable extends PersonenWsTable {

  private final LandTable landTable;

  public Page2TablesTable(LandTable landTable) {
    this.landTable = landTable;
  }

  @Override
  public void setColumns() {

    setSelectable(true);
    setMultiSelect(true);

    addColumn("Code", 50);
    addColumn("Omschrijving");
    addColumn("Datum ingang", 100);
    addColumn("Datum einde", 100);
    addColumn("Attributen");

    super.setColumns();
  }

  @Override
  public void setRecords() {

    for (LandTableRecord record : LandTabDao.getAllRecords(landTable.getCode(), true)) {

      Record r = addRecord(record);

      r.addValue(record.getCode());
      r.addValue(record.getDescription());
      r.addValue(date2str(record.getDateIn()));
      r.addValue(date2str(record.getDateEnd()));
      r.addValue(getAttributes(record.getAttr()));
    }

    super.setRecords();
  }

  private String getAttributes(String attr) {
    StringBuilder out = new StringBuilder();
    if (fil(attr)) {
      Properties attrProps = new Properties();
      ByteArrayInputStream bis = null;
      try {
        bis = new ByteArrayInputStream(attr.getBytes());
        attrProps.load(bis);
        for (Entry<Object, Object> ks : attrProps.entrySet()) {
          if (fil(astr(ks.getValue()))) {
            out.append(ks.getKey() + " = " + ks.getValue());
            out.append(", ");
          }
        }
      } catch (IOException e) {
        throw new ProException("Fout bij inlezen attributen", e);
      } finally {
        IOUtils.closeQuietly(bis);
      }
    }
    return trim(out.toString());
  }
}
