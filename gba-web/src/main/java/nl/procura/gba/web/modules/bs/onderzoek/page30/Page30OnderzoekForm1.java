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
import static nl.procura.gba.web.modules.bs.onderzoek.page30.Page30OnderzoekBean.REACTIE_ONTVANGEN;
import static nl.procura.gba.web.modules.bs.onderzoek.page30.Page30OnderzoekBean.START_FASE1_OP;
import static nl.procura.gba.web.modules.bs.onderzoek.page30.Page30OnderzoekBean.START_FASE1_TM;
import static nl.procura.gba.web.modules.bs.onderzoek.page30.Page30OnderzoekBean.TOELICHTING1;
import static nl.procura.gba.web.modules.bs.onderzoek.page30.Page30OnderzoekBean.VERVOLGACTIES;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ONDERZ_FASE1_TERMIJN;

import java.util.Date;

import com.vaadin.ui.Field;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.validators.DatumVolgordeValidator;
import nl.procura.gba.web.modules.bs.common.utils.BsOnderzoekUtils;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoek;
import nl.procura.vaadin.component.field.ProDateField;
import nl.procura.vaadin.component.layout.table.TableLayout;

public abstract class Page30OnderzoekForm1 extends GbaForm<Page30OnderzoekBean> {

  private final DossierOnderzoek zaakDossier;

  public Page30OnderzoekForm1(DossierOnderzoek zaakDossier) {
    this.zaakDossier = zaakDossier;
    setCaption("1e fase / tussentijdse beoordeling");
    setColumnWidths("200px", "200px", "130px", "");
    setOrder(START_FASE1_OP, START_FASE1_TM, REACTIE_ONTVANGEN, TOELICHTING1, VERVOLGACTIES);
    setBean(zaakDossier);
  }

  public void setBean(DossierOnderzoek zaakDossier) {

    Date datumIngangFase1 = zaakDossier.getFase1DatumIngang().getDate();
    Date datumEindeFase1 = zaakDossier.getFase1DatumEinde().getDate();
    DateTime datumOnderzoek = zaakDossier.getDatumAanvangOnderzoek();

    Page30OnderzoekBean bean = new Page30OnderzoekBean();
    bean.setStartFase1Op(datumIngangFase1 != null ? datumIngangFase1 : datumOnderzoek.getDate());
    bean.setStartFase1Tm(getDateByTerm(datumEindeFase1, bean.getStartFase1Op(), ONDERZ_FASE1_TERMIJN));
    bean.setReactieOntvangen(zaakDossier.getFase1Reactie());
    bean.setToelichting1(zaakDossier.getFase1Toelichting());
    bean.setVervolgacties(zaakDossier.getFase1Vervolg());

    setBean(bean);
  }

  @Override
  public void afterSetColumn(TableLayout.Column column, Field field, Property property) {

    if (!property.is(START_FASE1_OP, START_FASE1_TM)) {
      column.setColspan(3);
    }

    super.afterSetColumn(column, field, property);
  }

  @Override
  public void afterSetBean() {
    getField(VERVOLGACTIES).addListener((ValueChangeListener) event -> {
      onChangeVervolg((Boolean) event.getProperty().getValue());
      repaint();
    });

    ProDateField datumStart = getField(START_FASE1_OP, ProDateField.class);
    ProDateField datumEinde = getField(START_FASE1_TM, ProDateField.class);

    datumStart.addValidator(new DatumVolgordeValidator("Start 1e fase op", () -> (Date) datumStart.getValue(),
        "Datum einde termijn 1e fase",
        () -> (Date) datumEinde.getValue()));

    BsOnderzoekUtils.setDateListener(datumStart, datumEinde, ONDERZ_FASE1_TERMIJN);
  }

  protected abstract void onChangeVervolg(Boolean binnen);
}
