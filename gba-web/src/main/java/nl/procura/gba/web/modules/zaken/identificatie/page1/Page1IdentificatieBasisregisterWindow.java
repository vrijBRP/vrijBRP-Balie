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

package nl.procura.gba.web.modules.zaken.identificatie.page1;

import static nl.procura.burgerzaken.vrsclient.api.VrsAanleidingType.IDENTITEITSONDERZOEK;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import nl.procura.burgerzaken.vrsclient.api.VrsRequest;
import nl.procura.burgerzaken.vrsclient.model.ReisdocumentInformatiePersoonsGegevensInstantieResponse;
import nl.procura.burgerzaken.vrsclient.model.ReisdocumentInformatiePersoonsGegevensInstantieResponseReisdocumentInformatiePersoonsgegevens;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.identiteit.IdentificatieType;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentType;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.validation.Bsn;

public class Page1IdentificatieBasisregisterWindow extends GbaModalWindow {

  private final Page1Identificatie page1;
  private final BasePLExt          pl;

  public Page1IdentificatieBasisregisterWindow(Page1Identificatie page1, BasePLExt pl) {
    super("Reisdocumenten (Esc om te sluiten)", "1000px");
    this.page1 = page1;
    this.pl = pl;

    setContent(new VLayout()
        .margin(true)
        .spacing(true)
        .add(new InfoLayout("De huidige reisdocumenten in het basisregister van het RvIG",
            "Selecteer een record om te gebruiken in het vorige scherm"))
        .add(new Table()));
  }

  class Table extends GbaTable {

    @Override
    public void setColumns() {
      setSelectable(true);
      addColumn("Nummer", 100);
      addColumn("Soort");
      addColumn("Status", 200);
      addColumn("Datum afgifte", 100).setUseHTML(true);
      addColumn("Geldig tot", 100).setUseHTML(true);
      addColumn("Datum vermissing", 110).setUseHTML(true);

      super.setColumns();
    }

    @Override
    public void setRecords() {
      Services services = getApplication().getServices();
      VrsRequest request = new VrsRequest()
          .aanleiding(IDENTITEITSONDERZOEK)
          .bsn(new Bsn(pl.getPersoon().getBsn().toLong()));

      List<ReisdocumentInformatiePersoonsGegevensInstantieResponseReisdocumentInformatiePersoonsgegevens> reisdocumentenLijst = services
          .getReisdocumentService()
          .getVrsService()
          .getReisdocumenten(request)
          .map(ReisdocumentInformatiePersoonsGegevensInstantieResponse::getReisdocumentenLijst)
          .orElse(new ArrayList<>());

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

      super.setRecords();
    }

    @Override
    public void onClick(Record record) {
      ReisdocumentInformatiePersoonsGegevensInstantieResponseReisdocumentInformatiePersoonsgegevens rd = record
          .getObject(
              ReisdocumentInformatiePersoonsGegevensInstantieResponseReisdocumentInformatiePersoonsgegevens.class);
      if (rd != null) {
        switch (ReisdocumentType.get(rd.getReisdocument().getDocumentsoort().getCode())) {
          case EERSTE_NATIONAAL_PASPOORT:
          case EERSTE_ZAKENPASPOORT:
          case FACILITEITEN_PASPOORT:
          case TWEEDE_NATIONAAL_PASPOORT:
          case TWEEDE_ZAKENPASPOORT:
          case VLUCHTELINGEN_PASPOORT:
          case VREEMDELINGEN_PASPOORT:
            page1.setDocumentGegevens(IdentificatieType.PASPOORT, rd.getReisdocument().getDocumentnummer());
            break;

          case NEDERLANDSE_IDENTITEITSKAART:
            page1.setDocumentGegevens(IdentificatieType.IDENTITEITSKAART,
                rd.getReisdocument().getDocumentnummer());
            break;

          default:
            break;
        }
        closeWindow();
      }
    }
  }
}
