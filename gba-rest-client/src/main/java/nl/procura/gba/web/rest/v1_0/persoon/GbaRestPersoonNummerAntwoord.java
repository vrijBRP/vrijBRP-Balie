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

package nl.procura.gba.web.rest.v1_0.persoon;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElement;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementAntwoord;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType;

@XmlRootElement(name = "antwoord")
@XmlAccessorType(XmlAccessType.FIELD)
public class GbaRestPersoonNummerAntwoord extends GbaRestElementAntwoord {

  public GbaRestPersoonNummerAntwoord() {
  }

  public GbaRestElement getBsn() {
    return getAntwoordElement().get(GbaRestElementType.BSN);
  }

  public GbaRestElement getAnummer() {
    return getAntwoordElement().get(GbaRestElementType.ANR);
  }
}
