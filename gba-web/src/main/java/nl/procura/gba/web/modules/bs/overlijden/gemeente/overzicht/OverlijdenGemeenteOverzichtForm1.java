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

package nl.procura.gba.web.modules.bs.overlijden.gemeente.overzicht;

import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.overlijden.gemeente.DossierOverlijdenGemeente;

public class OverlijdenGemeenteOverzichtForm1 extends ReadOnlyForm {

  public OverlijdenGemeenteOverzichtForm1(DossierOverlijdenGemeente dossierOverlijden) {

    setColumnWidths("170px", "");

    init();

    OverlijdenGemeenteOverzichtBean1 bean = (OverlijdenGemeenteOverzichtBean1) getNewBean();

    bean.setNaam(dossierOverlijden.getAangever().getNaam().getGesl_pred_init_adel_voorv());
    bean.setGeboren(dossierOverlijden.getAangever().getGeboorte().getDatum_te_plaats());
    bean.setDocument(dossierOverlijden.getOntvangenDocument().getOms());
    bean.setPartner(
        dossierOverlijden.getOverledene().getPersonen(DossierPersoonType.PARTNER).size() > 0 ? "Ja" : "Geen");
    bean.setKinderen(
        dossierOverlijden.getOverledene().getPersonen(DossierPersoonType.KIND).size() > 0 ? "Ja" : "Geen");

    setBean(bean);
  }

  @Override
  public OverlijdenGemeenteOverzichtBean1 getBean() {
    return (OverlijdenGemeenteOverzichtBean1) super.getBean();
  }

  @Override
  public Object getNewBean() {
    return new OverlijdenGemeenteOverzichtBean1();
  }

  protected void init() {
  }
}
