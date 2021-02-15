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

package nl.procura.gba.web.services.zaken.documenten.aanvragen;

import static nl.procura.gba.common.MiscUtils.copy;
import static nl.procura.gba.common.MiscUtils.to;
import static nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoonType.AANGEVER;
import static nl.procura.standard.Globalfunctions.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.openoffice.DocumentPL;
import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.GeheimDao;
import nl.procura.gba.jpa.personen.dao.UittAanvrDao;
import nl.procura.gba.jpa.personen.dao.ZaakDao;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.jpa.personen.db.Document;
import nl.procura.gba.jpa.personen.db.UittAanvr;
import nl.procura.gba.web.common.misc.ZakenList;
import nl.procura.gba.web.services.ServiceEvent;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Timer;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.gba.ple.relatieLijst.RelatieType;
import nl.procura.gba.web.services.zaken.algemeen.AbstractZaakContactService;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakService;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContact;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoon;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

public class DocumentZakenService extends AbstractZaakContactService<DocumentZaak>
    implements ZaakService<DocumentZaak> {

  public DocumentZakenService() {
    super("Documentzaken", ZaakType.UITTREKSEL);
  }

  @Override
  @Timer
  @ThrowException("Fout bij opvragen uittrekselaanvragen")
  public int getZakenCount(ZaakArgumenten zaakArgumenten) {
    return UittAanvrDao.findCount(getArgumentenToMap(zaakArgumenten));
  }

  @Override
  public DocumentZaak setVolledigeZaakExtra(DocumentZaak zaak) {
    DocumentZaak impl = to(zaak, DocumentZaak.class);
    for (DocumentZaakPersoon p : impl.getPersonen()) {
      if (p.getPersoon() == null) {
        p.setPersoon(new DocumentPL(findPL(p.getAnummer(), p.getBurgerServiceNummer())));
      }
    }

    return super.setVolledigeZaakExtra(zaak);
  }

  @Override
  public ZaakContact getContact(DocumentZaak zaak) {

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
  @ThrowException("Fout bij opvragen uittrekselaanvragen")
  public List<DocumentZaak> getMinimalZaken(ZaakArgumenten zaakArgumenten) {

    List<DocumentZaak> zaken = new ZakenList<>();

    for (UittAanvr a : UittAanvrDao.find(getArgumentenToMap(zaakArgumenten))) {

      DocumentZaak u = copy(a, DocumentZaak.class);
      u.setDoc(copy(a.getDocument(), DocumentRecord.class));
      u.setRelatieType(RelatieType.getType(along(a.getRelatie())));

      zaken.add(u);
    }

    return zaken;
  }

  @Override
  public Zaak getNewZaak() {
    return aanvullenZaak(new DocumentZaak());
  }

  @Override
  @ThrowException("Het is niet mogelijk om de zaak op te vragen")
  public DocumentZaak getStandardZaak(DocumentZaak zaak) {

    DocumentZaak impl = to(zaak, DocumentZaak.class);
    impl.getPersonen().clear();

    for (Object[] nrs : UittAanvrDao.findNrs(impl.getZaakId())) {

      AnrFieldValue anr = new AnrFieldValue(astr(nrs[0]));
      BsnFieldValue bsn = new BsnFieldValue(astr(nrs[1]));
      RelatieType type = RelatieType.getType(along(nrs[2]));

      impl.getPersonen().add(new DocumentZaakPersoon(anr, bsn, type));
    }

    return super.getStandardZaak(zaak);
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van zaak-ids")
  public List<ZaakKey> getZaakKeys(ZaakArgumenten zaakArgumenten) {
    return UittAanvrDao.findZaakKeys(getArgumentenToMap(zaakArgumenten));
  }

  @Override
  @Transactional
  @ThrowException("Fout bij het opslaan")
  public void save(DocumentZaak zaak) {

    DocumentZaak zaakImpl = getInstance(zaak, DocumentZaak.class);

    getZaakStatussen().setInitieleStatus(zaak);

    if (isSaved(zaakImpl)) {
      opslaanStandaardZaak(zaakImpl);
    } else {

      zaakImpl.setDocument(copy(zaakImpl.getDoc(), Document.class));
      for (DocumentZaakPersoon p : zaak.getPersonen()) {

        zaakImpl.setRelAnr(p.getAnummer().getStringValue());
        zaakImpl.setRelBsn(p.getBurgerServiceNummer().getBigDecimalValue());
        zaakImpl.setRelatie(toBigDecimal(p.getRelatieType().getCode()));

        zaak.setId(null); // Null maken, zodat de volgende zaak ook wordt opgeslagen
        opslaanStandaardZaak(zaak);
      }

      getServices().getZaakIdentificatieService().save(zaak);
      getServices().getKassaService().addToWinkelwagen(zaakImpl);
    }

    callListeners(ServiceEvent.CHANGE);
  }

  @Override
  @Transactional
  @ThrowException("Fout bij het wijzigingen van de status")
  public void updateStatus(DocumentZaak zaak, ZaakStatusType huidigeStatus, ZaakStatusType newStatus,
      String opmerking) {

    if (fil(zaak.getZaakId())) {

      getZaakStatussen().updateStatus(zaak, huidigeStatus, newStatus, opmerking);

      ConditionalMap map = new ConditionalMap();
      map.put(GeheimDao.ZAAK_ID, zaak.getZaakId());

      for (UittAanvr e : UittAanvrDao.find(map)) {
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
  public void delete(DocumentZaak zaak) {

    if (fil(zaak.getZaakId())) {

      removeEntities(UittAanvrDao.find(new ConditionalMap(UittAanvrDao.ZAAK_ID, zaak.getZaakId())));

      deleteZaakRelaties(zaak);
    }

    callListeners(ServiceEvent.CHANGE);
  }

  private ConditionalMap getArgumentenToMap(ZaakArgumenten zaakArgumenten) {

    ConditionalMap map = getAlgemeneArgumentenToMap(zaakArgumenten);

    if (!map.containsKey(ZaakDao.ZAAK_ID)) {
      Set<String> types = new HashSet<>();

      if (zaakArgumenten instanceof DocumentZaakArgumenten) {
        DocumentZaakArgumenten uittArgs = (DocumentZaakArgumenten) zaakArgumenten;

        for (DocumentType dt : uittArgs.getDocumentTypes()) {
          types.add(dt.getType());
        }
      } else {

        // Alleen uittreksels en correspondentie.
        // De rest is irrelevant, maar wordt wel opgeslagen.
        types.add(DocumentType.PL_UITTREKSEL.getType());
      }

      map.putSet(UittAanvrDao.TYPE, types);
    }

    return map;
  }
}
