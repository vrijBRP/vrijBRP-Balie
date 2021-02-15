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

package nl.procura.diensten.gba.ple.procura.templates;

import static nl.procura.burgerzaken.gba.core.enums.GBAElem.*;

import java.util.ArrayList;
import java.util.List;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBARecStatus;
import nl.procura.diensten.gba.ple.procura.utils.PLEWoningObject;
import nl.procura.diensten.gba.ple.procura.utils.SortableObject;
import nl.procura.gba.jpa.probev.db.VboKrt;

public class Cat32WKTemplate extends PLETemplateProcura<VboKrt> {

  private List<PLEWoningObject> wkObjects = new ArrayList<>();

  @Override
  public void parse(SortableObject<VboKrt> so) {

    VboKrt vboKrt = so.getObject();

    long d_end = vboKrt.getId().getDEnd();
    GBARecStatus status = (d_end == -1L) ? GBARecStatus.CURRENT : GBARecStatus.HIST;
    if ((!getArguments().isShowArchives() && (vboKrt.getX().intValue() > 0)) || (d_end < -1L)) {
      return;
    }

    if ((status == GBARecStatus.CURRENT) || getArguments().isShowHistory()) {
      addCat(GBACat.WK, status);
      addElem(WK_VOLGORDE_GEZIN, padl(vboKrt.getVGezin(), 4));
      addElem(WK_VOLGORDE_PERSOON, padl(vboKrt.getVInw(), 4));
      addElem(WK_WONINGCODE, vboKrt.getCVbo());
      addElem(WK_DATUM_AANVANG, vboKrt.getDIn());
      addElem(WK_DATUM_EINDE, d_end);
    }
  }

  public List<PLEWoningObject> getWkObjects() {
    return wkObjects;
  }

  public void setWkObjects(ArrayList<PLEWoningObject> wkObjects) {
    this.wkObjects = wkObjects;
  }
}
