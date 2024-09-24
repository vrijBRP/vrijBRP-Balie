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

package nl.procura.gba.web.modules.persoonslijst.kiesrecht.page1;

import static nl.procura.gba.web.modules.persoonslijst.kiesrecht.page1.Page1KiesrechtBean.AANDUIDINGEUROPEESKIESRECHT;
import static nl.procura.gba.web.modules.persoonslijst.kiesrecht.page1.Page1KiesrechtBean.AANDUIDINGUITGESLOTENKIESRECHT;
import static nl.procura.gba.web.modules.persoonslijst.kiesrecht.page1.Page1KiesrechtBean.ADRES_EU_LIDSTAAT_VAN_HERKOMST;
import static nl.procura.gba.web.modules.persoonslijst.kiesrecht.page1.Page1KiesrechtBean.DATUMVERZOEKMEDEDELING;
import static nl.procura.gba.web.modules.persoonslijst.kiesrecht.page1.Page1KiesrechtBean.EINDDATUMUITSLUITING;
import static nl.procura.gba.web.modules.persoonslijst.kiesrecht.page1.Page1KiesrechtBean.EINDDATUMUITSLUITINGEUROPEESKIESRECHT;
import static nl.procura.gba.web.modules.persoonslijst.kiesrecht.page1.Page1KiesrechtBean.LAND_EU_LIDSTAAT_VAN_HERKOMST;
import static nl.procura.gba.web.modules.persoonslijst.kiesrecht.page1.Page1KiesrechtBean.PLAATS_EU_LIDSTAAT_VAN_HERKOMST;

import com.vaadin.ui.Field;

import nl.procura.gba.web.modules.persoonslijst.overig.PlForm;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page1KiesrechtForm extends PlForm {

  public Page1KiesrechtForm() {

    setOrder(AANDUIDINGUITGESLOTENKIESRECHT, EINDDATUMUITSLUITING, AANDUIDINGEUROPEESKIESRECHT,
        DATUMVERZOEKMEDEDELING, EINDDATUMUITSLUITINGEUROPEESKIESRECHT,
        ADRES_EU_LIDSTAAT_VAN_HERKOMST, PLAATS_EU_LIDSTAAT_VAN_HERKOMST, LAND_EU_LIDSTAAT_VAN_HERKOMST);
    setColumnWidths("200px", "");
  }

  @Override
  public Object getNewBean() {
    return new Page1KiesrechtBean();
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {
    if (property.is(AANDUIDINGEUROPEESKIESRECHT, ADRES_EU_LIDSTAAT_VAN_HERKOMST)) {
      getLayout().addBreak();
    }
  }
}
