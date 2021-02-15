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

package nl.procura.gbaws.web.vaadin.module.ws.page2;

import static nl.procura.gbaws.web.vaadin.module.ws.page2.Page2WsBean.*;

import nl.procura.gbaws.web.vaadin.layouts.forms.DefaultForm;
import nl.procura.gbaws.web.vaadin.module.ws.page1.Page1WsTable.Webservice;

public class Page2WsForm extends DefaultForm {

  public Page2WsForm(Webservice ws) {

    setCaption("Webservice");
    setColumnWidths("130px", "");
    setOrder(NAME, DESCRIPTION, ENDPOINT, WSDL);

    Page2WsBean bean = new Page2WsBean();

    bean.setName(ws.getName());
    bean.setDescription(ws.getDescription());
    bean.setEndpoint(ws.getUrl());

    StringBuilder wsdl = new StringBuilder();
    wsdl.append("<a href=\"");
    wsdl.append(ws.getWsdlUrl());
    wsdl.append("\" target=\"_blank\">");
    wsdl.append(ws.getWsdlUrl());
    wsdl.append("</a>");

    bean.setWsdl(wsdl.toString());

    setBean(bean);
  }

  @Override
  public Page2WsBean getBean() {
    return (Page2WsBean) super.getBean();
  }
}
