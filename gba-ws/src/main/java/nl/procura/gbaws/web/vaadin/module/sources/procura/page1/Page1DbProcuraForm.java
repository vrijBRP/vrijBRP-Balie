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

package nl.procura.gbaws.web.vaadin.module.sources.procura.page1;

import nl.procura.gbaws.web.vaadin.layouts.forms.DefaultForm;

public class Page1DbProcuraForm extends DefaultForm {

  public Page1DbProcuraForm(String caption, String... names) {
    setCaption(caption);
    setOrder(names);
    setColumnWidths("160px", "");
  }

  @Override
  public Object getNewBean() {
    return new Page1DbProcuraBean();
  }

  @Override
  public Page1DbProcuraBean getBean() {
    return (Page1DbProcuraBean) super.getBean();
  }
}
