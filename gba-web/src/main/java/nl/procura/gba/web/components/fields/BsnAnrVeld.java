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

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.fil;

import com.vaadin.data.validator.AbstractStringValidator;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;

import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.validation.Anummer;
import nl.procura.validation.Bsn;

public class BsnAnrVeld extends GbaTextField implements BlurListener {

  public BsnAnrVeld() {
    this(null);
  }

  private BsnAnrVeld(String naam) {

    super(naam);

    addValidator(new BsnAnrValidator());
    setValidationVisible(true);
    addListener((BlurListener) this);
  }

  @Override
  public void blur(BlurEvent event) {
  }

  @Override
  public Class<BsnAnrVeld> getType() {
    return BsnAnrVeld.class;
  }

  @Override
  public Object getValue() {
    return new Val(super.getValue());
  }

  @Override
  protected void fireEvent(Event event) {

    String value = event.getSource().toString();

    if (fil(value)) {

      FieldValue fv = new FieldValue(value);
      setValue(fv.getStringValue(), fv.getDescription());
    }

    super.fireEvent(event);
  }

  class BsnAnrValidator extends AbstractStringValidator {

    public BsnAnrValidator() {
      super("De gegeven waarde is geen BSN en geen A-nummer.");
    }

    @Override
    protected boolean isValidString(String nr) {
      return new Bsn(nr).isCorrect() || new Anummer(nr).isCorrect();
    }
  }

  class Val extends FieldValue {

    private Val(Object value) {

      Bsn bsn = new Bsn(astr(value));

      if (bsn.isCorrect()) {

        setValue(bsn.getLongBsn());
        setDescription(bsn.getFormatBsn());
      } else {

        Anummer anr = new Anummer(astr(value));

        if (anr.isCorrect()) {

          setValue(anr.getLongAnummer());
          setDescription(anr.getFormatAnummer());
        } else {
          setDescription(astr(value));
        }
      }
    }
  }
}
