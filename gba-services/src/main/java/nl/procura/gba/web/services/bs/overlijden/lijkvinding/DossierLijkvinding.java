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

package nl.procura.gba.web.services.bs.overlijden.lijkvinding;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.services.bs.algemeen.enums.OntvangenDocument;
import nl.procura.gba.web.services.bs.algemeen.enums.WijzeLijkbezorging;
import nl.procura.gba.web.services.bs.overlijden.AbstractDossierOverlijden;
import nl.procura.gba.web.services.bs.overlijden.DossierOverlijden;
import nl.procura.gba.web.services.bs.overlijden.DossierOverlijdenLijkbezorging;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

public class DossierLijkvinding extends AbstractDossierOverlijden
    implements DossierOverlijden, DossierOverlijdenLijkbezorging {

  private static final long serialVersionUID = -2436562001186420066L;

  public DossierLijkvinding() {
    super(ZaakType.LIJKVINDING);
    setBuitenBenelux(false);
  }

  @Override
  public String getAkteAanduiding() {
    return "G"; // Lijkvinding
  }

  @Override
  public BsnFieldValue getBurgerServiceNummer() {
    BsnFieldValue bsn = getOverledene().getBurgerServiceNummer();
    return (bsn.getValue() == null) ? new BsnFieldValue() : bsn;
  }

  @Override
  public String getTijdLijkvindingStandaard() {
    return gettLijkvin().longValue() >= 0 ? getTijdLijkvinding().getFormatTime("HH.mm") : "";
  }

  public boolean isAangifteVolledig() {

    boolean pLijk = (getPlaatsLijkvinding() != null);
    boolean dLijk = (getDatumLijkvinding() != null);
    boolean tLijk = (getTijdLijkvinding() != null);
    boolean wLijk = !(WijzeLijkbezorging.ONBEKEND.equals(getWijzeLijkBezorging()));
    boolean doc = !(OntvangenDocument.ONBEKEND.equals(getOntvangenDocument()));

    return isAllTrue(pLijk, dLijk, tLijk, wLijk, doc);
  }

  @Override
  public boolean isVolledig() {
    return true;
  }
}
