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

package nl.procura.gba.web.modules.zaken.rijbewijs.page4.page4b;

import static nl.procura.gba.web.modules.zaken.rijbewijs.page4.page4b.Page4bRijbewijsBean3.*;
import static nl.procura.standard.Globalfunctions.date2str;

import com.vaadin.ui.Field;

import nl.procura.gba.web.modules.zaken.rijbewijs.RdwReadOnlyForm;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsOngeldigCodeLV;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsUitgever;
import nl.procura.rdw.processen.p0252.f07.UITGMAATRGEG;
import nl.procura.rdw.processen.p0252.f07.VORDPROCGEG;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

/**
 * Vorderingsprocedure gegevens
 */
public class Page4bRijbewijsForm3 extends RdwReadOnlyForm {

  Page4bRijbewijsForm3(UITGMAATRGEG c) {

    setCaption("Vorderingsprocedure gegevens");

    setOrder(DATUM, EINDE1, AUTORITEIT4, AUTORITEIT5, AUTORITEIT6, RKKCODE, KORPSCODE, VERBALISANT, DOSSIER,
        SCHORSING, EINDE2, FEITELIJK, ONGELDIG1, ONGELDIG2, FEITELINL);

    setColumnWidths(WIDTH_130, "300px", "130px", "");

    Page4bRijbewijsBean3 b = new Page4bRijbewijsBean3();

    VORDPROCGEG ig = c.getVordprocgeg();

    b.setDatum(date2str(ig.getInvorddatvp().toString()));
    b.setEinde1(date2str(ig.getEindeinvvp().toString()));
    b.setAutoriteit4(RijbewijsUitgever.getCodeOms(ig.getSrtautinlvp()));
    b.setAutoriteit5(trimInteger(ig.getAutorcinlvp()) + " " + ig.getNaamautinlvp());
    b.setAutoriteit6(ig.getNaaminlvpdia());
    b.setRkkcode(ig.getRkkcodevp());
    b.setKorpscode(ig.getKorpscodevp());
    b.setVerbalisant(ig.getVerbalisnrvp());
    b.setDossier(ig.getDossiernrvp());
    b.setSchorsing(date2str(ig.getSchorsgelddat().toString()));
    b.setEinde2(date2str(ig.getEindeschdat().toString()));
    b.setFeitelijk(date2str(ig.getFeitindatsch().toString()));
    b.setOngeldig1(date2str(ig.getOngelddatvp().toString()));
    b.setOngeldig2(RijbewijsOngeldigCodeLV.getCodeOms(ig.getOngeldcodevp()));
    b.setFeitelinl(date2str(ig.getFeitinldatvp().toString()));

    setBean(b);
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {

    if (property.is(FEITELINL)) {
      column.setColspan(3);
    }

    super.setColumn(column, field, property);
  }
}
