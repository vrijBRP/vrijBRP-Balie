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

package nl.procura.gba.web.modules.bs.overlijden.buitenland.page20;

import static nl.procura.gba.web.modules.bs.overlijden.buitenland.page20.Page20OverlijdenBean.*;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.AFGEVER;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;

import java.util.List;

import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.modules.bs.common.pages.gerelateerdepage.PageBsRelaties;
import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsStatusForm;
import nl.procura.gba.web.modules.bs.overlijden.buitenland.page25.Page25Overlijden;
import nl.procura.gba.web.services.bs.algemeen.enums.TypeAfgever;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoonFilter;
import nl.procura.gba.web.services.bs.overlijden.buitenland.DossierOverlijdenBuitenland;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**

 * <p>
 * 6 Feb. 2013 8:00:00
 */
public class Page20Overlijden extends PageBsRelaties<DossierOverlijdenBuitenland> {

  private Form1          form1;
  private Form2          form2;
  private Form3          form3;
  private Form4          form4;
  private Table          table;
  private VerticalLayout nabestaandeLayout;

  public Page20Overlijden() {
    super("Overlijden buitenland - brondocument");
  }

  @Override
  public boolean checkPage() {

    form1.commit();
    form2.commit();
    form3.commit();

    DossierOverlijdenBuitenland dossier = getZaakDossier();

    dossier.setDatumOntvangst(new DateTime(along(form1.getBean().getDatumOntvangst().getValue())));
    dossier.setAfgeverType(form1.getBean().getOntvangstType());

    dossier.setDatumOverlijden(new DateTime(along(form2.getBean().getDatumOverlijden().getValue())));
    dossier.setPlaatsOverlijden(form2.getBean().getPlaatsOverlijden());
    dossier.setLandOverlijden(form2.getBean().getLandOverlijden());

    dossier.setTypeBronDocument(form3.getBean().getBronDocument());
    dossier.setLandAfgifte(form3.getBean().getLandAfgifte());
    dossier.setVoldoetAanEisen(form3.getBean().isVoldoetAanEisen());

    if (dossier.getAfgeverType() == TypeAfgever.NABESTAANDE
        && (dossier.getAfgever() == null || !dossier.getAfgever().isVolledig())) {
      throw new ProException(WARNING, "Er is geen nabestaande ingevuld");
    }

    getServices().getOverlijdenBuitenlandService().save(getDossier());

    return true;
  }

  @Override
  public void event(PageEvent event) {

    try {

      if (event.isEvent(InitPage.class)) {

        addButton(buttonPrev);
        addButton(buttonNext);

        nabestaandeLayout = new VerticalLayout();
        nabestaandeLayout.setSpacing(true);

        table = new Table(getZaakDossier());
        form1 = new Form1();
        form2 = new Form2();
        form3 = new Form3();
        form4 = new Form4(getDossierPersonen(getZaakDossier().getOverledene()));

        addComponent(new BsStatusForm(getDossier()));
        addComponent(form1);

        nabestaandeLayout.addComponent(new Fieldset("Nabestaande", table));
        nabestaandeLayout.addComponent(form4);
        addComponent(nabestaandeLayout);

        addComponent(form2);
        addComponent(form3);
      } else if (event.isEvent(AfterReturn.class)) {

        table.init();
        form1.reset();
      }
    } finally {
      super.event(event);
    }
  }

  @Override
  public void onNextPage() {
    goToNextProces();
  }

  @Override
  public void onPreviousPage() {
    goToPreviousProces();
  }

  public class Form1 extends Page20OverlijdenForm {

    public Form1() {
      super(getZaakDossier(), null);
    }

    @Override
    public void init() {

      setCaption("Ontvangst");
      setOrder(DATUMONTVANGST, ONTVANGSTTYPE);
    }

    @Override
    protected void wijzingOntvangstType(TypeAfgever typeAfgever) {

      nabestaandeLayout.setVisible(typeAfgever == TypeAfgever.NABESTAANDE);
      table.getRecords().clear();
    }
  }

  public class Form2 extends Page20OverlijdenForm {

    public Form2() {
      super(getZaakDossier(), null);
    }

    @Override
    public void init() {

      setCaption("Rechtsfeit");
      setOrder(DATUMOVERLIJDEN, PLAATSOVERLIJDEN, LANDOVERLIJDEN);
    }
  }

  public class Form3 extends Page20OverlijdenForm {

    public Form3() {
      super(getZaakDossier(), null);
    }

    @Override
    public void init() {

      setCaption("Soort");
      setOrder(BRONDOCUMENT, LANDAFGIFTE, VOLDOETAANEISEN);
    }
  }

  public class Form4 extends Page20OverlijdenForm {

    private Form4(List<DossierPersoon> relaties) {
      super(getZaakDossier(), relaties);
    }

    @Override
    public void init() {
      setOrder(GERELATEERDEN);
      setColumnWidths("200px", "");
    }

    @Override
    protected void wijzingGerelateerde(FieldValue value) {

      if (value != null) {
        BsPersoonUtils.kopieDossierPersoon((DossierPersoon) value.getValue(),
            getDossier().getPersoon(DossierPersoonFilter.filter(AFGEVER)));
        table.init();
      }
    }
  }

  class Table extends Page20OverlijdenTable {

    private Table(DossierOverlijdenBuitenland dossier) {
      super(dossier);
    }

    @Override
    public void onClick(Record record) {

      getNavigation().goToPage(new Page25Overlijden((DossierPersoon) record.getObject()));

      super.onClick(record);
    }
  }
}
