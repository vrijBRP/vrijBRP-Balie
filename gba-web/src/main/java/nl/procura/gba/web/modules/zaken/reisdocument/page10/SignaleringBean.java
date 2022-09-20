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

import static java.util.Optional.ofNullable;

import java.lang.annotation.ElementType;
import java.util.stream.Collectors;

import nl.procura.burgerzaken.vrsclient.model.Signalering;
import nl.procura.gba.common.DateTime;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

import lombok.Getter;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
@Getter
public class SignaleringBean {

  static final String ZAAKNUMMER       = "zaaknummer";
  static final String INSTANTIE        = "instantie";
  static final String REGISTRATIEDATUM = "registratiedatum";
  static final String ARTIKELEN        = "artikelen";
  static final String CONTACTPERSOON   = "contactpersoon";
  static final String TELEFOON         = "telefoon";
  static final String EMAIL            = "email";

  @Field(type = Field.FieldType.TEXT_FIELD, caption = "Registratiedatum")
  private final String registratiedatum;
  @Field(type = Field.FieldType.TEXT_FIELD, caption = "Artikelen")
  private final String artikelen;
  @Field(type = Field.FieldType.TEXT_FIELD, caption = "Instantie")
  private final String instantie;
  @Field(type = Field.FieldType.TEXT_FIELD, caption = "Zaaknummer")
  private final String zaaknummer;
  @Field(type = Field.FieldType.TEXT_FIELD, caption = "Contactpersoon")
  private final String contactpersoon;
  @Field(type = Field.FieldType.TEXT_FIELD, caption = "Telefoon")
  private final String telefoon;
  @Field(type = Field.FieldType.TEXT_FIELD, caption = "E-mail")
  private final String email;

  public SignaleringBean(Signalering signalering) {
    this.registratiedatum = ofNullable(signalering.getRegistratiedatum())
        .map(d -> DateTime.of(d)
            .getFormatDate())
        .orElse("Onbekend");
    this.artikelen = signalering.getSubartikelen().stream()
        .map(artikel -> artikel.getArtikel().getSubartikelomschrijvingScherm())
        .collect(Collectors.joining("<br/>\n"));
    this.instantie = signalering.getInstantie().getNaam();
    this.zaaknummer = signalering.getZaaknummer();
    this.contactpersoon = signalering.getNaamContactpersoon();
    this.telefoon = signalering.getTelefoonContactpersoon();
    this.email = signalering.getEmailContactpersoon();
  }
}
