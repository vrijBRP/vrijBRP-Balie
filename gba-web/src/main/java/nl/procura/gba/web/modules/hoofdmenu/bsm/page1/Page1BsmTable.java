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

package nl.procura.gba.web.modules.hoofdmenu.bsm.page1;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.standard.Globalfunctions.equalsIgnoreCase;
import static nl.procura.standard.Globalfunctions.fil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Embedded;

import nl.procura.bsm.rest.v1_0.objecten.log.BsmRestLogResultaatType;
import nl.procura.bsm.rest.v1_0.objecten.taak.BsmRestTaak;
import nl.procura.bsm.rest.v1_0.objecten.taak.BsmRestTaakStatus;
import nl.procura.gba.web.components.layouts.table.GbaTable;

public class Page1BsmTable extends GbaTable {

  private final List<BsmRestTaak> taken = new ArrayList<>();

  public Page1BsmTable() {
  }

  public static String getLaatsteStatus(BsmRestTaak taak) {

    BsmRestTaakStatus status = taak.getStatus();

    if (status == BsmRestTaakStatus.GEBLOKKEERD) {
      return "<b>Bezig ...</b>";
    }

    BsmRestLogResultaatType laatsteStatus = taak.getLaatsteUitvoeringStatus();

    switch (laatsteStatus) {
      case FOUT:
        return setClass("red", "FOUT");

      case INFO:
        return "Ter info";

      case SUCCES:
        return setClass("green", "SUCCES");

      case WAARSCHUWING:
        return setClass("orange", "WAARSCHUWING");

      case ONBEKEND:
      default:
        return "";
    }
  }

  public static String getStatus(BsmRestTaakStatus status) {

    switch (status) {

      case FOUT:
        return "Fout";

      case GEBLOKKEERD:
        return "<b>Bezig ...</b>";

      case NORMAAL:
        return "Normaal";

      case ONDERBROKEN:
        return setClass(false, "Onderbroken");

      case VOLTOOID:
        return "Voltooid";

      case ONBEKEND:
      default:
        return "Onbekend";
    }
  }

  public static Embedded getStatusIcon(BsmRestTaak taak) {

    String icon = "";

    BsmRestTaakStatus status = taak.getStatus();

    switch (status) {

      case FOUT:
      case ONDERBROKEN:
        if (taak.isUitgezet()) {
          icon = "icons/bullet-grey.png";
        } else {
          icon = "icons/bullet-red.png";
        }
        break;

      case GEBLOKKEERD:
        icon = "icons/indicator.gif";
        break;

      case NORMAAL:
      case VOLTOOID:
        icon = "icons/bullet-green.png";
        break;

      case ONBEKEND:
      default:
        break;
    }

    Embedded embedded = null;

    if (fil(icon)) {
      embedded = new Embedded("", new ThemeResource(icon));
    }

    return embedded;
  }

  @Override
  public void setColumns() {

    addColumn("", 20).setClassType(Embedded.class);
    addColumn("Omschrijving").setUseHTML(true);
    addColumn("Id", 200);
    addColumn("Laatste poging", 130).setUseHTML(true);
    addColumn("Volgende uitvoering", 130);

    super.setColumns();
  }

  @Override
  public void setRecords() {

    try {

      getApplication().getServices().getBsmService().checkWaarschuwingsIcoon();

      taken.clear();

      taken.addAll(getApplication().getServices().getBsmService().getBsmTaken());

      for (String categorie : getCategories()) {

        Record categoryRecord = addRecord(categorie);

        categoryRecord.addValue("");
        categoryRecord.addValue("<b>" + categorie + "</b>");
        categoryRecord.addValue("");
        categoryRecord.addValue("");
        categoryRecord.addValue("");

        for (BsmRestTaak taak : getTaken(categorie)) {

          Record r = addRecord(taak);

          r.addValue(getStatusIcon(taak));
          r.addValue("<div class='indent'>" + taak.getOmschrijving() + "</div>");
          r.addValue(taak.getTaak());
          r.addValue(getLaatsteStatus(taak));
          r.addValue(taak.getVolgendeUitvoering());
        }
      }
    } catch (Exception e) {
      getApplication().handleException(e);
    }

    super.setRecords();
  }

  private Set<String> getCategories() {

    Set<String> set = new TreeSet<>();
    for (BsmRestTaak taak : taken) {
      set.add(taak.getCategorie());
    }

    return set;
  }

  private List<BsmRestTaak> getTaken(String categorie) {

    List<BsmRestTaak> list = new ArrayList<>();

    for (BsmRestTaak job : taken) {
      if (equalsIgnoreCase(job.getCategorie(), categorie)) {
        list.add(job);
      }
    }

    return list;
  }
}
