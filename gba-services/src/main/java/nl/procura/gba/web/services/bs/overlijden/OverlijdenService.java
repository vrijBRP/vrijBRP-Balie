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

package nl.procura.gba.web.services.bs.overlijden;

import static nl.procura.gba.common.MiscUtils.copy;
import static nl.procura.gba.web.common.tables.GbaTables.*;
import static nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoonType.AANGEVER;
import static nl.procura.standard.Globalfunctions.pos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.jpa.personen.db.DossCorrDest;
import nl.procura.gba.jpa.personen.db.DossOverl;
import nl.procura.gba.jpa.personen.db.DossOverlAangever;
import nl.procura.gba.jpa.personen.db.DossOverlUitt;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Timer;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.gba.basistabellen.overlijdenaangever.OverlijdenAangever;
import nl.procura.gba.web.services.interfaces.GeldigheidStatus;
import nl.procura.gba.web.services.zaken.algemeen.AbstractZaakContactService;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakService;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContact;
import nl.procura.java.reflection.ReflectionUtil;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public abstract class OverlijdenService extends AbstractZaakContactService<Dossier>
    implements ZaakService<Dossier> {

  public OverlijdenService(String name, ZaakType zaakType) {
    super(name, zaakType);
  }

  @Override
  @Timer
  public int getZakenCount(ZaakArgumenten zaakArgumenten) {
    return getServices().getDossierService().getZakenCount(new ZaakArgumenten(zaakArgumenten, getZaakType()));
  }

  @Override
  public ZaakContact getContact(Dossier zaak) {
    DossierOverlijden overlijden = (DossierOverlijden) zaak.getZaakDossier();
    ZaakContact contact = new ZaakContact();
    contact.add(getServices().getDossierService().getContactPersoon(AANGEVER, overlijden.getAangever()));
    if (overlijden.getVerzoek().isVerzoekInd()) {
      contact.add(overlijden.getVerzoek().getContactpersoon());
    }
    return contact;
  }

  @Override
  @Timer
  public List<Dossier> getMinimalZaken(ZaakArgumenten zaakArgumenten) {
    return getServices().getDossierService().getMinimalZaken(new ZaakArgumenten(zaakArgumenten, getZaakType()));
  }

  @ThrowException("Fout bij ophalen aangevers van overlijden")
  public List<OverlijdenAangever> getOverlijdenAangevers(GeldigheidStatus status) {
    List<OverlijdenAangever> list = new ArrayList<>();
    for (DossOverlAangever aangever : findEntity(new DossOverlAangever())) {
      OverlijdenAangever aangeverImpl = copy(aangever, OverlijdenAangever.class);
      if (aangeverImpl.getGeldigheidStatus().is(status)) {
        aangeverImpl.setGeboorteland(LAND.get(aangeverImpl.getCGebLand()));
        aangeverImpl.setTitel(TITEL.get(aangeverImpl.getTp()));
        aangeverImpl.setGeboorteplaats(
            pos(aangeverImpl.getCGebPlaats()) ? PLAATS.get(aangeverImpl.getCGebPlaats())
                : new FieldValue(
                    aangeverImpl.getGebPlaats()));
        list.add(aangeverImpl);
      }
    }

    Collections.sort(list);
    return list;
  }

  @Override
  @Timer
  public List<ZaakKey> getZaakKeys(ZaakArgumenten zaakArgumenten) {
    return getServices().getDossierService().getZaakKeys(new ZaakArgumenten(zaakArgumenten, getZaakType()));
  }

  @Transactional
  @ThrowException("Fout bij opslaan overlijdenaangever")
  public void save(OverlijdenAangever aangever) {
    saveEntity(aangever);
  }

  @Override
  @Transactional
  public void delete(Dossier zaak) {
    getServices().getDossierService().deleteDossier(zaak);
  }

  @Transactional
  @ThrowException("Fout bij verwijderen overlijdenaangever")
  public void delete(OverlijdenAangever aangever) {
    removeEntity(aangever);
  }

  protected void saveUittreksels(AbstractDossierOverlijden zaak) {
    for (DossOverlUitt uitt : zaak.getVerzoek().getUittreksels()) {
      uitt.setUittCode(uitt.getUittCode());
      uitt.setUittDescr(uitt.getUittDescr());
      uitt.setDossOverl(ReflectionUtil.deepCopyBean(DossOverl.class, zaak));
      saveEntity(uitt);
    }
  }

  protected void saveCorrespondentie(AbstractDossierOverlijden zaak) {
    DossCorrDest correspondentie = zaak.getVerzoek().getCorrespondentie();
    correspondentie.setCDossCorrDest(zaak.getId());
    saveEntity(correspondentie);
  }

  protected static String getDescription(DossierOverlijden zaakDossier) {
    List<String> elements = new ArrayList<>();
    String overledene = zaakDossier.getOverledene().getNaam().getVoorv_gesl();
    if (StringUtils.isNotBlank(overledene)) {
      elements.add(overledene);
    }
    return String.join(" / ", elements);
  }
}
