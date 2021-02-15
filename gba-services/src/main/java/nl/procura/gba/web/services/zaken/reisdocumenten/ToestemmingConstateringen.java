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

package nl.procura.gba.web.services.zaken.reisdocumenten;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class ToestemmingConstateringen {

  private List<String>    persoon   = new ArrayList<>();
  private List<String>    aanvrager = new ArrayList<>();
  private ToestemmingType type;

  public List<String> getPersoon() {
    return persoon;
  }

  public void setPersoon(List<String> persoon) {
    this.persoon = persoon;
  }

  public List<String> getAanvrager() {
    return aanvrager;
  }

  public void setAanvrager(List<String> aanvrager) {
    this.aanvrager = aanvrager;
  }

  public String getPersoonAsString() {
    return toString(type.getOms(), persoon);
  }

  public String getAanvragerAsString() {
    return toString("Aanvrager", aanvrager);
  }

  private String toString(String name, List<String> list) {
    StringBuilder out = new StringBuilder();
    if (!list.isEmpty()) {
      out.append(name);
      out.append(" ");
      for (int i = 1; i <= list.size(); i++) {
        if (i > 1) {
          out.append(i == list.size() ? " en " : ", ");
        }
        out.append(list.get(i - 1));
      }
    }
    return out.toString();
  }

  public ToestemmingType getType() {
    return type;
  }

  public void setType(ToestemmingType type) {
    this.type = type;
  }

  public void clear() {
    persoon.clear();
    aanvrager.clear();
  }

  public String getString() {
    StringBuilder out = new StringBuilder();
    out.append(getPersoonAsString());
    if (!persoon.isEmpty()) {
      out.append("</br>");
    }
    out.append(getAanvragerAsString());
    return StringUtils.capitalize(out.toString());
  }
}
