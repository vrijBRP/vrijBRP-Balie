/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.zaken.onderzoek.page1;

import nl.procura.gba.common.ZaakFragment;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.modules.zaken.bs.page1.Page1BsTemplate;
import nl.procura.gba.web.modules.zaken.common.IdentificatieContactUtils;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;

/**
 * Onderzoeksdossiers
 */
public class Page1Onderzoek extends Page1BsTemplate {

  public Page1Onderzoek() {
    super("Onderzoeken");
  }

  @Override
  public void onNew() {
    IdentificatieContactUtils.startProcess(this, new OnderzoekMultiWindow(), false);
  }

  @Override
  protected DocumentType getDocumentType(ZaakType zaakType) {
    return DocumentType.ONDERZOEK;
  }

  @Override
  protected String getFragment(ZaakType zaakType) {
    return ZaakFragment.FR_ONDERZOEK;
  }

  @Override
  protected ProfielActie getProfielActie() {
    return ProfielActie.UPDATE_ZAAK_ONDERZOEK;
  }

  @Override
  protected ZaakType[] getZaakTypes() {
    return new ZaakType[]{ ZaakType.ONDERZOEK };
  }
}
