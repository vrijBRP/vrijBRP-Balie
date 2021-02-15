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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page8.tab1;

import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.bsm.BsmUitvoerenBean.TAAK;
import static nl.procura.gba.web.services.zaken.documenten.DocumentType.PL_UITTREKSEL;
import static nl.procura.gba.web.services.zaken.documenten.DocumentType.VERHUIZING_AANGIFTE;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Button;

import nl.procura.bsm.rest.v1_0.objecten.taak.BsmRestTaak;
import nl.procura.bsm.rest.v1_0.objecten.taak.BsmRestTaakVraag;
import nl.procura.bsm.taken.BsmTaak;
import nl.procura.bsm.taken.GbaBsmTaak;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.bsm.BsmUitvoerenForm;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.bsm.BsmUitvoerenWindow;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page8.Page8ZakenTab;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page8.Page8ZakenTabTemplate;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.documenten.DocumentService;
import nl.procura.gba.web.services.zaken.documenten.DocumentSoort;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.functies.VaadinUtils;

public class Page8ZakenTab1 extends Page8ZakenTabTemplate<Zaak> {

  private final List<DocumentSoort> soorten        = new ArrayList<>();
  private final Button              buttonAbonnees = new Button("Bepaal abonnees");

  public Page8ZakenTab1() {
    super("Zakenregister: documenten afdrukken");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonAbonnees);
      addButton(buttonStatus);
      addButton(buttonRefresh);

      soorten.addAll(getBrieven(PL_UITTREKSEL, VERHUIZING_AANGIFTE));

      setTable(new Table1());
      addExpandComponent(getTable());
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonAbonnees) {

      BsmTaak bsmTaak = GbaBsmTaak.PERSONEN_ZAKEN_MIJN_OVERHEID_1_0;
      BsmRestTaak taak = getServices().getBsmService().getBsmRestTaak(bsmTaak);
      BsmRestTaakVraag bsmVraag = new BsmRestTaakVraag(taak.getTaak());

      BsmUitvoerenForm form = new BsmUitvoerenForm("Betreft", true, TAAK);
      form.getBean().setTaak(taak.getOmschrijving());

      getWindow().addWindow(new BsmUitvoerenWindow(form, bsmTaak, bsmVraag) {

        @Override
        protected void reload() {

          Page8ZakenTab1.this.reload();
        }
      });
    }

    super.handleEvent(button, keyCode);
  }

  private List<DocumentSoort> getBrieven(DocumentType... types) {
    DocumentService documenten = getServices().getDocumentService();
    return documenten.getDocumentSoorten(getApplication().getServices().getGebruiker(), types);
  }

  class Table1 extends Page8ZakenTab1Table {

    @Override
    public void setRecords() {

      try {
        Page8ZakenTab parent = VaadinUtils.getParent(this, Page8ZakenTab.class);
        for (Zaak zaak : parent.getOnBepaald()) {
          addUittreksel(soorten, zaak);
        }
      } catch (Exception e) {
        getApplication().handleException(e);
      }

      super.setRecords();
    }
  }
}
