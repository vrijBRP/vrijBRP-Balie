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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page75;

import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakPersoonType.AANGEVER;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.ui.Button;

import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakPersoonType;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakTabsheet;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZakenregisterOptiePage;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page76.Page76Zaken;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page77.Page77Zaken;
import nl.procura.gba.web.modules.zaken.inhouding.overzicht.DocumentInhoudingOverzichtBuilder;
import nl.procura.gba.web.modules.zaken.inhouding.overzicht.DocumentInhoudingOverzichtLayout;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhouding;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhoudingenService;
import nl.procura.gba.web.services.zaken.inhoudingen.InhoudingType;
import nl.procura.gba.web.services.zaken.legezaak.LegeZaak;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentVermissing;

/**
 * Tonen reisdocumentinhouding
 */
public class Page75Zaken extends ZakenregisterOptiePage<DocumentInhouding> {

  protected final Button buttonVermissing = new Button("Vermissing afdrukken");

  public Page75Zaken(DocumentInhouding aanvraag) {
    super(aanvraag, "Zakenregister - inhouding van een document");
    addButton(buttonPrev);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonVermissing) {
      goToVermissing();
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  protected void addOptieButtons() {

    addOptieButton(buttonPersonen);
    if (InhoudingType.VERMISSING.equals(getZaak().getInhoudingType())) {
      addOptieButton(buttonVermissing);
      buttonDoc.setCaption("Zaak afdrukken");
    }

    addOptieButton(buttonDoc);
    addOptieButton(buttonFiat);
    addOptieButton(buttonVerwerken);
  }

  @Override
  protected void addTabs(ZaakTabsheet<DocumentInhouding> tabsheet) {

    String type = getZaak().getInhoudingType().getOms().toLowerCase() + " reisdocument";
    if (getZaak().isSprakeVanRijbewijs()) {
      type = getZaak().getInhoudingType().getOms().toLowerCase() + " rijbewijs";
    }

    DocumentInhoudingOverzichtLayout layout = DocumentInhoudingOverzichtBuilder.create(getZaak(), null,
        true, true);
    DocumentInhoudingOverzichtBuilder.addTab(tabsheet, getZaak(), layout, StringUtils.capitalize(type));
  }

  @Override
  protected ZaakPersoonType[] getZoekpersoonTypes() {
    return new ZaakPersoonType[]{ AANGEVER };
  }

  @Override
  protected void goToDocument() {
    getNavigation().goToPage(new Page77Zaken(getZaak()));
  }

  @Override
  protected void goToPersoon(ZaakPersoonType type) {
    goToPersoon("zaken.inhouding", getZaak().getAnummer(), getZaak().getBurgerServiceNummer());
  }

  private void goToVermissing() {

    DocumentInhoudingenService inhoudingen = getServices().getDocumentInhoudingenService();
    ReisdocumentVermissing vermissing = inhoudingen.getVermissing(getZaak());

    getNavigation().goToPage(new Page76Zaken(vermissing, new LegeZaak(getZaak().getBasisPersoon(),
        getApplication().getServices().getGebruiker())));
  }
}
