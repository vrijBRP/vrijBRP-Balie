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

package nl.procura.gba.web.modules.bs.erkenning.page25;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.containers.LandContainer;
import nl.procura.gba.web.components.containers.RechtbankContainer;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.modules.bs.geboorte.page35.ToestemminggeverTypeContainer;
import nl.procura.gba.web.services.bs.algemeen.enums.RechtbankLocatie;
import nl.procura.gba.web.services.bs.geboorte.erkenningbuitenproweb.ToestemminggeverType;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page25ErkenningBean2 implements Serializable {

  public static final String RECHT_MOEDER          = "rechtMoeder";
  public static final String RECHT_KIND            = "rechtKind";
  public static final String TOESTEMMINGGEVER_TYPE = "toestemminggeverType";
  public static final String RECHTBANK             = "rechtbank";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Moeder: toegepast recht van",
      required = true,
      width = "300px")
  @Select(containerDataSource = LandContainer.class)
  private FieldValue rechtMoeder = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Kind: toegepast recht van",
      required = true,
      width = "300px")
  @Select(containerDataSource = LandContainer.class)
  private FieldValue rechtKind = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Toestemminggever",
      required = true,
      width = "300px")
  @Select(containerDataSource = ToestemminggeverTypeContainer.class,
      nullSelectionAllowed = false)
  @Immediate
  private ToestemminggeverType toestemminggeverType = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Rechtbank",
      required = true,
      visible = false,
      width = "300px")
  @Select(containerDataSource = RechtbankContainer.class,
      nullSelectionAllowed = false)
  private RechtbankLocatie rechtbank = null;

  public RechtbankLocatie getRechtbank() {
    return rechtbank;
  }

  public void setRechtbank(RechtbankLocatie rechtbank) {
    this.rechtbank = rechtbank;
  }

  public FieldValue getRechtKind() {
    return rechtKind;
  }

  public void setRechtKind(FieldValue rechtKind) {
    this.rechtKind = rechtKind;
  }

  public FieldValue getRechtMoeder() {
    return rechtMoeder;
  }

  public void setRechtMoeder(FieldValue rechtMoeder) {
    this.rechtMoeder = rechtMoeder;
  }

  public ToestemminggeverType getToestemminggeverType() {
    return toestemminggeverType;
  }

  public void setToestemminggeverType(ToestemminggeverType toestemminggeverType) {
    this.toestemminggeverType = toestemminggeverType;
  }
}
