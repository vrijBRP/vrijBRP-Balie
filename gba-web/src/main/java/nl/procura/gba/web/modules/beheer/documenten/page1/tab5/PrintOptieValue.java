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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab5;

import static nl.procura.gba.web.services.zaken.documenten.printopties.PrintOptieType.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import nl.procura.bzconnector.app.client.actions.listprinters.Printer;
import nl.procura.gba.web.services.zaken.documenten.printopties.PrintOptieType;
import nl.procura.standard.Globalfunctions;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class PrintOptieValue extends FieldValue {

  private PrintOptieType type;
  private Printer        networkPrinter;

  public PrintOptieValue(PrintOptieType type, String value, String description) {
    super(value, description);
    this.type = type;
    if (Globalfunctions.emp(value)) {
      setValue(type.getCode());
    }
  }

  public PrintOptieValue(PrintOptieType type, String value) {
    this(type, value, value);
  }

  public boolean isLocalPrinter() {
    return LOCAL_PRINTER == type;
  }

  public boolean isNetworkPrinter() {
    return NETWORK_PRINTER == type;
  }

  public boolean isMediaPrinter() {
    return isNetworkPrinter() || isLocalPrinter();
  }

  public Printer getNetworkPrinter() {
    return networkPrinter;
  }

  public void setNetworkPrinter(Printer networkPrinter) {
    this.networkPrinter = networkPrinter;
  }

  public boolean isCommandPrinter() {
    return COMMAND == type;
  }

  public boolean isMijnOverheid() {
    return MIJN_OVERHEID == type;
  }

  public String toString() {
    return getDescription();
  }

  public PrintOptieType getType() {
    return type;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(getValue())
        .append(getType())
        .build();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof PrintOptieValue)) {
      return false;
    }
    PrintOptieValue pov = (PrintOptieValue) obj;
    return new EqualsBuilder()
        .append(getValue(), pov.getValue())
        .append(getType(), pov.getType())
        .build();
  }
}
