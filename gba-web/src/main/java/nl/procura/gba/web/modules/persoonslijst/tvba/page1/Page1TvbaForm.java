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

package nl.procura.gba.web.modules.persoonslijst.tvba.page1;

import static nl.procura.gba.web.modules.persoonslijst.tvba.page1.Page1TvbaBean.AANGIFTE;
import static nl.procura.gba.web.modules.persoonslijst.tvba.page1.Page1TvbaBean.ADRES;
import static nl.procura.gba.web.modules.persoonslijst.tvba.page1.Page1TvbaBean.AON;
import static nl.procura.gba.web.modules.persoonslijst.tvba.page1.Page1TvbaBean.DATUMINSCHRIJVING;
import static nl.procura.gba.web.modules.persoonslijst.tvba.page1.Page1TvbaBean.EINDE_GELDIG;
import static nl.procura.gba.web.modules.persoonslijst.tvba.page1.Page1TvbaBean.GEMEENTE;
import static nl.procura.gba.web.modules.persoonslijst.tvba.page1.Page1TvbaBean.INA;
import static nl.procura.gba.web.modules.persoonslijst.tvba.page1.Page1TvbaBean.OPENBARE_RUIMTE;
import static nl.procura.gba.web.modules.persoonslijst.tvba.page1.Page1TvbaBean.TYPE_ADRES;
import static nl.procura.gba.web.modules.persoonslijst.tvba.page1.Page1TvbaBean.WOONPLAATS;

import com.vaadin.ui.Field;

import nl.procura.gba.web.modules.persoonslijst.overig.PlForm;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page1TvbaForm extends PlForm {

  public Page1TvbaForm() {
    setOrder(ADRES, OPENBARE_RUIMTE, WOONPLAATS, GEMEENTE, DATUMINSCHRIJVING, AANGIFTE, EINDE_GELDIG, TYPE_ADRES, AON,
        INA);
    setColumnWidths("160px", "");
  }

  @Override
  public Object getNewBean() {
    return new Page1TvbaBean();
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {
    if (property.is(AANGIFTE)) {
      getLayout().addBreak();
    }
  }
}
