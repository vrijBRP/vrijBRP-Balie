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

package nl.procura.gba.web.modules.bs.huwelijk.page50;

import static java.util.Arrays.asList;
import static nl.procura.diensten.gba.ple.extensions.formats.BurgerlijkeStaatType.HUWELIJK;
import static nl.procura.diensten.gba.ple.extensions.formats.BurgerlijkeStaatType.PARTNERSCHAP;
import static nl.procura.gba.web.modules.bs.huwelijk.page50.Page50HuwelijkBean1.*;
import static nl.procura.standard.Globalfunctions.*;

import com.vaadin.ui.Window;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.modules.bs.common.pages.vereiste.BsVereistePage;
import nl.procura.gba.web.modules.bs.common.pages.vereiste.BsVereisteTable;
import nl.procura.gba.web.modules.bs.common.pages.vereiste.BurgerlijkeStandVereiste;
import nl.procura.gba.web.modules.bs.huwelijk.page50.info.Page50InfoWindow;
import nl.procura.gba.web.modules.zaken.curatele.CurateleWindow;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.algemeen.vereiste.DossierVereiste;
import nl.procura.gba.web.services.bs.huwelijk.DossierHuwelijk;
import nl.procura.gba.web.services.gba.ple.relatieLijst.RelatieLijstHandler;
import nl.procura.gba.web.services.gba.ple.relatieLijst.RelatieType;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class Page50Huwelijk extends BsVereistePage<DossierHuwelijk> {

  private Form1 form1 = null;

  public Page50Huwelijk() {
    super("Huwelijk/GPS - vereisten");
  }

  @Override
  public boolean checkPage() {

    super.checkPage();

    form1.commit();

    getZaakDossier().setRechtPartner1(form1.getBean().getRechtP1());
    getZaakDossier().setRechtPartner2(form1.getBean().getRechtP2());

    getProcessen().updateStatus();

    getApplication().getServices().getHuwelijkService().save(getDossier());

    return true;
  }

  @Override
  protected void addOptieComponenten() {

    BsVereisteTable table1 = new BsVereisteTable("l", "Leeftijd", getVereisten());
    BsVereisteTable table2 = new BsVereisteTable("c", "Wilsbepaling en Curatele", getVereisten());
    BsVereisteTable table3 = new BsVereisteTable("h", "Geen actueel huwelijk/GPS", getVereisten());
    BsVereisteTable table4 = new BsVereisteTable("b", "Geen te nauwe verwantschap", getVereisten());
    BsVereisteTable table5 = new BsVereisteTable("d", "Brondocumenten", getVereisten());

    DossierPersoon p1 = getZaakDossier().getPartner1();
    DossierPersoon p2 = getZaakDossier().getPartner2();

    Page50HuwelijkBean1 bean = new Page50HuwelijkBean1(p1, p2);
    bean.setRechtP1(getRecht(getZaakDossier().getRechtPartner1()));
    bean.setRechtP2(getRecht(getZaakDossier().getRechtPartner2()));
    form1 = new Form1(bean);

    addOptieComponenten(form1, "");
    addOptieComponenten(table5);
    addOptieComponenten(table1);
    addOptieComponenten(table2);
    addOptieComponenten(table3);
    addOptieComponenten(table4);

    super.addOptieComponenten();
  }

  @Override
  protected void addVereisten() {

    DossierPersoon p1 = getZaakDossier().getPartner1();
    DossierPersoon p2 = getZaakDossier().getPartner2();

    setLeeftijd("l1", p1);
    setLeeftijd("l2", p2);

    setCuratele("c1", p1);
    setCuratele("c2", p2);

    setHuwelijk("h1", p1);
    setHuwelijk("h2", p2);

    setBrondocument("d1", p1);
    setBrondocument("d2", p2);

    // TODO Huwelijk - banden met ouders controleren zetten

    // DossierPersoon p1o1 = getZaakDossier ().getPartner1Ouder1 ();
    // DossierPersoon p1o2 = getZaakDossier ().getPartner1Ouder2 ();

    // setBanden ("b1", p1, p2, p1o1, p1o2);
  }

  @Override
  protected Window getCuraleWindow() {

    DossierPersoon p1 = getZaakDossier().getPartner1();
    DossierPersoon p2 = getZaakDossier().getPartner2();
    return new CurateleWindow(asList(p1, p2));
  }

  @Override
  protected Window getInfoWindow() {
    return new Page50InfoWindow();
  }

  protected void setBanden(String id, DossierPersoon p1, DossierPersoon p2, DossierPersoon o1, DossierPersoon o2) {

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
          getVereisten().add(new BurgerlijkeStandVereiste(id, "Partners", "N", p1, p2));
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

  @Override
  protected void updateVereisten() {

    for (DossierVereiste v : getZaakDossier().getDossier().getVereisten()) {

      if (fil(v.getVoldaan())) {
        getVereiste(v.getIdKey()).setVoldoet(v.getVoldaan());
      }

      getVereiste(v.getIdKey()).setOverruleReason(v.getToelichting());
    }
  }

  private FieldValue getRecht(FieldValue recht) {
    return pos(recht.getValue()) ? recht : Landelijk.getNederland();
  }

  private RelatieType getRelatieType(DossierPersoon p1, DossierPersoon p2) {

    if (p1.isIngeschreven() && p2.isIngeschreven()) {

      BasePLExt pl1 = getPl(p1);
      BasePLExt pl2 = getPl(p2);

      return RelatieLijstHandler.getRelatieType(pl1, pl2);
    }

    return RelatieType.ONBEKEND;
  }

  private void setBrondocument(String id, DossierPersoon p) {

    getVereisten().add(new BurgerlijkeStandVereiste(id, "N.v.t.", "N", p));
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

  private void setHuwelijk(String id, DossierPersoon p) {

    if (pos(p.getBurgerServiceNummer().getValue())) {

      BasePLExt pl = getPersoonslijst(p.getBurgerServiceNummer());

      if (pl.getPersoon().getBurgerlijkeStand().getStatus().getType().is(HUWELIJK, PARTNERSCHAP)) {

        getVereisten().add(new BurgerlijkeStandVereiste(id, "Actueel huwelijk/GPS", "N", p));
      } else {
        getVereisten().add(new BurgerlijkeStandVereiste(id, "Geen actueel huwelijk/GPS", "J", p));
      }
    } else {

      getVereisten().add(new BurgerlijkeStandVereiste(id, "Geen informatie beschikbaar", "", p));
    }
  }

  private void setLeeftijd(String id, DossierPersoon p) {

    String waarde = aval(p.getGeboorte().getLeeftijd()) >= 18 ? "J" : "N";
    getVereisten().add(new BurgerlijkeStandVereiste(id, p.getGeboorte().getDatum_leeftijd(), waarde, p));
  }

  public class Form1 extends Page50HuwelijkForm1 {

    private Form1(Page50HuwelijkBean1 bean) {

      setBean(bean);
    }

    @Override
    public void init() {

      setCaption("Toegepast recht");

      setColumnWidths("190px", "");

      setOrder(NATIOP1, NATIOP2, RECHTP1, RECHTP2);
    }
  }
}
