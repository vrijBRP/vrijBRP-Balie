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

package nl.procura.diensten.gba.ple.procura.templates;

import static nl.procura.burgerzaken.gba.core.enums.GBAElem.*;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.diensten.gba.ple.procura.utils.SortableObject;
import nl.procura.gba.jpa.probev.db.AbstractKiesr;

public class Cat13KiesrTemplate extends PLETemplateProcura<AbstractKiesr> {

  @Override
  public void parse(SortableObject<AbstractKiesr> so) {

    addCat(GBACat.KIESR, so);

    AbstractKiesr kiesr = so.getObject();
    addElem(AANDUIDING_EURO_KIESR, kiesr.getEukiesr());
    addElem(DATUM_VERZ_OF_MED_EURO_KIESR, kiesr.getDEukiesr());
    addElem(EINDDATUM_UITSL_EURO_KIESR, kiesr.getDEukiesrEnd());

    addElem(AAND_UITGESLOTEN_KIESR, kiesr.getKiesrecht());
    addElem(EINDDATUM_UITSLUIT_KIESR, kiesr.getDKiesrEnd());

    addElem(GEMEENTE_DOC, kiesr.getGOntl());
    addElem(DATUM_DOC, kiesr.getDOntl());
    addElem(BESCHRIJVING_DOC, kiesr.getDocOntl());

    addElem(INGANGSDAT_GELDIG, kiesr.getId().getDGeld());
    addElem(VOLGCODE_GELDIG, kiesr.getId().getVGeld());
  }
}
