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

package nl.procura.gba.web.components.fields;

import javax.mail.internet.InternetAddress;

import com.vaadin.data.validator.AbstractStringValidator;

public class GbaInternetAddressField extends GbaTextField {

  public GbaInternetAddressField() {
    this(null);
  }

  public GbaInternetAddressField(String naam) {
    super(naam);
    addValidator(new Validator());
    setValidationVisible(true);
  }

  @Override
  public Class<GbaInternetAddressField> getType() {
    return GbaInternetAddressField.class;
  }

  public class Validator extends AbstractStringValidator {

    public Validator() {
      this("Het gegeven E-mailadres is ongeldig.");
    }

    public Validator(String errorMessage) {
      super(errorMessage);
    }

    @Override
    protected boolean isValidString(String value) {
      try {
        new InternetAddress(value).validate();
        return true;
      } catch (Exception e) {
        e.printStackTrace();
        return false;
      }
    }
  }
}
