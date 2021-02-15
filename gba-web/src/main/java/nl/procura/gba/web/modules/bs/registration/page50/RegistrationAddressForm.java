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

package nl.procura.gba.web.modules.bs.registration.page50;

import static nl.procura.gba.web.components.containers.Container.LAND;
import static nl.procura.standard.Globalfunctions.trim;
import static nl.procura.vaadin.annotation.field.Field.FieldType.LABEL;

import java.lang.annotation.ElementType;

import nl.procura.diensten.gba.ple.openoffice.formats.Adresformats;
import nl.procura.gba.web.common.tables.GbaTables;
import nl.procura.gba.web.components.containers.LandContainer;
import nl.procura.gba.web.components.fields.GbaDateField;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.bs.common.layouts.relocation.NumberOfPersonsContainer;
import nl.procura.gba.web.services.bs.registration.DossierRegistration;
import nl.procura.gba.web.services.bs.registration.OriginSituationType;
import nl.procura.gba.web.services.bs.registration.StaydurationType;
import nl.procura.gba.web.services.zaken.verhuizing.FunctieAdres;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.field.fieldvalues.TabelFieldValue;

import lombok.Data;

public class RegistrationAddressForm extends GbaForm<RegistrationAddressForm.Bean> {

  RegistrationAddressForm(DossierRegistration dossier) {
    setCaption("Inschrijving");
    setColumnWidths("130px", "260px", "180px", "");
    setOrder(Bean.F_ADDRESS, Bean.F_FUNCTION, Bean.F_POSTAL_CODE_AND_RESIDENCE, Bean.F_NO_OF_PEOPLE,
        Bean.F_COUNTY_OF_ORIGIN, Bean.F_START_DATE, Bean.F_DURATION, Bean.F_CONSENT_PROVIDER);

    Bean bean = new Bean();
    Adresformats adresformats = new Adresformats();
    adresformats.setValues(dossier.getStreet(), dossier.getHouseNumber().toString(), dossier.getHouseNumberL(),
        dossier.getHouseNumberT(), dossier.getHouseNumberA(), "", dossier.getPostalCode(),
        "", GbaTables.WOONPLAATS.get(dossier.getResidence()).getDescription(), "",
        "", "", "", "", "", "");
    bean.setAddress(adresformats.getAdres());
    bean.setFunction(FunctieAdres.get(dossier.getAddressFunction()).getOms());
    bean.setPostalCodeAndResidence(trim(adresformats.getPc_wpl_gem()));
    NumberOfPersonsContainer container = new NumberOfPersonsContainer(false);
    bean.setNoOfPeople(container.getItemById(new FieldValue(dossier.getNoOfPeople())).toString());
    bean.setConsentProvider(dossier.getConsent().toString());
    bean.setDuration(StaydurationType.valueOfCode(dossier.getDuration()).getDescription());
    bean.setStartDate(new GbaDateFieldValue(dossier.getDossier().getDatumIngang().getStringDate()));
    OriginSituationType originSituationType = OriginSituationType.valueOfCode(dossier.getOriginSituation());
    TabelFieldValue countryValue = LAND.get(dossier.getDepartureCountry().getStringValue());
    bean.setCountyOfOrigin(originSituationType == OriginSituationType.CHILD_BORN_IN_NL
        ? OriginSituationType.CHILD_BORN_IN_NL.getDescription()
        : countryValue.getDescription());
    setBean(bean);
  }

  @Data
  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public static class Bean {

    public static final String F_ADDRESS                   = "address";
    public static final String F_FUNCTION                  = "function";
    public static final String F_POSTAL_CODE_AND_RESIDENCE = "postalCodeAndResidence";
    public static final String F_NO_OF_PEOPLE              = "noOfPeople";
    public static final String F_COUNTY_OF_ORIGIN          = "countyOfOrigin";
    public static final String F_START_DATE                = "startDate";
    public static final String F_CONSENT_PROVIDER          = "consentProvider";
    public static final String F_DURATION                  = "duration";

    @Field(type = LABEL, caption = "Adres")
    private String address;

    @Field(type = LABEL, caption = "Functie adres")
    private String function;

    @Field(type = LABEL, caption = "Postcode / plaats")
    private String postalCodeAndResidence;

    @Field(type = LABEL, caption = "Aantal personen op adres")
    private String noOfPeople;

    @Field(type = LABEL, caption = "Herkomst")
    @Select(containerDataSource = LandContainer.class)
    private String countyOfOrigin;

    @Field(customTypeClass = GbaDateField.class,
        caption = "Datum aangifte",
        readOnly = true)
    private GbaDateFieldValue startDate;

    @Field(type = LABEL, caption = "Toestemminggever")
    private String consentProvider;

    @Field(type = LABEL, caption = "Duur")
    private String duration;
  }
}
