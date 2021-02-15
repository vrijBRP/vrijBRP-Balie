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

package nl.procura.gba.web.modules.bs.registration.person;

import static nl.procura.vaadin.annotation.field.Field.FieldType.LABEL;

import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.table.TableLayout;
import nl.procura.validation.Anummer;
import nl.procura.validation.Bsn;

import lombok.Data;

public class PersonSummaryForm extends GbaForm<PersonSummaryForm.Bean> {

  public PersonSummaryForm(DossierPersoon personDossier) {
    setCaption("ID-nummers / naam");
    setColumnWidths("130px", "250px", "130px", "");

    Bean bean = new Bean();
    bean.setBsn(Bsn.format(personDossier.getBurgerServiceNummer().getStringValue()));
    bean.setAnumber(Anummer.format(personDossier.getAnummer().getStringValue()));
    bean.setFamilyName(personDossier.getGeslachtsnaam());
    bean.setPrefix(personDossier.getVoorvoegsel());
    bean.setTitle(personDossier.getTp());
    bean.setFirstNames(personDossier.getVoornaam());
    bean.setGender(personDossier.getGeslacht().getNormaal());
    bean.setDateOfBirth(personDossier.getDatumGeboorte().getFormatDate());
    bean.setPlaceOfBirth(personDossier.getGebPlaats());
    bean.setCountryOfBirth(personDossier.getGeboorte().getGeboorteland());
    setBean(bean, Bean.F_FAMILY_NAME, Bean.F_BSN, Bean.F_FIRST_NAMES, Bean.F_ANUMBER, Bean.F_PREFIX,
        Bean.F_GENDER, Bean.F_TITLE, Bean.F_DATE_OF_BIRTH, Bean.F_COUNTRY_OF_BIRTH, Bean.F_PLACE_OF_BIRTH);
  }

  @Override
  public void setColumn(TableLayout.Column column, com.vaadin.ui.Field field, Property property) {
    if (property.is(Bean.F_TITLE, Bean.F_DATE_OF_BIRTH, Bean.F_COUNTRY_OF_BIRTH, Bean.F_PLACE_OF_BIRTH)) {
      column.setColspan(3);
    }
    super.setColumn(column, field, property);
  }

  @Override
  public void afterSetColumn(TableLayout.Column column, com.vaadin.ui.Field field, Property property) {
    if (property.is(Bean.F_TITLE)) {
      Fieldset fieldset = new Fieldset("Geboorte");
      fieldset.setHeight("25px");
      getLayout().addBreak(fieldset);
    }
    super.afterSetColumn(column, field, property);
  }

  @Data
  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public static class Bean {

    public static final String F_BSN              = "bsn";
    public static final String F_ANUMBER          = "anumber";
    public static final String F_FAMILY_NAME      = "familyName";
    public static final String F_PREFIX           = "prefix";
    public static final String F_TITLE            = "title";
    public static final String F_FIRST_NAMES      = "firstNames";
    public static final String F_GENDER           = "gender";
    public static final String F_DATE_OF_BIRTH    = "dateOfBirth";
    public static final String F_PLACE_OF_BIRTH   = "placeOfBirth";
    public static final String F_COUNTRY_OF_BIRTH = "countryOfBirth";

    @Field(type = LABEL, caption = "BSN")
    private String bsn;

    @Field(type = LABEL, caption = "A-nummer")
    private String anumber;

    @Field(type = LABEL, caption = "Geslachtsnaam")
    private String familyName;

    @Field(type = LABEL, caption = "Voorvoegsel")
    private String prefix;

    @Field(type = LABEL, caption = "Titel")
    private String title;

    @Field(type = LABEL, caption = "Voornaam")
    private String firstNames;

    @Field(type = LABEL, caption = "Geslacht")
    private String gender;

    @Field(type = LABEL, caption = "Geboortedatum")
    private String dateOfBirth;

    @Field(type = LABEL, caption = "Geboorteplaats")
    private String placeOfBirth;

    @Field(type = LABEL, caption = "Geboorteland")
    private String countryOfBirth;
  }
}
