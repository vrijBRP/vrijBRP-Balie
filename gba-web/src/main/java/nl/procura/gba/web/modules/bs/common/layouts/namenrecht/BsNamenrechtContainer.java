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

package nl.procura.gba.web.modules.bs.common.layouts.namenrecht;

import static nl.procura.gba.common.MiscUtils.to;

import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.modules.bs.common.utils.BsRechtContainer;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.interfaces.DossierNamenrecht;
import nl.procura.gba.web.services.bs.erkenning.DossierErkenning;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.bs.naamskeuze.DossierNaamskeuze;

public class BsNamenrechtContainer extends BsRechtContainer {

  public BsNamenrechtContainer(Services services, DossierNamenrecht dossier) {
    super(services);
    add(Landelijk.getNederland());

    if (dossier instanceof DossierNaamskeuze) {
      DossierNaamskeuze naamskeuze = to(dossier, DossierNaamskeuze.class);
      naamskeuze.getKinderen().forEach(this::addPersoonNationaliteiten);
      addPersoonNationaliteiten(naamskeuze.getPartner());
      addPersoonNationaliteiten(naamskeuze.getMoeder());

    } else if (dossier instanceof DossierErkenning) {
      DossierErkenning erkenning = to(dossier, DossierErkenning.class);
      erkenning.getKinderen().forEach(this::addPersoonNationaliteiten);

    } else if (dossier instanceof DossierGeboorte) {
      DossierGeboorte geboorte = to(dossier, DossierGeboorte.class);
      if (geboorte.getVragen().heeftErkenningVoorGeboorte()) {
        add(geboorte.getErkenningVoorGeboorte().getLandNaamRecht());
      }
    }

    addDossierNationaliteiten(dossier);
  }
}
