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

package nl.procura.gba.web.modules.bs.geboorte.page20;

import static nl.procura.gba.web.modules.bs.geboorte.page20.Page20GeboorteBean2.TARDIEVE_AANGIFTE;
import static nl.procura.gba.web.modules.bs.geboorte.page20.Page20GeboorteBean2.TARDIEVE_REDEN;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.isTru;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;

public class Page20GeboorteForm4 extends GbaForm<Page20GeboorteBean2> {

  public Page20GeboorteForm4(DossierGeboorte zaakDossier) {

    setColumnWidths("160px", "");
    setOrder(TARDIEVE_AANGIFTE, TARDIEVE_REDEN);

    Page20GeboorteBean2 b = new Page20GeboorteBean2();

    b.setTardieveAangifte(zaakDossier.isTardieveAangifte());
    b.setTardieveReden(zaakDossier.getTardieveReden());

    setBean(b);
  }

  @Override
  public Page20GeboorteBean2 getNewBean() {
    return new Page20GeboorteBean2();
  }

  @Override
  public void setBean(Object bean) {

    super.setBean(bean);

    getField(TARDIEVE_AANGIFTE)
        .addListener((ValueChangeListener) event -> checkRedenTardieve(isTru(astr(event.getProperty().getValue()))));

    checkRedenTardieve(isTru(astr(getField(TARDIEVE_AANGIFTE).getValue())));
  }

  private void checkRedenTardieve(boolean tardieve) {

    getField(TARDIEVE_REDEN).setVisible(tardieve);

    repaint();
  }
}
