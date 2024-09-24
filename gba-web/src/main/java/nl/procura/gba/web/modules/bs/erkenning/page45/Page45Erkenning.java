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

package nl.procura.gba.web.modules.bs.erkenning.page45;

import static nl.procura.standard.Globalfunctions.*;

import com.vaadin.ui.Window;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.modules.bs.common.pages.vereiste.BsVereistePage;
import nl.procura.gba.web.modules.bs.common.pages.vereiste.BsVereisteTable;
import nl.procura.gba.web.modules.bs.common.pages.vereiste.BurgerlijkeStandVereiste;
import nl.procura.gba.web.modules.bs.common.utils.BsOuderUtils;
import nl.procura.gba.web.modules.zaken.curatele.CurateleWindow;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.algemeen.vereiste.DossierVereiste;
import nl.procura.gba.web.services.bs.erkenning.DossierErkenning;
import nl.procura.gba.web.services.gba.ple.relatieLijst.RelatieLijstHandler;
import nl.procura.gba.web.services.gba.ple.relatieLijst.RelatieType;
import nl.procura.commons.core.exceptions.NotSupportedException;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.commons.core.exceptions.ProExceptionSeverity;

public class Page45Erkenning extends BsVereistePage<DossierErkenning> {

  public Page45Erkenning() {
    super("Erkenning - vereisten");
  }

  @Override
  public boolean checkPage() {

    super.checkPage();

    for (DossierVereiste v : getZaakDossier().getDossier().getVereisten()) {
      if (!v.isHeeftVoldaan()) {
        throw new ProException(ProExceptionSeverity.WARNING, "Er is niet voldaan aan alle vereisten.");
      }
    }

    getApplication().getServices().getErkenningService().save(getDossier());

    return true;
  }

  @Override
  protected void addOptieComponenten() {

    BsVereisteTable table1 = new BsVereisteTable("l", "Leeftijd", getVereisten());
    BsVereisteTable table2 = new BsVereisteTable("c", "Curatele", getVereisten());
    BsVereisteTable table3 = new BsVereisteTable("b", "Geen te nauwe verwantschap", getVereisten());

    addOptieComponenten(table1);
    addOptieComponenten(table2);
    addOptieComponenten(table3);

    super.addOptieComponenten();
  }

  @Override
  protected void addVereisten() {

    DossierPersoon moeder = getZaakDossier().getMoeder();
    DossierPersoon erkenner = getZaakDossier().getErkenner();

    DossierPersoon ouder1 = BsOuderUtils.getOuder(getServices(), moeder, GBACat.OUDER_1);
    DossierPersoon ouder2 = BsOuderUtils.getOuder(getServices(), moeder, GBACat.OUDER_2);

    setLeeftijd("l1", erkenner);
    setCuratele("c1", erkenner);

    setBanden("b1", moeder, erkenner, ouder1, ouder2);
  }

  @Override
  protected Window getCuraleWindow() {
    return new CurateleWindow(getPl(getZaakDossier().getErkenner()));
  }

  @Override
  protected Window getInfoWindow() {
    // return new Page50InfoWindow ();
    throw new NotSupportedException();
  }

  /*
   ===============
   Niet meer nodig
   ===============
   private void setHuwelijk (String id, DossierPersoon erkenner, DossierPersoon moeder) {
  
       if (pos (erkenner.getBurgerServiceNummer ().getValue ())) {
  
           BasisPLWrapper pl = getPl (erkenner.getBurgerServiceNummer ());
  
           if (pl.getHuwelijk ().isActueelOfMutatieRecord ()) {
  
               // Mag wel getrouwd zijn met de moeder. Niet met ander persoon.
  
               long bsnPartner = along (pl.getHuwelijk ().getActueelOfMutatieRecord ().getElementW (GBAElement.BURGERSERVICENUMMER).getWaarde ());
  
               if (along (moeder.getBurgerServiceNummer ().getValue ()) == bsnPartner) {
  
                   getVereisten ().add (new BurgerlijkeStandVereiste (id, "Actueel huwelijk/GPS met moeder", "J", erkenner));
                   }
               else {
  
                   getVereisten ().add (new BurgerlijkeStandVereiste (id, "Actueel huwelijk/GPS", "N", erkenner));
                   }
               }
           else {
  
               getVereisten ().add (new BurgerlijkeStandVereiste (id, "Geen actueel huwelijk/GPS", "J", erkenner));
               }
           }
       else {
  
           getVereisten ().add (new BurgerlijkeStandVereiste (id, "Geen informatie beschikbaar", "", erkenner));
           }
       }*/

  @Override
  protected void updateVereisten() {

    for (DossierVereiste v : getZaakDossier().getDossier().getVereisten()) {

      if (fil(v.getVoldaan())) {
        getVereiste(v.getIdKey()).setVoldoet(v.getVoldaan());
      }

      getVereiste(v.getIdKey()).setOverruleReason(v.getToelichting());
    }
  }

  private RelatieType getRelatieType(DossierPersoon p1, DossierPersoon p2) {

    if (p1.isIngeschreven() && p2.isIngeschreven()) {

      return RelatieLijstHandler.getRelatieType(getPl(p1), getPl(p2));
    }

    return RelatieType.ONBEKEND;
  }

  private void setBanden(String id, DossierPersoon p1, DossierPersoon p2, DossierPersoon o1, DossierPersoon o2) {

    if (p1.isIngeschreven() && p2.isIngeschreven()) {

      boolean halfBroerZus = false;
      boolean broerZus = false;

      RelatieType relOuder1 = getRelatieType(p2, o1);
      RelatieType relOuder2 = getRelatieType(p2, o2);

      if (relOuder1 == RelatieType.OUDER) {
        halfBroerZus = true;
      }

      if (relOuder2 == RelatieType.OUDER) {
        broerZus = halfBroerZus;
        halfBroerZus = true;
      }

      if (broerZus) {
        getVereisten().add(new BurgerlijkeStandVereiste(id, "Broer / Zus", "N", p1, p2));
        return;
      }

      if (halfBroerZus) {
        getVereisten().add(new BurgerlijkeStandVereiste(id, "Half-broer / -Zus", "N", p1, p2));
        return;
      }

      RelatieType relatieType = getRelatieType(p1, p2);

      switch (relatieType) {

        case PARTNER:
          getVereisten().add(new BurgerlijkeStandVereiste(id, "Partners", "J", p1, p2));
          return;

        case KIND:
        case MEERDERJARIG_KIND:
        case OUDER:
          getVereisten().add(new BurgerlijkeStandVereiste(id, "Ouder / kind", "N", p1, p2));
          return;

        default:
          getVereisten().add(new BurgerlijkeStandVereiste(id, "Geen verwantschap gevonden", "J", p1, p2));
      }
    }

    getVereisten().add(new BurgerlijkeStandVereiste(id, "Geen informatie beschikbaar", "", p1, p2));
  }

  private void setCuratele(String id, DossierPersoon p) {

    if (pos(p.getBurgerServiceNummer().getValue())) {

      BasePLExt pl = getPersoonslijst(p.getBurgerServiceNummer());

      if (pl.getGezag().staatOnderCuratele()) {

        getVereisten().add(new BurgerlijkeStandVereiste(id, "Curatele", "N", p));
      } else {
        getVereisten().add(new BurgerlijkeStandVereiste(id, "Geen curatele", "J", p));
      }
    } else {

      getVereisten().add(new BurgerlijkeStandVereiste(id, "Geen informatie beschikbaar", "", p));
    }
  }

  private void setLeeftijd(String id, DossierPersoon p) {

    String waarde = aval(p.getGeboorte().getLeeftijd()) >= 18 ? "J" : "N";
    getVereisten().add(new BurgerlijkeStandVereiste(id, p.getGeboorte().getDatum_leeftijd(), waarde, p));
  }
}
