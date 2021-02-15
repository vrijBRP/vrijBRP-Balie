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

package nl.procura.gba.web.rest.v1_0.zaak.toevoegen.inbox;

import static nl.procura.standard.Globalfunctions.isTru;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestZaakType;
import nl.procura.gba.web.rest.v1_0.zaak.toevoegen.GbaRestZaakToevoegenVraag;

@XmlRootElement(name = "vraag")
@XmlAccessorType(XmlAccessType.FIELD)
public class GbaRestInboxToevoegenVraag extends GbaRestZaakToevoegenVraag {

  public GbaRestInboxToevoegenVraag() {
    setZaakType(GbaRestZaakType.INBOX);
  }

  public GbaRestInboxToevoegenVraag(GbaRestZaakToevoegenVraag vraag) {
    setVraagElement(vraag.getVraagElement());
  }

  public boolean isNieuweZaak() {
    return isTru(getZaak().get(GbaRestElementType.NIEUWE_ZAAK).getWaarde());
  }

  public void setNieuweZaak(boolean waarde) {
    getZaak().add(GbaRestElementType.NIEUWE_ZAAK).set(waarde);
  }

  public String getBestandsnaam() {
    return getZaak().get(GbaRestElementType.BESTANDSNAAM).getWaarde();
  }

  public void setBestandsnaam(String waarde) {
    getZaak().add(GbaRestElementType.BESTANDSNAAM).set(waarde);
  }

  public String getOmschrijving() {
    return getZaak().get(GbaRestElementType.OMSCHRIJVING).getWaarde();
  }

  public void setOmschrijving(String waarde) {
    getZaak().add(GbaRestElementType.OMSCHRIJVING).set(waarde);
  }

  public String getBestand() {
    return getZaak().get(GbaRestElementType.BESTAND).getWaarde();
  }

  public void setBestand(String waarde) {
    getZaak().add(GbaRestElementType.BESTAND).set(waarde);
  }

  public String getVerwerkingId() {
    return getZaak().get(GbaRestElementType.VERWERKING_ID).getWaarde();
  }

  public void setVerwerkingId(String waarde) {
    getZaak().add(GbaRestElementType.VERWERKING_ID).set(waarde);
  }
}
