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

package nl.procura.gbaws.web.vaadin.module.auth.profile.page1;

import nl.procura.gbaws.db.enums.PersonenWsDatabaseType;
import nl.procura.gbaws.db.wrappers.GbavProfileWrapper;
import nl.procura.gbaws.db.wrappers.ProfileWrapper;

public class ElementsProfile {

  private ProfileWrapper         profile      = null;
  private PersonenWsDatabaseType databaseType = PersonenWsDatabaseType.PROCURA;
  private int                    refDatabase  = 0;
  private GbavProfileWrapper     gbavProfile  = null;

  public ProfileWrapper getProfile() {
    return profile;
  }

  public void setProfile(ProfileWrapper profile) {
    this.profile = profile;
  }

  public PersonenWsDatabaseType getDatabaseType() {
    return databaseType;
  }

  public void setDatabaseType(PersonenWsDatabaseType databaseType) {
    this.databaseType = databaseType;
  }

  public int getRefDatabase() {
    return refDatabase;
  }

  public void setRefDatabase(int refDatabase) {
    this.refDatabase = refDatabase;
  }

  public GbavProfileWrapper getGbavProfile() {
    return gbavProfile;
  }

  public void setGbavProfile(GbavProfileWrapper gbavProfile) {
    this.gbavProfile = gbavProfile;
  }
}
