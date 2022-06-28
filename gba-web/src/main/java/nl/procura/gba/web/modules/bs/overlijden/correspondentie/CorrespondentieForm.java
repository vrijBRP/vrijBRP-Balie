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

package nl.procura.gba.web.modules.bs.overlijden.correspondentie;

import static nl.procura.gba.web.modules.bs.overlijden.correspondentie.CorrespondentieBean.*;

import java.util.List;

import com.vaadin.ui.Field;

import nl.procura.gba.jpa.personen.db.DossCorrDest;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.algemeen.enums.CommunicatieType;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class CorrespondentieForm extends GbaForm<CorrespondentieBean> {

  public CorrespondentieForm(DossCorrDest correspondentie) {
    setReadThrough(true);
    setCaption("Correspondentie bij online aangifte");
    setColumnWidths("150px", "", "130px", "");
    setOrder(COMMUNICATIE_TYPE, ORGANISATIE, AFDELING,
        NAAM, EMAIL, TELEFOON,
        STRAAT, HNR, HNRL, HNRT, POSTCODE, PLAATS);

    CorrespondentieBean bean = new CorrespondentieBean();
    bean.setCommunicatieType(CommunicatieType.get(correspondentie.getCommunicatieType()));
    bean.setOrganisatie(correspondentie.getOrganisatie());
    bean.setAfdeling(correspondentie.getAfdeling());
    bean.setNaam(correspondentie.getNaam());
    bean.setEmail(correspondentie.getEmail());
    bean.setTelefoon(correspondentie.getTelefoon());
    bean.setStraat(correspondentie.getStraat());
    Integer hnr = correspondentie.getHnr();
    bean.setHnr((hnr != null && hnr > 0) ? hnr.toString() : "");
    bean.setHnrL(correspondentie.getHnrL());
    bean.setHnrT(correspondentie.getHnrT());
    bean.setPostcode(new FieldValue(correspondentie.getPostcode()));
    bean.setPlaats(correspondentie.getPlaats());
    setBean(bean);
    setValidation(bean.getCommunicatieType());
  }

  @Override
  public void afterSetBean() {
    super.afterSetBean();
    getField(COMMUNICATIE_TYPE).addListener((ValueChangeListener) event -> {
      CommunicatieType type = (CommunicatieType) event.getProperty().getValue();
      setValidation(type);
    });
  }

  private void setValidation(CommunicatieType type) {
    boolean isApplicable = type != null && CommunicatieType.NVT != type;
    getAllFields().forEach(f -> f.setVisible(isApplicable));
    getAllFields().forEach(f -> f.setRequired(false));

    if (type == CommunicatieType.EMAIL) {
      getFields(ORGANISATIE, NAAM, EMAIL)
          .forEach(f -> f.setRequired(true));

    } else if (type == CommunicatieType.POST) {
      getFields(ORGANISATIE, NAAM, STRAAT, POSTCODE, PLAATS)
          .forEach(f -> f.setRequired(true));
    }
    repaint();
  }

  private List<Field> getAllFields() {
    return getFields(ORGANISATIE, AFDELING, NAAM, EMAIL, TELEFOON,
        STRAAT, HNR, HNRL, HNRT, POSTCODE, PLAATS);
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {

    if (property.is(COMMUNICATIE_TYPE, ORGANISATIE, AFDELING, EMAIL,
        TELEFOON, NAAM, STRAAT, HNR, POSTCODE)) {
      column.setColspan(3);
    }

    if (property.is(HNR, HNRL, HNRT, PLAATS)) {
      column.setAppend(true);
    }

    super.setColumn(column, field, property);
  }

  @Override
  public Field newField(Field field, Property property) {
    super.newField(field, property);
    return field;
  }
}
