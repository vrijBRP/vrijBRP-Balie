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

package nl.procura.gba.web.services.zaken.documenten;

import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.zaken.algemeen.koppelenumeratie.KoppelEnumeratie;

public class KoppelEnumeratieService extends AbstractService {

  public KoppelEnumeratieService() {
    super("Koppelenum");
  }

  @Transactional
  @ThrowException("Fout bij het opslaan van de gekoppelende elementen")
  public void save(KoppelEnumeratie koppelenum) {
    saveEntity(koppelenum);
  }

  @Transactional
  @ThrowException("Fout bij het verwijderen van gekoppelende elementen")
  public void delete(KoppelEnumeratie koppelenum) {
    removeEntity(koppelenum);
  }
}
