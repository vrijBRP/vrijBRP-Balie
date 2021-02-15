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

package nl.procura.gba.web.services.bs.algemeen.akte;

import static java.util.Collections.singletonList;
import static nl.procura.gba.common.MiscUtils.copy;
import static nl.procura.gba.common.MiscUtils.copyList;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.KIND;
import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.jpa.personen.dao.DossAkteDao;
import nl.procura.gba.jpa.personen.db.DossAkte;
import nl.procura.gba.jpa.personen.db.DossAkteRd;
import nl.procura.gba.jpa.personen.db.DossPer;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.ZaakDossier;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.interfaces.DossierKinderen;
import nl.procura.gba.web.services.bs.algemeen.interfaces.DossierMetAkte;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.erkenning.DossierErkenning;
import nl.procura.gba.web.services.bs.erkenning.ErkenningsType;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.bs.huwelijk.DossierHuwelijk;
import nl.procura.gba.web.services.bs.naamskeuze.DossierNaamskeuze;
import nl.procura.gba.web.services.bs.naamskeuze.NaamskeuzeType;
import nl.procura.gba.web.services.bs.omzetting.DossierOmzetting;
import nl.procura.gba.web.services.bs.overlijden.DossierOverlijden;
import nl.procura.gba.web.services.bs.overlijden.lijkvinding.DossierLijkvinding;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.gba.web.services.zaken.algemeen.ControleerbareService;
import nl.procura.gba.web.services.zaken.algemeen.controle.Controles;
import nl.procura.gba.web.services.zaken.algemeen.controle.ControlesListener;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

public class AkteService extends AbstractService implements ControleerbareService {

  public AkteService() {
    super("Aktes");
  }

  /**
   * Vertaald ZAAK_ID value in map naar een Set
   */
  public static List<Long> getSoortSet(Set<DossierAkteRegistersoort> soorten) {
    return soorten.stream()
        .filter(Objects::nonNull)
        .map(DossierAkteRegistersoort::getCode)
        .collect(Collectors.toList());
  }

  /**
   * Voeg een akte toe of maakt er 1 aan
   */
  public void addUpdateAktes(Dossier dossier) {
    ZaakDossier zaakDossier = dossier.getZaakDossier();
    if (zaakDossier instanceof DossierMetAkte) {
      DossierMetAkte dossierMetAkte = (DossierMetAkte) zaakDossier;
      if (zaakDossier instanceof DossierKinderen) {
        // Aktes toevoegen als het kind nog geen akte heeft
        DossierKinderen dk = (DossierKinderen) zaakDossier;
        if (dk.isAktePerKind()) {
          for (DossierPersoon persoon : dossier.getPersonen(KIND)) {
            if (persoon.isVolledig()) {
              if (!getServices().getAkteService().heeftAkte(persoon)) {
                getServices().getAkteService().addAkte(dossier, singletonList(persoon));
              }
            }
          }
        }

        // Bij ongeboren kind zijn er geen kinderen, dus geen akte volgens
        // bovenstaande code.
        if (zaakDossier instanceof DossierErkenning) {
          DossierErkenning erkenning = (DossierErkenning) zaakDossier;
          if (erkenning.getErkenningsType().is(ErkenningsType.ERKENNING_ONGEBOREN_VRUCHT)) {
            if (dossier.getAktes().isEmpty()) {
              addAkte(dossier, dossierMetAkte.getAktePersonen());
            }
          }
        }
        if (zaakDossier instanceof DossierNaamskeuze) {
          DossierNaamskeuze naamskeuze = (DossierNaamskeuze) zaakDossier;
          if (naamskeuze.getDossierNaamskeuzeType().is(NaamskeuzeType.NAAMSKEUZE_VOOR_GEBOORTE)) {
            if (dossier.getAktes().isEmpty()) {
              addAkte(dossier, dossierMetAkte.getAktePersonen());
            }
          }
        }
      } else {
        // Akte toevoegen voor gewone dossiers met 1 akte per dossier.
        if (dossier.getAktes().isEmpty()) {
          addAkte(dossier, dossierMetAkte.getAktePersonen());
        }
      }

      updateAktePersonen(dossier.getAktes(), dossierMetAkte.getAktePersonen());
    }
  }

  public List<DossierAkteCategorie> getAkteRegisterCategorieen() {
    return copyList(DossAkteDao.getAkteLocaties(), DossierAkteCategorie.class);
  }

  public DossierAkteDeel getAkteRegisterDeel(DossierAkte akte) {
    DossAkteRd dosAkteDeel = DossAkteDao.getAkteDeel(along(akte.getRegistersoort()), akte.getRegisterdeel());
    if (dosAkteDeel != null) {
      return copy(dosAkteDeel, DossierAkteDeel.class);
    }
    return null;
  }

  public List<DossierAkteDeel> getAkteRegisterDelen(DossierAkteCategorie categorie) {
    return copyList(DossAkteDao.getAkteDelenAhvCategorie(categorie.getCode()), DossierAkteDeel.class);
  }

  public List<DossierAkteDeel> getAkteRegisterDelen(long registerSoort) {
    return copyList(DossAkteDao.getAkteDelenAhvRegistersoort(registerSoort), DossierAkteDeel.class);
  }

  public List<DossierAkte> getAktes(Dossier dossier) {
    List<DossierAkte> klappers = new ArrayList<>();

    if (dossier.getAktes() != null) {
      klappers.addAll(dossier.getAktes());
    }

    if (dossier.getZaakDossier() instanceof DossierGeboorte) {
      DossierGeboorte geboorte = (DossierGeboorte) dossier.getZaakDossier();

      switch (geboorte.getErkenningsType()) {
        case ERKENNING_BIJ_AANGIFTE:
          klappers.addAll(getAktes(geboorte.getErkenningBijGeboorte().getDossier()));
          break;

        case ERKENNING_ONGEBOREN_VRUCHT:
          if (geboorte.getVragen().heeftErkenningVoorGeboorte()) {
            klappers.addAll(getAktes(geboorte.getErkenningVoorGeboorte().getDossier()));
          }
          break;

        default:
          break;
      }
    }

    return KlapperUtils.sorteren(klappers, KlapperVolgordeType.DATUM_OPLOPEND);
  }

  public List<DossierAkte> getAktes(KlapperZoekargumenten searchArgumenten) {
    List<DossierAkte> aktes = copyList(DossAkteDao.find(getArgumentenToMap(searchArgumenten)), DossierAkte.class);
    return KlapperUtils.sorteren(aktes, searchArgumenten.getVolgorde());
  }

  public long getAkteVolgnummer(long codeAkte, long datum, DossierAkteDeel registerdeel) {
    return DossAkteDao.getAkteVolgnr(codeAkte, datum, registerdeel.getRegisterSoort().getCode(),
        registerdeel.getRegisterdeel());
  }

  @Override
  public Controles getControles(ControlesListener listener) {
    return new DossierAkteControles(this).getControles(listener);
  }

  public List<BigDecimal> getJaren() {
    return DossAkteDao.getJaren();
  }

  public boolean isCheck(DossierAkte akte, DossierAkteNummer akteNummer, AkteListener listener) {
    long datum = akteNummer.getDatum();
    long registersoort = akteNummer.getSoort().getCode();
    String registerdeel = akteNummer.getDeel().getRegisterdeel();
    long vnr = akteNummer.getVolgNummer();
    long codeAkte = isSaved(akte) ? akte.getCDossAkte() : -1;

    DossAkteDao.checkBruikbaarVolgnr(vnr, codeAkte, datum, registersoort, registerdeel);

    if (DossierAkteInvoerType.PROWEB_PERSONEN.equals(akte.getInvoerType())) {
      long correctVnr = DossAkteDao.getAkteVolgnr(codeAkte, datum, registersoort, registerdeel);

      if (!DossAkteDao.eersteVolgendeVolgnr(codeAkte, datum, registersoort, registerdeel, vnr)) {
        if (listener == null) { // Geen listener, dus altijd goed
          // gekeurd
          return true;
        }

        listener.verkeerdeAkte(akteNummer, vnr, correctVnr);
        return false;
      }
    }

    return true;
  }

  /**
   * Laad de aktes met alle personen
   */
  public void laadAktes(Dossier dossier) {
    dossier.getAktes().clear();
    for (DossAkte dossAkte : dossier.getDossAktes()) {
      DossierAkte akte = copy(dossAkte, DossierAkte.class);
      dossier.getAktes().add(akte);
      for (DossPer dossPer : dossAkte.getDossPers()) {
        for (DossierPersoon persoon : dossier.getAllePersonen()) {
          if (persoon.equals(dossPer)) {
            akte.addPersoon(persoon);
          }
        }
      }
    }
  }

  public void ontkoppelPersoonVanAktes(Dossier dossier, DossierPersoon persoon) {

    for (DossierAkte akte : getAktes(dossier, persoon)) {
      akte.ontkoppelPersoon(persoon);
    }

    for (DossierAkte akte : getAktes(dossier, persoon)) {
      persoon.ontkoppelAkte(akte);
    }
  }

  /**
   * Controleert of het aktenummer al in gebruik is.
   */
  @Transactional
  public void save(Dossier dossier, DossierAkte akte, DossierAkteNummer akteNummer, boolean skipCheck) {
    if (skipCheck || isCheck(akte, akteNummer, null)) {
      akte.setJaar(toBigDecimal(akteNummer.getJaar()));
      akte.setRegistersoort(toBigDecimal(akteNummer.getSoort().getCode()));
      akte.setRegisterdeel(akteNummer.getDeel().getRegisterdeel());
      akte.setVnr(toBigDecimal(akteNummer.getVolgNummer()));

      // Als de zaak is ingevoerd in PROWEB dan akteaanduiding opslaan
      // Bij de klapper kan dossier null zijn.
      if (akte.isDossierAkte() && dossier != null) {
        if (dossier.getZaakDossier() instanceof DossierMetAkte) {
          DossierMetAkte dossierMetAkte = (DossierMetAkte) dossier.getZaakDossier();
          akte.setAkteAand(dossierMetAkte.getAkteAanduiding());
          if (!pos(akte.getdIn())) {
            akte.setdIn(toBigDecimal(dossierMetAkte.getAkteDatum().getLongDate()));
          }
        }
      }

      updateAkteEigenschappen(dossier, akte);
    }
  }

  @ThrowException("Fout bij het opslaan van de akte")
  @Transactional
  public void save(DossierAkte dossierAkte) {
    saveEntity(dossierAkte);
  }

  @ThrowException("Fout bij het opslaan van de categorie")
  @Transactional
  public void saveRegisterCategorie(DossierAkteCategorie categorie) {
    saveEntity(categorie);
  }

  @ThrowException("Fout bij het opslaan van het registerdeel")
  @Transactional
  public void saveRegisterDeel(DossierAkteDeel deel) {
    if (!pos(deel.getMin())) {
      throw new ProException(WARNING, "Min moet hoger zijn dan 0.");
    }

    if (along(deel.getMax()) <= along(deel.getMin())) {
      throw new ProException(WARNING, "Max moet hoger zijn dan min.");
    }

    saveEntity(deel);
  }

  /**
   * Update alle afgeleiden velden op de akte (personen, datums, etc)
   */
  public void updateAkteEigenschappen(Dossier dossier) {
    for (DossierAkte akte : dossier.getAktes()) {
      updateAkteEigenschappen(dossier, akte);
    }
  }

  /**
   * Verwijder een bepaald akte van een dossier
   */
  public void deleteAkte(Dossier dossier, DossierAkte akte) {
    for (DossierPersoon aktePersoon : akte.getPersonen()) {
      aktePersoon.ontkoppelAkte(akte);
    }

    if (dossier != null) {
      dossier.verwijderAkte(akte);
    }

    removeEntity(akte);
  }

  @ThrowException("Fout bij het verwijderen van de register categorie")
  @Transactional
  public void deleteRegisterCategorie(DossierAkteCategorie categorie) {
    removeEntity(categorie);
  }

  @ThrowException("Fout bij het verwijderen van het registerdeel")
  @Transactional
  public void deleteRegisterDeel(DossierAkteDeel deel) {
    removeEntity(deel);
  }

  private void addAkte(Dossier dossier, Collection<DossierPersoon> personen) {
    DossierAkte akte = dossier.addAkte(saveEntity(new DossierAkte(dossier)));
    updateAktePersonen(singletonList(akte), personen);
  }

  private List<DossierAkte> getAktes(Dossier dossier, DossierPersoon persoon) {
    List<DossierAkte> list = new ArrayList<>();
    for (DossierAkte akte : new ArrayList<>(dossier.getAktes())) {
      for (DossierPersoon aktePersoon : akte.getPersonen()) {
        if (aktePersoon.equals(persoon)) {
          list.add(akte);
        }
      }
    }

    return list;
  }

  private ConditionalMap getArgumentenToMap(KlapperZoekargumenten zoekArgumenten) {

    ConditionalMap map = new ConditionalMap();
    map.putLong(DossAkteDao.D_INVOER_VANAF, zoekArgumenten.getDatumVanaf());
    map.putLong(DossAkteDao.D_INVOER_TM, zoekArgumenten.getDatumTm());
    map.putLong(DossAkteDao.JAAR_VAN, zoekArgumenten.getJaarVan());
    map.putLong(DossAkteDao.JAAR_TM, zoekArgumenten.getJaarTm());
    map.putLong(DossAkteDao.DATUM, zoekArgumenten.getDatum());
    map.putLong(DossAkteDao.VNR, zoekArgumenten.getNummer());
    map.putLong(DossAkteDao.BSN, zoekArgumenten.getBsn());
    map.putString(DossAkteDao.NAAM, zoekArgumenten.getGeslachtsnaam());

    if (zoekArgumenten.getSoorten().size() > 0) {
      map.putList(DossAkteDao.SOORT, getSoortSet(zoekArgumenten.getSoorten()));
    }

    if (zoekArgumenten.getDeel() != null) {
      map.putString(DossAkteDao.DEEL, zoekArgumenten.getDeel().getRegisterdeel());
    }

    if (zoekArgumenten.getInvoerType() != null) {
      map.putString(DossAkteDao.INVOER_TYPE, zoekArgumenten.getInvoerType().getCode());
    }

    return map;
  }

  private boolean heeftAkte(DossierPersoon persoon) {
    return persoon.getDossAktes().size() > 0;
  }

  private void setAkteNamen(List<DossierPersoon> personen, DossierAktePersoon aktepersoon) {

    aktepersoon.setBurgerServiceNummer(new BsnFieldValue(-1));
    aktepersoon.setVoornaam("");
    aktepersoon.setGeslachtsnaam("");
    aktepersoon.setVoorvoegsel("");
    aktepersoon.setGeslacht(Geslacht.ONBEKEND);
    aktepersoon.setGeboortedatum(new GbaDateFieldValue(-1));

    personen.stream().findFirst().ifPresent(persoon -> {
      aktepersoon.setBurgerServiceNummer(persoon.getBurgerServiceNummer());
      aktepersoon.setVoornaam(persoon.getVoornaam());
      aktepersoon.setGeslachtsnaam(persoon.getGeslachtsnaam());
      aktepersoon.setVoorvoegsel(persoon.getVoorvoegsel());
      aktepersoon.setGeslacht(persoon.getGeslacht());
      aktepersoon.setGeboortedatum(persoon.getDatumGeboorte());
    });
  }

  /**
   * Update alle afgeleiden velden op de akte (personen, datums, etc)
   */
  private void updateAkteEigenschappen(Dossier dossier, DossierAkte akte) {

    if (dossier != null && dossier.getZaakDossier() != null) {

      if (dossier.getZaakDossier() instanceof DossierOmzetting) {
        DossierOmzetting zaakDossier = (DossierOmzetting) dossier.getZaakDossier();
        akte.setDatumFeit(new GbaDateFieldValue(zaakDossier.getDatumVerbintenis()));
        setAkteNamen(akte.getPersonen(DossierPersoonType.PARTNER1), akte.getAktePersoon());
        setAkteNamen(akte.getPersonen(DossierPersoonType.PARTNER2), akte.getAktePartner());

      } else if (dossier.getZaakDossier() instanceof DossierHuwelijk) {
        DossierHuwelijk zaakDossier = (DossierHuwelijk) dossier.getZaakDossier();
        akte.setDatumFeit(new GbaDateFieldValue(zaakDossier.getDatumVerbintenis()));
        setAkteNamen(akte.getPersonen(DossierPersoonType.PARTNER1), akte.getAktePersoon());
        setAkteNamen(akte.getPersonen(DossierPersoonType.PARTNER2), akte.getAktePartner());

      } else if (dossier.getZaakDossier() instanceof DossierLijkvinding) {
        DossierLijkvinding zaakDossier = (DossierLijkvinding) dossier.getZaakDossier();
        akte.setDatumFeit(new GbaDateFieldValue(zaakDossier.getDatumLijkvinding()));
        setAkteNamen(akte.getPersonen(DossierPersoonType.OVERLEDENE), akte.getAktePersoon());

      } else if (dossier.getZaakDossier() instanceof DossierOverlijden) {
        DossierOverlijden zaakDossier = (DossierOverlijden) dossier.getZaakDossier();
        akte.setDatumFeit(new GbaDateFieldValue(zaakDossier.getDatumOverlijden()));
        setAkteNamen(akte.getPersonen(DossierPersoonType.OVERLEDENE), akte.getAktePersoon());

      } else if (dossier.getZaakDossier() instanceof DossierGeboorte) {
        for (DossierPersoon kind : akte.getPersonen(DossierPersoonType.KIND)) {
          akte.setDatumFeit(kind.getDatumGeboorte());
          setAkteNamen(akte.getPersonen(DossierPersoonType.KIND), akte.getAktePersoon());
        }
      } else if (dossier.getZaakDossier() instanceof DossierErkenning) {
        DossierErkenning zaakDossier = (DossierErkenning) dossier.getZaakDossier();
        akte.setDatumFeit(new GbaDateFieldValue(zaakDossier.getDossier().getDatumIngang()));
        setAkteNamen(akte.getPersonen(DossierPersoonType.KIND), akte.getAktePersoon());

      } else if (dossier.getZaakDossier() instanceof DossierNaamskeuze) {
        DossierNaamskeuze zaakDossier = (DossierNaamskeuze) dossier.getZaakDossier();
        akte.setDatumFeit(new GbaDateFieldValue(zaakDossier.getDossier().getDatumIngang()));
        setAkteNamen(akte.getPersonen(DossierPersoonType.KIND), akte.getAktePersoon());
      }
    }

    save(akte);
  }

  /**
   * Voeg personen toe aan de akte
   */
  private void updateAktePersonen(Collection<DossierAkte> aktes, Collection<DossierPersoon> personen) {
    for (DossierAkte akte : aktes) {
      for (DossierPersoon persoon : personen) {
        persoon.addAkte(akte);
        saveEntity(persoon);
      }

      saveEntity(akte);
    }
  }
}
