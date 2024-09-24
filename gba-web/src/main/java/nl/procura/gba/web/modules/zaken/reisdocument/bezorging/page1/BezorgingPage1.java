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

package nl.procura.gba.web.modules.zaken.reisdocument.bezorging.page1;

import static nl.procura.gba.common.ZaakStatusType.INBEHANDELING;
import static nl.procura.vaadin.theme.twee.ProcuraTheme.ICOON_24.WARNING;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.zaken.reisdocument.bezorging.BezorgingStatusWindow;
import nl.procura.gba.web.services.zaken.algemeen.controle.Controles;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentAanvraag;
import nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.Bezorging;
import nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.BezorgingStatusType;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.HLayout;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class BezorgingPage1 extends NormalPageTemplate {

  private final Bezorging     bezorging;
  private BezorgingForm1      bezorgingForm1;
  private BezorgingStatusForm bezorgingStatusForm;
  private BezorgingForm3      bezorgingForm3;

  public BezorgingPage1(Bezorging bezorging) {
    this.bezorging = bezorging;
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {

      addButton(new Button("Controleer uitwisseling bezorger", this::updateBezorging));

      if (bezorging.getMelding() != null) {
        if (bezorging.getMelding().isBezorgingGewenst()) {
          ReisdocumentAanvraag aanvraag = getServices()
              .getReisdocumentBezorgingService()
              .getReisdocumentAanvraag(bezorging.getMelding());

          if (aanvraag != null) {
            if (!aanvraag.getStatus().isMinimaal(INBEHANDELING)) {
              addComponent(new InfoLayout("Ter informatie", WARNING,
                  "De reisdocumentaanvraag heeft nog niet de status 'In behandeling'."));
            }
          } else {
            addComponent(new InfoLayout("Geen aanvraag gevonden", WARNING,
                "Geen reisdocumentaanvraag gevonden voor deze melding. "
                    + "Mogelijk is de aanvraag verwijderd of in een ander systeem ingevoerd."));
          }

          bezorgingForm1 = new BezorgingForm1(bezorging);
          bezorgingStatusForm = new BezorgingStatusForm(bezorging);
          bezorgingForm3 = new BezorgingForm3(bezorging);

          HLayout hLayout = new HLayout().widthFull();
          VLayout thuisbezorgingLayout = new VLayout(new Fieldset("Thuisbezorging"), bezorgingForm1);
          hLayout.addComponent(thuisbezorgingLayout);
          hLayout.addComponent(new VLayout(new Fieldset("Statussen"), bezorgingStatusForm).width("300px"));
          hLayout.setExpandRatio(thuisbezorgingLayout, 1F);
          addComponent(hLayout);
          addComponent(bezorgingForm3);

          addComponent(new Fieldset("Naam en adres"));
          addComponent(new BezorgingForm2(bezorging));

          addComponent(new Fieldset("In te houden documenten"));
          addComponent(new BezorgingBezorgingenTable(bezorging));

          addComponent(new Fieldset("Gekoppelde thuisbezorgingen"));
          addComponent(new BezorgingKoppelingTable(bezorging) {

            @Override
            public void onDoubleClick(Record record) {
              Bezorging bezorging = record.getObject(Bezorging.class);
              getNavigation().goToPage(new BezorgingPage1(bezorging));
            }
          });
        } else {
          addComponent(new InfoLayout(null, "Inwoner heeft niet voor thuisbezorging gekozen"));
          addComponent(new BezorgingForm4(bezorging));
        }
      } else {
        addComponent(new InfoLayout(null, "Geen melding van thuisbezorging voor deze aanvraag gevonden"));
      }
    }

    super.event(event);

  }

  private void updateBezorging(ClickEvent event) {

    Controles controles = getServices().getReisdocumentBezorgingService().checkBezorging(new Controles());

    if (bezorging.isMelding()) {
      BezorgingStatusType statusTypeVoor = bezorging.getStatus().getType();
      getServices().getReisdocumentBezorgingService()
          .findByAanvrNr(bezorging.getMelding().getAanvrNr())
          .ifPresent(bezorging::setMelding);

      bezorgingForm1.update();
      bezorgingStatusForm.update();
      bezorgingForm3.update();

      BezorgingStatusType statusTypeNa = bezorging.getStatus().getType();
      if (statusTypeVoor != statusTypeNa) {
        successMessage("De status van de bezorging is gewijzigd naar <b>" + statusTypeNa.getOms() + "</b>");
      } else {
        infoMessage("Geen wijzigingen in de status van deze bezorging");
      }
    }

    BezorgingStatusWindow window = new BezorgingStatusWindow(controles);
    getParentWindow().addWindow(window);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {
    super.handleEvent(button, keyCode);
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }
}
