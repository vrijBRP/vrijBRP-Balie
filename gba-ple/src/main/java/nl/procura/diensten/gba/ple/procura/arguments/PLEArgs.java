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

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.Globalfunctions.fil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.validation.Anr;
import nl.procura.validation.Bsn;

public class PLEArgs implements Serializable {

  private boolean showArchives  = false;
  private boolean showRemoved   = false;
  private boolean showHistory   = true;
  private boolean showMutations = true;
  private boolean showSuspended = true;

  private boolean searchOnAddress      = false;
  private boolean searchRelations      = false;
  private boolean searchIndications    = false; // Afnemerindicaties
  private String  reasonForIndications = "";

  private PLEDatasource datasource   = PLEDatasource.STANDAARD;
  private int           maxFindCount = 300;

  private Set<GBACat>   categories = EnumSet.noneOf(GBACat.class);
  private Set<PLNumber> numbers    = new LinkedHashSet<>();
  private String        customTemplate;

  private String geslachtsnaam        = "";
  private String voornaam             = "";
  private String voorvoegsel          = "";
  private String geslacht             = "";
  private String geboortedatum        = "";
  private String titel                = "";
  private String straat               = "";
  private String huisnummer           = "";
  private String huisletter           = "";
  private String huisnummertoevoeging = "";
  private String aanduiding           = "";
  private String postcode             = "";
  private String gemeentedeel         = "";
  private String gemeente             = "";

  public final void addCat(GBACat cat, GBACat... cats) {
    categories.add(cat);
    categories.addAll(Arrays.asList(cats));
  }

  public final void removeCat(GBACat cat, GBACat... cats) {
    categories.remove(cat);
    for (GBACat c : cats) {
      categories.remove(c);
    }
  }

  public void setCat(GBACat cat, boolean doAdd) {
    if (doAdd) {
      categories.add(cat);
    } else {
      categories.remove(cat);
    }
  }

  public List<String> getAnummers() {
    List<String> nrs = new ArrayList<>();
    for (PLNumber nummer : getNumbers()) {
      if (nummer.getA1() > 0) {
        nrs.add(new Anr(nummer.getA1(), nummer.getA2(), nummer.getA3()).getAnummer());
      }
    }

    return nrs;
  }

  public List<String> getBsn() {
    List<String> nrs = new ArrayList<>();
    for (PLNumber nummer : getNumbers()) {
      if (nummer.getBsn() > 0) {
        nrs.add(new Bsn(nummer.getBsn()).getDefaultBsn());
      }
    }

    return nrs;
  }

  public void addNummer(String... nrs) {
    for (String nr : nrs) {
      if (fil(nr)) {
        Anr a = new Anr(nr);
        if (a.isCorrect()) {
          getNumbers().add(new PLNumber(aval(a.getA1()), aval(a.getA2()), aval(a.getA3()), 0, -1));
        } else {
          Bsn b = new Bsn(nr);
          if (!b.isCorrect()) {
            throw new IllegalArgumentException("101: Incorrect BSN of A-nummer: " + nr);
          }

          getNumbers().add(new PLNumber(0, 0, 0, b.getLongBsn(), -1));
        }
      }
    }
  }

  public final boolean hasCat(GBACat cat) {
    if (datasource != PLEDatasource.PROCURA && cat == GBACat.VERW) {
      return false;
    }
    return categories.isEmpty() || categories.contains(cat);
  }

  public boolean isShowArchives() {
    if (datasource != PLEDatasource.PROCURA) {
      return false;
    }

    return showArchives;
  }

  public void setShowArchives(boolean showArchives) {
    this.showArchives = showArchives;
  }

  public boolean isShowRemoved() {
    if (datasource != PLEDatasource.PROCURA) {
      return false;
    }

    return showRemoved;
  }

  public void setShowRemoved(boolean showRemoved) {
    this.showRemoved = showRemoved;
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

  public boolean isShowMutations() {
    return showMutations;
  }

  public void setShowMutations(boolean showMutations) {
    this.showMutations = showMutations;
  }

  public boolean isSearchOnAddress() {
    return searchOnAddress;
  }

  public void setSearchOnAddress(boolean searchOnAddress) {
    this.searchOnAddress = searchOnAddress;
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
    this.maxFindCount = maxFindCount;
  }

  public Set<GBACat> getCategories() {
    return categories;
  }

  public void setCategories(Set<GBACat> categories) {
    this.categories = categories;
  }

  public Set<PLNumber> getNumbers() {
    return numbers;
  }

  public void setNumbers(Set<PLNumber> numbers) {
    this.numbers = numbers;
  }

  public String getCustomTemplate() {
    return customTemplate;
  }

  public void setCustomTemplate(String customTemplate) {
    this.customTemplate = customTemplate;
  }

  public String getGeslachtsnaam() {
    return geslachtsnaam;
  }

  public void setGeslachtsnaam(String geslachtsnaam) {
    this.geslachtsnaam = geslachtsnaam;
  }

  public String getVoornaam() {
    return voornaam;
  }

  public void setVoornaam(String voornaam) {
    this.voornaam = voornaam;
  }

  public String getVoorvoegsel() {
    return voorvoegsel;
  }

  public void setVoorvoegsel(String voorvoegsel) {
    this.voorvoegsel = voorvoegsel;
  }

  public String getGeslacht() {
    return geslacht;
  }

  public void setGeslacht(String geslacht) {
    this.geslacht = geslacht;
  }

  public String getGeboortedatum() {
    return geboortedatum;
  }

  public void setGeboortedatum(String geboortedatum) {
    this.geboortedatum = geboortedatum;
  }

  public String getTitel() {
    return titel;
  }

  public void setTitel(String titel) {
    this.titel = titel;
  }

  public String getStraat() {
    return straat;
  }

  public void setStraat(String straat) {
    this.straat = straat;
  }

  public String getHuisnummer() {
    return huisnummer;
  }

  public void setHuisnummer(String huisnummer) {
    this.huisnummer = huisnummer;
  }

  public String getPostcode() {
    return postcode;
  }

  public void setPostcode(String postcode) {
    this.postcode = astr(postcode).replaceAll("\\s+", "").toUpperCase();
  }

  public String getGemeentedeel() {
    return gemeentedeel;
  }

  public void setGemeentedeel(String gemeentedeel) {
    this.gemeentedeel = gemeentedeel;
  }

  public String getGemeente() {
    return gemeente;
  }

  public void setGemeente(String gemeente) {
    this.gemeente = gemeente;
  }

  public boolean isNawGevuld() {

    if (fil(getAanduiding())) {
      return true;
    }
    if (fil(getGeboortedatum())) {
      return true;
    }
    if (fil(getGemeente())) {
      return true;
    }
    if (fil(getGemeentedeel())) {
      return true;
    }
    if (fil(getGeslacht())) {
      return true;
    }
    if (fil(getGeslachtsnaam())) {
      return true;
    }
    if (fil(getHuisletter())) {
      return true;
    }
    if (fil(getHuisnummer())) {
      return true;
    }
    if (fil(getHuisnummertoevoeging())) {
      return true;
    }
    if (fil(getPostcode())) {
      return true;
    }
    if (fil(getStraat())) {
      return true;
    }
    if (fil(getTitel())) {
      return true;
    }
    if (fil(getVoornaam())) {
      return true;
    }
    return fil(getVoorvoegsel());
  }

  public String getHuisletter() {
    return huisletter;
  }

  public void setHuisletter(String huisletter) {
    this.huisletter = huisletter;
  }

  public String getHuisnummertoevoeging() {
    return huisnummertoevoeging;
  }

  public void setHuisnummertoevoeging(String huisnummertoevoeging) {
    this.huisnummertoevoeging = huisnummertoevoeging;
  }

  public String getAanduiding() {
    return aanduiding;
  }

  public void setAanduiding(String aanduiding) {
    this.aanduiding = aanduiding;
  }

  public boolean isSearchRelations() {
    return searchRelations;
  }

  public void setSearchRelations(boolean searchRelations) {
    this.searchRelations = searchRelations;
  }

  public boolean isSearchIndications() {
    return searchIndications;
  }

  public void setSearchIndications(boolean searchIndications) {
    this.searchIndications = searchIndications;
  }

  public String getReasonForIndications() {
    return reasonForIndications;
  }

  public void setReasonForIndications(String reasonForIndications) {
    this.reasonForIndications = reasonForIndications;
  }
}
