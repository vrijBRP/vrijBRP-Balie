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

package nl.procura.gba.web.modules.zaken.identificatie.page1;

import static nl.procura.gba.web.modules.zaken.identificatie.page1.Page1IdentificatieBean.EXTERNE_APP;
import static nl.procura.gba.web.modules.zaken.identificatie.page1.Page1IdentificatieBean.REISDOCUMENT_ADMINISTRATIE;
import static nl.procura.gba.web.modules.zaken.identificatie.page1.Page1IdentificatieBean.RIJBEWIJS_ADMINISTRATIE;
import static nl.procura.gba.web.modules.zaken.identificatie.page1.Page1IdentificatieBean.RPS;
import static nl.procura.gba.web.modules.zaken.identificatie.page1.Page1IdentificatieBean.VRAGEN_NIET_MOGELIJK;

import com.vaadin.ui.Field;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page1IdentificatieForm2 extends GbaForm<Page1IdentificatieBean> {

  public Page1IdentificatieForm2() {

    setCaption("Overig");
    setOrder(REISDOCUMENT_ADMINISTRATIE, RIJBEWIJS_ADMINISTRATIE, EXTERNE_APP, RPS, VRAGEN_NIET_MOGELIJK);
    setColumnWidths("240px", "", "220px", "");
    setBean(new Page1IdentificatieBean());
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {
    if (property.is(VRAGEN_NIET_MOGELIJK)) {
      column.setColspan(3);
    }
    super.setColumn(column, field, property);
  }
}
