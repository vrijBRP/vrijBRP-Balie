/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.modules.bs.naturalisatie.page40;

import static nl.procura.standard.Globalfunctions.astr;

import java.util.LinkedHashMap;
import java.util.Map;

import com.vaadin.ui.Component;

import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.modules.bs.naturalisatie.valuechoice.ValueChoiceComponent;

import lombok.Data;
import lombok.RequiredArgsConstructor;

public class GeslVoornComponent implements ValueChoiceComponent<GeslVoornComponent.GeslVoornComponentValue> {

  private final GbaTextField           gesl   = new GbaTextField();
  private final GbaTextField           voorn  = new GbaTextField();
  private final Map<String, Component> fields = new LinkedHashMap<>();

  public GeslVoornComponent(String geslCaption, String voornCaption) {
    gesl.setInputPrompt("Geslachtsnaam");
    gesl.setWidth("180px");

    voorn.setInputPrompt("Voornamen");
    voorn.setWidth("180px");

    fields.put(geslCaption, gesl);
    fields.put(voornCaption, voorn);
  }

  @Override
  public Map<String, Component> getFields() {
    return fields;
  }

  @Override
  public void setValue(GeslVoornComponentValue value) {
    gesl.setValue(value.getGesl());
    voorn.setValue(value.getVoorn());
  }

  @Override
  public GeslVoornComponentValue getValue() {
    return new GeslVoornComponentValue((String) gesl.getValue(), (String) voorn.getValue());
  }

  public static GeslVoornComponentValue value(String gesl, String voorn) {
    return new GeslVoornComponentValue(gesl, voorn);
  }

  @RequiredArgsConstructor
  @Data
  public static class GeslVoornComponentValue {

    private final String gesl;
    private final String voorn;

    public String toString() {
      return astr(gesl) + astr(voorn);
    }
  }

}
