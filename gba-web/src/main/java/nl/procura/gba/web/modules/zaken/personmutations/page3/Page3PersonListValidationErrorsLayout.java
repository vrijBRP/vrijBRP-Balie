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

package nl.procura.gba.web.modules.zaken.personmutations.page3;

import static nl.procura.burgerzaken.gba.core.enums.GBAElem.OMSCHR_REDEN_OPSCH_BIJHOUD;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.ADD_HISTORIC;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.ADD_SET;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.CORRECT_CURRENT_ADMIN;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.CORRECT_CURRENT_GENERAL;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.CORRECT_HISTORIC_ADMIN;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.CORRECT_HISTORIC_GENERAL;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.OVERWRITE_CURRENT;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.OVERWRITE_HISTORIC;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.SUPER_OVERWRITE_CURRENT;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.SUPER_OVERWRITE_HISTORIC;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.UPDATE_SET;
import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;
import static org.apache.commons.lang3.StringUtils.capitalize;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.gba.web.modules.zaken.personmutations.page2.PersonListMutElem;
import nl.procura.gba.web.modules.zaken.personmutations.page2.PersonListMutElems;
import nl.procura.gba.web.services.beheer.personmutations.PersonListActionType;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.theme.twee.ProcuraTheme;

import lombok.Value;

public class Page3PersonListValidationErrorsLayout extends VLayout {

  private final Set<ValidationMsg>   messages = new LinkedHashSet<>();
  private final PersonListMutElems   elems;
  private final PersonListActionType actionType;

  public Page3PersonListValidationErrorsLayout(PersonListMutElems elems, PersonListActionType actionType) {
    this.elems = elems;
    this.actionType = actionType;
  }

  public void setErrors() {
    addActionTypeMessages();
    StringBuilder msg = new StringBuilder();
    if (!messages.isEmpty()) {
      for (ValidationMsg message : messages) {
        String caption = "Algemene informatie";
        if (message.getElem() != null) {
          caption = capitalize(escapeHtml4(message.getElem().getElemType().getDescrAndCode()));
        }
        msg.append("<div><b>")
            .append(caption)
            .append("</b> - ")
            .append(escapeHtml4(message.getMsg()))
            .append("</div>");
      }
      String icon = ProcuraTheme.ICOON_24.WARNING;
      if (getErrors().isEmpty()) {
        icon = ProcuraTheme.ICOON_24.INFORMATION;
      }
      addComponent(new InfoLayout("", icon, msg.toString()));
    }
  }

  public void addActionTypeMessages() {
    getSuspendedMessage().ifPresent(this::addInfo);

    if (actionType.is(
        OVERWRITE_CURRENT,
        OVERWRITE_HISTORIC,
        SUPER_OVERWRITE_CURRENT,
        SUPER_OVERWRITE_HISTORIC)) {
      if (isNotChanged(elems, personListMutElem -> true)) {
        addError("Er dient minimaal één gegeven te worden gewijzigd.");
      }
    } else {
      getDuplicateRecordMessage().ifPresent(this::addInfo);
    }

    if (actionType.is(ADD_SET, UPDATE_SET, CORRECT_CURRENT_GENERAL, ADD_HISTORIC, CORRECT_HISTORIC_GENERAL)) {
      if (isNotChanged(elems, PersonListMutElem::isGeneralField)) {
        addError("Er dient minimaal één algemeen gegeven te worden gewijzigd.");
      }
    } else if (actionType.is(CORRECT_CURRENT_ADMIN, CORRECT_HISTORIC_ADMIN)) {
      if (isNotChanged(elems, PersonListMutElem::isAdminField)) {
        addError("Er dient minimaal één administratief gegeven te worden gewijzigd.");
      }
    }
  }

  /**
   * Return a message if the PL is suspended
   */
  private Optional<String> getSuspendedMessage() {
    if (isSuspended()) {
      return Optional.of("Deze persoonslijst is opgeschort");
    }
    return Optional.empty();
  }

  /**
   * Return a message if the new values already exists
   */
  private Optional<String> getDuplicateRecordMessage() {
    Set<Map<Integer, String>> recordHashes = new HashSet<>();
    if (!elems.isEmpty()) {
      for (BasePLRec rec : elems.get(0).getSet().getRecs()) {
        Map<Integer, String> recordMap = new TreeMap<>();
        for (BasePLElem elem : rec.getElems()) {
          GBAElem gbaElem = GBAElem.getByCode(elem.getElemCode());
          if (gbaElem.isNational() && !gbaElem.isAdmin()) {
            recordMap.put(elem.getElemCode(), elem.getValue().getVal().trim());
          }
          recordHashes.add(recordMap);
        }
      }

      Map<Integer, String> newRecordMap = new TreeMap<>();
      for (PersonListMutElem elem : elems) {
        if (elem.getElemType().isNational() && elem.isGeneralField()) {
          newRecordMap.put(elem.getElemType().getCode(), elem.getNewValue().trim());
        }
      }

      if (recordHashes.contains(newRecordMap)) {
        return Optional.of("Er is al een record met dezelfde algemene gegevens");
      }
    }
    return Optional.empty();
  }

  public void clearMessages() {
    messages.clear();
    removeAllComponents();
  }

  public void addError(String error) {
    addError(null, error);
  }

  public void addInfo(String error) {
    messages.add(new ValidationMsg(null, error, false));
  }

  public void addError(PersonListMutElem elem, String error) {
    messages.add(new ValidationMsg(elem, error, true));
  }

  public Set<ValidationMsg> getErrors() {
    return messages.stream()
        .filter(ValidationMsg::isError)
        .collect(Collectors.toSet());
  }

  public Set<ValidationMsg> getInfo() {
    return messages.stream()
        .filter(validationMsg -> !validationMsg.isError())
        .collect(Collectors.toSet());
  }

  private boolean isNotChanged(PersonListMutElems elems, Predicate<PersonListMutElem> predicate) {
    return elems.stream()
        .filter(predicate)
        .noneMatch(PersonListMutElem::isChanged);
  }

  private boolean isSuspended() {
    return elems.get(0).getPl()
        .getCat(GBACat.INSCHR)
        .getLatestRec()
        .getElemVal(OMSCHR_REDEN_OPSCH_BIJHOUD).isNotBlank();
  }

  @Value
  public static class ValidationMsg {

    PersonListMutElem elem;
    String            msg;
    boolean           error;

    public ValidationMsg(PersonListMutElem elem, String msg, boolean error) {
      this.elem = elem;
      this.msg = msg;
      this.error = error;
    }
  }
}
