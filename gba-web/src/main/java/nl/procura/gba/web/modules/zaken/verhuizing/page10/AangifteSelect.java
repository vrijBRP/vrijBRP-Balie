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

package nl.procura.gba.web.modules.zaken.verhuizing.page10;

import nl.procura.gba.web.components.containers.GbaContainer;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.services.gba.ple.relatieLijst.AangifteSoort;

public class AangifteSelect extends GbaNativeSelect {

  public AangifteSelect() {

    setNullSelectionAllowed(false);
    setWidth("100%");
    setImmediate(true);
    setDataSource(new AangifteContainer());
  }

  public AangifteSelect(AangifteSoort soort) {

    this();
    setValue(soort);
  }

  class AangifteContainer extends GbaContainer {

    public AangifteContainer() {

      addItem(AangifteSoort.AMBTSHALVE);
      addItem(AangifteSoort.ECHTGENOOT_GEREGISTREERD_PARTNER);
      addItem(AangifteSoort.GEZAGHOUDER);
      addItem(AangifteSoort.HOOFDINSTELLING);
      addItem(AangifteSoort.INFRASTRUCTURELE_WIJZIGING);
      addItem(AangifteSoort.INGESCHREVENE);
      addItem(AangifteSoort.INWONENDE_OUDER_VOOR_MEERDERJARIG_KIND);
      addItem(AangifteSoort.MEERDERJARIG_INWONEND_KIND_VOOR_OUDER);
      addItem(AangifteSoort.MEERDERJARIGE_GEMACHTIGDE);
      addItem(AangifteSoort.MINISTERIEELBESLUIT);
      addItem(AangifteSoort.TECHNISCHE_WIJZIGING_IVM_BAG);
    }
  }
}
