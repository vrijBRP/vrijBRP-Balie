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

package nl.procura.gba.web.rest.v1_0.zaak;

import nl.procura.diensten.gba.ple.base.BasePLElem;
import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.rest.v1_0.GbaRestHandler;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElement;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class GbaRestElementHandler extends GbaRestHandler {

  public GbaRestElementHandler(Services services) {
    super(services);
  }

  public static void add(GbaRestElement element, String type, BasePLElem plElement) {
    add(element, type, plElement.getValue());
  }

  public static void add(GbaRestElement element, String type, BasePLValue plElement) {

    String waarde = plElement.getVal();
    String omschrijving = plElement.getDescr();

    add(element, type, waarde, omschrijving);
  }

  public static void add(GbaRestElement element, String type, boolean value) {
    element.add(type).set(value);
  }

  public static void add(GbaRestElement element, String type, DateTime value) {
    element.add(type).set(value.getLongDate(), value.getFormatDate());
  }

  public static void add(GbaRestElement element, String type, FieldValue value) {
    element.add(type).set(value.getValue(), value.getDescription());
  }

  public static void add(GbaRestElement element, String type, Geslacht value) {
    element.add(type).set(value.getAfkorting(), value.getNormaal());
  }

  public static void add(GbaRestElement element, String type, long value) {
    element.add(type).set(value);
  }

  public static void add(GbaRestElement element, String type, Object value, String descr) {
    element.add(type).set(value, descr);
  }

  public static void add(GbaRestElement element, String type, Object code, String value, String descr) {
    element.add(type).set(code, value, descr);
  }

  public static void add(GbaRestElement element, String type, String value) {
    element.add(type).set(value);
  }
}
