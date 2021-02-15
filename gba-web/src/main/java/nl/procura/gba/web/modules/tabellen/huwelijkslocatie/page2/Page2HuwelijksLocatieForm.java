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

package nl.procura.gba.web.modules.tabellen.huwelijkslocatie.page2;

import static nl.procura.gba.web.modules.tabellen.belanghebbende.page2.Page2BelanghebbendeBean.INGANG_GELD;
import static nl.procura.gba.web.modules.tabellen.huwelijkslocatie.page2.Page2HuwelijksLocatieBean.*;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.ui.Field;

import nl.procura.gba.web.components.FormFieldset;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.gba.basistabellen.huwelijkslocatie.HuwelijksLocatie;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;

public class Page2HuwelijksLocatieForm extends GbaForm<Page2HuwelijksLocatieBean> {

  public Page2HuwelijksLocatieForm(HuwelijksLocatie locatie) {

    setCaption("Locatie");
    setOrder(LOCATIE, SOORT, ALIAS, TOELICHTING, INGANG_GELD, EINDE_GELD);
    setColumnWidths(WIDTH_130, "");

    Page2HuwelijksLocatieBean bean = new Page2HuwelijksLocatieBean();
    bean.setLocatie(locatie.getHuwelijksLocatie());
    bean.setSoort(locatie.getLocatieSoort());
    bean.setAlias(StringUtils.join(locatie.getAliassen(), ", "));
    bean.setToelichting(locatie.getToelichting());
    bean.setIngangGeld(new DateFieldValue(locatie.getDatumIngang().getLongDate()));
    bean.setEindeGeld(new DateFieldValue(locatie.getDatumEinde().getLongDate()));

    setBean(bean);
  }

  @Override
  public Field newField(Field field, Property property) {
    if (property.is(INGANG_GELD)) {
      getLayout().addBreak(new FormFieldset("Geldigheid"));
    }

    return super.newField(field, property);
  }
}
