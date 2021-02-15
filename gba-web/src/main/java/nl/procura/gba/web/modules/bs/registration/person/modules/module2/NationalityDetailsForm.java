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

package nl.procura.gba.web.modules.bs.registration.person.modules.module2;

import static nl.procura.gba.web.modules.bs.registration.person.modules.module2.NationalityBean.*;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierNationaliteitDatumVanafType.ANDERS;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierNationaliteitDatumVanafType.ERKENNINGS_DATUM;
import static nl.procura.standard.Globalfunctions.emp;

import java.util.function.Consumer;

import com.vaadin.data.Validator;
import com.vaadin.ui.Field;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.containers.NatioContainer;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.modules.bs.registration.AbstractRegistrationForm;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierNationaliteitDatumVanafType;
import nl.procura.gba.web.services.bs.algemeen.functies.BsNatioUtils;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteit;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.table.TableLayout;

class NationalityDetailsForm extends AbstractRegistrationForm<NationalityBean> {

  private final String                         CONFIRM_ON_EMPTY_FOREIGN_ID = "Het buitenlands ID-nr. is niet ingevuld terwijl dit mogelijk wel beschikbaar is. Weet u zeker dat u dit veld leeg wilt laten?";
  private final DossierNationaliteit           nationality;
  private final Consumer<DossierNationaliteit> onCommitListener;

  NationalityDetailsForm(DossierNationaliteit nationality, Consumer<DossierNationaliteit> onCommitListener) {
    this.nationality = nationality;
    this.onCommitListener = onCommitListener;

    setReadonlyAsText(false);
    setColumnWidths("130px", "");
    setOrder(F_NATIONALITY, F_SINCE, F_START_DATE, F_REASON, F_REASON_MANUAL, F_SOURCE, F_EUR_PERSON_NR);

    NationalityBean bean = new NationalityBean();
    bean.setSince(nationality.getTypeVerkrijging() == null ? null : nationality.getVerkrijgingType());
    bean.setStartDate(new GbaDateFieldValue(nationality.getDatumVerkrijging()));
    bean.setReason(nationality.getRedenverkrijgingNederlanderschap());
    bean.setReasonManual(nationality.getRedenverkrijgingNederlanderschap());
    bean.setSource(nationality.getSourceDescription());
    bean.setEuPersonNumber(nationality.getSourceForeignId());
    setBean(bean);
    getField(F_EUR_PERSON_NR).addValidator(new EuPersonNumberValidator(
        () -> getValue(F_NATIONALITY, FieldValue.class)));
  }

  @Override
  public void afterSetBean() {
    super.afterSetBean();

    GbaComboBox nationalityField = getField(F_NATIONALITY, GbaComboBox.class);
    NatioContainer nationalities = (NatioContainer) nationalityField.getContainerDataSource();
    nationalityField.setValue(nationalities.get(nationality.getCNatio()));
    nationalityField.addListener((ValueChangeListener) e -> onNationalityChange(false));

    Field sinceField = getField(F_SINCE);
    sinceField.addListener((ValueChangeListener) e -> onSinceChange(false, new GbaDateFieldValue()));
    onSinceChange(true, getBean().getStartDate());
  }

  @Override
  public void setColumn(final TableLayout.Column column, com.vaadin.ui.Field field, Property property) {
    if (property.is(F_START_DATE)) {
      column.setAppend(true);
    }
    super.setColumn(column, field, property);
  }

  @Override
  public void commit(Runnable next) throws SourceException, Validator.InvalidValueException {
    super.commit();

    final NationalityBean natioBean = getBean();
    nationality.setDatumTijdOntlening(new DateTime());
    nationality.setNationaliteit(natioBean.getNationality());

    final DossierNationaliteitDatumVanafType sinceType = natioBean.getSince();
    nationality.setTypeVerkrijging(sinceType.getCode());

    if (sinceType.is(ERKENNINGS_DATUM, ANDERS)) {
      nationality.setDatumVerkrijging(new DateTime(natioBean.getStartDate().getLongDate()));
    } else if (sinceType == DossierNationaliteitDatumVanafType.ONBEKEND) {
      nationality.setDatumVerkrijging(new DateTime(0L));
    } else {
      nationality.setDatumVerkrijging(new DateTime(-1L));
    }

    if (natioBean.getSince() == ANDERS) {
      nationality.setRedenverkrijgingNederlanderschap(natioBean.getReasonManual());
    } else {
      FieldValue reason = BsNatioUtils.getRedenVerkrijging(natioBean.getNationality(), natioBean.getSince());
      nationality.setRedenverkrijgingNederlanderschap(reason);
    }

    nationality.setSourceDescription(natioBean.getSource());
    nationality.setSourceForeignId(natioBean.getEuPersonNumber());

    if (validateForeignId(natioBean)) {
      getApplication().getParentWindow().addWindow(new ConfirmDialog(CONFIRM_ON_EMPTY_FOREIGN_ID) {

        @Override
        public void buttonYes() {
          onCommitListener.accept(nationality);
          super.buttonYes();
          next.run();
        }
      });
    } else {
      onCommitListener.accept(nationality);
      next.run();
    }
  }

  private void onSinceChange(boolean initial, GbaDateFieldValue startDate) {
    getField(F_START_DATE).setVisible(false);

    final Field sinceField = getField(F_SINCE);
    final DossierNationaliteitDatumVanafType sinceValue = (DossierNationaliteitDatumVanafType) sinceField.getValue();

    if (sinceValue != null && sinceValue.is(ERKENNINGS_DATUM, ANDERS)) {
      getField(F_START_DATE).setValue(startDate);
      getField(F_START_DATE).setVisible(true);
    }

    onNationalityChange(initial);
    repaint();
  }

  private void onNationalityChange(boolean initial) {
    final Field natField = getField(F_NATIONALITY);
    final Field sinceField = getField(F_SINCE);
    final DossierNationaliteitDatumVanafType sinceValue = (DossierNationaliteitDatumVanafType) sinceField.getValue();
    final FieldValue natValue = (FieldValue) natField.getValue();
    getField(F_REASON).setValue(BsNatioUtils.getRedenVerkrijging(natValue, sinceValue));
    getField(F_REASON).setVisible(ANDERS != sinceValue);
    if (!initial) {
      getField(F_REASON_MANUAL).setValue(null);
    }
    getField(F_REASON_MANUAL).setVisible(ANDERS == sinceValue);
  }

  private boolean validateForeignId(final NationalityBean natioBean) {
    return natioBean.getNationality().getAttributes().is("id", "1") && emp(natioBean.getEuPersonNumber());
  }
}
