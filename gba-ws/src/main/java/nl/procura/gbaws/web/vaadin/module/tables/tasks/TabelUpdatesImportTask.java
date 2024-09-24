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

package nl.procura.gbaws.web.vaadin.module.tables.tasks;

import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.Globalfunctions.emp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;

import javax.persistence.EntityManager;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.gba.config.GbaConfig;
import nl.procura.gba.jpa.personenws.db.Landtab;
import nl.procura.gba.jpa.personenws.db.LandtabElement;
import nl.procura.gba.jpa.personenws.db.LandtabRecord;
import nl.procura.gba.jpa.personenws.db.LandtabRecordPK;
import nl.procura.gba.jpa.personenws.utils.GbaWsJpa;
import nl.procura.gbaws.db.enums.LandTabType;
import nl.procura.gbaws.db.handlers.LandTabDao;
import nl.procura.gbaws.export.tables.LandTabJsonRecord;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.functies.task.VaadinTask;

public abstract class TabelUpdatesImportTask implements VaadinTask {

  private static final Logger LOGGER = LoggerFactory.getLogger(TabelUpdatesImportTask.class);

  private final EntityManager em;

  private boolean success = false;
  private boolean failed  = false;
  private double  percentage;

  public TabelUpdatesImportTask() {
    em = GbaWsJpa.createManager(GbaConfig.getProperties());
  }

  public boolean isAskedToStop() {
    return Thread.currentThread().isInterrupted();
  }

  @Override
  public double getPercentage() {
    return percentage;
  }

  @Override
  public double getSubPercentage() {
    return 0;
  }

  @Override
  public String getTaskActivityLabel() {
    return "";
  }

  public abstract List<LandTabJsonRecord> getRecordsByLandTabType(LandTabType landTabType);

  @Override
  public void execute() {

    LOGGER.debug("Start taak");
    try {
      importTables();
      success = true;
    } catch (RuntimeException e) {
      failed = true;
      LOGGER.error("Fout bij het importeren", e);
    } finally {
      em.close();
    }

    if (!isAskedToStop()) {
      percentage = 100.00;
    }
  }

  private void importTables() {
    LandTabType[] tables = LandTabType.values();
    int tableIndex = 0;
    for (LandTabType landTabType : tables) {
      if (landTabType.getCode() > 0) {
        importLandTabType(landTabType);
      }
      percentage = (100.0 / tables.length) * tableIndex;
      tableIndex++;
    }
  }

  private void importLandTabType(LandTabType landTabType) {
    LOGGER.debug(landTabType.getGbaTabel().getDescr());
    List<LandTabJsonRecord> records = getRecordsByLandTabType(landTabType);
    if (!records.isEmpty()) {
      importRecords(landTabType, records);
    }
  }

  private void importRecords(LandTabType landTabType, List<LandTabJsonRecord> records) {
    em.getTransaction().begin();
    LandTabDao.removeTable(em, landTabType.getCode());
    em.getTransaction().commit();

    em.getTransaction().begin();
    Landtab dbTable = em.merge(new Landtab(landTabType.getCode()));
    for (LandTabJsonRecord jsonRecord : records) {
      importRecord(landTabType, dbTable, jsonRecord);
    }
    em.getTransaction().commit();
  }

  private void importRecord(LandTabType landTabType, Landtab dbTable, LandTabJsonRecord jsonRecord) {
    String key = jsonRecord.getFormats().getCode();

    // Geen -1 of -2 keys
    if (key.startsWith("-") || emp(key)) {
      return;
    }

    LandtabRecord dbRecord = new LandtabRecord(new LandtabRecordPK(dbTable.getCTable(), key));
    dbRecord.setLandtab(dbTable);
    dbRecord.setOms(jsonRecord.getFormats().getDescription());
    dbRecord.setDIn(aval(jsonRecord.getD_in()));
    dbRecord.setDEnd(aval(jsonRecord.getD_end()));
    dbRecord.setAttr(toPropertyString(jsonRecord));

    em.merge(dbRecord);

    for (GBAElem element : landTabType.getElements()) {
      LandtabElement dbElement = new LandtabElement();
      dbElement.setCTable(dbTable);
      dbElement.setCElement(element.getCode());
      em.merge(dbElement);
    }

    em.merge(dbRecord);
  }

  private String toPropertyString(LandTabJsonRecord record) {
    Properties attrProps = new Properties();
    attrProps.put("eu", record.getEu());
    attrProps.put("eer", record.getEer());
    attrProps.put("id", record.getId());
    ByteArrayOutputStream bos = null;

    try {
      bos = new ByteArrayOutputStream();
      attrProps.store(bos, null);
      bos.flush();
      return new String(bos.toByteArray(), StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new ProException("Fout bij downloaden bestanden", e);
    } finally {
      IOUtils.closeQuietly(bos);
    }
  }

  @Override
  public void doStopNow() {
  }

  @Override
  public void onDone() {
  }

  @Override
  public boolean isFailed() {
    return failed;
  }

  @Override
  public boolean isSuccess() {
    return success;
  }
}
