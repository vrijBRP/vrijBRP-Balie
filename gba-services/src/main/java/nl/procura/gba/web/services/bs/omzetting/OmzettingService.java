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

package nl.procura.gba.web.services.bs.omzetting;

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
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.gba.basistabellen.huwelijksambtenaar.HuwelijksAmbtenaar;
import nl.procura.gba.web.services.gba.basistabellen.huwelijkslocatie.HuwelijksLocatie;
import nl.procura.gba.web.services.gba.basistabellen.huwelijkslocatieOptie.HuwelijksLocatieOptie;
import nl.procura.gba.web.services.interfaces.GeldigheidStatus;
import nl.procura.gba.web.services.zaken.algemeen.*;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContact;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class OmzettingService extends AbstractZaakContactService<Dossier> implements ZaakService<Dossier> {

  public OmzettingService() {
    super("Omzetting GPS", ZaakType.OMZETTING_GPS);
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
    DossierOmzetting huwelijk = (DossierOmzetting) zaak.getZaakDossier();
    contact.add(getServices().getDossierService().getContactPersoon(PARTNER_1, huwelijk.getPartner1()));
    contact.add(getServices().getDossierService().getContactPersoon(PARTNER_2, huwelijk.getPartner2()));
    contact.add(getServices().getDossierService().getContactPersoon(AMBTENAAR, huwelijk.getAmbtenaar3()));
    return contact;
  }

  @ThrowException("Fout bij ophalen huwelijkslocatie optie")
  public List<DossierOmzettingOptie> getDossierHuwelijksOpties(DossierOmzetting dossier) {

    List<DossierOmzettingOptie> set = new UniqueList<>();
    for (HuwelijksLocatieOptie optie : getHuwelijksLocatieOpties(dossier.getHuwelijksLocatie())) {
      set.add(getHuwOptie(dossier, optie));
    }

    return set;
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
    return new DossierOmzetting().getDossier();
  }

  @Override
  @ThrowException("Het is niet mogelijk om de zaak op te vragen")
  public Dossier getStandardZaak(Dossier zaak) {

    DossierOmzetting zaakDossier = getServices().getDossierService().getZaakDossier(zaak, DossierOmzetting.class);
    zaakDossier.setTitelPartner1(TITEL.get(zaakDossier.getP1Titel()));
    zaakDossier.setTitelPartner2(TITEL.get(zaakDossier.getP2Titel()));
    zaakDossier.setRechtPartner1(LAND.get(zaakDossier.getP1Recht()));
    zaakDossier.setRechtPartner2(LAND.get(zaakDossier.getP2Recht()));
    zaakDossier.setHuwelijksGemeente(PLAATS.get(zaakDossier.getcHuwGem()));

    if (zaakDossier.getHuwLocatie() != null) {
      zaakDossier.setHuwelijksLocatie(copy(zaakDossier.getHuwLocatie(), HuwelijksLocatie.class));
    }

    zaakDossier.getOpties().addAll(copyList(zaakDossier.getDossOmzetOpties(), DossierOmzettingOptie.class));

    // GPS gegevens
    zaakDossier.setLandPartnerschap(LAND.get(zaakDossier.getLandGps()));

    FieldValue plaatsNL = PLAATS.get(zaakDossier.getPlaatsGps());
    FieldValue plaatsBL = new FieldValue(zaakDossier.getPlaatsGps());

    zaakDossier.setPlaatsPartnerschap(plaatsNL.getBigDecimalValue().intValue() > 0 ? plaatsNL : plaatsBL);

    // GPS Akte
    zaakDossier.setAktePlaatsPartnerschap(PLAATS.get(zaakDossier.getAktePlaatsGps()));

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

    DossierOmzetting zaakDossier = ZaakUtils.newZaakDossier(zaak, getServices());

    getServices().getDossierService().saveDossier(zaak).savePersonen(zaak).saveVereisten(zaak)
        .saveAktes(zaak);

    opslaanHuwelijksOpties(zaakDossier);

    zaakDossier.setHuwelijksGemeente(PLAATS.get(getServices().getGebruiker().getGemeenteCode()));

    getServices().getDossierService().saveZaakDossier(zaakDossier);
  }

  @Override
  @Transactional
  public void delete(Dossier zaak) {
    getServices().getDossierService().deleteDossier(zaak);
  }

  @Transactional
  public void deleteGetuige(DossierOmzetting dossierHuwelijk, DossierPersoon persoon) {
    if (persoon.getCode() != null) {
      dossierHuwelijk.getDossier().verwijderPersoon(persoon);
      removeEntity(persoon);
    }
  }

  @ThrowException("Fout bij ophalen huwelijkslocatie optie")
  private List<HuwelijksLocatieOptie> getHuwelijksLocatieOpties(HuwelijksLocatie locatie) {
    List<HuwLocatieOptie> opties = findEntity(HuwLocatie.class,
        locatie.getCodeHuwelijksLocatie()).getHuwLocatieOpties();
    return copyList(opties, HuwelijksLocatieOptie.class);
  }

  private DossierOmzettingOptie getHuwOptie(DossierOmzetting dossier, HuwelijksLocatieOptie optie) {

    for (DossierOmzettingOptie huwOptie : dossier.getOpties()) {
      if (optie.getCodeHuwelijksLocatieOptie().equals(huwOptie.getOptie().getCodeHuwelijksLocatieOptie())) {
        return huwOptie;
      }
    }

    DossierOmzettingOptie huwOptie = new DossierOmzettingOptie();
    huwOptie.setOptie(optie);
    huwOptie.setWaarde("");

    return huwOptie;
  }

  private void opslaanHuwelijksOpties(DossierOmzetting dossier) {

    for (DossierOmzettingOptie optie : dossier.getOpties()) {

      optie.setDossier(dossier);
      optie.setOptie(optie.getOptie());

      saveEntity(optie);
    }
  }
}
