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

package nl.procura.gba.web.modules.zaken.verkiezing.page1;

import static nl.procura.gba.web.modules.zaken.verkiezing.page1.Page1VerkiezingBean.*;

import java.util.List;
import java.util.function.Consumer;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.beheer.verkiezing.Stempas;
import nl.procura.gba.web.services.beheer.verkiezing.Verkiezing;

public class Page1VerkiezingForm2 extends GbaForm<Page1VerkiezingBean> {

  private Stempas           stempas;
  private Consumer<Stempas> stempasListener;

  public Page1VerkiezingForm2() {
    setCaption("Stempassen");
    setOrder(F_STEMPAS, F_TOEGEVOEGD, F_AAND);
    setColumnWidths("200px", "");
    setReadonlyAsText(true);
  }

  public void setStempasListener(Consumer<Stempas> stempasListener) {
    this.stempasListener = stempasListener;
  }

  public Stempas getStempas() {
    return stempas;
  }

  @Override
  public void afterSetBean() {
    getField(F_STEMPAS).addListener((ValueChangeListener) event -> {
      stempas = (Stempas) event.getProperty().getValue();
      stempasListener.accept(stempas);
      if (stempas != null && stempas.isOpgeslagen()) {
        getField(F_TOEGEVOEGD).setValue(stempas.isToegevoegd() ? "Ja" : "Nee");
        getField(F_AAND).setValue(stempas.getAanduidingOmschrijving());
      } else {
        getField(F_TOEGEVOEGD).setValue("N.v.t.");
        getField(F_AAND).setValue("N.v.t.");
      }
      repaint();
    });

    super.afterSetBean();
  }

  public void setStempassen(List<Stempas> stempassen) {
    setBean(new Page1VerkiezingBean());
    StempasContainer container = new StempasContainer(stempassen);
    getField(F_STEMPAS, GbaNativeSelect.class).setContainerDataSource(container);
    repaint();
  }

  public Verkiezing getVerkiezing() {
    return (Verkiezing) getField(F_VERKIEZING).getValue();
  }
}
