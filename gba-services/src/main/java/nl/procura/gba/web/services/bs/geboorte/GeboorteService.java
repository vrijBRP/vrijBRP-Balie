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

package nl.procura.gba.web.services.bs.geboorte;

import static nl.procura.gba.web.common.tables.GbaTables.*;
import static nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoonType.*;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.fil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.jpa.personen.db.DossErk;
import nl.procura.gba.jpa.personen.db.DossNk;
import nl.procura.gba.web.common.tables.GbaTables;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Timer;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.erkenning.DossierErkenning;
import nl.procura.gba.web.services.bs.erkenning.ErkenningService;
import nl.procura.gba.web.services.bs.geboorte.erkenningbuitenproweb.ErkenningBuitenProweb;
import nl.procura.gba.web.services.bs.geboorte.naamskeuzebuitenproweb.NaamskeuzeBuitenProweb;
import nl.procura.gba.web.services.bs.naamskeuze.DossierNaamskeuze;
import nl.procura.gba.web.services.bs.naamskeuze.NaamskeuzeService;
import nl.procura.gba.web.services.zaken.algemeen.*;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContact;
import nl.procura.gba.web.services.zaken.contact.ContactgegevensService;
import nl.procura.gba.web.services.zaken.voorraad.VoorraadStatus;
import nl.procura.gba.web.services.zaken.voorraad.VoorraadType;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

public class GeboorteService extends AbstractZaakContactService<Dossier> implements ZaakService<Dossier> {

  private final static Logger LOGGER = LoggerFactory.getLogger(GeboorteService.class.getName());

  public GeboorteService() {
    super("Geboortes", ZaakType.GEBOORTE);
  }

  public GeboorteService(String name, ZaakType zaakType) {
    super(name, zaakType);
  }

  /**
   * ID-nummers worden nu door de BSM bepaald op het moment van verwerking
   */
  @Transactional
  @ThrowException("Fout bij het bepalen van de nummers")
  @Deprecated
  public void bepaalIdNummers(DossierGeboorte zaakDossier) {

    if (zaakDossier.getDossier().getStatus().isMinimaal(ZaakStatusType.OPGENOMEN)) {

      long gemeenteCode = along(getServices().getGebruiker().getGemeenteCode());
      long moederGemeenteCode = along(zaakDossier.getMoeder().getWoongemeente().getValue());

      // Alleen nummers toevoegen als moeder in gemeente woont
      if (gemeenteCode == moederGemeenteCode) {
        for (DossierPersoon kind : zaakDossier.getKinderen()) {
          if (heeftNieuweIdNummers(kind)) {
            getServices().getDossierService().savePersoon(kind);
            // Verwerk ook de nieuwe nummers in de aktes
            getServices().getAkteService().updateAkteEigenschappen(zaakDossier.getDossier());
          }
        }
      }
    }
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van de geboortezaken")
  public int getZakenCount(ZaakArgumenten zaakArgumenten) {
    return getServices().getDossierService().getZakenCount(new ZaakArgumenten(zaakArgumenten, getZaakType()));
  }

  @Override
  public ZaakContact getContact(Dossier zaak) {

    ZaakContact contact = new ZaakContact();

    DossierGeboorte geboorte = (DossierGeboorte) zaak.getZaakDossier();
    ContactgegevensService cg = getServices().getContactgegevensService();

    contact.add(getServices().getDossierService().getContactPersoon(AANGEVER, geboorte.getAangever()));
    contact.add(getServices().getDossierService().getContactPersoon(MOEDER, geboorte.getMoeder()));
    contact.add(getServices().getDossierService().getContactPersoon(VADER_ERKENNER, geboorte.getVaderErkenner()));

    return contact;
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van de geboortezaken")
  public List<Dossier> getMinimalZaken(ZaakArgumenten zaakArgumenten) {
    return getServices().getDossierService().getMinimalZaken(new ZaakArgumenten(zaakArgumenten, getZaakType()));
  }

  @Override
  public Zaak getNewZaak() {
    return aanvullenZaak(new DossierGeboorte().getDossier());
  }

  @Override
  @Timer
  @ThrowException("Het is niet mogelijk om de zaak op te vragen")
  public Dossier getStandardZaak(Dossier zaak) {

    DossierGeboorte zaakDossier = getServices().getDossierService().getZaakDossier(zaak, DossierGeboorte.class);

    zaakDossier.setVerblijfsLandAfstamming(LAND.get(zaakDossier.getcLandAfstamVb()));
    zaakDossier.setLandNaamRecht(LAND.get(zaakDossier.getcLandNaamRecht()));
    zaakDossier.setKeuzeTitel(TITEL.get(zaakDossier.getKeuzeNaamTp()));
    zaakDossier.setLandAfstammingsRecht(LAND.get(zaakDossier.getcLandAfstamRecht()));
    zaakDossier.setGemeente(PLAATS.get(zaakDossier.getcGemGeb()));

    ErkenningBuitenProweb ep = zaakDossier.getErkenningBuitenProweb();
    ep.setGemeente(PLAATS.get(zaakDossier.getcGemErk()));
    ep.setLandAfstamming(LAND.get(zaakDossier.getcLandAfstamRechtErk()));
    ep.setLandErkenning(LAND.get(zaakDossier.getcLandErk()));

    NaamskeuzeBuitenProweb nk = zaakDossier.getNaamskeuzeBuitenProweb();
    nk.setGemeente(PLAATS.get(zaakDossier.getcGemNk()));
    nk.setLand(LAND.get(zaakDossier.getcLandNk()));

    zaakDossier.setErkenningBijGeboorte(getGekoppeldeErkenning(zaakDossier.getDossErkGeb()));
    zaakDossier.setErkenningVoorGeboorte(getGekoppeldeErkenning(zaakDossier.getDossErk()));
    zaakDossier.setNaamskeuzeVoorGeboorte(getGekoppeldeNaamskeuze(zaakDossier.getDossNk()));

    getServices().getDossierService().getOverigDossier(zaak);
    return super.getStandardZaak(zaak);
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

    DossierGeboorte zaakDossier = ZaakUtils.newZaakDossier(zaak, getServices());
    zaak.setDescr(getDescription(zaakDossier));

    // Werk de ingangsdatum bij aan de hand
    // van de geboortedatums van de kinderen
    updateGeboorteDatums(zaak, zaakDossier);

    getServices().getDossierService()
        .saveDossier(zaak)
        .savePersonen(zaak)
        .saveNationaliteiten(zaak)
        .saveAktes(zaak);

    afleiden(zaakDossier);

    DossierGeboorteVragen vragen = zaakDossier.getVragen();

    if (vragen.heeftErkenningBuitenProweb() && !vragen.magErkenningBuitenProweb()
        || vragen.heeftErkenningVoorGeboorte()) {
      LOGGER.debug("Velden erkenning buiten proweb wordt gereset");
      zaakDossier.getErkenningBuitenProweb().reset();
    }

    if (vragen.heeftErkenningVoorGeboorte() && !vragen.magErkenningVoorGeboorte()) {
      LOGGER.debug("Eerder gedane erkenning wordt losgekoppeld");
    }

    getServices().getDossierService().saveZaakDossier(zaak.getZaakDossier());
  }

  private static String getDescription(DossierGeboorte zaakDossier) {
    List<String> elements = new ArrayList<>();
    elements.add(zaakDossier.getMoeder().getNaam().getVoorv_gesl());
    elements.add(zaakDossier.getVaderErkenner().getNaam().getVoorv_gesl());
    zaakDossier.getDossier().getAktes().forEach(akte -> elements.add(akte.getDescription()));
    return elements.stream()
        .filter(StringUtils::isNotBlank)
        .collect(Collectors.joining(" / "));
  }

  @Override
  @Transactional
  public void delete(Dossier zaak) {
    getServices().getDossierService().deleteDossier(zaak);
  }

  /**
   * Vul waarden in de af te leiden zijn van andere waarden
   */
  private void afleiden(DossierGeboorte zaakDossier) {

    zaakDossier.setGemeente(GbaTables.PLAATS.get(getServices().getGebruiker().getGemeenteCode()));

    if (zaakDossier.getMoeder().isIngeschreven()) {
      for (DossierPersoon kind : zaakDossier.getKinderen()) {
        kind.setWoonplaats(zaakDossier.getMoeder().getWoonplaats());
        kind.setLand(zaakDossier.getMoeder().getLand());
      }
    } else if (zaakDossier.getVader().isIngeschreven()) {
      for (DossierPersoon kind : zaakDossier.getKinderen()) {
        kind.setWoonplaats(zaakDossier.getVader().getWoonplaats());
        kind.setLand(zaakDossier.getVader().getLand());
      }
    }
  }

  /**
   * Zoek de gekoppelde erkenning op
   */
  private DossierErkenning getGekoppeldeErkenning(DossErk dossErk) {
    String zaakId = dossErk != null ? dossErk.getDoss().getDossiernummer() : "";
    if (fil(zaakId)) {
      ErkenningService service = getServices().getErkenningService();
      for (Zaak zaak : service.getMinimalZaken(new ZaakArgumenten(zaakId))) {
        Dossier volledigeErkenning = service.getCompleteZaak((Dossier) zaak);
        return (DossierErkenning) volledigeErkenning.getZaakDossier();
      }
    }

    return null;
  }

  /**
   * Zoek de gekoppelde naamskeuze op
   */
  private DossierNaamskeuze getGekoppeldeNaamskeuze(DossNk dossNk) {
    String zaakId = dossNk != null ? dossNk.getDoss().getDossiernummer() : "";
    if (fil(zaakId)) {
      NaamskeuzeService service = getServices().getNaamskeuzeService();
      for (Zaak zaak : service.getMinimalZaken(new ZaakArgumenten(zaakId))) {
        Dossier volledigeNaamskeuze = service.getCompleteZaak((Dossier) zaak);
        return (DossierNaamskeuze) volledigeNaamskeuze.getZaakDossier();
      }
    }

    return null;
  }

  /**
   * Geeft de laatst persoon terug.
   */
  private List<DossierPersoon> getLaatstGeborene(List<DossierPersoon> personen) {
    List<DossierPersoon> sorted = new ArrayList<>(personen);
    sorted.sort(new GeboorteDatumComparator(false));
    return sorted;
  }

  /**
   * Bepaal de BSN's en A-nummers
   */
  @Deprecated
  private boolean heeftNieuweIdNummers(DossierPersoon kind) {

    boolean heeftNieuweNummers = false;

    if (!kind.getBurgerServiceNummer().isCorrect()) {
      long bsn = getServices().getVoorraadService().getNummer(VoorraadType.BSN, VoorraadStatus.TIJDELIJK);
      kind.setBurgerServiceNummer(new BsnFieldValue(bsn));
      heeftNieuweNummers = true;
    }

    if (!kind.getAnummer().isCorrect()) {
      long anr = getServices().getVoorraadService().getNummer(VoorraadType.ANR, VoorraadStatus.TIJDELIJK);
      kind.setAnummer(new AnrFieldValue(anr));
      heeftNieuweNummers = true;
    }

    return heeftNieuweNummers;
  }

  private void updateGeboorteDatums(Dossier zaak, DossierGeboorte zaakDossier) {
    if (zaakDossier.getKinderen().size() > 0) {
      long datum = getLaatstGeborene(zaakDossier.getKinderen()).get(0).getDatumGeboorte().getLongDate();
      zaak.setDatumIngang(new DateTime(datum));
    }
  }
}
