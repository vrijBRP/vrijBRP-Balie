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

import java.util.List;

import nl.procura.gba.common.UniqueList;
import nl.procura.gba.jpa.personen.db.DossNatEntity;
import nl.procura.gba.jpa.personen.db.DossNatio;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteit;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteiten;
import nl.procura.java.reflection.ReflectionUtil;

public class BsNatioHandler {

  private final List<DossierNationaliteit> nationaliteiten = new UniqueList<>();

  public List<DossierNationaliteit> getNationaliteiten() {
    return nationaliteiten;
  }

  public boolean isNationaliteit(DossierNationaliteit nationaliteit) {
    for (DossierNationaliteit bestaandeNationaliteit : getNationaliteiten()) {
      if (bestaandeNationaliteit.isNationaliteit(nationaliteit)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Voeg nationaliteit toe
   */
  public DossierNationaliteit toevoegenNationaliteit(DossierNationaliteiten dossier,
      DossierNationaliteit nationaliteit) {
    nationaliteit.koppelenAan(dossier);
    nationaliteiten.add(nationaliteit);
    return nationaliteit;
  }

  /**
   * Verwijder bepaalde nationaliteit van een DossNatEntity
   */
  public void verwijderNationaliteit(DossNatEntity dossNat, DossierNationaliteit nationaliteit) {
    dossNat.getDossNats().remove(ReflectionUtil.deepCopyBean(DossNatio.class, nationaliteit));
    getNationaliteiten().remove(nationaliteit);
  }
}
