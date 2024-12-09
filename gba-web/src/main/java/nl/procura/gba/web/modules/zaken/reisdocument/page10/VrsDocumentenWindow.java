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

package nl.procura.gba.web.modules.zaken.reisdocument.page10;

import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsDocumentBean1.AANVRAAGNUMMER;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsDocumentBean1.AUT_VAN_VERSTREK;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsDocumentBean1.BSN;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsDocumentBean1.DOC_AFGIFTE;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsDocumentBean1.DOC_GELDIG_TOT;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsDocumentBean1.DOC_NR;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsDocumentBean1.DOC_SOORT;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsDocumentBean1.GEBOORTEDATUM;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsDocumentBean1.GEBOORTEPLAATS;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsDocumentBean1.GESLACHT;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsDocumentBean1.LENGTE;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsDocumentBean1.NAAM;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsDocumentBean1.NATIONALITEIT;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsDocumentBean1.STAATLOOS;

import com.vaadin.ui.Button;
import java.util.List;
import java.util.function.BiConsumer;
import nl.procura.burgerzaken.vrsclient.model.ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponse;
import nl.procura.burgerzaken.vrsclient.model.ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponseMeldingDetailDto;
import nl.procura.burgerzaken.vrsclient.model.ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponseStatusDto;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.HLayout;

public class VrsDocumentenWindow extends GbaModalWindow {

  private final ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponse                                  response2;
  private final BiConsumer<ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponse, VrsDocumentenWindow> documentConsumer;

  public VrsDocumentenWindow(ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponse response,
      BiConsumer<ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponse,
          VrsDocumentenWindow> documentConsumer) {
    super("De reisdocumentgegevens in het Basisregister reisdocumenten (Esc om te sluiten)", "1000px");
    this.response2 = response;
    this.documentConsumer = documentConsumer;
  }

  @Override
  public void attach() {
    super.attach();
    addComponent(new MainModuleContainer(false, new Page2(response2)));
  }

  public class Page2 extends NormalPageTemplate {

    private final ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponse                response;
    private       List<ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponseStatusDto> statussen;

    public Page2(ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponse response) {
      this.response = response;
    }

    @Override
    protected void initPage() {
      super.initPage();

      if (getNavigation().getPreviousPage() != null) {
        addButton(buttonPrev);
      }

      if (documentConsumer != null) {
        addButton(
            new Button("Selecteer dit document", e -> documentConsumer.accept(response, VrsDocumentenWindow.this)));
      }

      HLayout hLayout1 = new HLayout();
      hLayout1.setWidth("100%");
      hLayout1.add(new VrsDocumentForm1(response, "Reisdocument",
          DOC_NR, DOC_SOORT, DOC_AFGIFTE, DOC_GELDIG_TOT));
      hLayout1.add(new VrsDocumentForm1(response, "Personalisatiegegevens",
          LENGTE, STAATLOOS, AANVRAAGNUMMER));
      addComponent(hLayout1);

      HLayout hLayout2 = new HLayout();
      hLayout2.setWidth("100%");
      hLayout2.add(new VrsDocumentForm1(response, "Persoon",
          BSN, NAAM, GEBOORTEDATUM, GEBOORTEPLAATS, NATIONALITEIT, GESLACHT));
      hLayout2.add(new VrsDocumentForm1(response, "Autoriteiten", AUT_VAN_VERSTREK));
      addComponent(hLayout2);
      addComponent(new Fieldset("Statussen"));
      statussen = response.getStatussen();
      addExpandComponent(new Table());
    }

    @Override
    public void onPreviousPage() {
      getNavigation().goBackToPreviousPage();
    }

    public class Table extends GbaTable {

      public Table() {
        setSelectable(false);
        setClickable(false);
      }

      @Override
      public void setColumns() {
        setSelectable(true);
        addColumn("Datum / tijd", 150);
        addColumn("Status", 300);
        addColumn("Melding");
        super.setColumns();
      }

      @Override
      public void setRecords() {
        for (ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponseStatusDto status : statussen) {
          Record r = addRecord(status);
          r.addValue(DateTime.of(status.getDocumentstatusStartdatum().toLocalDateTime()));
          r.addValue(status.getDocumentstatusOmschrijving());
          r.addValue(getMelding(status));
        }
        super.setRecords();
      }
    }

    private String getMelding(ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponseStatusDto status) {
      return status.getMeldingen()
          .stream()
          .filter(melding -> melding.getMeldingOmschrijving() != null)
          .findFirst()
          .map(ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponseMeldingDetailDto::getMeldingOmschrijving)
          .orElse("");
    }
  }
}
