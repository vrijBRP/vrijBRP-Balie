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

package nl.procura.gba.web.modules.bs.common.pages.gerelateerdepage;

import static nl.procura.burgerzaken.gba.core.enums.GBACat.*;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.*;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.EXPARTNER;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.PARTNER;
import static nl.procura.standard.Globalfunctions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.enums.SoortVerbintenis;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class PageBsGerelateerdenUtils {

  private static final long VOLWASSEN_LEEFTIJD = 18;

  public static List<DossierPersoon> getOntbrekendePersonen(Services services, DossierPersoon dossierPersoon) {
    List<DossierPersoon> ontbrekendePersonen = new ArrayList<>();
    List<DossierPersoon> personen = dossierPersoon.getPersonen();
    if (dossierPersoon.getBurgerServiceNummer().isCorrect()) {
      for (DossierPersoon relatie : getTypePersonen(services, dossierPersoon, HUW_GPS, OUDER_1, OUDER_2, KINDEREN)) {
        if (personen.stream().noneMatch(p -> p.isPersoon(relatie))) {
          ontbrekendePersonen.add(relatie);
        }
      }
    }
    return ontbrekendePersonen;
  }

  public static Optional<String> getOntbrekendePersonenMelding(Services services,
      List<DossierPersoon> dossierPersonen) {
    List<DossierPersoon> ontbrekendePersonen = new ArrayList<>();
    dossierPersonen.forEach(p -> ontbrekendePersonen.addAll(getOntbrekendePersonen(services, p)));
    if (!ontbrekendePersonen.isEmpty()) {
      return Optional.of("<hr/>Controleer of onderstaande constatering relevant is: <br/>" +
          ontbrekendePersonen.stream()
              .map(persoon -> String.format("%s <b>%s</b> ontbreekt.</br>",
                  persoon.getDossierPersoonType(),
                  persoon.getNaam().getNaam_naamgebruik_eerste_voornaam()))
              .collect(Collectors.joining()));
    }
    return Optional.empty();
  }

  /**
   * Zoek de relaties van de persoon
   */
  public static List<DossierPersoon> getTypePersonen(Services services,
      DossierPersoon betreffende,
      GBACat... categorieen) {

    List<DossierPersoon> personen = new ArrayList<>();
    for (GBACat categorie : categorieen) {
      if (betreffende.isIngeschreven()) {
        BasePLExt pl = services.getPersonenWsService().findPL(betreffende.getBurgerServiceNummer());
        List<BasePLSet> sets = pl.getCat(categorie).getSets();
        for (BasePLSet set : sets) {
          DossierPersoon persoon = getTypePersoon(services, pl, set, categorie);
          if (persoon.isVolledig() && !isVolwassenKind(persoon)) {
            personen.add(persoon);
          }
        }
      }
    }

    return personen;
  }

  private static DossierPersoon getTypePersoon(Services services, BasePLExt persoonPl, BasePLSet set, GBACat gbaCat) {

    DossierPersoon persoon = new DossierPersoon();
    BasePLRec cat = set.getLatestRec();
    BasePLExt pl = getPersoonslijst(services, cat);

    if (pl == null) {
      return persoon;
    }

    switch (gbaCat) {
      case OUDER_1:
      case OUDER_2:
        persoon = new DossierPersoon(DossierPersoonType.OUDER);
        break;

      case HUW_GPS:
        persoon = new DossierPersoon(isHuidigePartner(cat) ? PARTNER : EXPARTNER);
        break;

      case KINDEREN:
        // Geen overleden kinderen tonen
        if (pl.getOverlijding().isOverleden()) {
          return persoon;
        }

        persoon = new DossierPersoon(DossierPersoonType.KIND);
        break;

      default:
        break;
    }

    /*
      Voeg de persoongegevens toe op basis van de categorie
     */
    if (pl.getPersoon().getBsn().getVal().isEmpty()) {
      BsPersoonUtils.kopieDossierPersoon(cat, persoon);

    } else {
      BsPersoonUtils.kopieDossierPersoon(pl, persoon);
    }

    // Verbintenis gegevens toevoegen

    if (GBACat.HUW_GPS == gbaCat) {
      BasePLRec sluiting = persoonPl.getHuwelijk().getSluiting(set, "");
      BasePLRec ontbinding = persoonPl.getHuwelijk().getOntbinding(set, "");

      BasePLValue sluitDatum = sluiting.getElemVal(DATUM_VERBINTENIS);
      BasePLValue sluitPlaats = sluiting.getElemVal(PLAATS_VERBINTENIS);
      BasePLValue sluitLand = sluiting.getElemVal(LAND_VERBINTENIS);

      BasePLValue ontbDatum = ontbinding.getElemVal(DATUM_ONTBINDING);
      BasePLValue ontbPlaats = ontbinding.getElemVal(PLAATS_ONTBINDING);
      BasePLValue ontbLand = ontbinding.getElemVal(LAND_ONTBINDING);
      BasePLValue ontbReden = ontbinding.getElemVal(REDEN_ONTBINDING);

      // Sluiting
      persoon.getVerbintenis().setSoort(
          SoortVerbintenis.get(sluiting.getElemVal(SOORT_VERBINTENIS).getVal()));
      persoon.getVerbintenis().getSluiting().setDatum(new DateTime(sluitDatum.getVal()));
      persoon.getVerbintenis().getSluiting().setPlaats(
          new FieldValue(sluitPlaats.getVal(), sluitPlaats.getDescr()));
      persoon.getVerbintenis().getSluiting().setLand(
          new FieldValue(sluitLand.getVal(), sluitLand.getDescr()));

      // Ontbinding
      persoon.getVerbintenis().getOntbinding().setDatum(new DateTime(ontbDatum.getVal()));
      persoon.getVerbintenis().getOntbinding().setPlaats(
          new FieldValue(ontbPlaats.getVal(), ontbPlaats.getDescr()));
      persoon.getVerbintenis().getOntbinding().setLand(
          new FieldValue(ontbLand.getVal(), ontbLand.getDescr()));
      persoon.getVerbintenis().getOntbinding().setReden(
          new FieldValue(ontbReden.getVal(), ontbReden.getDescr()));
    }

    return persoon;
  }

  /**
   * Vul persoonslijst op basis van BSN
   */
  private static BasePLExt getPersoonslijst(Services services, BasePLRec cat) {
    BasePLExt pl;
    if (cat.hasElems()) {
      BasePLValue bsn = cat.getElemVal(GBAElem.BSN);
      if (fil(bsn.getVal())) {
        pl = services.getPersonenWsService().findPL(new BsnFieldValue(bsn.getVal()));
        if (pl.getCats().size() > 0) {
          return pl;
        }
      }
      return new BasePLExt();
    }

    return null;
  }

  private static boolean isHuidigePartner(BasePLRec cat) {
    return emp(cat.getElemVal(GBAElem.DATUM_ONTBINDING).getVal());
  }

  private static boolean isVolwassenKind(DossierPersoon persoon) {
    boolean isKind = persoon.getDossierPersoonType().is(DossierPersoonType.KIND);
    boolean isVolwassen = along(persoon.getGeboorte().getLeeftijd()) >= VOLWASSEN_LEEFTIJD;
    return isKind && isVolwassen;
  }
}
