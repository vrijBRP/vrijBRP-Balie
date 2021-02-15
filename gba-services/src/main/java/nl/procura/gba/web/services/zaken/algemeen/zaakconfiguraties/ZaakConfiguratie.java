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

package nl.procura.gba.web.services.zaken.algemeen.zaakconfiguraties;

import static nl.procura.gba.common.ZaakType.ONBEKEND;
import static nl.procura.standard.Globalfunctions.along;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.db.Profile;
import nl.procura.gba.jpa.personen.db.ZaakConf;
import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.beheer.profiel.KoppelbaarAanProfiel;
import nl.procura.gba.web.services.beheer.profiel.Profiel;
import nl.procura.java.reflection.ReflectionUtil;

public class ZaakConfiguratie extends ZaakConf implements KoppelbaarAanProfiel {

  public ZaakConfiguratie() {
    setZaakConf("");
    setBron("");
    setLeverancier("");
  }

  @Override
  public boolean isGekoppeld(Profiel profiel) {
    return MiscUtils.contains(profiel, getProfiles());
  }

  @Override
  public void koppelActie(Profiel profiel, KoppelActie koppelActie) {
    if (KoppelActie.KOPPEL == koppelActie) {
      getProfiles().add(ReflectionUtil.deepCopyBean(Profile.class, profiel));
    } else {
      getProfiles().remove(profiel);
    }
  }

  public List<ZaakType> getZaakTypesAsList() {
    return Arrays.stream(Objects.toString(getZaakTypes(), "").split(","))
        .map(val -> ZaakType.get(along(val)))
        .filter(zt -> !zt.is(ONBEKEND))
        .collect(Collectors.toList());
  }

  public void setZaakTypes(List<ZaakType> zaakTypes) {
    super.setZaakTypes(zaakTypes.stream()
        .map(fv -> String.valueOf(fv.getCode()))
        .collect(Collectors.joining(",")));
  }

  public boolean isZaakType(ZaakType zaakType) {
    return getZaakTypesAsList().contains(zaakType);
  }
}
