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

package nl.procura.gba.web.modules.beheer.overzicht.page1;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;

import nl.procura.gba.web.common.annotations.ModuleAnnotation;
import nl.procura.gba.web.components.layouts.GbaHorizontalLayout;
import nl.procura.gba.web.components.layouts.ModuleTemplate;

@SuppressWarnings("unchecked")
public abstract class ZakenIcon extends GbaHorizontalLayout implements LayoutClickListener {

  private ModuleAnnotation annotation = null;

  public ZakenIcon(Class<? extends ModuleTemplate> object, boolean load) {
    this(object, null, load);
  }

  public ZakenIcon(Class<? extends ModuleTemplate> clazz, String caption, boolean load) {

    setWidth("99%");
    addStyleName("zaken_icon");
    setMargin(false, true, false, true);

    if (clazz.isAnnotationPresent(ModuleAnnotation.class)) {
      ModuleAnnotation ma = clazz.getAnnotation(ModuleAnnotation.class);
      setAnnotation(ma);

      Label label = new Label(caption == null ? ma.caption() : caption);
      addComponent(label);
      setExpandRatio(label, 1f);

      if (load) {

        final Label countLabel = new Label("", Label.CONTENT_XHTML);
        countLabel.setValue(getCount());
        countLabel.setSizeUndefined();
        addComponent(countLabel);
        setComponentAlignment(countLabel, Alignment.MIDDLE_RIGHT);
      }
    }

    addListener(this);
  }

  public ModuleAnnotation getAnnotation() {
    return annotation;
  }

  public void setAnnotation(ModuleAnnotation annotation) {
    this.annotation = annotation;
  }

  public abstract String getCount();

  public abstract boolean isAllowed();

  @Override
  public void layoutClick(LayoutClickEvent event) {

    if (getAnnotation() != null) {
      getWindow().open(new ExternalResource(
          getApplication().getExternalURL(getWindow().getName() + getAnnotation().url())));
    }
  }
}
