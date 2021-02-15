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

package nl.procura.diensten.zoekpersoon.objecten;

public class Reisdocument {

  private Reisdocumentgegevens   reisdocument_actueel;
  private Reisdocumentgegevens[] reisdocument_historie;

  public Reisdocument() {
  }

  public Reisdocumentgegevens getReisdocument_actueel() {
    return reisdocument_actueel;
  }

  public void setReisdocument_actueel(Reisdocumentgegevens reisdocument_actueel) {
    this.reisdocument_actueel = reisdocument_actueel;
  }

  public Reisdocumentgegevens[] getReisdocument_historie() {
    return reisdocument_historie;
  }

  public void setReisdocument_historie(Reisdocumentgegevens[] reisdocument_historie) {
    this.reisdocument_historie = reisdocument_historie;
  }
}
