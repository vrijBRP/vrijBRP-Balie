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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;

import nl.procura.gbaws.db.enums.LandTabType;
import nl.procura.gbaws.export.tables.LandTabExportUtils;
import nl.procura.gbaws.export.tables.LandTabJsonRecord;
import nl.procura.vaadin.functies.task.VaadinTaskLogger;

public class TabelUpdatesWebserviceImportTask extends TabelUpdatesImportTask {

  private final String url;

  public TabelUpdatesWebserviceImportTask() {
    this.url = LandTabExportUtils.getEndpoint();
  }

  @Override
  public List<LandTabJsonRecord> getRecordsByLandTabType(LandTabType landTabType) {
    return LandTabExportUtils.getRecords(url, landTabType);
  }

  @Override
  public List<VaadinTaskLogger> getLoggers() {
    List<VaadinTaskLogger> loggers = new ArrayList<>();
    loggers.add(new VaadinTaskLogger(TabelUpdatesWebserviceImportTask.class.getName(), Level.DEBUG));
    return loggers;
  }

}
