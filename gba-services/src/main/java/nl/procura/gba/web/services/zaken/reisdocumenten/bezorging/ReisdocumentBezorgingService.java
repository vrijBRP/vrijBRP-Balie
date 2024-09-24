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

package nl.procura.gba.web.services.zaken.reisdocumenten.bezorging;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.gba.common.ZaakStatusType.INBEHANDELING;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.AMP_ENABLED;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.AMP_ENABLED_BUNDLES;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.AMP_ENDPOINT;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.AMP_LOCATIONS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.AMP_SSL;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.GEMEENTE_CODES;
import static nl.procura.gba.web.services.zaken.reisdocumenten.LeveringType.DOCUMENT_GOED;
import static nl.procura.gba.web.services.zaken.reisdocumenten.LeveringType.DOCUMENT_NIET_GELEVERD;
import static nl.procura.gba.web.services.zaken.reisdocumenten.LeveringType.DOCUMENT_NIET_GOED;
import static nl.procura.gba.web.services.zaken.reisdocumenten.LeveringType.DOCUMENT_VERDWENEN;
import static nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentType.ONBEKEND;
import static nl.procura.gba.web.services.zaken.reisdocumenten.SluitingType.DOCUMENT_UITGEREIKT;
import static nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.BezorgingBlokkeerType.DOC_NIET_GELEVERD;
import static nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.BezorgingBlokkeerType.DOC_NIET_GOED;
import static nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.BezorgingBlokkeerType.DOC_UITGEREIKT;
import static nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.BezorgingBlokkeerType.DOC_VERDWENEN;
import static nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.BezorgingBlokkeerType.PL_ANDERE_GEMEENTE;
import static nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.BezorgingBlokkeerType.PL_IS_GEBLOKKEERD;
import static nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.BezorgingBlokkeerType.PL_IS_OPGESCHORT_E;
import static nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.BezorgingBlokkeerType.PL_IS_OPGESCHORT_M;
import static nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.BezorgingBlokkeerType.PL_IS_OPGESCHORT_O;
import static nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.BezorgingDienstType.DEL_ID_PB_IMG;
import static nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.BezorgingDienstType.SWAP_ID_PB_IMG;
import static nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.BezorgingGemeenteStatusType.IANN_BEZ;
import static nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.BezorgingGemeenteStatusType.IKPL_ORD;
import static nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.BezorgingGemeenteStatusType.ISTA_BLK;
import static nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.BezorgingGemeenteStatusType.ISTA_INK;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.pos;
import static nl.procura.standard.Globalfunctions.trim;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.isAllBlank;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import nl.amp.api.domain.BevestigingKoppelingOrderRequest;
import nl.amp.api.domain.BevestigingVerwerkingAnnuleringRequest;
import nl.amp.api.domain.BevestigingVerwerkingUitreikingRequest;
import nl.amp.api.domain.BlokkeringRequest;
import nl.amp.api.domain.GeannuleerdeOrder;
import nl.amp.api.domain.Geslacht;
import nl.amp.api.domain.InTeHoudenReisdocument;
import nl.amp.api.domain.ObjectFactory;
import nl.amp.api.domain.OpgehaaldeOrder;
import nl.amp.api.domain.OphalenAnnuleringenRequest;
import nl.amp.api.domain.OphalenAnnuleringenResult;
import nl.amp.api.domain.OphalenOrdersRequest;
import nl.amp.api.domain.OphalenOrdersResult;
import nl.amp.api.domain.OphalenUitreikingenRequest;
import nl.amp.api.domain.OphalenUitreikingenResult;
import nl.amp.api.domain.OrderUpdateRequest;
import nl.amp.api.domain.UitreikingenOrder;
import nl.amp.api.domain.VoormeldingRequest;
import nl.procura.amp.AMPSoapHandler;
import nl.procura.amp.AMPSoapUtils;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.formats.Adres;
import nl.procura.dto.raas.bestand.RaasBestandDto;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.jpa.personen.dao.RdmAmpDao;
import nl.procura.gba.jpa.personen.db.RdmAmp;
import nl.procura.gba.jpa.personen.db.RdmAmpDoc;
import nl.procura.gba.web.common.validators.PropertiesValidator;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.zaken.algemeen.ControleerbareService;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.controle.Controles;
import nl.procura.gba.web.services.zaken.algemeen.controle.ControlesListener;
import nl.procura.gba.web.services.zaken.algemeen.controle.StandaardControle;
import nl.procura.gba.web.services.zaken.contact.ContactgegevensService;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhouding;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhoudingenService;
import nl.procura.gba.web.services.zaken.inhoudingen.InhoudingType;
import nl.procura.gba.web.services.zaken.reisdocumenten.Aanvraagnummer;
import nl.procura.gba.web.services.zaken.reisdocumenten.LeveringType;
import nl.procura.gba.web.services.zaken.reisdocumenten.Reisdocument;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentAanvraag;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentType;
import nl.procura.gba.web.services.zaken.reisdocumenten.SluitingType;
import nl.procura.raas.rest.domain.raas.bestand.FindRaasBestandRequest;
import nl.procura.raas.rest.domain.raas.bestand.RaasBestandStatusType;
import nl.procura.raas.rest.domain.raas.bestand.RaasBestandType;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.validation.Bsn;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReisdocumentBezorgingService extends AbstractService implements ControleerbareService {

  public ReisdocumentBezorgingService() {
    super("Reisdocumentbezorging");
  }

  @Override
  public Controles getControles(ControlesListener listener) {
    Controles controles = new Controles();
    if (isEnabled()) {
      checkBezorging(controles);
    }
    return controles;
  }

  @ThrowException("Fout bij het controleren van de thuisbezorging")
  public Controles checkBezorging(Controles controles) {
    verzendenVoormeldingen().forEach(controles::addControle);
    ophalenOrders().forEach(controles::addControle);
    checkBlokkeringen().forEach(controles::addControle);
    inklarenOrders().forEach(controles::addControle);
    ophalenAnnuleringen().forEach(controles::addControle);
    ophalenUitreikingen().forEach(controles::addControle);
    afsluiten().forEach(controles::addControle);
    return controles;
  }

  @Transactional
  public void saveMelding(RdmAmp melding) {
    if (!new Aanvraagnummer(melding.getAanvrNr()).isCorrect()) {
      throw new ProException(WARNING, "Geen geldig aanvraagnummer ingevoerd");
    }
    if (emp(melding.getBundelRefNr())) {
      throw new ProException(WARNING, "Geen geldig referentienummer ingevoerd");
    }
    if (melding.isBezorgingGewenst()) {
      if (ReisdocumentType.get(melding.getDocType()) == ONBEKEND) {
        throw new ProException(WARNING, "Geen geldig reisdocumenttype ingevoerd");
      }
      if (!new Bsn(melding.getBsn()).isCorrect()) {
        throw new ProException(WARNING, "Geen geldig burgerservicenummer ingevoerd");
      }
      if (isAllBlank(melding.getEmail(), melding.getTel1(), melding.getTel2())) {
        throw new ProException(WARNING, "Geen e-mailadres, mobiel- of thuisnummer ingevoerd");
      }
      if (aval(melding.getLocatiecode()) < 0) {
        throw new ProException(WARNING, "Geen locatiecode ingevoerd");
      }
    }
    melding.setStatus(BezorgingStatus.getStatus(melding).getType().getCode());
    saveEntity(melding);
  }

  @Transactional
  public void saveInhouding(RdmAmpDoc ampDoc) {
    saveEntity(ampDoc);
  }

  @Transactional
  public void deleteInhouding(RdmAmpDoc ampDoc) {
    removeEntity(ampDoc);
  }

  public List<RdmAmp> findByOrderRefNr(String orderRefNr) {
    return RdmAmpDao.findByOrderRefNr(orderRefNr);
  }

  public Optional<RdmAmp> findByAanvrNr(String aanvrNr) {
    return RdmAmpDao.findByAanvrNr(new Aanvraagnummer(aanvrNr).getNummer());
  }

  public List<Bezorging> findOther(RdmAmp voormelding) {
    return RdmAmpDao.findOtherByRefNr(voormelding).stream().map(Bezorging::new).collect(toList());
  }

  public List<Bezorging> findOtherByAddress(Bezorging bezorging) {
    List<RdmAmp> meldingen = RdmAmpDao.findOtherByAddress(bezorging.getMelding());
    return meldingen.stream().map(Bezorging::new).collect(toList());
  }

  public RdmAmp createNewVoormelding(ReisdocumentAanvraag aanvraag) {
    RdmAmp melding = new RdmAmp();
    melding.setRdm01Id(aanvraag.getRdm01Id());
    melding.setAanvrNr(aanvraag.getAanvraagnummer().getNummer());
    melding.setBundelRefNr(aanvraag.getAanvraagnummer().getNummer());
    melding.setDocType(aanvraag.getReisdocumentType().getCode());
    melding.setStatus(BezorgingStatusType.INGEVOERD.getCode());
    melding.setDIn(along(new ProcuraDate().getSystemDate()));
    updateVoormelding(melding, aanvraag.getBurgerServiceNummer());
    return melding;
  }

  public void updateVoormelding(RdmAmp melding, BsnFieldValue bsn) {
    // adres
    BasePLExt pl = getServices().getPersonenWsService().getPersoonslijst(bsn.getStringValue());
    Adres adres = pl.getVerblijfplaats().getAdres();
    if (emp(melding.getPc())) {
      melding.setHnr(adres.getHuisnummer().getValue().toLong());
      melding.setHnrL(adres.getHuisletter().getValue().getVal());
      melding.setHnrT(adres.getHuisnummertoev().getValue().getVal());
      melding.setPc(adres.getPostcode().getValue().getVal());
      melding.setStraat(adres.getStraat().getValue().getVal());
      melding.setWpl(adres.getWoonplaats().getValue().getVal());
      melding.setGemeentecode(adres.getGemeente().getValue().toLong());
      melding.setGemeentenaam(adres.getGemeente().getValue().getDescr());
    }
    // persoon
    if (emp(melding.getGeslachtsnaam())) {
      melding.setVoorl(pl.getPersoon().getNaam().getInitialen());
      melding.setVoornamen(pl.getPersoon().getNaam().getVoornamen().getValue().getVal());
      melding.setVoorv(pl.getPersoon().getNaam().getVoorvoegsel().getValue().getVal());
      melding.setGeslachtsnaam(pl.getPersoon().getNaam().getGeslachtsnaam().getValue().getVal());
      melding.setDGeb(pl.getPersoon().getGeboorte().getGeboortedatum().toLong());
      melding.setGesl(pl.getPersoon().getGeslacht().getVal());
    }
    // contactgegevens
    ContactgegevensService dbcg = getServices().getContactgegevensService();
    if (emp(melding.getEmail())) {
      String email = dbcg.getContactWaarde(pl, ContactgegevensService.EMAIL);
      melding.setEmail(email);
    }
    if (emp(melding.getTel1())) {
      String mobiel = dbcg.getContactWaarde(pl, ContactgegevensService.TEL_MOBIEL);
      String thuis = dbcg.getContactWaarde(pl, ContactgegevensService.TEL_THUIS);
      melding.setTel1(defaultIfBlank(mobiel, thuis));
      melding.setTel2(Objects.equals(melding.getTel1(), mobiel) ? thuis : "");
    }
  }

  /*
  * Voor nu wordt uitgegaan van status In behandeling.
  * Misschien later nog handig
  */
  public boolean isVerstuurdNaarRAAS(Long aanvrNr) {
    if (getServices().getRaasService().isRaasServiceActive()) {
      FindRaasBestandRequest request = new FindRaasBestandRequest();
      request.setAanvraagNr(aanvrNr);
      request.setStatussen(singletonList(RaasBestandStatusType.VERWERKT));
      for (RaasBestandDto bestandDto : getServices().getRaasService().getAanvragen(request).getContent()) {
        if (RaasBestandType.AANVRAAG.equals(bestandDto.getType().getValue())) {
          return true;
        }
      }
    }
    return false;
  }

  private List<BezorgingCheck> verzendenVoormeldingen() {
    List<BezorgingCheck> checks = new ArrayList<>();
    List<RdmAmp> meldingen = RdmAmpDao.findNieuwe();
    for (RdmAmp melding : meldingen) {
      ReisdocumentAanvraag aanvraag = getReisdocumentAanvraag(melding);
      updateVoormelding(melding, new BsnFieldValue(melding.getBsn()));
      if (aanvraag != null && aanvraag.getStatus().isMinimaal(INBEHANDELING)) {
        VoormeldingRequest request = voorvermelding(melding);
        getAmpSoapHandler().voormelding(request);
        melding.setIndVoormelding(true);
        saveMelding(melding);
        checks.add(BezorgingCheck.verzonden(melding.getAanvrNr()));
      }
    }
    return checks;
  }

  private VoormeldingRequest voorvermelding(RdmAmp melding) {
    List<RdmAmpDoc> rmdAmdDocs = melding.getRdmAmpDocs();
    boolean sprakeVanInhoudingen = rmdAmdDocs != null && !rmdAmdDocs.isEmpty();
    // Create request
    ObjectFactory of = new ObjectFactory();
    VoormeldingRequest request = of.createVoormeldingRequest()
        .withAanvraagnummer(new Aanvraagnummer(melding.getAanvrNr()).toLong())
        .withAdresgegevens(of.createAdresgegevens()
            .withHuisnummer(melding.getHnr().intValue())
            .withHuisnummerToevoeging(trim(melding.getHnrL() + " " + melding.getHnrT()))
            .withPostcode(melding.getPc())
            .withStraatnaam(melding.getStraat())
            .withWoonplaats(melding.getWpl()))
        .withDiensttype(sprakeVanInhoudingen ? SWAP_ID_PB_IMG.getOms() : DEL_ID_PB_IMG.getOms())
        .withGemeentecode(melding.getGemeentecode().shortValue())
        .withGemeentenaam(melding.getGemeentenaam())
        .withHoofdorder(melding.getHoofdorder())
        .withLocatiecode(melding.getLocatiecode())
        .withLocatieOmschrijving(getLocatieOmschrijving(melding.getLocatiecode()))
        .withOpmerkingen(of.createVoormeldingRequestOpmerkingen(melding.getOpmerkingen()))
        .withPersoonsgegevens(of.createPersoonsgegevens()
            .withVoorletters(melding.getVoorl())
            .withVoornamen(melding.getVoornamen())
            .withVoorvoegsels(melding.getVoorv())
            .withAchternaam(melding.getGeslachtsnaam())
            .withGeboortedatum(melding.getDGeb().intValue())
            .withGeslacht(Geslacht.valueOf(melding.getGesl().toUpperCase()))
            .withEmailadres(melding.getEmail())
            .withTelefoonnummer1(melding.getTel1())
            .withTelefoonnummer2(melding.getTel2()))
        .withReferentienummer(of.createVoormeldingRequestReferentienummer(melding.getBundelRefNr()))
        .withReisdocumentOmschrijving(ReisdocumentType.get(melding.getDocType()).getOms());

    // In te houden reisdocumenten
    if (sprakeVanInhoudingen) {
      List<InTeHoudenReisdocument> docs = new ArrayList<>();
      for (RdmAmpDoc rmdAmdDoc : rmdAmdDocs) {
        docs.add(of.createInTeHoudenReisdocument()
            .withReisdocumentnummer(rmdAmdDoc.getDocNr())
            .withReisdocumentomschrijving(rmdAmdDoc.getDocType()));
      }
      request.withInTeHoudenReisdocumenten(AMPSoapUtils.toJaxbElement(docs));
    }

    return request;
  }

  private String getLocatieOmschrijving(Long locatiecode) {
    Map<String, String> map = PropertiesValidator.toMap(getSysteemParm(AMP_LOCATIONS, true));
    if (!map.containsKey(locatiecode.toString())) {
      throw new ProException(WARNING, "Locatiecode " + locatiecode + " niet gevonden");
    }
    return map.get(locatiecode.toString());
  }

  private List<BezorgingCheck> ophalenOrders() {
    List<BezorgingCheck> checks = new ArrayList<>();
    OphalenOrdersRequest request = new OphalenOrdersRequest(getGemeenteCode());
    OphalenOrdersResult result = getAmpSoapHandler().ophalenOrders(request);
    List<OpgehaaldeOrder> orders = result.getOrders().getValue().getOpgehaaldeOrder();
    for (OpgehaaldeOrder order : orders) {
      Long aanvrNr = order.getAanvraagnummer();
      String orderRefId = order.getOrderreferentieid().getValue();
      Optional<RdmAmp> vm = findByAanvrNr(aanvrNr.toString());
      if (vm.isPresent()) {
        RdmAmp melding = vm.get();
        if (melding.isIndVoormelding()) {
          bevestigKoppeling(orderRefId, melding);
          checks.add(BezorgingCheck.gekoppeld(melding.getAanvrNr()));
        }
      } else {
        checks.add(BezorgingCheck.voormeldingOnbekend(aanvrNr.toString()));
      }
    }
    return checks;
  }

  private void bevestigKoppeling(String orderRefId, RdmAmp melding) {
    // Send request
    getAmpSoapHandler().bevestigingKoppelingOrder(new BevestigingKoppelingOrderRequest()
        .withOrderreferentieid(orderRefId)
        .withGemeentecode(melding.getGemeentecode().shortValue())
        .withGemeentestatus(IKPL_ORD.getCode()));
    // Update melding
    melding.setOrderRefNr(orderRefId);
    melding.setIndKoppeling(true);
    saveMelding(melding);
  }

  private List<BezorgingCheck> inklarenOrders() {
    List<BezorgingCheck> checks = new ArrayList<>();
    for (RdmAmp melding : RdmAmpDao.findNietIngeklaarde()) {
      ReisdocumentAanvraag aanvraag = getReisdocumentAanvraag(melding);
      if (aanvraag != null && fil(aanvraag.getNrNlDoc())) {
        inklarenOrder(melding.getOrderRefNr(), aanvraag.getNrNlDoc(), melding);
        checks.add(BezorgingCheck.ingeklaard(melding.getOrderRefNr()));
      }
    }
    return checks;
  }

  private List<BezorgingCheck> checkBlokkeringen() {
    List<BezorgingCheck> checks = new ArrayList<>();
    for (RdmAmp melding : RdmAmpDao.findGekoppelde()) {
      ReisdocumentAanvraag aanvraag = getReisdocumentAanvraag(melding);
      if (aanvraag != null) {
        Bsn bsn = new Bsn(melding.getBsn());
        BasePLExt pl = getServices().getPersonenWsService().getPersoonslijst(bsn.getDefaultBsn());

        LeveringType statusLevering = aanvraag.getReisdocumentStatus().getStatusLevering();
        SluitingType statusAfsluiting = aanvraag.getReisdocumentStatus().getStatusAfsluiting();

        if (statusLevering.is(DOCUMENT_NIET_GOED)) {
          blokkeer(melding.getOrderRefNr(), DOC_NIET_GOED, melding);

        } else if (statusLevering.is(DOCUMENT_NIET_GELEVERD)) {
          blokkeer(melding.getOrderRefNr(), DOC_NIET_GELEVERD, melding);

        } else if (statusLevering.is(DOCUMENT_VERDWENEN)) {
          blokkeer(melding.getOrderRefNr(), DOC_VERDWENEN, melding);

        } else if (statusAfsluiting.is(DOCUMENT_UITGEREIKT)) {
          blokkeer(melding.getOrderRefNr(), DOC_UITGEREIKT, melding);

        } else if (pl.getPersoon().getStatus().isBlokkering()) {
          blokkeer(melding.getOrderRefNr(), PL_IS_GEBLOKKEERD, melding);

        } else if (pl.getPersoon().getStatus().isOpgeschort()) {
          if (pl.getPersoon().getStatus().isOverleden()) {
            blokkeer(melding.getOrderRefNr(), PL_IS_OPGESCHORT_O, melding);

          } else if (pl.getPersoon().getStatus().isEmigratie()) {
            blokkeer(melding.getOrderRefNr(), PL_IS_OPGESCHORT_E, melding);

          } else if (pl.getPersoon().getStatus().isMinisterieelBesluit()) {
            blokkeer(melding.getOrderRefNr(), PL_IS_OPGESCHORT_M, melding);
          }

        } else {
          int gemeenteCode = aval(getServices().getGebruiker().getGemeenteCode());
          int adresGemeenteCode = aval(pl.getVerblijfplaats().getAdres().getGemeente().getValue().getVal());

          if (gemeenteCode > 0 && adresGemeenteCode != gemeenteCode) {
            blokkeer(melding.getOrderRefNr(), PL_ANDERE_GEMEENTE, melding);
          }
        }
        if (fil(melding.getCodeBlokkering()) || fil(melding.getOmsBlokkering())) {
          checks.add(BezorgingCheck.blokkering(melding.getOrderRefNr()));
        }
      }
    }
    return checks;
  }

  private void blokkeer(String orderRefId, BezorgingBlokkeerType type, RdmAmp melding) {
    // Send request
    getAmpSoapHandler().blokkeer(new BlokkeringRequest()
        .withOrderreferentieid(orderRefId)
        .withGemeentecode(melding.getGemeentecode().shortValue())
        .withGemeentestatus(ISTA_BLK.getCode())
        .withOmschrijving(type.getOms()));
    // Update melding
    melding.setIndBlokkering(true);
    melding.setCodeBlokkering(type.getCode());
    melding.setOmsBlokkering(type.getOms());
    saveMelding(melding);
  }

  public ReisdocumentAanvraag getReisdocumentAanvraag(RdmAmp melding) {
    ZaakArgumenten args = new ZaakArgumenten(melding.getAanvrNr());
    return getServices().getReisdocumentService()
        .getMinimalZaken(args)
        .stream()
        .findFirst()
        .orElse(null);
  }

  private void inklarenOrder(String orderRefId, String reisdocumentNr, RdmAmp melding) {
    // Send request
    getAmpSoapHandler().orderUpdate(new OrderUpdateRequest()
        .withOrderreferentieid(orderRefId)
        .withGemeentecode(melding.getGemeentecode().shortValue())
        .withGemeentestatus(ISTA_INK.getCode())
        .withReisdocumentnummer(reisdocumentNr));
    // Update melding
    melding.setIndInklaring(true);
    saveMelding(melding);
  }

  private List<BezorgingCheck> ophalenAnnuleringen() {
    List<BezorgingCheck> checks = new ArrayList<>();
    OphalenAnnuleringenRequest request = new OphalenAnnuleringenRequest(getGemeenteCode());
    OphalenAnnuleringenResult result = getAmpSoapHandler().ophalenAnnuleringen(request);
    List<GeannuleerdeOrder> orders = result.getGeannuleerdeOrders().getGeannuleerdeOrder();
    for (GeannuleerdeOrder order : orders) {
      String orderRefNr = order.getOrderreferentieid();
      List<RdmAmp> rdmAmps = findByOrderRefNr(orderRefNr);
      if (rdmAmps.isEmpty()) {
        checks.add(BezorgingCheck.orderAnnulerenOnbekend(order.getOrderreferentieid()));
      } else {
        for (RdmAmp melding : rdmAmps) {
          if (melding.isIndInklaring()) {
            bevestigAnnulering(melding, order);
            checks.add(BezorgingCheck.geannuleerd(melding.getAanvrNr()));
          }
        }
      }
    }
    return checks;
  }

  private void bevestigAnnulering(RdmAmp melding, GeannuleerdeOrder order) {
    // Send request
    getAmpSoapHandler().bevestigingAnnulering(new BevestigingVerwerkingAnnuleringRequest()
        .withOrderreferentieid(melding.getOrderRefNr())
        .withGemeentecode(melding.getGemeentecode().shortValue())
        .withGemeentestatus(IANN_BEZ.getCode()));
    // Update melding
    melding.setIndAnnulering(true);
    melding.setCodeAnnulering(order.getCode());
    melding.setOmsAnnulering(order.getOmschrijving());
    saveMelding(melding);
  }
  /*
   * Ophalen van uitgereikte orders
   */

  private List<BezorgingCheck> ophalenUitreikingen() {
    List<BezorgingCheck> checks = new ArrayList<>();
    OphalenUitreikingenRequest request = new OphalenUitreikingenRequest(getGemeenteCode());
    OphalenUitreikingenResult result = getAmpSoapHandler().ophalenUitreikingen(request);
    List<UitreikingenOrder> orders = result.getOrders().getUitreikingenOrder();
    for (UitreikingenOrder order : orders) {
      String orderRefNr = order.getOrderreferentieid();
      List<RdmAmp> rdmAmps = findByOrderRefNr(orderRefNr);
      if (rdmAmps.isEmpty()) {
        checks.add(BezorgingCheck.orderUitreikenOnbekend(order.getOrderreferentieid()));
      } else {
        for (RdmAmp melding : rdmAmps) {
          if (melding.isIndInklaring()) {
            bevestigUitreiking(melding);
            checks.add(BezorgingCheck.uitgereikt(melding.getAanvrNr()));
          }
        }
      }
    }
    return checks;
  }

  private void bevestigUitreiking(RdmAmp melding) {
    // Send request
    getAmpSoapHandler().bevestigUitreiking(new BevestigingVerwerkingUitreikingRequest()
        .withOrderreferentieid(melding.getOrderRefNr())
        .withGemeentecode(melding.getGemeentecode().shortValue())
        .withGemeentestatus(BezorgingGemeenteStatusType.IUIT_BEZ.getCode()));
    // Update melding
    melding.setIndUitreiking(true);
    saveMelding(melding);
  }

  private List<BezorgingCheck> afsluiten() {
    List<BezorgingCheck> checks = new ArrayList<>();
    for (RdmAmp melding : RdmAmpDao.findUitgereikte()) {
      ReisdocumentAanvraag aanvraag = getReisdocumentAanvraag(melding);
      if (aanvraag.getReisdocumentStatus().isUitTeReiken()) {
        getServices().getReisdocumentService().afsluiten(aanvraag, DOCUMENT_GOED, DOCUMENT_UITGEREIKT, null);
        verwerkenInhoudingen(melding, checks);
      }
      melding.setDEnd(along(new ProcuraDate().getSystemDate()));
      saveMelding(melding);
      checks.add(BezorgingCheck.afsluiten(melding.getAanvrNr()));
    }

    return checks;
  }

  private void verwerkenInhoudingen(RdmAmp melding, List<BezorgingCheck> checks) {
    for (RdmAmpDoc rmdAmpDoc : melding.getRdmAmpDocs()) {
      DocumentInhoudingenService service = getServices().getDocumentInhoudingenService();
      if (moetIngehoudenWorden(melding, rmdAmpDoc)) {
        DocumentInhouding zaak = (DocumentInhouding) service.getNewZaak();
        zaak.setBsn(BigDecimal.valueOf(melding.getBsn()));
        zaak.setDocumentType(ReisdocumentType.get(rmdAmpDoc.getDocType()));
        zaak.setNummerDocument(rmdAmpDoc.getDocNr());
        zaak.setInhoudingType(InhoudingType.INHOUDING);
        zaak.setDatumTijdInvoer(new DateTime());
        zaak.setDatumIngang(new DateTime());
        zaak.setProcesVerbaalNummer("");
        zaak.setProcesVerbaalOms("");
        zaak.setSprakeVanRijbewijs(false);

        service.getZaakStatussen().setInitieleStatus(zaak, ZaakStatusType.OPGENOMEN);
        getServices().getZaakIdentificatieService().getDmsZaakId(zaak);
        service.save(zaak);

        checks.add(BezorgingCheck.verwerkInhouding(rmdAmpDoc.getDocNr()));
      }
    }
  }

  private boolean moetIngehoudenWorden(RdmAmp melding, RdmAmpDoc rmdAmpDoc) {
    DocumentInhoudingenService service = getServices().getDocumentInhoudingenService();
    BasePLExt pl = getServices().getPersonenWsService().getPersoonslijst(melding.getBsn().toString());
    for (Reisdocument reisdocument : service.getReisdocumentHistorie(pl)) {
      if (!service.isReisdocumentIngehouden(pl, reisdocument)) {
        if (rmdAmpDoc.getDocNr().equals(reisdocument.getNummerDocument().getVal())) {
          return true;
        }
      }
    }
    return false;
  }

  private short getGemeenteCode() {
    String gemeenteCode = getServices().getParameterService().getSysteemParm(GEMEENTE_CODES, true);
    return Integer.valueOf(gemeenteCode).shortValue();
  }

  public boolean isEnabled() {
    return pos(getSysteemParm(AMP_ENABLED, false));
  }

  public boolean isBundelingMogelijk() {
    return pos(getSysteemParm(AMP_ENABLED_BUNDLES, false));
  }

  public AMPSoapHandler getAmpSoapHandler() {
    String endpoint = getSysteemParm(AMP_ENDPOINT, true);
    if (pos(getSysteemParm(AMP_SSL, false))) {
      endpoint = getProxyUrl(endpoint, true);
    }
    return new AMPSoapHandler(endpoint);
  }

  public void testConnection() {
    try {
      getAmpSoapHandler().ophalenOrders(new OphalenOrdersRequest(getGemeenteCode()));
    } catch (RuntimeException e) {
      throw new ProException("Kan geen verbinding maken", e);
    }
  }

  @Getter
  public static class BezorgingCheck extends StandaardControle {

    public static BezorgingCheck voormeldingOnbekend(String id) {
      return new BezorgingCheck(id, "Ophalen orders", "bezorgmelding niet gevonden in VrijBRP", false);
    }

    public static BezorgingCheck orderAnnulerenOnbekend(String id) {
      return new BezorgingCheck(id, "Annuleren order", "order niet gevonden in VrijBRP", false);
    }

    public static BezorgingCheck orderUitreikenOnbekend(String id) {
      return new BezorgingCheck(id, "Uitreiken order", "order niet gevonden in VrijBRP", false);
    }

    public static BezorgingCheck verzonden(String id) {
      return new BezorgingCheck(id, "Verzenden voormelding", "bezorgmelding verzonden", true);
    }

    public static BezorgingCheck gekoppeld(String id) {
      return new BezorgingCheck(id, "Koppelen order", "bevestiging van koppeling verzonden", true);
    }

    public static BezorgingCheck ingeklaard(String id) {
      return new BezorgingCheck(id, "Inklaren order", "bevestiging van inklaring verzonden", true);
    }

    public static BezorgingCheck blokkering(String id) {
      return new BezorgingCheck(id, "Blokkeren order", "blokkering doorgegeven aan leverancier", true);
    }

    public static BezorgingCheck geannuleerd(String id) {
      return new BezorgingCheck(id, "Annuleren order", "bevestiging van annulering verzonden", true);
    }

    public static BezorgingCheck uitgereikt(String zaakId) {
      return new BezorgingCheck(zaakId, "Uitreiken order", "bevestiging van uitreiking verzonden", true);
    }

    public static BezorgingCheck afsluiten(String zaakId) {
      return new BezorgingCheck(zaakId, "Afsluiten melding", "Bezorgmelding afgesloten in VrijBRP", true);
    }

    public static BezorgingCheck verwerkInhouding(String zaakId) {
      return new BezorgingCheck(zaakId, "Invoeren inhouding", "Reisdocumentinhouding toegevoegd in VrijBRP", true);
    }

    public BezorgingCheck(String zaakId, String actie, String omschrijving, boolean gewijzigd) {
      super("Reisdocumentbezorging", actie + ": " + omschrijving);
      setId(zaakId);
      setGewijzigd(gewijzigd);
    }
  }
}
