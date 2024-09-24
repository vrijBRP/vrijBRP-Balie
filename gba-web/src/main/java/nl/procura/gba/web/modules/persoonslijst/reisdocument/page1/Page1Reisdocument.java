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

import java.util.Optional;

import nl.procura.burgerzaken.vrsclient.api.VrsRequest;
import nl.procura.burgerzaken.vrsclient.model.ReisdocumentInformatiePersoonsGegevensInstantieResponse;
import nl.procura.diensten.gba.ple.base.BasePLCat;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.gba.web.modules.persoonslijst.overig.listpage.PlListPage;
import nl.procura.gba.web.modules.persoonslijst.reisdocument.page2.Page2Reisdocument;
import nl.procura.gba.web.services.beheer.vrs.VrsService;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;
import nl.procura.validation.Bsn;

public class Page1Reisdocument extends PlListPage {

  private final Page1ReisdocumentTable1 table1;
  private       Page1ReisdocumentTable2 table2;

  public Page1Reisdocument(BasePLCat soort) {
    super("Reisdocument");

    table1 = new Page1ReisdocumentTable1(soort) {

      @Override
      public void onClick(Record record) {
        selectRecord(record);
      }
    };
  }

  @Override
  protected void initPage() {

    super.initPage();

    VrsService vrsService = getServices().getReisdocumentService().getVrsService();
    Optional<ReisdocumentInformatiePersoonsGegevensInstantieResponse> vrsDocumenten = vrsService
        .getReisdocumenten(new VrsRequest()
            .aanleiding(IDENTITEITSONDERZOEK)
            .bsn(new Bsn(getPl().getPersoon().getBsn().toLong())));

    vrsDocumenten.ifPresent(response -> {
      table2 = new Page1ReisdocumentTable2(response.getReisdocumentenLijst());
    });

    if (table2 != null) {
      addComponent(new Fieldset("Reisdocumenten op de persoonslijst", table1));
      addComponent(new Fieldset("Reisdocumenten in het basisregister"));
      addExpandComponent(table2);
    } else {
      addExpandComponent(table1);
    }
  }

  @Override
  public void onEnter() {
    if (table1.getRecord() != null) {
      selectRecord(table1.getRecord());
    }
  }

  private void selectRecord(Record record) {
    Page2Reisdocument page2 = new Page2Reisdocument((BasePLSet) record.getObject());
    getNavigation().goToPage(page2);
  }
}
