/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.zaken.inhouding.page4;

import com.vaadin.ui.Button;

import nl.procura.gba.web.modules.zaken.common.ZakenOverzichtPage;
import nl.procura.gba.web.modules.zaken.inhouding.overzicht.DocumentInhoudingOverzichtBuilder;
import nl.procura.gba.web.modules.zaken.inhouding.overzicht.DocumentInhoudingOverzichtLayout;
import nl.procura.gba.web.modules.zaken.inhouding.page3.Page3Inhouding;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhouding;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhoudingenService;
import nl.procura.gba.web.services.zaken.inhoudingen.InhoudingType;
import nl.procura.gba.web.services.zaken.legezaak.LegeZaak;
import nl.procura.gba.web.services.zaken.reisdocumenten.Reisdocument;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentVermissing;
import nl.procura.vaadin.component.layout.tabsheet.LazyTabsheet.LazyTab;

/**
 * Tonen reisdocumentinhouding
 */
public class Page4Inhouding extends ZakenOverzichtPage<DocumentInhouding> {

  protected final Button                   buttonVermissing = new Button("Vermissing afdrukken");
  private final Reisdocument               reisdocument;
  private DocumentInhoudingOverzichtLayout inhoudingLayout;

  public Page4Inhouding(DocumentInhouding inhouding, Reisdocument reisdocument) {
    super(inhouding, "Inhouding / vermissing");
    this.reisdocument = reisdocument;
    addButton(buttonPrev);
    buttonSave.setCaption("Nummer PV opslaan (F9)");
  }

  @Override
  public void handleEvent(Button button, int keyCode) {
    if (button == buttonVermissing) {
      goToVermissing();
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onSave() {

    if (buttonSave.getParent() != null) {
      inhoudingLayout.commit();
      inhoudingLayout.save();
      successMessage("Gegevens zijn bijgewerkt");
    }

    super.onSave();
  }

  @Override
  protected void addOptieButtons() {

    if (InhoudingType.VERMISSING.equals(getZaak().getInhoudingType())) {
      addOptieButton(buttonVermissing);
      buttonDoc.setCaption("Zaak afdrukken");
    }

    addOptieButton(buttonDoc);
    addOptieButton(buttonFiat);
    addOptieButton(buttonVerwerken);
  }

  @Override
  protected void addTabs() {

    String type = "Reisdocument / " + getZaak().getInhoudingType().getOms().toLowerCase();
    if (getZaak().isSprakeVanRijbewijs()) {
      type = "Rijbewijs / " + getZaak().getInhoudingType().getOms().toLowerCase();
    }

    inhoudingLayout = DocumentInhoudingOverzichtBuilder.create(getZaak(), reisdocument,
        true, false);
    DocumentInhoudingOverzichtBuilder.addTab(getTabsheet(), getZaak(), inhoudingLayout, type);
  }

  @Override
  protected void goToDocument() {
    getNavigation().goToPage(new Page3Inhouding(getZaak()));
  }

  @Override
  protected void onSelectedTabChange(LazyTab lazyTab) {

    boolean isCorrectTab = (lazyTab.getComponent() instanceof DocumentInhoudingOverzichtLayout);
    boolean isVermissing = InhoudingType.VERMISSING.equals(getZaak().getInhoudingType());

    if (isCorrectTab && isVermissing) {
      addButton(buttonSave);
    } else {
      removeButton(buttonSave);
    }

    super.onSelectedTabChange(lazyTab);
  }

  private void goToVermissing() {
    DocumentInhoudingenService inhoudingen = getServices().getDocumentInhoudingenService();
    ReisdocumentVermissing vermissing = inhoudingen.getVermissing(getZaak());
    LegeZaak legeZaak = new LegeZaak(getPl(), getApplication().getServices().getGebruiker());
    getNavigation().goToPage(new Page3Inhouding(vermissing, legeZaak));
  }
}
