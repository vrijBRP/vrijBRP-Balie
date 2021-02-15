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

package nl.procura.gba.web.services.beheer.profiel.gba_categorie;

import static nl.procura.standard.Globalfunctions.toBigDecimal;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.gba.jpa.personen.db.GbaCategory;
import nl.procura.gba.jpa.personen.db.Profile;
import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.beheer.profiel.KoppelbaarAanProfiel;
import nl.procura.gba.web.services.beheer.profiel.Profiel;
import nl.procura.java.reflection.ReflectionUtil;

public class PleCategorie extends GbaCategory implements KoppelbaarAanProfiel {

  private static final long serialVersionUID = 2764120706237201781L;

  public PleCategorie() {
  }

  public PleCategorie(GBACat gbaCategorie) {
    setCategory(toBigDecimal(gbaCategorie.getCode()));
  }

  @Override
  public boolean isGekoppeld(Profiel profiel) {
    return getProfiles().contains(profiel);
  }

  @Override
  public void koppelActie(Profiel profiel, KoppelActie koppelActie) {
    if (KoppelActie.KOPPEL == koppelActie) {
      getProfiles().add(ReflectionUtil.deepCopyBean(Profile.class, profiel));
    } else {
      getProfiles().remove(profiel);
    }
  }
}
