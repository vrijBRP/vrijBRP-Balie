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

package nl.procura.gba.web.modules.bs.naamskeuze.overzicht;

import static nl.procura.gba.common.MiscUtils.to;
import static nl.procura.standard.Globalfunctions.isTru;

import nl.procura.gba.web.modules.bs.geboorte.overzicht.GeboorteOverzichtLayout;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakTabsheet;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.zkndms.ZakenDmsLayout;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.bs.algemeen.ZaakDossier;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.bs.naamskeuze.DossierNaamskeuze;

public class NaamskeuzeOverzichtBuilder {

  public static void addTab(ZaakTabsheet tabsheet, ZaakDossier zaakDossier) {

    DossierNaamskeuze dossierNaamskeuze = to(zaakDossier, DossierNaamskeuze.class);
    tabsheet.addTab(new NaamskeuzeOverzichtLayout(dossierNaamskeuze),
        dossierNaamskeuze.getNaamskeuzeTypeOmschrijving());

    // Zoek de geboorte na deze naamskeuze
    if (dossierNaamskeuze.isVoorGeboorte()) {
      DossierGeboorte dossierGeboorte = tabsheet.getApplication().getServices().getNaamskeuzeService()
          .getGekoppeldeGeboorte(dossierNaamskeuze);
      if (dossierGeboorte != null) {
        tabsheet.addTab(new GeboorteOverzichtLayout(dossierGeboorte), "Geboorte");
      }
    }

    // Zaken DMS tab
    boolean isZakenDsmAan = isTru(tabsheet.getApplication().getParmValue(ParameterConstant.BSM_ZAKEN_DMS));
    boolean isZaakDmsAan = isTru(tabsheet.getApplication().getParmValue(ParameterConstant.ZAKEN_DMS_AFSTAM_NK));

    if (isZakenDsmAan && isZaakDmsAan) {
      ZakenDmsLayout zknDmsLayout = new ZakenDmsLayout(zaakDossier.getDossier());
      zknDmsLayout.setTab(tabsheet.addTab(zknDmsLayout, "Zaaksysteem"));
    }
  }
}
