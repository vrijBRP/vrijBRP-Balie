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

import java.util.List;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;

import nl.procura.gbaws.db.enums.PersonenWsDatabaseType;
import nl.procura.gbaws.db.handlers.ProfileDao;
import nl.procura.gbaws.db.wrappers.GbavProfileWrapper;
import nl.procura.gbaws.db.wrappers.ProfileWrapper;
import nl.procura.gbaws.web.vaadin.layouts.tables.PersonenWsTable;

@SuppressWarnings("serial")
public abstract class Page1AuthProfileTable extends PersonenWsTable {

  public Page1AuthProfileTable() {
  }

  @Override
  public void setColumns() {

    setSelectable(true);
    setMultiSelect(true);

    addColumn("Profiel");
    addColumn("Procura", 90).setClassType(Component.class);
    addColumn("GBA-V", 90).setClassType(Component.class);

    super.setColumns();
  }

  @Override
  public void setRecords() {

    List<ProfileWrapper> profiles = ProfileDao.getProfiles();

    for (ProfileWrapper profile : profiles) {

      Record r = addRecord(profile);
      r.addValue(profile.toString());
      r.addValue(getNewButton("Procura DB", profile));
      r.addValue(getNewButton("GBA-V", profile, profile.getGBAvProfiel()));
    }

    super.setRecords();
  }

  public abstract void onClick(ElementButton button);

  public ElementButton getNewButton(String caption, ProfileWrapper profile) {

    ElementButton b = new ElementButton(caption);
    b.addListener(new newButtonListener(b));

    b.getElementsProfile().setProfile(profile);
    b.getElementsProfile().setDatabaseType(PersonenWsDatabaseType.PROCURA);
    return b;
  }

  public ElementButton getNewButton(String caption, ProfileWrapper profile, GbavProfileWrapper gbav) {

    ElementButton b = getNewButton(caption, profile);

    ElementsProfile ep = b.getElementsProfile();
    ep.setDatabaseType(PersonenWsDatabaseType.GBA_V);

    if (gbav != null && gbav.getPk() != null) {
      ep.setRefDatabase(gbav.getPk());
      ep.setGbavProfile(gbav);
    } else {
      b.setEnabled(false);
    }

    return b;
  }

  public class newButtonListener implements Button.ClickListener {

    private final ElementButton button;

    public newButtonListener(ElementButton button) {
      this.button = button;
    }

    @Override
    public void buttonClick(ClickEvent event) {
      onClick(button);
    }
  }
}
