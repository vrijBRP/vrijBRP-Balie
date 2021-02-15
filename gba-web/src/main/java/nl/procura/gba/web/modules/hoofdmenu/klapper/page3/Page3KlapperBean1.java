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

package nl.procura.gba.web.modules.hoofdmenu.klapper.page3;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaDateField;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkteDeel;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkteInvoerType;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkteRegistersoort;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.component.field.NumberField;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page3KlapperBean1 implements Serializable {

  public static final String INVOERTYPE = "invoerType";
  public static final String DATUM      = "datum";
  public static final String SOORT      = "soort";
  public static final String DEEL       = "deel";
  public static final String VNR        = "vnr";

  @Field(customTypeClass = GbaNativeSelect.class,
      required = true,
      caption = "Type",
      width = "300px")
  @Immediate
  private DossierAkteInvoerType invoerType = null;

  @Field(customTypeClass = GbaDateField.class,
      required = true,
      caption = "Datum akte",
      width = "80px")
  @Immediate
  private GbaDateFieldValue datum = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      required = true,
      caption = "Registersoort",
      width = "300px")
  @Immediate
  private DossierAkteRegistersoort soort = DossierAkteRegistersoort.AKTE_ONBEKEND;

  @Field(customTypeClass = GbaNativeSelect.class,
      required = true,
      caption = "Registerdeel",
      width = "300px")
  @Immediate
  private DossierAkteDeel deel = null;

  @Field(customTypeClass = NumberField.class,
      caption = "Volgnummer",
      required = true,
      width = "80px")
  @TextField(maxLength = 4)
  private String vnr = "";

  public GbaDateFieldValue getDatum() {
    return datum;
  }

  public void setDatum(GbaDateFieldValue datum) {
    this.datum = datum;
  }

  public DossierAkteDeel getDeel() {
    return deel;
  }

  public void setDeel(DossierAkteDeel deel) {
    this.deel = deel;
  }

  public DossierAkteInvoerType getInvoerType() {
    return invoerType;
  }

  public void setInvoerType(DossierAkteInvoerType invoerType) {
    this.invoerType = invoerType;
  }

  public DossierAkteRegistersoort getSoort() {
    return soort;
  }

  public void setSoort(DossierAkteRegistersoort soort) {
    this.soort = soort;
  }

  public String getVnr() {
    return vnr;
  }

  public void setVnr(String vnr) {
    this.vnr = vnr;
  }
}
