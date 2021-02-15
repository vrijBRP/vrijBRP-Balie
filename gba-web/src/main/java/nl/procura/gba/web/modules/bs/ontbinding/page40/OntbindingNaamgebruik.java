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

package nl.procura.gba.web.modules.bs.ontbinding.page40;

import static nl.procura.standard.Globalfunctions.fil;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class OntbindingNaamgebruik {

  private String          naam              = "";
  private FieldValue      naamgebruikHuidig = null;
  private FieldValue      naamgebruikNieuw  = null;
  private GbaNativeSelect nieuwField        = null;

  public String getNaam() {
    return naam;
  }

  public void setNaam(String naam) {
    this.naam = naam;
  }

  public FieldValue getNaamgebruikHuidig() {
    return naamgebruikHuidig;
  }

  public void setNaamgebruikHuidig(FieldValue naamgebruikHuidig) {
    this.naamgebruikHuidig = naamgebruikHuidig;
  }

  public String getNaamgebruikHuidigTekst() {
    String code = getNaamgebruikHuidig().getStringValue();
    String descr = getNaamgebruikHuidig().getDescription();
    return fil(code) ? (code + ": " + descr) : "Niet vastgesteld";
  }

  public FieldValue getNaamgebruikNieuw() {
    return naamgebruikNieuw;
  }

  public void setNaamgebruikNieuw(FieldValue naamgebruikNieuw) {
    this.naamgebruikNieuw = naamgebruikNieuw;
  }

  public String getNieuweWaarde() {

    String nieuw = ((FieldValue) getNieuwField().getValue()).getStringValue();
    String oud = getNaamgebruikHuidig().getStringValue();

    return nieuw.equalsIgnoreCase(oud) ? "" : nieuw;
  }

  public GbaNativeSelect getNieuwField() {
    return nieuwField;
  }

  public void setNieuwField(GbaNativeSelect nieuwField) {
    this.nieuwField = nieuwField;
  }

  public boolean isNaamgebruikVastgesteld() {
    return fil(getNaamgebruikHuidig().getStringValue());
  }
}
