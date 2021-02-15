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

package nl.procura.gba.web.services.bs.overlijden.buitenland;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.services.bs.overlijden.AbstractDossierOverlijden;
import nl.procura.gba.web.services.bs.overlijden.DossierOverlijden;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

public class DossierOverlijdenBuitenland extends AbstractDossierOverlijden implements DossierOverlijden {

  private static final long serialVersionUID = -1252109599795822477L;

  public DossierOverlijdenBuitenland() {
    super(ZaakType.OVERLIJDEN_IN_BUITENLAND);
    setBuitenBenelux(true);
  }

  @Override
  public BsnFieldValue getBurgerServiceNummer() {

    if (getOverledene().getBurgerServiceNummer().getValue() == null) {
      return new BsnFieldValue();
    }
    return getOverledene().getBurgerServiceNummer();
  }

  public String getPlaatsOverlijden() {
    return getBuitenlandPlaatsOverlijden();
  }

  public void setPlaatsOverlijden(String plaatsOverlijden) {
    setBuitenlandPlaatsOverlijden(plaatsOverlijden);
  }

  public boolean isAangifteVolledig() {
    return true;
  }
}
