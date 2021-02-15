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

package nl.procura.gba.web.services.zaken.gv;

import static nl.procura.gba.common.MiscUtils.copyList;
import static nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoonType.AANGEVER;
import static nl.procura.gba.web.services.zaken.algemeen.koppelenumeratie.KoppelEnumeratieType.*;
import static nl.procura.standard.Globalfunctions.fil;

import java.util.List;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.GvDao;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.jpa.personen.db.Gv;
import nl.procura.gba.web.common.misc.ZakenList;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.services.ServiceEvent;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Timer;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.zaken.algemeen.*;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContact;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoon;
import nl.procura.gba.web.services.zaken.algemeen.koppelenumeratie.KoppelEnumeratieType;
import nl.procura.gba.web.services.zaken.algemeen.status.ZaakStatusService;

public class GegevensVerstrekkingService extends AbstractZaakContactService<GvAanvraag>
    implements ZaakService<GvAanvraag> {

  public GegevensVerstrekkingService() {
    super("Gv", ZaakType.GEGEVENSVERSTREKKING);
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van de gvaanvragen")
  public int getZakenCount(ZaakArgumenten zaakArgumenten) {
    return GvDao.findCount(getArgumentenToMap(zaakArgumenten));
  }

  @Override
  public ZaakContact getContact(GvAanvraag zaak) {

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
  @Timer
  @ThrowException("Fout bij het zoeken van de gv aanvragen")
  public List<GvAanvraag> getMinimalZaken(ZaakArgumenten zaakArgumenten) {
    return new ZakenList(copyList(GvDao.find(getArgumentenToMap(zaakArgumenten)), GvAanvraag.class));
  }

  @Override
  public Zaak getNewZaak() {
    return aanvullenZaak(new GvAanvraag());
  }

  @Override
  public GvAanvraag getStandardZaak(GvAanvraag zaak) {
    GvAanvraag zaakImpl = getInstance(zaak, GvAanvraag.class);
    zaak.getProcessen().getProcessen().addAll(copyList(zaakImpl.getGvProces(), GvAanvraagProces.class));
    return super.getStandardZaak(zaak);
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van zaak-ids")
  public List<ZaakKey> getZaakKeys(ZaakArgumenten zaakArgumenten) {
    return GvDao.findZaakKeys(getArgumentenToMap(zaakArgumenten));
  }

  /**
   * Opslaan van de gvaanvraag
   */
  @Override
  @Transactional
  @ThrowException("Fout bij het opslaan van het record")
  public void save(GvAanvraag zaak) {

    ZaakStatusService zaakStatussen = getZaakStatussen();

    boolean isNietToekennen = KoppelEnumeratieType.TK_NEE.is(zaak.getToekenningType());
    boolean isNuToekennen = KoppelEnumeratieType.TK_JA.is(zaak.getToekenningType());

    if (isNietToekennen) { // Bij de aanvraag
      zaakStatussen.setInitieleStatus(zaak, ZaakStatusType.GEWEIGERD, "Niet toegekend");

    } else if (isNuToekennen) { // Bij de aanvraag
      zaakStatussen.setInitieleStatus(zaak, ZaakStatusType.VERWERKT, "Verstrekt");

    } else {
      zaakStatussen.setInitieleStatus(zaak, ZaakStatusType.INBEHANDELING, "");
    }

    opslaanStandaardZaak(zaak);

    callListeners(ServiceEvent.CHANGE);
  }

  public void saveProces(GvAanvraag zaak, GvAanvraagProces zaakProces) {

    if (zaakProces != null) {
      GvAanvraagProces gvProcesImpl = zaakProces;

      if (!zaakProces.isStored()) {
        gvProcesImpl.setIngevoerdDoor(new UsrFieldValue(getServices().getGebruiker()));
        gvProcesImpl.setDatumTijdInvoer(new DateTime());
        gvProcesImpl.setGv(findEntity(Gv.class, zaak.getCGv()));
      }

      saveEntity(gvProcesImpl);

      boolean isToekenningVoorwaardelijk = TK_JA_VOORWAARDELIJK.is(zaak.getToekenningType());
      boolean isKenbaarMaken = PA_KENBAAR_MAKEN.is(zaakProces.getProcesActieType());
      boolean isNietVerstrekken = PA_NIET_VERSTREKKEN.is(zaakProces.getProcesActieType());
      boolean isNuVerstrekken = PA_NU_VERSTREKKEN.is(zaakProces.getProcesActieType());
      boolean isNaTermijnVerstrekken = PA_NA_TERMIJN_VERSTREKKEN.is(zaakProces.getProcesActieType());

      ZaakStatusService zaakStatussen = getZaakStatussen();

      if (isToekenningVoorwaardelijk) { // Bij de behandeling na de kenbaarheidstermijn
        if (isKenbaarMaken) {

          zaakStatussen.updateStatus(zaak, ZaakStatusType.INBEHANDELING, "");
        } else if (isNietVerstrekken) {

          zaakStatussen.updateStatus(zaak, ZaakStatusType.GEWEIGERD, "Niet verstrekken");
        } else if (isNaTermijnVerstrekken) {

          zaakStatussen.updateStatus(zaak, ZaakStatusType.INBEHANDELING, "Verstrekken na termijn");
        } else if (isNuVerstrekken) {

          zaakStatussen.updateStatus(zaak, ZaakStatusType.VERWERKT, "Wel verstrekken");
        } else {

          zaakStatussen.updateStatus(zaak, ZaakStatusUtils.getInitieleStatus(this, zaak), "");
        }
      }
    }
  }

  /**
   * Verwijderen van de gvaanvraag
   */
  @Override
  @Transactional
  @ThrowException("Fout bij het verwijderen van het record")
  public void delete(GvAanvraag zaak) {

    if (fil(zaak.getZaakId())) {

      ConditionalMap map = new ConditionalMap();
      map.put(GvDao.ZAAK_ID, zaak.getZaakId());
      map.put(GvDao.MAX_CORRECT_RESULTS, 1);

      removeEntities(GvDao.find(map));

      deleteZaakRelaties(zaak);
    }

    callListeners(ServiceEvent.CHANGE);
  }

  private ConditionalMap getArgumentenToMap(ZaakArgumenten zaakArgumenten) {
    return getAlgemeneArgumentenToMap(zaakArgumenten);
  }
}
