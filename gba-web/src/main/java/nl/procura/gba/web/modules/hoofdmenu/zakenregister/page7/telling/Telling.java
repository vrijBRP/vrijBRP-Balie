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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page7.telling;

import java.util.List;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.common.misc.ZaakPeriode;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page7.telling.TellingAlles.TypeAlles;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page7.telling.eigenschap.*;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page7.telling.eigenschap.TellingBron.TypeBron;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page7.telling.eigenschap.TellingGebruiker.TypeGebruiker;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page7.telling.eigenschap.TellingLeverancier.TypeLeverancier;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page7.telling.eigenschap.TellingLocatie.TypeLocatie;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page7.telling.eigenschap.TellingProfiel.TypeProfiel;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page7.telling.zaaktype.*;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page7.telling.zaaktype.TellingErkenning.TypeErkenning;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page7.telling.zaaktype.TellingGeboorte.TypeGeboorte;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page7.telling.zaaktype.TellingHuwelijk.TypeHuwelijk;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page7.telling.zaaktype.TellingInhouding.TypeInhoudingVermissing;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page7.telling.zaaktype.TellingOmzetting.TypeOmzetting;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page7.telling.zaaktype.TellingOntbinding.TypeOntbinding;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page7.telling.zaaktype.TellingOverlijden.TypeOverlijden;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page7.telling.zaaktype.TellingReisdocument.TypeReisdocument;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page7.telling.zaaktype.TellingRijbewijs.TypeRijbewijs;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page7.telling.zaaktype.TellingTerugmelding.TypeTerugmelding;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page7.telling.zaaktype.TellingUittreksel.TypeUittreksel;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page7.telling.zaaktype.TellingVerhuizing.TypeVerhuizing;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page7.telling.zaaktype.TellingVog.TypeVog;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraag;

import ch.lambdaj.group.Group;

public class Telling extends TellingTemplate {

  private final ZaakPeriode zaakPeriode;

  // Eigenschappen
  private final TypeAlles       alles;
  private final TypeLeverancier leverancier;
  private final TypeBron        bron;
  private final TypeLocatie     locatie;
  private final TypeGebruiker   gebruiker;
  private final TypeProfiel     profiel;

  // Zaken
  private TypeReisdocument        reisdocument         = new TypeReisdocument();
  private TypeRijbewijs           rijbewijs            = new TypeRijbewijs();
  private TypeUittreksel          uittreksel           = new TypeUittreksel();
  private TypeVerhuizing          verhuizing           = new TypeVerhuizing();
  private TypeGeboorte            geboorte             = new TypeGeboorte();
  private TypeHuwelijk            huwelijk             = new TypeHuwelijk();
  private TypeOmzetting           omzetting            = new TypeOmzetting();
  private TypeOntbinding          ontbinding           = new TypeOntbinding();
  private TypeVog                 vog                  = new TypeVog();
  private TypeErkenning           erkenning            = new TypeErkenning();
  private TypeOverlijden          overlijdenGemeente   = new TypeOverlijden();
  private TypeOverlijden          lijkvinding          = new TypeOverlijden();
  private TypeOverlijden          levenloosGeborenKind = new TypeOverlijden();
  private TypeInhoudingVermissing inhouding            = new TypeInhoudingVermissing();
  private TypeTerugmelding        terugmelding         = new TypeTerugmelding();

  public Telling(Services services, ZaakPeriode zaakPeriode, List<Zaak> zaken) {

    this.zaakPeriode = zaakPeriode;

    vulRijbewijsStatussen(services, zaken);

    Group<Zaak> typeGroups = getGroupsByType(zaken);
    for (Group<Zaak> typeGroup : typeGroups.subgroups()) {
      ZaakType zaakType = (ZaakType) typeGroup.key();

      switch (zaakType) {
        case REISDOCUMENT:
          reisdocument = TellingReisdocument.get(zaakPeriode, zaakType, typeGroup);
          break;

        case RIJBEWIJS:
          rijbewijs = TellingRijbewijs.get(zaakPeriode, zaakType, typeGroup);
          break;

        case UITTREKSEL:
          uittreksel = TellingUittreksel.get(zaakType, typeGroup);
          break;

        case VERHUIZING:
          verhuizing = TellingVerhuizing.get(zaakType, typeGroup);
          break;

        case GEBOORTE:
          geboorte = TellingGeboorte.get(zaakType, typeGroup);
          break;

        case ERKENNING:
          erkenning = TellingErkenning.get(zaakType, typeGroup);
          break;

        case OVERLIJDEN_IN_GEMEENTE:
          overlijdenGemeente = TellingOverlijden.getGemeente(zaakType, typeGroup);
          break;

        case LIJKVINDING:
          lijkvinding = TellingOverlijden.getLijkvinding(zaakType, typeGroup);
          break;

        case LEVENLOOS:
          levenloosGeborenKind = TellingOverlijden.getLevenloos(zaakType, typeGroup);
          break;

        case HUWELIJK_GPS_GEMEENTE:
          huwelijk = TellingHuwelijk.get(zaakType, typeGroup);
          break;

        case OMZETTING_GPS:
          omzetting = TellingOmzetting.get(zaakType, typeGroup);
          break;

        case ONTBINDING_GEMEENTE:
          ontbinding = TellingOntbinding.get(zaakType, typeGroup);
          break;

        case COVOG:
          vog = TellingVog.get(zaakType, typeGroup);
          break;

        case INHOUD_VERMIS:
          inhouding = TellingInhouding.get(zaakType, typeGroup);
          break;

        case CORRESPONDENTIE:
          // Nog niet geimplementeerd
          break;

        case GEGEVENSVERSTREKKING:
          // Nog niet geimplementeerd
          break;

        case GPK:
          // Nog niet geimplementeerd
          break;

        case NAAMSKEUZE:
          // Nog niet geimplementeerd
          break;

        case INDICATIE:
          // Nog niet geimplementeerd
          break;

        case LEGE_PERSOONLIJST:
          // Nog niet geimplementeerd
          break;

        case NAAMGEBRUIK:
          // Nog niet geimplementeerd
          break;

        case OVERLIJDEN_IN_BUITENLAND:
          // Nog niet geimplementeerd
          break;

        case TERUGMELDING:
          terugmelding = TellingTerugmelding.get(zaakType, typeGroup);
          break;

        case VERSTREKKINGSBEPERKING:
          // Nog niet geimplementeerd
          break;

        default:
          break;
      }
    }

    zaken.sort(new DateSorter());

    alles = new TellingAlles().get(typeGroups);
    gebruiker = new TellingGebruiker().get(zaken);
    locatie = new TellingLocatie().get(zaken);
    bron = new TellingBron().get(zaken);
    leverancier = new TellingLeverancier().get(zaken);
    profiel = new TellingProfiel().get(zaken);
  }

  public TypeAlles getAlles() {
    return alles;
  }

  public TypeBron getBron() {
    return bron;
  }

  public TypeErkenning getErkenning() {
    return erkenning;
  }

  public TypeGeboorte getGeboorte() {
    return geboorte;
  }

  public TypeGebruiker getGebruiker() {
    return gebruiker;
  }

  public TypeHuwelijk getHuwelijk() {
    return huwelijk;
  }

  public TypeInhoudingVermissing getInhouding() {
    return inhouding;
  }

  public TypeOverlijden getLevenloosGeborenKind() {
    return levenloosGeborenKind;
  }

  public TypeLeverancier getLeverancier() {
    return leverancier;
  }

  public TypeOverlijden getLijkvinding() {
    return lijkvinding;
  }

  public TypeLocatie getLocatie() {
    return locatie;
  }

  public TypeOmzetting getOmzetting() {
    return omzetting;
  }

  public TypeOntbinding getOntbinding() {
    return ontbinding;
  }

  public TypeOverlijden getOverlijdenGemeente() {
    return overlijdenGemeente;
  }

  public TypeProfiel getProfiel() {
    return profiel;
  }

  public TypeReisdocument getReisdocument() {
    return reisdocument;
  }

  public TypeRijbewijs getRijbewijs() {
    return rijbewijs;
  }

  public TypeTerugmelding getTerugmelding() {
    return terugmelding;
  }

  public void setTerugmelding(TypeTerugmelding terugmelding) {
    this.terugmelding = terugmelding;
  }

  public TypeUittreksel getUittreksel() {
    return uittreksel;
  }

  public TypeVerhuizing getVerhuizing() {
    return verhuizing;
  }

  public TypeVog getVog() {
    return vog;
  }

  public ZaakPeriode getZaakPeriode() {
    return zaakPeriode;
  }

  private void vulRijbewijsStatussen(Services services, List<Zaak> zaken) {
    for (Zaak zaak : zaken) {
      if (ZaakType.RIJBEWIJS.is(zaak.getType())) {
        services.getRijbewijsService().vulRijbewijsStatussen((RijbewijsAanvraag) zaak);
      }
    }
  }
}
