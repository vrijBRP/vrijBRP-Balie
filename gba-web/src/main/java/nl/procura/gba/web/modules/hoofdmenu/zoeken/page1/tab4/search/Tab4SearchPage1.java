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

package nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab4.search;

import static nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.ZoekBean.BSN;

import nl.procura.bvbsn.actions.ActionZoekNr;
import nl.procura.gba.web.services.gba.verificatievraag.VerificatievraagService;
import nl.procura.gba.web.theme.GbaWebTheme;

public class Tab4SearchPage1 extends Tab4SearchPage {

  public Tab4SearchPage1() {

    super("Verificatievraag: Toets BSN", BSN);

    getButton1().setStyleName(GbaWebTheme.BUTTON_DEFAULT);
  }

  @Override
  protected void zoek() {

    try {
      VerificatievraagService bvbsn = getApplication().getServices().getVerificatievraagService();
      ActionZoekNr actie = bvbsn.getActionZoekNr(getForm().getBean().getBsn().getStringValue());
      zoek(getWindow(), actie);
    } catch (Exception e) {
      getApplication().handleException(getWindow(), e);
    }
  }
}
