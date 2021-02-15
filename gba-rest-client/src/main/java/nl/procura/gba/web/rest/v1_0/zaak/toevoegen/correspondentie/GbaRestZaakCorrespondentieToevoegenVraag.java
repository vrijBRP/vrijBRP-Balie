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

package nl.procura.gba.web.rest.v1_0.zaak.toevoegen.correspondentie;

import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.*;
import static nl.procura.standard.Globalfunctions.along;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import nl.procura.gba.web.rest.v1_0.zaak.GbaRestZaakType;
import nl.procura.gba.web.rest.v1_0.zaak.toevoegen.GbaRestZaakToevoegenVraag;

@XmlRootElement(name = "vraag")
@XmlAccessorType(XmlAccessType.FIELD)
public class GbaRestZaakCorrespondentieToevoegenVraag extends GbaRestZaakToevoegenVraag {

  public GbaRestZaakCorrespondentieToevoegenVraag() {
    setZaakType(GbaRestZaakType.CORRESPONDENTIE);
  }

  public GbaRestZaakCorrespondentieToevoegenVraag(GbaRestZaakToevoegenVraag vraag) {
    setVraagElement(vraag.getVraagElement());
  }

  public String getRoute() {
    return getZaak().get(ROUTE).getWaarde();
  }

  public void setRoute(String waarde) {
    getZaak().add(ROUTE).set(waarde);
  }

  public long getCorrespondentieType() {
    return along(getZaak().get(CORRESPONDENTIE).getWaarde());
  }

  public void setCorrespondentieType(long waarde) {
    getZaak().add(CORRESPONDENTIE).set(waarde);
  }

  public String getAnders() {
    return get(getZaak(), ANDERS, false);
  }

  public void setAnders(String waarde) {
    getZaak().add(ANDERS).set(waarde);
  }

  public String getToelichting() {
    return get(getZaak(), TOELICHTING, false);
  }

  public void setToelichting(String waarde) {
    getZaak().add(TOELICHTING).set(waarde);
  }
}
