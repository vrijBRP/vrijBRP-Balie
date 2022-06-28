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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.verwijderen.page2;

import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.verwijderen.page2.Page2VerwijderenBean.*;

import java.util.function.Consumer;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.listeners.FieldChangeListener;
import nl.procura.gba.web.services.zaken.opschonen.VerwijderZakenActie;

public class Page2VerwijderenForm extends GbaForm<Page2VerwijderenBean> {

  private Consumer<Integer> consumer;

  public Page2VerwijderenForm() {
  }

  public Page2VerwijderenForm(VerwijderZakenActie telling, Consumer<Integer> consumer) {
    this.consumer = consumer;
    setOrder(OMS, TOTAAL, AANTAL);
    setReadThrough(true);
    setColumnWidths(WIDTH_130, "", "50px", "100px", "100px", "200px");
    Page2VerwijderenBean bean = new Page2VerwijderenBean();
    bean.setOms(telling.getVerwijderActie().getType().getOmschrijving());
    bean.setTotaal(telling.getVerwijderActie().getAantal());
    setBean(bean);
  }

  @Override
  public void afterSetBean() {
    super.afterSetBean();
    getField(AANTAL, GbaNativeSelect.class)
        .addListener(new FieldChangeListener<AantalResultaten>() {

          @Override
          public void onChange(AantalResultaten value) {
            consumer.accept(value.getAantal());
          }
        });
  }

  @Override
  public Page2VerwijderenBean getNewBean() {
    return new Page2VerwijderenBean();
  }
}
