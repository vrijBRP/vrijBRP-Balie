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

package nl.procura.gba.web.services.zaken.reisdocumenten;

import static nl.procura.burgerzaken.gba.core.enums.GBAElem.AAND_BEZIT_BUITENL_REISDOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.AAND_INH_VERMIS_NL_REISDOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.AUTORIT_VAN_AFGIFTE_NL_REISDOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_EINDE_GELDIG_NL_REISDOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_INH_VERMIS_NL_REISDOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_UITGIFTE_NL_REISDOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.NR_NL_REISDOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.SIG_MET_BETREK_TOT_VERSTREK_NL_REISDOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.SOORT_NL_REISDOC;
import static nl.procura.gba.common.MiscUtils.copy;
import static nl.procura.gba.common.MiscUtils.copyList;
import static nl.procura.gba.common.MiscUtils.to;
import static nl.procura.gba.common.ZaakStatusType.VERWERKT;
import static nl.procura.gba.common.ZaakStatusType.VERWERKT_IN_GBA;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.REISD_WIJZIGING_GEZAG;
import static nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoonType.AANGEVER;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.pos;
import static nl.procura.standard.Globalfunctions.toNul;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.burgerzaken.vrsclient.api.VrsRequest;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.openoffice.DocumentPL;
import nl.procura.diensten.gba.ple.openoffice.formats.Naamformats;
import nl.procura.dto.raas.aanvraag.DocAanvraagDto;
import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.Rdm01Dao;
import nl.procura.gba.jpa.personen.dao.ReisdocDao;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.jpa.personen.db.Rdm01;
import nl.procura.gba.jpa.personen.db.Reisdoc;
import nl.procura.gba.jpa.personen.db.Usr;
import nl.procura.gba.web.common.misc.ZakenList;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.services.ServiceEvent;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Timer;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.beheer.raas.AfsluitRequest;
import nl.procura.gba.web.services.beheer.raas.RaasService;
import nl.procura.gba.web.services.beheer.requestinbox.zaken.reisdocument.InboxReisdocumentProcessor;
import nl.procura.gba.web.services.beheer.requestinbox.zaken.reisdocument.InboxReisdocumentProcessor.ReisdocumentInboxData;
import nl.procura.gba.web.services.beheer.vrs.VrsService;
import nl.procura.gba.web.services.zaken.algemeen.AbstractZaakContactService;
import nl.procura.gba.web.services.zaken.algemeen.ControleerbareService;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakService;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContact;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoon;
import nl.procura.gba.web.services.zaken.algemeen.controle.Controles;
import nl.procura.gba.web.services.zaken.algemeen.controle.ControlesListener;
import nl.procura.gba.web.services.zaken.identiteit.Identificatie;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhoudingBasisregister;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhoudingenService;
import nl.procura.gba.web.services.zaken.reisdocumenten.clausule.Clausules;
import nl.procura.raas.rest.domain.aanvraag.AfsluitingStatusType;
import nl.procura.raas.rest.domain.aanvraag.FindAanvraagRequest;
import nl.procura.raas.rest.domain.aanvraag.LeveringStatusType;
import nl.procura.standard.ProcuraDate;
import nl.procura.validation.Bsn;

public class ReisdocumentService extends AbstractZaakContactService<ReisdocumentAanvraag>
    implements ZaakService<ReisdocumentAanvraag>, ControleerbareService, IdentificatieBijUitreikingService {

  public ReisdocumentService() {
    super("Reisdocumenten", ZaakType.REISDOCUMENT);
  }

  public VrsService getVrsService() {
    return new VrsService(getServices().getParameterService());
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van de reisdocumenten")
  public int getZakenCount(ZaakArgumenten zaakArgumenten) {
    return Rdm01Dao.findCount(getArgumentenToMap(zaakArgumenten));
  }

  @Override
  public ReisdocumentAanvraag setVolledigeZaakExtra(ReisdocumentAanvraag zaak) {

    ReisdocumentAanvraag impl = to(zaak, ReisdocumentAanvraag.class);
    BasePLExt pl = findPL(impl.getAnummer(), impl.getBurgerServiceNummer());

    if (impl.getClausules().getPartner() == null) {

      BasePLRec actueelRecord = pl.getHuwelijk().getActueelOfMutatieRecord();

      String partnerAnr = pl.getHuwelijk().getAnr().getCode();
      String partnerBsn = pl.getHuwelijk().getBsn().getCode();
      String gesl = actueelRecord.getElemVal(GBAElem.GESLACHTSNAAM).getDescr();
      String voorn = actueelRecord.getElemVal(GBAElem.VOORNAMEN).getDescr();
      String voorv = actueelRecord.getElemVal(GBAElem.VOORV_GESLACHTSNAAM).getDescr();
      String titel = actueelRecord.getElemVal(GBAElem.TITEL_PREDIKAAT).getDescr();

      Clausules clausulesImpl = impl.getClausules();

      if (fil(gesl)) {
        if (pos(partnerAnr) || pos(partnerBsn)) {
          clausulesImpl.setPartner(new DocumentPL(getPersoonslijst(partnerAnr, partnerBsn)));
        }

        if (clausulesImpl.getPartner() == null) {
          clausulesImpl.setPartner(new DocumentPL(new BasePLExt()));
        }

        clausulesImpl.getPartner()
            .getPersoon()
            .getFormats()
            .setNaam(new Naamformats(voorn, gesl, voorv, titel, "E", null));
      }
    }

    DocumentInhoudingenService inhoudingen = getServices().getDocumentInhoudingenService();
    impl.setInhoudingen(inhoudingen.getInhoudingenVanDeZaak(impl, findPL(impl.getAnummer(), impl.getBurgerServiceNummer())));
    impl.setDocumentHistorie(inhoudingen.getActueelReisdocumentHistorie(pl));
    impl.setBasisregister(getBasisregister(pl));

    return super.setVolledigeZaakExtra(zaak);
  }

  private DocumentInhoudingBasisregister getBasisregister(BasePLExt pl) {
    return getServices().getReisdocumentService().getVrsService().getBasisregister(pl);
  }

  /**
   * Geef aanvraagnummer terug op basis van het documentNummer
   */
  public String getBackOfficeAanvraag(String documentNummer) {
    String sql = String.format("select x.aanvrNr from Rdm01 x where x.nrNlDoc = '%s'", documentNummer);
    List<String[]> records = getServices().getProbevSqlService().find(sql);
    return records.stream().findFirst().map(row -> row[0]).orElse("");
  }

  public Optional<RdmRaasComparison> getRaasComparison(ReisdocumentAanvraag zaak, boolean updateProweb) {
    RaasService raasService = getServices().getRaasService();
    if (raasService.isRaasServiceActive()) {
      FindAanvraagRequest fr = new FindAanvraagRequest(zaak.getAanvraagnummer().toLong());
      if (updateProweb) {
        fr.setUpdateProweb(true);
      }
      Optional<DocAanvraagDto> result = raasService.findFirst(fr);
      if (result.isPresent()) {
        DocAanvraagDto documentAanvraag = result.get();
        return Optional.of(new RdmRaasComparison(zaak, documentAanvraag));
      }
    }

    return Optional.empty();
  }

  @Override
  public ZaakContact getContact(ReisdocumentAanvraag zaak) {
    ZaakContact zaakContact = new ZaakContact();
    BasePLExt basisPersoon = getBasisPersoon(zaak);

    if (basisPersoon != null) {
      ZaakContactpersoon persoon = new ZaakContactpersoon(AANGEVER, basisPersoon);
      persoon.setContactgegevens(getServices().getContactgegevensService().getContactgegevens(zaak));
      zaakContact.add(persoon);

    }

    return zaakContact;
  }

  @Override
  public Controles getControles(ControlesListener listener) {
    return new ReisdocumentZaakControles(this).getControles(listener);
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van de reisdocumenten")
  public List<ReisdocumentAanvraag> getMinimalZaken(ZaakArgumenten zaakArgumenten) {
    return new ZakenList<>(
        copyList(Rdm01Dao.find(getArgumentenToMap(zaakArgumenten)), ReisdocumentAanvraag.class));
  }

  @Override
  public Zaak getNewZaak() {
    return aanvullenZaak(new ReisdocumentAanvraag());
  }

  /**
   * Nieuwe aanvraag
   */
  public ReisdocumentAanvraag getNewZaak(BasePLExt persoonslijst) {
    ReisdocumentAanvraag aanvraag = (ReisdocumentAanvraag) getNewZaak();
    aanvraag.setBasisPersoon(persoonslijst);
    return aanvraag;
  }

  /**
   * Zoek reisdocumenthistorie op de PL
   */
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

      documenten.add(doc);
    }

    return documenten;
  }

  public void afsluiten(
      ReisdocumentAanvraag aanvraag,
      LeveringType statLev, SluitingType statAfsl,
      Gebruiker gebruiker) {

    Long aanvrNr = aanvraag.getAanvraagnummer().toLong();

    // Update RAAS service
    if (getServices().getRaasService().isRaasServiceActive()) {
      if (getServices().getRaasService().isAanvraag(aanvrNr)) {
        AfsluitRequest afsluitRequest = new AfsluitRequest();
        afsluitRequest.setAanvraagNummer(aanvrNr);
        afsluitRequest.setNrNLDocIn("");
        afsluitRequest.setStatusLevering(LeveringStatusType.getByCode(statLev.getCode()));
        afsluitRequest.setStatusAfsluiting(AfsluitingStatusType.getByCode(statAfsl.getCode()));
        getServices().getRaasService().updateAanvraag(afsluitRequest.createRequest());
      }
    }
    ReisdocumentStatus status = aanvraag.getReisdocumentStatus();
    status.setStatusLevering(statLev);
    status.setStatusAfsluiting(statAfsl);
    status.setDatumTijdAfsluiting(new DateTime());

    if (gebruiker != null) {
      aanvraag.setGebruikerUitgifte(new UsrFieldValue(gebruiker));
    }
    // Opslaan aanvraag
    save(aanvraag);
  }

  /**
   * Geeft de reisdocumenten uit de reisdocumententabel terug.
   *
   * @return List<Reisdocument>
   */
  @ThrowException("Fout bij het ophalen van gegevens uit de database")
  public List<SoortReisdocument> getSoortReisdocumenten() {
    List<SoortReisdocument> list = new ArrayList<>();
    for (Reisdoc reisDoc : ReisdocDao.findAllReisdocs()) {
      list.add(copy(reisDoc, SoortReisdocument.class));
    }

    return list;
  }

  @Override
  public ReisdocumentAanvraag getStandardZaak(ReisdocumentAanvraag zaak) {
    getServices().getReisdocumentBezorgingService().findByAanvrNr(zaak.getAanvrNr())
        .ifPresent(voormelding -> zaak.getThuisbezorging().setMelding(voormelding));
    return new ReisdocumentZaakControles(this).controleer(super.getStandardZaak(zaak));
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van zaak-ids")
  public List<ZaakKey> getZaakKeys(ZaakArgumenten zaakArgumenten) {
    return Rdm01Dao.findZaakKeys(getArgumentenToMap(zaakArgumenten));
  }

  @ThrowException("Fout bij het opzoeken van aanvraagnummers")
  public boolean nummerBestaat(Aanvraagnummer aanvraagNummer) {
    return pos(aanvraagNummer.getNummer()) &&
        Rdm01Dao.findFirstByExample(new Rdm01(aanvraagNummer.getNummer())) != null;

  }

  @Override
  @Transactional
  @ThrowException("Fout bij het opslaan van de reisdocument aanvraag")
  public void save(ReisdocumentAanvraag zaak) {
    getZaakStatussen().setInitieleStatus(zaak, ZaakStatusType.INCOMPLEET);
    if (isSaved(zaak)) {
      // Op verwerkt zetten na uitreiking
      if (!zaak.getZaakHistorie().getStatusHistorie().isEindStatus()) {
        if (zaak.getReisdocumentStatus().getStatusAfsluiting().getCode() > SluitingType.AANVRAAG_NIET_AFGESLOTEN
            .getCode()) {
          ZaakStatusType status = VERWERKT;
          if (getServices().getRaasService().isRaasServiceActive()) {
            Long aanvrNr = zaak.getAanvraagnummer().toLong();
            if (getServices().getRaasService().isAanvraag(aanvrNr)) {
              status = VERWERKT_IN_GBA;
            }
          }
          updateStatus(zaak, zaak.getStatus(), status, "");
        }
      }
      opslaanStandaardZaak(zaak);
    } else {
      zaak.setUsr1(findEntity(Usr.class, toNul(zaak.getGebruikerAanvraag().getValue())));
      zaak.setUsr2(findEntity(Usr.class, toNul(zaak.getGebruikerUitgifte().getValue())));
      zaak.setIdVaststelling(getIdentificatie(zaak).getKorteOmschrijving());
      opslaanStandaardZaak(zaak);
    }

    callListeners(ServiceEvent.CHANGE);
  }

  /**
   * Verwijderen van de geboorteaanvraag
   */
  @Override
  @Transactional
  @ThrowException("Fout bij het verwijderen")
  public void delete(ReisdocumentAanvraag zaak) {
    if (fil(zaak.getZaakId())) {
      ConditionalMap map = new ConditionalMap();
      map.put(Rdm01Dao.ZAAK_ID, zaak.getZaakId());
      map.put(Rdm01Dao.MAX_CORRECT_RESULTS, 1);
      removeEntities(Rdm01Dao.find(map));
      deleteZaakRelaties(zaak);
    }

    callListeners(ServiceEvent.CHANGE);
  }

  public Optional<ReisdocumentInboxData> getInboxRequestData(ReisdocumentAanvraag aanvraag) {
    return getServices().getRequestInboxService()
        .getInboxProcessor(aanvraag, InboxReisdocumentProcessor.class)
        .map(InboxReisdocumentProcessor::getData);
  }

  public boolean isNieuwGezagReglement(ProcuraDate date) {
    return date.isExpired(new ProcuraDate(getServices().getParameterService()
        .getSysteemParameter(REISD_WIJZIGING_GEZAG).getValue()));
  }

  public Optional<SignaleringResult> checkAanvraag(Aanvraagnummer aanvraagnummer, BasePLExt pl) {
    VrsService vrsService = new VrsService(getServices().getParameterService());
    if (vrsService.isEnabled()) {
      return vrsService.checkSignalering(new VrsRequest()
              .aanvraagnummer(aanvraagnummer.getNummer())
              .bsn(new Bsn(pl.getPersoon().getBsn().getDescr())))
          .filter(SignaleringResult::isHit);
    } else {
      return signaleringFromPl(pl);
    }
  }

  @Override
  public Identificatie getIdentificatieBijUitreiking(IdentificatieUitreikingZaak zaak) {
    ReisdocumentAanvraag reisdocumentAanvraag = (ReisdocumentAanvraag) zaak;
    DateTime datumVerstrek = new DateTime(reisdocumentAanvraag.getDAfsl(), reisdocumentAanvraag.getTAfsl());
    return getServices().getIdentificatieService().getIdentificatie(zaak, datumVerstrek);
  }

  private Optional<SignaleringResult> signaleringFromPl(BasePLExt pl) {
    if (pl.getReisdoc().heeftSignalering()) {
      return Optional.of(SignaleringResult.builder()
          .mededelingRvIG(getSysteemParm(ParameterConstant.REISD_SIGNAL_INFO, false))
          .resultaatOmschrijving("Signalering met betrekking tot verstrekken Nederlands reisdocument op de PL")
          .build());
    }

    return Optional.empty();
  }

  private ConditionalMap getArgumentenToMap(ZaakArgumenten zaakArgumenten) {
    ConditionalMap map = getAlgemeneArgumentenToMap(zaakArgumenten);
    if (!zaakArgumenten.isAll()) {
      if (zaakArgumenten instanceof ReisdocumentZaakArgumenten) {
        String documentNummer = ((ReisdocumentZaakArgumenten) zaakArgumenten).getDocumentnummer();
        map.putString(Rdm01Dao.NR_NL_DOC, documentNummer);
      }
    }

    return map;
  }


}
