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

package nl.procura.gba.web.services.zaken.verhuizing;

import static nl.procura.standard.Globalfunctions.*;

import java.io.Serializable;

import nl.procura.diensten.gba.ple.openoffice.DocumentPL;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

public class VerhuisToestemminggever implements Serializable {

  private static final long serialVersionUID = 1990719729654006086L;

  private BsnFieldValue   bsn = new BsnFieldValue();
  private DocumentPL      persoon;
  private VerhuisAanvraag aanvraag;

  public VerhuisToestemminggever(VerhuisAanvraag aanvraag) {
    setAanvraag(aanvraag);
  }

  public AangifteStatus getAangifteStatus() {
    switch (getAanvraag().getToestemminggever().getToestemmingStatus()) {
      case JA:
        return AangifteStatus.GEACCEPTEERD;

      default:
        switch (aval(getAanvraag().getAangifteAccept())) {
          case 0:
            return AangifteStatus.NIET_GEACCEPTEERD;
          case 1:
            return AangifteStatus.GEACCEPTEERD_ZONDER_TOESTEMMING;
          default:
            return AangifteStatus.NIET_INGEVULD;
        }
    }
  }

  public void setAangifteStatus(AangifteStatus status) {

    switch (status) {
      case GEACCEPTEERD:
      case GEACCEPTEERD_ZONDER_TOESTEMMING:
        getAanvraag().setAangifteAccept(toBigDecimal(1));
        break;

      case NIET_GEACCEPTEERD:
        getAanvraag().setAangifteAccept(toBigDecimal(0));
        break;

      case NIET_INGEVULD:
        getAanvraag().setAangifteAccept(toBigDecimal(-1));
        break;

      default:
        break;
    }
  }

  public VerhuisAanvraag getAanvraag() {
    return aanvraag;
  }

  public void setAanvraag(VerhuisAanvraag aanvraag) {
    this.aanvraag = aanvraag;
  }

  public String getAnders() {
    return getAanvraag().getToestAnders();
  }

  public void setAnders(String anders) {
    getAanvraag().setToestAnders(anders);
  }

  public BsnFieldValue getBurgerServiceNummer() {
    return bsn;
  }

  public void setBurgerServiceNummer(BsnFieldValue bsn) {
    this.bsn = bsn;
    getAanvraag().setToestBsn(bsn.getStringValue());
  }

  public DocumentPL getPersoon() {
    return persoon;
  }

  public void setPersoon(DocumentPL persoon) {
    this.persoon = persoon;
  }

  public ToestemmingStatus getToestemmingStatus() {

    if (fil(getAanvraag().getToestemminggever().getAnders())) {
      return ToestemmingStatus.JA;
    }

    if (pos(getAanvraag().getHoofdBsn())) {
      if (pos(getAanvraag().getToestBsn())) {
        switch (aval(getAanvraag().getToestGeg())) {
          case 1:
            return ToestemmingStatus.JA;

          case 0:
            return ToestemmingStatus.NEE;

          default:
            return ToestemmingStatus.NIET_INGEVULD;
        }
      }
      return ToestemmingStatus.NIET_INGEVULD;
    }

    return ToestemmingStatus.NIET_VAN_TOEPASSING;
  }

  public void setToestemmingStatus(ToestemmingStatus status) {

    switch (status) {
      case JA:
        setBurgerServiceNummer(getAanvraag().getHoofdbewoner().getBurgerServiceNummer());
        getAanvraag().setToestGeg(toBigDecimal(1));
        break;

      case NEE:
        setBurgerServiceNummer(getAanvraag().getHoofdbewoner().getBurgerServiceNummer());
        getAanvraag().setToestGeg(toBigDecimal(0));
        break;

      case NIET_INGEVULD:
      case NIET_VAN_TOEPASSING:
        setBurgerServiceNummer(new BsnFieldValue());
        getAanvraag().setToestGeg(toBigDecimal(-1));
        break;

      default:
        break;
    }
  }

  public boolean isNatuurlijkPersoon() {
    return emp(getAnders()) && getPersoon() != null;
  }

  public boolean isToestemmingGegeven() {
    return getToestemmingStatus() == ToestemmingStatus.JA;
  }

  public String toString() {
    if (getPersoon() != null) {
      return getPersoon().getPersoon().getFormats().getNaam().getPred_eerstevoorn_adel_voorv_gesl();
    } else if (fil(getAnders())) {
      return getAnders();
    }

    return "";
  }

  public enum AangifteStatus {

    GEACCEPTEERD("Ja"),
    GEACCEPTEERD_ZONDER_TOESTEMMING("Ja, zonder toestemming verwerken"),
    NIET_GEACCEPTEERD("Nee"),
    NIET_INGEVULD("Nog niet aangegeven");

    private String description;

    AangifteStatus(String description) {
      this.description = description;
    }

    public String toString() {
      return description;
    }
  }

  public enum ToestemmingStatus {

    NIET_INGEVULD("Niet ingevuld"),
    JA("Ja"),
    NEE("Nee"),
    NIET_VAN_TOEPASSING("Niet van toepassing");

    private String description;

    ToestemmingStatus(String description) {
      this.description = description;
    }

    public String toString() {
      return description;
    }
  }
}
