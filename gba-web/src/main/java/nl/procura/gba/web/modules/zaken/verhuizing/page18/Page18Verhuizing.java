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

package nl.procura.gba.web.modules.zaken.verhuizing.page18;

import static java.util.Arrays.asList;
import static nl.procura.gba.web.common.misc.Landelijk.LAND_ARUBA;
import static nl.procura.gba.web.common.misc.Landelijk.LAND_BONAIRE;
import static nl.procura.gba.web.common.misc.Landelijk.LAND_CURACAO;
import static nl.procura.gba.web.common.misc.Landelijk.LAND_SABA;
import static nl.procura.gba.web.common.misc.Landelijk.LAND_ST_EUSTATIUS;
import static nl.procura.gba.web.common.misc.Landelijk.LAND_ST_MAARTEN;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.fil;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;

import nl.procura.gba.web.components.dialogs.GoedkeuringsProcedure;
import nl.procura.gba.web.modules.zaken.common.ZakenProcesPrintPage;
import nl.procura.gba.web.modules.zaken.verhuizing.page1.Page1Verhuizing;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.gba.web.services.zaken.verhuizing.FunctieAdres;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraag;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisType;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuizingService;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Afdrukken aanvraag
 */

public class Page18Verhuizing extends ZakenProcesPrintPage<VerhuisAanvraag, VerhuisAanvraag>
    implements ValueChangeListener {

  private static final String AANGIFTE_VERHUIZING = "aangifte verhuizing";
  private static final String PIVA                = "piva";
  private static final String BRIEFADRES          = "briefadres";

  private final CheckBox incompleetCheckbox = new CheckBox("Incompleet");
  private String         incompleetMelding  = "";

  public Page18Verhuizing(VerhuisAanvraag verhuisAanvraag, boolean isEnd) {
    super("Verhuizing: afdrukken", verhuisAanvraag, isEnd, DocumentType.VERHUIZING_AANGIFTE);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      getButtonLayout().addComponent(incompleetCheckbox);
      incompleetCheckbox.setImmediate(true);
      incompleetCheckbox.addListener((ValueChangeListener) this);

      getButtonLayout().setComponentAlignment(incompleetCheckbox, Alignment.MIDDLE_CENTER);
      getServices().getVerhuizingService().setVolledigeZaakExtra(getModel());
    }

    super.event(event);
  }

  @Override
  public void onNextPage() {

    final VerhuisAanvraag zaak = getZaak();
    final VerhuizingService service = getApplication().getServices().getVerhuizingService();

    new GoedkeuringsProcedure(getApplication(), zaak) {

      @Override
      public void onAkkoord() {

        service.save(zaak, !incompleetCheckbox.booleanValue(), incompleetMelding);

        successMessage("De gegevens zijn opgeslagen.");

        getApplication().getProcess().endProcess();

        // Terug naar overzichtscherm

        getNavigation().goToPage(new Page1Verhuizing());
      }
    };
  }

  /**
   * Bepaalde documenten voorwaardelijk selecteren
   */
  @Override
  public boolean onSelectDocument(DocumentRecord document, boolean isPreSelect) {

    VerhuisAanvraag aanvraag = getModel();

    String doc = document.getDocument().toLowerCase();
    FunctieAdres fa = aanvraag.getNieuwAdres().getFunctieAdres();

    if (fa == FunctieAdres.BRIEFADRES) {

      if (doc.contains(BRIEFADRES)) {
        return true;
      }
    } else {

      if (doc.contains(AANGIFTE_VERHUIZING)) {
        return true;
      }
    }

    if (aanvraag.getTypeVerhuizing() == VerhuisType.EMIGRATIE) {

      long landCode = along(aanvraag.getEmigratie().getLand().getValue());

      if (asList(LAND_ARUBA, LAND_BONAIRE, LAND_CURACAO, LAND_SABA, LAND_ST_EUSTATIUS, LAND_ST_MAARTEN).contains(
          landCode)) {

        if (doc.contains(PIVA)) {

          return true;
        }
      }
    }

    return super.onSelectDocument(document, isPreSelect);
  }

  @Override
  public void setButtons() {

    addButton(buttonPrev);
    addButton(getPrintButtons());
    addButton(buttonAant);
    addButton(buttonNext);
  }

  @Override
  public void valueChange(ValueChangeEvent event) {
    if (event.getProperty() == incompleetCheckbox) {
      changeIncompleetCheckbox((boolean) event.getProperty().getValue());
    }
  }

  private void changeIncompleetCheckbox(boolean checked) {

    if (checked) {

      getWindow().addWindow(new IncompleetDialog(incompleetMelding) {

        @Override
        public void onCancel() {
          incompleetCheckbox.setValue(false);
        }

        @Override
        public void onSave(String melding) {

          incompleetCheckbox.setValue(fil(melding));
          incompleetMelding = melding;
        }
      });
    }
  }
}
