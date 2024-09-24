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

package nl.procura.gba.web.modules.zaken.personmutations.overview;

import static java.util.Optional.ofNullable;

import nl.procura.gba.jpa.personen.db.PlMut;
import nl.procura.gba.web.components.layouts.form.GbaForm;

public class PersonMutationOverviewForm extends GbaForm<PersonMutationOverviewBean> {

  public PersonMutationOverviewForm(PlMut mutation, String... fields) {
    setColumnWidths("100px", "400px", "100px", "");
    setOrder(fields);

    PersonMutationOverviewBean bean = new PersonMutationOverviewBean();
    String explanation = mutation.getExplanation();

    if (explanation != null) {
      bean.setExplanation(explanation.replaceAll("\n", "</br>"));
    }

    bean.setProcessRelations(ofNullable(mutation.getProcessRelations())
        .map(value -> value ? "Ja" : "Nee")
        .orElse("Niet van toepassing"));
    bean.setCategory(setFieldHeight(mutation.getDescrCat()));
    bean.setOperation(setFieldHeight(mutation.getDescrAction()));
    bean.setSet(setFieldHeight(mutation.getDescrSet()));
    bean.setRecord(setFieldHeight(mutation.getDescrRec()));

    setBean(bean);
  }

  /**
   * Hack to make sure fields in readonly forms and normal forms are the same height.
   * That way the user has less of a transition
   */
  private Object setFieldHeight(String descr) {
    return "<span style='line-height: 26px'>" + descr + "</span>";
  }
}
