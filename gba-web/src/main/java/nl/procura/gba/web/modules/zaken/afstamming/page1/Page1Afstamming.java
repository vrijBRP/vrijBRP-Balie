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

package nl.procura.gba.web.modules.zaken.afstamming.page1;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.modules.zaken.bs.page1.Page1BsTemplate;
import nl.procura.gba.web.modules.zaken.common.IdentificatieContactUtils;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;

public class Page1Afstamming extends Page1BsTemplate {

  public Page1Afstamming() {
    super("Naam & Afstamming dossiers");
  }

  @Override
  public void onNew() {
    IdentificatieContactUtils.startProcess(this, new AfstammingMultiWindow(), false);
  }

  @Override
  protected DocumentType getDocumentType(ZaakType zaakType) {
    switch (zaakType) {
      case NAAMSKEUZE:
        return DocumentType.NAAMSKEUZE;
      case ERKENNING:
        return DocumentType.ERKENNING;
      case GEBOORTE:
      default:
        return DocumentType.GEBOORTE;
    }
  }

  @Override
  protected String getFragment(ZaakType zaakType) {
    switch (zaakType) {
      case NAAMSKEUZE:
        return "bs.naamskeuze";
      case ERKENNING:
        return "bs.erkenning";
      case GEBOORTE:
      default:
        return "bs.geboorte";
    }
  }

  @Override
  protected ProfielActie getProfielActie() {
    return ProfielActie.UPDATE_ZAAK_AFSTAMMING;
  }

  @Override
  protected ZaakType[] getZaakTypes() {
    return new ZaakType[]{ ZaakType.GEBOORTE, ZaakType.ERKENNING, ZaakType.NAAMSKEUZE };
  }
}
