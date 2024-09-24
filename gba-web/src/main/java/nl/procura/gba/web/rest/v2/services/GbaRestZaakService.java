/*
 * Copyright 2023 - 2024 Procura B.V.
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

package nl.procura.gba.web.rest.v2.services;

import static nl.procura.gba.web.rest.v2.converters.GbaRestBaseTypeConverter.toTableRecord;
import static nl.procura.gba.web.rest.v2.converters.zaken.GbaRestZaakZoekenVraagConverter.toZaakargumenten;
import static nl.procura.gba.web.rest.v2.model.base.GbaRestEnum.toEnum;
import static nl.procura.gba.web.rest.v2.model.zaken.base.GbaRestZaakZoekGegeven.AANTALLEN;
import static nl.procura.gba.web.rest.v2.model.zaken.base.GbaRestZaakZoekGegeven.ALGEMENE_ZAAKGEGEVENS;
import static nl.procura.gba.web.rest.v2.model.zaken.base.GbaRestZaakZoekGegeven.SLEUTELS;
import static nl.procura.gba.web.rest.v2.model.zaken.base.GbaRestZaakZoekGegeven.SPECIFIEKE_ZAAKGEGEVENS;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.web.rest.v2.model.zaken.GbaRestZaakStatusUpdateVraag;
import nl.procura.gba.web.rest.v2.model.zaken.GbaRestZaakToevoegenVraag;
import nl.procura.gba.web.rest.v2.model.zaken.GbaRestZaakUpdateVraag;
import nl.procura.gba.web.rest.v2.model.zaken.GbaRestZaakZoekenAntwoord;
import nl.procura.gba.web.rest.v2.model.zaken.GbaRestZaakZoekenVraag;
import nl.procura.gba.web.rest.v2.model.zaken.base.GbaRestZaak;
import nl.procura.gba.web.rest.v2.model.zaken.base.GbaRestZaakAlgemeen;
import nl.procura.gba.web.rest.v2.model.zaken.base.GbaRestZaakAttribuut;
import nl.procura.gba.web.rest.v2.model.zaken.base.GbaRestZaakId;
import nl.procura.gba.web.rest.v2.model.zaken.base.GbaRestZaakRelatie;
import nl.procura.gba.web.rest.v2.model.zaken.base.GbaRestZaakSleutel;
import nl.procura.gba.web.rest.v2.model.zaken.base.GbaRestZaakStatus;
import nl.procura.gba.web.rest.v2.model.zaken.base.GbaRestZaakStatusType;
import nl.procura.gba.web.rest.v2.model.zaken.base.GbaRestZaakType;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakStatus;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;
import nl.procura.gba.web.services.zaken.algemeen.ZakenService;
import nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakAttribuut;
import nl.procura.gba.web.services.zaken.algemeen.identificatie.ZaakIdentificatie;
import nl.procura.gba.web.services.zaken.algemeen.identificatie.ZaakIdentificatieService;
import nl.procura.gba.web.services.zaken.algemeen.zaakrelaties.ZaakRelatie;
import nl.procura.gba.web.services.zaken.algemeen.zkndms.ZaakDmsService;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraag;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisType;
import nl.procura.commons.core.exceptions.ProException;

public class GbaRestZaakService extends GbaRestAbstractService {

  public GbaRestZaak getByZaakId(String zaakId) {
    return toGbaRestCompleteZaak(getZaakByZaakId(zaakId));
  }

  public GbaRestZaakZoekenAntwoord search(GbaRestZaakZoekenVraag request) {
    GbaRestZaakZoekenAntwoord antwoord = new GbaRestZaakZoekenAntwoord();
    ZakenService zakenService = getServices().getZakenService();

    if (request.isZoekGegeven(AANTALLEN)) {
      int aantalZaken = zakenService.getAantalZaken(toZaakargumenten(request));
      antwoord.setTotaal(aantalZaken);

    } else if (request.isZoekGegeven(SLEUTELS)) {
      antwoord.setSleutels(zakenService
          .getZaakKeys(toZaakargumenten(request))
          .stream()
          .map(this::toGbaRestZaakSleutel)
          .collect(Collectors.toList()));

    } else if (request.isZoekGegeven(ALGEMENE_ZAAKGEGEVENS)) {
      List<Zaak> standaardZaken = zakenService.getMinimaleZaken(toZaakargumenten(request));
      antwoord.setZaken(toGbaRestZaken(standaardZaken, false));
      antwoord.setTotaal(standaardZaken.size());

    } else if (request.isZoekGegeven(SPECIFIEKE_ZAAKGEGEVENS)) {
      List<Zaak> standaardZaken = zakenService.getStandaardZaken(toZaakargumenten(request));
      antwoord.setZaken(toGbaRestZaken(standaardZaken, true));
      antwoord.setTotaal(standaardZaken.size());
    }
    return antwoord;
  }

  public void deleteByZaakId(String zaakId) {
    getServices().getZakenService()
        .delete(new ZaakKey(getZaakByZaakId(zaakId).getZaakId()));
  }

  public GbaRestZaak addZaak(GbaRestZaakToevoegenVraag request) {
    List<String> zaakIds = getZaakIds(request.getZaak());
    zaakIds.stream()
        .filter(zaakId -> getServices()
            .getZakenService()
            .getAantalZaken(new ZaakArgumenten(zaakId)) > 0)
        .forEach(zaakId -> {
          throw new ProException(ERROR, "Er is al een zaak met zaak-id: " + zaakId);
        });

    GbaRestZaakType zaakType = request.getZaak().getAlgemeen().getType();
    Zaak savedZaak;
    switch (zaakType) {
      case BINNENVERHUIZING:
      case BUITENVERHUIZING:
      case EMIGRATIE:
      case HERVESTIGING:
        savedZaak = getRestServices()
            .getVerhuizingService()
            .addVerhuizing(request.getZaak());
        break;
      case GEBOORTE:
        savedZaak = getRestServices()
            .getGeboorteService()
            .addGeboorte(request.getZaak());
        break;
      case HUWELIJK_GPS_GEMEENTE:
        savedZaak = getRestServices()
            .getHuwelijkService()
            .addHuwelijk(request.getZaak());
        break;
      case OVERLIJDEN_IN_GEMEENTE:
      case LIJKVINDING:
        savedZaak = getRestServices()
            .getOverlijdenService()
            .addOverlijden(request.getZaak());
        break;
      default:
        throw new ProException(ERROR, zaakType + " is nog niet uitgewerkt");
    }

    return getByZaakId(savedZaak.getZaakId());
  }

  public GbaRestZaak updateZaak(GbaRestZaakUpdateVraag request) {
    Dossier zaak = getZaakByZaakId(request.getZaak().getAlgemeen().getZaakId());
    Zaak updatedZaak;
    switch (zaak.getType()) {
      case HUWELIJK_GPS_GEMEENTE:
        updatedZaak = getRestServices()
            .getHuwelijkService()
            .updateHuwelijk(request.getZaak(), zaak);
        break;
      default:
        throw new ProException(ERROR, zaak.getType() + " is nog niet uitgewerkt");
    }
    return getByZaakId(updatedZaak.getZaakId());
  }

  public void updateZaakStatus(GbaRestZaakStatusUpdateVraag request) {
    if (request.getZaakStatus() == null) {
      throw new ProException(ERROR, "zaakStatus is onbekend of niet gezet");
    }
    Zaak zaak = getZaakByZaakId(request.getZaakId());
    ZaakStatusType nieuweStatus = ZaakStatusType.get(request.getZaakStatus().getCode());
    getServices().getZakenService().getService(zaak)
        .updateStatus(zaak,
            zaak.getStatus(),
            nieuweStatus,
            request.getOpmerking());
  }

  private List<String> getZaakIds(GbaRestZaak zaak) {
    String zaakId = zaak.getAlgemeen().getZaakId();
    List<String> zaakIds = new ArrayList<>();
    if (StringUtils.isNotBlank(zaakId)) {
      zaakIds.add(zaakId);
    }
    List<GbaRestZaakId> ids = zaak.getAlgemeen().getIds();
    if (ids != null) {
      for (GbaRestZaakId id : ids) {
        zaakIds.add(id.getId());
      }
    }
    return zaakIds;
  }

  public <T extends Zaak> T getZaakByZaakId(String zaakId, ZaakType zaakType) {
    if (StringUtils.isBlank(zaakId)) {
      throw new ProException("Geen zaak-id");
    }
    ZaakArgumenten za = new ZaakArgumenten(zaakId);
    za.setMax(2);
    List<Zaak> zaken = getServices().getZakenService().getStandaardZaken(za);
    if (zaken.isEmpty()) {
      throw new ProException(String.format("Er is geen zaak met zaak-id '%s'", zaakId));
    } else if (zaken.size() > 1) {
      throw new ProException(String.format("Er zijn meerdere zaken met zaak-id '%s'", zaakId));
    }
    Zaak zaak = zaken.get(0);
    if (zaakType != ZaakType.ONBEKEND && zaak.getType() != zaakType) {
      throw new ProException(String.format("Er is een zaak met zaak-id '%s', " +
          "maar dat is van type '%s', niet van het type '%s'",
          zaakId, zaak.getType().getOms(), zaakType));
    }
    return (T) zaak;
  }

  public <T extends Zaak> T getZaakByZaakId(String zaakId) {
    return getZaakByZaakId(zaakId, ZaakType.ONBEKEND);
  }

  public GbaRestZaak toGbaRestBaseZaak(Zaak zaak) {
    GbaRestZaak restZaak = new GbaRestZaak();
    restZaak.setAlgemeen(new GbaRestZaakAlgemeen());
    addBaseElements(zaak, restZaak.getAlgemeen());
    return restZaak;
  }

  public GbaRestZaak toGbaRestCompleteZaak(Zaak zaak) {
    GbaRestZaak restZaak = toGbaRestBaseZaak(zaak);
    addExtraElements(zaak, restZaak.getAlgemeen());
    addZaakElements(zaak, restZaak);
    return restZaak;
  }

  protected void addGenericZaak(GbaRestZaak restObj, Zaak va) {
    GbaRestZaakAlgemeen algemeen = restObj.getAlgemeen();
    if (StringUtils.isNotBlank(algemeen.getZaakId())) {
      va.setZaakId(algemeen.getZaakId());
    }
    va.setBron(algemeen.getBron());
    va.setLeverancier(algemeen.getLeverancier());
    va.setDatumIngang(new DateTime(algemeen.getDatumIngang()));

    if (algemeen.getDatumInvoer() != null && algemeen.getTijdInvoer() != null) {
      va.setDatumTijdInvoer(new DateTime(algemeen.getDatumInvoer(), algemeen.getTijdInvoer()));
    } else {
      va.setDatumTijdInvoer(new DateTime());
    }
  }

  private void addBaseElements(Zaak zaak, GbaRestZaakAlgemeen baseElements) {
    baseElements.setZaakId(zaak.getZaakId());
    baseElements.setMeestRelevanteZaakId(ZaakUtils.getRelevantZaakId(zaak));
    baseElements.setType(toGbaRestZaakType(zaak));
    baseElements.setSoort(zaak.getSoort());
    baseElements.setOmschrijving(ZaakUtils.getTypeEnOmschrijving(zaak));
    baseElements.setStatus(toEnum(GbaRestZaakStatusType.values(), zaak.getStatus().getCode()));
    baseElements.setBron(zaak.getBron());
    baseElements.setLeverancier(zaak.getLeverancier());
    baseElements.setDatumIngang(zaak.getDatumIngang().getIntDate());
    baseElements.setDatumInvoer(zaak.getDatumTijdInvoer().getIntDate());
    baseElements.setTijdInvoer(zaak.getDatumTijdInvoer().getIntTime());
    baseElements.setLocatieInvoer(toTableRecord(
        zaak.getLocatieInvoer().getLocatie(),
        zaak.getLocatieInvoer().getOmschrijving()));
    baseElements.setGebruikerInvoer(toTableRecord(zaak.getIngevoerdDoor()));
  }

  private GbaRestZaakType toGbaRestZaakType(Zaak zaak) {
    if (ZaakType.VERHUIZING == zaak.getType()) {
      VerhuisAanvraag verhuisAanvraag = (VerhuisAanvraag) zaak;
      VerhuisType typeVerhuizing = verhuisAanvraag.getTypeVerhuizing();
      switch (typeVerhuizing) {
        case BINNENGEMEENTELIJK:
          return GbaRestZaakType.BINNENVERHUIZING;
        case INTERGEMEENTELIJK:
          return GbaRestZaakType.BUITENVERHUIZING;
        case EMIGRATIE:
          return GbaRestZaakType.EMIGRATIE;
        case HERVESTIGING:
          return GbaRestZaakType.HERVESTIGING;
        case ONBEKEND:
        default:
          throw new IllegalArgumentException("Unknown value: " + typeVerhuizing.getCode());
      }
    }
    return toEnum(GbaRestZaakType.values(), zaak.getType().getCode());
  }

  private void addZaakElements(Zaak zaak, GbaRestZaak restObj) {
    switch (zaak.getType()) {
      case VERHUIZING:
        restObj.setVerhuizing(getRestServices()
            .getVerhuizingService()
            .toGbaRestVerhuizing((VerhuisAanvraag) zaak));
        break;
      case GEBOORTE:
        restObj.setGeboorte(getRestServices()
            .getGeboorteService()
            .toGbaRestGeboorte((Dossier) zaak));
        break;
      case HUWELIJK_GPS_GEMEENTE:
        restObj.setHuwelijk(getRestServices()
            .getHuwelijkService()
            .toGbaRestHuwelijk((Dossier) zaak));
        break;
      case ERKENNING:
        restObj.setErkenning(getRestServices()
            .getErkenningService()
            .toGbaRestErkenning((Dossier) zaak));
        break;
      case OVERLIJDEN_IN_GEMEENTE:
      case LIJKVINDING:
        restObj.setOverlijden(getRestServices()
            .getOverlijdenService()
            .toGbaRestOverlijden((Dossier) zaak));
        break;
      case NAAMSKEUZE:
        restObj.setNaamskeuze(getRestServices()
            .getNaamskeuzeService()
            .toGbaRestNaamskeuze((Dossier) zaak));
        break;
      case UITTREKSEL:
      case VERSTREKKINGSBEPERKING:
      case NAAMGEBRUIK:
      case COVOG:
      case GPK:
      case REISDOCUMENT:
      case INHOUD_VERMIS:
      case RIJBEWIJS:
      case TERUGMELDING:
      case OVERLIJDEN_IN_BUITENLAND:
      case LEVENLOOS:
      case INDICATIE:
      case CORRESPONDENTIE:
      case GEGEVENSVERSTREKKING:
      case OMZETTING_GPS:
      case ONTBINDING_GEMEENTE:
      case INBOX:
      case ONDERZOEK:
      case REGISTRATION:
      case RISK_ANALYSIS:
      case PL_MUTATION:
        //throw new ProException(ERROR, zaak.getType() + " is nog niet uitgewerkt");
        break;

      case ONBEKEND:
      case LEGE_PERSOONLIJST:
        throw new ProException(ERROR, "Onbekende zaaktype:  " + zaak.getType());
    }
  }

  private void addExtraElements(Zaak zaak, GbaRestZaakAlgemeen restObj) {
    addIdentifications(zaak, restObj);
    addStatusses(zaak, restObj);
    addAttributes(zaak, restObj);
    addRelatedCases(zaak, restObj);
  }

  private void addIdentifications(Zaak zaak, GbaRestZaakAlgemeen restObj) {
    List<GbaRestZaakId> list = new ArrayList<>();
    for (ZaakIdentificatie zaakNr : zaak.getZaakHistorie().getIdentificaties().getNummers()) {
      GbaRestZaakId restZaakId = new GbaRestZaakId();
      restZaakId.setId(zaakNr.getExternId());
      restZaakId.setSysteem(zaakNr.getType());
      list.add(restZaakId);
    }
    restObj.setIds(list);
  }

  private void addStatusses(Zaak zaak, GbaRestZaakAlgemeen restObj) {
    List<ZaakStatus> statusses = zaak.getZaakHistorie().getStatusHistorie().getStatussen();
    int nr = statusses.size();
    List<GbaRestZaakStatus> list = new ArrayList<>();
    for (ZaakStatus status : statusses) {
      GbaRestZaakStatus restStatus = new GbaRestZaakStatus();
      restStatus.setVolgNr(nr--);
      restStatus.setType(toEnum(GbaRestZaakStatusType.values(), zaak.getStatus().getCode()));
      restStatus.setOpmerking(status.getOpmerking());
      restStatus.setInvoerDatum(status.getDatumTijdInvoer().getIntDate());
      restStatus.setInvoerTijd(status.getDatumTijdInvoer().getIntTime());
      restStatus.setInvoerGebruiker(toTableRecord(zaak.getIngevoerdDoor()));
      list.add(restStatus);
    }
    restObj.setStatussen(list);
  }

  private void addAttributes(Zaak zaak, GbaRestZaakAlgemeen restObj) {
    List<ZaakAttribuut> attributes = zaak.getZaakHistorie().getAttribuutHistorie().getAttributen();
    List<GbaRestZaakAttribuut> list = new ArrayList<>();
    for (ZaakAttribuut attr : attributes) {
      GbaRestZaakAttribuut restAttr = new GbaRestZaakAttribuut();
      restAttr.setWaarde(attr.getAttribuut());
      restAttr.setBestaat(true);
      list.add(restAttr);
    }
    restObj.setExtraAttributen(list);
  }

  private void addRelatedCases(Zaak zaak, GbaRestZaakAlgemeen restObj) {
    List<ZaakRelatie> relaties = zaak.getZaakHistorie().getRelaties().getRelaties();
    List<GbaRestZaakRelatie> list = new ArrayList<>();
    for (ZaakRelatie rel : relaties) {
      GbaRestZaakRelatie restRel = new GbaRestZaakRelatie();
      restRel.setZaakId(rel.getGerelateerdZaakId());
      restRel.setZaakType(toEnum(GbaRestZaakType.values(), rel.getGerelateerdZaakType().getCode()));
      list.add(restRel);
    }
    restObj.setGekoppeldeZaken(list);
  }

  private GbaRestZaakSleutel toGbaRestZaakSleutel(ZaakKey zaakKey) {
    GbaRestZaakSleutel sleutel = new GbaRestZaakSleutel();
    sleutel.setId(zaakKey.getZaakId());
    sleutel.setZaakType(toEnum(GbaRestZaakType.values(), zaakKey.getZaakType().getCode()));
    return sleutel;
  }

  private List<GbaRestZaak> toGbaRestZaken(List<Zaak> standaardZaken, boolean completeZaak) {
    GbaRestZaakService zaakService = getRestServices().getZaakService();
    return standaardZaken.stream()
        .map((Zaak zaak) -> completeZaak
            ? zaakService.toGbaRestCompleteZaak(zaak)
            : zaakService.toGbaRestBaseZaak(zaak))
        .collect(Collectors.toList());
  }

  public void addZaakIds(GbaRestZaak restZaak, Zaak zaak) {
    ZaakIdentificatieService zaakIdService = getServices().getZaakIdentificatieService();
    List<GbaRestZaakId> ids = restZaak.getAlgemeen().getIds();
    if (ids != null) {
      for (GbaRestZaakId restZaakId : ids) {
        ZaakIdentificatie identificatie = new ZaakIdentificatie();
        identificatie.setInternId(zaak.getZaakId());
        identificatie.setType(restZaakId.getSysteem());
        identificatie.setExternId(restZaakId.getId());
        identificatie.setZaakType(zaak.getType());
        zaakIdService.save(identificatie);
        zaak.getZaakHistorie().getIdentificaties().getNummers().add(identificatie);
      }
    }

    ZaakDmsService zaakDmsService = getServices().getZaakDmsService();
    if (zaakDmsService.isZakenDsmAan()) {
      zaakDmsService.genereerZaakId(zaak);
      getServices().getZakenService().getService(zaak).save(zaak);
    }
  }
}
