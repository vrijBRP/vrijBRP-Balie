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

package nl.procura.gba.web.modules.zaken.huwelijk.page1;

import nl.procura.gba.common.ZaakFragment;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.modules.zaken.bs.page1.Page1BsTemplate;
import nl.procura.gba.web.modules.zaken.common.IdentificatieContactUtils;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;

/**
 * Huwelijks dossiers
 */
public class Page1Huwelijk extends Page1BsTemplate {

  public Page1Huwelijk() {
    super("Huwelijk dossiers");
  }

  @Override
  public void onNew() {
    IdentificatieContactUtils.startProcess(this, new HuwelijkMultiWindow(), false);
  }

  @Override
  protected DocumentType getDocumentType(ZaakType zaakType) {
    switch (zaakType) {
      case OMZETTING_GPS:
        return DocumentType.GPS_OMZETTING;

      case ONTBINDING_GEMEENTE:
        return DocumentType.ONTBINDING_GEMEENTE;

      default:
        return DocumentType.HUWELIJK;
    }
  }

  @Override
  protected String getFragment(ZaakType zaakType) {
    switch (zaakType) {
      case OMZETTING_GPS:
        return ZaakFragment.FR_OMZETTING_GPS;

      case ONTBINDING_GEMEENTE:
        return ZaakFragment.FR_ONTBINDING;

      default:
        return ZaakFragment.FR_HUWELIJK;
    }
  }

  @Override
  protected ProfielActie getProfielActie() {
    return ProfielActie.UPDATE_ZAAK_HUWELIJKGPS;
  }

  @Override
  protected ZaakType[] getZaakTypes() {
    return new ZaakType[]{ ZaakType.HUWELIJK_GPS_GEMEENTE, ZaakType.OMZETTING_GPS, ZaakType.ONTBINDING_GEMEENTE };
  }
}
