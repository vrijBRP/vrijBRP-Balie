/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.services.zaken.inhoudingen;

import static java.lang.String.format;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.AAND_BEZIT_BUITENL_REISDOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.AAND_INH_VERMIS_NL_REISDOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.AUTORIT_VAN_AFGIFTE_NL_REISDOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_EINDE_GELDIG_NL_REISDOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_INH_VERMIS_NL_REISDOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_UITGIFTE_NL_REISDOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.NR_NL_REISDOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.SIG_MET_BETREK_TOT_VERSTREK_NL_REISDOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.SOORT_NL_REISDOC;
import static nl.procura.burgerzaken.vrsclient.api.VrsDocumentStatusType.GELDIG;
import static nl.procura.burgerzaken.vrsclient.api.VrsDocumentStatusType.ONGELDIG;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.commons.core.exceptions.ProExceptionType.SELECT;
import static nl.procura.gba.common.MiscUtils.copyList;
import static nl.procura.gba.common.MiscUtils.to;
import static nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoonType.AANGEVER;
import static nl.procura.gba.web.services.zaken.inhoudingen.InhoudingType.NIET_INGEHOUDEN;
import static nl.procura.gba.web.services.zaken.inhoudingen.InhoudingType.VAN_RECHTSWEGE_VERVALLEN;
import static nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentType.VLUCHTELINGEN_PASPOORT;
import static nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentType.VREEMDELINGEN_PASPOORT;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.equalsIgnoreCase;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.pos;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.vrsclient.api.VrsDocumentStatusType;
import nl.procura.burgerzaken.vrsclient.model.RegistratieMeldingReisdocumentResponse;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.ReisdInhDao;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.web.common.misc.ZakenList;
import nl.procura.gba.web.services.ServiceEvent;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Timer;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.beheer.vrs.VrsService;
import nl.procura.gba.web.services.zaken.algemeen.AbstractZaakContactService;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakService;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContact;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoon;
import nl.procura.gba.web.services.zaken.reisdocumenten.Reisdocument;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentAanvraag;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentService;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentType;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentTypeExclusions;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentVermissing;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

public class DocumentInhoudingenService extends AbstractZaakContactService<DocumentInhouding>
    implements ZaakService<DocumentInhouding> {

  public DocumentInhoudingenService() {
    super("Documentinhoudingen", ZaakType.INHOUD_VERMIS);
  }

  /**
   * Voeg een kassaproduct toe
   */
  @Transactional
  @ThrowException("Fout bij het toevoegen van het kassaproduct")
  public void addVermissingKassaProduct(Zaak zaak) {
    getServices().getKassaService().addToWinkelwagen(zaak);
    callListeners(ServiceEvent.CHANGE);
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van de documentinhouding")
  public int getZakenCount(ZaakArgumenten zaakArgumenten) {
    return ReisdInhDao.findCount(getArgumentenToMap(zaakArgumenten));
  }

  public List<Reisdocument> getActueelReisdocumentHistorie(BasePLExt pl) {
    List<Reisdocument> documenten = new ArrayList<>();
    for (Reisdocument document : getReisdocumentHistorie(pl)) {
      if (!isReisdocumentIngehouden(pl, document)) {
        documenten.add(document);
      }
    }

    return documenten;
  }

  @Override
  public DocumentInhouding setVolledigeZaakExtra(DocumentInhouding zaak) {
    DocumentInhouding impl = to(zaak, DocumentInhouding.class);
    BasePLExt pl = findPL(impl.getAnummer(), impl.getBurgerServiceNummer());
    impl.setAutoriteit(getAutoriteit(pl, impl.getNummerDocument()));
    return super.setVolledigeZaakExtra(zaak);
  }

  @Override
  public ZaakContact getContact(DocumentInhouding zaak) {

    ZaakContact zaakContact = new ZaakContact();
    BasePLExt basisPersoon = getBasisPersoon(zaak);

    if (basisPersoon != null) {
      ZaakContactpersoon persoon = new ZaakContactpersoon(AANGEVER, basisPersoon);
      persoon.setContactgegevens(getServices().getContactgegevensService().getContactgegevens(zaak));
      zaakContact.add(persoon);
    }

    return zaakContact;
  }

  public DocumentInhouding getInhouding(BasePLExt pl, Reisdocument rd) {
    return getInhouding(pl, rd.getNummerDocument().getVal());
  }

  public DocumentInhouding getInhouding(BasePLExt pl, String nummerDocument) {
    ZaakArgumenten zaakArgumenten = new ZaakArgumenten(pl).addZaakKey(new ZaakKey(nummerDocument));
    for (Zaak zaak : getMinimalZaken(zaakArgumenten)) {
      DocumentInhouding inh = (DocumentInhouding) zaak;
      if (equalsIgnoreCase(nummerDocument, inh.getNummerDocument())) {
        return getCompleteZaak(inh);
      }
    }

    return null;
  }

  public List<DocumentInhouding> getInhoudingenVanDeZaak(ReisdocumentAanvraag aanvraag, BasePLExt pl) {

    ZaakArgumenten z = new ZaakArgumenten(pl);
    z.setTypen(ZaakType.INHOUD_VERMIS);
    z.setdInvoerVanaf(aanvraag.getDatumTijdInvoer().getLongDate());
    z.setdInvoerTm(aanvraag.getDatumTijdInvoer().getLongDate());
    z.setMax(10);

    List<DocumentInhouding> list = new ArrayList<>();
    for (Zaak zaak : getMinimalZaken(z)) {
      list.add(getStandardZaak((DocumentInhouding) zaak));
    }
    return list;
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van de documentinhouding")
  public List<DocumentInhouding> getMinimalZaken(ZaakArgumenten zaakArgumenten) {
    return new ZakenList(copyList(ReisdInhDao.find(getArgumentenToMap(zaakArgumenten)), DocumentInhouding.class));
  }

  @Override
  public Zaak getNewZaak() {
    return aanvullenZaak(new DocumentInhouding());
  }

  public Reisdocument getReisdocument(BasePLExt pl, String nummer) {
    for (Reisdocument reisdocument : getReisdocumentHistorie(pl)) {
      if (equalsIgnoreCase(reisdocument.getNummerDocument().getVal(), nummer)) {
        return reisdocument;
      }
    }

    return null;
  }

  public List<Reisdocument> getReisdocumentHistorie(BasePLExt pl) {
    List<Reisdocument> documenten = new ArrayList<>();

    for (BasePLSet set : pl.getCat(GBACat.REISDOC).getSets()) {
      BasePLRec record = set.getLatestRec();

      Reisdocument doc = new Reisdocument();
      doc.setAanduidingInhoudingVermissing(
          record.getElemVal(AAND_INH_VERMIS_NL_REISDOC));
      doc.setAutoriteitVanAfgifte(record.getElemVal(AUTORIT_VAN_AFGIFTE_NL_REISDOC));
      doc.setBuitenlandsReisdocument(record.getElemVal(AAND_BEZIT_BUITENL_REISDOC));
      doc.setDatumEindeGeldigheid(record.getElemVal(DATUM_EINDE_GELDIG_NL_REISDOC));
      doc.setDatumInhoudingVermissing(
          record.getElemVal(DATUM_INH_VERMIS_NL_REISDOC));
      doc.setDatumUitgifte(record.getElemVal(DATUM_UITGIFTE_NL_REISDOC));
      doc.setNederlandsReisdocument(record.getElemVal(SOORT_NL_REISDOC));
      doc.setNummerDocument(record.getElemVal(NR_NL_REISDOC));
      doc.setSignalering(record.getElemVal(SIG_MET_BETREK_TOT_VERSTREK_NL_REISDOC));

      if (doc.isTonen()) {
        documenten.add(doc);
      }
    }

    return documenten;
  }

  /**
   * Maak een vermissingsobject op basis van een zaak
   */
  public ReisdocumentVermissing getVermissing(DocumentInhouding zaak) {
    ReisdocumentVermissing vermissing = new ReisdocumentVermissing();
    vermissing.setInhouding(zaak);
    vermissing.setReisdocument(getReisdocument(zaak.getBasisPersoon(), zaak.getNummerDocument()));
    vermissing.setBasisregister(getBasisregister(zaak.getBasisPersoon()));
    return vermissing;
  }

  private DocumentInhoudingBasisregister getBasisregister(BasePLExt pl) {
    return getServices().getReisdocumentService().getVrsService().getBasisregister(pl);
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van zaak-ids")
  public List<ZaakKey> getZaakKeys(ZaakArgumenten zaakArgumenten) {
    return ReisdInhDao.findZaakKeys(getArgumentenToMap(zaakArgumenten));
  }

  /**
   * Heeft de persoon nog een document met het meegegeven type dat nog niet ingeleverd is en niet te laat is.
   */
  public ReisdocumentType getInTeleverenDocument(BasePLExt pl, ReisdocumentType reisdocumentType) {
    VrsService vrsService = getServices().getReisdocumentService().getVrsService();
    List<ReisdocumentType> types = new ArrayList<>();
    if (vrsService.isRegistratieMeldingEnabled()) {
      // Basisregister
      types.addAll(vrsService.getReisdocumentenLijst(pl, null)
          .stream()
          .filter(reisdoc -> {
            String statusCode = reisdoc.getStatusMeestRecent().getDocumentstatusCode();
            return VrsDocumentStatusType.getByCode(statusCode).in(GELDIG, ONGELDIG);
          })
          .map(reisdoc -> ReisdocumentType.get(reisdoc.getDocumentsoort().getCode()))
          .collect(Collectors.toList()));

    } else {
      // BRP
      for (Reisdocument doc : getReisdocumentHistorie(pl)) {
        if (doc.getNederlandsReisdocument() != null) {
          ReisdocumentType existingDocument = ReisdocumentType.get(doc.getNederlandsReisdocument().getVal());
          if (!isReisdocumentIngehouden(pl, doc) && !isTeLaat(doc)) {
            types.add(existingDocument);
          }
        }
      }
    }

    for (ReisdocumentType doc : types) {
      if (ReisdocumentTypeExclusions.exclude(reisdocumentType, doc)) {
        return doc;
      }
      if (pl.getNatio().isNederlander() && doc.isDocument(VREEMDELINGEN_PASPOORT, VLUCHTELINGEN_PASPOORT)) {
        return doc;
      }
    }

    return null;
  }

  public long getAantalInTeLeverenDocumenten(BasePLExt pl) {
    VrsService vrsService = getServices().getReisdocumentService().getVrsService();
    if (vrsService.isRegistratieMeldingEnabled()) {
      return (int) vrsService.getReisdocumentenLijst(pl, null)
          .stream()
          .map(doc -> doc.getStatusMeestRecent().getDocumentstatusCode())
          .filter(code -> VrsDocumentStatusType.getByCode(code) == ONGELDIG).count();
    }
    return getReisdocumentHistorie(pl).stream()
        .filter(document -> moetNogInleveren(pl, document))
        .count();
  }

  public boolean isReisdocumentIngehouden(BasePLExt pl, Reisdocument rd) {
    String inhType = getInhoudingsType(pl, rd);
    return fil(inhType) && !InhoudingType.get(inhType).isVanRechtswegeVervallen();

  }

  public boolean isRijbewijsIngehouden(BasePLExt pl, String rdwNummer) {
    return getInhouding(pl, rdwNummer) != null;
  }

  public boolean moetNogInleveren(BasePLExt pl) {
    return getAantalInTeLeverenDocumenten(pl) > 0;
  }

  @Override
  @Transactional
  @ThrowException("Fout bij het opslaan van de reisdocument aanvraag")
  public void save(DocumentInhouding zaak) {
    getZaakStatussen().setInitieleStatus(zaak);
    opslaanStandaardZaak(zaak);
    callListeners(ServiceEvent.CHANGE);
  }

  /**
   * Voegt een rijbewijsvermissing toe
   */
  public DocumentInhouding setRijbewijsInhouding(BasePLExt pl, String dIn, String rdwNummer, String pvNummer,
      InhoudingType type) {

    if (!isRijbewijsIngehouden(pl, rdwNummer)) {

      DocumentInhouding inh = (DocumentInhouding) getNewZaak();
      inh.setBasisPersoon(pl);
      inh.setBurgerServiceNummer(new BsnFieldValue(pl.getPersoon().getBsn().getVal()));
      inh.setAnummer(new AnrFieldValue(pl.getPersoon().getAnr().getVal()));
      inh.setInhoudingType(type);
      inh.setNummerDocument(rdwNummer);
      inh.setDocumentType(ReisdocumentType.ONBEKEND);
      inh.setDatumTijdInvoer(new DateTime());
      inh.setDatumIngang(new DateTime(along(dIn)));
      inh.setProcesVerbaalNummer(pvNummer);
      inh.setProcesVerbaalOms("");
      inh.setSprakeVanRijbewijs(true);

      // Een ID opvragen in het Zaak-DMS
      getServices().getZaakIdentificatieService().getDmsZaakId(inh);

      save(inh);

      addVermissingKassaProduct(inh);
      callListeners(ServiceEvent.CHANGE);

      return inh;
    }

    return null;
  }

  /**
   * Voegt een reisdocument inhouding toe
   */
  public DocumentInhouding createNewReisdocumentInhouding(BasePLExt pl, Reisdocument rd, InhoudingType type) {

    DocumentInhouding inh = null;

    // Verwijder bestaande 'rechtswege zaak' eerst bij upgrade naar inhouding/vermissing.
    DocumentInhouding inhouding = getInhouding(pl, rd);
    if (inhouding != null) {
      InhoudingType inhoudingType = inhouding.getInhoudingType();
      if (inhoudingType.isVanRechtswegeVervallen()) {
        inh = inhouding;
      } else {
        throw new ProException(SELECT, WARNING, "Dit document is al ingehouden.");
      }
    }

    if (inh != null) {
      String opmerking = format("Type gewijzigd van '%s' naar '%s'", inh.getInhoudingType().getOms(),
          type.getOms());
      getZaakStatussen().updateStatus(inh, ZaakStatusType.OPGENOMEN, opmerking);

    } else {
      inh = (DocumentInhouding) getNewZaak();
      inh.setBasisPersoon(pl);

      inh.setBurgerServiceNummer(new BsnFieldValue(pl.getPersoon().getBsn().getVal()));
      inh.setAnummer(new AnrFieldValue(pl.getPersoon().getAnr().getVal()));

      inh.setNummerDocument(rd.getNummerDocument().getVal());
      inh.setDocumentType(ReisdocumentType.get(rd.getNederlandsReisdocument().getVal()));

      inh.setDatumTijdInvoer(new DateTime());
      inh.setDatumIngang(new DateTime());
      inh.setProcesVerbaalNummer("");
      inh.setProcesVerbaalOms("");
      inh.setSprakeVanRijbewijs(false);

      // Een ID opvragen in het Zaak-DMS
      getServices().getZaakIdentificatieService().getDmsZaakId(inh);
    }

    inh.setInhoudingType(type);

    return inh;
  }

  /**
   * Voegt een reisdocument inhouding toe
   */
  public DocumentInhouding setReisdocumentInhouding(BasePLExt pl, Reisdocument rd, InhoudingType type) {

    DocumentInhouding inh = createNewReisdocumentInhouding(pl, rd, type);
    save(inh);
    addVermissingKassaProduct(inh);
    callListeners(ServiceEvent.CHANGE);

    return inh;
  }

  /**
   * Verwijderen van de geboorteaanvraag
   */
  @Override
  @Transactional
  @ThrowException("Fout bij het verwijderen")
  public void delete(DocumentInhouding zaak) {

    if (fil(zaak.getNummerDocument())) {

      ConditionalMap map = new ConditionalMap();
      map.put(ReisdInhDao.ZAAK_ID, zaak.getNummerDocument());
      map.put(ReisdInhDao.D_IN, zaak.getDatumTijdInvoer().getLongDate());
      map.put(ReisdInhDao.T_IN, zaak.getDatumTijdInvoer().getLongTime());
      map.put(ReisdInhDao.MAX_CORRECT_RESULTS, 1);

      removeEntities(ReisdInhDao.find(map));

      deleteZaakRelaties(zaak);
    }

    callListeners(ServiceEvent.CHANGE);
  }

  @Transactional
  public void registreerMelding(DocumentInhouding zaak) {
    ReisdocumentService reisdocumentService = getServices().getReisdocumentService();
    RegistratieMeldingReisdocumentResponse response = reisdocumentService.getVrsService().registreerMelding(zaak);
    DateTime localDateTime = DateTime.of(response.getMeldingdatum().toLocalDateTime());
    zaak.setVrsDIn(localDateTime.getLongDate());
    zaak.setVrsTIn(localDateTime.getLongTime());
    save(zaak);
  }

  private ConditionalMap getArgumentenToMap(ZaakArgumenten zaakArgumenten) {

    ZaakArgumenten nZaakArgumenten = new ZaakArgumenten(zaakArgumenten);

    // Nodig omdat de PROBEV kijkt naar status 1 (in behandeling). De zaken zijn echter al wel verwerkt.
    // Dit kan weg als de verwerking via de BSM verloopt.

    if (!nZaakArgumenten.getStatussen().isEmpty()) {
      boolean isBehandelingWel = nZaakArgumenten.getStatussen().contains(ZaakStatusType.INBEHANDELING);
      boolean isVerwerktWel = nZaakArgumenten.getStatussen().contains(ZaakStatusType.VERWERKT);
      boolean isBehandelingNiet = nZaakArgumenten.getNegeerStatussen().contains(ZaakStatusType.INBEHANDELING);
      boolean isVerwerktNiet = nZaakArgumenten.getNegeerStatussen().contains(ZaakStatusType.VERWERKT);

      if (isBehandelingWel) { // Bij behandeling niets teruggeven
        nZaakArgumenten.getStatussen().remove(ZaakStatusType.INBEHANDELING);
        nZaakArgumenten.getStatussen().add(ZaakStatusType.ONBEKEND);
      }

      if (isBehandelingNiet) { // Bij behandeling niets teruggeven
        nZaakArgumenten.getNegeerStatussen().remove(ZaakStatusType.INBEHANDELING);
        nZaakArgumenten.getNegeerStatussen().add(ZaakStatusType.ONBEKEND);
      }

      if (isVerwerktWel) {
        nZaakArgumenten.getStatussen().add(ZaakStatusType.INBEHANDELING);
      }

      if (isVerwerktNiet) {
        nZaakArgumenten.getNegeerStatussen().add(ZaakStatusType.INBEHANDELING);
      }
    }

    return getAlgemeneArgumentenToMap(nZaakArgumenten);
  }

  private String getAutoriteit(BasePLExt pl, String reisdocumentNummer) {
    return pl.getReisdoc().getAutoriteit(reisdocumentNummer);
  }

  /**
   * Geeft aanduidingInhoudingVermissing terug Het type in de zaak overrruled het type op de persoonslijst
   */
  private String getInhoudingsType(BasePLExt pl, Reisdocument rd) {
    // Kijken op pl
    InhoudingType inhType = InhoudingType.get(rd.getAanduidingInhoudingVermissing().getVal());
    if (inhType.is(NIET_INGEHOUDEN, VAN_RECHTSWEGE_VERVALLEN)) {
      // Kijken in zaak
      DocumentInhouding inhouding = getInhouding(pl, rd);
      if (inhouding != null) {
        inhType = inhouding.getInhoudingType();
      }
    }

    return inhType.getCode();
  }

  /**
   * Is het document verlopen?
   */
  private boolean isTeLaat(Reisdocument rd) {
    String dEnd = rd.getDatumEindeGeldigheid().getVal();
    return pos(dEnd) && (new ProcuraDate().diffInDays(dEnd) <= 0);
  }

  /**
   * Is het betreffende reisdocument van rechtswege vervallen
   */
  private boolean isVanRechtswegeVervallen(BasePLExt pl, Reisdocument rd) {
    String inhType = getInhoudingsType(pl, rd);
    return InhoudingType.get(inhType).isVanRechtswegeVervallen();
  }

  /**
   * Moet het reisdocument nog worden ingeleverd. Een van rechtswege inhouding geldt als nog-niet-ingehouden
   */
  private boolean moetNogInleveren(BasePLExt pl, Reisdocument rd) {

    boolean ingehouden = isReisdocumentIngehouden(pl, rd);
    boolean teLaat = isTeLaat(rd);
    boolean nogVanRechtswegeWordenIngehouden = isVanRechtswegeVervallen(pl, rd);

    return !ingehouden && (teLaat || nogVanRechtswegeWordenIngehouden);
  }
}
