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

package nl.procura.gbaws.db.wrappers;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import nl.procura.gba.jpa.personenws.db.EmailConfig;
import nl.procura.gba.jpa.personenws.db.EmailLog;
import nl.procura.gba.jpa.personenws.utils.GbaWsJpa;
import nl.procura.gbaws.mail.Email;

public class EmailConfigWrapper extends AbstractTableWrapper<EmailConfig, Integer> {

  public EmailConfigWrapper() {
    super(new EmailConfig());
  }

  public EmailConfigWrapper(int code) {
    this(GbaWsJpa.getManager().find(EmailConfig.class, code));
  }

  public EmailConfigWrapper(EmailConfig profile) {
    super(profile);
  }

  @Override
  public Integer getPk() {
    return getTable().getCEmailConfig();
  }

  public void delete(EntityManager m) {
    m.remove(getTable());
  }

  public void deleteAndCommit() {

    final EntityManager m = GbaWsJpa.getManager();

    m.getTransaction().begin();

    final EmailConfig o = m.find(EmailConfig.class, getPk());

    m.remove(o);

    m.getTransaction().commit();
  }

  public void merge(EntityManager m) {
    setTable(merge(m, getTable()));
  }

  public void saveAndCommit() {

    final EntityManager m = GbaWsJpa.getManager();

    m.getTransaction().begin();

    merge(m);

    m.getTransaction().commit();
  }

  public List<EmailLogWrapper> getLogs() {

    final List<EmailLogWrapper> l = new ArrayList<>();

    final TypedQuery<EmailLog> query = GbaWsJpa.getManager().createQuery(
        "select g from EmailLog g order by g.dIn desc", EmailLog.class);
    final List<EmailLog> list = query.getResultList();

    for (final EmailLog log : list) {
      l.add(new EmailLogWrapper(log));
    }

    return l;
  }

  public boolean isTypeFoutWW() {
    return getTable().getTypes().contains(Email.TYPE_FOUT_WW);
  }

  public boolean isTypeNieuwWW() {
    return getTable().getTypes().contains(Email.TYPE_NIEUW_WW);
  }
}
