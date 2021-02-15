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

package nl.procura.gba.web.services.bs.algemeen;

import static java.util.Arrays.asList;
import static nl.procura.gba.common.MiscUtils.copy;
import static nl.procura.gba.common.MiscUtils.copyList;
import static nl.procura.gba.web.common.tables.GbaTables.*;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.KIND;
import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.standard.exceptions.ProExceptionSeverity.INFO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.formats.BurgerlijkeStaatType;
import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.DossDao;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.jpa.personen.utils.GbaDaoUtils;
import nl.procura.gba.web.common.misc.ZakenList;
import nl.procura.gba.web.services.ServiceEvent;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.bs.algemeen.document.DossierDocument;
import nl.procura.gba.web.services.bs.algemeen.enums.SoortVerbintenis;
import nl.procura.gba.web.services.bs.algemeen.functies.BsNatioUtils;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.interfaces.DossierKinderen;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteit;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteiten;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersonen;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.algemeen.vereiste.DossierVereiste;
import nl.procura.gba.web.services.bs.erkenning.DossierErkenning;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.bs.huwelijk.DossierHuwelijk;
import nl.procura.gba.web.services.bs.levenloos.DossierLevenloos;
import nl.procura.gba.web.services.bs.naamskeuze.DossierNaamskeuze;
import nl.procura.gba.web.services.bs.omzetting.DossierOmzetting;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoek;
import nl.procura.gba.web.services.bs.ontbinding.DossierOntbinding;
import nl.procura.gba.web.services.bs.overlijden.buitenland.DossierOverlijdenBuitenland;
import nl.procura.gba.web.services.bs.overlijden.gemeente.DossierOverlijdenGemeente;
import nl.procura.gba.web.services.bs.overlijden.lijkvinding.DossierLijkvinding;
import nl.procura.gba.web.services.bs.riskanalysis.DossierRiskAnalysis;
import nl.procura.gba.web.services.zaken.algemeen.AbstractZaakService;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakNumbers;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoon;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoonType;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class DossierService extends AbstractZaakService<Dossier> implements ZaakNumbers<Dossier> {

  public DossierService() {
    super("Dossiers", null);
  }

  public DossierService(String name, ZaakType zaakType) {
    super(name, zaakType);
  }

  /**
   * Vragen het aantal op aan de hand van zaakargumenten
   */
  @Override
  @ThrowException("Fout bij het zoeken van het dossier")
  public int getZakenCount(ZaakArgumenten zaakArgumenten) {
    return DossDao.findCount(getArgumentenToMap(zaakArgumenten), zaakArgumenten.getListener());
  }

  /**
   * Vraag de aanvragen op aan de hand van zaakargumenten
   */
  @Override
  @ThrowException("Fout bij het zoeken van dossiers")
  public List<Dossier> getMinimalZaken(ZaakArgumenten zaakArgumenten) {
    return new ZakenList(copyList(DossDao.find(getArgumentenToMap(zaakArgumenten), zaakArgumenten.getListener()),
        Dossier.class));
  }

  /**
   * Algemene dossier opvraag functies
   */
  public Dossier getOverigDossier(Dossier dossier) {

    Dossier dImpl = dossier;

    // Opschonen
    dImpl.getDocumenten().clear();
    dImpl.getDocumenten().addAll(copyList(dImpl.getDossDocs(), DossierDocument.class));
    dossier.getPersonen().addAll(copyList(dImpl.getDossPers(), DossierPersoon.class));
    dossier.getNationaliteiten().addAll(copyList(dImpl.getDossNats(), DossierNationaliteit.class));
    dossier.getVereisten().addAll(copyList(dImpl.getDossVereistes(), DossierVereiste.class));

    herladen(dossier);
    herladen(dossier.getPersonen());

    // Aktes
    getServices().getAkteService().laadAktes(dossier);

    return dossier;
  }

  public <T> T getZaakDossier(Dossier zaak, Class<T> type) {

    Class<? extends ZaakDossier> classImplType;

    switch (zaak.getType()) {
      case ERKENNING:
        classImplType = DossierErkenning.class;
        break;

      case NAAMSKEUZE:
        classImplType = DossierNaamskeuze.class;
        break;

      case GEBOORTE:
        classImplType = DossierGeboorte.class;
        break;

      case ONDERZOEK:
        classImplType = DossierOnderzoek.class;
        break;

      case HUWELIJK_GPS_GEMEENTE:
        classImplType = DossierHuwelijk.class;
        break;

      case OMZETTING_GPS:
        classImplType = DossierOmzetting.class;
        break;

      case ONTBINDING_GEMEENTE:
        classImplType = DossierOntbinding.class;
        break;

      case LIJKVINDING:
        classImplType = DossierLijkvinding.class;
        break;

      case OVERLIJDEN_IN_GEMEENTE:
        classImplType = DossierOverlijdenGemeente.class;
        break;

      case OVERLIJDEN_IN_BUITENLAND:
        classImplType = DossierOverlijdenBuitenland.class;
        break;

      case LEVENLOOS:
        classImplType = DossierLevenloos.class;
        break;

      case RISK_ANALYSIS:
        classImplType = DossierRiskAnalysis.class;
        break;

      default:
        throw new ProException(ERROR, "Onbekend dossiertype: " + zaak.getType());
    }

    Class entityClass = GbaDaoUtils.getEntityClass(classImplType, false).getClazz();
    Object object = findEntity(entityClass, zaak.getCode());
    zaak.setZaakDossier(copy(object, classImplType));

    return type.cast(zaak.getZaakDossier());
  }

  @Override
  @ThrowException("Fout bij het zoeken van zaak-ids")
  public List<ZaakKey> getZaakKeys(ZaakArgumenten zaakArgumenten) {
    return DossDao.findZaakKeys(getArgumentenToMap(zaakArgumenten), zaakArgumenten.getListener());
  }

  @ThrowException("Fout bij opslaan van aktes")
  public DossierService saveAktes(Dossier zaak) {
    getServices().getAkteService().addUpdateAktes(zaak);
    return this;
  }

  /**
   * Sla het dossier op
   */
  @Transactional
  @ThrowException("Fout bij het opslaan van het dossier")
  public DossierService saveDossier(Dossier zaak) {
    getZaakStatussen().setInitieleStatus(zaak, ZaakStatusType.INCOMPLEET);
    opslaanStandaardZaak(zaak);
    callListeners(ServiceEvent.CHANGE);
    return this;
  }

  /**
   * Sla de nationaliteiten op
   */
  public DossierService saveNationaliteiten(DossierNationaliteiten zaak) {
    for (DossierNationaliteit nationaliteit : zaak.getNationaliteiten()) {
      nationaliteit.koppelenAan(zaak);
      saveEntity(nationaliteit);
    }

    saveEntity(zaak);
    return this;
  }

  @ThrowException("Fout bij opslaan van personen")
  public DossierService savePersonen(DossierPersonen dossierPersonen) {
    if (dossierPersonen.getPersonen().size() > 0) {
      for (DossierPersoon dossierPersoon : dossierPersonen.getPersonen()) {
        dossierPersoon.koppelenAan(dossierPersonen);
        savePersoon(dossierPersoon);
        savePersonen(dossierPersoon);
      }
    }

    return this;
  }

  @ThrowException("Fout bij opslaan van een persoon")
  public DossierPersoon savePersoon(DossierPersoon persoon) {
    saveEntity(persoon);
    saveNationaliteiten(persoon);
    return persoon;
  }

  @ThrowException("Fout bij opslaan van personen")
  public DossierService saveVereisten(Dossier zaak) {
    for (DossierVereiste vereiste : zaak.getVereisten()) {
      vereiste.koppelenAan(zaak);
      saveEntity(vereiste);
    }

    return this;
  }

  /**
   * Sla het dossier op
   */
  @Transactional
  @ThrowException("Fout bij het opslaan van het dossier")
  public void saveZaakDossier(ZaakDossier zaakDossier) {
    saveEntity(zaakDossier);
    callListeners(ServiceEvent.CHANGE);
  }

  @Transactional
  public void addKind(DossierKinderen dossierKinderen, DossierPersoon kind) {

    kind.setDossierPersoonType(KIND);

    if (dossierKinderen.getDossier()
        .getType()
        .is(ZaakType.ERKENNING, ZaakType.NAAMSKEUZE)
        && !kind.isVolledig()) {
      return;
    }

    if (dossierKinderen.getDossier().isPersoon(kind)) {
      throw new ProException(INFO, kind.getNaam().getPred_adel_voorv_gesl_voorn() + " is al toegevoegd.");
    }

    savePersoon(kind);

    dossierKinderen.getDossier().toevoegenPersoon(kind);

    saveDossier(dossierKinderen.getDossier());
  }

  @Transactional
  public void addNationaliteit(DossierNationaliteiten dossier, DossierNationaliteit nationaliteit) {
    BsNatioUtils.checkNationaliteit(nationaliteit);
    if (!dossier.isNationaliteit(nationaliteit)) {
      dossier.toevoegenNationaliteit(nationaliteit);
    }
  }

  @Transactional
  public void deleteNationaliteiten(DossierNationaliteiten dossier,
      Collection<DossierNationaliteit> nationaliteiten) {

    for (DossierNationaliteit nationaliteit : new ArrayList<>(nationaliteiten)) {
      removeEntity(nationaliteit);
      dossier.verwijderNationaliteit(nationaliteit);
    }
  }

  @Transactional
  public void deletePersonen(Dossier dossier, Collection<DossierPersoon> personen) {
    if (!personen.isEmpty()) {
      for (DossierPersoon persoon : personen) {
        getServices().getAkteService().ontkoppelPersoonVanAktes(dossier, persoon);
        dossier.verwijderPersoon(persoon);
        removeEntity(persoon);
      }
    }
  }

  protected ConditionalMap getArgumentenToMap(ZaakArgumenten zaakArgumenten) {

    ConditionalMap map = getAlgemeneArgumentenToMap(zaakArgumenten);

    Set<ZaakType> typen = zaakArgumenten.getTypen();

    if (getZaakType() != null) { // Alle andere typen weghalen
      map.putList(DossDao.TYPE, asList(getZaakType().getCode()));
    } else {
      if (typen != null) {
        map.putList(DossDao.TYPE, zaakArgumenten.getTypeCodes());
      }
    }

    if (!zaakArgumenten.isAll()) {

      map.putString(DossDao.DOSS_ID, zaakArgumenten.getZaakKey().getZaakId());
      map.putString(DossDao.AKTE_VNR, zaakArgumenten.getAkteVolgnr());
    }

    return map;
  }

  public ZaakContactpersoon getContactPersoon(ZaakContactpersoonType type, DossierPersoon persoon) {
    ZaakContactpersoon contactPersoon = new ZaakContactpersoon(type,
        persoon.getNaam().getPred_eerstevoorn_adel_voorv_gesl());
    contactPersoon.setContactgegevens(getServices()
        .getContactgegevensService()
        .getContactgegevens(persoon));
    return contactPersoon;
  }

  /**
   * Herlaad een object
   */
  protected <T> List<T> herladen(List<T> records) {

    List<T> list = new ArrayList<>();

    for (Object record : records) {
      list.add((T) herladen(record));
    }

    return list;
  }

  protected <T> T herladen(T object) {

    if (object instanceof DossierPersoon) {
      return (T) herlaadPersoon((DossierPersoon) object);
    } else if (object instanceof DossierNationaliteit) {
      return (T) herlaadNationaliteit((DossierNationaliteit) object);
    } else if (object instanceof DossierNationaliteiten) {
      return (T) herlaadNationaliteiten((DossierNationaliteiten) object);
    } else {
      throw new IllegalArgumentException("Onbekende waarde: " + object.getClass());
    }
  }

  @Transactional
  @ThrowException("Fout bij het verwijderen van het dossier")
  public void deleteDossier(Dossier dossier) {
    removeEntity(dossier);
    deleteZaakRelaties(dossier);
    callListeners(ServiceEvent.CHANGE);
  }

  /**
   * Voegt een aantal afgeleide gegevens toe aan DossierPersoon
   */
  private DossierPersoon aanvullen(DossierPersoon d) {

    DossierPersoon impl = d;

    // Plaatsen
    impl.setWoonplaats(new FieldValue(impl.getWoonPlaats()));
    impl.setGeboorteplaats(PLAATS.get(impl.getCGebPlaats(), impl.getGebPlaats()));
    impl.setWoongemeente(PLAATS.get(impl.getCWoonGemeente(), astr(impl.getWoongemeente())));
    impl.setGeboorteAktePlaats(PLAATS.get(impl.getcGebAktePlaats()));
    impl.setInGemeente(getServices().getGebruiker().isGemeente(along(impl.getCWoonGemeente())));

    // Landen
    impl.setLand(LAND.get(impl.getCLand()));
    impl.setGeboorteland(LAND.get(impl.getCGebLand()));

    BigDecimal cSluitPlaats = impl.getcHuwSluitPlaats();
    BigDecimal sOntbPlaats = impl.getcHuwOntbPlaats();

    FieldValue ontbPlaats = PLAATS.get(sOntbPlaats, impl.getHuwOntbPlaats());

    impl.getVerbintenis().getSluiting().setPlaats(PLAATS.get(cSluitPlaats, impl.getHuwSluitPlaats()));
    impl.getVerbintenis().getSluiting().setLand(LAND.get(impl.getcHuwSluitLand()));

    impl.getVerbintenis().getOntbinding().setPlaats(ontbPlaats);
    impl.getVerbintenis().getOntbinding().setLand(LAND.get(impl.getcHuwOntbLand()));
    impl.getVerbintenis().getOntbinding().setReden(REDEN_HUW_ONTB.get(impl.getHuwOntbRdn()));
    impl.getVerbintenis().setSoort(SoortVerbintenis.get(impl.getSrtHuw()));

    impl.setTitel(TITEL.get(impl.getTp()));
    impl.setPartnerTitel(TITEL.get(impl.getpTp()));
    impl.setBurgerlijkeStaat(BurgerlijkeStaatType.get(impl.getBurgStaat()));
    impl.setVerblijfstitel(VBT.get(impl.getCVbt()));

    // Nationaliteiten
    impl.getNationaliteiten().clear();
    impl.getNationaliteiten().addAll(herladen(copyList(impl.getDossNats(), DossierNationaliteit.class)));

    // Gerelateerde personen
    impl.getPersonen().clear();
    impl.getPersonen().addAll(copyList(impl.getDossPers(), DossierPersoon.class));

    return impl;
  }

  /**
   * Zoek DossierPersoon op basis van BSN
   */
  private DossierPersoon getPersoon(BsnFieldValue bsn) {

    if (bsn != null && pos(bsn.getValue())) {
      BasePLExt basisPl = getServices().getPersonenWsService().getPersoonslijst(bsn.getStringValue());

      if (basisPl != null) {
        return BsPersoonUtils.kopieDossierPersoon(basisPl, new DossierPersoon());
      }
    }

    return null;
  }

  /**
   * Herlaad de nationaliteit
   */
  private DossierNationaliteit herlaadNationaliteit(DossierNationaliteit natio) {

    FieldValue rv = natio.getRedenverkrijgingNederlanderschap();
    rv.setDescription(REDEN_NATIO.get(rv.getStringValue()).getDescription());
    return natio;
  }

  /**
   * Herlaad de nationaliteit
   */
  private DossierNationaliteiten herlaadNationaliteiten(DossierNationaliteiten dossier) {

    for (DossierNationaliteit nationaliteit : dossier.getNationaliteiten()) {
      herlaadNationaliteit(nationaliteit);
    }

    return dossier;
  }

  /**
   * Herlaad DossierPersoon op basis van de BSN
   */
  private DossierPersoon herlaadPersoon(DossierPersoon persoon) {

    aanvullen(persoon);

    if (!persoon.isVolledig()) {

      // Zoeken in BRP
      DossierPersoon newPersoon = getPersoon(persoon.getBurgerServiceNummer());

      if (newPersoon != null) {
        BsPersoonUtils.kopieDossierPersoon(newPersoon, persoon);
      }
    }

    for (DossierPersoon relatiePersoon : persoon.getPersonen()) {
      herlaadPersoon(relatiePersoon);
    }

    return persoon;
  }
}
