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

package nl.procura.gba.web.modules.zaken.reisdocument.page11;

import static nl.procura.standard.Globalfunctions.aval;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.formats.BurgerlijkeStaat;
import nl.procura.gba.web.services.zaken.reisdocumenten.clausule.VermeldPartnerType;
import nl.procura.vaadin.component.container.ArrayListContainer;

public class PartnervermeldContainer extends ArrayListContainer {

  private VermeldPartnerType standaardWaarde = null;

  public PartnervermeldContainer(BasePLExt pl) {

    BurgerlijkeStaat bs = pl.getPersoon().getBurgerlijkeStand();

    if (aval(bs.getStatus().getType().getCode()) > 1) {
      addItem(VermeldPartnerType.PARTNER_NIET_VERMELDEN);
    }

    switch (aval(bs.getStatus().getType().getCode())) {

      case 2:
        addItem(VermeldPartnerType.ECHTGENOOT_VAN);
        break;

      case 3:
        addItem(VermeldPartnerType.GESCHEIDEN_VAN);
        break;

      case 4:
        addItem(VermeldPartnerType.WEDUWNAAR_VAN);
        break;

      case 5:
        addItem(VermeldPartnerType.GEREGISTREERD_PARTNER_VAN);
        break;

      case 6:
        addItem(VermeldPartnerType.GEREGISTREERD_PARTNER_GEWEEST_VAN);
        break;

      case 7:
        addItem(VermeldPartnerType.ACHTERGEBLEVEN_GEREGISTREERD_PARTNER_VAN);
        break;

      default:
        addItem(VermeldPartnerType.NIET_VAN_TOEPASSING);
        break;
    }

    if (pl.getPersoon().getNaam().isEigenNaam()) {
      standaardWaarde = (VermeldPartnerType) getIdByIndex(0);
    } else {
      if (getItemIds().size() > 1) {
        standaardWaarde = (VermeldPartnerType) getIdByIndex(getItemIds().size() - 1);
      }
    }
  }

  public VermeldPartnerType getStandaardWaarde() {
    return standaardWaarde;
  }
}
