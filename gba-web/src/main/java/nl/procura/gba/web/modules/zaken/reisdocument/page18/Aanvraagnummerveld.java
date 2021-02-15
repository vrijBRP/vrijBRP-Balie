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

package nl.procura.gba.web.modules.zaken.reisdocument.page18;

import static nl.procura.standard.Globalfunctions.fil;

import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;

import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.services.zaken.reisdocumenten.Aanvraagnummer;

public class Aanvraagnummerveld extends GbaTextField implements BlurListener {

  public Aanvraagnummerveld() {
    this(null);
  }

  private Aanvraagnummerveld(String naam) {
    super(naam);
    setValidationVisible(true);
    addListener((BlurListener) this);
  }

  @Override
  public void blur(BlurEvent event) {
  }

  @Override
  public Class<Aanvraagnummerveld> getType() {
    return Aanvraagnummerveld.class;
  }

  @Override
  protected void fireEvent(Event event) {

    String value = event.getSource().toString();

    if (fil(value)) {
      Aanvraagnummer a = new Aanvraagnummer(value);

      if (a.isCorrect()) {
        setValue(value, a.getFormatNummer());
      }
    }

    super.fireEvent(event);
  }
}
