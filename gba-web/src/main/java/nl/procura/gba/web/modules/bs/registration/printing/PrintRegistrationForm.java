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

package nl.procura.gba.web.modules.bs.registration.printing;

import static nl.procura.gba.web.modules.bs.registration.printing.PrintingBean.F_PERSON;
import static nl.procura.gba.web.modules.bs.registration.printing.PrintingBean.F_SORT;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.GERELATEERDE_BRP;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.INSCHRIJVER;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.registration.DossierRegistration;
import nl.procura.vaadin.component.field.ProNativeSelect;

public class PrintRegistrationForm extends GbaForm<PrintingBean> {

  private final DossierRegistration zaakDossier;

  public PrintRegistrationForm(DossierRegistration zaakDossier) {
    this.zaakDossier = zaakDossier;

    setCaption("Soort");
    setColumnWidths("130px", "");
    setReadonlyAsText(false);
    setOrder(F_SORT, F_PERSON);

    setDossierRegistration(zaakDossier);
  }

  public void setDossierRegistration(DossierRegistration zaakDossier) {
    final PrintingBean bean = new PrintingBean();
    bean.setSoort("Eerste inschrijving");
    setBean(bean);
  }

  @Override
  public void afterSetBean() {

    super.afterSetBean();
    getField(F_PERSON, ProNativeSelect.class).setContainerDataSource(getPersoonContainer());
  }

  private PersonContainer getPersoonContainer() {
    return new PersonContainer(zaakDossier.getDossier().getPersonen(INSCHRIJVER, GERELATEERDE_BRP));
  }
}
