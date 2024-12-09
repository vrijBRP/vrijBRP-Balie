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

package nl.procura.gba.web.modules.persoonslijst.reisdocument.page1;

import static nl.procura.burgerzaken.vrsclient.api.VrsAanleidingType.IDENTITEITSONDERZOEK;
import static nl.procura.standard.Globalfunctions.astr;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import nl.procura.burgerzaken.vrsclient.api.VrsRequest;
import nl.procura.burgerzaken.vrsclient.model.ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponse;
import nl.procura.burgerzaken.vrsclient.model.ReisdocumentInformatiePersoonsGegevensInstantieResponseReisdocumentInformatiePersoonsgegevens;
import nl.procura.burgerzaken.vrsclient.model.ReisdocumentInformatiePersoonsGegevensInstantieResponseReisdocumentPersoonsgegevensResponse;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsDocumentenWindow;

public class Page1ReisdocumentTable2 extends GbaTable {

  private final List<ReisdocumentInformatiePersoonsGegevensInstantieResponseReisdocumentInformatiePersoonsgegevens> reisdocumenten;

  public Page1ReisdocumentTable2(
      List<ReisdocumentInformatiePersoonsGegevensInstantieResponseReisdocumentInformatiePersoonsgegevens> reisdocumenten) {
    this.reisdocumenten = reisdocumenten;
    setSelectable(true);
  }

  @Override
  public void setColumns() {
    addColumn("Vnr.", 50);
    addColumn("&nbsp;", 20).setUseHTML(true);
    addColumn("Nummer", 100);
    addColumn("Reisdocument");
    addColumn("Status", 200);
    addColumn("Uitgifte", 90);
    addColumn("Einde", 90);
    addColumn("Vermissing", 250);

    super.setColumns();
  }

  @Override
  public void setRecords() {
    if (isNotEmpty(reisdocumenten)) {
      int nr = reisdocumenten.size();
      for (ReisdocumentInformatiePersoonsGegevensInstantieResponseReisdocumentInformatiePersoonsgegevens reisdoc : reisdocumenten) {
        ReisdocumentInformatiePersoonsGegevensInstantieResponseReisdocumentPersoonsgegevensResponse reisdocument = reisdoc
            .getReisdocument();
        Record r = addRecord(reisdoc);
        r.addValue(astr(nr--));
        r.addValue("");
        r.addValue(reisdocument.getDocumentnummer());
        r.addValue(reisdocument.getDocumentsoort().getOmschrijving());
        r.addValue(reisdocument.getStatusMeestRecent().getDocumentstatusOmschrijving());
        r.addValue(DateTime.of(reisdocument.getDatumafgifte()));
        r.addValue(DateTime.of(reisdocument.getDatumGeldigTot()));
        r.addValue(Optional.ofNullable(reisdocument.getDatumMeldingvermissing())
            .map(OffsetDateTime::toLocalDate)
            .map(DateTime::of)
            .orElse(null));
      }
    }
  }

  @Override
  public void onClick(Record record) {
    ReisdocumentInformatiePersoonsGegevensInstantieResponseReisdocumentInformatiePersoonsgegevens value = record
        .getObject(ReisdocumentInformatiePersoonsGegevensInstantieResponseReisdocumentInformatiePersoonsgegevens.class);

    VrsRequest request = new VrsRequest()
        .aanleiding(IDENTITEITSONDERZOEK)
        .documentnummer(value.getReisdocument().getDocumentnummer());

    Optional<ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponse> document = getApplication()
        .getServices()
        .getReisdocumentService()
        .getVrsService()
        .getReisdocument(request);

    document.ifPresent(response -> getApplication()
        .getParentWindow()
        .addWindow(new VrsDocumentenWindow(response, null)));
  }
}
