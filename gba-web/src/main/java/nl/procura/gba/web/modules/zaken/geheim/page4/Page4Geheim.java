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

package nl.procura.gba.web.modules.zaken.geheim.page4;

import nl.procura.gba.web.components.dialogs.GoedkeuringsProcedure;
import nl.procura.gba.web.modules.zaken.common.ZakenProcesPrintPage;
import nl.procura.gba.web.modules.zaken.geheim.page1.Page1Geheim;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.gba.web.services.zaken.geheim.GeheimAanvraag;
import nl.procura.gba.web.services.zaken.geheim.VerstrekkingsBeperkingService;

/**
 * Afdrukken aanvraag
 */

public class Page4Geheim extends ZakenProcesPrintPage<GeheimAanvraag, GeheimAanvraag> {

  public Page4Geheim(GeheimAanvraag aanvraag) {
    super("Verstrekkingsbeperking: afdrukken", aanvraag, true, DocumentType.VERSTREKKINGSBEPERKING_AANVRAAG);
  }

  @Override
  public void onNextPage() {

    final GeheimAanvraag aanvraag = getZaak();
    final VerstrekkingsBeperkingService service = getApplication().getServices().getVerstrekkingsBeperkingService();

    new GoedkeuringsProcedure(getApplication(), aanvraag) {

      @Override
      public void onAkkoord() {

        service.save(aanvraag);
        successMessage("De gegevens zijn opgeslagen.");

        getApplication().getProcess().endProcess();
        getNavigation().goToPage(new Page1Geheim());
        getNavigation().removeOtherPages();
      }
    };
  }
}
