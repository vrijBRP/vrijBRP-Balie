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

package nl.procura.gba.web.common.database.checks;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;

import nl.procura.gba.config.GbaConfig;
import nl.procura.gba.config.GbaConfigProperty;
import nl.procura.gba.jpa.personen.db.Usr;
import nl.procura.gba.jpa.personen.db.UsrPwHist;
import nl.procura.gba.web.common.database.DBCheckTemplateLb;
import nl.procura.gba.web.services.beheer.gebruiker.Version0PasswordEncryptor;
import nl.procura.gba.web.services.beheer.gebruiker.Version1PasswordEncryptor;

import liquibase.database.Database;

public class DBCheckPost6 extends DBCheckTemplateLb {

  public DBCheckPost6(EntityManager entityManager, Database database, String type) {
    super(entityManager, database, type, "Opschonen wachtwoord resets");
  }

  @Override
  public void init() {
    deleteResetPw();
    deleteOldPasswords();
    updatePasswords();
  }

  /**
   * Verwijdert alle wachtwoorden behalve de laatste 10 per gebruiker
   */
  private void deleteOldPasswords() {
    List<Usr> resultList = getEntityManager().createQuery("select u from Usr u", Usr.class).getResultList();
    for (Usr usr : resultList) {
      int count = 0;
      for (UsrPwHist pw : usr.getUsrPwHists()) {
        count++;
        if (count > 10) {
          getEntityManager().remove(pw);
        }
      }
    }
  }

  private void deleteResetPw() {
    count(getEntityManager().createQuery("delete from UsrPwHist u where u.resetPw > 0",
        UsrPwHist.class).executeUpdate());
  }

  private void updatePasswords() {
    // Update version 0 to version 1
    String version0Key = GbaConfig.get(GbaConfigProperty.ENCRYPTION_KEY0);
    if (StringUtils.isNotBlank(version0Key)) {
      String query = "select u from UsrPwHist u where u.pwVersion = 0";
      List<UsrPwHist> usrPwHists = getEntityManager().createQuery(query, UsrPwHist.class).getResultList();
      if (!usrPwHists.isEmpty()) {
        Version0PasswordEncryptor version0PasswordEncryptor = new Version0PasswordEncryptor();
        Version1PasswordEncryptor version1PasswordEncryptor = new Version1PasswordEncryptor();
        for (UsrPwHist usrPwHist : usrPwHists) {
          String pw = version0PasswordEncryptor.decrypt(usrPwHist.getPw());
          usrPwHist.setPw(version1PasswordEncryptor.encrypt(pw));
          usrPwHist.setPwVersion(BigDecimal.valueOf(1L));
          getEntityManager().merge(usrPwHist);
        }
      }

      // Remove version 0 key from properties
      GbaConfig.getProperties().remove(GbaConfigProperty.ENCRYPTION_KEY0.getProperty());
      GbaConfig.storeProperties();
    }

    // Update version 1 to new version 1
    String version1KeyUpdate = GbaConfig.get(GbaConfigProperty.ENCRYPTION_KEY1_UPDATE);
    if (StringUtils.isNotBlank(version1KeyUpdate)) {
      Version1PasswordEncryptor currentEncryptor = new Version1PasswordEncryptor();
      Version1PasswordEncryptor updateEncryptor = new Version1PasswordEncryptor(version1KeyUpdate);
      String query = "select u from UsrPwHist u where u.pwVersion = 1";
      List<UsrPwHist> usrPwHists = getEntityManager().createQuery(query, UsrPwHist.class).getResultList();
      if (!usrPwHists.isEmpty()) {
        for (UsrPwHist usrPwHist : usrPwHists) {
          String pw = currentEncryptor.decrypt(usrPwHist.getPw());
          usrPwHist.setPw(updateEncryptor.encrypt(pw));
          getEntityManager().merge(usrPwHist);
        }
      }
      GbaConfig.getProperties().remove(GbaConfigProperty.ENCRYPTION_KEY1_UPDATE.getProperty());
      GbaConfig.getProperties().setProperty(GbaConfigProperty.ENCRYPTION_KEY1.getProperty(), version1KeyUpdate);
      GbaConfig.storeProperties();
    }
  }
}
