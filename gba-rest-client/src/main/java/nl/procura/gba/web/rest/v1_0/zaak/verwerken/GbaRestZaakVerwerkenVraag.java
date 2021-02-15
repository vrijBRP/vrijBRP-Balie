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

package nl.procura.gba.web.rest.v1_0.zaak.verwerken;

import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.ALGEMEEN;
import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.ZAAK;
import static nl.procura.standard.Globalfunctions.fil;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElement;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementVraag;

@XmlRootElement(name = "vraag")
@XmlAccessorType(XmlAccessType.FIELD)
public class GbaRestZaakVerwerkenVraag extends GbaRestElementVraag {

  public GbaRestZaakVerwerkenVraag() {
    getVraagElement().add(ZAAK).add(ALGEMEEN);
  }

  public GbaRestElement getZaak() {
    return getVraagElement().get(ZAAK);
  }

  public GbaRestElement getAlgemeen() {
    return getZaak().get(ALGEMEEN);
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
}
