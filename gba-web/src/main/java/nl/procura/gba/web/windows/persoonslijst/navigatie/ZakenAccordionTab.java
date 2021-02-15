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

package nl.procura.gba.web.windows.persoonslijst.navigatie;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.modules.zaken.afstamming.ModuleAfstamming;
import nl.procura.gba.web.modules.zaken.correspondentie.ModuleCorrespondentie;
import nl.procura.gba.web.modules.zaken.document.ModuleDocumenten;
import nl.procura.gba.web.modules.zaken.geheim.ModuleGeheim;
import nl.procura.gba.web.modules.zaken.gpk.ModuleGpk;
import nl.procura.gba.web.modules.zaken.gv.ModuleGv;
import nl.procura.gba.web.modules.zaken.huwelijk.ModuleHuwelijk;
import nl.procura.gba.web.modules.zaken.indicatie.ModuleIndicatie;
import nl.procura.gba.web.modules.zaken.inhouding.ModuleInhouding;
import nl.procura.gba.web.modules.zaken.naamgebruik.ModuleNaamgebruik;
import nl.procura.gba.web.modules.zaken.onderzoek.ModuleOnderzoek;
import nl.procura.gba.web.modules.zaken.overlijden.ModuleOverlijden;
import nl.procura.gba.web.modules.zaken.registration.ModuleRegistration;
import nl.procura.gba.web.modules.zaken.reisdocument.ModuleReisdocument;
import nl.procura.gba.web.modules.zaken.rijbewijs.ModuleRijbewijs;
import nl.procura.gba.web.modules.zaken.riskanalysis.ModuleRiskAnalysis;
import nl.procura.gba.web.modules.zaken.tmv.ModuleTmv;
import nl.procura.gba.web.modules.zaken.uittreksel.ModuleUittreksel;
import nl.procura.gba.web.modules.zaken.verhuizing.ModuleVerhuizing;
import nl.procura.gba.web.modules.zaken.vog.ModuleVog;

public class ZakenAccordionTab extends PlAccordionTab {

  private static final long serialVersionUID = 3373738303702313147L;

  public ZakenAccordionTab(GbaApplication application) {

    super("Zaken", application);

    addLink(ModuleAfstamming.class);
    addLink(ModuleCorrespondentie.class);
    addLink(ModuleDocumenten.class);
    addLink(ModuleRegistration.class);
    addLink(ModuleGpk.class);
    addLink(ModuleGv.class);
    addLink(ModuleHuwelijk.class);
    addLink(ModuleIndicatie.class);
    addLink(ModuleInhouding.class);
    addLink(ModuleNaamgebruik.class);
    addLink(ModuleOnderzoek.class);
    addLink(ModuleOverlijden.class);
    addLink(ModuleReisdocument.class);
    addLink(ModuleRijbewijs.class);
    addLink(ModuleRiskAnalysis.class);
    addLink(ModuleTmv.class);
    addLink(ModuleUittreksel.class);
    addLink(ModuleVerhuizing.class);
    addLink(ModuleGeheim.class);
    addLink(ModuleVog.class);
  }

  @Override
  protected boolean isLinkAllowed(GbaApplication application, ZaakType[] zaakTypes) {

    BasePLExt pl = application.getServices().getPersonenWsService().getHuidige();

    if (pl.heeftVerwijzing() || pl.getDatasource() == PLEDatasource.GBAV) {

      for (ZaakType zaakType : zaakTypes) {

        switch (zaakType) {

          case GPK:
          case INHOUD_VERMIS:
          case INDICATIE:
          case REISDOCUMENT:
          case COVOG:
            return false;

          default:
            break;
        }
      }
    }

    return true;
  }
}
