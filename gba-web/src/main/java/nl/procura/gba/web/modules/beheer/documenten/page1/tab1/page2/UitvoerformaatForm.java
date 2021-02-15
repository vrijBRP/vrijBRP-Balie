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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page2;

import static nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page2.UitvoerformaatBean.*;

import java.util.List;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.UitvoerformaatType;
import nl.procura.gba.web.services.zaken.documenten.printopties.PrintOptie;
import nl.procura.gba.web.services.zaken.documenten.printopties.PrintOptieType;

public class UitvoerformaatForm extends GbaForm<UitvoerformaatBean> {

  public UitvoerformaatForm(DocumentRecord document) {
    setCaption("Uitvoerformaten");
    setOrder(PDF, ODT, DOC);
    setColumnWidths("170px", "170px", "170px", "170px", "170px", "");
    initFields(document);
  }

  private void initFields(DocumentRecord document) {

    UitvoerformaatBean bean = new UitvoerformaatBean();
    List<PrintOptie> printopties = document.getPrintOpties();

    for (PrintOptie p : printopties) {
      if (p.getPrintType() == PrintOptieType.POPUP) {
        switch (p.getUitvoerformaatType()) {
          case PDF:
            bean.setPdf(UitvoerformaatType.PDF);
            break;

          case PDF_A1:
            bean.setPdf(UitvoerformaatType.PDF_A1);
            break;

          case ODT:
            bean.setOdt(true);
            break;

          case DOC:
            bean.setDoc(true);
            break;

          default:
            break;
        }
      }
    }

    setBean(bean);
    repaint();
  }
}
