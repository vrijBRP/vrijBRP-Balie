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

package nl.procura.gba.web.services.bs.naturalisatie;

import static nl.procura.gba.common.MiscUtils.copyList;
import static nl.procura.gba.common.MiscUtils.to;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.TOESTEMMINGGEVER;
import static nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils.kopieDossierPersoon;
import static nl.procura.gba.web.services.bs.naturalisatie.NaturalisatieDossierNummer.ofGemeente;
import static nl.procura.gba.web.services.bs.naturalisatie.NaturalisatieDossierNummer.ofValue;
import static nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoonType.AANGEVER;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.trim;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.openoffice.DocumentPL;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.DossNatDao;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.jpa.personen.db.DossNaturVerz;
import nl.procura.gba.web.services.ServiceEvent;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Timer;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.zaken.algemeen.AbstractZaakContactService;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakService;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContact;
import nl.procura.java.reflection.ReflectionUtil;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import org.apache.commons.lang3.StringUtils;

public class NaturalisatieService extends AbstractZaakContactService<Dossier> implements ZaakService<Dossier> {

  private static final String BSM_NATURALISATIE_PROCESSOR_ID = "procura.personen.probev.naturalisatie";

  public NaturalisatieService() {
    super("Naturalisatie", ZaakType.NATURALISATIE);
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van naturalisatie & optie zaken")
  public int getZakenCount(ZaakArgumenten zaakArgumenten) {
    return getServices().getDossierService().getZakenCount(new ZaakArgumenten(zaakArgumenten, getZaakType()));
  }

  @Override
  public ZaakContact getContact(Dossier zaak) {
    ZaakContact contact = new ZaakContact();
    DossierNaturalisatie naturalisatie = (DossierNaturalisatie) zaak.getZaakDossier();
    contact.add(getServices().getDossierService().getContactPersoon(AANGEVER, naturalisatie.getAangever()));
    return contact;
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van naturalisatie & optie zaken")
  public List<Dossier> getMinimalZaken(ZaakArgumenten zaakArgumenten) {
    return getServices().getDossierService().getMinimalZaken(new ZaakArgumenten(zaakArgumenten, getZaakType()));
  }

  @Override
  public Zaak getNewZaak() {
    Dossier dossier = aanvullenZaak(new DossierNaturalisatie().getDossier());
    dossier.setDatumIngang(new DateTime());
    return dossier;
  }

  @Override
  @ThrowException("Het is niet mogelijk om de zaak op te vragen")
  public Dossier getStandardZaak(Dossier zaak) {

    DossierNaturalisatie zaakDossier = getServices().getDossierService()
        .getZaakDossier(zaak, DossierNaturalisatie.class);

    copyList(zaakDossier.getDossNaturVerz(), DossierNaturalisatieVerzoeker.class).forEach(bron -> {
      zaakDossier.getVerzoekerGegevens().add(bron);
    });

    getServices().getDossierService().getOverigDossier(zaak);

    return super.getStandardZaak(zaak);
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van zaak-ids")
  public List<ZaakKey> getZaakKeys(ZaakArgumenten zaakArgumenten) {
    return getServices().getDossierService().getZaakKeys(new ZaakArgumenten(zaakArgumenten, getZaakType()));
  }

  public void saveVerzoekerGegevens(DossierNaturalisatie zaakDossier) {
    // Add 'verzoekerGegevens' if verzoeker is present
    for (DossierPersoon verzoekerPersoon : zaakDossier.getAlleVerzoekers()) {
      if (!zaakDossier.isVerzoekerGegevens(verzoekerPersoon.getBurgerServiceNummer())) {
        DossierNaturalisatieVerzoeker verzoekerGegevens = new DossierNaturalisatieVerzoeker();
        verzoekerGegevens.setBsn(BigDecimal.valueOf(verzoekerPersoon.getBsn().longValue()));
        zaakDossier.getVerzoekerGegevens().add(verzoekerGegevens);
      }
    }
    // Remove 'verzoekerGegevens' if no verzoeker person
    for (DossierNaturalisatieVerzoeker verzoekerGegevens : new ArrayList<>(zaakDossier.getVerzoekerGegevens())) {
      BsnFieldValue bsn = verzoekerGegevens.getBurgerServiceNummer();
      if (!zaakDossier.isVerzoeker(bsn)) {
        deleteVerzoeker(zaakDossier, verzoekerGegevens);
      }
    }
  }

  @Override
  @Transactional
  @ThrowException("Fout bij opslaan van het dossier")
  public void save(Dossier zaak) {
    DossierNaturalisatie zaakDossier = ZaakUtils.newZaakDossier(zaak, getServices());
    setDerivedValues(zaakDossier);
    getServices().getDossierService().saveDossier(zaak);
    getServices().getDossierService().savePersonen(zaak);
    opslaanVerzoekerGegevens(zaakDossier);
    getServices().getDossierService().saveZaakDossier(zaak.getZaakDossier());
    callListeners(ServiceEvent.CHANGE);
  }

  /**
   * Algemene afleidingen.
   * Gegevens die bedoeld zijn voor bijvoorbeeld het afdrukken van documenten.
   */
  @Override
  public Dossier setVolledigeZaakExtra(Dossier zaak) {
    return super.setVolledigeZaakExtra(zaak);
  }

  @Override
  @Transactional
  public void delete(Dossier zaak) {
    getServices().getDossierService().deleteDossier(zaak);
  }

  @Transactional
  public void deleteVerzoeker(DossierNaturalisatie zaakDossier, DossierNaturalisatieVerzoeker verzoeker) {
    if (verzoeker.getCode() != null) {
      DossierNaturalisatie zaakDossierImpl = to(zaakDossier, DossierNaturalisatie.class);
      zaakDossierImpl.getVerzoekerGegevens().remove(verzoeker);
      zaakDossierImpl.getDossNaturVerz().remove(ReflectionUtil.deepCopyBean(DossNaturVerz.class, verzoeker));
      removeEntity(verzoeker);
    }
  }

  public NaturalisatieDossierNummer getNextDossierNummer() {
    long gemeenteCode = along(getServices().getGebruiker().getGemeenteCode());
    NaturalisatieDossierNummer defaultNumber = ofGemeente(gemeenteCode);
    NaturalisatieDossierNummer currentNumber = ofValue(DossNatDao.getMaxDossierNummer(
        defaultNumber.getGemeenteCode(),
        defaultNumber.getYearDigit()));
    return currentNumber.isValid() ? currentNumber.getNextNumber() : defaultNumber;
  }

  public void herlaadToestemminggevers(DossierNaturalisatie zaakDossier) {
    for (DossierNaturalisatieVerzoeker gegevens : zaakDossier.getVerzoekerGegevens()) {
      BsnFieldValue bsn = gegevens.getBsnToestemminggever();
      if (bsn.isCorrect() && gegevens.getToestemminggever() == null) {
        BasePLExt pl = getServices().getPersonenWsService().getPersoonslijst(bsn.toString());
        if (pl != null) {
          DossierPersoon dossierPersoon = kopieDossierPersoon(pl);
          dossierPersoon.setDossierPersoonType(TOESTEMMINGGEVER);
          dossierPersoon.setPersoon(new DocumentPL(pl));
          gegevens.setToestemminggever(dossierPersoon);
        }
      }
    }
  }

  private void opslaanVerzoekerGegevens(DossierNaturalisatie zaakDossier) {
    for (DossierNaturalisatieVerzoeker gegevens : zaakDossier.getVerzoekerGegevens()) {
      // Reset values
      if (zaakDossier.isOptie()) {
        gegevens.setInburgeringType(null);
        gegevens.setNaamstGeslGew("");
        gegevens.setNaamstVoornGew("");
        gegevens.setAdviesBurgemeesterType(null);
        gegevens.setBehDKoningBesluit(null);
        gegevens.setBehNrKoningBesluit(null);
      } else {
        gegevens.setBeslissingType(null);
        gegevens.setBehDBevest(null);
      }
      gegevens.setDossier(zaakDossier);
      saveEntity(gegevens);
    }
  }

  private static void setDerivedValues(DossierNaturalisatie zaakDossier) {
    zaakDossier.getDossier().setDescr(getDescription(zaakDossier));
  }

  private static String getDescription(DossierNaturalisatie zaakDossier) {
    DossierPersoon persoon = zaakDossier.getAangever();
    return trim(Stream.of(zaakDossier.isOptie() ? "Optie" : "Naturalisatie",
        zaakDossier.getDossierNummer().getValue(),
        persoon.getNaam().getGeslachtsnaam(),
        persoon.getDatumGeboorteStandaard(),
        persoon.getAdres().getAdres())
        .filter(StringUtils::isNotBlank)
        .collect(Collectors.joining(" / ")));
  }
}
