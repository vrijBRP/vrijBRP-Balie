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

package nl.procura.gba.web.modules.zaken.rijbewijs.page11;

import static nl.procura.gba.common.MiscUtils.trimPostcode;
import static nl.procura.standard.Globalfunctions.*;

import java.util.HashMap;
import java.util.Map.Entry;

import com.vaadin.ui.Label;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.formats.Adres;
import nl.procura.gba.web.components.containers.LandContainer;
import nl.procura.gba.web.components.containers.NatioContainer;
import nl.procura.gba.web.components.containers.PlaatsContainer;
import nl.procura.gba.web.modules.zaken.rijbewijs.RijbewijsPage;
import nl.procura.gba.web.modules.zaken.rijbewijs.page5.Page5Rijbewijs;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.rdw.messages.P1651;
import nl.procura.rdw.processen.p1651.f08.AANRYBKOVERZ;
import nl.procura.rdw.processen.p1651.f08.ADRESNATPGEG;
import nl.procura.rdw.processen.p1651.f08.NATPERSOONGEG;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.layout.table.TableLayout;
import nl.procura.validation.Anummer;
import nl.procura.validation.Bsn;

/**
 * Vergelijkingscherm
 */
public class Page11Rijbewijs extends RijbewijsPage {

  private final P1651               p1651;
  private TableLayout               l   = new TableLayout();
  private HashMap<Object, Object[]> map = new HashMap<>();

  public Page11Rijbewijs(P1651 p1651) {

    super("Rijbewijsaanvraag: tonen geselecteerde personen");

    this.p1651 = p1651;

    setMargin(true);

    addButton(buttonPrev);
    addButton(buttonNext);

    setInfo("Persoonsgegevens vergelijken",
        "De ambtenaar controleert de gegevens uit de BRP met de gegevens van het CRB. <br/>"
            + "De ambtenaar bepaalt of de juist persoon is geselecteerd bij het CRB");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      l = new TableLayout();

      map = new HashMap<>();

      l.addStyleName("rdwvergelijk");

      l.setColumnWidths("160px", "300px", "");

      add(1, "A-nummer");
      add(2, "BSN");
      add(3, "Geslachtsnaam");
      add(4, "Voorvoegsel");
      add(5, "Titel/predikaat");
      add(6, "Voornaam");
      add(7, "Geboren");
      add(8, "-");
      add(9, "-");
      add(10, "Nationaliteit");
      add(11, "Adres");
      add(12, "-");
      add(13, "Locatie");
      add(14, "Postcode");
      add(15, "Woonplaats");

      setGBA();
      setCRB();

      set();

      addComponent(l);
    }

    super.event(event);
  }

  @Override
  public void onNextPage() {

    getNavigation().goToPage(new Page5Rijbewijs(p1651));

    super.onNextPage();
  }

  private void add(Object id, Object label) {

    Object[] array = { label, "", "" };

    map.put(id, array);
  }

  private void put(Object id, int arrayId, Object val) {

    map.get(id)[arrayId] = val;
  }

  private void set() {

    for (Entry<Object, Object[]> e : map.entrySet()) {

      Object label = e.getValue()[0];
      Object gba = e.getValue()[1];
      Object crb = e.getValue()[2];

      l.addLabel(astr(label));
      l.addData(new Label(astr(gba)));

      Label crbLabel = new Label(astr(crb));
      crbLabel.setStyleName(
          astr(gba).trim().equals(astr(crb).trim()) ? GbaWebTheme.TEXT.GREEN : GbaWebTheme.TEXT.RED);

      l.addData(crbLabel);
    }
  }

  private void setCRB() {

    PlaatsContainer plaatsen = new PlaatsContainer();
    LandContainer landen = new LandContainer();
    NatioContainer natios = new NatioContainer();

    AANRYBKOVERZ p1651f8 = (AANRYBKOVERZ) p1651.getResponse().getObject();

    NATPERSOONGEG g = p1651f8.getNatpersoongeg();

    Anummer anr = new Anummer(astr(g.getGbanrnatp()));
    Bsn snr = new Bsn(astr(g.getFiscnrnatp()));

    if (anr.isCorrect()) {
      put(1, 2, anr.getFormatAnummer());
    }

    if (snr.isCorrect()) {
      put(2, 2, snr.getFormatBsn());
    }

    put(3, 2, g.getGeslnaamnatp());
    put(4, 2, g.getVoorvoegnatp());
    put(5, 2, g.getAdelprednatp());
    put(6, 2, g.getVoornaamnatp());

    ProcuraDate geboortedatum = new ProcuraDate(astr(g.getGebdatnatp()));

    if (geboortedatum.isCorrect()) {
      put(7, 2, geboortedatum.getFormatDate());
    }

    long cgebplaats = along(g.getAutorcgebpl());
    String sgebplaats = g.getGebplbuitenl();

    if ((cgebplaats > 0) && (cgebplaats <= 1999)) {
      sgebplaats = plaatsen.get(astr(cgebplaats)).getDescription();
    }

    put(8, 2, sgebplaats);

    if (pos(g.getAutorcgebpl()) && (aval(g.getAutorcgebpl()) > 1999)) {
      put(9, 2, landen.get(g.getAutorcgebpl().toString()).getDescription());
    } else {
      put(9, 2, "Nederland");
    }

    long cnatio = along(g.getNationalpers());
    String snatio = "";

    if (cnatio > 0) {
      snatio = natios.get(astr(cnatio)).getDescription();
    }

    put(10, 2, snatio);

    try {

      ADRESNATPGEG a = p1651f8.getAdresnatpgeg();

      put(11, 2, a.getStraatnatp());
      put(12, 2, a.getHuisnrnatp() + " " + a.getHuistvnatp());
      put(13, 2, a.getLocregelnatp());
      put(14, 2, trimPostcode(a.getPostcnnatp() + a.getPostcanatp()));
      put(15, 2, a.getWoonplnatp());

    } catch (Exception e) {}
  }

  private void setGBA() {

    BasePLExt pl = getPl();

    Anummer anr = new Anummer(pl.getPersoon().getAnr().getVal());
    Bsn snr = new Bsn(pl.getPersoon().getBsn().getVal());

    if (anr.isCorrect()) {
      put(1, 1, anr.getFormatAnummer());
    }

    if (snr.isCorrect()) {
      put(2, 1, snr.getFormatBsn());
    }

    put(3, 1, pl.getPersoon().getNaam().getGeslachtsnaam().getValue().getDescr());
    put(4, 1, pl.getPersoon().getNaam().getVoorvoegsel().getValue().getDescr());
    put(5, 1, pl.getPersoon().getNaam().getTitel().getValue().getVal());
    put(6, 1, pl.getPersoon().getNaam().getEersteVoornaam());

    ProcuraDate geboortedatum = new ProcuraDate(pl.getPersoon().getGeboorte().getDatum());

    if (geboortedatum.isCorrect()) {
      put(7, 1, geboortedatum.getFormatDate());
    }

    put(8, 1, pl.getPersoon().getGeboorte().getGeboorteplaats().getDescr());
    put(9, 1, pl.getPersoon().getGeboorte().getGeboorteland().getDescr());

    put(10, 1, pl.getNatio().getNationaliteiten());

    Adres adr = pl.getVerblijfplaats().getAdres();

    put(11, 1, adr.getStraat().getValue().getDescr());

    String hnr = adr.getHuisnummer().getValue().getVal();
    String hnrl = adr.getHuisletter().getValue().getVal();
    String hnrt = adr.getHuisnummertoev().getValue().getVal();
    String hnra = adr.getHuisnummeraand().getValue().getVal();

    String adres = hnr + " " + hnrl + " " + hnrt + " " + hnra;

    put(12, 1, adres);

    put(13, 1, adr.getLocatie().getValue().getDescr());
    put(14, 1, adr.getPostcode().getValue().getVal());

    String gem = adr.getGemeente().getValue().getDescr();
    String gemDeel = adr.getGemeentedeel().getValue().getDescr();

    put(15, 1, fil(gemDeel) ? gemDeel : gem);
  }
}
