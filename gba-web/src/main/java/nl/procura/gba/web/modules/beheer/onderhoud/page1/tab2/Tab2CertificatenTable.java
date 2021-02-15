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

package nl.procura.gba.web.modules.beheer.onderhoud.page1.tab2;

import static nl.procura.standard.Globalfunctions.date2str;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.ssl.web.rest.v1_0.certificates.SslRestCertificate;

public class Tab2CertificatenTable extends GbaTable {

  @Override
  public void setColumns() {

    setSelectable(true);
    setMultiSelect(true);

    addColumn("Naam", 420);
    addColumn("Soort", 100);
    addColumn("Verloopdatum", 100).setUseHTML(true);
    addColumn("Dagen geldig", 100);
    addColumn("Status").setUseHTML(true);
  }

  @Override
  public void setRecords() {

    try {
      for (SslRestCertificate certificate : getApplication()
          .getServices().getOnderhoudService()
          .getSSLCertificates().getCertificates()) {

        Record record = addRecord(certificate);
        record.addValue(certificate.getDescription());
        record.addValue(certificate.isPrivateKey() ? "Sleutel" : "Certificaat");
        record.addValue(date2str(certificate.getEndDate()));
        record.addValue(certificate.getDaysValid());
        record.addValue(Tab2CertificatenWindow.getStatus(certificate));
      }
    } catch (Exception e) {
      getApplication().handleException(e);
    }

    super.setRecords();
  }

  @Override
  public void onDoubleClick(Record record) {
    Tab2CertificatenWindow window = new Tab2CertificatenWindow(record.getObject(SslRestCertificate.class));
    getApplication().getParentWindow().addWindow(window);
    super.onDoubleClick(record);
  }
}
