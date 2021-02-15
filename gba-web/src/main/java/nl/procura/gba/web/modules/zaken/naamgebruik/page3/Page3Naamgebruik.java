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

package nl.procura.gba.web.modules.zaken.naamgebruik.page3;

import nl.procura.gba.web.components.dialogs.GoedkeuringsProcedure;
import nl.procura.gba.web.modules.zaken.common.ZakenProcesPrintPage;
import nl.procura.gba.web.modules.zaken.naamgebruik.page1.Page1Naamgebruik;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.gba.web.services.zaken.naamgebruik.NaamgebruikAanvraag;
import nl.procura.gba.web.services.zaken.naamgebruik.NaamgebruikWijzigingService;

/**
 * Afdrukken aanvraag
 */

public class Page3Naamgebruik extends ZakenProcesPrintPage<NaamgebruikAanvraag, NaamgebruikAanvraag> {

  public Page3Naamgebruik(NaamgebruikAanvraag aanvraag) {
    super("Naamgebruikaanvraag: nieuw", aanvraag, true, DocumentType.NAAMGEBRUIK_AANVRAAG);
  }

  @Override
  public void onNextPage() {

    final NaamgebruikAanvraag aanvraag = getZaak();
    final NaamgebruikWijzigingService service = getApplication().getServices().getNaamgebruikWijzigingService();

    new GoedkeuringsProcedure(getApplication(), aanvraag) {

      @Override
      public void onAkkoord() {

        service.save(aanvraag);
        successMessage("De gegevens zijn opgeslagen.");

        getApplication().getProcess().endProcess();
        getNavigation().goToPage(new Page1Naamgebruik());
        getNavigation().removeOtherPages();
      }
    };
  }
}
