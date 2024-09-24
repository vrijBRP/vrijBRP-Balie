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

package nl.procura.gba.web.services.gba.presentievraag;

import static java.util.stream.Collectors.toList;
import static nl.procura.gba.common.MiscUtils.copy;
import static nl.procura.gba.common.MiscUtils.copyList;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.PRESENTIE_VRAAG_ENDPOINT;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;

import java.util.ArrayList;
import java.util.List;

import nl.bprbzk.bcgba.v14.AfzenderDE;
import nl.procura.bcgba.v14.BcGbaActionTemplate;
import nl.procura.bcgba.v14.actions.ActionMatchIdenGeg;
import nl.procura.bcgba.v14.actions.IDGegevens;
import nl.procura.bcgba.v14.misc.BcGbaVraagbericht;
import nl.procura.bcgba.v14.soap.BcGbaSoapHandler;
import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.jpa.personen.dao.PresentievraagDao;
import nl.procura.gba.jpa.personen.db.PresVraag;
import nl.procura.gba.web.common.misc.GbaLogger;
import nl.procura.gba.web.common.misc.GbaSorter;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.gba.templates.GbaTemplateService;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;
import nl.procura.proweb.rest.utils.JsonUtils;
import nl.procura.commons.core.exceptions.ProException;

public class PresentievraagService extends GbaTemplateService {

  public PresentievraagService() {
    super("Presentievragen");
  }

  public Presentievraag getNew(PresentievraagAntwoord antwoord) {
    final Presentievraag presentievraag = new Presentievraag();

    // Algemene
    final Gebruiker gebruiker = getServices().getGebruiker();
    final Locatie locatie = gebruiker.heeftLocatie() ? gebruiker.getLocatie() : Locatie.getDefault();
    presentievraag.setIngevoerdDoor(new UsrFieldValue(gebruiker));
    presentievraag.setLocatieInvoer(locatie);
    presentievraag.setDatumTijdInvoer(new DateTime());

    // Presentievraag json
    presentievraag.setPresentievraagVersie(PresentievraagVersie.VERSIE_1_4);
    presentievraag.setAntwoord(JsonUtils.toJson(antwoord).trim());

    return presentievraag;
  }

  public ActionMatchIdenGeg getActionMatchIdenGeg(BcGbaVraagbericht vraagbericht,
      String instantie, IDGegevens idGegevens) {
    return new ActionMatchIdenGeg(vraagbericht, instantie, getAfzender(), idGegevens);
  }

  @ThrowException("Fout bij ophalen presentievragen")
  public List<Presentievraag> getPresenceQuestionsByZaakId(String zaakId) {
    List<Presentievraag> presentievragen = copyList(PresentievraagDao.findByCode(zaakId), Presentievraag.class);
    presentievragen.sort(GbaSorter.getByDatumTijdInvoer());
    presentievragen.forEach(this::addDescription);
    return presentievragen;
  }

  public Presentievraag getPresenceQuestionById(Long presentievragenID) {
    return addDescription(copy(PresentievraagDao.findById(presentievragenID), Presentievraag.class));
  }

  public List<Presentievraag> getPresenceQuestionsByPerson(DossierPersoon persoon) {
    List<Presentievraag> presentievragen = new ArrayList<>(persoon.getPresentievragen())
        .stream()
        .map(PresVraag::getcPresentievraag)
        .collect(toList())
        .stream()
        .map(this::getPresenceQuestionById)
        .collect(toList());

    presentievragen.sort(GbaSorter.getByDatumTijdInvoer());
    presentievragen.forEach(this::addDescription);
    return presentievragen;
  }

  public List<Presentievraag> getPresenceQuestionsByArguments(PresentievraagZoekargumenten searchArguments) {
    final List<PresVraag> presVragen = PresentievraagDao.find(getArgumentenToMap(searchArguments));
    final List<Presentievraag> presentievragen = copyList(presVragen, Presentievraag.class);
    presentievragen.sort(GbaSorter.getByDatumTijdInvoer());
    presentievragen.forEach(this::addDescription);
    return presentievragen;
  }

  @Transactional
  @ThrowException("Fout bij opslaan presentievraag")
  public void save(Presentievraag presentievraag) {
    saveEntity(presentievraag);
  }

  @Transactional
  @ThrowException("Fout bij opslaan presentievraag")
  public void delete(Presentievraag presentievraag) {
    removeEntity(presentievraag);
  }

  @Transactional
  @ThrowException("Fout bij verwijderen presentievragen")
  public void delete(String zaakId) {
    removeEntities(getPresenceQuestionsByZaakId(zaakId));
  }

  @ThrowException("Er is een fout opgetreden bij de presentievraag")
  public void zoek(BcGbaActionTemplate actie) {

    final String endpoint = getProxyUrl(PRESENTIE_VRAAG_ENDPOINT, true);
    actie.setSoapHandler(new BcGbaSoapHandler(endpoint));

    try {
      actie.send();
    } finally {
      GbaLogger.log("presentievraag", actie.getSoapHandler());
    }
  }

  private AfzenderDE getAfzender() {
    final AfzenderDE afzender = new AfzenderDE();
    afzender.setAfzender(getCodeGemeente());
    afzender.setBerichtNr("1");
    return afzender;
  }

  private ConditionalMap getArgumentenToMap(PresentievraagZoekargumenten zoekArgumenten) {

    if (!zoekArgumenten.isGevuld()) {
      throw new ProException(WARNING, "Geef minimaal een periode in");
    }

    final ConditionalMap map = new ConditionalMap();
    map.putLong(PresentievraagDao.D_INVOER_VANAF, zoekArgumenten.getDatumInvoerVanaf());
    map.putLong(PresentievraagDao.D_INVOER_TM, zoekArgumenten.getDatumInvoerTm());
    map.putString(PresentievraagDao.ANTWOORD, zoekArgumenten.getInhoudBericht());

    return map;
  }

  // Zaakomschrijving erbij zoeken
  private String getDescription(Presentievraag presentievraag) {
    if (fil(presentievraag.getZaakId())) {
      final ZaakArgumenten z = new ZaakArgumenten(presentievraag.getZaakId());
      for (final Zaak zaak : Services.getInstance().getZakenService().getMinimaleZaken(z)) {
        return ZaakUtils.getTypeEnOmschrijving(zaak);
      }
    }
    return "Niet van toepassing";
  }

  private Presentievraag addDescription(Presentievraag presentievraag) {
    presentievraag.setZaakOmschrijving(getDescription(presentievraag));
    return presentievraag;
  }

  private String getCodeGemeente() {
    return getParm(ParameterConstant.GEMEENTE_CODES);
  }
}
