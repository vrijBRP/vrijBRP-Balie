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

package nl.procura.gbaws.db.handlers;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import nl.procura.gba.jpa.personenws.db.EmailConfig;
import nl.procura.gba.jpa.personenws.utils.GbaWsJpa;
import nl.procura.gbaws.db.wrappers.EmailConfigWrapper;

public class EmailConfigDao extends GenericDao {

  public static EmailConfigWrapper getConfig() {
    return getConfigs().stream().findFirst().orElse(new EmailConfigWrapper());
  }

  public static List<EmailConfigWrapper> getConfigs() {
    final EntityManager m = GbaWsJpa.getManager();
    final TypedQuery<EmailConfig> q = m.createQuery("select p from EmailConfig p", EmailConfig.class);
    final List<EmailConfig> list = q.getResultList();
    return list.stream().map(EmailConfigWrapper::new).collect(Collectors.toList());
  }
}
