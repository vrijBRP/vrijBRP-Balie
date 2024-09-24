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

import static nl.procura.burgerzaken.gba.core.enums.GBAElem.AANDUIDING_EURO_KIESR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.AAND_UITGESLOTEN_KIESR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.ADRES_EU_LIDSTAAT_VAN_HERKOMST;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.BESCHRIJVING_DOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_DOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_VERZ_OF_MED_EURO_KIESR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.EINDDATUM_UITSLUIT_KIESR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.EINDDATUM_UITSL_EURO_KIESR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GEMEENTE_DOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.INGANGSDAT_GELDIG;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.LAND_EU_LIDSTAAT_VAN_HERKOMST;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.PLAATS_EU_LIDSTAAT_VAN_HERKOMST;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.VOLGCODE_GELDIG;

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

    addElem(ADRES_EU_LIDSTAAT_VAN_HERKOMST, kiesr.getAdrHerk());
    addElem(PLAATS_EU_LIDSTAAT_VAN_HERKOMST, kiesr.getPHerk());
    addElem(LAND_EU_LIDSTAAT_VAN_HERKOMST, kiesr.getLHerk());

    addElem(GEMEENTE_DOC, kiesr.getGOntl());
    addElem(DATUM_DOC, kiesr.getDOntl());
    addElem(BESCHRIJVING_DOC, kiesr.getDocOntl());

    addElem(INGANGSDAT_GELDIG, kiesr.getId().getDGeld());
    addElem(VOLGCODE_GELDIG, kiesr.getId().getVGeld());
  }
}
