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

import static nl.procura.commons.core.utils.ProNumberUtils.isPos;
import static nl.procura.gba.web.modules.bs.registration.page40.relations.partner.PartnerDetailsBean.*;

import java.math.BigDecimal;

import com.vaadin.ui.Field;

import nl.procura.gba.jpa.personen.db.DossPersRelation;
import nl.procura.gba.web.common.tables.GbaTables;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.bs.registration.MunicipalityEnabler;
import nl.procura.gba.web.services.bs.registration.CommitmentType;
import nl.procura.standard.Globalfunctions;
import nl.procura.vaadin.component.layout.table.TableLayout;

public class PartnerRelationForm2 extends GbaForm<PartnerDetailsBean> {

  private final DossPersRelation relation;

  public PartnerRelationForm2(DossPersRelation relation) {
    this.relation = relation;

    setOrder(F_END, F_END_DATE, F_END_REASON, F_END_COUNTRY, F_END_MUNICIPALITY, F_END_FOREIGN_MUNICIPALITY);
    setColumnWidths("130px", "75px", "60px", "");
    setCaption("Ontbinding huwelijk / GPS");

    PartnerDetailsBean bean = new PartnerDetailsBean();
    bean.setCommitmentType(CommitmentType.valueOfCode(relation.getCommitmentType()));
    if (relation.getCustomEndDate() != null) {
      bean.setEndDate(new GbaDateFieldValue(relation.getCustomEndDate()));
    }

    bean.setEnd(isPos(relation.getCustomEndDate()));
    bean.setEndReason(GbaTables.REDEN_HUW_ONTB.get(relation.getEndReason()));
    bean.setEndCountry(GbaTables.LAND.get(relation.getStartDateCountry()));

    String endDateMunicipality = relation.getEndDateMunicipality();
    if (Globalfunctions.pos(endDateMunicipality)) {
      bean.setEndMunicipality(GbaTables.PLAATS.get(endDateMunicipality));
    } else {
      bean.setEndForeignMunicipality(endDateMunicipality);
    }

    setBean(bean);
    onEndChange(bean.getEnd());
    repaint();
  }

  @Override
  public void afterSetBean() {
    GbaComboBox endCountry = (GbaComboBox) getField(PartnerDetailsBean.F_END_COUNTRY);
    MunicipalityEnabler endCountryListener = new MunicipalityEnabler(this, F_END_MUNICIPALITY,
        F_END_FOREIGN_MUNICIPALITY);
    endCountry.addListener(endCountryListener);
    endCountryListener.valueChange(new ValueChangeEvent(endCountry));

    getField(F_END).addListener((ValueChangeListener) e -> {
      onEndChange((Boolean) e.getProperty().getValue());
      repaint();
    });

    super.afterSetBean();
  }

  private void onEndChange(Boolean value) {
    if (value) {
      setColumnWidths("130px", "75px", "60px", "");
    } else {
      setColumnWidths("130px", "");
      getField(F_END_MUNICIPALITY).setValue(null);
      getField(F_END_DATE).setValue(null);
      getField(F_END_FOREIGN_MUNICIPALITY).setValue(null);
      getField(F_END_COUNTRY).setValue(null);
      getField(F_END_REASON).setValue(null);
      getField(F_END_MUNICIPALITY).setVisible(false);
      getField(F_END_FOREIGN_MUNICIPALITY).setVisible(false);
    }

    getField(F_END_DATE).setVisible(value);
    getField(F_END_DATE).focus();
    getField(F_END_COUNTRY).setVisible(value);
    getField(F_END_REASON).setVisible(value);
  }

  @Override
  public void setColumn(TableLayout.Column column, Field field, Property property) {
    if (property.is(F_END_COUNTRY, F_END_MUNICIPALITY, F_END_FOREIGN_MUNICIPALITY, F_END_REASON)) {
      column.setColspan(3);
    }
    super.setColumn(column, field, property);
  }

  void saveRelation(DossPersRelation relation) {
    PartnerDetailsBean bean = getBean();
    saveRelationEndDate(bean, relation);
  }

  private void saveRelationEndDate(PartnerDetailsBean bean, DossPersRelation relation) {
    if (!isReadOnly()) {
      relation.setEndDateCountry("");
      relation.setEndDateMunicipality("");
      relation.setEndReason("");
      relation.setCustomEndDate(BigDecimal.valueOf(-1L));

      if (bean.getEnd()) {
        relation.setCustomEndDate(bean.getEndDate().toBigDecimal());
        relation.setEndDateCountry(bean.getEndCountry().getStringValue());
        relation.setEndReason(bean.getEndReason().getStringValue());

        if (getField(F_END_MUNICIPALITY).isVisible()) {
          relation.setEndDateMunicipality(bean.getEndMunicipality().getStringValue());
        } else {
          relation.setEndDateMunicipality(bean.getEndForeignMunicipality());
        }
      }
    }
  }
}
