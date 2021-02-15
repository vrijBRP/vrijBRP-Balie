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

package nl.procura.diensten.gba.ple.procura.arguments;

import java.io.Serializable;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

import nl.procura.burgerzaken.gba.core.enums.GBACat;

public class PLECatArgs implements Serializable {

  private static final long serialVersionUID = 4705678726633120003L;

  private boolean showArchives    = false;
  private boolean showHistory     = true;
  private boolean showSuspended   = true;
  private boolean searchOnAddress = false;

  private PLEDatasource datasource   = PLEDatasource.STANDAARD;
  private int           maxFindCount = 300;

  private EnumSet<GBACat> categories = EnumSet.noneOf(GBACat.class);
  private String          template;

  public PLECatArgs() {
  }

  public final void add(GBACat cat, GBACat... cats) {
    categories.add(cat);
    categories.addAll(Arrays.asList(cats));
  }

  public final void remove(GBACat cat, GBACat... cats) {
    categories.remove(cat);
    Arrays.stream(cats).forEach(c -> categories.remove(c));
  }

  public void set(GBACat cat, boolean doAdd) {
    if (doAdd) {
      categories.add(cat);
    } else {
      categories.remove(cat);
    }
  }

  public final boolean has(GBACat cat) {
    return categories.contains(cat);
  }

  public PLEDatasource getDatasource() {
    return datasource;
  }

  public void setDatasource(PLEDatasource datasource) {
    this.datasource = datasource;
  }

  public int getMaxFindCount() {
    return maxFindCount;
  }

  public void setMaxFindCount(int maxFindCount) {
    if (maxFindCount < this.maxFindCount) {
      this.maxFindCount = maxFindCount;
    }
  }

  public String getTemplate() {
    return template;
  }

  public void setTemplate(String template) {
    this.template = template;
  }

  public final Set<GBACat> getCategories() {
    return EnumSet.copyOf(categories);
  }

  public final void setCategories(Set<GBACat> categories) {
    this.categories = EnumSet.copyOf(categories);
  }

  public boolean isShowArchives() {
    return showArchives;
  }

  public void setShowArchives(boolean showArchives) {
    this.showArchives = showArchives;
  }

  public boolean isShowHistory() {
    return showHistory;
  }

  public void setShowHistory(boolean showHistory) {
    this.showHistory = showHistory;
  }

  public boolean isShowSuspended() {
    return showSuspended;
  }

  public void setShowSuspended(boolean showSuspended) {
    this.showSuspended = showSuspended;
  }

  public boolean isSearchOnAddress() {
    return searchOnAddress;
  }

  public void setSearchOnAddress(boolean searchOnAddress) {
    this.searchOnAddress = searchOnAddress;
  }
}
