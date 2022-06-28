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

package nl.procura.gba.web.services.bs.riskanalysis;

import static java.util.stream.Collectors.toList;
import static nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisService.SIGNALTYPE.ADDRESS;
import static nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisService.SIGNALTYPE.BSN;
import static nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakAttribuutType.WACHT_OP_RISICOANALYSE;
import static nl.procura.standard.Globalfunctions.toBigDecimal;
import static nl.procura.standard.NaturalComparator.compareTo;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.GenericDao;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.jpa.personen.db.*;
import nl.procura.gba.jpa.personen.types.RiskProfileRelatedCaseType;
import nl.procura.gba.web.services.ServiceEvent;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Timer;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.beheer.bag.ProcuraPersonListAddress;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.interfaces.address.Address;
import nl.procura.gba.web.services.zaken.algemeen.*;
import nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakAttribuut;
import nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakAttribuutService;
import nl.procura.gba.web.services.zaken.algemeen.controle.Controles;
import nl.procura.gba.web.services.zaken.algemeen.controle.ControlesListener;
import nl.procura.gba.web.services.zaken.algemeen.status.ZaakStatusService;
import nl.procura.gba.web.services.zaken.algemeen.zaakrelaties.ZaakRelatie;
import nl.procura.gba.web.services.zaken.algemeen.zaakrelaties.ZaakRelatieService;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraag;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisPersoon;
import nl.procura.java.reflection.ReflectionUtil;
import nl.procura.standard.exceptions.ProException;
import nl.procura.validation.Postcode;

public class RiskAnalysisService extends AbstractZaakService<Dossier>
    implements ZaakService<Dossier>, ControleerbareService {

  private static final Logger LOGGER = LoggerFactory.getLogger(RiskAnalysisService.class);

  private final ZaakAttribuutService attribuutService;

  @Inject
  public RiskAnalysisService(ZaakAttribuutService attribuutService) {
    super("Risicoanalyses", ZaakType.RISK_ANALYSIS);
    this.attribuutService = attribuutService;
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van risicoanalyses")
  public int getZakenCount(ZaakArgumenten zaakArgumenten) {
    return getServices().getDossierService().getZakenCount(new ZaakArgumenten(zaakArgumenten, getZaakType()));
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van risicoanalyses")
  public List<Dossier> getMinimalZaken(ZaakArgumenten zaakArgumenten) {
    return getServices().getDossierService().getMinimalZaken(new ZaakArgumenten(zaakArgumenten, getZaakType()));
  }

  @Override
  public Zaak getNewZaak() {
    Dossier dossier = new DossierRiskAnalysis().getDossier();
    dossier.setDatumIngang(new DateTime());
    return dossier;
  }

  @Override
  @ThrowException("Het is niet mogelijk om de zaak op te vragen")
  public Dossier getStandardZaak(Dossier zaak) {
    getServices().getDossierService().getZaakDossier(zaak, DossierRiskAnalysis.class);
    getServices().getDossierService().getOverigDossier(zaak);
    return super.getStandardZaak(zaak);
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van zaak-ids")
  public List<ZaakKey> getZaakKeys(ZaakArgumenten zaakArgumenten) {
    return getServices().getDossierService().getZaakKeys(new ZaakArgumenten(zaakArgumenten, getZaakType()));
  }

  @Timer
  @ThrowException("Fout bij opslaan van het dossier")
  public Dossier getNewZaak(RiskProfile riskProfile, RiskAnalysisRelatedCase relatedCase) {

    // New Dossier
    Dossier dossier = (Dossier) getNewZaak();
    DossierRiskAnalysis zaakDossier = (DossierRiskAnalysis) dossier.getZaakDossier();

    // Set zaakDossier
    zaakDossier.setRiskProfile(riskProfile);

    if (relatedCase.isRelocation()) {
      VerhuisAanvraag relocation = relatedCase.getRelocation();
      zaakDossier.setRelocation(relatedCase);

      // Add the subjects from the relocation
      for (VerhuisPersoon p : relocation.getPersonen()) {
        DossierPersoon persoon = new DossierPersoon();
        BsPersoonUtils.kopieDossierPersoon(p.getPersoon().getBasisPl(), persoon);

        DossRiskAnalysisSubject subjectItem = new DossRiskAnalysisSubject();
        subjectItem.setPerson(ReflectionUtil.deepCopyBean(DossPer.class, persoon));
        subjectItem.setDossRiskAnalysis(GenericDao.find(DossRiskAnalysis.class, dossier.getCode()));
        subjectItem.getPerson().setTypePersoon(toBigDecimal(DossierPersoonType.BETROKKENE.getCode()));
        zaakDossier.getSubjects().add(subjectItem);
      }
    } else if (relatedCase.isRegistration()) {

      zaakDossier.setRelocation(relatedCase);
      // Add the subjects from the relocation
      for (DossierPersoon p : relatedCase.getRegistration()
          .getDossier().getPersonen(DossierPersoonType.INSCHRIJVER)) {

        DossierPersoon persoon = new DossierPersoon();
        BsPersoonUtils.kopieDossierPersoon(p, persoon);

        DossRiskAnalysisSubject subjectItem = new DossRiskAnalysisSubject();
        subjectItem.setPerson(ReflectionUtil.deepCopyBean(DossPer.class, persoon));
        subjectItem.setDossRiskAnalysis(GenericDao.find(DossRiskAnalysis.class, dossier.getCode()));
        subjectItem.getPerson().setTypePersoon(toBigDecimal(DossierPersoonType.BETROKKENE.getCode()));
        zaakDossier.getSubjects().add(subjectItem);
      }
    } else {
      throw new ProException("Niet toegestane zaak voor een risicoanalyse: " + relatedCase.getZaakId());
    }

    return dossier;
  }

  @Override
  @Transactional
  @ThrowException("Fout bij opslaan van het dossier")
  public void save(Dossier zaak) {

    DossierRiskAnalysis zaakDossier = ZaakUtils.newZaakDossier(zaak, getServices());
    List<DossRiskAnalysisSubject> subjects = new ArrayList<>(zaakDossier.getSubjects());
    zaakDossier.getSubjects().clear();

    ZaakStatusService statussen = getServices().getZaakStatusService();
    getZaakStatussen().setInitieleStatus(zaak, statussen.getInitieleStatus(zaak), "");
    opslaanStandaardZaak(zaak);
    getServices().getDossierService().saveZaakDossier(zaak.getZaakDossier());

    // Add the relation to the related case
    ZaakRelatieService relaties = getServices().getZaakRelatieService();
    ZaakRelatie relatie = new ZaakRelatie();
    relatie.setZaakId(zaak.getZaakId());
    relatie.setZaakType(ZaakType.RISK_ANALYSIS);
    relatie.setGerelateerdZaakId(zaakDossier.getRefCaseId());
    relatie.setGerelateerdZaakType(ZaakType.get(zaakDossier.getRefCaseType().longValue()));
    relaties.save(relatie);

    for (DossRiskAnalysisSubject subject : subjects) {
      // Save DossPers
      subject.getPerson().setDoss(GenericDao.find(Doss.class, zaak.getCode()));
      saveEntity(subject.getPerson());

      // Save RiskAnalysis subject
      subject.setDossRiskAnalysis(GenericDao.find(DossRiskAnalysis.class, zaak.getCode()));
      subject.setcDossPers(subject.getPerson().getCDossPers());
      subject.setScore(toBigDecimal(-1L));
      saveEntity(subject);
    }

    callListeners(ServiceEvent.CHANGE);
  }

  @Transactional
  @ThrowException("Fout bij opslaan van het risicoprofiel")
  public void save(RiskProfile profile) {
    saveEntity(profile);
  }

  @Transactional
  @ThrowException("Fout bij opslaan van het risicoprofiel")
  public void save(RiskProfile profile, Set<RiskProfileRelatedCaseType> types) {
    saveEntity(profile);
    removeTypes(profile);
    types.forEach(t -> saveEntity(t.toRiskProfileType(profile.getcRp())));
  }

  private void removeTypes(RiskProfile profile) {
    removeEntities(getRiskProfileTypes(profile));
  }

  public Set<RiskProfileRelatedCaseType> getTypes(RiskProfile profile) {
    return getRiskProfileTypes(profile).stream()
        .map(RiskProfileRelatedCaseType::of)
        .collect(Collectors.toSet());
  }

  public String getTypesDescription(RiskProfile profile) {
    return getRiskProfileTypes(profile).stream()
        .map(RiskProfileRelatedCaseType::of)
        .map(RiskProfileRelatedCaseType::descr)
        .collect(Collectors.joining(", "));
  }

  private List<RiskProfileType> getRiskProfileTypes(RiskProfile profile) {
    RiskProfileType type = new RiskProfileType();
    type.setCRp(profile.getcRp());
    return findEntity(type);
  }

  @Transactional
  @ThrowException("Fout bij opslaan van het risicoprofielregel")
  public void save(RiskProfileRule profileRule) {
    RiskProfile profile = profileRule.getRiskProfile();
    saveEntity(profileRule);
    profileRule.setRiskProfile(profile);
    profile.getRules().add(profileRule);
  }

  @Transactional
  @ThrowException("Fout bij opslaan van het record")
  public void save(DossRiskAnalysisSubject subject) {
    saveEntity(subject);
  }

  @Override
  @Transactional
  public void delete(Dossier dossier) {
    getServices().getDossierService().deleteDossier(dossier);
    callListeners(ServiceEvent.CHANGE);
  }

  @Transactional
  public void delete(RiskProfile riskProfile) {
    removeEntities(getRiskProfileTypes(riskProfile));
    removeEntity(riskProfile);
  }

  @Transactional
  public void delete(RiskProfileRule rule) {
    rule.getRiskProfile().getRules().remove(rule);
    removeEntity(rule);
  }

  @ThrowException("Fout bij zoeken van de risicoprofielen")
  public List<RiskProfile> getRiskProfiles() {
    return findEntity(new RiskProfile());
  }

  @Override
  public Controles getControles(ControlesListener listener) {
    return new RiskAnalysisChecks(this).getControles(listener);
  }

  /**
   * Returns the applicable cases
   * <ul>
   * <ol>With WACHT_OP_RISICOANALYSE attribute</ol>
   * <ol>Without status INCOMPLEET</ol>
   * <ol>Without existing risk analysis case</ol>
   * <ol>With 1 or more people</ol>
   * </ul>
   */
  public List<RiskAnalysisRelatedCase> getApplicableCases() {
    ZaakArgumenten zaakArgumenten = new ZaakArgumenten();
    zaakArgumenten.addAttributen(WACHT_OP_RISICOANALYSE.getCode());
    zaakArgumenten.addNegeerStatussen(ZaakStatusType.INCOMPLEET);
    ZakenService casesService = getServices().getZakenService();
    return casesService.getStandaardZaken(zaakArgumenten).stream()
        .map(RiskAnalysisRelatedCase::new)
        .collect(toList());
  }

  /**
   * Returns the applicable cases based on the profile and person
   */
  public List<RiskAnalysisRelatedCase> getApplicableCases(RiskProfile profile, BasePLExt pl) {
    ZaakArgumenten zaakArgumenten = new ZaakArgumenten();
    zaakArgumenten.setNummer(pl.getPersoon().getAnr().getVal(), pl.getPersoon().getBsn().getVal());
    zaakArgumenten.addAttributen(WACHT_OP_RISICOANALYSE.getCode());

    ZakenService casesService = getServices().getZakenService();
    return casesService.getStandaardZaken(zaakArgumenten)
        .stream()
        .filter(zaak -> matchProfile(profile, zaak))
        .map(RiskAnalysisRelatedCase::new)
        .collect(toList());
  }

  /**
   * Returns the RiskProfile that is applicable to the case
   */
  public Optional<RiskProfile> getApplicableRiskProfile(Zaak zaak) {
    return getRiskProfiles().stream()
        .filter(profile -> matchProfile(profile, zaak))
        .findFirst();
  }

  private boolean matchProfile(RiskProfile profile, Zaak zaak) {
    Set<RiskProfileRelatedCaseType> types = getTypes(profile);
    return ProfileMatcher.match(zaak, types);
  }

  /**
   * Is the case applicable for a riskprofile case
   */
  public boolean isApplicable(Zaak zaak) {
    return getApplicableRiskProfile(zaak).isPresent();
  }

  /**
   * Does this case have a riskanalysis case
   */
  public boolean hasRiskAnalysisCase(RiskAnalysisRelatedCase relatedCase) {
    return hasRiskAnalysisCase(relatedCase.getZaak());
  }

  public static boolean hasRiskAnalysisCase(Zaak zaak) {
    return zaak.getZaakHistorie()
        .getRelaties()
        .getRelaties()
        .stream()
        .anyMatch(relatie -> ZaakType.RISK_ANALYSIS
            .equals(relatie.getGerelateerdZaakType()));
  }

  public Dossier getRiskAnalysisCase(RiskAnalysisRelatedCase relatedCase) {
    return (Dossier) getServices().getZaakRelatieService().getGerelateerdeZaakByType(relatedCase.getZaak(),
        ZaakType.RISK_ANALYSIS, false);
  }

  @Transactional
  public void delete(RiskProfileSig signal) {
    removeEntity(signal);
  }

  @ThrowException("Fout bij zoeken van de gemarkeerde personen en of adressen")
  public List<RiskProfileSig> getSignals(SIGNALTYPE type) {
    RiskProfileSig sig = new RiskProfileSig();
    sig.setType(type.getCode());
    List<RiskProfileSig> list = findEntity(sig);
    list.sort((o1, o2) -> compareTo(o1.getLabel(), o2.getLabel()));
    return list;
  }

  public void switchSignaling(RiskProfileSig sig) {
    if (getSignal(sig).isPresent()) {
      this.removeSignal(sig);
    } else {
      addSignal(sig);
    }
  }

  @Transactional
  public void addSignal(RiskProfileSig signal) {
    saveEntity(signal);
  }

  @Transactional
  public void removeSignal(RiskProfileSig sig) {
    removeEntity(findEntity(stripForSearchSignal(sig)).get(0));
  }

  /**
   * Returns the stored signal
   */
  @ThrowException("Fout bij zoeken van de gemarkeerde personen en of adressen")
  public Optional<RiskProfileSig> getSignal(RiskProfileSig signal) {
    return getSignal(Arrays.asList(signal));
  }

  /**
   * Returns the first signal instance
   */
  @ThrowException("Fout bij zoeken van de gemarkeerde personen en of adressen")
  public Optional<RiskProfileSig> getSignal(List<RiskProfileSig> signals) {
    return signals.stream()
        .map(example -> findEntity(stripForSearchSignal(example)))
        .filter(sig -> !sig.isEmpty())
        .findFirst()
        .map(sig -> sig.get(0));
  }

  public RiskProfileSig buildBsnSignal(BasePLExt pl) {
    RiskProfileSig sig = new RiskProfileSig();
    sig.setType(BSN.getCode());
    sig.setBsn(BigDecimal.valueOf(pl.getPersoon().getBsn().toLong()));
    sig.setLabel(pl.getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam());
    return sig;
  }

  public RiskProfileSig buildAddressSignal(BasePLExt pl) {
    return buildSignal(new ProcuraPersonListAddress(pl));
  }

  /**
   * Build an instanceof RiskProfileSig based on an address
   */
  public RiskProfileSig buildSignal(Address address) {
    RiskProfileSig sig = new RiskProfileSig();
    sig.setType(ADDRESS.getCode());
    sig.setPc(Postcode.getCompact(address.getPostalCode()));
    sig.setHnr(toBigDecimal(address.getHnr()));
    sig.setHnrL(address.getHnrL());
    sig.setHnrT(address.getHnrT());
    sig.setHnrA(address.getHnrA());
    sig.setLabel(address.getLabel());
    return sig;
  }

  public void onSaveZaak(Zaak zaak) {
    if (hasRiskAnalysisCase(zaak)) {
      return;
    }
    if (isApplicable(zaak)) {
      attribuutService.save(new ZaakAttribuut(zaak.getZaakId(), WACHT_OP_RISICOANALYSE));
    }
  }

  public void removeWaitForRiskAnalysis(Zaak zaak) {
    LOGGER.info("Verwijder attribuut {} van zaak {}", WACHT_OP_RISICOANALYSE, zaak.getZaakId());
    attribuutService.delete(zaak.getZaakId(), WACHT_OP_RISICOANALYSE);
  }

  /**
   * To avoid differences in the search object and db object a new
   * entity is created for searching.
   */
  private RiskProfileSig stripForSearchSignal(RiskProfileSig sig) {
    RiskProfileSig searchSig = new RiskProfileSig();
    searchSig.setType(sig.getType());
    if (BSN.code.equals(sig.getType())) {
      searchSig.setBsn(sig.getBsn());
    } else if (ADDRESS.code.equals(sig.getType())) {
      searchSig.setPc(Postcode.getCompact(sig.getPc()));
      searchSig.setHnr(sig.getHnr());
      searchSig.setHnrL(sig.getHnrL());
      searchSig.setHnrT(sig.getHnrT());
      searchSig.setHnrA(sig.getHnrA());
    } else {
      throw new ProException("Onbekende signalering: " + sig.getType());
    }
    return searchSig;
  }

  public enum SIGNALTYPE {

    UNKNOWN(BigDecimal.valueOf(-1)),
    BSN(BigDecimal.valueOf(1)),
    ADDRESS(BigDecimal.valueOf(2));

    private final BigDecimal code;

    SIGNALTYPE(BigDecimal code) {
      this.code = code;
    }

    public BigDecimal getCode() {
      return code;
    }
  }
}
