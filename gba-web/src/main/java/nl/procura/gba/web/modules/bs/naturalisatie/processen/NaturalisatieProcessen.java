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

package nl.procura.gba.web.modules.bs.naturalisatie.processen;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.modules.bs.common.BsProcessen;
import nl.procura.gba.web.modules.bs.naturalisatie.page10.Page10Naturalisatie;
import nl.procura.gba.web.modules.bs.naturalisatie.page20.Page20Naturalisatie;
import nl.procura.gba.web.modules.bs.naturalisatie.page40.Page40Naturalisatie;
import nl.procura.gba.web.modules.bs.naturalisatie.page50.Page50Naturalisatie;
import nl.procura.gba.web.modules.bs.naturalisatie.page60.Page60Naturalisatie;
import nl.procura.gba.web.modules.bs.naturalisatie.page70.Page70Naturalisatie;
import nl.procura.gba.web.services.bs.algemeen.Dossier;

public class NaturalisatieProcessen extends BsProcessen {

  public NaturalisatieProcessen(Dossier dossier, GbaApplication gbaApplication) {
    this(gbaApplication);
    setDossier(dossier);
  }

  private NaturalisatieProcessen(GbaApplication gbaApplication) {
    super(gbaApplication);

    addProces("Procedurekeuze", Page10Naturalisatie.class);
    addProces("Optie / naturalisatie", Page20Naturalisatie.class);
    addProces("Toetsing", Page40Naturalisatie.class);
    addProces("Behandeling", Page50Naturalisatie.class);
    addProces("Ceremonie", Page60Naturalisatie.class);
    addProces("Overzicht", Page70Naturalisatie.class);
  }

  @Override
  public void updateStatus() {
  }
}
