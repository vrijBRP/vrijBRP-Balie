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

package nl.procura.gba.web.services.bs.algemeen.vereiste;

import static nl.procura.standard.Globalfunctions.isTru;
import static nl.procura.standard.Globalfunctions.pos;

import java.math.BigDecimal;

import nl.procura.gba.jpa.personen.db.Doss;
import nl.procura.gba.jpa.personen.db.DossVereiste;
import nl.procura.gba.jpa.personen.db.DossVereistePK;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.java.reflection.ReflectionUtil;

public class DossierVereiste extends DossVereiste {

  private static final long serialVersionUID = 5103620635580225589L;

  public DossierVereiste() {
    setId(new DossVereistePK());
  }

  public String getDossierVereiste() {
    return getDossVereiste();
  }

  public void setDossierVereiste(String dossierVereiste) {
    setDossVereiste(dossierVereiste);
  }

  public String getIdKey() {
    return getId().getId();
  }

  public void setIdKey(String id) {
    getId().setId(id);
  }

  public String getToelichting() {
    return getOms();
  }

  public void setToelichting(String toelichting) {
    setOms(toelichting);
  }

  public boolean isHeeftVoldaan() {
    return isTru(getVoldaan());
  }

  public boolean isOverruled() {
    return pos(getOverrule());
  }

  public void setOverruled(boolean overruled) {
    setOverrule(BigDecimal.valueOf(overruled ? 1 : 0));
  }

  public void koppelenAan(Dossier dossier) {

    setDoss(ReflectionUtil.deepCopyBean(Doss.class, dossier));
    getId().setCDoss(dossier.getCode());
  }
}
