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

package nl.procura.gba.web.modules.bs.onderzoek.page30;

import static nl.procura.gba.web.modules.bs.common.utils.BsOnderzoekUtils.getDateByTerm;
import static nl.procura.gba.web.modules.bs.onderzoek.page30.Page30OnderzoekBean.ONDERZOEK_TER_PLAATSE;
import static nl.procura.gba.web.modules.bs.onderzoek.page30.Page30OnderzoekBean.START_FASE1_OP;
import static nl.procura.gba.web.modules.bs.onderzoek.page30.Page30OnderzoekBean.START_FASE1_TM;
import static nl.procura.gba.web.modules.bs.onderzoek.page30.Page30OnderzoekBean.START_FASE2_OP;
import static nl.procura.gba.web.modules.bs.onderzoek.page30.Page30OnderzoekBean.START_FASE2_TM;
import static nl.procura.gba.web.modules.bs.onderzoek.page30.Page30OnderzoekBean.TOELICHTING2;
import static nl.procura.gba.web.modules.bs.onderzoek.page30.Page30OnderzoekBean.UITGEVOERD_OP;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ONDERZ_FASE2_TERMIJN;

import java.util.Date;

import com.vaadin.ui.Field;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.validators.DatumVolgordeValidator;
import nl.procura.gba.web.modules.bs.common.utils.BsOnderzoekUtils;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoek;
import nl.procura.vaadin.component.field.ProDateField;
import nl.procura.vaadin.component.layout.table.TableLayout;

public class Page30OnderzoekForm2 extends GbaForm<Page30OnderzoekBean> {

  private final Page30OnderzoekForm1 form1;
  private final DossierOnderzoek     zaakDossier;

  public Page30OnderzoekForm2(Page30OnderzoekForm1 form1, DossierOnderzoek zaakDossier) {
    this.form1 = form1;
    this.zaakDossier = zaakDossier;
    setCaption("2e fase / vervolgactie(s)");
    setColumnWidths("200px", "200px", "130px", "");
    setOrder(START_FASE2_OP, START_FASE2_TM, ONDERZOEK_TER_PLAATSE, UITGEVOERD_OP,
        TOELICHTING2);
    setBean(zaakDossier);
  }

  public void setBean(DossierOnderzoek zaakDossier) {
    Page30OnderzoekBean bean = new Page30OnderzoekBean();

    Date datumEindeFase1 = (Date) form1.getField(START_FASE1_TM).getValue();
    Date datumIngangFase2 = zaakDossier.getFase2DatumIngang().getDate();
    Date datumEindeFase2 = zaakDossier.getFase2DatumEinde().getDate();

    bean.setStartFase2Op(BsOnderzoekUtils.getDate(datumIngangFase2, datumEindeFase1));
    bean.setStartFase2Tm(getDateByTerm(datumEindeFase2, bean.getStartFase2Op(), ONDERZ_FASE2_TERMIJN));
    bean.setOnderzoekTerPlaatse(zaakDossier.getFase2OnderzoekGewenst());
    bean.setUitgevoerdOp(zaakDossier.getFase2DatumOnderzoek().getDate());
    bean.setToelichting2(zaakDossier.getFase2Toelichting());

    setBean(bean);
  }

  @Override
  public void afterSetBean() {

    ProDateField datumStart = getField(START_FASE2_OP, ProDateField.class);
    ProDateField datumEinde = getField(START_FASE2_TM, ProDateField.class);
    ProDateField datumOnderzoek = getField(UITGEVOERD_OP, ProDateField.class);

    datumStart.addValidator(new DatumVolgordeValidator("Start 2e fase op", () -> (Date) datumStart.getValue(),
        "Datum einde termijn 2e fase",
        () -> (Date) datumEinde.getValue()));

    ProDateField datumStartFase1 = form1.getField(START_FASE1_OP, ProDateField.class);
    ProDateField datumEindeFase1 = form1.getField(START_FASE1_TM, ProDateField.class);
    ProDateField datumStartFase2 = getField(START_FASE2_OP, ProDateField.class);

    datumStart.addValidator(
        new DatumVolgordeValidator("Datum start 1e fase", () -> (Date) datumStartFase1.getValue(),
            "Datum start 2e fase", () -> (Date) datumStartFase2.getValue()));

    datumOnderzoek.addValidator(
        new DatumVolgordeValidator("Datum start 2e fase", () -> (Date) datumStartFase2.getValue(),
            "Datum onderzoek", () -> (Date) datumOnderzoek.getValue()));

    getField(ONDERZOEK_TER_PLAATSE).addListener((ValueChangeListener) event -> {
      onChangeOnderzoekTerPlaatse((Boolean) event.getProperty().getValue());
      repaint();
    });

    BsOnderzoekUtils.setDateListener(datumStart, datumEinde, ONDERZ_FASE2_TERMIJN);
    onChangeOnderzoekTerPlaatse(getBean().getOnderzoekTerPlaatse());
  }

  private void onChangeOnderzoekTerPlaatse(Boolean value) {
    boolean onderzoek = (value != null && value);
    getField(UITGEVOERD_OP).setVisible(onderzoek);
    getField(TOELICHTING2).setVisible(onderzoek);
    if (!onderzoek) {
      getField(UITGEVOERD_OP).setValue(null);
      getField(TOELICHTING2).setValue("");
    }
  }

  @Override
  public void afterSetColumn(TableLayout.Column column, Field field, Property property) {

    if (!property.is(START_FASE2_OP, START_FASE2_TM)) {
      column.setColspan(3);
    }

    super.afterSetColumn(column, field, property);
  }
}
