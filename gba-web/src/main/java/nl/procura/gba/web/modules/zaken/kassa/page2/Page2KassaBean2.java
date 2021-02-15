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

package nl.procura.gba.web.modules.zaken.kassa.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.services.beheer.kassa.KassaType;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.reisdocumenten.SoortReisdocument;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraagSoort;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page2KassaBean2 implements Serializable {

  public static final String KASSATYPE    = "kassaType";
  public static final String DOCUMENT     = "document";
  public static final String REISDOCUMENT = "reisdocument";
  public static final String RIJBEWIJS    = "rijbewijs";
  public static final String PRODUCTGROEP = "productgroep";
  public static final String PRODUCT      = "product";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Type",
      required = true)
  @Immediate()
  private KassaType kassaType;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Document",
      required = true)
  @Immediate()
  private DocumentRecord document;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Reisdocument",
      required = true)
  @Immediate()
  private SoortReisdocument reisdocument;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Rijbewijs",
      required = true)
  @Immediate()
  private RijbewijsAanvraagSoort rijbewijs;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Productgroep",
      required = true)
  @Immediate()
  private String productgroep;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Product",
      required = true)
  @Immediate()
  private String product;

  public DocumentRecord getDocument() {
    return document;
  }

  public void setDocument(DocumentRecord document) {
    this.document = document;
  }

  public KassaType getKassaType() {
    return kassaType;
  }

  public void setKassaType(KassaType type) {
    this.kassaType = type;
  }

  public String getProduct() {
    return product;
  }

  public void setProduct(String product) {
    this.product = product;
  }

  public String getProductgroep() {
    return productgroep;
  }

  public void setProductgroep(String productgroep) {
    this.productgroep = productgroep;
  }

  public SoortReisdocument getReisdocument() {
    return reisdocument;
  }

  public void setReisdocument(SoortReisdocument reisdocument) {
    this.reisdocument = reisdocument;
  }

  public RijbewijsAanvraagSoort getRijbewijs() {
    return rijbewijs;
  }

  public void setRijbewijs(RijbewijsAanvraagSoort rijbewijs) {
    this.rijbewijs = rijbewijs;
  }
}
