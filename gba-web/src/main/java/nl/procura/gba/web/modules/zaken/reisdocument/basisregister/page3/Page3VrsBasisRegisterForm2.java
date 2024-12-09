/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.zaken.reisdocument.basisregister.page3;

import static nl.procura.gba.web.modules.zaken.reisdocument.basisregister.page3.Page3VrsBasisRegisterBean1.BRP_ACTIE;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhouding;
import nl.procura.gba.web.services.zaken.inhoudingen.InhoudingType;

public class Page3VrsBasisRegisterForm2 extends GbaForm<Page3VrsBasisRegisterBean1> {

  private final DocumentInhouding inhouding;

  public Page3VrsBasisRegisterForm2(DocumentInhouding inhouding) {
    this.inhouding = inhouding;
    setCaption("BRP wijziging");
    setColumnWidths("160px", "");
    setOrder(BRP_ACTIE);
    setReadonlyAsText(false);
    updateBrpActie(inhouding.getInhoudingType());
  }

  public void updateBrpActie(InhoudingType inhoudingType) {
    Page3VrsBasisRegisterBean1 bean = new Page3VrsBasisRegisterBean1();
    if (inhouding.isVrsOnlyBasisregister()) {
      bean.setBrpActie("Niet van toepassing (document alleen geregistreerd in het basisregister)");
    } else {
      switch (inhoudingType) {
        case INHOUDING:
          bean.setBrpActie("Wijziging categorie 12: inhouding");
          break;
        case VERMISSING:
          bean.setBrpActie("Wijziging categorie 12: vermissing");
          break;
        case VAN_RECHTSWEGE_VERVALLEN:
          bean.setBrpActie("Wijziging categorie 12: van rechtswege vervallen");
          break;
        default:
          break;
      }
    }
    setBean(bean);
  }
}