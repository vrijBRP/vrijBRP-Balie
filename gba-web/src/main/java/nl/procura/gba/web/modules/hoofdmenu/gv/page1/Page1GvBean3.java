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

import nl.procura.gba.web.services.zaken.algemeen.koppelenumeratie.KoppelEnumeratieType;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.component.field.ProDateField;
import nl.procura.vaadin.component.field.ProNativeSelect;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page1GvBean3 implements Serializable {

  public static final String ACTIESOORT          = "actiesoort";
  public static final String DATUM_EINDE_TERMIJN = "datumEindeTermijn";

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Actiesoort",
      required = true,
      width = "300px")
  @Select(nullSelectionAllowed = false)
  @Immediate
  private KoppelEnumeratieType actiesoort = null;

  @Field(customTypeClass = ProDateField.class,
      caption = "Datum einde termijn",
      width = "300px",
      required = true)
  private Date datumEindeTermijn = null;

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
}
