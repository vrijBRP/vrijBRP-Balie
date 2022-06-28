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

package nl.procura.gba.web.modules.beheer.aktes.page3;

import static nl.procura.gba.web.modules.beheer.aktes.page3.Page2AktesBean2.*;
import static nl.procura.standard.Globalfunctions.astr;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkteDeel;

public class Page2AktesForm2 extends GbaForm<Page2AktesBean2> {

  public Page2AktesForm2(DossierAkteDeel deel) {
    setCaption("Akte registerdelen");
    setOrder(SOORT, CODE, OMSCHRIJVING, MIN, MAX, EINDDATUM);
    setColumnWidths(WIDTH_130, "");
    setAkteDeel(deel);
  }

  public void setAkteDeel(DossierAkteDeel deel) {
    Page2AktesBean2 bean = new Page2AktesBean2();
    bean.setSoort(deel.getRegisterSoort());
    bean.setCode(deel.getRegisterdeel());
    bean.setMin(astr(deel.getMin()));
    bean.setMax(astr(deel.getMax()));
    bean.setOmschrijving(deel.getOmschrijving());
    bean.setEinddatum(deel.getdEnd());
    setBean(bean);
  }
}
