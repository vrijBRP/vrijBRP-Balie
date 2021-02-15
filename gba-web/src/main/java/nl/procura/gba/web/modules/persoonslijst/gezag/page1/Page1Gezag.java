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

package nl.procura.gba.web.modules.persoonslijst.gezag.page1;

import static nl.procura.gba.web.modules.persoonslijst.overig.data.PlDataBean.*;
import static nl.procura.standard.Globalfunctions.pos;

import com.vaadin.ui.Field;

import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.gba.web.modules.persoonslijst.overig.PlForm;
import nl.procura.gba.web.modules.persoonslijst.overig.data.PlDataBeanForm;
import nl.procura.gba.web.modules.persoonslijst.overig.data.PlDataPage;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page1Gezag extends PlDataPage {

  public Page1Gezag(BasePLSet set) {
    super("Gezagsverhouding", set);
  }

  @Override
  public PlForm getMainForm() {

    return new Page1GezagForm() {

      @Override
      public void setBean(Object bean) {

        Page1GezagBean b = new Page1GezagBean();
        b.setGezag(getElementW(GBAElem.IND_GEZAG_MINDERJ).getDescr());

        BasePLElem curElem = getElement(GBAElem.IND_CURATELE_REG);
        String curatorCode = curElem.getValue().getCode();
        String curatorOms = curElem.getValue().getDescr();
        b.setCuratele(curElem.isAllowed() ? (pos(curatorCode) ? "Er is een curator aangesteld." : "") : curatorOms);

        super.setBean(b);
      }
    };
  }

  @Override
  public PlDataBeanForm getMetaForm(BasePLRec record) {

    return new PlDataBeanForm(record) {

      @Override
      public String[] getOrder() {
        return new String[]{ GELDIGHEID, OPNAME, ONJUIST, INONDERZOEK,
            DATUMINGANG, DATUMEINDE, ONTLENING, DOCUMENT };
      }

      @Override
      public void setColumn(Column column, Field field, Property property) {
        if (property.is(ONJUIST, ONTLENING)) {
          getLayout().addBreak();
        }
      }
    };
  }
}
