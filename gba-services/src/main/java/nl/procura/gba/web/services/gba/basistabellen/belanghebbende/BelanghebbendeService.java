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

package nl.procura.gba.web.services.gba.basistabellen.belanghebbende;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.jpa.personen.dao.BelanghDao;
import nl.procura.gba.web.common.tables.GbaTables;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.interfaces.GeldigheidStatus;

public class BelanghebbendeService extends AbstractService {

  public BelanghebbendeService() {
    super("Belanghebbende");
  }

  @ThrowException("Fout bij ophalen records")
  public List<Belanghebbende> getBelanghebbenden(BelanghebbendeType type, GeldigheidStatus status) {
    List<Belanghebbende> belanghebbenden = new ArrayList<>();
    for (Belanghebbende belanghebbendeImpl : MiscUtils.copyList(BelanghDao.find(), Belanghebbende.class)) {
      if (type == null || (belanghebbendeImpl.getBelanghebbendeType() == type)) {
        if (belanghebbendeImpl.getGeldigheidStatus().is(status)) {
          belanghebbendeImpl.setLand(GbaTables.LAND.get(belanghebbendeImpl.getcLand()));
          belanghebbenden.add(belanghebbendeImpl);
        }
      }
    }

    return belanghebbenden;
  }

  @ThrowException("Fout bij ophalen records")
  public List<Belanghebbende> getBelanghebbenden(GeldigheidStatus status) {
    return getBelanghebbenden(null, status);
  }

  @ThrowException("Fout bij het opslaan van het record")
  @Transactional
  public void save(Belanghebbende belanghebbende) {
    saveEntity(belanghebbende);
  }

  @ThrowException("Fout bij het verwijderen van het record")
  @Transactional
  public void delete(Belanghebbende belanghebbende) {
    removeEntity(belanghebbende);
  }
}
