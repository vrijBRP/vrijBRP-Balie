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

package nl.procura.gba.web.modules.zaken.reisdocument.page10;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page10ReisdocumentBean2 implements Serializable {

  public static final String NLNATIONALITEIT = "nlNationaliteit";
  public static final String NOGDOCINLEVEREN = "nogDocInleveren";
  public static final String SIGNALERING     = "signalering";
  public static final String DERDEGEZAG      = "derdeGezag";
  public static final String VERBLIJFSTITEL  = "verblijfstitel";
  public static final String STAATLOZE       = "staatloze";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "NL nationaliteit")
  private String nlNationaliteit = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Nog documenten inleveren")
  private String nogDocInleveren = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Signalering")
  private String signalering = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Derde gezag / curatele")
  private String derdeGezag = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Verblijfstitelcode")
  private String verblijfstitel = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Staatloze")
  private String staatloze = "";

  public String getDerdeGezag() {
    return derdeGezag;
  }

  public void setDerdeGezag(String derdeGezag) {
    this.derdeGezag = derdeGezag;
  }

  public String getNlNationaliteit() {
    return nlNationaliteit;
  }

  public void setNlNationaliteit(String nlNationaliteit) {
    this.nlNationaliteit = nlNationaliteit;
  }

  public String getNogDocInleveren() {
    return nogDocInleveren;
  }

  public void setNogDocInleveren(String nogDocInleveren) {
    this.nogDocInleveren = nogDocInleveren;
  }

  public String getSignalering() {
    return signalering;
  }

  public void setSignalering(String signalering) {
    this.signalering = signalering;
  }

  public String getStaatloze() {
    return staatloze;
  }

  public void setStaatloze(String staatloze) {
    this.staatloze = staatloze;
  }

  public String getVerblijfstitel() {
    return verblijfstitel;
  }

  public void setVerblijfstitel(String verblijfstitel) {
    this.verblijfstitel = verblijfstitel;
  }
}
