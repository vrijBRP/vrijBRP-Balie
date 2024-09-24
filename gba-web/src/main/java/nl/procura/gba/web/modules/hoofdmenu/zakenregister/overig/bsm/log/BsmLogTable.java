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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.bsm.log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nl.procura.bsm.rest.v1_0.objecten.log.BsmRestLog;
import nl.procura.bsm.rest.v1_0.objecten.log.BsmRestLogRegel;
import nl.procura.bsm.rest.v1_0.objecten.log.BsmRestLogRegelType;
import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.vaadin.theme.twee.ProcuraTheme;

public abstract class BsmLogTable extends GbaTable {

  private final List<BsmRestLog> logs;

  public BsmLogTable(List<BsmRestLog> logs) {
    this.logs = logs;
  }

  @Override
  public void onClick(Record record) {
    Object object = record.getObject();

    if (object != null) {
      List<BsmRestLog> logs = new ArrayList<>();
      logs.addAll((Collection<? extends BsmRestLog>) object);
      goToPage(logs);
    }

    super.onClick(record);
  }

  @Override
  public void setColumns() {

    setSelectable(false);

    addColumn("Nr", 40);
    addColumn("LogLevel", 100).setUseHTML(true);
    addColumn("Regel");
    addStyleName(ProcuraTheme.TABLE.NEWLINE_WRAP);

    super.setColumns();
  }

  @Override
  public void setRecords() {
    addLogs(logs);
    super.setRecords();
  }

  protected abstract void goToPage(List<BsmRestLog> logs);

  private void addLog(BsmRestLog log) {
    addLogRegels(log);
  }

  private void addLogRegels(BsmRestLog log) {
    for (BsmRestLogRegel regel : log.getLogRegels()) {
      Record r = addRecord(log.getSubLogs());
      r.addValue(getRecords().size());
      r.addValue(getTypeTekst(regel.getType()));
      r.addValue(regel.getRegel());
    }
  }

  private void addLogs(List<BsmRestLog> logs) {
    if (logs != null && logs.size() > 0) {
      for (BsmRestLog log : logs) {
        if (log.getSubLogs() != null && !log.getSubLogs().isEmpty()) {
          setClickable(true);
          addLog(log);
        } else {
          addLog(log);
        }
      }
    }
  }

  private String getTypeTekst(BsmRestLogRegelType type) {

    switch (type) {
      case ERROR:
        return MiscUtils.setClass(false, type.getDescr());

      case INFO:
        return type.getDescr();

      case SUCCESS:
        return MiscUtils.setClass(true, type.getDescr());

      case WARN:
        return MiscUtils.setClass("orange", type.getDescr());

      case ONBEKEND:
      default:
        return "";
    }
  }
}
