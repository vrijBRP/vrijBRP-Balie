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

package nl.procura.gba.web.services.zaken.algemeen;

import static java.util.Comparator.comparingInt;
import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.*;
import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.diensten.gba.ple.openoffice.formats.Adresformats;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.GenericDao;
import nl.procura.gba.jpa.personen.dao.ZaakDao;
import nl.procura.gba.jpa.personen.db.ZaakId;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.ZaakDossier;
import nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakAttribuutType;
import nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakBehandelaar;
import nl.procura.gba.web.services.zaken.algemeen.commentaar.CommentaarZaak;
import nl.procura.gba.web.services.zaken.algemeen.commentaar.ZaakCommentaarType;
import nl.procura.gba.web.services.zaken.algemeen.identificatie.ZaakIdType;
import nl.procura.gba.web.services.zaken.algemeen.identificatie.ZaakIdentificatie;
import nl.procura.gba.web.services.zaken.documenten.aanvragen.DocumentZaak;
import nl.procura.gba.web.services.zaken.verhuizing.FunctieAdres;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraag;
import nl.procura.standard.ProcuraDate;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

public class ZaakUtils {

  private final static Logger LOGGER = LoggerFactory.getLogger(ZaakUtils.class.getName());

  public static final String PROWEB_PERSONEN = "PROWEB Personen";
  public static final String PROCURA         = "PROCURA";

  public static void check(Zaak zaak) {

    String zaakDescr = getZaakTypeEnId(zaak);

    if (emp(zaak.getZaakId())) {
      LOGGER.error(String.format("%s - zaak-id is leeg", zaakDescr));
    }

    if (emp(zaak.getBron())) {
      LOGGER.error(zaakDescr + " - bron is leeg");
    }

    if (emp(zaak.getLeverancier())) {
      LOGGER.error(zaakDescr + " - leverancier is leeg");
    }

    if (zaak.getBurgerServiceNummer() == null || !zaak.getBurgerServiceNummer().isCorrect()) {
      LOGGER.error(zaakDescr + " - BSN is leeg");
    }

    if (zaak.getDatumIngang() == null || !pos(zaak.getDatumIngang().getLongDate())) {
      LOGGER.error(zaakDescr + " - datum ingang is leeg");
    }

    if (zaak.getIngevoerdDoor() == null || !pos(zaak.getIngevoerdDoor().getValue())) {
      LOGGER.error(zaakDescr + " - ingevoerd door is leeg");
    }
  }

  /**
   * Format het gegenereerde id met het zaak-id
   */
  public static String generateId(ZaakType zaakType) {
    String uid = pad_left(astr(zaakType.getCode()), "0", 4) + getRandomId(6);
    return uid.substring(0, 4) +
        "-" +
        uid.substring(4, 7) +
        "-" +
        uid.substring(7);
  }

  public static Optional<ZaakBehandelaar> getBehandelaar(Zaak zaak) {
    return zaak.getZaakHistorie().getBehandelaarHistorie().getBehandelaar();
  }

  public static boolean isProbleemBijVerwerking(Zaak zaak) {
    return !zaak.getStatus().isEindStatus()
        && zaak.getZaakHistorie().getAttribuutHistorie()
            .is(ZaakAttribuutType.FOUT_BIJ_VERWERKING);
  }

  public static boolean isWachtOpRisicoAnalyse(Zaak zaak) {
    return !zaak.getStatus().isEindStatus()
        && zaak.getZaakHistorie().getAttribuutHistorie()
            .is(ZaakAttribuutType.WACHT_OP_RISICOANALYSE);
  }

  public static ZaakCommentaarType getCommentaarIcon(Zaak zaak) {
    if (zaak instanceof CommentaarZaak) {
      return ((CommentaarZaak) zaak).getCommentaren().getCommentaar().getType();
    } else {
      return null;
    }
  }

  public static String getDatumEnDagenTekst(String datum) {
    return trim(String.format("%s (%s)", date2str(datum), getDagenTekst(datum)));
  }

  public static String getDagenTekst(String datum) {
    if (pos(datum)) {
      int dagen = new ProcuraDate().diffInDays(datum);
      switch (dagen) {
        case -1:
          return setClass(false, "gisteren");
        case 0:
          return "vandaag";
        case 1:
          return "morgen";
        case 2:
          return "overmorgen";
        default:
          if (dagen < 0) {
            return setClass(false, -dagen + " dagen geleden");
          }

          return "over " + dagen + " dagen";
      }
    }

    return "";
  }

  public static String getIngevoerdDoorGebruiker(Zaak zaak) {
    if (pos(zaak.getIngevoerdDoor().getValue())) {
      return zaak.getIngevoerdDoor().getDescription();
    }
    return "Onbekend";
  }

  /**
   * Geeft een uitgebreide zaakomschrijving terug
   */
  public static String getOmschrijving(Zaak zaak) {

    StringBuilder oms = new StringBuilder();
    if (zaak != null && fil(zaak.getSoort())) {
      StringBuilder soort = new StringBuilder();
      soort.append(zaak.getSoort());

      if (zaak.getType() == ZaakType.VERHUIZING) {
        VerhuisAanvraag v = (VerhuisAanvraag) zaak;
        soort.append(", ");

        switch (v.getTypeVerhuizing()) {
          case BINNENGEMEENTELIJK:
          case INTERGEMEENTELIJK:
          case HERVESTIGING:
            Adresformats verhuisAdres = v.getNieuwAdres().getAdres();
            if (v.getNieuwAdres().getFunctieAdres() == FunctieAdres.BRIEFADRES) {
              soort.append(" briefadres: ");
            }
            soort.append(verhuisAdres.getAdres() + " " + verhuisAdres.getPc_wpl());
            break;

          case EMIGRATIE:
            String emigratieAdres = v.getEmigratie().getAdres();
            soort.append(emigratieAdres);
            break;

          default:
            break;
        }
      }

      oms.append(trim(soort.toString()));
    }

    return oms.toString();
  }

  /**
   * Geeft een uitgebreide zaakomschrijving terug
   */
  public static String getTypeEnOmschrijving(Zaak zaak) {

    StringBuilder oms = new StringBuilder();
    if (zaak != null) {
      if (zaak.getType() == ZaakType.UITTREKSEL) {
        DocumentZaak u = (DocumentZaak) zaak;
        oms.append(trim(u.getDoc().getDocumentType().getOms()));
      } else {
        oms.append(zaak.getType().toString());
      }

      if (fil(zaak.getSoort())) {
        oms.append(" (");
        oms.append(getOmschrijving(zaak));
        oms.append(")");
      }
    }

    return oms.toString();
  }

  /**
   * Genereer een random id
   */
  public static String getRandomId(int length) {

    String alphabet = "0123456789abcdefghijklmnopqrstuvwxyz";
    StringBuilder result = new StringBuilder();
    SecureRandom random = new SecureRandom();

    for (int i = 0; i < length; i++) {
      result.append(alphabet.charAt(random.nextInt(alphabet.length())));
    }

    return result.toString();
  }

  /**
   * Geeft zaak-id terug waarbij het externe zaak-id het voornaamste is
   */
  public static String getRelevantZaakId(Zaak zaak) {
    List<ZaakIdentificatie> nummers = new ArrayList<>();
    if (zaak.getZaakHistorie() != null) {
      nummers.addAll(zaak.getZaakHistorie().getIdentificaties().getNummers());
      nummers.sort(comparingInt(z -> z.getZaakIdType().getOrder()));
    }
    return nummers.stream()
        .map(ZaakId::getExternId)
        .findFirst()
        .orElse(zaak.getZaakId());
  }

  public static String getZaaksysteemId(Zaak zaak) {
    List<ZaakIdentificatie> nummers = zaak.getZaakHistorie().getIdentificaties().getNummers();
    return nummers.stream()
        .filter(z -> ZaakIdType.ZAAKSYSTEEM == z.getZaakIdType())
        .findFirst()
        .map(ZaakId::getExternId)
        .orElseThrow(() -> new ProException(WARNING, "Deze zaak heeft geen identificatie van het type 'ZAAKSYSTEEM'"));
  }

  public static String getStatus(ZaakStatusType status) {
    String s = status.getOms();
    switch (status) {
      case INCOMPLEET:
        return setClass("red", s);
      case VERWERKT:
      case VERWERKT_IN_GBA:
        return setClass("green", s);
      default:
        return s;
    }
  }

  public static <T extends Zaak> T newZaak(T zaak, Services services) {

    if (!GenericDao.isSaved(zaak) && emp(zaak.getZaakId())) {
      if (StringUtils.isBlank(zaak.getBron())) {
        zaak.setBron(PROWEB_PERSONEN);
      }
      if (StringUtils.isBlank(zaak.getLeverancier())) {
        zaak.setLeverancier(PROCURA);
      }
      zaak.setZaakId(getNewZaakId(zaak));
      zaak.setDatumTijdInvoer(new DateTime());

      Gebruiker gebruiker = services.getGebruiker();
      Locatie locatie = Locatie.getDefault();

      // Gebruiker & locatie toevoegen
      if (gebruiker != null && pos(gebruiker.getCUsr())) {
        zaak.setIngevoerdDoor(new UsrFieldValue(gebruiker));

        Locatie gebruikerLocatie = gebruiker.getLocatie();
        if (gebruikerLocatie != null && pos(gebruikerLocatie.getCLocation())) {
          locatie = gebruikerLocatie;
        }
      }

      zaak.setLocatieInvoer(locatie);

      if (zaak instanceof ZaakAfhaalbaar) {
        ((ZaakAfhaalbaar) zaak).setLocatieAfhaal(Locatie.getDefault());
      }
    }

    return zaak;
  }

  public static <T extends ZaakDossier> T newZaakDossier(Dossier zaak, Services services) {
    return (T) newZaak(zaak, services).getZaakDossier();
  }

  public static String getNummer(IdentificatieNummers nummers) {
    String nummer = "";
    BsnFieldValue bsn = nummers.getBurgerServiceNummer();
    AnrFieldValue anummer = nummers.getAnummer();
    if (bsn.isCorrect()) {
      nummer = bsn.getStringValue();
    } else if (anummer.isCorrect()) {
      nummer = anummer.getStringValue();
    }
    return nummer;
  }

  /**
   * Genereer een nieuw Zaak-Id
   */
  private static String getNewZaakId(Zaak zaak) {
    String id = generateId(zaak.getType());
    if (ZaakDao.isExistingZaakId(zaak, id)) {
      return getNewZaakId(zaak);
    }

    return id;
  }

  /**
   * Geef een zaakOmschrijving terug
   */
  private static String getZaakTypeEnId(Zaak zaak) {
    StringBuilder sb = new StringBuilder(zaak.getType().toString());
    if (fil(zaak.getZaakId())) {
      sb.append(": " + zaak.getZaakId());
    }

    return sb.toString();
  }
}
