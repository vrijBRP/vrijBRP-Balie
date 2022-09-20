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

package nl.procura.gba.web.modules.beheer.onderhoud.page1.tab1;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.ssl.web.rest.v1_0.connections.SslRestConnection;
import nl.procura.ssl.web.rest.v1_0.connections.SslRestConnectionMessage;

public class Tab1VerbindingenTable extends GbaTable {

  private List<SslRestConnection> connections = new ArrayList<>();

  public Tab1VerbindingenTable() {
  }

  @Override
  public void setColumns() {

    setSelectable(true);
    setMultiSelect(true);

    addColumn("Naam", 170);
    addColumn("URL", 340);
    addColumn("Certificaat", 210);
    addColumn("Melding").setUseHTML(true);
  }

  public void setConnections(List<SslRestConnection> connections) {
    this.connections = connections;
    init();
  }

  @Override
  public void setRecords() {
    try {
      for (SslRestConnection connection : connections) {
        Record record = addRecord(connection);
        record.addValue(connection.getName());
        record.addValue(connection.getUrl());
        record.addValue(connection.getCertificate());
        record.addValue(getStatus(connection.getMessages()));
      }
    } catch (Exception e) {
      getApplication().handleException(e);
    }

    super.setRecords();
  }

  @Override
  public void onDoubleClick(Record record) {
    VerbindingControleWindow controleWindow = new VerbindingControleWindow(record.getObject(SslRestConnection.class));
    getApplication().getParentWindow().addWindow(controleWindow);
    super.onDoubleClick(record);
  }

  public static String getStatus(List<SslRestConnectionMessage> messages) {
    return VerbindingControleWindow.toHtmlStatus(messages.stream()
        .reduce((first, second) -> second)
        .orElse(new SslRestConnectionMessage()));
  }
}
