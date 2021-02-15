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

package nl.procura.gba.web.modules.beheer.onderhoud.rdw.page3;

import static nl.procura.gba.web.modules.beheer.onderhoud.rdw.page3.Page3RdwBean.WACHTWOORD;

import nl.procura.gba.web.components.layouts.form.GbaForm;

public class Page3RdwForm extends GbaForm<Page3RdwBean> {

  public Page3RdwForm() {
    setColumnWidths("120px", "");
    setOrder(WACHTWOORD);
    setBean(new Page3RdwBean());
  }

  public void setWachtwoord(String wachtwoord) {
    setBean(new Page3RdwBean(wachtwoord));
  }
}
