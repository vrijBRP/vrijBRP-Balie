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

package nl.procura.gba.web.modules.zaken.tmv.objects;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.fil;

import com.vaadin.ui.AbstractField;

import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.gba.web.modules.persoonslijst.overig.grid.RecordElementCombo;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class TmvRecord {

  private RecordElementCombo gbaCombo;
  private AbstractField      field;

  public TmvRecord(RecordElementCombo gbaCombo, AbstractField field) {
    super();
    this.gbaCombo = gbaCombo;
    this.field = field;
  }

  public long getCat() {
    return getGbaCombo().getGbaElement().getCatCode();
  }

  public String getDescr() {
    return getGbaCombo().getPleElement().getElem().getDescr();
  }

  public long getElem() {
    return getGbaCombo().getGbaElement().getElemCode();
  }

  public AbstractField getField() {
    return field;
  }

  public void setField(AbstractField field) {
    this.field = field;
  }

  public RecordElementCombo getGbaCombo() {
    return gbaCombo;
  }

  public void setGbaCombo(RecordElementCombo gbaCombo) {
    this.gbaCombo = gbaCombo;
  }

  public BasePLValue getHuidigeWaarde() {
    return getGbaCombo().getGbaElement().getValue();
  }

  public Object getNieuweFormatWaarde() {

    if (getField().getValue() instanceof FieldValue) {
      return ((FieldValue) getField().getValue()).getDescription();
    }

    return getField().getValue();
  }

  public Object getNieuweWaarde() {

    if (getField().getValue() instanceof FieldValue) {
      return ((FieldValue) getField().getValue()).getValue();
    }

    return getField().getValue();
  }

  public long getSet() {
    return getGbaCombo().getGbaSet().getExtIndex() - 1;
  }

  public boolean isVerschillend() {
    return fil(astr(getNieuweWaarde())) && !getHuidigeWaarde().getVal().equals(astr(getNieuweWaarde()));
  }
}
