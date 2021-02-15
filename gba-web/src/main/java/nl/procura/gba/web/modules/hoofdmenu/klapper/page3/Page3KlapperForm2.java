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

package nl.procura.gba.web.modules.hoofdmenu.klapper.page3;

import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkte;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkteRegistersoort;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.validation.Bsn;

public class Page3KlapperForm2 extends Page3KlapperForm<Page3KlapperBean2> {

  private final DossierAkteRegistersoort soort;

  public Page3KlapperForm2(DossierAkteRegistersoort soort, DossierAkte akte, boolean muteerbaar, String caption,
      String... order) {

    super(akte, muteerbaar);

    this.soort = soort;

    setColumnWidths(WIDTH_130, "");
    setReadThrough(true);
    setCaption(caption);
    setOrder(order);

    Page3KlapperBean2 bean = new Page3KlapperBean2();

    bean.setDatumErkenning(new DateFieldValue(akte.getDatumFeit().getLongDate()));
    bean.setDatumGeboorte(new DateFieldValue(akte.getDatumFeit().getLongDate()));
    bean.setDatumHuwelijk(new DateFieldValue(akte.getDatumFeit().getLongDate()));
    bean.setDatumOverlijden(new DateFieldValue(akte.getDatumFeit().getLongDate()));

    bean.setBsn1(akte.getAktePersoon().getBurgerServiceNummer());
    bean.setVoorn1(akte.getAktePersoon().getVoornaam());
    bean.setVoorv1(new FieldValue(akte.getAktePersoon().getVoorvoegsel()));
    bean.setNaam1(akte.getAktePersoon().getGeslachtsnaam());
    bean.setGeslacht1(akte.getAktePersoon().getGeslacht());
    bean.setGeboortedatum1(akte.getAktePersoon().getGeboortedatum());

    bean.setBsn2(akte.getAktePartner().getBurgerServiceNummer());
    bean.setVoorn2(akte.getAktePartner().getVoornaam());
    bean.setVoorv2(new FieldValue(akte.getAktePartner().getVoorvoegsel()));
    bean.setNaam2(akte.getAktePartner().getGeslachtsnaam());
    bean.setGeslacht2(akte.getAktePartner().getGeslacht());
    bean.setGeboortedatum2(akte.getAktePartner().getGeboortedatum());

    setBean(bean);
  }

  public DossierAkteRegistersoort getSoort() {
    return soort;
  }

  @SuppressWarnings("unused")
  public void onButtonPersoon(Bsn bsn) {
  }
}
