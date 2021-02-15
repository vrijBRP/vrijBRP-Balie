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

package nl.procura.gba.web.services.gba.basistabellen.huwelijkslocatieOptie;

import static nl.procura.standard.Globalfunctions.pos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;

import nl.procura.gba.jpa.personen.db.HuwLocatieOptie;
import nl.procura.gba.web.services.gba.basistabellen.huwelijkslocatie.HuwelijksLocatieOptieType;

public class HuwelijksLocatieOptie extends HuwLocatieOptie implements Comparable<HuwelijksLocatieOptie> {

  private static final long serialVersionUID = -3687697344992648069L;

  public HuwelijksLocatieOptie() {
    setHuwelijksLocatieOptie("");
    setType("");
    setVerplichteOptie(false);
  }

  @Override
  public int compareTo(HuwelijksLocatieOptie o) {
    return new CompareToBuilder().append(getVnr(), o.getVnr()).append(getHuwelijksLocatieOptie(),
        o.getHuwelijksLocatieOptie()).build();
  }

  public List<String> getAliassen() {
    List<String> aliassen = new ArrayList<>();
    for (String alias : StringUtils.defaultIfBlank(getAlias(), "").split(",")) {
      if (StringUtils.isNotBlank(alias)) {
        aliassen.add(alias.trim());
      }
    }

    return aliassen;
  }

  public void setAliassen(List<String> aliassen) {
    setAlias(StringUtils.join(aliassen, ","));
  }

  public Long getCodeHuwelijksLocatieOptie() {
    return getCHuwLocatieOptie();
  }

  public void setCodeHuwelijksLocatieOptie(long code) {
    setCHuwLocatieOptie(code);
  }

  public String getHuwelijksLocatieOptie() {
    return getHuwLocatieOptie();
  }

  public void setHuwelijksLocatieOptie(String huwelijksLocatieOptie) {
    setHuwLocatieOptie(huwelijksLocatieOptie);
  }

  public String getHuwelijksLocatieOptieOms() {

    StringBuilder sb = new StringBuilder(getHuwelijksLocatieOptie());
    if (getOptieType() == HuwelijksLocatieOptieType.NUMBER) {
      sb.append(" (").append(getMin()).append(" - ").append(getMax()).append(")");
    }

    return sb.toString();
  }

  public HuwelijksLocatieOptieType getOptieType() {
    return HuwelijksLocatieOptieType.get(getType());
  }

  public void setOptieType(HuwelijksLocatieOptieType type) {
    setType(type.getCode());
  }

  public boolean isVerplichteOptie() {
    return pos(getVerplicht());
  }

  public void setVerplichteOptie(boolean verplicht) {
    setVerplicht(BigDecimal.valueOf(verplicht ? 1 : 0));
  }
}
