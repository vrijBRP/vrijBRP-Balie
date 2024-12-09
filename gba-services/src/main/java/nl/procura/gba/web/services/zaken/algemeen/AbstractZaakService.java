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

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.pos;

import lombok.extern.slf4j.Slf4j;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.ZaakDao;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.ServiceEvent;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.zaken.algemeen.aantekening.AantekeningService;
import nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakAttribuutService;
import nl.procura.gba.web.services.zaken.algemeen.identificatie.ZaakIdentificatieService;
import nl.procura.gba.web.services.zaken.algemeen.status.ZaakStatusService;
import nl.procura.gba.web.services.zaken.algemeen.tasks.TaskService;
import nl.procura.gba.web.services.zaken.algemeen.zaakrelaties.ZaakRelatieService;
import nl.procura.gba.web.services.zaken.identiteit.Identificatie;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public abstract class AbstractZaakService<T extends Zaak> extends AbstractService {

  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractZaakService.class);
  private final ZaakType      zaakType;

  public AbstractZaakService(String name, ZaakType zaakType) {
    super(name);
    this.zaakType = zaakType;
  }

  /**
   * Alleen gegevens uit de database
   */
  @ThrowException("Het is niet mogelijk om de zaak op te vragen")
  public T getStandardZaak(T zaak) {
    LOGGER.debug("ZaakService - zoek standaard zaak");
    setZaakHistory(zaak);
    return zaak;
  }

  /**
   * De volledige zaak inclusief persoonsgegevens van de aanvrager
   */
  public T getCompleteZaak(T zaak) {
    try {
      T standaardZaak = getStandardZaak(zaak);
      LOGGER.debug("ZaakService - zoek volledige zaak");
      return setVolledigeZaakExtra(standaardZaak);
    } catch (RuntimeException e) {
      e.printStackTrace();
      throw new ProException(ERROR, "Het is niet mogelijk om de volledige zaak ({0}) op te vragen", zaak.getZaakId());
    }
  }

  public ZaakStatusService getZaakStatussen() {
    return getServices().getZaakStatusService();
  }

  public ZaakType getZaakType() {
    return zaakType;
  }

  /**
   * Algemene afleidingen.
   * Gegevens die bedoeld zijn voor bijvoorbeeld het afdrukken van documenten.
   */
  public T setVolledigeZaakExtra(T zaak) {

    zaak.setBasisPersoon(getBasisPersoon(zaak));
    zaak.setGemeente(getServices().getGemeenteService().getGemeente(zaak.getBasisPersoon()));

    if (getServices().isTestomgeving()) {
      ZaakUtils.check(zaak);
    }

    return zaak;
  }

  /**
   * Update zaak historie
   */
  public void setZaakHistory(T zaak) {
    ZaakHistorie historie = zaak.getZaakHistorie();
    if (historie != null) {
      zaak.setIdentificatie(getIdentificatie(zaak));
      getServices().getIdentificatieService().setIdentificatieByUitreiking(zaak);
      historie.setStatusHistorie(getServices().getZaakStatusService().getStatusHistorie(zaak));
      historie.setAantekeningHistorie(getServices().getAantekeningService().getZaakAantekeningen(zaak));
      historie.setAttribuutHistorie(getServices().getZaakAttribuutService().getZaakAttribuutHistorie(zaak));
      historie.setBehandelaarHistorie(getServices().getZaakAttribuutService().getZaakBehandelaarHistorie(zaak));
      historie.setIdentificaties(getServices().getZaakIdentificatieService().getIdentificaties(zaak));
      historie.setRelaties(getServices().getZaakRelatieService().getGerelateerdeZaakRelaties(zaak));


    }
  }

  public void updateStatus(T zaak, ZaakStatusType huidigeStatus, ZaakStatusType newStatus, String opmerking) {
    getZaakStatussen().updateStatus(zaak, huidigeStatus, newStatus, opmerking);
  }

  /**
   * Verwijderen van zaakstatussen
   */
  @Transactional
  @ThrowException("Fout bij het verwijderen van zaak-gerelateerde zaken")
  public void deleteZaakRelaties(T zaak) {
    if (fil(zaak.getZaakId())) {
      getZaakStatussen().delete(zaak);
      getAantekeningen().delete(zaak);
      getZaakIdentificaties().delete(zaak);
      getZaakAttributen().delete(zaak);
      getZaakRelaties().delete(zaak);
      getTasks().delete(zaak);

      log.info(String.format("Zaak: %s - %s verwijderd door gebruiker %s.",
          zaak.getType().getOms(),
          zaak.getZaakId(),
          getServices().getGebruiker().getGebruikersnaam()));
    }

    callListeners(ServiceEvent.CHANGE);
  }

  /**
   * Vul de zaak aan met standaard informatie
   */
  protected T aanvullenZaak(T zaak) {
    return ZaakUtils.newZaak(zaak, getServices());
  }

  protected ConditionalMap getAlgemeneArgumentenToMap(ZaakArgumenten zaakArgumenten) {

    ConditionalMap map = new ConditionalMap();

    if (!zaakArgumenten.isAll()) {
      map.putSet(ZaakDao.ZAAK_ID, zaakArgumenten.getZaakIds());
      map.putString(ZaakDao.ZAAK_ID_TYPE, zaakArgumenten.getZaakIdType().getCode());
      map.putString(ZaakDao.NIEUWE_ZAAK, Boolean.valueOf(zaakArgumenten.isZonderBehandelaar()).toString());

      map.putPosString(ZaakDao.BSN, astr(zaakArgumenten.getBsn()));
      map.putPosString(ZaakDao.ANR, astr(zaakArgumenten.getAnr()));

      map.putLong(ZaakDao.D_INVOER_VANAF, zaakArgumenten.getdInvoerVanaf());
      map.putLong(ZaakDao.D_INVOER_TM, zaakArgumenten.getdInvoerTm());

      map.putLong(ZaakDao.D_INGANG_VANAF, zaakArgumenten.getdIngangVanaf());
      map.putLong(ZaakDao.D_INGANG_TM, zaakArgumenten.getdIngangTm());

      map.putLong(ZaakDao.D_AFHAAL_VANAF, zaakArgumenten.getDAfhaalVanaf());
      map.putLong(ZaakDao.D_AFHAAL_TM, zaakArgumenten.getDAfhaalTm());

      map.putLong(ZaakDao.D_MUT_VANAF, zaakArgumenten.getdMutatieVanaf());
      map.putLong(ZaakDao.D_MUT_TM, zaakArgumenten.getdMutatieTm());

      map.putList(ZaakDao.IND_VERWERKT, zaakArgumenten.getStatusCodes());
      map.putList(ZaakDao.NIET_IND_VERWERKT, zaakArgumenten.getNegeerStatusCodes());

      map.putLong(ZaakDao.C_USR, zaakArgumenten.getCodeGebruiker());
      map.putLong(ZaakDao.C_USR_TOEK, zaakArgumenten.getCodeBehandelaar());
      map.putLong(ZaakDao.C_USR_FAV, zaakArgumenten.getCodeGebruikerFavoriet());
      map.putLong(ZaakDao.C_PROFILE, zaakArgumenten.getCodeProfiel());
      map.putLong(ZaakDao.D_END_TERMIJN, zaakArgumenten.getdEndTerm());
      map.putLong(ZaakDao.MAX_RESULTS, zaakArgumenten.getMax());
      map.putList(ZaakDao.ATTRIBUTEN, zaakArgumenten.getAttributen());
      map.putList(ZaakDao.BRONNEN, zaakArgumenten.getBronnen());
      map.putList(ZaakDao.LEVERANCIERS, zaakArgumenten.getLeveranciers());
      map.putList(ZaakDao.ONTB_ATTRIBUTEN, zaakArgumenten.getOntbrekendeAttributen());
    }

    return map;
  }

  /**
   * Haalt de basispersoon op voor in de zaak
   */
  protected BasePLExt getBasisPersoon(Zaak zaak) {

    if (zaak.getBasisPersoon() == null) {

      BsnFieldValue bsn = zaak.getBurgerServiceNummer();
      AnrFieldValue anr = zaak.getAnummer();

      String nummer = "";
      if (bsn != null && pos(bsn.getValue())) {
        nummer = bsn.getStringValue();
      } else if (anr != null && pos(anr.getValue())) {
        nummer = anr.getStringValue();
      }

      if (pos(nummer)) {
        return getServices().getPersonenWsService().getPersoonslijst(nummer);
      }
    }

    return zaak.getBasisPersoon();
  }

  /**
   * Haalt de basispersoon op voor in de zaak
   */
  protected Identificatie getIdentificatie(Zaak zaak) {
    return getServices().getIdentificatieService().getIdentificatie(zaak);
  }

  @Transactional
  protected void opslaanStandaardZaak(Zaak zaak) {
    saveEntity(zaak);
    getServices().getZaakIdentificatieService().save(zaak);
  }

  private AantekeningService getAantekeningen() {
    return getServices().getAantekeningService();
  }

  private ZaakAttribuutService getZaakAttributen() {
    return getServices().getZaakAttribuutService();
  }

  private ZaakIdentificatieService getZaakIdentificaties() {
    return getServices().getZaakIdentificatieService();
  }

  private ZaakRelatieService getZaakRelaties() {
    return getServices().getZaakRelatieService();
  }

  private TaskService getTasks() {
    return getServices().getTaskService();
  }
}
