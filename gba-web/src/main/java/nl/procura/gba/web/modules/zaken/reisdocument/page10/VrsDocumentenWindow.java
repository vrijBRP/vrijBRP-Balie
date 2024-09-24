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

import static nl.procura.burgerzaken.vrsclient.api.VrsAanleidingType.REISDOCUMENTAANVRAAG;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsDocumentBean1.AANVRAAGNUMMER;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsDocumentBean1.AUT_VAN_AFGIFTE;
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

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

import com.vaadin.ui.Button;

import nl.procura.burgerzaken.vrsclient.api.VrsRequest;
import nl.procura.burgerzaken.vrsclient.model.ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponse;
import nl.procura.burgerzaken.vrsclient.model.ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponseMeldingDetailDto;
import nl.procura.burgerzaken.vrsclient.model.ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponseStatusDto;
import nl.procura.burgerzaken.vrsclient.model.ReisdocumentInformatiePersoonsGegevensInstantieResponse;
import nl.procura.burgerzaken.vrsclient.model.ReisdocumentInformatiePersoonsGegevensInstantieResponseReisdocumentInformatiePersoonsgegevens;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.reisdocumenten.Aanvraagnummer;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.HLayout;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.validation.Bsn;

public class VrsDocumentenWindow extends GbaModalWindow {

  private Aanvraagnummer                                                                                    aanvraagnummer;
  private ReisdocumentInformatiePersoonsGegevensInstantieResponse                                           response1;
  private ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponse                                  response2;
  private BiConsumer<ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponse, VrsDocumentenWindow> documentConsumer;

  public VrsDocumentenWindow(Aanvraagnummer aanvraagnummer,
      ReisdocumentInformatiePersoonsGegevensInstantieResponse response1) {
    super("De reisdocumentgegevens in het Basisregister reisdocumenten (Esc om te sluiten)", "1000px");
    this.aanvraagnummer = aanvraagnummer;
    this.response1 = response1;
  }

  public VrsDocumentenWindow(ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponse response,
      BiConsumer<ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponse, VrsDocumentenWindow> documentConsumer) {
    super("De reisdocumentgegevens in het Basisregister reisdocumenten (Esc om te sluiten)", "1000px");
    this.response2 = response;
    this.documentConsumer = documentConsumer;
  }

  @Override
  public void attach() {
    super.attach();
    if (response1 != null) {
      addComponent(new MainModuleContainer(false, new Page1()));
    } else {
      addComponent(new MainModuleContainer(false, new Page2(response2)));
    }
  }

  public class Page1 extends NormalPageTemplate {

    public Page1() {
      addComponent(new InfoLayout("De huidige reisdocumenten in het basisregister van het RvIG",
          "Klik op een regel voor meer detailgegevens van het reisdocument"));
      addComponent(new Table());
    }

    public class Table extends GbaTable {

      @Override
      public void setColumns() {
        setSelectable(true);
        addColumn("Nummer", 100);
        addColumn("Soort");
        addColumn("Status", 200);
        addColumn("Datum afgifte", 100).setUseHTML(true);
        addColumn("Geldig tot", 100).setUseHTML(true);
        addColumn("Vermissing", 110).setUseHTML(true);

        super.setColumns();
      }

      @Override
      public void setRecords() {
        List<ReisdocumentInformatiePersoonsGegevensInstantieResponseReisdocumentInformatiePersoonsgegevens> reisdocumentenLijst = response1
            .getReisdocumentenLijst();
        for (ReisdocumentInformatiePersoonsGegevensInstantieResponseReisdocumentInformatiePersoonsgegevens document : reisdocumentenLijst) {
          Record r = addRecord(document);
          r.addValue(document.getReisdocument().getDocumentnummer());
          r.addValue(document.getReisdocument().getDocumentsoort().getOmschrijving());
          r.addValue(document.getReisdocument().getStatusMeestRecent().getDocumentstatusOmschrijving());
          r.addValue(DateTime.of(document.getReisdocument().getDatumafgifte()));
          r.addValue(DateTime.of(document.getReisdocument().getDatumGeldigTot()));
          r.addValue(Optional.ofNullable(document.getReisdocument().getDatumMeldingvermissing())
              .map(OffsetDateTime::toLocalDate)
              .map(DateTime::of)
              .orElse(null));
        }
      }

      @Override
      public void onClick(Record record) {
        ReisdocumentInformatiePersoonsGegevensInstantieResponseReisdocumentInformatiePersoonsgegevens rd = record
            .getObject(
                ReisdocumentInformatiePersoonsGegevensInstantieResponseReisdocumentInformatiePersoonsgegevens.class);
        if (rd != null) {
          Services services = getApplication().getServices();
          VrsRequest request = new VrsRequest()
              .aanleiding(REISDOCUMENTAANVRAAG)
              .bsn(new Bsn(rd.getReisdocument().getPersoon().getBsn()))
              .aanvraagnummer(aanvraagnummer.getNummer())
              .documentnummer(rd.getReisdocument().getDocumentnummer());

          services.getReisdocumentService()
              .getVrsService()
              .getReisdocument(request)
              .ifPresent(response -> getNavigation().goToPage(new Page2(response)));
        }
      }
    }
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
      hLayout2.add(new VrsDocumentForm1(response, "Autoriteiten",
          AUT_VAN_AFGIFTE, AUT_VAN_VERSTREK));
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
