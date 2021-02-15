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

package nl.procura.gba.web.services.zaken.documenten.stempel;

import static nl.procura.gba.common.MiscUtils.copyList;

import java.util.List;

import nl.procura.gba.jpa.personen.dao.DocumentDao;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.zaken.documenten.KoppelbaarAanDocumentStempel;

public class StempelService extends AbstractService {

  public StempelService() {
    super("Stempels");
  }

  @Transactional
  @ThrowException("Fout bij het opslaan van stempel")
  public void save(DocumentStempel stempel) {
    saveEntity(stempel);
  }

  @Transactional
  @ThrowException("Fout bij het verwijderen van stempels")
  public void delete(DocumentStempel stempel) {
    removeEntity(stempel);
  }

  public List<DocumentStempel> getStempels() {
    return copyList(DocumentDao.findStempels(), DocumentStempel.class);
  }

  @Transactional
  @ThrowException("Fout bij het koppelen van record")
  public void koppelActie(List<? extends KoppelbaarAanDocumentStempel> koppelList,
      List<DocumentStempel> poList,
      KoppelActie koppelActie) {

    for (KoppelbaarAanDocumentStempel koppelObject : koppelList) {
      for (DocumentStempel printOptie : poList) {
        if (koppelActie.isPossible(koppelObject.isGekoppeld(printOptie))) {
          koppelObject.koppelActie(printOptie, koppelActie);
          printOptie.koppelActie(koppelObject, koppelActie);
          saveEntity(printOptie);
        }
      }

      saveEntity(koppelObject);
    }
  }
}
