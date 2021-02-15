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

package nl.procura.gba.web.services.bs.algemeen.interfaces;

import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public interface DossierNaamgebruik extends DossierPartners {

  String getNaamGebruikPartner1();

  void setNaamGebruikPartner1(String naamgebruik);

  String getNaamGebruikPartner2();

  void setNaamGebruikPartner2(String naamgebruik);

  String getNaamPartner1();

  void setNaamPartner1(String naam);

  String getNaamPartner2();

  void setNaamPartner2(String naam);

  FieldValue getTitelPartner1();

  void setTitelPartner1(FieldValue titel);

  FieldValue getTitelPartner2();

  void setTitelPartner2(FieldValue titel);

  String getVoorvPartner1();

  void setVoorvPartner1(String voorv);

  String getVoorvPartner2();

  void setVoorvPartner2(String voorv);

  boolean isAdelPartner1();

  boolean isAdelPartner2();

  boolean isPredikaatPartner1();

  boolean isPredikaatPartner2();
}
