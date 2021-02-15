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

package nl.procura.gba.web.modules.hoofdmenu.gv.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.util.Date;

import nl.procura.gba.web.services.zaken.algemeen.koppelenumeratie.KoppelEnumeratieType;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.component.field.ProDateField;
import nl.procura.vaadin.component.field.ProNativeSelect;
import nl.procura.vaadin.component.field.ProTextArea;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page2GvBean4 implements Serializable {

  public static final String ACTIESOORT          = "actiesoort";
  public static final String DATUM_EINDE_TERMIJN = "datumEindeTermijn";
  public static final String REACTIE             = "reactie";
  public static final String MOTIVERING          = "motivering";

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Actiesoort",
      required = true,
      width = "300px")
  @Select()
  @Immediate
  private KoppelEnumeratieType actiesoort = null;

  @Field(customTypeClass = ProDateField.class,
      caption = "Datum einde termijn",
      width = "300px",
      required = true)
  private Date datumEindeTermijn = null;

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Reactie",
      required = true,
      width = "300px",
      visible = false)
  @Select()
  @Immediate
  private KoppelEnumeratieType reactie = null;

  @Field(customTypeClass = ProTextArea.class,
      caption = "Motivering (aanvullend)",
      required = true,
      width = "500px",
      visible = false)
  @TextArea(rows = 3,
      nullRepresentation = "")
  private String motivering = "";

  public KoppelEnumeratieType getActiesoort() {
    return actiesoort;
  }

  public void setActiesoort(KoppelEnumeratieType actiesoort) {
    this.actiesoort = actiesoort;
  }

  public Date getDatumEindeTermijn() {
    return datumEindeTermijn;
  }

  public void setDatumEindeTermijn(Date datumEindeTermijn) {
    this.datumEindeTermijn = datumEindeTermijn;
  }

  public String getMotivering() {
    return motivering;
  }

  public void setMotivering(String motivering) {
    this.motivering = motivering;
  }

  public KoppelEnumeratieType getReactie() {
    return reactie;
  }

  public void setReactie(KoppelEnumeratieType reactie) {
    this.reactie = reactie;
  }
}
