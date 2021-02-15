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

package nl.procura.gba.web.services.bs.omzetting;

import org.apache.commons.lang3.builder.CompareToBuilder;

import nl.procura.gba.jpa.personen.db.DossOmzet;
import nl.procura.gba.jpa.personen.db.DossOmzetOptie;
import nl.procura.gba.jpa.personen.db.DossOmzetOptiePK;
import nl.procura.gba.jpa.personen.db.HuwLocatieOptie;
import nl.procura.gba.web.services.gba.basistabellen.huwelijkslocatieOptie.HuwelijksLocatieOptie;
import nl.procura.java.reflection.ReflectionUtil;

public class DossierOmzettingOptie extends DossOmzetOptie implements Comparable<DossierOmzettingOptie> {

  private static final long serialVersionUID = -7511873973668646010L;

  private HuwelijksLocatieOptie optie = null;

  public DossierOmzettingOptie() {
    setId(new DossOmzetOptiePK());
  }

  @Override
  public int compareTo(DossierOmzettingOptie o) {
    return new CompareToBuilder().append(getOptie(), o.getOptie()).build();
  }

  public HuwelijksLocatieOptie getOptie() {
    if (optie == null) {
      optie = ReflectionUtil.deepCopyBean(HuwelijksLocatieOptie.class, getHuwLocatieOptie());
    }
    return optie;
  }

  public void setOptie(HuwelijksLocatieOptie optie) {
    this.optie = optie;

    setHuwLocatieOptie(ReflectionUtil.deepCopyBean(HuwLocatieOptie.class, optie));
    getId().setCHuwLocatieOptie(optie.getCodeHuwelijksLocatieOptie());
  }

  public void setDossier(DossierOmzetting dossier) {

    setDossOmzet(ReflectionUtil.deepCopyBean(DossOmzet.class, dossier));
    getId().setCDossOmzet(dossier.getCode());
  }

  public String toString() {
    return getOptie().getHuwelijksLocatieOptie();
  }
}
