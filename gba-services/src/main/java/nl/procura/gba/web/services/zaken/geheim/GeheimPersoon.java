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

package nl.procura.gba.web.services.zaken.geheim;

import nl.procura.diensten.gba.ple.openoffice.DocumentPL;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

public class GeheimPersoon {

  private AnrFieldValue anummer             = new AnrFieldValue();
  private BsnFieldValue burgerServiceNummer = new BsnFieldValue();
  private DocumentPL    persoon             = null;

  public GeheimPersoon(AnrFieldValue anummer, BsnFieldValue burgerServiceNummer) {

    setAnummer(anummer);
    setBurgerServiceNummer(burgerServiceNummer);
  }

  public AnrFieldValue getAnummer() {
    return anummer;
  }

  public void setAnummer(AnrFieldValue anummer) {
    this.anummer = anummer;
  }

  public BsnFieldValue getBurgerServiceNummer() {
    return burgerServiceNummer;
  }

  public void setBurgerServiceNummer(BsnFieldValue burgerServiceNummer) {
    this.burgerServiceNummer = burgerServiceNummer;
  }

  public DocumentPL getPersoon() {
    return persoon;
  }

  public void setPersoon(DocumentPL persoon) {
    this.persoon = persoon;
  }
}
