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

import static nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.ZoekBean.*;
import static nl.procura.standard.Globalfunctions.fil;

import nl.procura.bvbsn.actions.ActionVerificatieIdentiteitsDocument;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.ZoekBean;
import nl.procura.gba.web.services.gba.verificatievraag.VerificatievraagService;
import nl.procura.gba.web.theme.GbaWebTheme;

public class Tab4SearchPage5 extends Tab4SearchPage {

  public Tab4SearchPage5() {

    super("Verificatievraag: Toets document", REISDOC, RIJBEWIJS, VREEMDELINGENDOC);

    getButton5().setStyleName(GbaWebTheme.BUTTON_DEFAULT);
  }

  @Override
  protected void zoek() {

    try {

      VerificatievraagService bvbsn = getApplication().getServices().getVerificatievraagService();
      ZoekBean b = getForm().getBean();

      String type = "";
      String waarde = "";

      if (fil(b.getRijbewijs())) {
        type = ActionVerificatieIdentiteitsDocument.TYPE_RIJBEWIJS;
        waarde = b.getRijbewijs();
      } else if (fil(b.getReisdoc())) {
        type = ActionVerificatieIdentiteitsDocument.TYPE_REISDOCUMENT;
        waarde = b.getReisdoc();
      } else if (fil(b.getVreemdelingendoc())) {
        type = ActionVerificatieIdentiteitsDocument.TYPE_VREEMDELINGENDOCUMENT;
        waarde = b.getVreemdelingendoc();
      }

      ActionVerificatieIdentiteitsDocument actie = bvbsn.getActionVerificatieIdentiteitsDocument(type, waarde);

      zoek(getWindow(), actie);
    } catch (Exception e) {
      getApplication().handleException(getWindow(), e);
    }
  }
}
