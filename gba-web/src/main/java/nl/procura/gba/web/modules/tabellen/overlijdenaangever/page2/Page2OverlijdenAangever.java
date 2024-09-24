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

package nl.procura.gba.web.modules.tabellen.overlijdenaangever.page2;

import static nl.procura.gba.web.modules.tabellen.overlijdenaangever.page2.Page2OverlijdenAangeverBean.*;
import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.INFO;

import java.util.List;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.formats.Geboorte;
import nl.procura.diensten.gba.ple.extensions.formats.Naam;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.services.gba.basistabellen.overlijdenaangever.OverlijdenAangever;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.gba.web.services.zaken.contact.ContactgegevensService;
import nl.procura.gba.web.services.zaken.contact.PlContactgegeven;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page2OverlijdenAangever extends NormalPageTemplate {

  private Form1 form1 = null;
  private Form2 form2 = null;

  private OverlijdenAangever aangever;

  public Page2OverlijdenAangever(OverlijdenAangever aangever) {

    super("Toevoegen / muteren aangever");

    this.aangever = aangever;

    addButton(buttonPrev);
    addButton(buttonNew);
    addButton(buttonSave);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      form1 = new Form1(aangever);
      form2 = new Form2(aangever);

      addComponent(form1);
      addComponent(form2);
    }

    super.event(event);
  }

  @Override
  public void onNew() {

    form1.reset();
    form2.reset();

    aangever = new OverlijdenAangever();
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @Override
  public void onSave() {

    form1.commit();
    form2.commit();

    Page2OverlijdenAangeverBean b1 = form1.getBean();
    Page2OverlijdenAangeverBean b2 = form2.getBean();

    aangever.setBurgerServiceNummer(b1.getBsn());
    aangever.setVoornamen(b2.getVoorn());
    aangever.setVoorvoegsel(astr(b2.getVoorv()));
    aangever.setTitel(b2.getTitel());

    aangever.setGeslachtsnaam(b2.getNaam());
    aangever.setGeslacht(b2.getGeslacht());
    aangever.setDatumGeboorte(b2.getGeboortedatum());
    aangever.setGeboorteland(b2.getGeboorteland());

    if (aangever.getGeboorteland() != null) {
      boolean isGebNl = Landelijk.isNederland(aangever.getGeboorteland());
      aangever.setGeboorteplaats(
          isGebNl ? b2.getGeboorteplaatsNL() : new FieldValue(-1, b2.getGeboorteplaatsBuitenland()));
      aangever.setGeboorteland(b2.getGeboorteland());
    }

    aangever.setTelefoon(b2.getTelefoon());
    aangever.setEmail(b2.getEmail());
    aangever.setDatumIngang(new DateTime(b2.getIngangGeld().getLongValue()));
    aangever.setDatumEinde(new DateTime(b2.getEindeGeld().getLongValue()));

    getServices().getOverlijdenGemeenteService().save(aangever);

    successMessage("De gegevens zijn opgeslagen.");

    super.onSave();
  }

  class Form1 extends Page2OverlijdenAangeverForm {

    public Form1(OverlijdenAangever aangever) {
      setCaption("Zoeken");
      setOrder(BSN);
      init(aangever);
    }

    @Override
    public void onOphalen(String bsn) {
      if (emp(bsn)) {
        throw new ProException(INFO, "Geen burgerservicenummer ingegeven");
      }

      form2.reset();

      BasePLExt pl = getApplication().getServices().getPersonenWsService().getPersoonslijst(bsn);

      if (pl != null) {

        Naam naam = pl.getPersoon().getNaam();
        Geboorte geboorte = pl.getPersoon().getGeboorte();

        form2.getField(VOORN).setValue(naam.getVoornamen().getValue().getVal());
        form2.getField(VOORV).setValue(new FieldValue(naam.getVoorvoegsel().getValue().getVal()));
        form2.getField(TITEL).setValue(new FieldValue(naam.getTitel().getValue().getVal()));
        form2.getField(NAAM).setValue(naam.getGeslachtsnaam().getValue().getVal());
        form2.getField(GESLACHT).setValue(Geslacht.get(pl.getPersoon().getGeslacht().getVal()));
        form2.getField(GEBOORTEDATUM).setValue(geboorte.getDatum());
        form2.getField(GEBOORTELAND).setValue(new FieldValue(geboorte.getGeboorteland().getVal()));

        String geboortePlaats = geboorte.getGeboorteplaats().getVal();

        if (pos(geboortePlaats)) {
          form2.getField(GEBOORTEPLAATS_NL).setValue(new FieldValue(geboortePlaats));
        } else {
          form2.getField(GEBOORTEPLAATS_BL).setValue(new FieldValue(geboortePlaats));
        }
        String email = "";
        String telefoon = "";

        List<PlContactgegeven> cg = getApplication().getServices().getContactgegevensService().getContactgegevens(
            pl);
        for (PlContactgegeven g : cg) {
          if (g.getContactgegeven().isGegeven(ContactgegevensService.EMAIL)) {
            email = g.getAant();
          }
          if (g.getContactgegeven().isGegeven(ContactgegevensService.TEL_MOBIEL,
              ContactgegevensService.TEL_WERK)) {
            if (fil(g.getAant())) {
              telefoon = g.getAant();
            }
          }
        }

        if (fil(email)) {
          form2.getField(EMAIL).setValue(email);
        }

        if (fil(telefoon)) {
          form2.getField(TELEFOON).setValue(telefoon);
        }
      }
    }
  }

  class Form2 extends Page2OverlijdenAangeverForm {

    public Form2(OverlijdenAangever aangever) {
      setCaption("Aangever");
      setOrder(VOORN, VOORV, TITEL, NAAM, GESLACHT, GEBOORTEDATUM, GEBOORTELAND, GEBOORTEPLAATS_NL,
          GEBOORTEPLAATS_BL, TELEFOON, EMAIL, INGANG_GELD, EINDE_GELD);
      init(aangever);
    }
  }
}
