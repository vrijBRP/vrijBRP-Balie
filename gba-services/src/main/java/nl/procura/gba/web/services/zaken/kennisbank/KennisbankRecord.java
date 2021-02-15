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

package nl.procura.gba.web.services.zaken.kennisbank;

import java.util.ArrayList;
import java.util.List;

public class KennisbankRecord {

  private long                natioCode         = 0;
  private String              natioOmsOfficieel = "";
  private long                landCode          = 0;
  private String              landOmsOfficieel  = "";
  private String              landOmsKennisbank = "";
  private List<KennisbankURL> urls              = new ArrayList<>();

  public String getLand() {
    return getLandCode() >= 0 ? (getLandOmsOfficieel() + " (" + getLandCode() + ")") : "N.v.t.";
  }

  public long getLandCode() {
    return landCode;
  }

  public void setLandCode(long landCode) {
    this.landCode = landCode;
  }

  public String getLandOmsKennisbank() {
    return landOmsKennisbank;
  }

  public void setLandOmsKennisbank(String landOmsKennisbank) {
    this.landOmsKennisbank = landOmsKennisbank;
  }

  public String getLandOmsOfficieel() {
    return landOmsOfficieel;
  }

  public void setLandOmsOfficieel(String landOmsOfficieel) {
    this.landOmsOfficieel = landOmsOfficieel;
  }

  public String getNatio() {
    return getNatioCode() >= 0 ? (getNatioOmsOfficieel() + " (" + getNatioCode() + ")") : "N.v.t.";
  }

  public long getNatioCode() {
    return natioCode;
  }

  public void setNatioCode(long natioCode) {
    this.natioCode = natioCode;
  }

  public String getNatioOmsOfficieel() {
    return natioOmsOfficieel;
  }

  public void setNatioOmsOfficieel(String natioOmsOfficieel) {
    this.natioOmsOfficieel = natioOmsOfficieel;
  }

  public List<KennisbankURL> getUrls() {
    return urls;
  }

  public void setUrls(List<KennisbankURL> urls) {
    this.urls = urls;
  }

  public static class KennisbankURL {

    private KennisBankDoel doel = KennisBankDoel.AFSTAMMING;
    private String         url  = "";

    public KennisbankURL(KennisBankDoel doel, String url) {
      setDoel(doel);
      setUrl(url);
    }

    public KennisBankDoel getDoel() {
      return doel;
    }

    public void setDoel(KennisBankDoel doel) {
      this.doel = doel;
    }

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }
  }
}
