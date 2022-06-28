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

package nl.procura.gba.web.modules.zaken.personmutations.page2;

import static java.util.EnumSet.of;
import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.*;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.*;
import static nl.procura.standard.Globalfunctions.astr;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.vaadin.ui.AbstractField;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.burgerzaken.gba.core.enums.GBAGroup;
import nl.procura.burgerzaken.gba.core.enums.GBAGroupElements;
import nl.procura.diensten.gba.ple.base.*;
import nl.procura.gba.web.services.beheer.personmutations.PersonListActionType;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class PersonListMutElem {

  private final BasePL                        pl;
  private final BasePLRec                     rec;
  private final BasePLSet                     set;
  private final BasePLElem                    elem;
  private final GBAGroupElements.GBAGroupElem type;
  private final PersonListActionType          action;

  private Supplier<FieldValue> defaultValue;
  private AbstractField        field;

  public PersonListMutElem(BasePL pl, BasePLRec rec, BasePLElem elem, PersonListActionType action) {
    this.pl = pl;
    this.set = rec.getSet();
    this.rec = rec;
    this.elem = elem;
    this.type = GBAGroupElements.getByCat(elem.getCatCode(), elem.getElemCode());
    this.action = action;
  }

  public boolean isVisible() {
    List<GBAGroup> list = new ArrayList<>();
    if (rec.getCatType().is(GBACat.INSCHR)) {
      list.add(OPNEMING);
    }
    list.add(GBAGroup.VERIFICATIE);
    list.add(GBAGroup.RNI_DEELNEMER);
    list.add(GBAGroup.BUITENL_REISDOC);
    return !list.contains(type.getGroup());
  }

  public boolean isChangeble() {
    if (action.is(SUPER_CHANGE)) {
      return true;
    }

    if (action.is(CORRECT_CATEGORY)) {
      return false;
    }
    if (action.is(CORRECT_CURRENT_ADMIN) && !type.getElem().isAdmin()) {
      return false;

    } else if (!action.is(ADD_HISTORIC, SUPER_CHANGE) && GBAElem.IND_ONJUIST == type.getElem()) {
      return false;

    } else
      return GBAElem.DATUM_VAN_OPNEMING != type.getElem();
  }

  public boolean isRequiredGroup() {
    return of(AKTE, DOCUMENT, GELDIGHEID, OPNEMING).contains(type.getGroup());
  }

  public BasePL getPl() {
    return pl;
  }

  public BasePLElem getElem() {
    return elem;
  }

  public BasePLRec getRec() {
    return rec;
  }

  public BasePLSet getSet() {
    return set;
  }

  public GBAGroupElements.GBAGroupElem getTypes() {
    return type;
  }

  public GBAElem getElemType() {
    return type.getElem();
  }

  public GBACat getCat() {
    return type.getCat();
  }

  public GBAGroup getGroup() {
    return type.getGroup();
  }

  public int getSetIndex() {
    return getSet().getExtIndex();
  }

  public BasePLValue getCurrentValue() {
    return elem.getValue();
  }

  public String getNewValue() {
    if (field.getValue() instanceof FieldValue) {
      FieldValue val = (FieldValue) field.getValue();
      return astr(val.getValue());
    }
    return astr(field.getValue());
  }

  public String getNewDescription() {
    if (field.getValue() instanceof FieldValue) {
      FieldValue fv = (FieldValue) field.getValue();
      return fv.getDescription();
    }
    return astr(field.getValue());
  }

  public boolean isChanged() {
    // Workaround voor datums die soms 0 of soms 00000000 hebben.
    long currentNr = NumberUtils.toLong(getNewValue(), -1L);
    long newNr = NumberUtils.toLong(getCurrentValue().getVal(), -1L);
    if (currentNr >= 0 && newNr >= 0) {
      return currentNr != newNr;
    } else {
      return !Objects.equals(getNewValue(), getCurrentValue().getVal());
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + elem.getCatCode();
    result = prime * result + elem.getElemCode();
    result = prime * result + rec.getIndex();
    result = prime * result + set.getExtIndex();
    return result;
  }

  public AbstractField getField() {
    return field;
  }

  public void setField(AbstractField field) {
    this.field = field;
  }

  public Optional<Supplier<FieldValue>> getDefaultValue() {
    return Optional.ofNullable(defaultValue);
  }

  public void setDefaultValue(Supplier<FieldValue> defaultValue) {
    this.defaultValue = defaultValue;
  }

  public PersonListActionType getAction() {
    return action;
  }

  public boolean isBlank() {
    return StringUtils.isBlank(getNewValue());
  }

  /**
   * A "non administrative" field. In dutch: Algemeen gegeven
   */
  public boolean isGeneralField() {
    return !getElemType().isAdmin();
  }

  /**
   * An "administrative" field. In dutch: administratief gegeven
   */
  public boolean isAdminField() {
    return getElemType().isAdmin();
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }

    if (obj == null) {
      return false;
    }

    if (!(obj instanceof PersonListMutElem)) {
      return false;
    }

    PersonListMutElem other = (PersonListMutElem) obj;
    if (other.elem.getCatCode() != elem.getCatCode()) {
      return false;
    } else if (other.elem.getElemCode() != elem.getElemCode()) {
      return false;
    } else if (other.getRec().getIndex() != getRec().getIndex()) {
      return false;
    } else {
      return other.getSet().getExtIndex() == getSet().getExtIndex();
    }
  }

  public void validate() {
    if (field != null) {
      try {
        field.validate();
      } finally {
        field.requestRepaint();
      }
    }
  }
}
