/*
 * Copyright 2023 - 2024 Procura B.V.
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

package nl.procura.gba.web.modules.beheer.locaties.page2;

import static nl.procura.gba.web.services.beheer.locatie.LocatieType.NORMALE_LOCATIE;
import static nl.procura.standard.Globalfunctions.eq;
import static nl.procura.standard.Globalfunctions.toBigDecimal;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.commons.core.exceptions.ProExceptionType.ENTRY;

import java.util.List;

import com.vaadin.ui.Button;

import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.beheer.locaties.CouplePrintOptionsToLocsPage;
import nl.procura.gba.web.modules.beheer.locaties.page3.Page3Locaties;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.gba.web.services.beheer.locatie.LocatieType;
import nl.procura.commons.core.exceptions.ProException;

public class Page2Locaties extends NormalPageTemplate {

  private final Button buttonGebruikers = new Button("Gebruikers koppelen");
  private final Button buttonPrintERS   = new Button("Printers koppelen");

  private final Page2LocatiesForm1 form1;
  private final Page2LocatiesForm2 form2;
  private Locatie                  locatie;

  public Page2Locaties(Locatie locatie) {

    super("Toevoegen / muteren locatie");
    this.locatie = locatie;

    addButton(buttonPrev);
    addButton(buttonNew);
    addButton(buttonSave);

    form2 = new Page2LocatiesForm2(locatie);

    form1 = new Page2LocatiesForm1(locatie) {

      @Override
      protected void onTypeChange(LocatieType type) {

        form2.setVisible(type == LocatieType.NORMALE_LOCATIE);

        super.onTypeChange(type);
      }
    };

    OptieLayout ol = new OptieLayout();
    ol.getLeft().addComponent(form1);
    ol.getLeft().addComponent(form2);

    ol.getRight().setWidth("200px");
    ol.getRight().setCaption("Opties");

    ol.getRight().addButton(buttonGebruikers, this);
    ol.getRight().addButton(buttonPrintERS, this);
    addComponent(ol);
  }

  @Override
  public void handleEvent(com.vaadin.ui.Button button, int keyCode) {

    if (button == buttonGebruikers) {
      getNavigation().goToPage(new Page3Locaties(locatie));
    } else if (button == buttonPrintERS) {
      getNavigation().goToPage(new CouplePrintOptionsToLocsPage(locatie));
    }
    super.handleEvent(button, keyCode);
  }

  @Override
  public void onNew() {

    form1.reset();
    locatie = new Locatie();
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @Override
  public void onSave() {

    form1.commit();

    Page2LocatiesBean b1 = form1.getBean();

    if (!locatie.isStored()) {
      checkLocatie(b1.getLocatie());
    }

    locatie.setLocatieType(b1.getType());
    locatie.setLocatie(b1.getLocatie());
    locatie.setOmschrijving(b1.getOmschrijving());
    locatie.setZynyoDeviceId(b1.getZynyoDeviceId());

    if (locatie.getLocatieType() == LocatieType.NORMALE_LOCATIE) {

      form2.commit();

      Page2LocatiesBean b2 = form2.getBean();

      locatie.setIp(b2.getIPadressen());
      locatie.setCodeRas(b2.getRasCode());
      locatie.setCodeRaas(b2.getRaasCode());
      locatie.setGkasId(b2.getJCCGKASid());

      for (String ipadres : locatie.getIpAdressen()) {

        try {
          getServices().getLocatieService().getLookupAdress(ipadres);
        } catch (Exception e) {
          throw new ProException(ERROR, "Adres: " + ipadres + " kan niet worden gevonden.");
        }
      }
    } else {
      locatie.setIp("");
      locatie.setCodeRas(toBigDecimal(0));
      locatie.setCodeRaas(toBigDecimal(0));
      locatie.setGkasId("");
    }

    getServices().getLocatieService().save(locatie);
    successMessage("Locatie is opgeslagen.");

    super.onSave();
  }

  private void checkLocatie(String locatie) {
    List<Locatie> locList = getServices().getLocatieService().getAlleLocaties(NORMALE_LOCATIE);

    for (Locatie loc : locList) {
      if (eq(loc.getLocatie(), locatie)) {
        throw new ProException(ENTRY, WARNING,
            "De ingevoerde locatie is reeds opgeslagen." + "<br/> Voer een unieke locatienaam in a.u.b.");
      }
    }
  }
}
