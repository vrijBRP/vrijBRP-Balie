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

package nl.procura.gba.web.modules.zaken.verhuizing.page27;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6.results.PresentatievraagResultsLayout;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6.results.PresentievraagToelichtingWindow;
import nl.procura.gba.web.modules.zaken.common.ZakenPage;
import nl.procura.gba.web.modules.zaken.verhuizing.VerhuisRelatie;
import nl.procura.gba.web.modules.zaken.verhuizing.page10.Page10Verhuizing;
import nl.procura.gba.web.modules.zaken.verhuizing.page24.Page24Verhuizing;
import nl.procura.gba.web.modules.zaken.verhuizing.page26.Page26Verhuizing;
import nl.procura.gba.web.services.gba.presentievraag.Presentievraag;
import nl.procura.gba.web.services.gba.presentievraag.PresentievraagMatch;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraag;
import nl.procura.proweb.rest.utils.JsonUtils;

public class Page27Verhuizing extends ZakenPage {

  private final VerhuisAanvraag verhuisAanvraag;
  private final VerhuisRelatie  verhuisRelatie;
  private final Presentievraag  presentievraag;

  public Page27Verhuizing(final VerhuisAanvraag verhuisAanvraag, final VerhuisRelatie verhuisRelatie,
      Presentievraag presentievraag) {

    super("Presentievraag - resultaat");

    this.verhuisAanvraag = verhuisAanvraag;
    this.verhuisRelatie = verhuisRelatie;
    this.presentievraag = presentievraag;

    addButton(buttonPrev);

    if (verhuisRelatie != null) {
      addButton(buttonNext);
    }

    PresentatievraagResultsLayout presentatievraagResultsLayout = new PresentatievraagResultsLayout(
        presentievraag) {

      @Override
      protected void navigateToResult(Presentievraag presentievraag, PresentievraagMatch match) {
        getNavigation().goToPage(new Page26Verhuizing(presentievraag, match));
      }
    };

    addExpandComponent(presentatievraagResultsLayout);
  }

  @Override
  public void onNextPage() {

    if (buttonNext.getParent() != null) {

      presentievraag.setZaakId(verhuisAanvraag.getZaakId());
      presentievraag.setAntwoord(JsonUtils.toJson(presentievraag.getPresentievraagAntwoord()).trim());

      String defaultText = "Deze presentievraag is uitgevoerd ten behoeve van een hervestiging.";
      PresentievraagToelichtingWindow commentaarWindow = new PresentievraagToelichtingWindow(defaultText) {

        @Override
        public void onSave(String comment) {

          presentievraag.setToelichting(comment);
          GbaApplication.getInstance().getServices().getPresentievraagService().save(presentievraag);

          verhuisRelatie.setPresentievraagType(presentievraag.getPresentievraagAntwoord().getResultaatType());

          if (getNavigation().getPage(Page24Verhuizing.class).zijnAllePresentievragenUitgevoerd()) {
            successMessage("Alle benodigde presentievragen zijn uitgevoerd");
            getNavigation().goBackToPage(Page10Verhuizing.class);
          } else {
            getNavigation().goBackToPage(Page24Verhuizing.class);
          }
        }
      };

      getParentWindow().addWindow(commentaarWindow);
    }
  }
}
