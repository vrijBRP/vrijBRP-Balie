/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.burgerzaken.requestinbox.api;

import nl.procura.validation.Bsn;

public class RequestInboxUtils {

  public static String getVrijBRPChannel() {
    return "https://vrijbrp.nl";
  }

  public static String getVrijBRPCustomer(Bsn bsn) {
    return bsn.isCorrect() ? getVrijBRPChannel() + "/bsn/" + bsn.getLongBsn() : "";
  }

  public static String getVrijBRPUser(Long id) {
    return id > 0 ? getVrijBRPChannel() + "/user/" + id : "";
  }

  public static Bsn getBsnFromUrl(String url) {
    return new Bsn(getValueAfterLastSlash(url));
  }

  public static String getUserIdFromUrl(String url) {
    return getValueAfterLastSlash(url);
  }

  private static String getValueAfterLastSlash(String url) {
    return url.contains(getVrijBRPChannel()) ? url.substring(url.lastIndexOf("/") + 1) : "";
  }
}
