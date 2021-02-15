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

package nl.procura.gba.web.modules.zaken.reisdocument.page19;

import nl.procura.gba.web.modules.zaken.common.ZakenProcesPrintPage;
import nl.procura.gba.web.modules.zaken.reisdocument.page1.Page1Reisdocument;
import nl.procura.gba.web.services.zaken.algemeen.status.ZaakStatusService;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentAanvraag;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentService;

/**
 * Afdrukken aanvraag
 */
public class Page19Reisdocument extends ZakenProcesPrintPage<ReisdocumentAanvraag, ReisdocumentAanvraag> {

  public Page19Reisdocument(ReisdocumentAanvraag aanvraag) {
    super("Aanvraag reisdocument: afdrukken", aanvraag, true, DocumentType.REISDOCUMENT_AANVRAAG);
  }

  @Override
  public void onNextPage() {

    ReisdocumentService service = getApplication().getServices().getReisdocumentService();
    service.save(getZaak());

    ZaakStatusService statusService = getServices().getZaakStatusService();
    statusService.updateStatus(getZaak(), getZaak().getStatus(),
        statusService.getInitieleStatus(getZaak()),
        "Aanvraag opgeslagen");

    successMessage("De gegevens zijn opgeslagen.");

    getApplication().getProcess().endProcess();
    getNavigation().goToPage(new Page1Reisdocument());
    getNavigation().removeOtherPages();
  }
}
