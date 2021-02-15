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

package nl.procura.gba.web.services.bs.algemeen.persoon;

import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.gba.functies.Geslacht;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DossierPersoonFilter {

  private int                  index  = 0;
  private Geslacht             gender = null;
  private DossierPersoonType[] types  = null;

  public static DossierPersoonFilter filter(DossierPersoonType... types) {
    return new DossierPersoonFilter()
        .setIndex(0)
        .setGender(null)
        .setTypes(types);
  }

  public static DossierPersoonFilter filter(int index, DossierPersoonType... types) {
    return new DossierPersoonFilter()
        .setIndex(index)
        .setGender(null)
        .setTypes(types);
  }

  public static DossierPersoonFilter filter(int index, Geslacht gender, DossierPersoonType... types) {
    return new DossierPersoonFilter()
        .setIndex(index)
        .setGender(gender)
        .setTypes(types);
  }
}
