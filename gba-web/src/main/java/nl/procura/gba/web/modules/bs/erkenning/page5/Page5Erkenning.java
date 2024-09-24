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

package nl.procura.gba.web.modules.bs.erkenning.page5;

import static java.util.Arrays.asList;
import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.gba.web.modules.bs.erkenning.page5.Page5ErkenningForm1.ERKENNINGS_TYPE;
import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.INFO;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;

import java.util.Collection;

import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.modules.bs.common.pages.BsPage;
import nl.procura.gba.web.modules.bs.common.pages.gerelateerdepage.PageBsKindTable;
import nl.procura.gba.web.modules.bs.common.pages.nationaliteitpage.BsNatioType;
import nl.procura.gba.web.modules.bs.common.pages.nationaliteitpage.BsNatioWindow;
import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsStatusForm;
import nl.procura.gba.web.modules.bs.erkenning.page10.Page10Erkenning;
import nl.procura.gba.web.modules.bs.erkenning.page5.Page5ErkenningForm2.Page5ErkenningBean2;
import nl.procura.gba.web.modules.bs.erkenning.page5.akte.Page5ErkenningAkteWindow;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteit;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.erkenning.DossierErkenning;
import nl.procura.gba.web.services.bs.erkenning.ErkenningsType;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**

 * <p>
 * 3 nov. 2011 15:00:17
 */

public class Page5Erkenning extends BsPage<DossierErkenning> {

  private final Button buttonAanpassen    = new Button("Aanpassen");
  private final Button buttonKinderen     = new Button("Kinderen");
  private final Button buttonNieuwKind    = new Button("Kind opvoeren");
  private final Button buttonGeboorteAkte = new Button("Geboorteakte");

  private final VerticalLayout geborenLayout = new VerticalLayout();
  private KindTable1           kindTable1    = null;
  private Page5ErkenningForm1  form1         = null;
  private Page5ErkenningForm2  form2         = null;
  // Zijn de geboortegevens overruled?
  private boolean geboorteGegevensOverruled = false;

  public Page5Erkenning() {
    super("Erkenning - kind(eren)");
  }

  @Override
  public boolean checkPage() {

    form1.commit();

    switch (form1.getBean().getErkenningsType()) {

      case ERKENNING_BESTAAND_KIND:

        if (getZaakDossier().getKinderen().isEmpty()) {
          throw new ProException(INFO, "Er zijn geen kinderen geselecteerd of opgevoerd.");
        }

        if (BsPersoonUtils.getGedeeldeLanden(getZaakDossier().getKinderen()).isEmpty()) {
          throw new ProException(WARNING, "De kinderen wonen niet in hetzelfde land.");
        }

        if (BsPersoonUtils.getGedeeldeNationaliteiten(getZaakDossier().getKinderen()).isEmpty()) {
          throw new ProException(WARNING, "De kinderen hebben verschillende nationaliteiten.");
        }

        if (BsPersoonUtils.getGedeeldeLeeftijden(getZaakDossier().getKinderen()).isEmpty()) {
          throw new ProException(WARNING, "De kinderen hebben verschillende leeftijdscategorieën.");
        }

        break;

      default:
        break;
    }

    if (!geboorteGegevensOverruled && !isGeboorteakteGegevensGevuld()) {

      getParentWindow().addWindow(
          new ConfirmDialog("Niet alle geboorteaktegegevens zijn gevuld.<br/>Toch doorgaan?") {

            @Override
            public void buttonYes() {

              geboorteGegevensOverruled = true;

              super.buttonYes();

              goToNextProces();
            }
          });

      return false;
    }

    geboorteGegevensOverruled = false;

    gegevensGevuld();

    return true;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonPrev);
      addButton(buttonNext);

      addComponent(new BsStatusForm(getDossier()));

      setInfo("Kies een soort erkenning. Doorloop eventueel aanvullende stappen (bij erkenning bestaand kind) "
          + "en druk op Volgende (F2) om verder te gaan.");

      setOptieForm();
      setGeborenLayout();
      checkErkenningType();
    } else if (event.isEvent(AfterReturn.class)) {

      kindTable1.init();
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonKinderen) {

      getParentWindow().addWindow(new Page5ErkenningWindow(this, getZaakDossier().getMoeder()));
    } else if (button == buttonNieuwKind) {

      getNavigation().goToPage(new Page10Erkenning(new DossierPersoon()));
    } else if (button == buttonAanpassen) {

      String msg = "Vul minimaal één nationaliteit in. Dit is belangrijk voor het verdere verloop van het proces.";

      getParentWindow().addWindow(new BsNatioWindow(BsNatioType.STANDAARD, getDossier(), msg) {

        @Override
        public void add(DossierNationaliteit nationaliteit) {
          toevoegenNationaliteit(nationaliteit);
        }

        @Override
        public void delete(DossierNationaliteit nationaliteit) {
          verwijderNationaliteit(nationaliteit);
        }

      });
    } else if (button == buttonGeboorteAkte) {

      if (kindTable1.isSelectedRecords()) {
        getParentWindow().addWindow(
            new Page5ErkenningAkteWindow(this, kindTable1.getSelectedValues(DossierPersoon.class).get(0)));
      } else {
        throw new ProException(WARNING, "Geen records geselecteerd");
      }
    }

    super.handleEvent(button, keyCode);
  }

  public void herlaadKindTabel() {
    kindTable1.init();
  }

  @Override
  public void onDelete() {

    if (!kindTable1.isSelectedRecords()) {
      infoMessage("Selecteer eerst één of meerdere records.");
    }

    verwijderKinderen(kindTable1.getSelectedValues(DossierPersoon.class));
  }

  @Override
  public void onNextPage() {

    goToNextProces();
  }

  @Override
  public void onPreviousPage() {

    goToPreviousProces();

    super.onPreviousPage();
  }

  public void toevoegenKind(DossierPersoon kind) {

    if (kind.isVolledig()) {

      getServices().getDossierService().addKind(getZaakDossier(), kind);

      kindTable1.init();
    }
  }

  private void checkErkenningType() {

    ErkenningsType type = (ErkenningsType) form1.getField(ERKENNINGS_TYPE).getValue();

    removeComponent(geborenLayout);

    if (type != null) {
      if (type == ErkenningsType.ERKENNING_BESTAAND_KIND) {
        addComponent(geborenLayout);
      }
    }
  }

  private void gegevensGevuld() {

    getZaakDossier().setErkenningsType(form1.getBean().getErkenningsType());

    super.checkPage();

    getServices().getErkenningService().save(getDossier());
  }

  private boolean isGeboorteakteGegevensGevuld() {

    for (DossierPersoon kind : getZaakDossier().getKinderen()) {

      boolean isBsNr = fil(kind.getGeboorteAkteNummer());
      boolean isBrpNr = fil(kind.getGeboorteAkteBrpNummer());
      boolean isJaar = pos(kind.getGeboorteAkteJaar());
      boolean isPlaats = fil(astr(kind.getGeboorteAktePlaats()));

      if (!(isBsNr && isBrpNr && isJaar && isPlaats)) {
        return false;
      }
    }

    return true;
  }

  private void setGeborenLayout() {

    kindTable1 = new KindTable1(getZaakDossier());
    form2 = new Page5ErkenningForm2();

    if (getZaakDossier().isGelijkMetGeboorte()) {

      addComponent(kindTable1);
      addComponent(form2);

      kindTable1.setSelectable(false);
    } else {

      OptieLayout optieLayout = new OptieLayout();
      optieLayout.getLeft().addComponent(kindTable1);
      optieLayout.getLeft().addComponent(form2);

      optieLayout.getRight().addButton(buttonKinderen, this);
      optieLayout.getRight().addButton(buttonNieuwKind, this);
      optieLayout.getRight().addButton(buttonGeboorteAkte, this);

      if (!getZaakDossier().isGelijkMetGeboorte()) {

        // Alleen kinderen verwijderen als de erkenning op zichtzelf staat

        optieLayout.getRight().addButton(buttonDel, this);
      }

      optieLayout.getRight().setWidth("150px");

      form1.getField(ERKENNINGS_TYPE).addListener((ValueChangeListener) event -> checkErkenningType());

      geborenLayout.setSpacing(true);
      geborenLayout.addComponent(new Fieldset("Te erkennen kind(eren)"));
      geborenLayout.addComponent(new InfoLayout("",
          "Druk op de button om te zien welk(e) kind(eren) van de moeder voor erkenning in aanmerking "
              + "komen en selecteer deze. Als er meerdere kinderen moeten worden erkend, let dan op of hun gegevens voldoende overeenkomen. "
              + "Maak er anders aparte zaken van."));
      geborenLayout.addComponent(optieLayout);
    }
  }

  private void setOptieForm() {
    form1 = new Page5ErkenningForm1(getZaakDossier());

    addComponent(form1);
    addComponent(new Fieldset("Kind(eren) van de moeder", new KindTable2()));
  }

  private void toevoegenNationaliteit(DossierNationaliteit natio) {
    getServices().getDossierService().addNationaliteit(getDossier(), natio);
  }

  private void verwijderKinderen(Collection<DossierPersoon> kinderen) {
    getServices().getDossierService().deletePersonen(getDossier(), kinderen);
    kindTable1.init();
  }

  private void verwijderNationaliteit(DossierNationaliteit nationaliteit) {
    getServices().getDossierService().deleteNationaliteiten(getDossier(), asList(nationaliteit));

    for (DossierPersoon kind : getZaakDossier().getKinderen()) {
      getServices().getDossierService().deleteNationaliteiten(getDossier(), asList(nationaliteit));
      getServices().getDossierService().deleteNationaliteiten(kind, asList(nationaliteit));
    }
  }

  class KindTable1 extends Page5ErkenningTable1 {

    public KindTable1(DossierErkenning erkenning) {
      super(erkenning);
    }

    @Override
    public void onDoubleClick(Record record) {

      getParentWindow().addWindow(
          new Page5ErkenningAkteWindow(Page5Erkenning.this, record.getObject(DossierPersoon.class)));

      super.onDoubleClick(record);
    }

    @Override
    public void setRecords() {

      Page5ErkenningBean2 b = form2.getNewBean();

      int aantalKinderen = getZaakDossier().getKinderen().size();

      if (aantalKinderen > 0) {

        addDossierPersonen(getZaakDossier().getKinderen());

        String gedLand = BsPersoonUtils.getGedeeldeLanden(getZaakDossier().getKinderen());
        String gedNat = BsPersoonUtils.getGedeeldeNationaliteiten(getZaakDossier().getKinderen());
        String gedLeeftijd = BsPersoonUtils.getGedeeldeLeeftijden(getZaakDossier().getKinderen());

        if (aantalKinderen == 1) {

          b.setLand(fil(gedLand) ? gedLand : setClass("red", "Het kind heeft geen land."));
          b.setNat(fil(gedNat) ? gedNat : setClass("red", "Het kind heeft geen nationaliteit."));
          b.setLeeftijd(gedLeeftijd);
        } else {

          b.setLand(fil(gedLand) ? gedLand : setClass("red", "De kinderen hebben geen gedeelde landen."));
          b.setNat(fil(gedNat) ? gedNat
              : setClass("red",
                  "De kinderen hebben geen gedeelde nationaliteiten."));
          b.setLeeftijd(fil(gedLeeftijd) ? gedLeeftijd
              : setClass("red",
                  "De kinderen hebben geen gedeelde leeftijdscategorieën."));
        }
      }

      form2.setBean(b);

      super.setRecords();
    }
  }

  class KindTable2 extends PageBsKindTable {

    public KindTable2() {

      super(getZaakDossier().getMoeder());
    }
  }
}
