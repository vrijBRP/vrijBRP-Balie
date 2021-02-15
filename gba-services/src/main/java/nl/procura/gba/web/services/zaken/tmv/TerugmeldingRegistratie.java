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

package nl.procura.gba.web.services.zaken.tmv;

import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.astr;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.jpa.personen.db.TerugmTmv;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.services.interfaces.DatabaseTable;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class TerugmeldingRegistratie extends TerugmTmv implements DatabaseTable {

  private static final long serialVersionUID = -4648479908378286298L;

  private UsrFieldValue usr    = new UsrFieldValue();
  private FieldValue    gemBeh = new FieldValue();

  public TerugmeldingRegistratie() {
    setToelichting("");
    setResultaat("");
  }

  public DateTime getAanleg() {
    return new DateTime(getDAanleg());
  }

  public TmvActie getActie() {
    return TmvActie.get(getBerichtcode());
  }

  public long getDossiernummer() {
    return getId().getDossiernummer();
  }

  public FieldValue getGemBeh() {
    return this.gemBeh;
  }

  public void setGemBeh(FieldValue value) {
    this.gemBeh = FieldValue.from(value);
  }

  public DateTime getIn() {
    return new DateTime(getId().getDIn(), getId().getTIn());
  }

  public TmvResultaat getOnderzoekResultaat() {
    return TmvResultaat.get(along(getResultaatonderzoek()));
  }

  public TmvStatus getStatus() {
    return TmvStatus.get(along(getDossierstatus()));
  }

  public String getToelichtingOmschrijving() {
    return astr(getToelichting()).replaceAll("\n", "<br/>");
  }

  public String getToelichtingResultaat() {
    return astr(getResultaat()).replaceAll("\n", "<br/>");
  }

  public UsrFieldValue getUsr() {
    return this.usr;
  }

  public void setUsr(UsrFieldValue usr) {
    this.usr = usr;
  }

  public DateTime getVerwAfh() {
    return new DateTime(getDVerwAfh());
  }

  public DateTime getWijz() {
    return new DateTime(getDWijz());
  }
}
