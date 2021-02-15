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

import java.math.BigDecimal;

public interface ToestemmingbaarReisdocument {

  BigDecimal getAantalToestemmingen();

  String getAanvrNr();

  void setAanvrNr(String aanvrNr);

  String getToestCuratorNaam();

  void setToestCuratorNaam(String s);

  String getToestDerdeNaam();

  void setToestDerdeNaam(String s);

  String getToestOuder1Anr();

  void setToestOuder1Anr(String s);

  String getToestOuder1Naam();

  void setToestOuder1Naam(String s);

  String getToestOuder2Anr();

  void setToestOuder2Anr(String s);

  String getToestOuder2Naam();

  void setToestOuder2Naam(String s);

  BigDecimal getToestOuder1();

  void setToestOuder1(BigDecimal type);

  BigDecimal getToestOuder2();

  void setToestOuder2(BigDecimal type);

  BigDecimal getToestDerde();

  void setToestDerde(BigDecimal type);

  BigDecimal getToestCurator();

  void setToestCurator(BigDecimal type);
}
