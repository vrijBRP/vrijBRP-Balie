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

package nl.procura.gba.web.services.bs.lv.afstamming;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Timer;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.zaken.algemeen.AbstractZaakContactService;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakService;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContact;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoonType;

public class LvService extends AbstractZaakContactService<Dossier> implements ZaakService<Dossier> {

  private final static Logger LOGGER = LoggerFactory.getLogger(LvService.class.getName());

  public LvService() {
    super("Latere vermelding", ZaakType.LV);
  }

  public LvService(String name, ZaakType zaakType) {
    super(name, zaakType);
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van de zaken")
  public int getZakenCount(ZaakArgumenten zaakArgumenten) {
    return getServices().getDossierService().getZakenCount(new ZaakArgumenten(zaakArgumenten, getZaakType()));
  }

  @Override
  public ZaakContact getContact(Dossier zaak) {
    ZaakContact contact = new ZaakContact();
    DossierLv lv = (DossierLv) zaak.getZaakDossier();
    contact.add(getServices().getDossierService().getContactPersoon(ZaakContactpersoonType.AANGEVER, lv.getKind()));
    return contact;
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van de zaken")
  public List<Dossier> getMinimalZaken(ZaakArgumenten zaakArgumenten) {
    return getServices().getDossierService().getMinimalZaken(new ZaakArgumenten(zaakArgumenten, getZaakType()));
  }

  @Override
  public Zaak getNewZaak() {
    return aanvullenZaak(new DossierLv().getDossier());
  }

  @Override
  @Timer
  @ThrowException("Het is niet mogelijk om de zaak op te vragen")
  public Dossier getStandardZaak(Dossier zaak) {
    getServices().getDossierService().getZaakDossier(zaak, DossierLv.class);
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
    DossierLv zaakDossier = ZaakUtils.newZaakDossier(zaak, getServices());
    zaak.setDescr(getDescription(zaakDossier));

    getServices().getDossierService()
        .saveDossier(zaak)
        .savePersonen(zaak)
        .saveNationaliteiten(zaak)
        .saveAktes(zaak);

    getServices().getDossierService().saveZaakDossier(zaak.getZaakDossier());
  }

  private static String getDescription(DossierLv zaakDossier) {
    List<String> elements = new ArrayList<>();
    elements.add(zaakDossier.getKind().getNaam().getVoorv_gesl());
    zaakDossier.getDossier().getAktes().forEach(akte -> elements.add(akte.getDescription()));
    return elements.stream()
        .filter(StringUtils::isNotBlank)
        .collect(Collectors.joining(" / "));
  }

  @Override
  @Transactional
  public void delete(Dossier zaak) {
    getServices().getDossierService().deleteDossier(zaak);
  }
}
