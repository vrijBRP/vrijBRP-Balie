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

package nl.procura.gba.web.services.zaken.algemeen.zaakconfiguraties;

import static nl.procura.gba.common.MiscUtils.copyList;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.db.ZaakConf;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Transactional;

public class ZaakConfiguratieService extends AbstractService {

  public ZaakConfiguratieService() {
    super("Zaakconfiguraties");
  }

  @ThrowException("Fout bij ophalen zaakconfiguraties")
  public List<ZaakConfiguratie> getConfiguraties() {
    return copyList(findEntity(new ZaakConf()), ZaakConfiguratie.class);
  }

  public Set<ZaakConfiguratie> getZaakConfiguraties(ZaakType zaakType) {
    return getServices()
        .getGebruiker()
        .getProfielen()
        .getAlle()
        .stream()
        .flatMap(profiel -> profiel
            .getZaakConfiguraties()
            .getAlle()
            .stream()
            .filter(conf -> conf.isZaakType(zaakType)))
        .collect(Collectors.toSet());
  }

  @Transactional
  @ThrowException("Fout bij opslaan zaakconfiguratie")
  public void save(ZaakConfiguratie configuratie) {
    saveEntity(configuratie);
  }

  @Transactional
  @ThrowException("Fout bij verwijderen zaakconfiguratie")
  public void delete(ZaakConfiguratie configuratie) {
    removeEntity(configuratie);
  }
}
