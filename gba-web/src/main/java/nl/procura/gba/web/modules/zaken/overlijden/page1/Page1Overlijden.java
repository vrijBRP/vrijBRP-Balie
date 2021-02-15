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

package nl.procura.gba.web.modules.zaken.overlijden.page1;

import static nl.procura.gba.common.ZaakType.*;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.modules.zaken.bs.page1.Page1BsTemplate;
import nl.procura.gba.web.modules.zaken.common.IdentificatieContactUtils;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;

/**
 * Overlijden dossiers
 */
public class Page1Overlijden extends Page1BsTemplate {

  public Page1Overlijden() {
    super("Overlijden dossiers");
  }

  @Override
  public void onNew() {
    IdentificatieContactUtils.startProcess(this, new OverlijdenMultiWindow(), false);
  }

  @Override
  protected DocumentType getDocumentType(ZaakType zaakType) {

    if (zaakType == ZaakType.LEVENLOOS) {
      return DocumentType.LEVENLOOS;
    }

    return DocumentType.OVERLIJDEN;
  }

  @Override
  protected String getFragment(ZaakType zaakType) {

    switch (zaakType) {
      case LEVENLOOS:
        return "bs.levenloos";

      case LIJKVINDING:
        return "bs.lijkvinding";

      default:
        return "bs.overlijden.gemeente";
    }
  }

  @Override
  protected ProfielActie getProfielActie() {
    return ProfielActie.UPDATE_ZAAK_OVERLIJDEN;
  }

  @Override
  protected ZaakType[] getZaakTypes() {
    return new ZaakType[]{ OVERLIJDEN_IN_GEMEENTE, LIJKVINDING, LEVENLOOS };
  }
}
