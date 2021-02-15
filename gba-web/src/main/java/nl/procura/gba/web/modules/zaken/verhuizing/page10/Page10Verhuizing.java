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

package nl.procura.gba.web.modules.zaken.verhuizing.page10;

import static ch.lambdaj.Lambda.*;
import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.gba.web.modules.zaken.verhuizing.page10.Page10VerhuizingBean1.AANGEVER;
import static nl.procura.gba.web.modules.zaken.verhuizing.page10.Page10VerhuizingBean1.TOELICHTING;
import static nl.procura.gba.web.services.gba.ple.relatieLijst.AangifteSoort.MEERDERJARIGE_GEMACHTIGDE;
import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.standard.exceptions.ProExceptionSeverity.INFO;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.standard.exceptions.ProExceptionType.SELECT;
import static org.hamcrest.Matchers.equalTo;

import org.hamcrest.Matcher;

import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;

import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.Cat1PersoonExt;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.TableImage;
import nl.procura.gba.web.components.containers.GbaContainer;
import nl.procura.gba.web.components.dialogs.IndicatieDialog;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.modules.zaken.verhuizing.AdresType;
import nl.procura.gba.web.modules.zaken.verhuizing.VerhuisAanvraagPage;
import nl.procura.gba.web.modules.zaken.verhuizing.VerhuisAdres;
import nl.procura.gba.web.modules.zaken.verhuizing.VerhuisRelatie;
import nl.procura.gba.web.modules.zaken.verhuizing.page11.Page11Verhuizing;
import nl.procura.gba.web.modules.zaken.verhuizing.page12.Page12Verhuizing;
import nl.procura.gba.web.modules.zaken.verhuizing.page13.Page13Verhuizing;
import nl.procura.gba.web.modules.zaken.verhuizing.page19.Page19Verhuizing;
import nl.procura.gba.web.modules.zaken.verhuizing.page20.Page20Verhuizing;
import nl.procura.gba.web.modules.zaken.verhuizing.page24.Page24Verhuizing;
import nl.procura.gba.web.services.gba.ple.relatieLijst.AangifteSoort;
import nl.procura.gba.web.services.gba.ple.relatieLijst.Relatie;
import nl.procura.gba.web.services.gba.ple.relatieLijst.RelatieLijst;
import nl.procura.gba.web.services.gba.ple.relatieLijst.RelatieType;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraag;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisPersoon;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisType;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuizingService;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.theme.twee.Icons;

import ch.lambdaj.function.matcher.HasArgumentWithValue;

/**
 * Nieuwe aanvraag
 */

public class Page10Verhuizing extends VerhuisAanvraagPage {

  private static final int      MINIMALE_LEEFTIJD = 16;
  private final Button          buttonPersoon     = new Button("Toevoegen persoon (F3)");
  private final Button          buttonAdres       = new Button("Huidig adres vervangen (F4)");
  private final Button          buttonPresent     = new Button("Presentievragen");
  private final Table1          table1            = new Table1();
  private final OptieLayout     optieLayout       = new OptieLayout();
  private Page10VerhuizingForm1 form1             = null;

  public Page10Verhuizing(VerhuisAanvraag verhuisAanvraag) {
    super("Verhuizing: personen", verhuisAanvraag);
    setAanvraag(verhuisAanvraag);
    addButton(buttonPrev);
    addButton(buttonNext);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      String msg = "Selecteer de te verhuizen personen. Als het adres waar vandaan wordt verhuisd een ander adres is, "
          + "maak dan gebruik van de optie Huidig adres vervangen (F4). "
          + "Met Toevoegen persoon (F3) kan een niet gerelateerde persoon worden toegevoegd. "
          + "Druk op Volgende (F2) om verder te gaan.";

      if (isMinderJarig()) {
        msg += "<hr/><b>Deze persoon is jonger dan 16 jaar en daarmee niet bevoegd om zelf aangifte te doen</b>";
      }

      setInfo(msg);

      form1 = new Page10VerhuizingForm1();

      optieLayout.getLeft().addComponent(form1);

      optieLayout.getRight().setWidth("200px");
      optieLayout.getRight().setCaption("Opties");

      buttonPersoon.setDescription("Niet-gerelateerden toevoegen");
      buttonAdres.setDescription("Een ander vertrekadres dan het adres van de gezochte persoon");

      optieLayout.getRight().addButton(buttonPersoon, this);

      initForm();

      addComponent(optieLayout);

      addExpandComponent(table1);
    } else if (event.isEvent(AfterReturn.class)) {

      reload();
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if ((button == buttonPersoon) || (keyCode == KeyCode.F3)) {

      getNavigation().addPage(new Page11Verhuizing(getHuidigVerhuisType(), getHuidigAdres()));
    } else if ((button == buttonAdres) || (buttonAdres.isVisible() && (keyCode == KeyCode.F4))) {

      getNavigation().addPage(new Page12Verhuizing(form1.getHuidigAdresVeld()));
    } else if (button == buttonPresent) {

      if (!table1.isSelectedRecords()) {
        throw new ProException(INFO, "Selecteer minimaal één persoon.");
      }

      getNavigation().addPage(
          new Page24Verhuizing(getAanvraag(), table1.getSelectedValues(VerhuisRelatie.class)));
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onNextPage() {

    form1.commit();

    if (!table1.isSelectedRecords()) {
      throw new ProException(INFO, "Selecteer minimaal één persoon.");
    }

    if (isMachtigingGeselecteerd()) {
      if (isMachtigingMogelijk()) {
        toonMachtigingScherm();
      } else {
        toonMachtigingWaarschuwing();
      }
    } else {
      doNextPage();
    }

    super.onNextPage();
  }

  private void changeAangeverVeld() {

    // Bij wijziging adres opnieuw lijst vullen
    table1.init();

    FieldValue fv = (FieldValue) form1.getField(AANGEVER).getValue();

    form1.getField(TOELICHTING).setVisible(fil(astr(fv.getValue())));

    form1.repaint();
  }

  private void doNextPage() {

    vulAanvraag();

    if (form1.getBean().getTypeVerhuizing() == VerhuisType.EMIGRATIE) {

      int aantalAlle = table1.getRecords().size();
      int aantalSelect = table1.getSelectedRecords().size();

      // Door naar emigratiescherm
      getNavigation().goToPage(new Page19Verhuizing(getAanvraag(), (aantalSelect == aantalAlle)));

    } else if (form1.getBean().getTypeVerhuizing() == VerhuisType.HERVESTIGING) {

      if (isNogPresentieVraagNodig()) {

        getNavigation().goToPage(
            new Page24Verhuizing(getAanvraag(), table1.getSelectedValues(VerhuisRelatie.class)));
      } else {

        // Door naar hervestigingsscherm
        getNavigation().goToPage(new Page20Verhuizing(getAanvraag()));
      }
    } else {

      // Door naar binnen-/intergem. verh. scherm
      getNavigation().goToPage(new Page13Verhuizing(getAanvraag()));
    }
  }

  /**
   * Het huidig geselecteerd adres
   */
  private VerhuisAdres getHuidigAdres() {
    return (VerhuisAdres) form1.getHuidigAdresVeld().getValue();
  }

  /**
   * Het huidig geselecteerd verhuistype
   */
  private VerhuisType getHuidigVerhuisType() {
    return (VerhuisType) form1.getSoortVeld().getValue();
  }

  private String getStatus(VerhuisType type, Relatie relatie) {

    boolean isGeemigreerd = relatie.getPl().getPersoon().getStatus().isEmigratie();
    boolean isRni = relatie.getPl().getPersoon().getStatus().isRni();
    boolean isMinister = relatie.getPl().getPersoon().getStatus().isMinisterieelBesluit();

    StringBuilder sb = new StringBuilder();

    switch (type) {

      case HERVESTIGING:
        if (!isGeemigreerd && !isRni && !isMinister) {
          sb.append("Geen sprake van emigratie, RNI of ministrieel besluit, ");
        }

        break;

      default:
        if (isGeemigreerd) {
          sb.append("Geëmigreerd, ");
        }

        if (isRni) {
          sb.append("PL aangelegd in de RNI, ");
        }
    }

    if (heeftOpenstaandeAanvragen(relatie.getPl())) {
      sb.append("Niet-verwerkte aanvraag, ");
    }

    if (relatie.getPl().getPersoon().getStatus().isOverleden()) {
      sb.append("Overleden, ");
    }

    return trim(" - " + trim(sb.toString()));
  }

  private boolean heeftOpenstaandeAanvragen(BasePLExt pl) {

    if (getApplication() != null) {
      VerhuizingService d = getApplication().getServices().getVerhuizingService();
      for (Zaak zaak : d.getMinimalZaken(new ZaakArgumenten(pl))) {
        if (!zaak.getStatus().isEindStatus()) {
          return true;
        }
      }
    }

    return false;
  }

  private void initForm() {

    form1.getHuidigAdresVeld().setDataSource(new AdresContainer());
    form1.getAangeverVeld().setDataSource(new AangeverContainer());
    form1.getSoortVeld().setDataSource(new SoortContainer());

    form1.getHuidigAdresVeld().addListener((ValueChangeListener) event -> {
      // Set verhuistype veld opnieuw in
      form1.getSoortVeld().setDataSource(new SoortContainer());
      reload();
    });

    form1.getAangeverVeld().addListener((ValueChangeListener) event -> changeAangeverVeld());

    form1.getSoortVeld().addListener((ValueChangeListener) event -> {
      // Zet eerste waarde. Na wijzigen datasource wordt deze weer op null gezet.
      if (form1.getSoortVeld().getValue() == null) {
        form1.getSoortVeld().setValue(new SoortContainer().getItemIds().iterator().next());
      }

      initHervestiging();
      reload();
    });

    initHervestiging();

    changeAangeverVeld();
  }

  private void initHervestiging() {

    boolean isHervestiging = form1.getSoortVeld().getValue() == VerhuisType.HERVESTIGING;

    buttonAdres.setVisible(!isHervestiging);

    if (isHervestiging) {

      optieLayout.getRight().removeComponent(buttonAdres);
      optieLayout.getRight().addButton(buttonPresent, this);
    } else {

      optieLayout.getRight().removeComponent(buttonPresent);
      optieLayout.getRight().addButton(buttonAdres, this);
    }
  }

  private boolean isMachtigingGeselecteerd() {

    Matcher<VerhuisRelatie> matcher = having(on(VerhuisRelatie.class).getSelect().getValue(),
        equalTo(MEERDERJARIGE_GEMACHTIGDE));

    return exists(table1.getSelectedValues(VerhuisRelatie.class), matcher);
  }

  private boolean isMachtigingMogelijk() {
    return form1.getBean().getTypeVerhuizing() != VerhuisType.EMIGRATIE;
  }

  private boolean isMinderJarig() {
    return getPl().getPersoon().getGeboorte().getLeeftijd() < MINIMALE_LEEFTIJD;
  }

  private boolean isNogPresentieVraagNodig() {
    HasArgumentWithValue<Object, Boolean> matcher = having(on(VerhuisRelatie.class).isPresentievraag(),
        equalTo(false));
    return exists(table1.getSelectedValues(VerhuisRelatie.class), matcher);
  }

  /**
   * reload table en form
   */
  private void reload() {

    if (getApplication() != null) {

      // Bij wijziging adres opnieuw lijst vullen
      table1.init();

      // Repaint form
      form1.repaint();
    }
  }

  /**
   * Zoek de huidige gerelateerde huisgenoten van de aanvrager
   */
  private VerhuisAdres setInitieleAdresRelaties(VerhuisAdres adres) {
    RelatieLijst rl = getApplication().getServices().getPersonenWsService().getRelatieLijst(getPl(), false);
    for (Relatie relatie : rl.getSortedRelaties()) {
      if (relatie.isHuisgenoot()) {
        adres.addVerhuisRelatie(new VerhuisRelatie(relatie));
      }
    }

    return adres;
  }

  private void toonMachtigingScherm() {

    getWindow().addWindow(new ConfirmDialog("Is er een machtiging overgelegd?") {

      @Override
      public void buttonYes() {

        closeWindow();

        doNextPage();

      }
    });
  }

  private void toonMachtigingWaarschuwing() {

    getWindow().addWindow(
        new IndicatieDialog("Waarschuwing", "", "Emigratie door een gemachtigde is niet meer mogelijk.",
            "300px"));
  }

  /**
   * Vul de aanvraag aan
   */
  private void vulAanvraag() {

    getAanvraag().setDatumTijdInvoer(new DateTime());
    getAanvraag().setDatumIngang(new DateTime());
    getAanvraag().setIngevoerdDoor(new UsrFieldValue(getApplication().getServices().getGebruiker()));

    // Voeg personen toe aan aanvraag

    getAanvraag().getPersonen().clear();

    for (VerhuisRelatie vr : table1.getSelectedValues(VerhuisRelatie.class)) {

      VerhuisPersoon vp = new VerhuisPersoon();
      Cat1PersoonExt persoon = vr.getRelatie().getPl().getPersoon();
      BasePLValue gem = vr.getRelatie().getPl().getVerblijfplaats().getAdres().getGemeente().getValue();

      if (fil(getStatus(getHuidigVerhuisType(), vr.getRelatie()))) {
        String naam = vr.getRelatie().getPl().getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam();
        throw new ProException(SELECT, WARNING, naam + " komt niet voor dit type verhuizing in aanmerking.");
      }

      vp.setAangifte((AangifteSoort) vr.getSelect().getValue());
      vp.setAnummer(new AnrFieldValue(persoon.getAnr().getVal()));
      vp.setBurgerServiceNummer(new BsnFieldValue(persoon.getBsn().getVal()));
      vp.setGemeenteHerkomst(new FieldValue(gem.getVal(), gem.getDescr()));

      getAanvraag().getPersonen().add(vp);
    }

    // Voeg huidig adres toe aan de aanvraag
    AddressConverter.toVerhuisAanvraagAdres(getAanvraag().getHuidigAdres(), getHuidigAdres());

    // Verhuistype
    getAanvraag().setTypeVerhuizing(form1.getBean().getTypeVerhuizing());

    // Aangever
    FieldValue aangever = form1.getBean().getAangever();

    getAanvraag().getAangever().setAnummer(new AnrFieldValue());
    getAanvraag().getAangever().setBurgerServiceNummer(new BsnFieldValue());
    getAanvraag().getAangever().setPersoon(null);
    getAanvraag().getAangever().setHoofdInstelling(false);
    getAanvraag().getAangever().setAmbtshalve(false);

    if (AangifteSoort.HOOFDINSTELLING.getCode().equals(aangever.getValue())) {
      getAanvraag().getAangever().setHoofdInstelling(true);
    } else if (AangifteSoort.AMBTSHALVE.getCode().equals(aangever.getValue())) {
      getAanvraag().getAangever().setAmbtshalve(true);
    } else {
      getAanvraag().getAangever().setAnummer(new AnrFieldValue(getPl().getPersoon().getAnr().getVal()));
      getAanvraag().getAangever().setBurgerServiceNummer(
          new BsnFieldValue(getPl().getPersoon().getBsn().getVal()));
    }

    getAanvraag().getAangever().setToelichting(form1.getBean().getToelichting());

    // Een ID opvragen in het Zaak-DMS
    getApplication().getServices().getZaakIdentificatieService().getDmsZaakId(getAanvraag());
  }

  class AangeverContainer extends GbaContainer {

    public AangeverContainer() {

      if (!isMinderJarig()) {
        addItem(new FieldValue("", getPl().getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam()));
      }

      addItem(new FieldValue(AangifteSoort.AMBTSHALVE.getCode(), "Ambtshalve / verzorger"));
      addItem(new FieldValue(AangifteSoort.HOOFDINSTELLING.getCode(), AangifteSoort.HOOFDINSTELLING.getOms()));
    }
  }

  class AdresContainer extends GbaContainer {

    public AdresContainer() {

      addItem(setInitieleAdresRelaties(new VerhuisAdres(getPl(), getApplication().getServices().getGebruiker())));
    }
  }

  class SoortContainer extends GbaContainer {

    public SoortContainer() {

      AdresType adresType = getHuidigAdres().getAddressType();

      switch (adresType) {

        case BINNENGEMEENTELIJK:
          addItem(VerhuisType.BINNENGEMEENTELIJK);
          addItem(VerhuisType.EMIGRATIE);

          break;

        default:
          addItem(VerhuisType.INTERGEMEENTELIJK);
      }

      addItem(VerhuisType.HERVESTIGING);
    }
  }

  private class Table1 extends Page10VerhuizingTable1 {

    @Override
    public void setRecords() {

      VerhuisAdres vp = (VerhuisAdres) form1.getHuidigAdresVeld().getValue();
      FieldValue aangever = (FieldValue) form1.getAangeverVeld().getValue();

      for (VerhuisRelatie vr : vp.getPersons()) {

        vr.setHoofdInstelling(
            AangifteSoort.HOOFDINSTELLING.getCode().equals(aangever.getValue())); // IS hoofdinstelling
        vr.setAmbtshalve(
            AangifteSoort.AMBTSHALVE.getCode().equals(aangever.getValue())); // IS Ambtshalve

        if (isMinderJarig() && vr.getRelatie().getRelatieType() != RelatieType.AANGEVER) {
          continue;
        }

        Relatie relatie = vr.getRelatie();
        Record r = addRecord(vr);

        String naam = relatie.getPl().getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam();
        String status = getStatus((VerhuisType) form1.getSoortVeld().getValue(), relatie);

        TableImage warning = new TableImage(Icons.getIcon(Icons.ICON_WARN));
        r.addValue(fil(status) ? warning : null);
        r.addValue(naam + " " + setClass("red", status));

        if (getHuidigVerhuisType() == VerhuisType.HERVESTIGING) {
          r.addValue(setClass(vr.isPresentievraag(), vr.getPresentievraagType().toString()));
        } else {
          r.addValue("N.v.t");
        }

        if (relatie.getRelatieType() == RelatieType.AANGEVER) {
          r.addValue("-");
        } else {
          r.addValue(relatie.getRelatieType().getOms());
        }

        r.addValue(vr.getSelect());
        r.addValue(relatie.getPl().getPersoon().getGeslacht().getDescr());
        r.addValue(relatie.getPl().getPersoon().getGeboorte().getDatumLeeftijd());
      }
    }
  }
}
