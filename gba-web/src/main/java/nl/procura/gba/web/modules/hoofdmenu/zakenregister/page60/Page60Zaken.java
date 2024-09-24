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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page60;

import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakPersoonType.AANGEVER;
import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakPersoonType.HOOFDBEWONER;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.INFO;

import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakPersoonType;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakTabsheet;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZakenregisterOptiePage;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page61.Page61Zaken;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page62.Page62Zaken;
import nl.procura.gba.web.modules.zaken.verhuizing.overzicht.VerhuizingOverzichtBuilder;
import nl.procura.gba.web.modules.zaken.verhuizing.overzicht.VerhuizingOverzichtLayout;
import nl.procura.gba.web.modules.zaken.verhuizing.page2.toestemming.InwoningToestemmingWindow;
import nl.procura.gba.web.services.gba.presentievraag.Presentievraag;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAangever;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraag;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisPersoon;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

/**
 * Tonen verhuisaanvraag
 */
public class Page60Zaken extends ZakenregisterOptiePage<VerhuisAanvraag> {

  public Page60Zaken(VerhuisAanvraag aanvraag) {
    super(aanvraag, "Zakenregister - verhuizing");
    addButton(buttonPrev);
  }

  @Override
  protected void addOptieButtons() {

    addOptieButton(buttonDoc);
    addOptieButton(buttonPersonen);
    addOptieButton(buttonFiat);
    addOptieButton(buttonVerwerken);
  }

  @Override
  protected void addTabs(ZaakTabsheet<VerhuisAanvraag> tabsheet) {

    VerhuizingOverzichtLayout overzichtLayout = new VerhuizingOverzichtLayout(getZaak()) {

      @Override
      public void goToPresentievraag(Presentievraag presentievraag) {
        Page60Zaken.this.getNavigation().addPage(new Page62Zaken(getZaak(), presentievraag));
      }

      @Override
      protected void goToPersoon(VerhuisPersoon persoon) {
        Page60Zaken.this.goToPersoon(persoon.getAnummer(), persoon.getBurgerServiceNummer());
      }

      @Override
      protected void onOpslaanVerwerking() {
        Page60Zaken.this.getServices().getVerhuizingService().saveVerwerkingPersonen(getZaak());
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
  protected ZaakPersoonType[] getZoekpersoonTypes() {
    return new ZaakPersoonType[]{ AANGEVER, HOOFDBEWONER };
  }

  @Override
  protected void goToDocument() {
    getNavigation().goToPage(new Page61Zaken(getZaak()));
  }

  protected void goToPersoon(FieldValue... fieldValues) {
    super.goToPersoon("zaken.verhuizing", fieldValues);
  }

  @Override
  protected void goToPersoon(ZaakPersoonType type) {

    switch (type) {
      case HOOFDBEWONER:
        goToPersoon(getZaak().getHoofdbewoner().getBurgerServiceNummer());
        break;

      case AANGEVER:
        if (getZaak().getAangever().isHoofdInstelling()) {
          throw new ProException(INFO, "De aangever was een hoofd instelling.");
        }

        VerhuisAangever aangever = getZaak().getAangever();
        goToPersoon(aangever.getAnummer(), aangever.getBurgerServiceNummer());

        break;

      default:
        throw new ProException("Onbekende persoonstype: " + type);
    }

    super.goToPersoon(type);
  }
}
