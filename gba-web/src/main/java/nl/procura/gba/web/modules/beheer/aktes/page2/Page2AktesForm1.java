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

package nl.procura.gba.web.modules.beheer.aktes.page2;

import static nl.procura.gba.web.modules.beheer.aktes.page2.Page2AktesBean1.CATEGORIE;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkteCategorie;

public class Page2AktesForm1 extends GbaForm<Page2AktesBean1> {

  public Page2AktesForm1(DossierAkteCategorie categorie) {

    setCaption("Akte categorie");

    setOrder(CATEGORIE);

    setColumnWidths(WIDTH_130, "");

    Page2AktesBean1 bean = new Page2AktesBean1();

    bean.setCategorie(categorie.getCategorie());

    setBean(bean);
  }
}
