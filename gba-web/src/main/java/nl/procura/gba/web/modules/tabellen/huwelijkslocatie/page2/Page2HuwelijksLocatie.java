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

package nl.procura.gba.web.modules.tabellen.huwelijkslocatie.page2;

import java.util.Collections;
import java.util.List;

import com.vaadin.ui.Embedded;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.TableImage;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.gba.basistabellen.huwelijkslocatie.HuwelijksLocatie;
import nl.procura.gba.web.services.gba.basistabellen.huwelijkslocatie.HuwelijksLocatieContactpersoon;
import nl.procura.gba.web.services.gba.basistabellen.huwelijkslocatieOptie.HuwelijksLocatieOptie;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.HLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.theme.twee.Icons;

public class Page2HuwelijksLocatie extends NormalPageTemplate {

  private Page2HuwelijksLocatieForm  form1 = null;
  private Page2HuwelijksLocatieForm2 form2 = null;

  private HuwelijksLocatie locatie;
  private Table1           table1 = null;

  public Page2HuwelijksLocatie(HuwelijksLocatie locatie) {

    super("Toevoegen / muteren locatie");

    this.locatie = locatie;

    setSpacing(true);

    addButton(buttonPrev);
    addButton(buttonNew);
    addButton(buttonSave);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      form1 = new Page2HuwelijksLocatieForm(locatie);
      form2 = new Page2HuwelijksLocatieForm2(locatie.getContactpersoon());
      table1 = new Table1();

      addComponent(new HLayout(form1, form2).widthFull());
      addComponent(new Fieldset("Selecteer de opties die van toepassing zijn op deze locatie"));
      addExpandComponent(table1);
    }

    super.event(event);
  }

  @Override
  public void onNew() {

    form1.reset();
    form2.reset();

    locatie = new HuwelijksLocatie();

    table1.init();
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @Override
  public void onSave() {

    form1.commit();
    form2.commit();

    // Locatie
    Page2HuwelijksLocatieBean b1 = form1.getBean();
    locatie.setHuwelijksLocatie(b1.getLocatie());
    locatie.setLocatieSoort(b1.getSoort());
    locatie.setToelichting(b1.getToelichting());
    locatie.setAlias(b1.getAlias());
    locatie.setDatumIngang(new DateTime(b1.getIngangGeld().getLongValue()));
    locatie.setDatumEinde(new DateTime(b1.getEindeGeld().getLongValue()));

    // Contactpersoon
    Page2HuwelijksLocatieBean2 b2 = form2.getBean();
    HuwelijksLocatieContactpersoon contactpersoon = locatie.getContactpersoon();
    contactpersoon.setTerAttentieVanAanhef(b2.getTavAanhef());
    contactpersoon.setTavVoorl(b2.getTavVoorl());
    contactpersoon.setTavNaam(b2.getTavNaam());
    contactpersoon.setAdres(b2.getAdres());
    contactpersoon.setPostcode(b2.getPostcode());
    contactpersoon.setPlaats(b2.getPlaats());
    contactpersoon.setLand(b2.getLand());
    contactpersoon.setTelefoon(b2.getTelefoon());
    contactpersoon.setEmail(b2.getEmail());

    getServices().getHuwelijkService().save(locatie);

    successMessage("De gegevens zijn opgeslagen.");

    super.onSave();
  }

  private boolean isLinked(HuwelijksLocatieOptie op) {

    for (HuwelijksLocatieOptie o : locatie.getOpties()) {

      if (op.getCodeHuwelijksLocatieOptie().equals(o.getCodeHuwelijksLocatieOptie())) {

        return true;
      }
    }

    return false;
  }

  class Table1 extends GbaTable {

    @Override
    public void onClick(Record record) {

      HuwelijksLocatieOptie opt = (HuwelijksLocatieOptie) record.getObject();

      if (locatie.getOpties().contains(opt)) {

        locatie.getOpties().remove(opt);

      } else {
        locatie.getOpties().add(opt);
      }

      getServices().getHuwelijkService().save(locatie);

      init();

      super.onClick(record);
    }

    @Override
    public void setColumns() {

      setClickable(true);

      addColumn("&nbsp;", 20).setClassType(Embedded.class);
      addColumn("Optie");

      super.setColumns();
    }

    @Override
    public void setRecords() {

      List<HuwelijksLocatieOptie> opties = getServices().getHuwelijkService().getHuwelijksLocatieOpties();

      Collections.sort(opties);

      for (HuwelijksLocatieOptie o : opties) {

        Record r = addRecord(o);

        if (isLinked(o)) {

          r.addValue(new TableImage(Icons.getIcon(Icons.ICON_OK)));
        } else {
          r.addValue(new TableImage(Icons.getIcon(Icons.ICON_ERROR)));
        }

        r.addValue(o.getHuwelijksLocatieOptieOms());
      }

      super.setRecords();
    }
  }
}
