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

package nl.procura.gba.web.modules.bs.registration.page40.relations;

import static nl.procura.gba.web.modules.bs.registration.page40.relations.RelativeBean.*;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.ui.Field;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.vaadin.component.layout.table.TableLayout;

public class RelativeForm extends GbaForm<RelativeBean> {

  public RelativeForm(DossierPersoon person, String caption) {
    setCaption(caption);
    setColumnWidths("130px", "260px", "130px", "");
    setOrder(F_IDS, F_GENDER, F_NAME, F_BIRTH);
    initFields(person);
  }

  private void initFields(DossierPersoon person) {
    final RelativeBean bean = new RelativeBean();
    bean.setIds(formatIds(person));
    bean.setName(person.getNaam().getNaam_naamgebruik_eerste_voornaam());
    bean.setGender(person.getGeslacht());
    bean.setBirth(person.getGeboorte().getDatum_plaats_land());
    setBean(bean);
  }

  private String formatIds(DossierPersoon person) {
    String bsn = person.getBurgerServiceNummer().toString();
    String anr = person.getAnummer().toString();
    return StringUtils.isBlank(bsn) ? "" : bsn + " / " + anr;
  }

  @Override
  public void afterSetColumn(TableLayout.Column column, Field field, Property property) {
    if (property.is(F_NAME, F_BIRTH)) {
      column.setColspan(3);
    }
    super.afterSetColumn(column, field, property);
  }
}
