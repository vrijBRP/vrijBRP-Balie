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
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkte;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkteRegistersoort;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.bs.geboorte.naamskeuzebuitenproweb.NaamskeuzeBuitenProweb;
import nl.procura.gba.web.services.bs.lv.LatereVermelding;
import nl.procura.gba.web.services.bs.lv.naamskeuze.LvNaamskeuze;
import nl.procura.gba.web.services.bs.naamskeuze.DossierNaamskeuze;
import nl.procura.gba.web.services.bs.naamskeuze.NaamskeuzeType;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class LatereVermeldingNaamskeuzeGeboorte implements LatereVermelding<LvNaamskeuze> {

  private final List<LvNaamskeuze> aktes = new ArrayList<>();

  public LatereVermeldingNaamskeuzeGeboorte(DossierGeboorte geboorte) {
    if (geboorte.getVragen().heeftNaamskeuzeVoorGeboorte()) {
      for (DossierAkte geboorteAkte : geboorte.getDossier().getAktes()) {
        DossierNaamskeuze naamskeuze = geboorte.getNaamskeuzeVoorGeboorte();
        for (DossierAkte naamskeuzeAkte : naamskeuze.getDossier().getAktes()) {
          LvNaamskeuze akte = addNaamskeuze(naamskeuzeAkte, naamskeuze);
          akte.getGeboorteAkte().setNummer(geboorteAkte.getAkte());
          akte.getGeboorteAkte().setJaar(astr(geboorteAkte.getJaar()));
          akte.getGeboorteAkte().setPlaats(new FieldValue()); // Niet opgeslagen bij geboorte
          aktes.add(akte);
        }
      }
    } else if (geboorte.getVragen().heeftNaamskeuzeBuitenProweb()) {
      addNaamskeuzeProweb(geboorte);
    }
  }

  @Override
  public List<LvNaamskeuze> getAktes() {
    return aktes;
  }

  /**
   * Naamskeuze binnen proweb
   */
  private LvNaamskeuze addNaamskeuze(DossierAkte dossierAkte, DossierNaamskeuze naamskeuze) {
    LvNaamskeuze akte = new LvNaamskeuze();
    akte.setKind(dossierAkte.getAktePersoon());
    akte.setLand(Landelijk.getNederland());
    akte.setGemeente(naamskeuze.getGemeente());
    akte.setBuitenlandsePlaats("");
    akte.setDatum(dossierAkte.getDatumIngang());
    akte.setGemeente(naamskeuze.getGemeente());
    akte.setMoeder(naamskeuze.getMoeder());
    akte.setPartner(naamskeuze.getPartner());
    akte.setNaamskeuzeType(naamskeuze.getDossierNaamskeuzeType());
    akte.setTitel(naamskeuze.getKeuzeTitel());
    akte.setVoorvoegsel(naamskeuze.getKeuzeVoorvoegsel());
    akte.setGeslachtsnaam(naamskeuze.getKeuzeGeslachtsnaam());
    akte.setAkte(dossierAkte);

    return akte;
  }

  /**
   * Naamskeuze buiten Proweb
   */
  private void addNaamskeuzeProweb(DossierGeboorte geboorte) {

    NaamskeuzeBuitenProweb naamskeuze = geboorte.getNaamskeuzeBuitenProweb();

    for (DossierAkte geboorteAkte : geboorte.getDossier().getAktes()) {
      LvNaamskeuze akte = new LvNaamskeuze();
      akte.setDatum(naamskeuze.getDatum());

      akte.getGeboorteAkte().setNummer(geboorteAkte.getAkte());
      akte.getGeboorteAkte().setBrpNummer(geboorteAkte.getBrpAkte());
      akte.getGeboorteAkte().setJaar(astr(geboorteAkte.getJaar()));
      akte.getGeboorteAkte().setPlaats(new FieldValue()); // Wordt niet gevuld bij geboorte

      akte.setLand(naamskeuze.getLand());
      akte.setGemeente(naamskeuze.getGemeente());
      akte.setBuitenlandsePlaats(naamskeuze.getBuitenlandsePlaats());
      akte.setNaamskeuzeType(NaamskeuzeType.NAAMSKEUZE_VOOR_GEBOORTE);

      ProcuraDate naamskeuzesDatum = new ProcuraDate(naamskeuze.getDatum().getDate());

      DossierAkte d = new DossierAkte();
      d.setJaar(toBigDecimal(naamskeuzesDatum.getYear()));
      d.setRegistersoort(toBigDecimal(DossierAkteRegistersoort.AKTE_ERKENNING_NAAMSKEUZE));
      d.setRegisterdeel(""); // Nvt
      d.setVnr(toBigDecimal(naamskeuze.getAktenummer()));
      d.setDatumIngang(naamskeuze.getDatum());

      akte.setAkte(d);
      akte.setKind(geboorteAkte.getAktePersoon());
      akte.setMoeder(geboorte.getMoeder());
      akte.setPartner(geboorte.getVader());
      akte.setNaamskeuzeType(NaamskeuzeType.NAAMSKEUZE_VOOR_GEBOORTE);

      for (DossierPersoon persoon : geboorteAkte.getPersonen()) {
        if (persoon.getDossierPersoonType().is(KIND)) {
          akte.setTitel(persoon.getTitel());
          akte.setVoorvoegsel(persoon.getVoorvoegsel());
          akte.setGeslachtsnaam(persoon.getGeslachtsnaam());
        }
      }
      aktes.add(akte);
    }
  }
}
