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

package nl.procura.gba.web.modules.persoonslijst.afnIndicaties;

import static nl.procura.burgerzaken.gba.core.enums.GBACat.AFNEMERS;

import java.util.List;

import nl.procura.diensten.gba.ple.base.BasePLCat;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.gba.web.common.annotations.ModuleAnnotation;
import nl.procura.gba.web.components.layouts.ModuleTemplate;
import nl.procura.gba.web.modules.persoonslijst.afnIndicaties.page1.Page1AfnIndicaties;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.gba.web.services.gba.ple.PersonenWsService;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

@ModuleAnnotation(url = "#pl.afnIndicaties",
    caption = "Afn. indicaties",
    profielActie = ProfielActie.SELECT_PL_AFN_INDICATIES)
public class ModuleAfnIndicaties extends ModuleTemplate {

  public ModuleAfnIndicaties() {
  }

  @Override
  public void event(PageEvent event) {
    super.event(event);

    if (event.isEvent(InitPage.class)) {

      // Zoek een afnemerindicatie
      PLEArgs args = new PLEArgs();
      args.addNummer(getPl().getPersoon().getNummer().getVal());
      args.setSearchIndications(true);
      args.setReasonForIndications("B"); // Gemeente / RNI
      args.setDatasource(PLEDatasource.GBAV);

      PersonenWsService personenWs = getApplication().getServices().getPersonenWsService();
      List<BasePLExt> pls = personenWs.getPersoonslijsten(args, false).getBasisPLWrappers();
      BasePLCat categorie = new BasePLCat(AFNEMERS);

      for (BasePLExt pl : pls) {
        categorie = pl.getCat(AFNEMERS);
        break;
      }

      Page1AfnIndicaties page1 = new Page1AfnIndicaties(categorie);
      getPages().getNavigation().goToPage(page1);
    }
  }
}
