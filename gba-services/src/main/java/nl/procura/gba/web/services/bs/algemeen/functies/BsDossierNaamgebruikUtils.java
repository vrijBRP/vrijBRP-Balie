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

package nl.procura.gba.web.services.bs.algemeen.functies;

import static nl.procura.standard.Globalfunctions.fil;

import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class BsDossierNaamgebruikUtils {

  /**
   * Is de fieldvalue een adelijke titel (geen jonkheer/jonkvrouw)
   */
  public static boolean isAdel(FieldValue fv) {
    String value = FieldValue.from(fv).getStringValue().toLowerCase();
    return fil(value) && !value.startsWith("j"); // Jonkheer/jonkvouw
  }

  /**
   * Is de fieldvalue een predikaat (jonkheer/jonkvrouw)
   */
  public static boolean isPredikaat(FieldValue fv) {
    return FieldValue.from(fv).getStringValue().toLowerCase().startsWith("j"); // Jonkheer/jonkvouw
  }

  public static String getNameWithAge(DossierPersoon person) {
    int age = person.getLeeftijd();
    StringBuilder out = new StringBuilder();
    out.append(person.getNaam().getNaam_naamgebruik_eerste_voornaam());
    if (person.getDGeb().longValue() >= 0) {
      out.append(" (");
      out.append(person.getGeboorte().getGeboortedatum());
      out.append(")");
    }
    return out.toString();
  }

  public static String getNormalizedName(DossierPersoon person) {
    return person.getNaam().getPred_eerstevoorn_adel_voorv_gesl();
  }

  public static String getNormalizedNameWithAge(DossierPersoon person) {
    int age = person.getLeeftijd();
    StringBuilder out = new StringBuilder();
    out.append(person.getNaam().getPred_eerstevoorn_adel_voorv_gesl());
    if (person.getDGeb().longValue() >= 0) {
      out.append(" (");
      out.append(age);
      out.append(" jr)");
    }
    return out.toString();
  }
}
