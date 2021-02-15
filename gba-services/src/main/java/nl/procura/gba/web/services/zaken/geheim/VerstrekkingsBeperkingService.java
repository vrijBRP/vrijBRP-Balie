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

package nl.procura.gba.web.services.zaken.geheim;

import static nl.procura.gba.common.MiscUtils.copyList;
import static nl.procura.gba.common.MiscUtils.to;
import static nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoonType.AANGEVER;
import static nl.procura.standard.Globalfunctions.*;

import java.util.List;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.openoffice.DocumentPL;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.GeheimDao;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.jpa.personen.db.Geheimhouding;
import nl.procura.gba.web.common.misc.ZakenList;
import nl.procura.gba.web.services.ServiceEvent;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Timer;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.zaken.algemeen.AbstractZaakContactService;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakService;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContact;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoon;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

public class VerstrekkingsBeperkingService extends AbstractZaakContactService<GeheimAanvraag>
    implements ZaakService<GeheimAanvraag> {

  public VerstrekkingsBeperkingService() {
    super("Geheim", ZaakType.VERSTREKKINGSBEPERKING);
  }

  @Override
  @ThrowException("Fout bij het zoeken van de geheimhoudingen")
  @Timer
  public int getZakenCount(ZaakArgumenten zaakArgumenten) {
    return GeheimDao.findCount(getArgumentenToMap(zaakArgumenten));
  }

  @Override
  public GeheimAanvraag setVolledigeZaakExtra(GeheimAanvraag zaak) {

    GeheimAanvraag impl = to(zaak, GeheimAanvraag.class);

    for (GeheimPersoon p : impl.getPersonen()) {
      if (p.getPersoon() == null) {
        p.setPersoon(new DocumentPL(findPL(p.getAnummer(), p.getBurgerServiceNummer())));
      }
    }

    return super.setVolledigeZaakExtra(zaak);
  }

  @Override
  public ZaakContact getContact(GeheimAanvraag zaak) {

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
  @ThrowException("Fout bij het zoeken van zaken")
  public List<GeheimAanvraag> getMinimalZaken(ZaakArgumenten zaakArgumenten) {
    return new ZakenList(copyList(GeheimDao.find(getArgumentenToMap(zaakArgumenten)), GeheimAanvraag.class));
  }

  @Override
  public Zaak getNewZaak() {
    return aanvullenZaak(new GeheimAanvraag());
  }

  @Override
  @ThrowException("Het is niet mogelijk om de zaak op te vragen")
  public GeheimAanvraag getStandardZaak(GeheimAanvraag zaak) {

    GeheimAanvraag impl = to(zaak, GeheimAanvraag.class);
    impl.getPersonen().clear();

    for (Object[] nrs : GeheimDao.findNrs(impl.getZaakId())) {

      AnrFieldValue anr = new AnrFieldValue(astr(nrs[0]));
      BsnFieldValue bsn = new BsnFieldValue(astr(nrs[1]));

      impl.getPersonen().add(new GeheimPersoon(anr, bsn));
    }

    return super.getStandardZaak(zaak);
  }

  @Override
  @ThrowException("Fout bij het zoeken van zaak-ids")
  @Timer
  public List<ZaakKey> getZaakKeys(ZaakArgumenten zaakArgumenten) {
    return GeheimDao.findZaakKeys(getArgumentenToMap(zaakArgumenten));
  }

  @Override
  @Transactional
  @ThrowException("Fout bij het opslaan van de verstrekkingsbeperking")
  public void save(GeheimAanvraag zaak) {

    if (getServices().getPersonenWsService().getHuidige().getDatasource() == PLEDatasource.GBAV) {
      getZaakStatussen().setInitieleStatus(zaak, ZaakStatusType.WACHTKAMER, " Niet-inwoner");
    } else {
      getZaakStatussen().setInitieleStatus(zaak);
    }

    if (isSaved(zaak)) {
      opslaanStandaardZaak(zaak);
    } else {

      for (GeheimPersoon p : zaak.getPersonen()) {

        GeheimAanvraag gI = zaak;
        gI.setAnr(p.getAnummer().getStringValue());
        gI.setBsn(p.getBurgerServiceNummer().getBigDecimalValue());

        zaak.setId(null); // Null maken, zodat de volgende zaak ook wordt opgeslagen
        opslaanStandaardZaak(zaak);
      }
    }

    callListeners(ServiceEvent.CHANGE);
  }

  /**
   * Status wijzigen van de verhuisaanvraag
   */
  @Override
  @Transactional
  @ThrowException("Fout bij het wijzigingen van de status")
  public void updateStatus(GeheimAanvraag zaak, ZaakStatusType huidigeStatus, ZaakStatusType newStatus,
      String opmerking) {

    if (fil(zaak.getZaakId())) {
      super.updateStatus(zaak, huidigeStatus, newStatus, opmerking);

      ConditionalMap map = new ConditionalMap();
      map.put(GeheimDao.ZAAK_ID, zaak.getZaakId());

      for (Geheimhouding e : GeheimDao.find(map)) {
        e.setIndVerwerkt(toBigDecimal(newStatus.getCode()));
        saveEntity(e);
      }
    }

    callListeners(ServiceEvent.CHANGE);
  }

  /**
   * Verwijderen van de verhuisaanvraag
   */
  @Override
  @Transactional
  @ThrowException("Fout bij het verwijderen")
  public void delete(GeheimAanvraag zaak) {

    if (fil(zaak.getZaakId())) {

      removeEntities(GeheimDao.find(new ConditionalMap(GeheimDao.ZAAK_ID, zaak.getZaakId())));

      deleteZaakRelaties(zaak);
    }

    callListeners(ServiceEvent.CHANGE);
  }

  private ConditionalMap getArgumentenToMap(ZaakArgumenten zaakArgumenten) {
    return getAlgemeneArgumentenToMap(zaakArgumenten);
  }
}
