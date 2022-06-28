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

import static nl.procura.gba.web.modules.zaken.verkiezing.page1.Page1VerkiezingBean.F_VERKIEZING;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.beheer.verkiezing.Verkiezing;
import org.jetbrains.annotations.NotNull;

public class Page1VerkiezingForm1 extends GbaForm<Page1VerkiezingBean> {

  private Consumer<Verkiezing> verkiezingListener;

  public Page1VerkiezingForm1() {
    setCaption("Kiezersregister");
    setOrder(F_VERKIEZING);
    setColumnWidths("200px", "");
    setReadonlyAsText(true);
  }

  public void setVerkiezingListener(Consumer<Verkiezing> verkiezingListener) {
    this.verkiezingListener = verkiezingListener;
  }

  public void setVerkiezingen(List<Verkiezing> verkiezingen) {
    Verkiezing verkiezing = getBean() != null ? getVerkiezing() : null;
    setBean(new Page1VerkiezingBean());
    VerkiezingContainer container = new VerkiezingContainer(verkiezingen);
    GbaNativeSelect field = getField(F_VERKIEZING, GbaNativeSelect.class);
    field.setContainerDataSource(container);
    repaint();
    setVerkiezing(verkiezingen, verkiezing).ifPresent(field::setValue);
  }

  @NotNull
  private Optional<Verkiezing> setVerkiezing(List<Verkiezing> verkiezingen, Verkiezing verkiezing) {
    return verkiezingen.stream().filter(v -> v.equals(verkiezing)).findFirst();
  }

  @Override
  public void afterSetBean() {
    getField(F_VERKIEZING).addListener((ValueChangeListener) event -> {
      Verkiezing verkiezing = (Verkiezing) event.getProperty().getValue();
      verkiezingListener.accept(verkiezing);
      repaint();
    });

    super.afterSetBean();
  }

  public Verkiezing getVerkiezing() {
    return (Verkiezing) getField(F_VERKIEZING).getValue();
  }
}
