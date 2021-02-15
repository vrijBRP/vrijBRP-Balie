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

package nl.procura.gba.web.modules.beheer.documenten.components;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DocumentExport implements Serializable {

  private static final long serialVersionUID = -3224414020738017065L;

  private List<DocumentExportEntry> dList = new ArrayList<>();

  public DocumentExportEntry addExportEntry() {

    DocumentExportEntry d = new DocumentExportEntry();
    getdList().add(d);
    return d;
  }

  public List<DocumentExportEntry> getdList() {
    return dList;
  }

  public void setdList(List<DocumentExportEntry> dList) {
    this.dList = dList;
  }

  public static class DocumentExportEntry implements Serializable {

    private static final long serialVersionUID = 7444156440521781425L;

    private long       volgNr      = 0;
    private String     naam        = "";
    private String     sjabloon    = "";
    private String     type        = "";
    private String     map         = "";
    private BigDecimal vervalDatum = null;

    private boolean kopieOpslaan          = false;
    private boolean protocollering        = false;
    private boolean standaardGeselecteerd = false;
    private boolean iedereenToegang       = false;
    private boolean stillbornAllowed      = false;

    private List<Long> koppelEnums = new ArrayList<>();

    public DocumentExportEntry() {
    }

    public List<Long> getKoppelEnums() {
      return koppelEnums;
    }

    public void setKoppelEnums(List<Long> koppelEnums) {
      this.koppelEnums = koppelEnums;
    }

    public String getMap() {
      return map;
    }

    public void setMap(String map) {
      this.map = map;
    }

    public String getNaam() {
      return naam;
    }

    public void setNaam(String naam) {
      this.naam = naam;
    }

    public String getSjabloon() {
      return sjabloon;
    }

    public void setSjabloon(String sjabloon) {
      this.sjabloon = sjabloon;
    }

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }

    public BigDecimal getVervalDatum() {
      return vervalDatum;
    }

    public void setVervalDatum(BigDecimal vervalDatum) {
      this.vervalDatum = vervalDatum;
    }

    public long getVolgNr() {
      return volgNr;
    }

    public void setVolgNr(long volgNr) {
      this.volgNr = volgNr;
    }

    public boolean isIedereen() {
      return iedereenToegang;
    }

    public boolean isKopieOpslaan() {
      return kopieOpslaan;
    }

    public void setKopieOpslaan(boolean kopieOpslaan) {
      this.kopieOpslaan = kopieOpslaan;
    }

    public boolean isProtocollering() {
      return protocollering;
    }

    public void setProtocollering(boolean protocollering) {
      this.protocollering = protocollering;
    }

    public boolean isStandaardGeselecteerd() {
      return standaardGeselecteerd;
    }

    public void setStandaardGeselecteerd(boolean standaardGeselecteerd) {
      this.standaardGeselecteerd = standaardGeselecteerd;
    }

    public void setIedereenToegang(boolean iedereenToegang) {
      this.iedereenToegang = iedereenToegang;
    }

    public boolean isStillbornAllowed() {
      return stillbornAllowed;
    }

    public void setStillbornAllowed(boolean stillbornAllowed) {
      this.stillbornAllowed = stillbornAllowed;
    }
  }
}
