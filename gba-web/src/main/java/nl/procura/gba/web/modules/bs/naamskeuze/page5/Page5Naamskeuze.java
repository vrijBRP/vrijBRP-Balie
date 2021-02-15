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

package nl.procura.gba.web.modules.bs.naamskeuze.page5;

import static java.util.Arrays.asList;
import static nl.procura.gba.web.modules.bs.naamskeuze.page5.Page5NaamskeuzeForm1.NAAMSKEUZE_TYPE;
import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.standard.exceptions.ProExceptionSeverity.INFO;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;

import java.util.ArrayList;

import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.modules.bs.common.pages.BsPage;
import nl.procura.gba.web.modules.bs.common.pages.gerelateerdepage.PageBsKindTable;
import nl.procura.gba.web.modules.bs.common.pages.nationaliteitpage.BsNatioType;
import nl.procura.gba.web.modules.bs.common.pages.nationaliteitpage.BsNatioWindow;
import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsStatusForm;
import nl.procura.gba.web.modules.bs.naamskeuze.page10.Page10Naamskeuze;
import nl.procura.gba.web.modules.bs.naamskeuze.page5.akte.Page5NaamskeuzeAkteWindow;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteit;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.naamskeuze.DossierNaamskeuze;
import nl.procura.gba.web.services.bs.naamskeuze.NaamskeuzeType;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page5Naamskeuze extends BsPage<DossierNaamskeuze> {

  private final Button buttonAanpassen    = new Button("Aanpassen");
  private final Button buttonKinderen     = new Button("Kinderen");
  private final Button buttonNieuwKind    = new Button("Kind opvoeren");
  private final Button buttonGeboorteAkte = new Button("Geboorteakte");

  private final VerticalLayout geborenLayout                 = new VerticalLayout();
  private KindTable1           kindTable1                    = null;
  private Page5NaamskeuzeForm1 form1                         = null;
  private boolean              geboorteAkteGegevensOverruled = false;

  public Page5Naamskeuze() {
    super("Naamskeuze - kind(eren)");
  }

  @Override
  public boolean checkPage() {

    form1.commit();

    switch (form1.getBean().getNaamskeuzesType()) {

      case NAAMSKEUZE_BESTAAND_KIND:

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

      case ONBEKEND:
      default:
        break;
    }

    if (!geboorteAkteGegevensOverruled && !isGeboorteakteGegevensGevuld()) {
      getParentWindow().addWindow(new ConfirmDialog("Niet alle geboorteaktegegevens zijn gevuld." +
          "<br/>Toch doorgaan?") {

        @Override
        public void buttonYes() {
          geboorteAkteGegevensOverruled = true;
          super.buttonYes();
          goToNextProces();
        }
      });

      return false;
    }

    geboorteAkteGegevensOverruled = false;
    gegevensGevuld();
    return true;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      addButton(buttonPrev);
      addButton(buttonNext);

      addComponent(new BsStatusForm(getDossier()));

      setInfo("Kies een soort naamskeuze. Doorloop eventueel aanvullende stappen (bij naamskeuze bestaand kind) "
          + "en druk op Volgende (F2) om verder te gaan.");

      setOptieForm();
      setGeborenLayout();
      checkNaamskeuzeType();

    } else if (event.isEvent(AfterReturn.class)) {
      kindTable1.init();
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonKinderen) {
      getParentWindow().addWindow(new Page5NaamskeuzeWindow(this, getZaakDossier().getMoeder()));

    } else if (button == buttonNieuwKind) {
      getNavigation().goToPage(new Page10Naamskeuze(new DossierPersoon()));

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
        getParentWindow()
            .addWindow(new Page5NaamskeuzeAkteWindow(this,
                kindTable1.getSelectedValues(DossierPersoon.class).get(0)));

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
      infoMessage("Selecteer eerst een record.");
    }

    verwijderKinderen();
  }

  @Override
  public void onNextPage() {
    if (getDossier().getKinderen().size() > 1) {
      throw new ProException("Selecteer maximaal één kind");
    }
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

  public void verwijderKinderen() {
    ArrayList<DossierPersoon> kinderen = kindTable1.getAllValues(DossierPersoon.class);
    getServices().getDossierService().deletePersonen(getDossier(), kinderen);
    kindTable1.init();
  }

  private void checkNaamskeuzeType() {
    NaamskeuzeType type = (NaamskeuzeType) form1.getField(NAAMSKEUZE_TYPE).getValue();
    removeComponent(geborenLayout);

    if (type != null) {
      if (type == NaamskeuzeType.NAAMSKEUZE_BESTAAND_KIND) {
        addComponent(geborenLayout);
      }
    }
  }

  private void gegevensGevuld() {
    getZaakDossier().setDossierNaamskeuzeType(form1.getBean().getNaamskeuzesType());
    super.checkPage();
    getServices().getNaamskeuzeService().save(getDossier());
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

    OptieLayout optieLayout = new OptieLayout();
    optieLayout.getLeft().addComponent(kindTable1);

    optieLayout.getRight().addButton(buttonKinderen, this);
    optieLayout.getRight().addButton(buttonNieuwKind, this);
    optieLayout.getRight().addButton(buttonGeboorteAkte, this);
    optieLayout.getRight().addButton(buttonDel, this);
    optieLayout.getRight().setWidth("150px");

    form1.getField(NAAMSKEUZE_TYPE).addListener((ValueChangeListener) event -> checkNaamskeuzeType());

    geborenLayout.setSpacing(true);
    geborenLayout.addComponent(new Fieldset("Gegevens kind(eren)"));
    geborenLayout.addComponent(new InfoLayout("",
        "Druk op de button \"Kinderen\" om daar te selecteren voor welk kind er naamskeuze moet worden gedaan. " +
            "Indien van toepassing kunnen kindgegevens zelf worden opgevoerd. " +
            "<br/>De geboorteaktegegevens worden uit de persoonslijst gehaald. " +
            "Als deze niet voorhanden zijn kunnen deze handmatig worden opgevoerd."));
    geborenLayout.addComponent(optieLayout);

  }

  private void setOptieForm() {
    form1 = new Page5NaamskeuzeForm1(getZaakDossier());
    addComponent(form1);
    addComponent(new Fieldset("Kind(eren) van de moeder", new KindTable2()));
  }

  private void toevoegenNationaliteit(DossierNationaliteit natio) {
    getServices().getDossierService().addNationaliteit(getDossier(), natio);
  }

  private void verwijderNationaliteit(DossierNationaliteit nationaliteit) {
    getServices().getDossierService().deleteNationaliteiten(getDossier(), asList(nationaliteit));
    for (DossierPersoon kind : getZaakDossier().getKinderen()) {
      getServices().getDossierService().deleteNationaliteiten(getDossier(), asList(nationaliteit));
      getServices().getDossierService().deleteNationaliteiten(kind, asList(nationaliteit));
    }
  }

  class KindTable1 extends Page5NaamskeuzeTable1 {

    public KindTable1(DossierNaamskeuze naamskeuze) {
      super(naamskeuze);
    }

    @Override
    public void onDoubleClick(Record record) {
      getParentWindow().addWindow(new Page5NaamskeuzeAkteWindow(Page5Naamskeuze.this,
          record.getObject(DossierPersoon.class)));

      super.onDoubleClick(record);
    }

    @Override
    public void setRecords() {
      int aantalKinderen = getZaakDossier().getKinderen().size();
      if (aantalKinderen > 0) {
        addDossierPersonen(getZaakDossier().getKinderen());
      }

      super.setRecords();
    }
  }

  class KindTable2 extends PageBsKindTable {

    public KindTable2() {
      super(getZaakDossier().getMoeder());
    }
  }
}
