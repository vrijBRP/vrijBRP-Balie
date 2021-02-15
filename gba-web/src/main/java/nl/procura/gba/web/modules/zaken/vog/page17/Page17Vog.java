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

package nl.procura.gba.web.modules.zaken.vog.page17;

import nl.procura.gba.web.components.dialogs.GoedkeuringsProcedure;
import nl.procura.gba.web.modules.zaken.common.ZakenProcesPrintPage;
import nl.procura.gba.web.modules.zaken.vog.page1.Page1Vog;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.gba.web.services.zaken.vog.VogAanvraag;
import nl.procura.gba.web.services.zaken.vog.VogBerichtService;
import nl.procura.gba.web.services.zaken.vog.VogsService;

/**
 * Afdrukken aanvraag
 */
public class Page17Vog extends ZakenProcesPrintPage<VogAanvraag, VogAanvraag> {

  public Page17Vog(VogAanvraag aanvraag) {
    super("Verklaring omtrent gedrag: nieuw", aanvraag, true, DocumentType.COVOG_AANVRAAG);
  }

  @Override
  public void onNextPage() {

    final VogAanvraag zaak = getZaak();
    final VogBerichtService service = getServices().getVogBerichtService();

    new GoedkeuringsProcedure(getApplication(), zaak) {

      @Override
      public void onAkkoord() {

        service.registreer(zaak);

        VogsService service = getApplication().getServices().getVogService();
        service.save(zaak);

        successMessage("De gegevens zijn opgeslagen en verstuurd.");
        getApplication().getProcess().endProcess();
        getNavigation().goToPage(new Page1Vog());
        getNavigation().removeOtherPages();
      }
    };
  }
}
