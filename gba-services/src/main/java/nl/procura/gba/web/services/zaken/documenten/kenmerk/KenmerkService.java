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

package nl.procura.gba.web.services.zaken.documenten.kenmerk;

import static nl.procura.gba.common.MiscUtils.copyList;
import static nl.procura.gba.common.MiscUtils.sort;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.procura.gba.jpa.personen.dao.DocumentDao;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.zaken.documenten.KoppelbaarAanDocumentKenmerk;

public class KenmerkService extends AbstractService {

  public KenmerkService() {
    super("Kenmerken");
  }

  @Transactional
  @ThrowException("Fout bij het verwijderen van kenmerk")
  public void delete(DocumentKenmerk kenmerk) {
    removeEntity(kenmerk);
  }

  public Set<DocumentKenmerk> getKenmerken() {
    return new HashSet<>(sort(copyList(DocumentDao.findKenmerken(), DocumentKenmerk.class)));
  }

  @Transactional
  @ThrowException("Fout bij het koppelen van record")
  public void koppelActie(List<? extends KoppelbaarAanDocumentKenmerk> koppelList,
      List<DocumentKenmerk> poList,
      KoppelActie koppelActie) {

    for (KoppelbaarAanDocumentKenmerk koppelObject : koppelList) {
      for (DocumentKenmerk documentKenmerk : poList) {
        if (koppelActie.isPossible(koppelObject.isGekoppeld(documentKenmerk))) {
          koppelObject.koppelActie(documentKenmerk, koppelActie);
          documentKenmerk.koppelActie(koppelObject, koppelActie);
          saveEntity(documentKenmerk);
        }
      }

      saveEntity(koppelObject);
    }
  }

  @Transactional
  @ThrowException("Fout bij het opslaan van kenmerk")
  public void save(DocumentKenmerk kenmerk) {
    saveEntity(kenmerk);
  }
}
