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

import java.util.stream.Stream;

import nl.procura.burgerzaken.gba.core.enums.GBATable;
import nl.procura.gba.web.common.tables.GbaTables;
import nl.procura.standard.Globalfunctions;
import nl.procura.vaadin.component.field.fieldvalues.TabelFieldValue;

public class DutchTravelDocumentAuthorityContainer extends TabelContainer {

  private static final long serialVersionUID = 3176111152564790667L;

  public DutchTravelDocumentAuthorityContainer() {
    super(GBATable.AUT_VERSTREK_NED_REISD, false);
  }

  public static boolean isCountryCode(TabelFieldValue value) {
    if (value == null) {
      return false;
    }
    String stringValue = value.getKey().toString();
    return stringValue.startsWith("C") || stringValue.startsWith("G");
  }

  /**
   * The municipality is sometime part of the value id, like BI0518
   * If that is the case then extract the municipality
   */
  public static TabelFieldValue getMayorMunicipality(TabelFieldValue value) {
    if (isMayorCode(value)) {
      String number = value.getKey().toString().replaceAll("\\D+", "");
      if (Globalfunctions.pos(number)) {
        return GbaTables.PLAATS.get(number);
      }
    }
    return null;
  }

  public static boolean isMayorCode(TabelFieldValue value) {
    if (value == null) {
      return false;
    }
    String stringValue = value.getKey().toString();
    return stringValue.startsWith("B");
  }

  public static boolean isForeignAndInternalAffairs(TabelFieldValue value) {
    if (value == null) {
      return false;
    }
    String stringValue = value.getKey().toString();
    return stringValue.startsWith("BU") || stringValue.startsWith("BI");
  }

  @Override
  public void add(TabelFieldValue record, boolean isCurrent, boolean showValue) {
    if (Stream.of("B", "BU0518", "C", "G", "GH", "GN")
        .anyMatch(p -> record.getKey().toString().matches(p))) {
      super.add(record, isCurrent, showValue);
    }
  }
}
