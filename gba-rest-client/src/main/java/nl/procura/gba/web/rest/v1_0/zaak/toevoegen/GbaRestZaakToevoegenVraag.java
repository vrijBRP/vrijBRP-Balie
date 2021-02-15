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

package nl.procura.gba.web.rest.v1_0.zaak.toevoegen;

import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.*;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.fil;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElement;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementVraag;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestZaakStatus;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestZaakType;

@XmlRootElement(name = "vraag")
@XmlAccessorType(XmlAccessType.FIELD)
public class GbaRestZaakToevoegenVraag extends GbaRestElementVraag {

  public GbaRestZaakToevoegenVraag() {
    getVraagElement().add(ZAAK).add(ALGEMEEN);
    getVraagElement().get(ZAAK).add(DEELZAKEN);
  }

  public GbaRestElement getZaak() {
    return getVraagElement().get(ZAAK);
  }

  public GbaRestElement getAlgemeen() {
    return getZaak().get(ALGEMEEN);
  }

  public GbaRestElement getDeelzaken() {
    return getZaak().get(DEELZAKEN);
  }

  // Bsn
  public long getBurgerServiceNummer() {
    return along(getAlgemeen().get(GbaRestElementType.BSN).getWaarde());
  }

  public void setBurgerServiceNummer(long waarde) {
    getAlgemeen().add(GbaRestElementType.BSN).set(waarde);
  }

  // A-nummer
  public long getAnummer() {
    return along(getAlgemeen().get(GbaRestElementType.ANR).getWaarde());
  }

  public void setAnummer(long waarde) {
    getAlgemeen().add(GbaRestElementType.ANR).set(waarde);
  }

  public String getZaakId() {
    return getAlgemeen().get(GbaRestElementType.ZAAKID).getWaarde();
  }

  public boolean isZaakId() {
    return getAlgemeen().is(GbaRestElementType.ZAAKID) && fil(getZaakId());
  }

  // ZaakId
  public void setZaakId(String waarde) {
    getAlgemeen().add(GbaRestElementType.ZAAKID).set(waarde);
  }

  public GbaRestZaakStatus getZaakStatus() {
    return GbaRestZaakStatus.get(along(getAlgemeen().get(GbaRestElementType.STATUS).getWaarde()));
  }

  // Status
  public void setZaakStatus(GbaRestZaakStatus waarde) {
    getAlgemeen().add(GbaRestElementType.STATUS).set(waarde.getCode(), waarde.getOms());
  }

  public GbaRestZaakType getZaakType() {
    return GbaRestZaakType.get(along(getAlgemeen().get(GbaRestElementType.TYPE).getWaarde()));
  }

  // ZaakType
  public void setZaakType(GbaRestZaakType waarde) {
    getAlgemeen().add(GbaRestElementType.TYPE).set(waarde.getCode(), waarde.getOms());
  }

  public String getBron() {
    if (getAlgemeen().is(GbaRestElementType.BRON)) {
      return getAlgemeen().get(GbaRestElementType.BRON).getWaarde();
    }
    return "";
  }

  // Bron
  public void setBron(String waarde) {
    getAlgemeen().add(GbaRestElementType.BRON).set(waarde);
  }

  public String getLeverancier() {
    if (getAlgemeen().is(GbaRestElementType.LEVERANCIER)) {
      return getAlgemeen().get(GbaRestElementType.LEVERANCIER).getWaarde();
    }
    return "";
  }

  // Leverancier
  public void setLeverancier(String waarde) {
    getAlgemeen().add(GbaRestElementType.LEVERANCIER).set(waarde);
  }

  public String getDatumIngang() {
    if (getAlgemeen().is(GbaRestElementType.DATUM_INGANG)) {
      return getAlgemeen().get(GbaRestElementType.DATUM_INGANG).getWaarde();
    }
    return "";
  }

  // Ingang
  public void setDatumIngang(String waarde) {
    getAlgemeen().add(GbaRestElementType.DATUM_INGANG).set(waarde);
  }

  public String getDatumInvoer() {
    return getAlgemeen().get(GbaRestElementType.DATUM_INVOER).getWaarde();
  }

  // Invoer
  public void setDatumInvoer(String waarde) {
    getAlgemeen().add(GbaRestElementType.DATUM_INVOER).set(waarde);
  }

  public String getTijdInvoer() {
    return getAlgemeen().get(GbaRestElementType.TIJD_INVOER).getWaarde();
  }

  public void setTijdInvoer(String waarde) {
    getAlgemeen().add(GbaRestElementType.TIJD_INVOER).set(waarde);
  }
}
