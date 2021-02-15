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

package nl.procura.gba.web.services.zaken.algemeen.commentaar;

public class ZaakCommentaar implements Comparable<ZaakCommentaar> {

  private ZaakCommentaarType type;
  private String             tekst;

  public ZaakCommentaar(ZaakCommentaarType type, String tekst) {
    this.type = type;
    this.tekst = tekst;
  }

  @Override
  public int compareTo(ZaakCommentaar o) {
    return o.getType().getVnr() > getType().getVnr() ? 1 : -1;
  }

  public String getTekst() {
    return tekst;
  }

  public void setTekst(String tekst) {
    this.tekst = tekst;
  }

  public ZaakCommentaarType getType() {
    return type;
  }

  public void setType(ZaakCommentaarType type) {
    this.type = type;
  }
}
