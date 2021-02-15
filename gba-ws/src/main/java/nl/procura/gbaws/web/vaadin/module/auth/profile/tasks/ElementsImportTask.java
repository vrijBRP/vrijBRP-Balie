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

package nl.procura.gbaws.web.vaadin.module.auth.profile.tasks;

import static java.util.Arrays.asList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.gba.config.GbaConfig;
import nl.procura.gba.jpa.personenws.utils.GbaWsJpaStorageWrapper;
import nl.procura.gbaws.db.handlers.ProfileDao;
import nl.procura.gbaws.db.handlers.ProfileDao.ElementWrapper;
import nl.procura.gbaws.db.wrappers.ProfileWrapper.ProfielElement;
import nl.procura.gbaws.export.elements.ElementsExportUtils;
import nl.procura.gbaws.export.elements.ElementsJsonRecord;
import nl.procura.gbaws.web.vaadin.module.auth.profile.page1.ElementsProfile;
import nl.procura.standard.threadlocal.ThreadLocalStorage;
import nl.procura.vaadin.functies.task.VaadinTask;
import nl.procura.vaadin.functies.task.VaadinTaskLogger;

public class ElementsImportTask implements VaadinTask {

  private static final Logger   LOGGER  = LoggerFactory.getLogger(ElementsImportTask.class);
  private final ElementsProfile ep;
  private final File            file;
  private boolean               success = false;
  private boolean               failed  = false;
  private double                percentage;

  public ElementsImportTask(ElementsProfile elementsProfile, File file) {
    this.ep = elementsProfile;
    this.file = file;
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
    loggers.add(new VaadinTaskLogger(this.getClass().getName(), Level.DEBUG));
    return loggers;
  }

  @Override
  public void execute() {

    LOGGER.debug("Start taak");

    ThreadLocalStorage.init(new GbaWsJpaStorageWrapper(GbaConfig.getProperties()));

    try {
      List<ElementsJsonRecord> allRecords = ElementsExportUtils.getRecords(file);

      int tableIndex = 0;

      LOGGER.debug("Huidige elementen worden ontkoppeld.");

      List<ProfielElement> elements = ep.getProfile().getElementen(ep.getDatabaseType().getCode(),
          ep.getRefDatabase());

      for (ProfielElement e : elements) {

        ElementWrapper ew = new ElementWrapper(true, e.getCode_cat(), e.getCode_element());

        ProfileDao.ontKoppeling(ep, asList(ew));
      }

      LOGGER.debug(allRecords.size() + " elementen worden geladen.");

      for (ElementsJsonRecord jsonRecord : allRecords) {

        ProfileDao.koppeling(ep, asList(new ElementWrapper(false, jsonRecord.getCatCode(),
            jsonRecord.getElemCode())));

        percentage = (100.0 / allRecords.size()) * tableIndex;

        tableIndex++;
      }

      success = true;
    } catch (Exception e) {
      failed = true;
      e.printStackTrace();
      LOGGER.error("Fout bij het importeren", e);
    } finally {
      ThreadLocalStorage.remove();
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

  @Override
  public boolean isFailed() {
    return failed;
  }

  @Override
  public boolean isSuccess() {
    return success;
  }
}
