/*
 * Copyright 2023 - 2024 Procura B.V.
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

package nl.procura.gba.web.modules.bs.onderzoek.aanschrijving;

import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ONDERZ_AANSCHR_TEKST;
import static nl.procura.standard.Globalfunctions.astr;

import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.onderzoek.enums.AanschrijvingFaseType;
import nl.procura.vaadin.component.field.ProTextArea;
import nl.procura.vaadin.component.layout.Fieldset;

public class MotiveringLayout extends VerticalLayout {

  private final Services        services;
  private AanschrijvingFaseType type;
  private ProTextArea           msgArea;

  public MotiveringLayout(Services services) {
    this.services = services;
  }

  public void load(AanschrijvingFaseType type) {
    this.type = type;

    msgArea = new ProTextArea();
    msgArea.setValue(services.getParameterService().getSysteemParameter(getParm()).getValue());
    msgArea.setNullRepresentation("");
    msgArea.setInputPrompt("Geef eventueel een aanvullende motivering in voor bepaalde documenten");
    msgArea.setRows(7);
    msgArea.setWidth("100%");
    msgArea.setHeight("100%");

    Fieldset fieldset = new Fieldset("Motivering (aanvullend) - " + type.getOms(), msgArea);
    fieldset.setWidth("100%");
    fieldset.setHeight("100%");

    removeAllComponents();
    addComponent(fieldset);
  }

  public String getMessage() {
    return astr(msgArea.getValue());
  }

  public void save() {
    if (type != null) {
      services.getParameterService().saveParameter(getParm(), astr(msgArea.getValue()), 0, 0);
    }
  }

  private String getParm() {
    return ONDERZ_AANSCHR_TEKST.getKey() + "_" + type.getOms();
  }
}
