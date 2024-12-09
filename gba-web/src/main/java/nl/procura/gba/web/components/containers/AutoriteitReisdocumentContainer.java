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

package nl.procura.gba.web.components.containers;

import nl.procura.burgerzaken.gba.core.enums.GBATable;
import nl.procura.gba.web.common.tables.GBATableList;
import nl.procura.gba.web.common.tables.GbaTables;
import nl.procura.standard.Globalfunctions;
import nl.procura.vaadin.component.field.fieldvalues.TabelFieldValue;

public class AutoriteitReisdocumentContainer extends TabelContainer {

  public AutoriteitReisdocumentContainer() {
    super(GBATable.AUT_VERSTREK_NED_REISD);
    // B
    addGemeenten();

    // BI
    add("BI0518", "Minister van Binnenlandse Zaken");
    add("BI0599", "Bureau Internationale Vaart Rijnschippers Rotterdam", 19920401L, 19940101L);

    // BU
    add("BU0518", "Minister van Buitenlandse Zaken", 19920401, -1L);

    // BW: Niet uitgewerkt: College van Burgemeester en Wethouders

    // BZ
    add("BZ0394", "Minister van Buitenlandse Zaken (Schiphol)", -1L, 19920401);
    add("BZ0518", "Minister van Buitenlandse Zaken", -1L, 19920401);
    add("BZ0599", "Bureau Internationale Vaart Rijnschippers", -1L, 19920401);

    // C
    addConsulairePosten();

    // G
    add("G5095", "Gouverneur van Aruba", -1L, 20101010L);
    add("G7011", "Gouverneur van of Gezaghebber in de Nederlandse Antillen", -1L, 20101010L);

    // GH
    add("GH5106", "Gezaghebber op Bonaire", 20101010, -1L);
    add("GH5108", "Gezaghebber op Saba", 20101010, -1L);
    add("GH5109", "Gezaghebber op Sint Eustatius", 20101010, -1L);

    // GN
    add("GN5095", "Gouverneur van Aruba", 20101010, -1L);
    add("GN5107", "Gouverneur van Curaçao", 20101010, -1L);
    add("GN5110", "Gouverneur van Sint Maarten", 20101010, -1L);

    // KM: Niet uitgewerkt: Commandant van de Koninklijke Marechaussee
    // P: Commissaris van de Koningin van de gecodeerd genoemde provincie

    // PK
    add("PK", "Gegevens ontleend aan de persoonskaart");
  }

  @Override
  public void setValueContainer(GBATableList valueContainer, boolean isCurrent, boolean showValue) {
    addContainerProperty(OMSCHRIJVING, String.class, "");
    removeAllItems();
  }

  private void addGemeenten() {
    for (TabelFieldValue plaats : GbaTables.PLAATS.get()) {
      if (Globalfunctions.aval(plaats.getKey()) < 2000) {
        String value = "B" + plaats.getKey();
        String descr = "Burgemeester van " + plaats.getDescription();
        add(value, descr);
      }
    }
  }

  private void addConsulairePosten() {
    for (TabelFieldValue land : GbaTables.LAND.get()) {
      String value = "C" + land.getKey();
      String descr = "Consulaire post in " + land.getDescription();
      add(value, descr, -1, 20140309);
    }
  }

  private void add(String value, String descr) {
    add(value, descr, -1L, -1L);
  }

  private void add(String value, String descr, long dIn, long dEnd) {
    TabelFieldValue fieldValue = new TabelFieldValue("", value, descr, dIn, dEnd);
    super.add(fieldValue, false, true);
  }

}
