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

package nl.procura.gba.web.modules.bs.onderzoek.page10.adresselectie.zoeken;

import static nl.procura.gba.common.MiscUtils.formatPostcode;

import java.util.List;
import java.util.stream.Collectors;

import nl.procura.diensten.gba.ple.openoffice.formats.Adresformats;
import nl.procura.diensten.gba.wk.baseWK.BaseWK;
import nl.procura.diensten.gba.wk.baseWK.BaseWKPerson;
import nl.procura.diensten.gba.wk.extensions.BaseWKExt;
import nl.procura.java.reflection.ReflectionUtil;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SelectieAdres extends BaseWK {

  private String adres;

  public SelectieAdres(BaseWKExt wk) {
    update(wk);
  }

  public void update(BaseWKExt wk) {

    BaseWK bwk = wk.getBasisWk();

    try {
      ReflectionUtil.deepCopyBean(this, bwk);
    } catch (Exception e) {
      e.printStackTrace();
    }

    Adresformats af = new Adresformats();
    String gemeentedeel = bwk.getGemeentedeel().getDescr();
    String woonplaats = bwk.getWoonplaats().getDescr();

    af.setValues(bwk.getStraat().getDescr(), bwk.getHuisnummer().getDescr(),
        bwk.getHuisletter().getDescr(), bwk.getToevoeging().getDescr(),
        bwk.getAanduiding().getDescr(), bwk.getLocatie().getDescr(),
        formatPostcode(bwk.getPostcode().getDescr()), gemeentedeel, woonplaats, "", "", "", "", "",
        "", "");

    adres = af.getAdres_pc_wpl_gem();
  }

  public List<BaseWKPerson> getBewoners() {
    return getPersonen().stream()
        .filter(BaseWKPerson::isCurrentResident)
        .collect(Collectors.toList());
  }

  @Override
  public String toString() {
    return getAdres();
  }
}
