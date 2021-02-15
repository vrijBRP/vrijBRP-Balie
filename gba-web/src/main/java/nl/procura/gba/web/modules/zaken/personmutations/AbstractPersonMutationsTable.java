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

package nl.procura.gba.web.modules.zaken.personmutations;

import static nl.procura.burgerzaken.gba.core.enums.GBAGroup.*;
import static nl.procura.gba.web.modules.zaken.personmutations.PersonListMutationsCheckBoxType.*;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.CORRECT_CATEGORY;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.fil;

import java.util.Objects;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Embedded;

import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.burgerzaken.gba.core.enums.GBATable;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.zaken.personmutations.page2.PersonListMutElem;

public abstract class AbstractPersonMutationsTable extends GbaTable {

  private PersonListMutationsCheckBoxType filter = PersonListMutationsCheckBoxType.ALL;

  public PersonListMutationsCheckBoxType getFilter() {
    return filter;
  }

  public void setFilter(PersonListMutationsCheckBoxType filter) {
    this.filter = filter;
    init();
  }

  /**
   * Hack to make sure rows in readonly tables and tables with fields are the same height.
   * That way the user has less of a transition
   */
  protected String setRowHeight(String descr) {
    return "<span style='line-height: 24px'>" + descr + "</span>";
  }

  /**
   * Return a (checked) checkbox image
   */
  protected Embedded getCheckbox(boolean checked) {
    Embedded component = new Embedded(null, new ThemeResource("icons/check.png"));
    return checked ? component : null;
  }

  protected String change(boolean changed, Object value) {
    return changed ? ("<b>" + astr(value) + "</b>") : astr(value);
  }

  public boolean isShowElem(PersonListMutElem elem) {
    if (!elem.getElemType().isNational()) {
      return false;
    }

    if (REQUIRED_GROUPS == filter) {
      if (elem.getAction().is(CORRECT_CATEGORY)) {
        return elem.getGroup().is(RDN_EINDE_NATIO, DOCUMENT, GELDIGHEID);
      }
      return elem.isRequiredGroup();
    }

    if (CHANGED == filter && !elem.isChanged()) {
      return false;

    } else if (ADMIN == filter && !elem.getElemType().isAdmin()) {
      return false;
    }
    return true;
  }

  public String getDescr(int elem, String val, String descr) {
    boolean table = GBATable.ONBEKEND != GBAElem.getByCode(elem).getTable();
    return descr + (table && fil(val) && !Objects.equals(val, descr) ? " (" + val + ")" : "");
  }
}
