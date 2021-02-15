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

package nl.procura.gba.web.modules.zaken.vog.page14;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.zaken.vog.VogAanvraag;

public class Page14VogForm1 extends GbaForm<Page14VogBean1> {

  private static final String BYZOMSTANDIGHEDEN     = "byzOmstandigheden";
  private static final String BYZOMSTANDIGHEDENTOEL = "byzOmstandighedenToel";

  public Page14VogForm1(VogAanvraag aanvraag) {

    setCaption("Screening / functiegebieden: bijzondere omstandigheden");
    setOrder(BYZOMSTANDIGHEDEN, BYZOMSTANDIGHEDENTOEL);
    setColumnWidths(WIDTH_130, "");

    Page14VogBean1 b = new Page14VogBean1();
    b.setByzOmstandigheden(aanvraag.getScreening().isOmstandigheden());
    b.setByzOmstandighedenToel(aanvraag.getScreening().getOmstandighedenTekst());

    setBean(b);
    initFields();
  }

  private void checkVelden() {
    checkVelden(BYZOMSTANDIGHEDEN, BYZOMSTANDIGHEDENTOEL);
    repaint();
  }

  private void checkVelden(Object p1, Object p2) {
    getField(p2).setVisible((Boolean) getField(p1).getValue());
  }

  private void initFields() {
    getField(BYZOMSTANDIGHEDEN).addListener((ValueChangeListener) event -> checkVelden());
    checkVelden();
  }
}
