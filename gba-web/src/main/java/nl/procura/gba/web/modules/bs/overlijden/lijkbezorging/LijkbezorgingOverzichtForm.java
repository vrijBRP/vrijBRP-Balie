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

package nl.procura.gba.web.modules.bs.overlijden.lijkbezorging;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.bs.overlijden.DossierOverlijdenLijkbezorging;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

import lombok.Data;

public class LijkbezorgingOverzichtForm extends ReadOnlyForm {

  public static final String WIJZE         = "wijze";
  public static final String OP            = "op";
  public static final String TERMIJN       = "termijn";
  public static final String BUITENBENELUX = "buitenBenelux";
  public static final String DOCUMENT      = "document";

  public LijkbezorgingOverzichtForm(DossierOverlijdenLijkbezorging lijkbezorging) {

    setCaption("Lijkbezorging");
    setOrder(WIJZE, OP, TERMIJN, BUITENBENELUX, DOCUMENT);
    setColumnWidths("170px", "350px", "130px", "");

    Bean bean = getNewBean();
    bean.setWijze(lijkbezorging.getWijzeLijkBezorging().getOms());
    bean.setOp(lijkbezorging.getDatumLijkbezorging() + " " + lijkbezorging.getTijdLijkbezorgingStandaard());
    bean.setTermijn(lijkbezorging.getTermijnLijkbezorging().getOms());
    bean.setBuitenBenelux(lijkbezorging.getBuitenBeneluxTekst());
    bean.setDocument(lijkbezorging.getOntvangenDocumentLijkbezorging().getOms());

    setBean(bean);
  }

  @Override
  public Bean getBean() {
    return (Bean) super.getBean();
  }

  @Override
  public Bean getNewBean() {
    return new Bean();
  }

  @Override
  public void setColumn(Column column, com.vaadin.ui.Field field, Property property) {

    if (property.is(DOCUMENT)) {
      column.setColspan(3);
    }

    super.setColumn(column, field, property);
  }

  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  @Data
  protected class Bean implements Serializable {

    @Field(type = FieldType.LABEL,
        caption = "Wijze")
    private String wijze = "";

    @Field(type = FieldType.LABEL,
        caption = "Op")
    private String op = "";

    @Field(type = FieldType.LABEL,
        caption = "Termijn")
    private String termijn = "";

    @Field(type = FieldType.LABEL,
        caption = "Buiten Benelux")
    private String buitenBenelux = "";

    @Field(type = FieldType.LABEL,
        caption = "Ontvangen document")
    private String document = "";
  }
}
