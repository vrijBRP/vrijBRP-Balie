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

package nl.procura.gba.web.modules.bs.geboorte.page35.form1;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.services.bs.erkenning.ErkenningsType;
import nl.procura.gba.web.services.bs.geboorte.GezinssituatieType;
import nl.procura.gba.web.services.bs.naamskeuze.NaamskeuzeType;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.Select;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page35GeboorteBean1 implements Serializable {

  public static final String GEZIN           = "gezin";
  public static final String ERKENNINGS_TYPE = "erkenningsType";
  public static final String ERKENNINGS_APP  = "erkenningsApp";

  public static final String NAAMSKEUZE_TYPE = "naamskeuzeType";
  public static final String NAAMSKEUZE_APP  = "naamskeuzeApp";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Gezinssituatie",
      required = true,
      width = "500px")
  @Select(containerDataSource = GezinssituatieContainer.class,
      nullSelectionAllowed = false)
  @Immediate
  private GezinssituatieType gezin = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Soort erkenning",
      required = true,
      visible = false,
      width = "500px")
  @Select(containerDataSource = GeboorteErkenningsTypeContainer.class)
  @Immediate
  private ErkenningsType erkenningsType = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Erkenning gedaan",
      required = true,
      visible = false,
      width = "500px")
  @Select(containerDataSource = GeboorteErkenningsAppContainer.class)
  @Immediate
  private ErkenningsApplicatie erkenningsApp = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Soort naamskeuze",
      required = true,
      visible = false,
      width = "500px")
  @Select(containerDataSource = GeboorteNaamskeuzeTypeContainer.class)
  @Immediate
  private NaamskeuzeType naamskeuzeType = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Naamskeuze gedaan",
      required = true,
      visible = false,
      width = "500px")
  @Select(containerDataSource = GeboorteNaamskeuzeAppContainer.class)
  @Immediate
  private NaamskeuzeApplicatie naamskeuzeApp = null;

  public ErkenningsApplicatie getErkenningsApp() {
    return erkenningsApp;
  }

  public void setErkenningsApp(ErkenningsApplicatie erkenningsApp) {
    this.erkenningsApp = erkenningsApp;
  }

  public ErkenningsType getErkenningsType() {
    return erkenningsType;
  }

  public void setErkenningsType(ErkenningsType erkenningsType) {
    this.erkenningsType = erkenningsType;
  }

  public NaamskeuzeType getNaamskeuzeType() {
    return naamskeuzeType;
  }

  public void setNaamskeuzeType(NaamskeuzeType naamskeuzeType) {
    this.naamskeuzeType = naamskeuzeType;
  }

  public NaamskeuzeApplicatie getNaamskeuzeApp() {
    return naamskeuzeApp;
  }

  public void setNaamskeuzeApp(NaamskeuzeApplicatie naamskeuzeApp) {
    this.naamskeuzeApp = naamskeuzeApp;
  }

  public GezinssituatieType getGezin() {
    return gezin;
  }

  public void setGezin(GezinssituatieType gezin) {
    this.gezin = gezin;
  }
}
