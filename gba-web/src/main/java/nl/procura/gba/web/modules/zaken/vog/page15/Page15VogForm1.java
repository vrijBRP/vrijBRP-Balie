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

package nl.procura.gba.web.modules.zaken.vog.page15;

import static nl.procura.gba.web.modules.zaken.vog.page15.Page15VogBean1.*;

import com.vaadin.ui.Field;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.zaken.vog.VogAanvraag;

public class Page15VogForm1 extends GbaForm<Page15VogBean1> {

  public Page15VogForm1(VogAanvraag aanvraag) {

    setReadonlyAsText(true);
    setCaption("Opmerkingen");
    setOrder(ADVIES, BIJZONDER, BIJZONDERTOEL, PERSIST, PERSISTTOEL, COVOGADVIES, COVOGADVIESTOEL, ALGTOELICHTING,
        ALGTOELICHTINGTOEL);
    setColumnWidths("200px", "");

    Page15VogBean1 b = new Page15VogBean1();

    b.setAdvies(aanvraag.getOpmerkingen().getBurgemeesterAdvies());

    b.setBijzonder(aanvraag.getOpmerkingen().isByzonderheden());
    b.setBijzonderToel(aanvraag.getOpmerkingen().getByzonderhedenTekst());

    b.setPersist(aanvraag.getOpmerkingen().isPersisteren());
    b.setPersistToel(aanvraag.getOpmerkingen().getPersisterenTekst());

    b.setCovogAdvies(aanvraag.getOpmerkingen().isCovogAdvies());
    b.setCovogAdviesToel(aanvraag.getOpmerkingen().getCovogAdviesTekst());

    b.setAlgToelichting(aanvraag.getOpmerkingen().isToelichting());
    b.setAlgToelichtingToel(aanvraag.getOpmerkingen().getToelichtingTekst());

    setBean(b);

    initFields();
  }

  private void checkVelden() {
    checkVelden(BIJZONDER, BIJZONDERTOEL);
    checkVelden(PERSIST, PERSISTTOEL);
    checkVelden(COVOGADVIES, COVOGADVIESTOEL);
    checkVelden(ALGTOELICHTING, ALGTOELICHTINGTOEL);
    repaint();
  }

  private void checkVelden(Object p1, Object p2) {
    getField(p2).setVisible((Boolean) getField(p1).getValue());
  }

  private void initFields() {

    for (Field f : getFields(BIJZONDER, PERSIST, COVOGADVIES, ALGTOELICHTING)) {
      f.addListener((ValueChangeListener) event -> checkVelden());
    }

    checkVelden();
  }
}
