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

package nl.procura.gba.web.modules.bs.geboorte.overzicht;

import static nl.procura.gba.common.MiscUtils.to;
import static nl.procura.standard.Globalfunctions.isTru;

import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;

import nl.procura.gba.web.modules.bs.erkenning.overzicht.ErkenningOverzichtLayout;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakTabsheet;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.zkndms.ZakenDmsLayout;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.bs.algemeen.ZaakDossier;
import nl.procura.gba.web.services.bs.erkenning.ErkenningsType;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.bs.geboorte.GezinssituatieType;

public class GeboorteOverzichtBuilder {

  public static void addTab(ZaakTabsheet tabsheet, ZaakDossier zaakDossier) {

    DossierGeboorte geboorte = to(zaakDossier, DossierGeboorte.class);

    if (geboorte.getGezinssituatie() == GezinssituatieType.BINNEN_HETERO_HUWELIJK) {
      tabsheet.addTab(new GeboorteOverzichtLayout(geboorte), "Geboorte binnen huwelijk");
    } else {

      if (geboorte.getErkenningsType() == ErkenningsType.GEEN_ERKENNING) {
        tabsheet.addTab(new GeboorteOverzichtLayout(geboorte), "Geboorte buiten huwelijk, geen erkenning");
      } else {
        tabsheet.addTab(new GeboorteOverzichtLayout(geboorte), "Geboorte buiten huwelijk");
      }
    }

    if (geboorte.getVragen().heeftErkenningBijGeboorte()) {
      Component layout = new ErkenningOverzichtLayout(geboorte.getErkenningBijGeboorte());
      tabsheet.addTab(layout, "Erkenning bij aangifte geboorte");
    } else if (geboorte.getVragen().heeftErkenningVoorGeboorte()) {
      Layout layout = new ErkenningOverzichtLayout(geboorte.getErkenningVoorGeboorte());
      tabsheet.addTab(layout, "Erkenning vóór geboorte (in Proweb)");
    } else if (geboorte.getVragen().heeftErkenningBuitenProweb()) {
      Component layout = new ErkenningOverzichtLayout(geboorte.getErkenningBuitenProweb());
      tabsheet.addTab(layout, "Erkenning vóór geboorte (buiten Proweb)");
    }

    // Zaken DMS tab
    boolean isZakenDsmAan = isTru(tabsheet.getApplication().getParmValue(ParameterConstant.BSM_ZAKEN_DMS));
    boolean isZaakDmsAan = isTru(tabsheet.getApplication().getParmValue(ParameterConstant.ZAKEN_DMS_AFSTAM_GEB));

    if (isZakenDsmAan && isZaakDmsAan) {
      ZakenDmsLayout zknDmsLayout = new ZakenDmsLayout(zaakDossier.getDossier());
      zknDmsLayout.setTab(tabsheet.addTab(zknDmsLayout, "Zaaksysteem"));
    }
  }
}
