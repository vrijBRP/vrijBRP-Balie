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

package nl.procura.gba.web.modules.zaken.reisdocument.page24;

import static nl.procura.gba.web.modules.zaken.reisdocument.page24.Page24ReisdocumentBean1.AFLEVERING;
import static nl.procura.gba.web.modules.zaken.reisdocument.page24.Page24ReisdocumentBean1.AFSLUITING;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.zaken.reisdocumenten.LeveringType;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentAanvraag;
import nl.procura.gba.web.services.zaken.reisdocumenten.SluitingType;
import nl.procura.vaadin.component.field.ProNativeSelect;

public class Page24ReisdocumentForm1 extends GbaForm<Page24ReisdocumentBean1> {

  public Page24ReisdocumentForm1(ReisdocumentAanvraag a) {

    setOrder(AFLEVERING, AFSLUITING);
    setColumnWidths(WIDTH_130, "");

    Page24ReisdocumentBean1 bean = new Page24ReisdocumentBean1();

    if (a.getReisdocumentStatus().getStatusLevering().getCode() > LeveringType.ONBEKEND.getCode()) {
      bean.setAflevering(a.getReisdocumentStatus().getStatusLevering());
    }

    if (a.getReisdocumentStatus().getStatusAfsluiting().getCode() > SluitingType.AANVRAAG_NIET_AFGESLOTEN.getCode()) {
      bean.setAfsluiting(a.getReisdocumentStatus().getStatusAfsluiting());
    }

    setBean(bean);
  }

  @Override
  public void afterSetBean() {

    ProNativeSelect sluitingField = getField(AFSLUITING, ProNativeSelect.class);
    ProNativeSelect leveringField = getField(AFLEVERING, ProNativeSelect.class);
    setSluitingField(sluitingField, getBean().getAflevering());

    leveringField.addListener((ValueChangeListener) event -> {
      setSluitingField(sluitingField, (LeveringType) leveringField.getValue());
    });

    super.afterSetBean();
  }

  private void setSluitingField(ProNativeSelect sluitingField, LeveringType leveringType) {
    if (LeveringType.DOCUMENT_GOED.equals(leveringType)) {
      sluitingField.setContainerDataSource(SluitingContainer.getAll());
      sluitingField.setValue(SluitingType.DOCUMENT_UITGEREIKT);
    } else {
      sluitingField.setContainerDataSource(SluitingContainer.getError());
      sluitingField.setValue(SluitingType.DOCUMENT_NIET_UITGEREIKT_ONJUIST);
    }
  }
}
