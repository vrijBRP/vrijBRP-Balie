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

import static nl.procura.gba.common.MiscUtils.copy;
import static nl.procura.gba.web.common.tables.GbaTables.LAND;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.INSCHRIJVER;
import static nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoonType.AANGEVER;
import static nl.procura.standard.Globalfunctions.pos;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import nl.procura.bsm.rest.v1_0.objecten.gba.probev.idnumbers.IdNumbersRequestRestElement;
import nl.procura.bsm.rest.v1_0.objecten.gba.probev.idnumbers.IdNumbersResponseRestElement;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.jpa.personen.db.DossRegistration;
import nl.procura.gba.web.services.ServiceEvent;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Timer;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.DossierService;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoonFilter;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisService;
import nl.procura.gba.web.services.gba.ple.relatieLijst.Relatie;
import nl.procura.gba.web.services.zaken.algemeen.*;
import nl.procura.gba.web.services.zaken.algemeen.consent.ConsentProvider;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContact;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoonType;

public class RegistrationService extends AbstractZaakContactService<Dossier> implements ZaakService<Dossier> {

  private static final String BSM_IDNUMBERS_PROCESSOR_ID = "procura.personen.probev.idnumbers";

  public RegistrationService() {
    super("Eerste inschrijving", ZaakType.REGISTRATION);
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van eerste registraties")
  public int getZakenCount(ZaakArgumenten zaakArgumenten) {
    return getServices().getDossierService().getZakenCount(new ZaakArgumenten(zaakArgumenten, getZaakType()));
  }

  @Override
  public ZaakContact getContact(Dossier zaak) {
    ZaakContact contact = new ZaakContact();
    DossierRegistration dossierReg = (DossierRegistration) zaak.getZaakDossier();
    Optional<DossierPersoon> declarent = dossierReg.getDeclarant().getPerson();
    declarent.ifPresent(d -> contact.add(getServices().getDossierService().getContactPersoon(AANGEVER, d)));

    List<DossierPersoon> dossPers = dossierReg.getDossier().getPersonen(INSCHRIJVER);
    for (DossierPersoon persoon : dossPers) {
      contact.add(getServices().getDossierService().getContactPersoon(ZaakContactpersoonType.INSCHRIJVER, persoon));
    }
    return contact;
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van eerste registraties")
  public List<Dossier> getMinimalZaken(ZaakArgumenten zaakArgumenten) {
    return getServices().getDossierService().getMinimalZaken(new ZaakArgumenten(zaakArgumenten, getZaakType()));
  }

  @Override
  public Zaak getNewZaak() {
    return newDossier();
  }

  public Dossier newDossier() {
    final Dossier dossier = new DossierRegistration().getDossier();
    ZaakUtils.newZaakDossier(dossier, getServices());
    return dossier;
  }

  /**
   * Retrieve specific registration data from the database and add it to the given dossier.
   *
   * @param dossier must contain the generic dossier (table "doss")
   * @return the given dossier enriched with the specific registration data (table "doss_registration")
   */
  @Override
  @ThrowException("Het is niet mogelijk om de zaak op te vragen")
  public Dossier getStandardZaak(Dossier dossier) {
    // generic dossier is already read in "zaak"
    // read specific registration dossier from DB
    final DossRegistration entity = findEntity(DossRegistration.class, dossier.getCode());
    dossier.setZaakDossier(copy(entity, DossierRegistration.class));
    getServices().getDossierService().getOverigDossier(dossier);
    return super.getStandardZaak(dossier);
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van zaak-ids")
  public List<ZaakKey> getZaakKeys(ZaakArgumenten zaakArgumenten) {
    return getServices().getDossierService().getZaakKeys(new ZaakArgumenten(zaakArgumenten, getZaakType()));
  }

  @Override
  @Transactional
  @ThrowException("Fout bij opslaan van het dossier")
  public void save(Dossier zaak) {
    saveRegistration((DossierRegistration) zaak.getZaakDossier());
  }

  @Transactional
  @ThrowException("Fout bij opslaan van het dossier")
  public void saveRegistration(DossierRegistration dossierRegistration) {

    setDerivedFields(dossierRegistration);

    final DossierService dossierService = getServices().getDossierService();
    final Dossier dossier = dossierRegistration.getDossier();
    dossier.setDescr(getDescription(dossierRegistration));

    dossierService
        .saveDossier(dossier)
        .savePersonen(dossier)
        .saveZaakDossier(dossierRegistration);

    // Do riskanalysis check
    RiskAnalysisService riskAnalysisService = getServices().getRiskAnalysisService();
    riskAnalysisService.onSaveZaak(dossier);
    callListeners(ServiceEvent.CHANGE);
  }

  private static String getDescription(DossierRegistration zaakDossier) {
    List<String> elements = new ArrayList<>();
    elements.add(LAND.get(zaakDossier.getDepartureCountry().getStringValue()).toString());
    DossierPersoon persoon = zaakDossier.getDossier().getPersoon(DossierPersoonFilter.filter(INSCHRIJVER));
    if (persoon != null) {
      elements.add(persoon.getNaam().getGeslachtsnaam());
    }
    elements.add(zaakDossier.getAddress().getAdres_pc_wpl());
    zaakDossier.getDossier().getAktes().forEach(akte -> elements.add(akte.getDescription()));
    return elements.stream()
        .filter(StringUtils::isNotBlank)
        .collect(Collectors.joining(" / "));
  }

  /**
   * Add derived fields. For example extra data necessary for printing.
   */
  @Override
  public Dossier setVolledigeZaakExtra(Dossier zaak) {
    DossierRegistration dossierRegistration = (DossierRegistration) zaak.getZaakDossier();
    String bsn = dossierRegistration.getBsnOfConsentProvider();
    String otherConsentProvider = dossierRegistration.getOtherConsentProvider();
    if (pos(bsn)) {
      Relatie relatie = new Relatie();
      relatie.setPl(getPersoonslijst(bsn));
      dossierRegistration.setConsent(ConsentProvider.consentProvider(relatie));
    } else if (isNotBlank(otherConsentProvider)) {
      dossierRegistration.setConsent(ConsentProvider.otherConsentProvider(otherConsentProvider));
    } else {
      dossierRegistration.setConsent(ConsentProvider.notDeclared());
    }
    return super.setVolledigeZaakExtra(zaak);
  }

  private void setDerivedFields(DossierRegistration zaakDossier) {
    // implement real implementation or remove method
    zaakDossier.setBurgerServiceNummer(zaakDossier.getBurgerServiceNummer());
  }

  /**
   * Delete registration dossier from database and call CHANGE event listeners.
   *
   * @param dossier a minimal dossier
   */
  @Override
  @Transactional
  public void delete(Dossier dossier) {
    // only delete generic dossier, db will delete registration dossier
    getServices().getDossierService().deleteDossier(dossier);

    callListeners(ServiceEvent.CHANGE);
  }

  /**
   * Returns BSN and/or ANR
   */
  public IdNumbersResponseRestElement getIdentificationNumbers(boolean requestBsn, boolean requestAnr) {
    final IdNumbersResponseRestElement responseRestElement = new IdNumbersResponseRestElement();
    getServices().getBsmService().bsmQuery(BSM_IDNUMBERS_PROCESSOR_ID,
        new IdNumbersRequestRestElement().requestBsn(requestBsn).requestAnr(requestAnr), responseRestElement);
    return responseRestElement;
  }
}
