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

package nl.procura.gba.web.services.bs.naamskeuze;

import static nl.procura.gba.common.MiscUtils.to;
import static nl.procura.gba.common.ZaakStatusType.*;
import static nl.procura.gba.web.common.tables.GbaTables.*;
import static nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoonType.MOEDER;
import static nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoonType.PARTNER;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.pos;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.jpa.personen.db.DossGeb;
import nl.procura.gba.web.common.tables.GbaTables;
import nl.procura.gba.web.services.ServiceEvent;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Timer;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.bs.geboorte.GeboorteService;
import nl.procura.gba.web.services.bs.naamskeuze.argumenten.NaamskeuzeArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.*;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContact;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

public class NaamskeuzeService extends AbstractZaakContactService<Dossier> implements ZaakService<Dossier> {

  public NaamskeuzeService() {
    super("Naamskeuze", ZaakType.NAAMSKEUZE);
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van naamskeuzeen")
  public int getZakenCount(ZaakArgumenten zaakArgumenten) {
    return getServices().getDossierService().getZakenCount(new ZaakArgumenten(zaakArgumenten, getZaakType()));
  }

  @Override
  public ZaakContact getContact(Dossier zaak) {
    ZaakContact contact = new ZaakContact();
    DossierNaamskeuze naamskeuze = (DossierNaamskeuze) zaak.getZaakDossier();
    contact.add(getServices().getDossierService().getContactPersoon(MOEDER, naamskeuze.getMoeder()));
    contact.add(getServices().getDossierService().getContactPersoon(PARTNER, naamskeuze.getPartner()));
    return contact;
  }

  /**
   * Zoek de gekoppelde geboorte
   */
  public DossierGeboorte getGekoppeldeGeboorte(DossierNaamskeuze naamskeuze) {
    DossierNaamskeuze zaakDossier = to(naamskeuze, DossierNaamskeuze.class);
    for (DossGeb dossGeb : zaakDossier.getDossGebs()) {
      String zaakId = dossGeb != null ? dossGeb.getDoss().getZaakId() : "";
      if (fil(zaakId)) {
        GeboorteService geboorteService = getServices().getGeboorteService();
        for (Zaak zaakGeboorte : geboorteService.getMinimalZaken(new ZaakArgumenten(zaakId))) {
          Dossier volledigeGeboorte = geboorteService.getCompleteZaak((Dossier) zaakGeboorte);
          return (DossierGeboorte) volledigeGeboorte.getZaakDossier();
        }
      }
    }

    return null;
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van naamskeuzeen")
  public List<Dossier> getMinimalZaken(ZaakArgumenten zaakArgumenten) {
    return getServices().getDossierService().getMinimalZaken(new ZaakArgumenten(zaakArgumenten, getZaakType()));
  }

  @Override
  public Zaak getNewZaak() {
    Dossier dossier = new DossierNaamskeuze().getDossier();
    dossier.setDatumIngang(new DateTime());
    return dossier;
  }

  public List<Zaak> getNaamskeuzesVoorGeboorte(NaamskeuzeArgumenten zaakArgumenten) {
    List<Zaak> zaken = new ArrayList<>();
    zaakArgumenten.setStatussen(OPGENOMEN, VERWERKT, VERWERKT_IN_GBA);

    if (pos(zaakArgumenten.getBsn()) || fil(zaakArgumenten.getAkteVolgnr())) {
      for (Zaak zaak : getMinimalZaken(zaakArgumenten)) {
        DossierNaamskeuze erk = to(getCompleteZaak((Dossier) zaak).getZaakDossier(), DossierNaamskeuze.class);
        if (fil(zaakArgumenten.getAkteVolgnr())) {
          zaken.add(zaak);

        } else if (zaakArgumenten.getMoeder() != null) {
          if (NaamskeuzeType.NAAMSKEUZE_VOOR_GEBOORTE.is(erk.getDossierNaamskeuzeType())) {
            BsnFieldValue bsnMoeder = erk.getMoeder().getBurgerServiceNummer();
            BsnFieldValue bsnZoek = zaakArgumenten.getMoeder().getBurgerServiceNummer();
            if (bsnMoeder.equals(bsnZoek)) {
              zaken.add(zaak);
            }
          }
        }
      }
    }

    return zaken;
  }

  @Override
  @ThrowException("Het is niet mogelijk om de zaak op te vragen")
  public Dossier getStandardZaak(Dossier zaak) {

    DossierNaamskeuze zaakDossier = getServices().getDossierService().getZaakDossier(zaak, DossierNaamskeuze.class);
    zaakDossier.setLandNaamRecht(LAND.get(zaakDossier.getcLandNaamRecht()));
    zaakDossier.setKeuzeTitel(TITEL.get(zaakDossier.getKeuzeNaamTp()));
    zaakDossier.setGemeente(PLAATS.get(zaakDossier.getcCGemNk()));
    getServices().getDossierService().getOverigDossier(zaak);

    return super.getStandardZaak(zaak);
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van zaak-ids")
  public List<ZaakKey> getZaakKeys(ZaakArgumenten zaakArgumenten) {
    return getServices().getDossierService().getZaakKeys(new ZaakArgumenten(zaakArgumenten, getZaakType()));
  }

  /**
   * Is de naamskeuze gekoppeld aan een geboorte
   */
  public boolean isGekoppeldAanGeboorte(DossierNaamskeuze naamskeuze) {
    DossierNaamskeuze zaakDossier = to(naamskeuze, DossierNaamskeuze.class);
    return !zaakDossier.getDossGebs().isEmpty();
  }

  @Override
  @Transactional
  @ThrowException("Fout bij opslaan van het dossier")
  public void save(Dossier zaak) {

    DossierNaamskeuze zaakDossier = ZaakUtils.newZaakDossier(zaak, getServices());
    getServices().getDossierService().saveDossier(zaak);
    verwijderOverbodigeKinderen(zaakDossier);
    zaak.setDescr(getDescription(zaakDossier));

    getServices().getDossierService()
        .savePersonen(zaak)
        .saveNationaliteiten(zaak)
        .saveVereisten(zaak)
        .saveAktes(zaak);

    setDerivedValues(zaakDossier);
    getServices().getDossierService().saveZaakDossier(zaak.getZaakDossier());
    callListeners(ServiceEvent.CHANGE);
  }

  private static String getDescription(DossierNaamskeuze zaakDossier) {
    List<String> elements = new ArrayList<>();
    elements.add(zaakDossier.getNaamskeuzeTypeOmschrijving());
    elements.add(zaakDossier.getMoeder().getNaam().getVoorv_gesl());
    elements.add(zaakDossier.getVaderErkenner().getNaam().getVoorv_gesl());
    zaakDossier.getDossier().getAktes().forEach(akte -> elements.add(akte.getDescription()));
    return elements.stream()
        .filter(StringUtils::isNotBlank)
        .collect(Collectors.joining(" / "));
  }

  @Override
  @Transactional
  public void delete(Dossier zaak) {
    DossierNaamskeuze erk = to(getStandardZaak(zaak).getZaakDossier(), DossierNaamskeuze.class);
    if (isGekoppeldAanGeboorte(erk)) {
      throw new ProException(WARNING, "De naamskeuze kan niet worden verwijderd." +
          "<br>Deze is gekoppeld aan een geboorte.");
    }
    getServices().getDossierService().deleteDossier(zaak);
  }

  /**
   * Vul waarden in de af te leiden zijn van andere waarden
   */
  private void setDerivedValues(DossierNaamskeuze zaakDossier) {
    zaakDossier.setGemeente(GbaTables.PLAATS.get(getServices().getGebruiker().getGemeenteCode()));
    if (zaakDossier.getMoeder().isIngeschreven()) {
      for (DossierPersoon kind : zaakDossier.getKinderen()) {
        kind.setWoonplaats(zaakDossier.getMoeder().getWoonplaats());
        kind.setLand(zaakDossier.getMoeder().getLand());
      }
    } else if (zaakDossier.getPartner().isIngeschreven()) {
      for (DossierPersoon kind : zaakDossier.getKinderen()) {
        kind.setWoonplaats(zaakDossier.getPartner().getWoonplaats());
        kind.setLand(zaakDossier.getPartner().getLand());
      }
    }
  }

  /**
   * Als er geen akte is per kind dan alle kinderen verwijderen
   */
  private void verwijderOverbodigeKinderen(DossierNaamskeuze zaakDossier) {
    if (!zaakDossier.isAktePerKind()) {
      getServices().getDossierService().deletePersonen(zaakDossier.getDossier(), zaakDossier.getKinderen());
    }
  }
}
