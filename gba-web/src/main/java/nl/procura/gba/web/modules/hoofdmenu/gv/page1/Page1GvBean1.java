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

package nl.procura.gba.web.modules.hoofdmenu.gv.page1;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.util.Date;

import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.services.zaken.algemeen.koppelenumeratie.KoppelEnumeratieType;
import nl.procura.gba.web.services.zaken.documenten.afnemers.DocumentAfnemer;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.component.field.ProDateField;
import nl.procura.vaadin.component.field.ProNativeSelect;
import nl.procura.vaadin.component.field.ProTextArea;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page1GvBean1 implements Serializable {

  public static final String DATUM_ONTVANGST       = "datumOntvangst";
  public static final String GRONDSLAG             = "grondslag";
  public static final String AFNEMER               = "afnemer";
  public static final String TOEKENNING            = "toekenning";
  public static final String TOEKENNING_MOTIVERING = "toekenningMotivering";

  @Field(customTypeClass = ProDateField.class,
      caption = "Datum ontvangst",
      width = "97px",
      required = true)
  private Date datumOntvangst = null;

  @Field(type = FieldType.LABEL,
      caption = "Grondslag",
      visible = false)
  private KoppelEnumeratieType grondslag = null;

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Door",
      width = "300px",
      required = true)
  @Select()
  @Immediate
  private DocumentAfnemer afnemer = null;

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Toekennen",
      required = true,
      visible = false)
  @Select(nullSelectionAllowed = false)
  @Immediate
  private KoppelEnumeratieType toekenning = null;

  @Field(customTypeClass = ProTextArea.class,
      caption = "Motivering",
      width = "500px",
      visible = false)
  @TextArea(rows = 3)
  private String toekenningMotivering = "";

  public DocumentAfnemer getAfnemer() {
    return afnemer;
  }

  public void setAfnemer(DocumentAfnemer afnemer) {
    this.afnemer = afnemer;
  }

  public Date getDatumOntvangst() {
    return datumOntvangst;
  }

  public void setDatumOntvangst(Date datumOntvangst) {
    this.datumOntvangst = datumOntvangst;
  }

  public KoppelEnumeratieType getGrondslag() {
    return grondslag;
  }

  public void setGrondslag(KoppelEnumeratieType grondslag) {
    this.grondslag = grondslag;
  }

  public KoppelEnumeratieType getToekenning() {
    return toekenning;
  }

  public void setToekenning(KoppelEnumeratieType toekenning) {
    this.toekenning = toekenning;
  }

  public String getToekenningMotivering() {
    return toekenningMotivering;
  }

  public void setToekenningMotivering(String toekenningMotivering) {
    this.toekenningMotivering = toekenningMotivering;
  }
}
