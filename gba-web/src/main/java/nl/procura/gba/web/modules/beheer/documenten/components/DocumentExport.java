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

import lombok.Data;

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

  @Data
  public static class DocumentExportEntry implements Serializable {

    private static final long serialVersionUID = 7444156440521781425L;

    private long       volgNr            = 0;
    private String     naam              = "";
    private String     alias             = "";
    private String     documentDmsType   = "";
    private String     omschrijving      = "";
    private String     sjabloon          = "";
    private String     type              = "";
    private String     map               = "";
    private String     vertrouwelijkheid = "";
    private String     formats           = "";
    private BigDecimal vervalDatum       = null;

    private boolean kopieOpslaan          = false;
    private boolean protocollering        = false;
    private boolean standaardGeselecteerd = false;
    private boolean iedereenToegang       = false;
    private boolean stillbornAllowed      = false;

    private List<Long> koppelEnums = new ArrayList<>();

    public DocumentExportEntry() {
    }
  }
}
