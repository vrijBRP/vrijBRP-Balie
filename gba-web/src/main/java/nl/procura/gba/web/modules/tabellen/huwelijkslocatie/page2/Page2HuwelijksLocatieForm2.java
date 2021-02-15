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

import static nl.procura.gba.web.modules.tabellen.huwelijkslocatie.page2.Page2HuwelijksLocatieBean2.*;

import com.vaadin.ui.Field;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.gba.basistabellen.huwelijkslocatie.HuwelijksLocatieContactpersoon;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page2HuwelijksLocatieForm2 extends GbaForm<Page2HuwelijksLocatieBean2> {

  public Page2HuwelijksLocatieForm2(HuwelijksLocatieContactpersoon persoon) {

    setColumnWidths(WIDTH_130, "");
    setCaption("Contactpersoon");
    setOrder(TAV_AANHEF, TAV_VOORL, TAV_NAAM, ADRES, POSTCODE, PLAATS, LAND, TELEFOON, EMAIL);
    updateBean(persoon);
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {

    if (property.is(TAV_VOORL, TAV_NAAM)) {
      column.setAppend(true);
    }

    super.setColumn(column, field, property);
  }

  public void updateBean(HuwelijksLocatieContactpersoon persoon) {

    Page2HuwelijksLocatieBean2 bean = new Page2HuwelijksLocatieBean2();
    bean.setTavAanhef(persoon.getTerAttentieVanAanhef());
    bean.setTavVoorl(persoon.getTavVoorl());
    bean.setTavNaam(persoon.getTavNaam());
    bean.setAdres(persoon.getAdres());
    bean.setPostcode(persoon.getPostcode());
    bean.setPlaats(persoon.getPlaats());
    bean.setLand(persoon.getLand());
    bean.setTelefoon(persoon.getTelefoon());
    bean.setEmail(persoon.getEmail());

    setBean(bean);
  }
}
