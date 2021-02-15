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

package nl.procura.gba.web.modules.persoonslijst.overig.header;

import static nl.procura.gba.web.modules.persoonslijst.overig.header.HeaderBean.*;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ACCESS_RISK_PROFILE_SIGNALS;
import static nl.procura.standard.Globalfunctions.pos;

import com.vaadin.ui.Field;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.modules.persoonslijst.overig.PlForm;
import nl.procura.gba.web.modules.persoonslijst.overig.header.buttons.FavButton;
import nl.procura.gba.web.modules.persoonslijst.overig.header.buttons.MarkButton;
import nl.procura.gba.web.modules.persoonslijst.overig.header.buttons.ReloadButton;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisService;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.standard.Globalfunctions;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class HeaderForm extends PlForm {

  public HeaderForm() {

    setOrder(NAAM, HeaderBean.BSN, ADRES, BURGSTAAT, ANR, STATUS, GEBOORTEDATUM, GESLACHT);
    setColumnWidths("50px", "", "100px", "100px", "100px", "100px");
    addStyleName("headerform");
  }

  @Override
  public void afterSetColumn(Column column, Field field, Property property) {

    super.afterSetColumn(column, field, property);

    if (property.is(NAAM)) {
      column.addComponent(new ReloadButton(getServices()));
      MarkButton markButton = new MarkButton(getServices());
      if (isMarkButtonEnabled()) {
        column.addComponent(markButton);
      }
      column.addComponent(new FavButton(getServices()));
    }

    if (property.is(ADRES)) {
      if (getBean().getAdresIndicatie() != null && pos(getBean().getAdresIndicatie().getValue())) {
        column.addComponent(new AdresIndicatieLabel(getBean().getAdresIndicatie()));
      }
    }
  }

  @Override
  public HeaderBean getBean() {
    return (HeaderBean) super.getBean();
  }

  public GbaApplication getGbaApplication() {
    return super.getApplication();
  }

  @Override
  public Object getNewBean() {
    return new HeaderBean();
  }

  public Services getServices() {
    GbaApplication app = getApplication();
    return app.getServices();
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {

    if (property.is(NAAM)) {
      column.setColspan(3);
    }

    if (property.is(STATUS)) {
      field.addStyleName(GbaWebTheme.TEXT.RED);
    }

    super.setColumn(column, field, property);
  }

  private boolean isMarkButtonEnabled() {
    RiskAnalysisService service = getServices().getRiskAnalysisService();
    return Globalfunctions.isTru(service.getParm(ACCESS_RISK_PROFILE_SIGNALS));
  }
}
