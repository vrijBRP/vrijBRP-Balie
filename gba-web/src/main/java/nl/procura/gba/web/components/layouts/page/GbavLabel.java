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

package nl.procura.gba.web.components.layouts.page;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.gba.web.application.GbaApplication;

public class GbavLabel extends MainLabel {

  public GbavLabel(GbaApplication application) {
    super(application, "Landelijk");
  }

  @Override
  public void doCheck() {
    BasePLExt pl = getServices().getPersonenWsService().getHuidige();
    if (pl.getDatasource() == PLEDatasource.GBAV) {
      setAdd(true);
      setDescription("Deze persoonlijst is gevonden in de landelijke database.");
    } else if (pl.heeftVerwijzing()) {
      setValue("Archief persoonslijst");
      setDescription("Deze persoonlijst is gevonden in het archief van de gemeentelijke database.");
      setAdd(true);
    }
  }
}
