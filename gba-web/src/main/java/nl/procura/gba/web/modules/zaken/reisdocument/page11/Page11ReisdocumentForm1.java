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

package nl.procura.gba.web.modules.zaken.reisdocument.page11;

import static nl.procura.gba.web.components.containers.Container.LAND;
import static nl.procura.gba.web.modules.zaken.reisdocument.page11.Page11ReisdocumentBean1.*;
import static nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentUtils.*;
import static nl.procura.standard.Globalfunctions.pos;

import java.util.List;

import com.vaadin.ui.Field;

import nl.procura.gba.web.components.fields.ExtendedMultiField;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentAanvraag;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentService;
import nl.procura.gba.web.services.zaken.reisdocumenten.clausule.VermeldPartnerType;
import nl.procura.gba.web.services.zaken.reisdocumenten.clausule.VermeldTitelType;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.container.ArrayListContainer;
import nl.procura.vaadin.component.field.ProNativeSelect;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page11ReisdocumentForm1 extends GbaForm<Page11ReisdocumentBean1> {

  private final ReisdocumentService  reisdocumenten;
  private final ReisdocumentAanvraag aanvraag;

  public Page11ReisdocumentForm1(ReisdocumentService reisdocumenten, ReisdocumentAanvraag aanvraag) {

    this.reisdocumenten = reisdocumenten;
    this.aanvraag = aanvraag;

    setCaption("Reisdocumentgegevens");
    setReadThrough(true);
    setOrder(VERMELDING_PARTNER, VERMELDING_TITEL, VERMELDING_LAND, PSEUDONIEM, ONDERTEKENING, BUITENLDOC,
        NRVBDOC, DVBDOC, TERMIJNGELD, DGELDEND, DGELDENDOMS, NATIOGBA, UITGEZONDERDELANDEN,
        GELDIGVOORREIZENNAAR, STAATLOOS, TERVERVANGINGVAN);
    setColumnWidths("220px", "");

    Page11ReisdocumentBean1 bean = new Page11ReisdocumentBean1();

    ProcuraDate dIn = getDatumGeldigMin(aanvraag.getBasisPersoon());
    ProcuraDate dEnd = getDatumGeldigMax(reisdocumenten, aanvraag.getReisdocumentType(), aanvraag.getBasisPersoon());

    bean.setdGeldEndOms(String.format("(Geef een datum tussen <b>%s</b> en <b>%s</b>)",
        dIn.getFormatDate(), dEnd.getFormatDate()));
    bean.setdGeldEnd(new DateFieldValue(dEnd.getSystemDate()));
    bean.setTermijnGeld(new FieldValue(aanvraag.getGeldigheidsTermijn()));
    bean.setNatioGBA(aanvraag.getBasisPersoon().getNatio().getNationaliteiten());

    // Clausules
    bean.setStaatloos(aanvraag.getClausules().getStaatloos());
    bean.setGeldigVoorReizenNaar(aanvraag.getClausules().getGeldigVoorReizen());
    bean.setOndertekening(aanvraag.getClausules().getOndertekening());
    bean.setPseudoniem(aanvraag.getClausules().getPseudoniem());
    bean.setTerVervangingVan(aanvraag.getClausules().getTvv());
    bean.setBuitenlDoc(aanvraag.getClausules().isInBezitBuitenlandsDocument());

    // Verblijfsdocument
    bean.setNrVbDoc(aanvraag.getNrVbDocument());
    bean.setdVbDoc(new DateFieldValue(aanvraag.getDatumEindeGeldigheidVb().getIntDate()));

    setBean(bean);

    List<FieldValue> termijnen = getGeldigTermijnen(reisdocumenten, aanvraag.getReisdocumentType(),
        aanvraag.getBasisPersoon());
    ArrayListContainer termijnenContainer = new ArrayListContainer(termijnen);
    ProNativeSelect termijnField = getField(TERMIJNGELD, ProNativeSelect.class);
    termijnField.setDataSource(termijnenContainer);

    // Einde geldigheid
    Field dGeldEndField = getField(DGELDEND);
    dGeldEndField.addValidator(new DatEndGeldValidator(dIn, dEnd));

    if (aanvraag.getGeldigheidsTermijn().intValue() >= 0) {
      getField(TERMIJNGELD).setValue(new FieldValue(aanvraag.getGeldigheidsTermijn()));
      dGeldEndField.setValue(new DateFieldValue(aanvraag.getDatumEindeGeldigheid().getIntDate()));
    }

    // Uitgezonderde landen
    ExtendedMultiField uitgLandenField = getField(UITGEZONDERDELANDEN, ExtendedMultiField.class);
    uitgLandenField.setContainer(LAND);
    uitgLandenField.setValue(aanvraag.getClausules().getUitzonderingLanden());

    // Partner
    PartnervermeldContainer partnerVermeldContainer = new PartnervermeldContainer(aanvraag.getBasisPersoon());
    GbaNativeSelect vermeldPartnerField = getField(VERMELDING_PARTNER, GbaNativeSelect.class);
    vermeldPartnerField.setDataSource(partnerVermeldContainer);
    vermeldPartnerField.setValue(partnerVermeldContainer.getStandaardWaarde());

    if (VermeldPartnerType.ONBEKEND != aanvraag.getClausules().getVermeldingPartner()) {
      vermeldPartnerField.setValue(aanvraag.getClausules().getVermeldingPartner());
    }

    // Titel
    TitelvermeldContainer tpVermeldContainer = new TitelvermeldContainer(aanvraag.getBasisPersoon());
    GbaNativeSelect vermeldTpField = getField(VERMELDING_TITEL, GbaNativeSelect.class);
    vermeldTpField.setDataSource(tpVermeldContainer);
    vermeldTpField.setValue(tpVermeldContainer.getDefaultValue());

    if (VermeldTitelType.ONBEKEND != aanvraag.getVermeldingTitel()) {
      vermeldTpField.setValue(aanvraag.getVermeldingTitel());
    }

    // Land
    LandvermeldContainer landVermeldContainer = new LandvermeldContainer(aanvraag.getBasisPersoon());
    GbaNativeSelect vermeldLandField = getField(VERMELDING_LAND, GbaNativeSelect.class);
    vermeldLandField.setDataSource(landVermeldContainer);
    vermeldLandField.setValue(aanvraag.isVermeldingLand());
  }

  @Override
  public Field newField(Field field, Property property) {
    super.newField(field, property);
    if (property.is(NRVBDOC, NATIOGBA)) {
      getLayout().addBreak();
    }

    return field;
  }

  @Override
  public void onValueChange(Field field, Property property, com.vaadin.data.Property.ValueChangeEvent event) {

    Field fieldTermijn = getField(TERMIJNGELD);
    Field fieldDatum = getField(DGELDEND);
    Field fieldDatumOms = getField(DGELDENDOMS);

    if (fieldDatum != null && field == fieldTermijn && fieldTermijn.getValue() != null) {

      fieldDatum.setVisible(false);
      fieldDatumOms.setVisible(false);

      FieldValue termijn = (FieldValue) fieldTermijn.getValue();

      if (!pos(termijn.getValue())) {
        fieldDatum.setVisible(true);
        fieldDatumOms.setVisible(true);
      }

      ProcuraDate max = getDatumGeldigMax(reisdocumenten, aanvraag.getReisdocumentType(),
          aanvraag.getBasisPersoon());
      getField(DGELDEND).setValue(new DateFieldValue(max.getSystemDate()));

      repaint();

    } else {
      super.onValueChange(field, property, event);
    }
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {

    if (property.is(DGELDENDOMS)) {
      column.setAppend(true);
    }

    super.setColumn(column, field, property);
  }
}
