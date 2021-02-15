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

package nl.procura.gba.web.services.bs.overlijden.gemeente;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.services.bs.algemeen.enums.OntvangenDocument;
import nl.procura.gba.web.services.bs.algemeen.enums.TermijnLijkbezorging;
import nl.procura.gba.web.services.bs.algemeen.enums.WijzeLijkbezorging;
import nl.procura.gba.web.services.bs.overlijden.AbstractDossierOverlijden;
import nl.procura.gba.web.services.bs.overlijden.DossierOverlijden;
import nl.procura.gba.web.services.bs.overlijden.DossierOverlijdenLijkbezorging;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class DossierOverlijdenGemeente extends AbstractDossierOverlijden
    implements DossierOverlijden, DossierOverlijdenLijkbezorging {

  private static final long serialVersionUID = -1252109599795822477L;

  public DossierOverlijdenGemeente() {
    super(ZaakType.OVERLIJDEN_IN_GEMEENTE);
    setBuitenBenelux(false);
  }

  public FieldValue getPlaatsOverlijden() {
    return getGemeenteOverlijden();
  }

  public void setPlaatsOverlijden(FieldValue plaatsOverlijden) {
    setGemeenteOverlijden(plaatsOverlijden);
  }

  public boolean isAangifteVolledig() {

    boolean pOverlijden = (getGemeenteOverlijden() != null);
    boolean dOverlijden = (getDatumOverlijden() != null);
    boolean dLijk = (getDatumLijkbezorging() != null);
    boolean wLijk = !(WijzeLijkbezorging.ONBEKEND.equals(getWijzeLijkBezorging()));
    boolean tLijk = !(TermijnLijkbezorging.ONBEKEND.equals(getTermijnLijkbezorging()));
    boolean doc = !(OntvangenDocument.ONBEKEND.equals(getOntvangenDocument()));
    boolean buitenB = !isBuitenBenelux();

    return isAllTrue(pOverlijden, dOverlijden, dLijk, wLijk, tLijk, doc, buitenB);
  }
}
