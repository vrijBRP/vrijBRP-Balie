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

package nl.procura.gba.web.modules.bs.onderzoek.aanschrijving;

import static nl.procura.gba.web.modules.bs.common.utils.BsOnderzoekUtils.getDate;
import static nl.procura.gba.web.modules.bs.common.utils.BsOnderzoekUtils.setDateListener;
import static nl.procura.gba.web.modules.bs.onderzoek.aanschrijving.AanschrijvingBean.*;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.*;
import static nl.procura.gba.web.services.bs.onderzoek.enums.AanschrijvingFaseType.*;

import java.util.Date;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.validators.DatumVolgordeValidator;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoek;
import nl.procura.gba.web.services.bs.onderzoek.enums.AanschrijvingFaseType;
import nl.procura.vaadin.component.field.ProDateField;
import nl.procura.vaadin.component.field.ProNativeSelect;

public abstract class AanschrijvingForm extends GbaForm<AanschrijvingBean> {

  private final DossierOnderzoek zaakDossier;

  public AanschrijvingForm(DossierOnderzoek zaakDossier) {
    this.zaakDossier = zaakDossier;

    setCaption("Soort aanschrijving");
    setColumnWidths(WIDTH_130, "");
    setReadonlyAsText(false);

    setOrder(F_SOORT, F_AANSCHRIJFPERSOON, F_EXTERNE_BRON, F_DATUM_FASE1,
        F_DATUM_FASE1_END, F_DATUM_FASE2, F_DATUM_FASE2_END, F_DATUM_EXTRA,
        F_DATUM_EXTRA_END, F_DATUM_VOORNEMEN, F_DATUM_VOORNEMEN_END, F_DATUM_BESLUIT);
    setBean(zaakDossier);
  }

  public void setBean(DossierOnderzoek zaakDossier) {
    AanschrijvingBean bean = new AanschrijvingBean();

    bean.setSoort(zaakDossier.getAanschrijvingFase());

    bean.setDatumFase1(
        getDate(zaakDossier.getAanschrDatumInFase1().getDate(), zaakDossier.getFase1DatumIngang().getDate()));

    bean.setDatumFase1End(
        getDate(zaakDossier.getAanschrDatumEindFase1().getDate(), zaakDossier.getFase1DatumEinde().getDate()));

    bean.setDatumFase2(
        getDate(zaakDossier.getAanschrDatumInFase2().getDate(), zaakDossier.getFase2DatumIngang().getDate()));

    bean.setDatumFase2End(
        getDate(zaakDossier.getAanschrDatumEindFase2().getDate(), zaakDossier.getFase2DatumEinde().getDate()));

    bean.setDatumExtra(zaakDossier.getAanschrDatumInExtra().getDate());
    bean.setDatumExtraEnd(zaakDossier.getAanschrDatumEindExtra().getDate());
    bean.setDatumVoornemen(zaakDossier.getAanschrDatumInVoornemen().getDate());
    bean.setDatumVoornemenEnd(zaakDossier.getAanschrDatumEindVoornemen().getDate());
    bean.setDatumBesluit(zaakDossier.getAanschrDatumBesluit().getDate());

    setBean(bean);
  }

  @Override
  public void afterSetBean() {

    super.afterSetBean();

    getField(F_AANSCHRIJFPERSOON, ProNativeSelect.class).setContainerDataSource(getPersoonContainer());
    getField(F_EXTERNE_BRON, ProNativeSelect.class).setContainerDataSource(getExterneBronContainer());
    getField(F_SOORT, ProNativeSelect.class).setContainerDataSource(new AanschrijvingFaseContainer(zaakDossier));
    getField(F_SOORT).addListener((ValueChangeListener) event -> {
      AanschrijvingFaseType soort = (AanschrijvingFaseType) event.getProperty().getValue();
      updateSoort(soort);
      getWindow().center();
    });

    updateSoort(zaakDossier.getAanschrijvingFase());

    // Fase 1
    ProDateField fase1dIn = getField(F_DATUM_FASE1, ProDateField.class);
    ProDateField fase1dEnd = getField(F_DATUM_FASE1_END, ProDateField.class);

    setValidator(fase1dIn, fase1dEnd, "Aangemaakt op");
    setDateListener(fase1dIn, fase1dEnd, ONDERZ_FASE1_TERMIJN);

    // Fase 2
    ProDateField fase2dIn = getField(F_DATUM_FASE2, ProDateField.class);
    ProDateField fase2dEnd = getField(F_DATUM_FASE2_END, ProDateField.class);

    setValidator(fase2dIn, fase2dEnd, "Aangemaakt op");
    setDateListener(fase2dIn, fase2dEnd, ONDERZ_FASE2_TERMIJN);

    // Extra
    ProDateField extradIn = getField(F_DATUM_EXTRA, ProDateField.class);
    ProDateField extradEnd = getField(F_DATUM_EXTRA_END, ProDateField.class);

    extradIn.setValue(getDate((Date) extradIn.getValue()));
    setValidator(extradIn, extradEnd, "Aangemaakt op");
    setDateListener(extradIn, extradEnd, ONDERZ_EXTRA_TERMIJN);

    // Voornemen
    ProDateField voornemendIn = getField(F_DATUM_VOORNEMEN, ProDateField.class);
    ProDateField voornemendEnd = getField(F_DATUM_VOORNEMEN_END, ProDateField.class);

    voornemendIn.setValue(getDate((Date) voornemendIn.getValue()));
    setValidator(voornemendIn, voornemendEnd, "Datum voornemen");
    setDateListener(voornemendIn, voornemendEnd, ONDERZ_VOORNEMEN_TERMIJN);

    ProDateField besluit = getField(F_DATUM_BESLUIT, ProDateField.class);
    besluit.setValue(getDate((Date) besluit.getValue()));
  }

  @Override
  public AanschrijvingBean getNewBean() {
    return new AanschrijvingBean();
  }

  private void setValidator(ProDateField dIn, ProDateField dEnd, String captionDIn) {
    dEnd.addValidator(new DatumVolgordeValidator(captionDIn, dIn, "Uiterste reactiedatum", dEnd));
  }

  private AanschrijfpersoonContainer getPersoonContainer() {
    return new AanschrijfpersoonContainer(zaakDossier.getBetrokkenen());
  }

  private ExterneBronContainer getExterneBronContainer() {
    return new ExterneBronContainer(zaakDossier.getBronnen());
  }

  private void updateSoort(AanschrijvingFaseType soort) {

    getField(F_AANSCHRIJFPERSOON).setVisible(!FASE_2.equals(soort));
    getField(F_EXTERNE_BRON).setVisible(FASE_2.equals(soort));
    getField(F_DATUM_FASE1).setVisible(FASE_1.equals(soort));
    getField(F_DATUM_FASE1_END).setVisible(FASE_1.equals(soort));
    getField(F_DATUM_FASE2).setVisible(FASE_2.equals(soort));
    getField(F_DATUM_FASE2_END).setVisible(FASE_2.equals(soort));
    getField(F_DATUM_EXTRA).setVisible(FASE_EXTRA.equals(soort));
    getField(F_DATUM_EXTRA_END).setVisible(FASE_EXTRA.equals(soort));
    getField(F_DATUM_VOORNEMEN).setVisible(FASE_VOORNEMEN.equals(soort));
    getField(F_DATUM_VOORNEMEN_END).setVisible(FASE_VOORNEMEN.equals(soort));
    getField(F_DATUM_BESLUIT).setVisible(FASE_BESLUIT.equals(soort));

    onChangeSoort(soort);
    repaint();
  }

  protected abstract void onChangeSoort(AanschrijvingFaseType soort);
}
