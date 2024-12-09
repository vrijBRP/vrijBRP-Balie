package nl.procura.gba.web.modules.zaken.reisdocument.basisregister.page1;

import static nl.procura.burgerzaken.vrsclient.api.VrsAanleidingType.IDENTITEITSONDERZOEK;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import nl.procura.burgerzaken.vrsclient.api.VrsRequest;
import nl.procura.burgerzaken.vrsclient.model.ReisdocumentInformatiePersoonsGegevensInstantieResponse;
import nl.procura.burgerzaken.vrsclient.model.ReisdocumentInformatiePersoonsGegevensInstantieResponseReisdocumentInformatiePersoonsgegevens;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.zaken.reisdocument.basisregister.page2.Page2VrsBasisRegister;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.reisdocumenten.Aanvraagnummer;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.validation.Bsn;

public class Page1VrsBasisRegister extends NormalPageTemplate {

  private final BasePLExt      pl;
  private final Aanvraagnummer aanvraagnummer;

  public Page1VrsBasisRegister(BasePLExt pl, Aanvraagnummer aanvraagnummer) {
    this.pl = pl;
    this.aanvraagnummer = aanvraagnummer;
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
      addColumn("Status", 250);
      addColumn("Datum afgifte", 100).setUseHTML(true);
      addColumn("Geldig tot", 100).setUseHTML(true);
      addColumn("Vermissing", 110).setUseHTML(true);

      super.setColumns();
    }

    @Override
    public void setRecords() {
      Optional<ReisdocumentInformatiePersoonsGegevensInstantieResponse> vrsDocumenten = getApplication().getServices()
          .getReisdocumentService()
          .getVrsService()
          .getReisdocumenten(pl, aanvraagnummer);

      vrsDocumenten.ifPresent(resp -> {
        List<ReisdocumentInformatiePersoonsGegevensInstantieResponseReisdocumentInformatiePersoonsgegevens> reisdocumentenLijst = resp
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
      });
    }

    @Override
    public void onClick(Record record) {
      ReisdocumentInformatiePersoonsGegevensInstantieResponseReisdocumentInformatiePersoonsgegevens rd = record
          .getObject(
              ReisdocumentInformatiePersoonsGegevensInstantieResponseReisdocumentInformatiePersoonsgegevens.class);
      if (rd != null) {
        Services services = getApplication().getServices();
        VrsRequest request = new VrsRequest()
            .aanleiding(IDENTITEITSONDERZOEK)
            .bsn(new Bsn(rd.getReisdocument().getPersoon().getBsn()))
            .documentnummer(rd.getReisdocument().getDocumentnummer());

        services.getReisdocumentService()
            .getVrsService()
            .getReisdocument(request)
            .ifPresent(response -> {
              getNavigation().goToPage(new Page2VrsBasisRegister(pl, response));
            });
      }
    }
  }
}