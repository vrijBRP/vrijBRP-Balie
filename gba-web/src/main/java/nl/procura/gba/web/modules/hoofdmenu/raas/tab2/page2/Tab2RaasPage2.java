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

package nl.procura.gba.web.modules.hoofdmenu.raas.tab2.page2;

import static nl.procura.gba.web.modules.hoofdmenu.raas.tab2.page2.Tab2RaasPage2Bean.*;

import java.io.ByteArrayInputStream;
import java.util.List;

import com.vaadin.ui.Button;

import nl.procura.dto.raas.aanvraag.DocAanvraagDto;
import nl.procura.dto.raas.bestand.RaasBestandDto;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.modules.beheer.documenten.DocumentenTabPage;
import nl.procura.gba.web.modules.hoofdmenu.raas.tab1.page2.windows.RaasWindow;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakregisterNavigator;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.raas.rest.domain.aanvraag.FindAanvraagRequest;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.commons.core.exceptions.ProExceptionSeverity;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.functies.downloading.DownloadHandlerImpl;

public class Tab2RaasPage2 extends DocumentenTabPage {

  private final OptieLayout    optieLayout = new OptieLayout();
  private DocAanvraagDto       aanvraag;
  private final RaasBestandDto bestand;
  private Tab2RaasPage2Layout  raasLayout;

  public Tab2RaasPage2(DocAanvraagDto aanvraag, RaasBestandDto bestand) {
    this.aanvraag = aanvraag;
    this.bestand = bestand;
    setMargin(true);
    addButton(buttonPrev, 1f);
    if (aanvraag != null) {
      addButton(buttonClose);
    }
  }

  public Tab2RaasPage2(RaasBestandDto bestand) {
    super("Inzage RAAS bestand");
    this.bestand = bestand;
    setMargin(true);
    addButton(buttonPrev, 1f);
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      raasLayout = new Tab2RaasPage2Layout(bestand);
      if (aanvraag == null) {
        optieLayout.getLeft().addExpandComponent(raasLayout);
        optieLayout.getRight().setWidth("160px");
        optieLayout.getRight().setCaption("Opties");
        optieLayout.getRight().addButton(raasLayout.buttonZaak, this);
        optieLayout.getRight().addButton(raasLayout.buttonAanvraag, this);
        optieLayout.getRight().addButton(raasLayout.buttonExport, this);
        addExpandComponent(optieLayout);
      } else {
        addExpandComponent(raasLayout);
      }
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {
    if (raasLayout.buttonZaak.equals(button)) {
      goToZaak();
    }
    if (raasLayout.buttonAanvraag.equals(button)) {
      goToRaasAanvraag();
    }
    if (raasLayout.buttonExport.equals(button)) {
      exportRaasBestand();
    }

    super.handleEvent(button, keyCode);
  }

  private void goToZaak() {
    if (bestand.getAanvraagNr().isNotBlank()) {
      List<Zaak> zaken = getServices().getZakenService()
          .getMinimaleZaken(new ZaakArgumenten(bestand.getAanvraagNr().getValue().toString()));
      if (zaken.isEmpty()) {
        throw new ProException(ProExceptionSeverity.INFO, "Geen zaak gevonden met dit aanvraagnummer");
      }
      zaken.stream().findFirst().ifPresent(zaak -> ZaakregisterNavigator.navigatoTo(zaak, this, false));
    }
  }

  private void goToRaasAanvraag() {
    if (bestand.getAanvraagNr().isNotBlank()) {
      FindAanvraagRequest request = new FindAanvraagRequest(bestand.getAanvraagNr().getValue());
      DocAanvraagDto documentAanvraag = getServices().getRaasService().get(request);
      getParentWindow().addWindow(new RaasWindow(documentAanvraag));
    }
  }

  private void exportRaasBestand() {
    DownloadHandlerImpl downloadHandler = new DownloadHandlerImpl(getParentWindow());
    downloadHandler.download(new ByteArrayInputStream(bestand.getContent().getValue().getBytes()),
        bestand.getNaam().getValue(), true);
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  public class Form extends Tab2RaasPage2Form {

    public Form(RaasBestandDto bestand) {
      super(bestand);
      setCaption("Raasbestand");
    }

    @Override
    protected void createFields() {
      setOrder(TYPE, NAAM, AANVRAAGNUMMER, DATUM_TIJD, RICHTING, STATUS);
    }
  }
}
