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

import static nl.procura.standard.Globalfunctions.fil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ZaakCommentaren {

  private List<ZaakCommentaar> commentaren = new ArrayList<>();
  private CommentaarZaak       zaak        = null;

  public ZaakCommentaren() {
  }

  public ZaakCommentaren(CommentaarZaak zaak) {
    this.zaak = zaak;

    if (zaak.getCommentaar() != null) {
      for (String regel : zaak.getCommentaar().split("\n")) {
        if (fil(regel)) {
          String[] e = regel.trim().split("\\|");
          if (e.length == 1) {
            commentaren.add(new ZaakCommentaar(ZaakCommentaarType.get(e[0]), ""));
          } else if (e.length == 2) {
            commentaren.add(new ZaakCommentaar(ZaakCommentaarType.get(e[0]), e[1]));
          }
        }
      }
    }
  }

  public boolean exists() {
    return getCommentaar().getType().getVnr() > 0;
  }

  public ZaakCommentaar getCommentaar() {

    for (ZaakCommentaar c : getCommentaren()) {
      if (c.getType().getVnr() > 0) {
        return c;
      }
    }

    return new ZaakCommentaar(ZaakCommentaarType.ONBEKEND, "");
  }

  public String getCommentaarTekst() {

    StringBuilder sb = new StringBuilder();

    for (ZaakCommentaar c : commentaren) {

      sb.append(c.getType().getCode() + "|" + c.getTekst() + "\n");
    }

    return sb.toString().trim();
  }

  public List<ZaakCommentaar> getCommentaren() {
    Collections.sort(commentaren);
    return commentaren;
  }

  public void setCommentaren(List<ZaakCommentaar> commentaren) {
    this.commentaren = commentaren;
  }

  public void toevoegen(ZaakCommentaarType type, String tekst) {
    if (fil(tekst)) {
      commentaren.add(new ZaakCommentaar(type, tekst));
    }
  }

  public void toevoegenWarn(List<String> teksts) {
    toevoegenWarn(teksts.toArray(new String[teksts.size()]));
  }

  public void toevoegenWarn(String... teksts) {
    for (String tekst : teksts) {
      toevoegen(ZaakCommentaarType.WARN, tekst);
    }

    if (zaak != null) {
      zaak.setCommentaar(zaak.getCommentaren().getCommentaarTekst());
    }
  }

  public ZaakCommentaren verwijderen() {

    int i = commentaren.size();
    for (ZaakCommentaar c : new ArrayList<>(commentaren)) {
      i--;
      if (c.getType().getVnr() > 0) {
        commentaren.remove(i);
      }
    }

    return this;
  }
}
