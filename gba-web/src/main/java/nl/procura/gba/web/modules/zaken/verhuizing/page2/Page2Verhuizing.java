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

package nl.procura.gba.web.modules.zaken.verhuizing.page2;

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.INFO;

import com.vaadin.ui.Button;

import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakTabsheet;
import nl.procura.gba.web.modules.zaken.common.ZakenOverzichtPage;
import nl.procura.gba.web.modules.zaken.verhuizing.overzicht.VerhuizingOverzichtBuilder;
import nl.procura.gba.web.modules.zaken.verhuizing.overzicht.VerhuizingOverzichtLayout;
import nl.procura.gba.web.modules.zaken.verhuizing.page2.toestemming.InwoningToestemmingWindow;
import nl.procura.gba.web.modules.zaken.verhuizing.page27.Page27Verhuizing;
import nl.procura.gba.web.modules.zaken.verhuizing.page3.Page3Verhuizing;
import nl.procura.gba.web.services.gba.presentievraag.Presentievraag;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAangever;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraag;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisPersoon;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

/**
 * Tonen verhuisaanvraag
 */

public class Page2Verhuizing extends ZakenOverzichtPage<VerhuisAanvraag> {

  private final Button buttonAangever     = new Button("Zoek aangever");
  private final Button buttonHoofdbewoner = new Button("Zoek hoofdbewoner");

  public Page2Verhuizing(VerhuisAanvraag aanvraag) {
    super(aanvraag, "Verhuizing");
    addButton(buttonPrev);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonAangever) {
      if (getZaak().getAangever().isHoofdInstelling()) {
        throw new ProException(INFO, "De aangever was een hoofd instelling.");
      }

      VerhuisAangever aangever = getZaak().getAangever();
      goToPersoon(aangever.getAnummer(), aangever.getBurgerServiceNummer());
    } else if (button == buttonHoofdbewoner) {
      goToPersoon(getZaak().getHoofdbewoner().getBurgerServiceNummer());
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  protected void addOptieButtons() {

    addOptieButton(buttonDoc);
    addOptieButton(buttonAangever);
    addOptieButton(buttonHoofdbewoner);
    addOptieButton(buttonFiat);
    addOptieButton(buttonVerwerken);
  }

  @Override
  protected void addTabs(ZaakTabsheet<VerhuisAanvraag> tabsheet) {

    VerhuizingOverzichtLayout overzichtLayout = new VerhuizingOverzichtLayout(getZaak()) {

      @Override
      public void goToPresentievraag(Presentievraag presentievraag) {
        Page2Verhuizing.this.getNavigation().addPage(new Page27Verhuizing(getZaak(), null, presentievraag));
      }

      @Override
      protected void goToPersoon(VerhuisPersoon persoon) {
        Page2Verhuizing.this.goToPersoon(persoon.getAnummer(), persoon.getBurgerServiceNummer());
      }

      @Override
      protected void onOpslaanVerwerking() {
        Page2Verhuizing.this.getServices().getVerhuizingService().saveVerwerkingPersonen(getZaak());
      }

      @Override
      protected void onToestemming() {

        InwoningToestemmingWindow toestemmingWindow = new InwoningToestemmingWindow(getZaak()) {

          @Override
          public void closeWindow() {

            super.closeWindow();

            resetForm3();
          }
        };

        getParent().getWindow().addWindow(toestemmingWindow);
      }
    };

    VerhuizingOverzichtBuilder.addTab(tabsheet, overzichtLayout, getZaak());
  }

  @Override
  protected void goToDocument() {
    getNavigation().goToPage(new Page3Verhuizing(getZaak()));
  }

  protected void goToPersoon(FieldValue... fieldValues) {
    super.goToPersoon("zaken.verhuizing", fieldValues);
  }
}
