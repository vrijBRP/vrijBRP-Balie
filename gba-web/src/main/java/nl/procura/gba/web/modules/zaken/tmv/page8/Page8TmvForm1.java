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

package nl.procura.gba.web.modules.zaken.tmv.page8;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.zaken.tmv.TerugmeldingAanvraag;
import nl.procura.gba.web.services.zaken.tmv.TerugmeldingReactie;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.TextArea;

import lombok.Data;

public class Page8TmvForm1 extends GbaForm<Page8TmvForm1.Bean> {

  private static final String MELDING   = "melding";
  private static final String GEBRUIKER = "gebruiker";
  private static final String DATUMTIJD = "datumTijd";
  private static final String REACTIE   = "reactie";

  private TerugmeldingAanvraag tmv        = null;
  private TerugmeldingReactie  tmvReactie = null;

  public Page8TmvForm1(TerugmeldingAanvraag tmv, TerugmeldingReactie tmvReactie) {

    setCaption("Nieuwe reactie");
    setColumnWidths(WIDTH_130, "");
    setOrder(MELDING, GEBRUIKER, DATUMTIJD, REACTIE);
    setReadonlyAsText(false);

    setTmv(tmv);
    setTmvReactie(tmvReactie);

    setBean(new Bean());
  }

  @Override
  public void attach() {

    Bean b = new Bean();
    b.setMelding(getTmv().getTerugmelding());

    if (getTmvReactie().isStored()) {
      b.setGebruiker(getTmvReactie().getIngevoerdDoor());
      b.setDatumTijd(getTmvReactie().getDatumTijdInvoer());
      b.setReactie(getTmvReactie().getTerugmReactie());

    } else {
      b.setGebruiker(new UsrFieldValue(getApplication().getServices().getGebruiker()));
    }

    setBean(b);

    getField(REACTIE).focus();

    super.attach();
  }

  public TerugmeldingAanvraag getTmv() {
    return tmv;
  }

  public void setTmv(TerugmeldingAanvraag tmv) {
    this.tmv = tmv;
  }

  public TerugmeldingReactie getTmvReactie() {
    return tmvReactie;
  }

  public void setTmvReactie(TerugmeldingReactie tmvReactie) {
    this.tmvReactie = tmvReactie;
  }

  @Data
  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public static class Bean implements Serializable {

    @Field(type = FieldType.TEXT_AREA,
        caption = "Melding",
        readOnly = true,
        width = "400px")
    @TextArea(rows = 2)
    private String melding = "";

    @Field(type = FieldType.LABEL,
        caption = "Gebruiker")
    private UsrFieldValue gebruiker = new UsrFieldValue();

    @Field(type = FieldType.LABEL,
        caption = "Datum/tijd")
    private DateTime datumTijd = new DateTime();

    @Field(type = FieldType.TEXT_AREA,
        caption = "Reactie",
        width = "400px",
        required = true)
    private String reactie = "";
  }
}
