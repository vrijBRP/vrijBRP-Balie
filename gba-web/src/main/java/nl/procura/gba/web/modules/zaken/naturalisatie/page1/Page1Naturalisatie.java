/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.modules.zaken.naturalisatie.page1;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.modules.zaken.bs.page1.Page1BsTemplate;
import nl.procura.gba.web.modules.zaken.common.IdentificatieContactUtils;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.naturalisatie.DossierNaturalisatie;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.gba.web.windows.home.HomeWindow;

public class Page1Naturalisatie extends Page1BsTemplate {

  public Page1Naturalisatie() {
    super("Nationaliteit");
  }

  @Override
  public void onNew() {
    IdentificatieContactUtils.startProcess(this, this::start, false);
  }

  private void start() {
    Dossier dossier = (Dossier) getApplication().getServices().getNaturalisatieService().getNewZaak();
    DossierNaturalisatie zaakDossier = (DossierNaturalisatie) dossier.getZaakDossier();
    BasePLExt pl = getApplication().getServices().getPersonenWsService().getHuidige();
    BsPersoonUtils.kopieDossierPersoon(pl, zaakDossier.getAangever());
    getApplication().getServices().getMemoryService().setObject(Dossier.class, zaakDossier.getDossier());
    getApplication().openWindow(getApplication().getParentWindow(), new HomeWindow(), "bs.naturalisatie");
  }

  @Override
  protected DocumentType getDocumentType(ZaakType zaakType) {
    return DocumentType.ZAAK;
  }

  @Override
  protected String getFragment(ZaakType zaakType) {
    return "bs.naturalisatie";
  }

  @Override
  protected ProfielActie getProfielActie() {
    return ProfielActie.UPDATE_ZAAK_NATURALISATIE;
  }

  @Override
  protected ZaakType[] getZaakTypes() {
    return new ZaakType[]{ ZaakType.NATURALISATIE };
  }
}
