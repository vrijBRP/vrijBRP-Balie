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

package nl.procura.gba.web.services.bs.erkenning;

import static nl.procura.gba.common.MiscUtils.to;
import static nl.procura.gba.common.ZaakStatusType.*;
import static nl.procura.gba.web.common.tables.GbaTables.*;
import static nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoonType.ERKENNER;
import static nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoonType.MOEDER;
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
import nl.procura.gba.web.services.bs.erkenning.argumenten.ErkenningArgumenten;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.bs.geboorte.GeboorteService;
import nl.procura.gba.web.services.zaken.algemeen.*;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContact;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

public class ErkenningService extends AbstractZaakContactService<Dossier> implements ZaakService<Dossier> {

  public ErkenningService() {
    super("Erkenning", ZaakType.ERKENNING);
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van erkenningen")
  public int getZakenCount(ZaakArgumenten zaakArgumenten) {
    return getServices().getDossierService().getZakenCount(new ZaakArgumenten(zaakArgumenten, getZaakType()));
  }

  @Override
  public ZaakContact getContact(Dossier zaak) {
    ZaakContact contact = new ZaakContact();
    DossierErkenning erkenning = (DossierErkenning) zaak.getZaakDossier();
    contact.add(getServices().getDossierService().getContactPersoon(MOEDER, erkenning.getMoeder()));
    contact.add(getServices().getDossierService().getContactPersoon(ERKENNER, erkenning.getErkenner()));
    return contact;
  }

  public GeboorteService getGeboortes() {
    return getServices().getGeboorteService();
  }

  /**
   * Zoek de gekoppelde geboorte
   */
  public DossierGeboorte getGekoppeldeGeboorte(DossierErkenning erkenning) {

    DossierErkenning zaakDossier = to(erkenning, DossierErkenning.class);
    List<DossGeb> dossGebs = new ArrayList<>();

    dossGebs.addAll(zaakDossier.getDossGebs()); // Als sprake is van erkenning ongeboren vrucht
    dossGebs.addAll(zaakDossier.getDossErkGebs()); // Als sprake is van erkenning bij geboorte

    for (DossGeb dossGeb : dossGebs) {
      String zaakId = dossGeb != null ? dossGeb.getDoss().getZaakId() : "";
      if (fil(zaakId)) {
        for (Zaak zaakGeboorte : getGeboortes().getMinimalZaken(new ZaakArgumenten(zaakId))) {
          Dossier volledigeGeboorte = getGeboortes().getCompleteZaak((Dossier) zaakGeboorte);
          return (DossierGeboorte) volledigeGeboorte.getZaakDossier();
        }
      }
    }

    return null;
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van erkenningen")
  public List<Dossier> getMinimalZaken(ZaakArgumenten zaakArgumenten) {
    return getServices().getDossierService().getMinimalZaken(new ZaakArgumenten(zaakArgumenten, getZaakType()));
  }

  @Override
  public Zaak getNewZaak() {
    Dossier dossier = new DossierErkenning().getDossier();
    dossier.setDatumIngang(new DateTime());
    return dossier;
  }

  public List<Zaak> getOngeborenErkenningen(ErkenningArgumenten zaakArgumenten) {

    List<Zaak> zaken = new ArrayList<>();

    zaakArgumenten.setStatussen(OPGENOMEN, VERWERKT, VERWERKT_IN_GBA);

    if (pos(zaakArgumenten.getBsn()) || fil(zaakArgumenten.getAkteVolgnr())) {

      for (Zaak zaak : getMinimalZaken(zaakArgumenten)) {

        DossierErkenning erk = to(getCompleteZaak((Dossier) zaak).getZaakDossier(), DossierErkenning.class);

        if (fil(zaakArgumenten.getAkteVolgnr())) {

          zaken.add(zaak);
        } else if (zaakArgumenten.getMoeder() != null) { // Bsn van moeder matched

          if (ErkenningsType.ERKENNING_ONGEBOREN_VRUCHT.is(erk.getErkenningsType())) {

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

    DossierErkenning zaakDossier = getServices().getDossierService().getZaakDossier(zaak, DossierErkenning.class);

    zaakDossier.setLandNaamRecht(LAND.get(zaakDossier.getcLandNaamRecht()));
    zaakDossier.setKeuzeTitel(TITEL.get(zaakDossier.getKeuzeNaamTp()));
    zaakDossier.setLandToestemmingsRechtMoeder(LAND.get(zaakDossier.getcLandToestRechtMoeder()));
    zaakDossier.setLandToestemmingsRechtKind(LAND.get(zaakDossier.getcLandToestRechtKind()));
    zaakDossier.setLandAfstammingsRecht(LAND.get(zaakDossier.getcLandAfstamRecht()));
    zaakDossier.setGemeente(PLAATS.get(zaakDossier.getcGemErk()));

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
   * Is de erkenning gekoppeld aan een geboorte
   */
  public boolean isGekoppeldAanGeboorte(DossierErkenning erkenning) {

    DossierErkenning zaakDossier = to(erkenning, DossierErkenning.class);
    List<DossGeb> dossGebs = new ArrayList<>();

    dossGebs.addAll(zaakDossier.getDossGebs()); // Als sprake is van erkenning ongeboren vrucht
    dossGebs.addAll(zaakDossier.getDossErkGebs()); // Als sprake is van erkenning bij geboorte

    return !dossGebs.isEmpty();
  }

  @Override
  @Transactional
  @ThrowException("Fout bij opslaan van het dossier")
  public void save(Dossier zaak) {

    DossierErkenning zaakDossier = ZaakUtils.newZaakDossier(zaak, getServices());
    zaak.setDescr(getDescription(zaakDossier));
    getServices().getDossierService().saveDossier(zaak);

    verwijderOverbodigeKinderen(zaakDossier);

    getServices().getDossierService()
        .savePersonen(zaak)
        .saveNationaliteiten(zaak)
        .saveVereisten(zaak)
        .saveAktes(zaak);

    afleiden(zaakDossier);
    getServices().getDossierService().saveZaakDossier(zaak.getZaakDossier());

    callListeners(ServiceEvent.CHANGE);
  }

  private static String getDescription(DossierErkenning zaakDossier) {
    List<String> elements = new ArrayList<>();
    elements.add(zaakDossier.getErkenningsTypeOmschrijving());
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
    DossierErkenning erk = to(getStandardZaak(zaak).getZaakDossier(), DossierErkenning.class);
    if (isGekoppeldAanGeboorte(erk)) {
      throw new ProException(WARNING,
          "De erkenning kan niet worden verwijderd.</br>Deze is gekoppeld aan een geboorte.");
    }
    getServices().getDossierService().deleteDossier(zaak);
  }

  /**
   * Vul waarden in de af te leiden zijn van andere waarden
   */
  private void afleiden(DossierErkenning zaakDossier) {

    zaakDossier.setGemeente(GbaTables.PLAATS.get(getServices().getGebruiker().getGemeenteCode()));

    if (zaakDossier.getMoeder().isIngeschreven()) {
      for (DossierPersoon kind : zaakDossier.getKinderen()) {
        kind.setWoonplaats(zaakDossier.getMoeder().getWoonplaats());
        kind.setLand(zaakDossier.getMoeder().getLand());
      }
    } else if (zaakDossier.getErkenner().isIngeschreven()) {
      for (DossierPersoon kind : zaakDossier.getKinderen()) {
        kind.setWoonplaats(zaakDossier.getErkenner().getWoonplaats());
        kind.setLand(zaakDossier.getErkenner().getLand());
      }
    }
  }

  /**
   * Als er geen akte is per kind dan alle kinderen verwijderen
   */
  private void verwijderOverbodigeKinderen(DossierErkenning zaakDossier) {

    if (!zaakDossier.isAktePerKind()) {
      getServices().getDossierService().deletePersonen(zaakDossier.getDossier(), zaakDossier.getKinderen());
    }
  }
}
