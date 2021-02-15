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

package nl.procura.gba.web.modules.beheer.gebruikers.page2;

import static nl.procura.gba.common.MiscUtils.cleanPath;
import static nl.procura.gba.common.MiscUtils.setClass;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.modules.beheer.overig.MapValidator;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.component.field.DatumVeld;
import nl.procura.vaadin.component.field.EmailField;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page2GebruikersBean implements Serializable {

  public static final String GEBRUIKERS_NAAM         = "gebruikersnaam";
  public static final String VOLLEDIGE_NAAM          = "volledigeNaam";
  public static final String IS_APPLICATIE_BEHEERDER = "applicatieBeheerder";
  public static final String EIND_GELD_GEBR          = "eindeGeldGebr";
  public static final String INGANG_GELD_GEBR        = "ingangGeldGebr";
  public static final String EIND_GELD_WW            = "eindeGeldWw";
  public static final String GEBLOKKEERD             = "geblokkeerd";
  public static final String EXTRA_INFO              = "extraInfo";
  public static final String MAP                     = "map";
  public static final String EMAIL                   = "email";
  public static final String AFDELING                = "afdeling";
  public static final String TELEFOONNUMMER          = "telefoonnummer";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Gebruikersnaam",
      required = true)
  private String gebruikersnaam = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Volledige naam",
      required = true,
      width = "300px")
  @TextField(nullRepresentation = "")
  private String volledigeNaam = "";

  @Field(type = FieldType.CHECK_BOX,
      caption = "Applicatie beheerder")
  private boolean applicatieBeheerder;

  @Field(customTypeClass = DatumVeld.class,
      caption = "Datum einde")
  private DateFieldValue eindeGeldGebr = new DateFieldValue();

  @Field(customTypeClass = DatumVeld.class,
      caption = "Datum ingang")
  private DateFieldValue ingangGeldGebr = new DateFieldValue();

  @Field(type = FieldType.LABEL,
      caption = "Einde geldigheid wachtwoord")
  private String eindeGeldWw = "";

  @Field(type = FieldType.LABEL,
      caption = "Geblokkeerd")
  private String geblokkeerd = "";

  @Field(type = FieldType.TEXT_AREA,
      caption = "Extra informatie",
      width = "400px")
  private String extraInfo = "";

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Map",
      width = "300px",
      validators = MapValidator.class)
  @Immediate
  private String map = ""; // Merk op dat ComboBox de input per default op 'null' zet!

  @Field(customTypeClass = EmailField.class,
      caption = "E-mail",
      width = "300px")
  private String email = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Afdeling",
      width = "300px")
  private String afdeling = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Telefoonnummer",
      width = "300px")
  private String telefoonnummer = "";

  // Na een commit van de form staat de waarde van de map dan ook op 'null'.

  public Page2GebruikersBean() {
    this(new Gebruiker());
  }

  public Page2GebruikersBean(Gebruiker gebruiker) {

    setGeblokkeerd("Nee");

    if (gebruiker.isStored()) {

      setGebruikersnaam(gebruiker.getGebruikersnaam());
      setVolledigeNaam(gebruiker.getNaam());
      setApplicatieBeheerder(gebruiker.isAdministrator());

      setIngangGeldGebr(new DateFieldValue(gebruiker.getDatumIngang().getLongDate()));
      setEindeGeldGebr(new DateFieldValue(gebruiker.getDatumEinde().getLongDate()));
      setGeblokkeerd(gebruiker.isGeblokkeerd() ? setClass(false, "Ja") : "Nee");
      setExtraInfo(gebruiker.getOmschrijving());
      setMap(cleanPath(gebruiker.getPad()));
    }
  }
}
