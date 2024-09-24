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

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.zaken.reisdocument.page14.Page14ReisdocumentTable1;
import nl.procura.gba.web.services.zaken.identiteit.IdentificatieType;
import nl.procura.gba.web.services.zaken.reisdocumenten.Reisdocument;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentType;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.layout.info.InfoLayout;

public class Page1IdentificatieReisdocumentWindow extends GbaModalWindow {

  private final Page1Identificatie page1;

  public Page1IdentificatieReisdocumentWindow(Page1Identificatie page1, BasePLExt pl) {
    super("Reisdocumenten (Esc om te sluiten)", "1000px");
    this.page1 = page1;
    setContent(new VLayout()
        .margin(true)
        .spacing(true)
        .add(new InfoLayout("De huidige reisdocumenten",
            "Selecteer een record om te gebruiken in het vorige scherm"))
        .add(new Table(pl)));
  }

  class Table extends Page14ReisdocumentTable1 {

    private Table(BasePLExt pl) {
      super(pl);
    }

    @Override
    public void onClick(Record record) {
      Reisdocument rd = (Reisdocument) record.getObject();
      if (rd != null) {
        switch (ReisdocumentType.get(rd.getNederlandsReisdocument().getVal())) {
          case EERSTE_NATIONAAL_PASPOORT:
          case EERSTE_ZAKENPASPOORT:
          case FACILITEITEN_PASPOORT:
          case TWEEDE_NATIONAAL_PASPOORT:
          case TWEEDE_ZAKENPASPOORT:
          case VLUCHTELINGEN_PASPOORT:
          case VREEMDELINGEN_PASPOORT:
            page1.setDocumentGegevens(IdentificatieType.PASPOORT, rd.getNummerDocument().getVal());
            break;

          case NEDERLANDSE_IDENTITEITSKAART:
            page1.setDocumentGegevens(IdentificatieType.IDENTITEITSKAART,
                rd.getNummerDocument().getVal());
            break;

          default:
            break;
        }
        closeWindow();
      }
    }
  }
}
