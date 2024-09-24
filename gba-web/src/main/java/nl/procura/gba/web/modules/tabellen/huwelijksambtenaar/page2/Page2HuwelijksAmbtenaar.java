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

package nl.procura.gba.web.modules.tabellen.huwelijksambtenaar.page2;

import static nl.procura.gba.web.modules.tabellen.huwelijksambtenaar.page2.Page2HuwelijksAmbtenaarBean.*;
import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.INFO;

import java.util.List;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.formats.Naam;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.services.gba.basistabellen.huwelijksambtenaar.HuwelijksAmbtenaar;
import nl.procura.gba.web.services.zaken.contact.ContactgegevensService;
import nl.procura.gba.web.services.zaken.contact.PlContactgegeven;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page2HuwelijksAmbtenaar extends NormalPageTemplate {

  private Form1 form1 = null;
  private Form2 form2 = null;

  private HuwelijksAmbtenaar ambtenaar;

  public Page2HuwelijksAmbtenaar(HuwelijksAmbtenaar ambtenaar) {

    super("Toevoegen / muteren ambtenaar");

    this.ambtenaar = ambtenaar;

    addButton(buttonPrev);
    addButton(buttonNew);
    addButton(buttonSave);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      form1 = new Form1(ambtenaar);
      form2 = new Form2(ambtenaar);

      addComponent(form1);
      addComponent(form2);
    }

    super.event(event);
  }

  @Override
  public void onNew() {

    form1.reset();
    form2.reset();

    ambtenaar = new HuwelijksAmbtenaar();
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @Override
  public void onSave() {

    form1.commit();
    form2.commit();

    Page2HuwelijksAmbtenaarBean b1 = form1.getBean();
    Page2HuwelijksAmbtenaarBean b2 = form2.getBean();

    ambtenaar.setBurgerServiceNummer(b1.getBsn());
    ambtenaar.setTelefoon(b2.getTelefoon());
    ambtenaar.setEmail(b2.getEmail());
    ambtenaar.setHuwelijksAmbtenaar(b2.getNaam());
    ambtenaar.setAlias(b2.getAlias());
    ambtenaar.setToelichting(b2.getToelichting());
    ambtenaar.setDatumIngang(new DateTime(b2.getIngangGeld().getLongValue()));
    ambtenaar.setDatumEinde(new DateTime(b2.getEindeGeld().getLongValue()));

    getServices().getHuwelijkService().save(ambtenaar);

    successMessage("De gegevens zijn opgeslagen.");

    super.onSave();
  }

  public class Form1 extends Page2HuwelijksAmbtenaarForm {

    public Form1(HuwelijksAmbtenaar ambtenaar) {
      setCaption("Zoeken");
      setOrder(BSN);
      init(ambtenaar);
    }

    @Override
    public void onOphalen(String bsn) {

      if (emp(bsn)) {
        throw new ProException(INFO, "Geen burgerservicenummer ingegeven");
      }

      String naam = "";
      String telefoon = "";
      String email = "";

      BasePLExt pl = getApplication().getServices().getPersonenWsService().getPersoonslijst(bsn);

      if (pl != null) {

        List<PlContactgegeven> cg = getApplication().getServices().getContactgegevensService().getContactgegevens(
            pl);

        Naam persoonNaam = pl.getPersoon().getNaam();
        naam = trim(
            persoonNaam.getInitNen() + " " + persoonNaam.getNaamNaamgebruikGeslachtsnaamVoorvAanschrijf());

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
      }

      form2.getField(Page2HuwelijksAmbtenaarBean.NAAM).setValue(naam);
      form2.getField(Page2HuwelijksAmbtenaarBean.EMAIL).setValue(email);
      form2.getField(Page2HuwelijksAmbtenaarBean.TELEFOON).setValue(telefoon);
    }
  }

  public class Form2 extends Page2HuwelijksAmbtenaarForm {

    public Form2(HuwelijksAmbtenaar ambtenaar) {
      setCaption("Ambtenaar");
      setOrder(NAAM, TELEFOON, EMAIL, ALIAS, TOELICHTING, INGANG_GELD, EINDE_GELD);
      init(ambtenaar);
    }
  }
}
