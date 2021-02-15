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

package nl.procura.gba.web.services.bs.ontbinding;

import static nl.procura.gba.web.common.tables.GbaTables.PLAATS;
import static nl.procura.gba.web.common.tables.GbaTables.TITEL;
import static nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoonType.PARTNER_1;
import static nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoonType.PARTNER_2;

import java.util.List;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.web.common.tables.GbaTables;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Timer;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.zaken.algemeen.*;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContact;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class OntbindingService extends AbstractZaakContactService<Dossier> implements ZaakService<Dossier> {

  public OntbindingService() {
    super("Ontbinding/einde huwelijk/GPS in gemeente", ZaakType.ONTBINDING_GEMEENTE);
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van zaken")
  public int getZakenCount(ZaakArgumenten zaakArgumenten) {
    return getServices().getDossierService().getZakenCount(new ZaakArgumenten(zaakArgumenten, getZaakType()));
  }

  @Override
  public ZaakContact getContact(Dossier zaak) {
    ZaakContact contact = new ZaakContact();
    DossierOntbinding ontbinding = (DossierOntbinding) zaak.getZaakDossier();
    contact.add(getServices().getDossierService().getContactPersoon(PARTNER_1, ontbinding.getPartner1()));
    contact.add(getServices().getDossierService().getContactPersoon(PARTNER_2, ontbinding.getPartner2()));
    return contact;
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van huwelijkszaken")
  public List<Dossier> getMinimalZaken(ZaakArgumenten zaakArgumenten) {
    return getServices().getDossierService().getMinimalZaken(new ZaakArgumenten(zaakArgumenten, getZaakType()));
  }

  @Override
  public Zaak getNewZaak() {
    return new DossierOntbinding().getDossier();
  }

  @Override
  @ThrowException("Het is niet mogelijk om de zaak op te vragen")
  public Dossier getStandardZaak(Dossier zaak) {

    DossierOntbinding zaakDossier = getServices().getDossierService().getZaakDossier(zaak, DossierOntbinding.class);

    // sluitinggegevens
    zaakDossier.setLandVerbintenis(GbaTables.LAND.get(zaakDossier.getLandVb()));

    FieldValue plaatsNL = PLAATS.get(zaakDossier.getPlaatsVb());
    FieldValue plaatsBL = new FieldValue(zaakDossier.getPlaatsVb());
    zaakDossier.setPlaatsVerbintenis(plaatsNL.getBigDecimalValue().intValue() > 0 ? plaatsNL : plaatsBL);

    // aktegegevens
    zaakDossier.setAktePlaatsVerbintenis(PLAATS.get(zaakDossier.getAktePlaats()));
    zaakDossier.setOntbindingsGemeente(PLAATS.get(zaakDossier.getcOntbGem()));

    // Naamgebruik
    zaakDossier.setTitelPartner1(TITEL.get(zaakDossier.getP1Titel()));
    zaakDossier.setTitelPartner2(TITEL.get(zaakDossier.getP2Titel()));

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

    DossierOntbinding zaakDossier = ZaakUtils.newZaakDossier(zaak, getServices());

    getServices().getDossierService().saveDossier(zaak).savePersonen(zaak);

    zaakDossier.setOntbindingsGemeente(PLAATS.get(getServices().getGebruiker().getGemeenteCode()));

    getServices().getDossierService().saveZaakDossier(zaakDossier);
  }

  @Override
  @Transactional
  public void delete(Dossier zaak) {
    getServices().getDossierService().deleteDossier(zaak);
  }
}
