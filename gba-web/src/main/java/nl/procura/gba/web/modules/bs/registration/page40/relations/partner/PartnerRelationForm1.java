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

package nl.procura.gba.web.modules.bs.registration.page40.relations.partner;

import static nl.procura.gba.web.modules.bs.registration.page40.relations.partner.PartnerDetailsBean.*;

import nl.procura.gba.jpa.personen.db.DossPersRelation;
import nl.procura.gba.web.common.tables.GbaTables;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.bs.registration.MunicipalityEnabler;
import nl.procura.gba.web.services.bs.registration.CommitmentType;
import nl.procura.standard.Globalfunctions;

public class PartnerRelationForm1 extends GbaForm<PartnerDetailsBean> {

  private final DossPersRelation relation;

  public PartnerRelationForm1(DossPersRelation relation) {
    this.relation = relation;

    setWidth("410px");
    setColumnWidths("130px", "");
    setCaption("Verbintenis huwelijk / GPS");

    PartnerDetailsBean bean = new PartnerDetailsBean();
    if (relation.getCustomStartDate() != null) {
      bean.setStartDate(new GbaDateFieldValue(relation.getCustomStartDate()));
    }
    bean.setCommitmentType(CommitmentType.valueOfCode(relation.getCommitmentType()));
    bean.setStartCountry(GbaTables.LAND.get(relation.getStartDateCountry()));

    if (Globalfunctions.pos(relation.getStartDateMunicipality())) {
      bean.setStartMunicipality(GbaTables.PLAATS.get(relation.getStartDateMunicipality()));
    } else {
      bean.setStartForeignMunicipality(relation.getStartDateMunicipality());
    }

    setBean(bean, F_START_DATE, F_COMMITMENT_TYPE, F_START_COUNTRY, F_START_MUNICIPALITY, F_START_FOREIGN_MUNICIPALITY);
  }

  @Override
  public void afterSetBean() {
    GbaComboBox startCountry = (GbaComboBox) getField(F_START_COUNTRY);
    MunicipalityEnabler startCountryListener = new MunicipalityEnabler(this,
        F_START_MUNICIPALITY, F_START_FOREIGN_MUNICIPALITY);
    startCountry.addListener(startCountryListener);
    startCountryListener.valueChange(new ValueChangeEvent(startCountry));

    super.afterSetBean();
  }

  void saveRelation(DossPersRelation relation) {
    PartnerDetailsBean bean = getBean();
    saveRelationStartDate(bean, relation);
    relation.setCommitmentType(bean.getCommitmentType().getCode());
  }

  private void saveRelationStartDate(PartnerDetailsBean bean, DossPersRelation relation) {
    if (!isReadOnly()) {
      relation.setCustomStartDate(bean.getStartDate().toBigDecimal());
      relation.setStartDateCountry(bean.getStartCountry().getStringValue());
      if (getField(F_START_MUNICIPALITY).isVisible()) {
        relation.setStartDateMunicipality(bean.getStartMunicipality().getStringValue());
      } else {
        relation.setStartDateMunicipality(bean.getStartForeignMunicipality());
      }
    }
  }
}
