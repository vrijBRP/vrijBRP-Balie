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

package nl.procura.gba.web.modules.beheer.logbestanden.page2;

import java.util.ArrayList;
import java.util.List;

public class Word {

  private List<Letter> letters = new ArrayList<>();

  public Word(Letter l) {
    getLetters().add(l);
  }

  public int getAantal() {
    return getLetters().get(0).getAantal();
  }

  public int getLastIndex() {
    return getLetters().get(getLetters().size() - 1).getIndex();
  }

  public List<Letter> getLetters() {
    return letters;
  }

  public void setLetters(List<Letter> letters) {
    this.letters = letters;
  }

  public int getStartIndex() {
    return getLetters().get(0).getIndex();
  }

  public String getWord() {

    StringBuilder sb = new StringBuilder();

    for (Letter l : getLetters()) {
      sb.append(l.getLetter());
    }

    return sb.toString();
  }

  /**
   * Is deze letter onderdeel van een woord?
   */
  public boolean isNaast(Letter l) {

    for (Letter l2 : getLetters()) {

      int diff = l2.getIndex() - l.getIndex();

      if (diff == 1 && l.getAantal() == l2.getAantal()) {

        return true;
      }
    }

    return false;
  }

  @Override
  public String toString() {
    return getWord() + ", s: " + getStartIndex() + ", e: " + getLastIndex() + ", a: " + getAantal();
  }
}
