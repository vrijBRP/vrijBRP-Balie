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

import static nl.procura.gba.web.modules.bs.geboorte.page20.Page20GeboorteBean2.REDEN_VERPLICHT;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.bs.geboorte.RedenVerplicht;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.vaadin.component.field.ProNativeSelect;

public class Page20GeboorteForm2 extends GbaForm<Page20GeboorteBean2> {

  public Page20GeboorteForm2(DossierGeboorte zaakDossier) {

    setColumnWidths("160px", "");
    setCaption("Aangifte");
    setOrder(REDEN_VERPLICHT);

    ZaakType zaakType = zaakDossier.getDossier().getType();
    RedenVerplicht reden = zaakDossier.getRedenVerplichtBevoegd();
    Geslacht geslacht = zaakDossier.getAangever().getGeslacht();

    setBean(new Page20GeboorteBean2(reden));
    getField(REDEN_VERPLICHT, ProNativeSelect.class).setDataSource(new RedenVerplichtContainer(zaakType, geslacht));
  }

  @Override
  public Page20GeboorteBean2 getNewBean() {
    return new Page20GeboorteBean2();
  }
}
