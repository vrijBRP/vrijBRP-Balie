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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.Level;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.gba.config.GbaConfig;
import nl.procura.gba.jpa.personenws.utils.GbaWsJpa;
import nl.procura.gbaws.db.enums.LandTabType;
import nl.procura.gbaws.export.tables.LandTabExportUtils;
import nl.procura.gbaws.export.tables.LandTabJsonExport;
import nl.procura.gbaws.export.tables.LandTabJsonRecord;
import nl.procura.vaadin.functies.task.VaadinTask;
import nl.procura.vaadin.functies.task.VaadinTaskLogger;

public class TabelUpdatesExportTask implements VaadinTask {

  private static final Logger LOGGER  = LoggerFactory.getLogger(TabelUpdatesExportTask.class);
  private final String        url;
  private boolean             success = false;
  private boolean             failed  = false;
  private double              percentage;
  private byte[]              content = null;

  public TabelUpdatesExportTask() {
    this.url = LandTabExportUtils.getEndpoint();
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

  public boolean isAskedToStop() {
    return Thread.currentThread().isInterrupted();
  }

  @Override
  public List<VaadinTaskLogger> getLoggers() {
    List<VaadinTaskLogger> loggers = new ArrayList<>();
    loggers.add(new VaadinTaskLogger(TabelUpdatesExportTask.class.getName(), Level.DEBUG));
    return loggers;
  }

  @Override
  public void execute() {

    LOGGER.debug("Start taak");
    LOGGER.debug("Downloaden tabellen van " + url + " ...");

    EntityManager em = GbaWsJpa.createManager(GbaConfig.getProperties());

    try {
      LandTabType[] tables = LandTabType.values();
      LandTabJsonExport export = new LandTabJsonExport();

      int tableIndex = 0;

      for (LandTabType landTabType : tables) {
        if (landTabType.getCode() > 0) {

          LOGGER.debug(landTabType.getGbaTabel().getDescr());
          List<LandTabJsonRecord> records = LandTabExportUtils.getRecords(url, landTabType);

          if (records.size() > 0) {
            export.getRecords().addAll(records);
          }
        }

        percentage = (100.0 / tables.length) * tableIndex;
        tableIndex++;
      }

      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      JsonFactory factory = new MappingJsonFactory();
      JsonGenerator generator = factory.createJsonGenerator(bos);

      generator.writeObject(export);
      generator.flush();
      generator.close();

      setContent(bos.toByteArray());
      success = true;
      LOGGER.debug("Het bestand wordt weggeschreven naar <b>proweb-tabellen.json</b>");
    } catch (Exception e) {
      failed = true;
      e.printStackTrace();
      LOGGER.error("Fout bij het exporteren", e);
    } finally {
      em.close();
    }

    if (!isAskedToStop()) {
      percentage = 100.00;
    }
  }

  @Override
  public void doStopNow() {
  }

  @Override
  public void onDone() {
  }

  public synchronized byte[] getContent() {
    return content;
  }

  public synchronized void setContent(byte[] content) {
    this.content = content;
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
