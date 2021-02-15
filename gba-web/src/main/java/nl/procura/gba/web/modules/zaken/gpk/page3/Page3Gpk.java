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

package nl.procura.gba.web.modules.zaken.gpk.page3;

import nl.procura.gba.web.modules.zaken.common.ZakenProcesPrintPage;
import nl.procura.gba.web.modules.zaken.gpk.page1.Page1Gpk;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.gba.web.services.zaken.gpk.GpkAanvraag;
import nl.procura.gba.web.services.zaken.gpk.GpkService;

/**
 * Afdrukken aanvraag
 */

public class Page3Gpk extends ZakenProcesPrintPage<GpkAanvraag, GpkAanvraag> {

  public Page3Gpk(GpkAanvraag aanvraag) {
    super("Gehandicapten parkeerkaart: nieuw", aanvraag, true, DocumentType.GPK_AANVRAAG);
  }

  @Override
  public void onNextPage() {

    GpkService service = getApplication().getServices().getGpkService();
    service.save(getZaak());

    successMessage("De gegevens zijn opgeslagen.");

    getApplication().getProcess().endProcess();
    getNavigation().goToPage(new Page1Gpk());
    getNavigation().removeOtherPages();
  }
}
