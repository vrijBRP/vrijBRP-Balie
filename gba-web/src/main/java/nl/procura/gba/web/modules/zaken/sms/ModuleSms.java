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

package nl.procura.gba.web.modules.zaken.sms;

import nl.procura.gba.web.common.annotations.ModuleAnnotation;
import nl.procura.gba.web.modules.zaken.ZakenModuleTemplate;
import nl.procura.gba.web.modules.zaken.sms.page1.Page1Sms;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

@ModuleAnnotation(url = "#zaken.sms",
    caption = "SMS",
    profielActie = ProfielActie.SELECT_OPTIE_SMS)
public class ModuleSms extends ZakenModuleTemplate {

  @Override
  public void event(PageEvent event) {
    super.event(event);
    if (event.isEvent(InitPage.class)) {
      getPages().getNavigation().goToPage(Page1Sms.class);
    }
  }
}
