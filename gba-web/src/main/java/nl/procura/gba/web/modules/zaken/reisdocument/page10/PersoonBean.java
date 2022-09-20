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

import nl.procura.burgerzaken.vrsclient.model.Persoon;
import nl.procura.commons.misc.formats.geboorte.Geboorte;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.standard.Globalfunctions;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

import lombok.Getter;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
@Getter
public class PersoonBean {

  static final String BSN            = "bsn";
  static final String NAAM           = "naam";
  static final String GESLACHTSNAAM  = "geslachtsnaam";
  static final String VOORVOEGSEL    = "voorvoegsel";
  static final String VOORNAMEN      = "voornamen";
  static final String GESLACHT       = "geslacht";
  static final String GEBOREN        = "geboren";
  static final String GEBOORTEDATUM  = "geboortedatum";
  static final String GEBOORTEPLAATS = "geboorteplaats";
  static final String GEBOORTELAND   = "geboorteland";

  @Field(type = Field.FieldType.TEXT_FIELD, caption = "Burgerservicenummer")
  private final String bsn;
  @Field(type = Field.FieldType.TEXT_FIELD, caption = "Naam")
  private final String naam;
  @Field(type = Field.FieldType.TEXT_FIELD, caption = "Geslacht")
  private final String geslacht;
  @Field(type = Field.FieldType.TEXT_FIELD, caption = "Geboren")
  private final String geboren;

  public PersoonBean(Persoon persoon) {
    String geslachtsnaam = persoon.getGeslachtsnaam();
    String voorvoegsel = ofNullable(persoon.getVoorvoegselGeslachtsnaam()).orElse("");
    String voornamen = persoon.getVoornamen();

    String geboortedatum = persoon.getGeboortedatum();
    String geboorteplaats = persoon.getGeboorteplaats();
    String geboorteland = persoon.getGeboorteland().getNaam();

    this.bsn = persoon.getBsn();
    this.naam = Globalfunctions.trim(voornamen + " " + voorvoegsel + " " + geslachtsnaam);
    this.geboren = new Geboorte(geboortedatum, geboorteplaats, geboorteland).getDatumTePlaatsLand();
    this.geslacht = Geslacht.get(persoon.getGeslacht()).getNormaal();
  }
}
