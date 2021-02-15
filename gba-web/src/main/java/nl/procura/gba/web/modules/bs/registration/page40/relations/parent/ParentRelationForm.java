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

package nl.procura.gba.web.modules.bs.registration.page40.relations.parent;

import com.vaadin.ui.Field;

import nl.procura.gba.jpa.personen.db.DossPersRelation;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.registration.RelationshipDateType;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class ParentRelationForm extends GbaForm<ParentDetailBean> {

  ParentRelationForm(DossPersRelation relation) {
    setCaption("Aanvullende gegevens");
    setColumnWidths("130px", "");
    setOrder(ParentDetailBean.F_START_OF_RELATION, ParentDetailBean.F_DATE_OF_RELATION_START);

    ParentDetailBean bean = new ParentDetailBean();
    bean.setStartOfRelation(RelationshipDateType.valueOfCode(relation.getStartDateType()));
    bean.setDateOfRelationStart(new GbaDateFieldValue(relation.getCustomStartDate()));
    setBean(bean);
  }

  @Override
  public void setReadOnly(boolean readOnly) {
    super.setReadOnly(readOnly);
    if (readOnly) {
      setOrder(ParentDetailBean.F_START_OF_RELATION_LABEL);
      setBean(new ParentDetailBean());
    }
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {
    if (property.is(ParentDetailBean.F_DATE_OF_RELATION_START)) {
      column.setAppend(true);
    }
    super.setColumn(column, field, property);
  }

  @Override
  public void afterSetBean() {
    super.afterSetBean();
    if (!isReadOnly()) {
      getField(ParentDetailBean.F_START_OF_RELATION).addListener((ValueChangeListener) e -> {
        onStartOfRelationChange((RelationshipDateType) e.getProperty().getValue());
      });
      getField(ParentDetailBean.F_DATE_OF_RELATION_START).setVisible(false);
      onStartOfRelationChange(getBean().getStartOfRelation());
    }
  }

  private void onStartOfRelationChange(RelationshipDateType value) {
    if (RelationshipDateType.CUSTOM == value) {
      getField(ParentDetailBean.F_DATE_OF_RELATION_START).setVisible(true);
    } else {
      getField(ParentDetailBean.F_DATE_OF_RELATION_START).setValue(null);
      getField(ParentDetailBean.F_DATE_OF_RELATION_START).setVisible(false);
    }
    repaint();
  }
}
