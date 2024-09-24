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

package nl.procura.gba.web.services.bs.lv.geboorte;

import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.KIND;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.toBigDecimal;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkte;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkteRegistersoort;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.erkenning.DossierErkenning;
import nl.procura.gba.web.services.bs.erkenning.ErkenningsType;
import nl.procura.gba.web.services.bs.erkenning.NaamskeuzeVanToepassingType;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.bs.geboorte.erkenningbuitenproweb.ErkenningBuitenProweb;
import nl.procura.gba.web.services.bs.lv.LatereVermelding;
import nl.procura.gba.web.services.bs.lv.erkenning.LvErkenning;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class LatereVermeldingErkenningGeboorte implements LatereVermelding<LvErkenning> {

  private final List<LvErkenning> aktes = new ArrayList<>();

  /**
   * Voor geboorte
   */
  public LatereVermeldingErkenningGeboorte(DossierGeboorte geboorte) {

    if (geboorte.getVragen().heeftErkenningBijGeboorte()) {

      // Erkenning bij geboorte.
      // Hierbij heeft elk kind zijn/haar eigen geboorteakte en eigen erkenningsakte.

      int akteVolgcode = 0;
      for (DossierAkte geboorteAkte : geboorte.getDossier().getAktes()) {

        akteVolgcode++;
        DossierPersoon kind = getKind(geboorteAkte);
        DossierAkte erkenningAkte = getAkteVanPersoon(kind, geboorte.getErkenningBijGeboorte().getDossier());

        if (erkenningAkte != null) {

          LvErkenning akte = addErkenning(akteVolgcode, erkenningAkte, geboorte.getErkenningBijGeboorte());
          akte.getGeboorteAkte().setNummer(geboorteAkte.getAkte());
          akte.getGeboorteAkte().setBrpNummer(geboorteAkte.getBrpAkte());
          akte.getGeboorteAkte().setJaar(astr(geboorteAkte.getJaar()));
          akte.getGeboorteAkte().setPlaats(new FieldValue()); // Wordt niet gevuld bij geboorte

          aktes.add(akte);
        }
      }
    } else if (geboorte.getVragen().heeftErkenningVoorGeboorte()) {

      // Erkenning vóór geboorte (in proweb).
      // Hierbij delen de geboren kinderen de erkenningsakte.

      int akteVolgcode = 0;
      for (DossierAkte geboorteAkte : geboorte.getDossier().getAktes()) {

        DossierErkenning erkenning = geboorte.getErkenningVoorGeboorte();
        for (DossierAkte erkenningAkte : erkenning.getDossier().getAktes()) {

          akteVolgcode++;
          LvErkenning akte = addErkenning(akteVolgcode, erkenningAkte, erkenning);
          akte.getGeboorteAkte().setNummer(geboorteAkte.getAkte());
          akte.getGeboorteAkte().setJaar(astr(geboorteAkte.getJaar()));
          akte.getGeboorteAkte().setPlaats(new FieldValue()); // Niet opgeslagen bij geboorte

          aktes.add(akte);
        }
      }
    } else if (geboorte.getVragen().heeftErkenningBuitenProweb()) {

      // Erkenning vóór geboorte (buiten proweb)
      // Hierbij delen de geboren kinderen de erkenningsakte.

      addErkenningProweb(geboorte);
    }
  }

  @Override
  public List<LvErkenning> getAktes() {
    return aktes;
  }

  /**
   * Erkenning binnen proweb
   */
  private LvErkenning addErkenning(int akteVolgcode, DossierAkte dossierAkte, DossierErkenning erkenning) {

    LvErkenning akte = new LvErkenning();

    setNaamskeuze(akteVolgcode, erkenning.getNaamskeuzeType(), dossierAkte, akte);

    akte.setKind(dossierAkte.getAktePersoon());
    akte.setLandErkenning(Landelijk.getNederland());
    akte.setGemeente(erkenning.getGemeente());
    akte.setBuitenlandsePlaats("");
    akte.setDatum(dossierAkte.getDatumIngang());
    akte.setGemeente(erkenning.getGemeente());
    akte.setMoeder(erkenning.getMoeder());
    akte.setErkenner(erkenning.getErkenner());
    akte.setRechtbank(erkenning.getRechtbank());
    akte.setErkenningsType(erkenning.getErkenningsType());
    akte.setTitel(erkenning.getKeuzeTitel());
    akte.setVoorvoegsel(erkenning.getKeuzeVoorvoegsel());
    akte.setGeslachtsnaam(erkenning.getKeuzeGeslachtsnaam());
    akte.setToestemminggeverType(erkenning.getToestemminggeverType());
    akte.setVerklaringGezag(erkenning.isVerklaringGezag());
    akte.setAfstammingsrecht(erkenning.getLandAfstammingsRecht());
    akte.setAkte(dossierAkte);

    return akte;
  }

  /**
   * Erkenning buiten Proweb
   */
  private void addErkenningProweb(DossierGeboorte geboorte) {

    ErkenningBuitenProweb erkenning = geboorte.getErkenningBuitenProweb();

    for (DossierAkte geboorteAkte : geboorte.getDossier().getAktes()) {

      LvErkenning akte = new LvErkenning();

      setNaamskeuze(1, erkenning.getNaamskeuzeType(), geboorteAkte, akte);

      akte.setDatum(erkenning.getDatumErkenning());

      akte.getGeboorteAkte().setNummer(geboorteAkte.getAkte());
      akte.getGeboorteAkte().setBrpNummer(geboorteAkte.getBrpAkte());
      akte.getGeboorteAkte().setJaar(astr(geboorteAkte.getJaar()));
      akte.getGeboorteAkte().setPlaats(new FieldValue()); // Wordt niet gevuld bij geboorte

      akte.setLandErkenning(erkenning.getLandErkenning());
      akte.setGemeente(erkenning.getGemeente());
      akte.setBuitenlandsePlaats(erkenning.getBuitenlandsePlaats());
      akte.setToestemminggeverType(erkenning.getToestemminggeverType());
      akte.setVerklaringGezag(erkenning.isVerklaringGezag());
      akte.setAfstammingsrecht(erkenning.getLandAfstamming());
      akte.setNaamskeuzeType(erkenning.getNaamskeuzeType());

      ProcuraDate erkenningsDatum = new ProcuraDate(erkenning.getDatumErkenning().getDate());

      DossierAkte d = new DossierAkte();
      d.setJaar(toBigDecimal(erkenningsDatum.getYear()));
      d.setRegistersoort(toBigDecimal(DossierAkteRegistersoort.AKTE_ERKENNING_NAAMSKEUZE));
      d.setRegisterdeel(""); // Nvt
      d.setVnr(toBigDecimal(erkenning.getAktenummer()));
      d.setDatumIngang(erkenning.getDatumErkenning());

      akte.setAkte(d);
      akte.setKind(geboorteAkte.getAktePersoon());
      akte.setMoeder(geboorte.getMoeder());
      akte.setErkenner(geboorte.getVader());
      akte.setRechtbank(erkenning.getRechtbank());
      akte.setErkenningsType(ErkenningsType.ERKENNING_ONGEBOREN_VRUCHT);

      for (DossierPersoon p : geboorteAkte.getPersonen()) {
        if (p.getDossierPersoonType().is(KIND)) {
          akte.setTitel(p.getTitel());
          akte.setVoorvoegsel(p.getVoorvoegsel());
          akte.setGeslachtsnaam(p.getGeslachtsnaam());
        }
      }

      aktes.add(akte);
    }
  }

  /**
   * Zoek de akte in het dossier waar ook de 'persoon' aan gekoppeld is.
   */
  private DossierAkte getAkteVanPersoon(DossierPersoon persoon, Dossier dossier) {

    for (DossierAkte akte : dossier.getAktes()) {
      DossierPersoon kind = getKind(akte);
      if (kind != null && kind.isPersoon(persoon)) {
        return akte;
      }
    }

    return null;
  }

  /**
   * Zoek de persoon met het type 'kind'
   */
  private DossierPersoon getKind(DossierAkte akte) {
    for (DossierPersoon p : akte.getPersonen()) {
      if (p.getDossierPersoonType().is(KIND)) {
        return p;
      }
    }
    return null;
  }

  /**
   * Als 1e kind is dan naamskeuze overnemen. Bij 2e kind altijd geen naamskeuze.
   */
  private void setNaamskeuze(int akteVolgcode, NaamskeuzeVanToepassingType type, DossierAkte dossierAkte,
      LvErkenning akte) {

    akte.setNaamskeuzeType(NaamskeuzeVanToepassingType.NVT);
    if (akteVolgcode < 2) { // Alleen bij de eerste de waarde overnemen. 2e kind heeft geen naamskeuze
      akte.setNaamskeuzeType(type);
    }

    // Overrulen als er kinderen zijn
    for (DossierPersoon p : dossierAkte.getPersonen()) {
      if (p.getDossierPersoonType().is(KIND)) {
        akte.setNaamskeuzeType(p.getNaamskeuzeType());
      }
    }
  }
}
