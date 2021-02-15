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

package nl.procura.gba.web.services.bs.huwelijk;

import static java.util.Collections.singletonList;
import static nl.procura.gba.common.MiscUtils.copy;
import static nl.procura.gba.common.MiscUtils.copyList;
import static nl.procura.gba.web.common.tables.GbaTables.*;
import static nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoonType.*;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.common.UniqueList;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.jpa.personen.db.HuwAmbt;
import nl.procura.gba.jpa.personen.db.HuwLocatie;
import nl.procura.gba.jpa.personen.db.HuwLocatieOptie;
import nl.procura.gba.web.common.tables.GbaTables;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Timer;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.gba.basistabellen.huwelijksambtenaar.HuwelijksAmbtenaar;
import nl.procura.gba.web.services.gba.basistabellen.huwelijkslocatie.HuwelijksLocatie;
import nl.procura.gba.web.services.gba.basistabellen.huwelijkslocatie.HuwelijksLocatieOptieType;
import nl.procura.gba.web.services.gba.basistabellen.huwelijkslocatieOptie.HuwelijksLocatieOptie;
import nl.procura.gba.web.services.interfaces.GeldigheidStatus;
import nl.procura.gba.web.services.zaken.algemeen.*;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContact;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoon;
import nl.procura.gba.web.services.zaken.contact.Contactgegeven;
import nl.procura.gba.web.services.zaken.contact.ContactgegevensService;
import nl.procura.gba.web.services.zaken.contact.PlContactgegeven;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.validation.Bsn;

public class HuwelijkService extends AbstractZaakContactService<Dossier> implements ZaakService<Dossier> {

  public HuwelijkService() {
    super("Huwelijk / GPS", ZaakType.HUWELIJK_GPS_GEMEENTE);
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van huwelijkszaken")
  public int getZakenCount(ZaakArgumenten zaakArgumenten) {
    return getServices().getDossierService().getZakenCount(new ZaakArgumenten(zaakArgumenten, getZaakType()));
  }

  @Override
  public ZaakContact getContact(Dossier zaak) {
    ZaakContact contact = new ZaakContact();
    DossierHuwelijk huwelijk = (DossierHuwelijk) zaak.getZaakDossier();
    contact.add(getServices().getDossierService().getContactPersoon(PARTNER_1, huwelijk.getPartner1()));
    contact.add(getServices().getDossierService().getContactPersoon(PARTNER_2, huwelijk.getPartner2()));
    contact.add(getHuwelijksambtenaar(huwelijk));
    return contact;
  }

  @ThrowException("Fout bij ophalen huwelijkslocatie optie")
  public List<DossierHuwelijkOptie> getDossierHuwelijksOpties(DossierHuwelijk dossier) {

    List<DossierHuwelijkOptie> set = new UniqueList<>();
    for (HuwelijksLocatieOptie optie : getHuwelijksLocatieOpties(dossier.getHuwelijksLocatie())) {
      set.add(getHuwOptie(dossier, optie));
    }

    return set;
  }

  /**
   * Vervangt de dossierpersoongegevens van ambtenaar3 met die uit de basistabel
   */
  public HuwelijksAmbtenaar getHuwelijksambtenaar(BsnFieldValue bsn) {

    for (HuwelijksAmbtenaar huw : getHuwelijksAmbtenaren(GeldigheidStatus.ALLES)) {

      String bsnTabel = bsn.getStringValue();
      String bsnAmbtenaar = huw.getBurgerServiceNummer().getStringValue();

      if (new Bsn(bsnTabel).eq(bsnAmbtenaar)) {
        return huw;
      }
    }

    return null;
  }

  @ThrowException("Fout bij ophalen huwelijksambtenaar")
  public List<HuwelijksAmbtenaar> getHuwelijksAmbtenaren(GeldigheidStatus status) {
    List<HuwelijksAmbtenaar> list = new ArrayList<>();
    for (HuwAmbt huwAmbt : findEntity(new HuwAmbt())) {
      HuwelijksAmbtenaar huwAmbtImpl = copy(huwAmbt, HuwelijksAmbtenaar.class);
      if (huwAmbtImpl.getGeldigheidStatus().is(status)) {
        list.add(huwAmbtImpl);
      }
    }
    return list;
  }

  @ThrowException("Fout bij ophalen huwelijkslocatie optie")
  public List<HuwelijksLocatieOptie> getHuwelijksLocatieOpties() {
    return copyList(findEntity(new HuwLocatieOptie()), HuwelijksLocatieOptie.class);
  }

  @ThrowException("Fout bij ophalen huwelijkslocatie")
  public List<HuwelijksLocatie> getHuwelijksLocaties(GeldigheidStatus status) {

    List<HuwelijksLocatie> list = new ArrayList<>();
    for (HuwLocatie huwLoc : findEntity(new HuwLocatie())) {
      if (huwLoc.getCHuwLocatie() > 0) {
        HuwelijksLocatie locatieImpl = copy(huwLoc, HuwelijksLocatie.class);
        if (locatieImpl.getGeldigheidStatus().is(status)) {
          locatieImpl.getContactpersoon().setLand(GbaTables.LAND.get(locatieImpl.getcLand()));
          list.add(locatieImpl);
        }
      }
    }

    return list;
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van huwelijkszaken")
  public List<Dossier> getMinimalZaken(ZaakArgumenten zaakArgumenten) {
    return getServices().getDossierService().getMinimalZaken(new ZaakArgumenten(zaakArgumenten, getZaakType()));
  }

  @Override
  public Zaak getNewZaak() {
    return aanvullenZaak(new DossierHuwelijk().getDossier());
  }

  @Override
  @ThrowException("Het is niet mogelijk om de zaak op te vragen")
  public Dossier getStandardZaak(Dossier zaak) {

    DossierHuwelijk zaakDossier = getServices().getDossierService().getZaakDossier(zaak, DossierHuwelijk.class);
    zaakDossier.setTitelPartner1(TITEL.get(zaakDossier.getP1Titel()));
    zaakDossier.setTitelPartner2(TITEL.get(zaakDossier.getP2Titel()));
    zaakDossier.setRechtPartner1(LAND.get(zaakDossier.getP1Recht()));
    zaakDossier.setRechtPartner2(LAND.get(zaakDossier.getP2Recht()));
    zaakDossier.setHuwelijksGemeente(PLAATS.get(zaakDossier.getcHuwGem()));

    if (zaakDossier.getHuwLocatie() != null) {
      zaakDossier.setHuwelijksLocatie(copy(zaakDossier.getHuwLocatie(), HuwelijksLocatie.class));
    }

    zaakDossier.getOpties().addAll(copyList(zaakDossier.getDossHuwOpties(), DossierHuwelijkOptie.class));

    getServices().getDossierService().getOverigDossier(zaak);

    return super.getStandardZaak(zaak);
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van zaak-ids")
  public List<ZaakKey> getZaakKeys(ZaakArgumenten zaakArgumenten) {
    return getServices().getDossierService().getZaakKeys(new ZaakArgumenten(zaakArgumenten, getZaakType()));
  }

  @Override
  @Transactional
  @ThrowException("Fout bij opslaan van het dossier")
  public void save(Dossier zaak) {

    DossierHuwelijk zaakDossier = ZaakUtils.newZaakDossier(zaak, getServices());

    zaak.setDescr(getDescription(zaakDossier));
    getServices().getDossierService()
        .saveDossier(zaak)
        .savePersonen(zaak)
        .saveVereisten(zaak)
        .saveAktes(zaak);

    opslaanHuwelijksOpties(zaakDossier);

    zaakDossier.setHuwelijksGemeente(PLAATS.get(getServices().getGebruiker().getGemeenteCode()));

    getServices().getDossierService().saveZaakDossier(zaakDossier);
  }

  private static String getDescription(DossierHuwelijk zaakDossier) {
    List<String> elements = new ArrayList<>();
    addIfNotEmpty(elements, zaakDossier.getPartner1().getNaam().getVoorv_gesl());
    addIfNotEmpty(elements, zaakDossier.getPartner2().getNaam().getVoorv_gesl());
    zaakDossier.getDossier().getAktes()
        .forEach(akte -> addIfNotEmpty(elements, akte.getDescription()));
    return String.join(" / ", elements);
  }

  private static void addIfNotEmpty(List<String> list, String element) {
    if (!element.isEmpty()) {
      list.add(element);
    }
  }

  @Transactional
  @ThrowException("Fout bij opslaan huwelijksambtenaar")
  public void save(HuwelijksAmbtenaar ambtenaar) {
    saveEntity(ambtenaar);
  }

  @Transactional
  @ThrowException("Fout bij opslaan huwelijkslocatie")
  public void save(HuwelijksLocatie locatie) {

    saveEntity(locatie);

    HuwLocatie loc = findEntity(HuwLocatie.class, locatie.getCodeHuwelijksLocatie());

    for (HuwLocatieOptie o : loc.getHuwLocatieOpties()) {
      o.getHuwLocaties().remove(loc);
      saveEntity(o);
    }

    for (HuwelijksLocatieOptie o : locatie.getOpties()) {

      HuwLocatieOptie opt = findEntity(HuwLocatieOptie.class, o.getCodeHuwelijksLocatieOptie());
      opt.getHuwLocaties().add(findEntity(HuwLocatie.class, locatie.getCodeHuwelijksLocatie()));
      saveEntity(opt);
    }
  }

  @Transactional
  @ThrowException("Fout bij opslaan huwelijkslocatie optie")
  public void save(HuwelijksLocatieOptie optie) {

    if (optie.getOptieType() != HuwelijksLocatieOptieType.NUMBER) {
      optie.setMin(0);
      optie.setMax(0);
    }

    saveEntity(optie);
  }

  @Override
  @Transactional
  public void delete(Dossier zaak) {
    getServices().getDossierService().deleteDossier(zaak);
  }

  @Transactional
  @ThrowException("Fout bij verwijderen huwelijksambtenaar")
  public void delete(HuwelijksAmbtenaar ambtenaar) {
    removeEntity(ambtenaar);
  }

  @Transactional
  @ThrowException("Fout bij verwijderen huwelijkslocatie")
  public void delete(HuwelijksLocatie locatie) {
    removeEntity(locatie);
  }

  @Transactional
  @ThrowException("Fout bij verwijderen huwelijkslocatie optie")
  public void delete(HuwelijksLocatieOptie optie) {
    removeEntity(optie);
  }

  @Transactional
  @ThrowException("Fout bij verwijderen huwelijkslocatie optie")
  public void delete(DossierHuwelijk dossierHuwelijk, DossierHuwelijkOptie optie) {
    dossierHuwelijk.getDossHuwOpties().remove(optie);
    dossierHuwelijk.getOpties().remove(optie);
    removeEntity(optie);
  }

  @Transactional
  public void deleteGetuigen(DossierHuwelijk huwelijk) {
    huwelijk.getDossier().getPersonen(DossierPersoonType.GETUIGE)
        .forEach(dossierGetuige -> deleteGetuige(huwelijk, dossierGetuige));
  }

  @Transactional
  public void deleteGetuige(DossierHuwelijk dossierHuwelijk, DossierPersoon persoon) {
    if (persoon.getCode() != null) {
      dossierHuwelijk.getDossier().verwijderPersoon(persoon);
      removeEntity(persoon);
    }
  }

  @Transactional
  public void resetLocatie(DossierHuwelijk huwelijk) {
    new ArrayList<>(huwelijk.getOpties()).forEach(optie -> delete(huwelijk, optie));
    huwelijk.setHuwelijksLocatie(null);
  }

  /**
   * Vervangt de dossierpersoongegevens van ambtenaar3 met die uit de basistabel
   */
  private ZaakContactpersoon getHuwelijksambtenaar(DossierHuwelijk huwelijk) {
    DossierPersoon ambtenaar = huwelijk.getAmbtenaar3();
    ZaakContactpersoon contactpersoon = null;
    if (ambtenaar != null) {
      HuwelijksAmbtenaar huw = getHuwelijksambtenaar(ambtenaar.getBurgerServiceNummer());

      if (huw != null) {
        contactpersoon = new ZaakContactpersoon(AMBTENAAR, huw.getHuwelijksAmbtenaar());
        contactpersoon.setContactgegevens(
            singletonList(new PlContactgegeven().setAant(huw.getEmail()).setContactgegeven(
                new Contactgegeven(ContactgegevensService.EMAIL))));
      }
    }

    return contactpersoon != null ? contactpersoon
        : getServices().getDossierService().getContactPersoon(AMBTENAAR, ambtenaar);
  }

  @ThrowException("Fout bij ophalen huwelijkslocatie optie")
  private List<HuwelijksLocatieOptie> getHuwelijksLocatieOpties(HuwelijksLocatie locatie) {
    List<HuwLocatieOptie> opties = findEntity(HuwLocatie.class,
        locatie.getCodeHuwelijksLocatie()).getHuwLocatieOpties();
    return copyList(opties, HuwelijksLocatieOptie.class);
  }

  private DossierHuwelijkOptie getHuwOptie(DossierHuwelijk dossier, HuwelijksLocatieOptie optie) {
    for (DossierHuwelijkOptie huwOptie : dossier.getOpties()) {
      if (optie.getCodeHuwelijksLocatieOptie().equals(huwOptie.getOptie().getCodeHuwelijksLocatieOptie())) {
        return huwOptie;
      }
    }

    DossierHuwelijkOptie huwOptie = new DossierHuwelijkOptie();
    huwOptie.setOptie(optie);
    huwOptie.setWaarde("");
    return huwOptie;
  }

  private void opslaanHuwelijksOpties(DossierHuwelijk dossier) {
    for (DossierHuwelijkOptie optie : dossier.getOpties()) {
      optie.setDossier(dossier);
      optie.setOptie(optie.getOptie());
      saveEntity(optie);
    }
  }
}
