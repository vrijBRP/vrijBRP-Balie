/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.zaken.reisdocument.page26;

import static nl.procura.gba.web.modules.zaken.reisdocument.page26.Page26ReisdocumentBean1.ADRES;
import static nl.procura.gba.web.modules.zaken.reisdocument.page26.Page26ReisdocumentBean1.BEZORGING_GEWENST;
import static nl.procura.gba.web.modules.zaken.reisdocument.page26.Page26ReisdocumentBean1.BUNDELING;
import static nl.procura.gba.web.modules.zaken.reisdocument.page26.Page26ReisdocumentBean1.OPMERKINGEN;

import java.util.function.Consumer;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.Bezorging;
import nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.ReisdocumentBezorgingService;
import nl.procura.vaadin.component.container.ArrayListContainer;
import nl.procura.vaadin.component.field.ProNativeSelect;

public class Page26ReisdocumentForm1 extends GbaForm<Page26ReisdocumentBean1> {

  private final Bezorging                    bezorging;
  private final ReisdocumentBezorgingService service;
  private final Consumer<Boolean>            bezorgingListener;

  public Page26ReisdocumentForm1(
      Page26ReisdocumentBean1 bean,
      Bezorging bezorging,
      ReisdocumentBezorgingService service,
      Consumer<Boolean> bezorgingListener) {
    this.bezorging = bezorging;
    this.service = service;
    this.bezorgingListener = bezorgingListener;
    setOrder(BEZORGING_GEWENST, OPMERKINGEN, BUNDELING, ADRES);
    setColumnWidths("150px", "");
    setReadonlyAsText(false);
    setBean(bean);
  }

  @Override
  public void afterSetBean() {
    super.afterSetBean();
    ProNativeSelect bezorgingGewenstField = getField(BEZORGING_GEWENST, ProNativeSelect.class);
    bezorgingGewenstField.addListener((ValueChangeListener) event -> {
      boolean bezorgingGewenst = (boolean) event.getProperty().getValue();
      getField(BUNDELING).setVisible(bezorgingGewenst);
      bezorgingListener.accept(bezorgingGewenst);
      repaint();
    });

    ProNativeSelect bundelingField = getField(BUNDELING, ProNativeSelect.class);
    BundelingContainer ds = new BundelingContainer(service);
    bundelingField.setContainerDataSource(ds);
    bundelingField.setVisible(service.isBundelingMogelijk());
    bundelingField.setValue(ds.getItemIds().stream().findFirst().orElse(null));

    getField(BUNDELING).setVisible(bezorging.getMelding().isBezorgingGewenst());
    bezorgingListener.accept(bezorging.getMelding().isBezorgingGewenst());
    bundelingField.setValue(new BezorgingBundeling(bezorging, bezorging.getMelding().getBundelRefNr()));
  }

  private class BundelingContainer extends ArrayListContainer {

    public BundelingContainer(ReisdocumentBezorgingService service) {
      service.findOtherByAddress(bezorging).forEach(hoofdBez -> {
        addItem(new BezorgingBundeling(hoofdBez, hoofdBez.getMelding().getAanvrNr()));
      });
      addItem(new BezorgingBundeling(null, bezorging.getMelding().getAanvrNr()));
    }
  }
}
