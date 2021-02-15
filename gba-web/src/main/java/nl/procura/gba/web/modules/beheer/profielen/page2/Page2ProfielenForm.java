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

package nl.procura.gba.web.modules.beheer.profielen.page2;

import static nl.procura.gba.web.modules.beheer.profielen.page2.Page2ProfielenBean.OMSCHRIJVING;
import static nl.procura.gba.web.modules.beheer.profielen.page2.Page2ProfielenBean.PROFIEL;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.beheer.profiel.Profiel;

public class Page2ProfielenForm extends GbaForm<Page2ProfielenBean> {

  public Page2ProfielenForm(Profiel profiel) {

    setCaption("Profiel");

    setOrder(PROFIEL, OMSCHRIJVING);

    setColumnWidths("200px", "");

    initFields(profiel);
  }

  @Override
  public void reset() {

    super.reset();

    initFields(new Profiel());
  }

  private void initFields(Profiel profiel) {

    Page2ProfielenBean bean = new Page2ProfielenBean();

    if (profiel.isStored()) {

      bean.setProfiel(profiel.getProfiel());
      bean.setOmschrijving(profiel.getOmschrijving());
    }

    setBean(bean);

    repaint();
  }
}
