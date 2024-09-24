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

package nl.procura.gba.web.components.layouts.footer;

import static nl.procura.gba.common.MiscUtils.getBuildText;
import static nl.procura.gba.common.MiscUtils.getCopyright;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

import nl.procura.vaadin.theme.twee.ProcuraTheme;

public class GbaWebNormalFooterLayout extends GbaWebFooterTemplate {

  public GbaWebNormalFooterLayout() {
    setMargin(false);
    setStyleName(ProcuraTheme.LAYOUT_FIXED_FOOTER);
  }

  @Override
  public void attach() {

    HorizontalLayout h = new HorizontalLayout();
    h.setWidth("100%");

    Label copyrightLabel = new Label(getCopyright(), Label.CONTENT_XHTML);
    copyrightLabel.addStyleName(ProcuraTheme.LAYOUT_LEFT);
    copyrightLabel.setSizeUndefined();

    Label buildLabel = new Label(getBuildText(), Label.CONTENT_XHTML);
    buildLabel.addStyleName(ProcuraTheme.LAYOUT_RIGHT);
    buildLabel.setSizeUndefined();

    h.addComponent(copyrightLabel);
    h.addComponent(buildLabel);
    h.setComponentAlignment(copyrightLabel, Alignment.MIDDLE_LEFT);
    h.setComponentAlignment(buildLabel, Alignment.MIDDLE_RIGHT);
    addComponent(h);

    super.attach();
  }
}
