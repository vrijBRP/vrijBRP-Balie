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

package nl.procura.gba.web.modules.bs.overlijden.lijkvinding.overzicht;

import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.overlijden.lijkvinding.DossierLijkvinding;

public class LijkvindingOverzichtForm1 extends ReadOnlyForm {

  public LijkvindingOverzichtForm1(DossierLijkvinding dossierLijkvinding) {

    setReadonlyAsText(true);

    setColumnWidths("170px", "");

    init();

    LijkvindingOverzichtBean1 bean = (LijkvindingOverzichtBean1) getNewBean();

    bean.setSchriftelijkeAangever(dossierLijkvinding.getSchriftelijkeAangever());
    StringBuilder plaats = new StringBuilder();
    plaats.append(dossierLijkvinding.getPlaatsLijkvinding().getDescription());
    plaats.append(dossierLijkvinding.getPlaatsToevoeging());
    bean.setPlaatsLijkvinding(plaats.toString());

    bean.setDocument(dossierLijkvinding.getOntvangenDocument().getOms());
    bean.setPartner(
        dossierLijkvinding.getOverledene().getPersonen(DossierPersoonType.PARTNER).size() > 0 ? "Ja" : "Geen");
    bean.setKinderen(
        dossierLijkvinding.getOverledene().getPersonen(DossierPersoonType.KIND).size() > 0 ? "Ja" : "Geen");

    setBean(bean);
  }

  @Override
  public LijkvindingOverzichtBean1 getBean() {
    return (LijkvindingOverzichtBean1) super.getBean();
  }

  @Override
  public Object getNewBean() {
    return new LijkvindingOverzichtBean1();
  }

  protected void init() {
  }
}
