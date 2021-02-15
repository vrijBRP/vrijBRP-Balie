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

package nl.procura.gba.web.modules.zaken.curatele.page3;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.modules.zaken.curatele.page1.CurateleUtils;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.rechtspraak.namespaces.ccr.CCRWS.Curandus.Beschikkingen.Beschikking;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page3CurateleBean implements Serializable {

  public static final String INSTANTIE        = "instantie";
  public static final String REKEST           = "rekest";
  public static final String BESCHIKKINGSOORT = "beschikkingsoort";
  public static final String DATUM            = "datum";
  public static final String NAAM             = "naam";
  public static final String ADRES            = "adres";
  public static final String GEMEENTE         = "gemeente";

  @Field(type = FieldType.LABEL,
      caption = "Instantie")
  private String instantie = "";

  @Field(type = FieldType.LABEL,
      caption = "Rekest")
  private String rekest = "";

  @Field(type = FieldType.LABEL,
      caption = "Beschikkingsoort")
  private String beschikkingsoort = "";

  @Field(type = FieldType.LABEL,
      caption = "Datum")
  private String datum = "";

  @Field(type = FieldType.LABEL,
      caption = "Naam")
  private String naam = "";

  @Field(type = FieldType.LABEL,
      caption = "Adres")
  private String adres = "";

  @Field(type = FieldType.LABEL,
      caption = "Gemeente")
  private String gemeente = "";

  public Page3CurateleBean(Beschikking b) {

    setInstantie(b.getInstantie());
    setRekest(b.getRekestnummerjaar() + " - " + b.getRekestnummernummer());
    setBeschikkingsoort(b.getBeschikkingsoort());
    setDatum(CurateleUtils.getDatum(b.getDatumBeschikking()).getValue().getDescr());
    setNaam(CurateleUtils.getNaam(b.getCbVoornamen(), b.getCbAchternaam(),
        b.getCbVoorvoegsels()).getNaamNaamgebruikEersteVoornaam());
    setAdres(CurateleUtils.getAdres(b.getCbAdresstraat(), b.getCbAdresnummer(), b.getCbAdresnummertoevoeging(),
        b.getCbAdrespcnummers() + " " + b.getCbAdrespcletters(), b.getCbPlaats(),
        b.getCbGemeente()).getAdresPcWplGem());

    setGemeente(b.getCbGemeente());
  }
}
