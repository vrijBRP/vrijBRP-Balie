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

package nl.procura.gba.web.modules.bs.common.pages.persoonpage;

import static java.util.Arrays.asList;
import static nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsPersoonBean1.BSN;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.emp;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.Window.CloseListener;

import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.modules.bs.common.pages.BsPage;
import nl.procura.gba.web.modules.bs.common.pages.nationaliteitpage.BsNatioType;
import nl.procura.gba.web.modules.bs.common.pages.nationaliteitpage.BsNatioWindow;
import nl.procura.gba.web.modules.bs.common.pages.zoekpage.BsZoekTab;
import nl.procura.gba.web.modules.bs.common.pages.zoekpage.BsZoekWindow;
import nl.procura.gba.web.modules.persoonslijst.overig.header.LopendeZakenWindow;
import nl.procura.gba.web.services.bs.algemeen.ZaakDossier;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteit;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.label.Ruler;
import nl.procura.vaadin.component.layout.HLayout;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Aangever
 */

public class BsPersoonPage<T extends ZaakDossier> extends BsPage<T> {

  protected final Button buttonGba   = new Button("Selecteer persoon");
  protected final Button buttonNat   = new Button("Nationaliteiten");
  protected final Button buttonZoek  = new Button("Persoonslijst");
  protected final Button buttonZaken = new Button("Lopende zaken");

  private BsPersoonForm1 form1 = null;
  private BsPersoonForm2 form2 = null;
  private BsPersoonForm3 form3 = null;
  private BsPersoonForm4 form4 = null;
  private BsPersoonForm5 form5 = null;
  private BsPersoonForm6 form6 = null;

  private OptieLayout          optieLayout    = new OptieLayout();
  private DossierPersoon       dossierPersoon = null;
  private BsPersoonRequirement requirement    = null;

  public BsPersoonPage(String title) {
    super(title);
  }

  public void addButtons() {

    buttonReset.setEnabled(!isDefinitief());
    buttonGba.setEnabled(!isDefinitief());

    getOptieLayout().getRight().addButton(buttonReset, this);
    getOptieLayout().getRight().addButton(buttonGba, this);
    getOptieLayout().getRight().addComponent(new Ruler());
    getOptieLayout().getRight().addButton(buttonNat, this);
    getOptieLayout().getRight().addButton(buttonZoek, this);
    getOptieLayout().getRight().addButton(buttonZaken, this);
  }

  /**
   * Controleer de pagina alvorens verder te gaan
   */
  @Override
  public boolean checkPage() {

    commitAll();

    BsPersoonBean1 b1 = form1.getBean();
    BsPersoonBean1 b2 = form2.getBean();
    BsPersoonBean1 b3 = form3.getBean();
    BsPersoonBean1 b4 = form4.getBean();
    BsPersoonBean1 b5 = form5.getBean();
    BsPersoonBean1 b6 = form6.getBean();

    DossierPersoon a = getDossierPersoon();

    // Persoon
    a.setDatumMoment(new DateTime());
    a.setBurgerServiceNummer(new BsnFieldValue(b1.getBsn()));
    a.setGeslachtsnaam(b1.getNaam());
    a.setVoorvoegsel(b1.getVoorv().getStringValue());
    a.setTitel(b1.getTitel());
    a.setVoornaam(b1.getVoorn());
    a.setNaamgebruik(b1.getNg());
    a.setGeslacht(b1.getGeslacht());
    a.setAktenaam(b1.getAktenaam());

    // Geboorte
    a.setDatumGeboorte(b2.getGeboortedatum());

    if (b2.getGeboorteland() != null) {
      boolean isGebNl = Landelijk.isNederlandOfOnbekend(b2.getGeboorteland());
      a.setGeboorteplaats(
          isGebNl ? b2.getGeboorteplaatsNL() : new FieldValue(-1, b2.getGeboorteplaatsBuitenland()));
      a.setGeboorteland(b2.getGeboorteland());
    } else {
      a.setGeboorteland(new FieldValue(-1, ""));
      a.setGeboorteplaats(new FieldValue(-1, b2.getGeboorteplaatsBuitenland()));
    }

    // Overlijden
    a.setDatumOverlijden(b6.getOverlijdensdatum());

    // Op de akte
    a.setGeboortelandAkte(b2.getGeboortelandAkte());
    a.setGeboorteplaatsAkte(b2.getGeboorteplaatsAkte());

    // Immigratie
    a.setDatumImmigratie(b5.getImmigratiedatum());
    a.setImmigratieland(new FieldValue(-1, ""));

    if (b5.getImmigratieland() != null) {
      a.setImmigratieland(b5.getImmigratieland());
    }

    // Adres
    a.setStraat(b3.getStraat());
    a.setHuisnummer(along(b3.getHnr()));
    a.setHuisnummerLetter(b3.getHnrL());
    a.setHuisnummerToev(b3.getHnrT());
    a.setHuisnummerAand(b3.getHnrA().getStringValue());
    a.setPostcode(b3.getPc());
    a.setWoonplaatsAkte(b3.getWoonplaatsAkte());
    a.setWoonlandAkte(b3.getWoonlandAkte());

    if (b3.getWoonland() != null) {
      boolean isWoonNl = Landelijk.isNederland(b3.getWoonland());
      a.setWoonplaats(isWoonNl ? b3.getWoonplaatsNL() : new FieldValue(-1, b3.getWoonplaatsBuitenland()));
      a.setWoongemeente(b3.getWoongemeente());
      a.setLand(b3.getWoonland());
    }

    // Overige
    a.setBurgerlijkeStaat(b4.getBs());
    a.setDatumBurgerlijkeStaat(new DateTime(along(b4.getBsVanaf().getValue())));
    a.setVerblijfstitel(b4.getVbt());
    a.setVerstrekkingsbeperking(b4.isVerstrek());
    a.setOnderCuratele(b4.isCuratele());
    a.setToelichting(b4.getToelichting());

    if (requirement.isNationaliteitenVerplicht() && a.getNationaliteiten().isEmpty()) {
      openNationaliteitenPage();
      return false;
    }

    BsPersoonUtils.controleerGeslacht(a);
    BsPersoonUtils.controleerBurgerlijkeStaat(a);

    a.setDefinitief(requirement.isDefinitiefNaControle());

    return true;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonPrev);
      addButton(buttonNext);

      ZaakType zaakType = getZaakDossier().getDossier().getType();
      requirement = new BsPersoonRequirementChecker().getRequirement(zaakType,
          dossierPersoon.getDossierPersoonType());

      form1 = new BsPersoonForm1(getApplication(), getZaakDossier().getDossier(), dossierPersoon, requirement);
      form2 = new BsPersoonForm2(dossierPersoon, requirement);
      form3 = new BsPersoonForm3(dossierPersoon, requirement);
      form4 = new BsPersoonForm4(dossierPersoon, requirement);
      form5 = new BsPersoonForm5(dossierPersoon, requirement);
      form6 = new BsPersoonForm6(dossierPersoon, requirement);

      addComponent(new BsStatusForm(getDossier()));

      setInfo(getInfo());

      HLayout h = new HLayout();
      h.sizeFull();
      h.addComponent(form2);
      form2.setWidth("416px");
      VLayout form5Layout = new VLayout(form5, form6).spacing(true);
      h.addExpandComponent(form5Layout);

      getOptieLayout().getLeft().addComponent(form1);
      getOptieLayout().getLeft().addComponent(h);
      getOptieLayout().getLeft().addComponent(form3);
      getOptieLayout().getLeft().addComponent(form4);

      getOptieLayout().getRight().setWidth("150px");
      getOptieLayout().getRight().setCaption("Opties");

      addButtons();

      addComponent(getOptieLayout());
    }

    super.event(event);

    if (event.isEvent(InitPage.class) && emp(getDossierPersoon().getGeslachtsnaam())) {

      openZoekWindow();
    }
  }

  public DossierPersoon getDossierPersoon() {
    return dossierPersoon;
  }

  public void setDossierPersoon(DossierPersoon dossierPersoon) {
    this.dossierPersoon = dossierPersoon;
  }

  public OptieLayout getOptieLayout() {
    return optieLayout;
  }

  public void setOptieLayout(OptieLayout optieLayout) {
    this.optieLayout = optieLayout;
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonGba) {

      openZoekWindow();
    } else if (button == buttonNat) {

      commitAll();

      openNationaliteitenPage();
    } else if (button == buttonZoek) {

      getApplication().goToPl(getParentWindow(), "", PLEDatasource.STANDAARD, form1.getBean().getBsn());
    } else if (button == buttonZaken) {

      getWindow().addWindow(
          new LopendeZakenWindow(getPersoonslijst(new BsnFieldValue(form1.getBean().getBsn()))));
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onNew() {

    if (isDefinitief()) {
      return;
    }

    form1.reset();
    form2.reset();
    form3.reset();
    form4.reset();
    form5.reset();
    form6.reset();

    update();

    super.onNew();
  }

  public void update() {

    form1.update();
    form2.update();
    form3.update();
    form4.update();
    form5.update();
    form6.update();

    buttonZoek.setEnabled(form1.getField(BSN).isReadOnly());
    buttonZaken.setEnabled(form1.getField(BSN).isReadOnly());
  }

  protected List<BsZoekTab> getExtraTabs() {
    return new ArrayList<>();
  }

  protected String getInfo() {
    return "Zoek indien nodig de persoon, controleer de gegevens of vul deze in. Druk op Volgende (F2) om verder te gaan.";
  }

  protected boolean isDefinitief() {
    return dossierPersoon != null && dossierPersoon.isDefinitief();
  }

  private void commitAll() {

    InvalidValueException ex = null;

    ex = commitForm(form1, ex);
    ex = commitForm(form2, ex);
    ex = commitForm(form3, ex);
    ex = commitForm(form4, ex);
    ex = commitForm(form5, ex);
    ex = commitForm(form6, ex);

    if (ex != null) {
      throw ex;
    }
  }

  private InvalidValueException commitForm(Form form, InvalidValueException ex) {

    try {
      form.commit();
      return ex;
    } catch (InvalidValueException e) {
      return e;
    }
  }

  private void openNationaliteitenPage() {

    String msg = "Vul bij deze persoon minimaal één nationaliteit in. Dit is belangrijk voor het verdere verloop van het proces.";

    BsNatioWindow natWindow = new BsNatioWindow(BsNatioType.STANDAARD, dossierPersoon, msg) {

      @Override
      public void add(DossierNationaliteit natio) {

        getServices().getDossierService().addNationaliteit(dossierPersoon, natio);
      }

      @Override
      public void closeWindow() {

        form4.updateNationaliteiten();

        super.closeWindow();
      }

      @Override
      public void delete(DossierNationaliteit nationaliteit) {

        getServices().getDossierService().deleteNationaliteiten(dossierPersoon, asList(nationaliteit));
      }
    };

    getParentWindow().addWindow(natWindow);
  }

  private void openZoekWindow() {

    BsZoekWindow zoekWindow = new BsZoekWindow(dossierPersoon, getExtraTabs());

    zoekWindow.addListener((CloseListener) e -> update());

    getParentWindow().addWindow(zoekWindow);
  }
}
