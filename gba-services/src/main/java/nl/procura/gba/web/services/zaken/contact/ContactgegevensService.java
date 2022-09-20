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

package nl.procura.gba.web.services.zaken.contact;

import static java.util.Collections.emptyList;
import static nl.procura.gba.common.ZaakType.*;
import static nl.procura.standard.Globalfunctions.*;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.jpa.personen.dao.Aant3Dao;
import nl.procura.gba.jpa.personen.db.Aant3;
import nl.procura.gba.web.common.tables.GbaTables;
import nl.procura.gba.web.common.validators.GbaEmailValidator;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.ServiceEvent;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.interfaces.DossierPartners;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.java.Pair;
import nl.procura.sms.rest.number.DutchPhoneNumberParser;
import nl.procura.sms.rest.number.MobileNumber;
import nl.procura.validation.Bsn;

public class ContactgegevensService extends AbstractService {

  public static final String    EMAIL             = "email";
  public static final String    TEL_THUIS         = "tel_thuis";
  public static final String    TEL_WERK          = "tel_werk";
  public static final String    TEL_MOBIEL        = "tel_mobiel";
  public static final String    TEL_MOBIEL_BL     = "tel_mobiel_bl";
  private static final String[] ALL_CONTACT_TYPES = { EMAIL, TEL_MOBIEL, TEL_THUIS, TEL_WERK, TEL_MOBIEL_BL };

  private static final String AANGEVER = "Aangever";

  public ContactgegevensService() {
    super("Contactgegevens");
  }

  public Contact getCurrentContact() {
    BasePLExt pl = getServices().getPersonenWsService().getHuidige();
    return contactFromPl(pl);
  }

  public List<PlContactgegeven> getContactgegevens(BasePLExt pl) {
    if (pl != null) {
      return getContactgegevens(contactFromPl(pl));
    }

    return emptyList();
  }

  public MobilePhoneNumber getValidMobileNumber(List<PlContactgegeven> contactgegevens) {
    MobilePhoneNumber out = new MobilePhoneNumber();
    for (PlContactgegeven gegeven : contactgegevens) {
      if (fil(gegeven.getAant())) {
        if (gegeven.getContactgegeven().isGegeven(ContactgegevensService.TEL_MOBIEL)) {
          MobileNumber nr = DutchPhoneNumberParser.getMobileNumber(gegeven.getAant());
          if (nr.isValid()) {
            out.setType(ContactgegevensService.TEL_MOBIEL);
            out.setMobileNr(nr.getFormats().standard());
            out.setValid(true);
          }
        } else if (gegeven.getContactgegeven().isGegeven(ContactgegevensService.TEL_MOBIEL_BL)) {
          if (!out.isValid()) {
            out.setType(ContactgegevensService.TEL_MOBIEL_BL);
            out.setMobileNr(gegeven.getAant());
            out.setValid(true);
            out.setCountry(GbaTables.LAND.get(gegeven.getCountry()));
          }
        }
      }
    }
    return out;
  }

  @ThrowException("Fout bij het zoeken van contactgegevens")
  public List<PlContactgegeven> getContactgegevens(Contact contact) {
    List<PlContactgegeven> gegevens = new ArrayList<>(getContactgegevens(contact.getAnr(), contact.getBsn(), AANGEVER));
    contact.getEmailWithDate().ifPresent(email -> setCorrespondentieEmailadres(email, gegevens));

    return gegevens;
  }

  @ThrowException("Fout bij het zoeken van contactgegevens")
  public List<PlContactgegeven> getContactgegevens(DossierPersoon p) {
    List<PlContactgegeven> gegevens = new ArrayList<>();
    Bsn bsn = getBsn(p);
    if (bsn.isCorrect() && p != null) {
      gegevens.addAll(getContactgegevens(-1, bsn.getLongBsn(), p.getDossierPersoonType().getDescr()));
    }

    return gegevens;
  }

  @ThrowException("Fout bij het zoeken van contactgegevens")
  public List<PlContactgegeven> getContactgegevensAangever(Zaak zaak) {
    long anr = zaak.getAnummer().getLongValue();
    long bsn = zaak.getBurgerServiceNummer().getLongValue();
    return getContactgegeven(anr, bsn, AANGEVER);
  }

  @ThrowException("Fout bij het zoeken van contactgegevens")
  public List<PlContactgegeven> getContactgegevens(long anr, long bsn, String persoon) {

    List<PlContactgegeven> nl = new ArrayList<>();
    List<PlContactgegeven> l = getContactgegeven(anr, bsn, persoon);

    for (Pair<String, String> b : getAvailableAant()) {
      PlContactgegeven plc = new PlContactgegeven(anr, bsn, persoon, null,
          new Contactgegeven(-1, b.left, b.right), "", -1);
      for (PlContactgegeven a : l) {
        if (equalsIgnoreCase(b.left, a.getContactgegeven().getGegeven())) {
          plc = a;
        }
      }

      nl.add(plc);
    }

    nl.sort(new ContactgegevenSorter());

    return nl;
  }

  @ThrowException("Fout bij het zoeken van contactgegevens")
  public List<PlContactgegeven> getContactgegevens(Zaak zaak) {
    List<PlContactgegeven> gegevens = new ArrayList<>();
    if (zaak != null) {
      if (zaak.getBasisPersoon() != null) {
        gegevens.addAll(getContactgegevens(zaak.getBasisPersoon()));
      } else {
        long anr = along(zaak.getAnummer().getValue());
        long bsn = along(zaak.getBurgerServiceNummer().getValue());
        gegevens.addAll(getContactgegevens(anr, bsn, AANGEVER));
      }
    }

    return gegevens;
  }

  /**
   * Tweede variant op de contactgegevens
   */
  @ThrowException("Fout bij het zoeken van contactgegevens")
  public PlContactgegevens getContactgegevens2(Zaak zaak) {

    PlContactgegevens g = new PlContactgegevens();

    if (zaak != null) {

      if (zaak.getType().is(HUWELIJK_GPS_GEMEENTE, OMZETTING_GPS, ONTBINDING_GEMEENTE)) {

        DossierPartners zaakDossier = (DossierPartners) ((Dossier) zaak).getZaakDossier();

        DossierPersoon p1 = zaakDossier.getPartner1();
        DossierPersoon p2 = zaakDossier.getPartner2();

        if (getBsn(p1).isCorrect()) {
          String persoon = "Partner 1";
          List<PlContactgegeven> aantekeningen = getContactgegevens(-1L, getBsn(p1).getLongBsn(), persoon);
          g.getPersonen().add(new PlContactgegevenPersoon(persoon, aantekeningen));
        }

        if (getBsn(p2).isCorrect()) {
          String persoon = "Partner 2";
          List<PlContactgegeven> aantekeningen = getContactgegevens(-1L, getBsn(p2).getLongBsn(), persoon);
          g.getPersonen().add(new PlContactgegevenPersoon(persoon, aantekeningen));
        }
      } else {
        g.getPersonen().add(new PlContactgegevenPersoon(AANGEVER, getContactgegevens(zaak)));
      }
    }

    return g;
  }

  public String getContactWaarde(BasePLExt pl, String aant) {
    PlContactgegeven a = getContactgegeven(pl, aant);
    return (a != null) ? a.getAant() : "";
  }

  @ThrowException("Fout bij het zoeken van contactgegevens")
  public List<PlContactgegeven> getGevuldeContactgegevens(Zaak zaak) {
    List<PlContactgegeven> gegevens = new ArrayList<>();
    for (PlContactgegeven a : getContactgegevens(zaak)) {
      if (fil(a.getAant())) {
        gegevens.add(a);
      }
    }

    return gegevens;
  }

  public ContactVerplichtMate getMateVerplicht() {
    return ContactVerplichtMate.get(
        along(getServices().getParameterService().getParm(ParameterConstant.CONTACT_VERPLICHT)));
  }

  public boolean isVastGesteld(BasePLExt pl, boolean vandaag) {
    return isVastGesteld(pl, vandaag, ALL_CONTACT_TYPES);
  }

  @Transactional
  @ThrowException("Fout bij het opslaan van de contactgegevens")
  public void setAantekeningWaarde(PlContactgegeven g, String waarde, long country) {
    Aant3Dao.saveAant(g.getAnr(), g.getBsn(), g.getContactgegeven().getGegeven(), waarde, country);
    callListeners(ServiceEvent.CHANGE); // Waarschuw listeners
  }

  public void setContactWaarde(long anr, long bsn, String contactgegeven, String waarde, long country) {
    setAantekeningWaarde(getGegeven(anr, bsn, contactgegeven), waarde, country);
  }

  public void updateContactWaarden(Contact contact) {
    for (PlContactgegeven aant : getContactgegevens(contact)) {
      if (isNotBlank(aant.getAant())) {
        setAantekeningWaarde(aant, aant.getAant(), aant.getCountry());
      }
    }
  }

  private Contact contactFromPl(BasePLExt pl) {
    String name = pl.getPersoon().getNaam().getPredAdelVoorvGeslVoorn();
    long anr = along(pl.getPersoon().getAnr().getCode());
    long bsn = along(pl.getPersoon().getBsn().getCode());
    return getDiversenCorrespondentieEmail(pl)
        .map(email -> new Contact(name, anr, bsn, email))
        .orElse(new Contact(name, anr, bsn));
  }

  private List<Pair<String, String>> getAvailableAant() {

    List<Pair<String, String>> l = new ArrayList<>();

    l.add(new Pair<>(EMAIL, "E-mail adres"));
    l.add(new Pair<>(TEL_MOBIEL, "Telefoon (mobiel NL)"));
    l.add(new Pair<>(TEL_MOBIEL_BL, "Telefoon (mobiel buitenland)"));
    l.add(new Pair<>(TEL_THUIS, "Telefoon (thuis)"));
    l.add(new Pair<>(TEL_WERK, "Telefoon (werk)"));

    return l;
  }

  private String getAvailableAantOms(String aant) {

    for (Pair<String, String> a : getAvailableAant()) {
      if (a.left.equals(aant)) {
        return a.right;
      }
    }

    return "";
  }

  private Bsn getBsn(DossierPersoon p) {
    if (p != null && p.getBurgerServiceNummer() != null) {
      return new Bsn(p.getBurgerServiceNummer().getLongValue());
    }
    return new Bsn();
  }

  private PlContactgegeven getContactgegeven(BasePLExt pl, String aant) {

    for (PlContactgegeven c : getContactgegevens(pl)) {
      if (equalsIgnoreCase(c.getContactgegeven().getGegeven(), aant)) {
        return c;
      }
    }

    return null;
  }

  private List<PlContactgegeven> getContactgegeven(long anr, long bsn, String persoon) {

    List<PlContactgegeven> l = new ArrayList<>();

    for (Aant3 aant3 : Aant3Dao.findAant(anr, bsn)) {

      PlContactgegeven c = new PlContactgegeven();
      c.setAnr(aant3.getId().getAnr());
      c.setBsn(aant3.getId().getBsn());
      c.setAant(aant3.getAant());
      c.setCountry(aant3.getCountry().longValue());
      c.setDatum(new DateTime(aant3.getDIn()));
      c.setPersoon(persoon);

      Contactgegeven g = new Contactgegeven();
      g.setCode(aant3.getAantek3().getCAantek3());
      g.setGegeven(aant3.getAantek3().getAantek3());
      g.setOms(getAvailableAantOms(g.getGegeven()));
      c.setContactgegeven(g);

      l.add(c);
    }

    return l;
  }

  /**
   * Geeft het e-mailadres terug dat op de pl in categorie diversen staat
   */
  private Optional<Pair<String, DateTime>> getDiversenCorrespondentieEmail(BasePLExt pl) {

    BasePLRec record = pl.getLatestRec(GBACat.DIV);
    String datumIngang = record.getElemVal(GBAElem.INGANGSDAT_GELDIG).getVal();
    String waarde = record.getElemVal(GBAElem.DIV_CORR_ADRES_1).getVal();

    if (fil(waarde) && new GbaEmailValidator().isValid(waarde)) {
      return Optional.of(new Pair<>(waarde, new DateTime(datumIngang)));
    }

    return Optional.empty();
  }

  private PlContactgegeven getGegeven(long anr, long bsn, String contactgegeven) {
    PlContactgegeven g = new PlContactgegeven();
    g.setAnr(anr);
    g.setBsn(bsn);
    g.setContactgegeven(new Contactgegeven(-1, contactgegeven, ""));
    return g;
  }

  /**
   * Zijn de contactgegevens vandaag vastgesteld / geaccordeerd
   */
  private boolean isVastGesteld(BasePLExt pl, boolean vandaag, String... contactGegevens) {

    for (PlContactgegeven aant : getContactgegevens(pl)) {
      boolean isContactGegeven = aant.getContactgegeven().isGegeven(contactGegevens);

      if (isContactGegeven && fil(aant.getAant())) {
        if (!vandaag || (aant.getDatum().getLongDate() == along(getSystemDate()))) {
          return true;
        }
      }
    }

    return false;
  }

  public boolean isVastGesteld(Contact contact) {
    for (PlContactgegeven aant : getContactgegevens(contact)) {
      boolean isContactGegeven = aant.getContactgegeven().isGegeven(ALL_CONTACT_TYPES);
      if (isContactGegeven && isNotBlank(aant.getAant())) {
        return true;
      }
    }
    return false;
  }

  /**
   * Update het e-mailadres met dat uit de categorie diversen
   */
  private void setCorrespondentieEmailadres(Pair<String, DateTime> emailWithDate, List<PlContactgegeven> gegevens) {
    for (PlContactgegeven gegeven : gegevens) {
      if (EMAIL.equals(gegeven.getContactgegeven().getGegeven())) {
        DateTime dateProweb = gegeven.getDatum();
        if (dateProweb == null || (emailWithDate.right.getLongDate() > dateProweb.getLongDate())) {
          gegeven.setDatum(emailWithDate.right);
          gegeven.setAant(emailWithDate.left);
        }
      }
    }
  }

  private class ContactgegevenSorter implements Comparator<PlContactgegeven> {

    @Override
    public int compare(PlContactgegeven o1, PlContactgegeven o2) {
      return o1.getContactgegeven().getOms().compareTo(o2.getContactgegeven().getOms());
    }
  }
}
