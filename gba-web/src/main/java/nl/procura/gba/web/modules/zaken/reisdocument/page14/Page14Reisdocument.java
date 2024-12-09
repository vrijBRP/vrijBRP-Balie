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

package nl.procura.gba.web.modules.zaken.reisdocument.page14;

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.commons.core.exceptions.ProExceptionType.SELECT;
import static nl.procura.standard.Globalfunctions.astr;

import com.vaadin.ui.Button;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.components.dialogs.ZaakConfiguratieDialog;
import nl.procura.gba.web.modules.zaken.common.ZakenPage;
import nl.procura.gba.web.modules.zaken.inhouding.page2.Page2Inhouding;
import nl.procura.gba.web.modules.zaken.inhouding.page4.Page4Inhouding;
import nl.procura.gba.web.modules.zaken.inhouding.page5.Page5Inhouding;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhouding;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhoudingenService;
import nl.procura.gba.web.services.zaken.inhoudingen.InhoudingType;
import nl.procura.gba.web.services.zaken.reisdocumenten.Reisdocument;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterBackwardReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Overzicht reisdocumenten
 */

public class Page14Reisdocument extends ZakenPage {

  private final Button    buttonInhoud   = new InhButton("Inhouding");
  private final Button    buttonVermis   = new InhButton("Vermissing");
  private final Button    buttonRechts   = new InhButton("Van rechtswege");
  private final Button    buttonOnbekend = new InhButton("Onbekend");
  private final Button    buttonHerst    = new InhButton("Herstellen");
  private       Table1    table1;
  private       BasePLExt pl;

  public Page14Reisdocument(BasePLExt pl) {
    super("In te houden documenten");
    setPl(pl);

    addButton(buttonPrev);
    addButton(buttonNext, 1f);
    addButton(buttonInhoud);
    addButton(buttonVermis);
    addButton(buttonRechts);
    addButton(buttonOnbekend);
    addButton(buttonHerst);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      setInfo("Overzicht van de reisdocumenten van deze persoon",
          "Inhoudingen of vermissingen die reeds zijn verwerkt op de persoonlijst zijn niet meer zichtbaar in deze lijst.");

      table1 = new Table1(pl);

      addComponent(new Fieldset("Huidige documenten van deze persoon"));
      addExpandComponent(table1);
    } else if (event.isEvent(AfterBackwardReturn.class)) {

      // bij terugkomst tabel opnieuw laden

      table1.init();
    }

    super.event(event);
  }

  @Override
  public BasePLExt getPl() {
    return pl;
  }

  public void setPl(BasePLExt pl) {
    this.pl = pl;
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonInhoud) {
      actie(InhoudingType.INHOUDING);

    } else if (button == buttonVermis) {
      actie(InhoudingType.VERMISSING);

    } else if (button == buttonOnbekend) {
      actie(InhoudingType.ONBEKEND);

    } else if (button == buttonRechts) {
      actie(InhoudingType.VAN_RECHTSWEGE_VERVALLEN);

    } else if (button == buttonHerst) {
      actie(InhoudingType.NIET_INGEHOUDEN);
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onDelete() {
    actie(InhoudingType.NIET_INGEHOUDEN);
  }

  @Override
  public void onNextPage() {
    super.onPreviousPage();
  }

  private void actie(InhoudingType inhoudingType) {

    if (table1.getSelectedRecords().size() > 0) {

      final Reisdocument document = (Reisdocument) table1.getSelectedRecord().getObject();
      final DocumentInhoudingenService inhoudingen = getServices().getDocumentInhoudingenService();
      final DocumentInhouding inhouding = inhoudingen.getInhouding(pl, document);

      if (inhouding != null && !inhoudingType.isNogNietIngehouden()) {

        switch (inhouding.getInhoudingType()) {

          case INHOUDING:
            throw new ProException(SELECT, WARNING, "Dit document is al ingehouden.");

          case VERMISSING:
            throw new ProException(SELECT, WARNING, "Dit document is al als vermist opgegeven.");

          case VAN_RECHTSWEGE_VERVALLEN:
            if (inhoudingType == InhoudingType.VAN_RECHTSWEGE_VERVALLEN) {
              throw new ProException(SELECT, WARNING, "Dit document is al van rechtswege ingehouden.");
            }
            break;

          default:
            throw new ProException(SELECT, WARNING,
                "Dit document is al als verloren met onbekende oorzaak opgegeven.");
        }
      }

      InhoudingType inhoudingTypePl = InhoudingType.get(document.getAanduidingInhoudingVermissing().getVal());

      switch (inhoudingTypePl) {

        case INHOUDING:
          warningMessage("Dit document is al ingehouden en verwerkt op de persoonslijst");
          break;

        case VERMISSING:
          warningMessage("Dit document is al als vermist opgegeven en verwerkt op de persoonslijst");
          break;

        case ONBEKEND:
          warningMessage(
              "Dit document is al verloren met onbekende oorzaak opgegeven en verwerkt op de persoonslijst");
          break;

        case NIET_INGEHOUDEN:
          doActie(inhoudingType, inhouding, document);
          break;

        case VAN_RECHTSWEGE_VERVALLEN:
          if (inhoudingType.isVanRechtswegeVervallen()) {
            warningMessage("Dit document is al ingehouden van rechtswege op de persoonslijst");
          } else {
            doActie(inhoudingType, inhouding, document);
          }
          break;

        default:
          break;
      }
    } else {
      throw new ProException(SELECT, WARNING, "Geen records geselecteerd");
    }
  }

  private void doActie(InhoudingType type, final DocumentInhouding bestaandeInhouding, final Reisdocument document) {

    switch (type) {

      case INHOUDING:
        showDialog(InhoudingType.INHOUDING,
            "Dit document als ingehouden beschouwen?", document);
        break;

      case VERMISSING:
        showDialog(InhoudingType.VERMISSING,
            "Dit document als vermist beschouwen?", document);
        break;

      case ONBEKEND:
        showDialog(InhoudingType.ONBEKEND,
            "Dit document als verloren beschouwen met onbekende reden?", document);
        break;

      case VAN_RECHTSWEGE_VERVALLEN:

        BasePLValue dEnd = document.getDatumEindeGeldigheid();
        boolean isVerlopen = new ProcuraDate(dEnd.getVal()).isExpired();

        if (isVerlopen) {
          throw new ProException(SELECT, WARNING,
              "Dit document is verlopen en kan niet van rechtswege worden vervallen");

        } else {
          showDialog(InhoudingType.VAN_RECHTSWEGE_VERVALLEN,
              "Dit document als van rechtswege vervallen beschouwen?", document);
        }

        break;

      case NIET_INGEHOUDEN:

        if (bestaandeInhouding == null) {
          throw new ProException(SELECT, WARNING, "Dit document is nog in bezit van de burger");
        }

        if (!isHerstellenToegestaan(bestaandeInhouding)) {
          throw new ProException(SELECT, WARNING,
              "Dit is alleen mogelijk om de dag van inhouding of vermissing.");
        }

        ConfirmDialog niDialog = new ConfirmDialog("Dit document als niet-ingehouden beschouwen?") {

          @Override
          public void buttonYes() {

            close();

            herstellen(bestaandeInhouding);
          }
        };
        getWindow().addWindow(niDialog);

        break;

      default:
        break;
    }
  }

  private void showDialog(InhoudingType inhoudingType, String msg, Reisdocument document) {

    final DocumentInhoudingenService inhoudingen = getServices().getDocumentInhoudingenService();

    getWindow().addWindow(new ConfirmDialog(msg) {

      @Override
      public void buttonYes() {
        close();
        DocumentInhouding nieuweInhouding = inhoudingen.createNewReisdocumentInhouding(pl, document, inhoudingType);
        ZaakConfiguratieDialog.of(Page14Reisdocument.this.getApplication(), nieuweInhouding, getServices(), () -> {
          getNavigation().goToPage(new Page2Inhouding(nieuweInhouding, document));
        });
      }
    });
  }

  private void herstellen(DocumentInhouding inh) {

    getServices().getDocumentInhoudingenService().delete(inh);

    table1.init();
  }

  private boolean isHerstellenToegestaan(DocumentInhouding inh) {
    return new ProcuraDate().diffInDays(new ProcuraDate(astr(inh.getDatumTijdInvoer().getLongDate()))) > -1;
  }

  public class InhButton extends Button {

    public InhButton(String caption) {
      super(caption);
      setWidth("130px");
    }
  }

  class Table1 extends Page14ReisdocumentTable1 {

    private Table1(BasePLExt pl) {
      super(pl);
    }

    @Override
    public void onDoubleClick(Record record) {

      Reisdocument rd = (Reisdocument) record.getObject();
      DocumentInhoudingenService db = getServices().getDocumentInhoudingenService();
      DocumentInhouding inhouding = db.getInhouding(pl, rd);

      if (inhouding == null) {
        getNavigation().goToPage(new Page5Inhouding(rd));
      } else {
        getNavigation().goToPage(new Page4Inhouding(inhouding, rd));
      }

      super.onDoubleClick(record);
    }
  }
}
