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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.selecties.export;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.services.zaken.selectie.Selectie;

import lombok.Data;

@Data
public class SelectieExport implements Serializable {

  private static final long serialVersionUID = -8456923977140993755L;
  private List<Sel>         list             = new ArrayList<>();

  public Sel addSelectie(Selectie selectie) {
    Sel sel = new Sel();
    sel.setId(selectie.getId());
    sel.setSelectie(selectie.getSelectie());
    sel.setOmschrijving(selectie.getOmschrijving());
    sel.setStatement(selectie.getStatement());
    list.add(sel);
    return sel;
  }

  @Data
  public static class Sel implements Serializable {

    private static final long serialVersionUID = 5883553512055040660L;

    private String id           = "";
    private String selectie     = "";
    private String omschrijving = "";
    private String statement    = "";
  }
}
