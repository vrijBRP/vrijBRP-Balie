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

package nl.procura.gba.web.services.bs.registration;

import static java.util.Objects.requireNonNull;
import static nl.procura.gba.web.common.tables.GbaTables.WOONPLAATS;

import nl.procura.diensten.gba.ple.openoffice.formats.Adresformats;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.db.DossPer;
import nl.procura.gba.jpa.personen.db.DossRegistration;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.ZaakDossier;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.zaken.algemeen.consent.ConsentProvider;
import nl.procura.java.reflection.ReflectionUtil;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class DossierRegistration extends DossRegistration implements ZaakDossier {

  private static final long serialVersionUID = -4938557681641461615L;
  private Dossier           dossier;
  private ConsentProvider   consentProvider  = ConsentProvider.notDeclared();

  public DossierRegistration() {
    super();
    dossier = new Dossier(ZaakType.REGISTRATION, this);
    dossier.setDatumIngang(new DateTime());
  }

  @Override
  public void beforeSave() {
    setCDossRegistration(getDossier().getCode());
  }

  @Override
  public AnrFieldValue getAnummer() {
    return getDeclarant().getPerson()
        .map(DossierPersoon::getAnummer)
        .orElse(new AnrFieldValue());
  }

  @Override
  public void setAnummer(AnrFieldValue anummer) {
    // ignore, setting an a-number on a person shouldn't be allowed
  }

  @Override
  public BsnFieldValue getBurgerServiceNummer() {
    return getDeclarant().getPerson()
        .map(DossierPersoon::getBurgerServiceNummer)
        .orElse(new BsnFieldValue());
  }

  @Override
  public void setBurgerServiceNummer(BsnFieldValue burgerServiceNummer) {
    // ignore, setting a BSN on a person shouldn't be allowed
  }

  public Long getCode() {
    return getCDossRegistration();
  }

  @Override
  public Dossier getDossier() {
    return dossier;
  }

  @Override
  public void setDossier(Dossier dossier) {
    this.dossier = dossier;
  }

  @Override
  public boolean isVolledig() {
    // implement with real implementation and remove this comment
    return false;
  }

  public RegistrationDeclarant getDeclarant() {
    return RegistrationDeclarant.fromCodeAndPerson(getDeclarantType(), getDeclarantPerson());
  }

  public void setDeclarant(RegistrationDeclarant declarant) {
    setDeclarantType(declarant.getCode());
    setDeclarantPerson(ReflectionUtil.deepCopyBean(DossPer.class, declarant.getPerson().orElse(null)));
  }

  public ConsentProvider getConsent() {
    return consentProvider;
  }

  public void setConsent(ConsentProvider consentProvider) {
    requireNonNull(consentProvider);
    setConsentProvider(consentProvider.getConsentCode());
    setBsnOfConsentProvider(consentProvider.getBsn());
    setOtherConsentProvider(consentProvider.getOtherConsentProvider());
    this.consentProvider = consentProvider;
  }

  public FieldValue getDepartureCountry() {
    return new FieldValue(getCountryOfDeparture());
  }

  public void setDepartureCountry(FieldValue country) {
    setCountryOfDeparture(country.getBigDecimalValue());
  }

  public Adresformats getAddress() {
    String straat = getStreet();
    String hnr = getHouseNumber().toString();
    String hnrL = getHouseNumberL();
    String hnrT = getHouseNumberT();
    String hnrA = getHouseNumberA();
    String pc = getPostalCode();
    String wpl = WOONPLAATS.get(getResidence()).toString();
    return new Adresformats().setValues(straat, hnr, hnrL, hnrT, hnrA, "", pc, "", wpl, "", "", "", "", "", "", "");
  }
}
