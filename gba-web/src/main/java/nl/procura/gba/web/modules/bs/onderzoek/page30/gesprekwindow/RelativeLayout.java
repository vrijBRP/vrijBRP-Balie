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

package nl.procura.gba.web.modules.bs.onderzoek.page30.gesprekwindow;

import static nl.procura.gba.web.modules.bs.onderzoek.page30.gesprekwindow.BronBean.*;

import com.vaadin.ui.Field;

import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoekBron;
import nl.procura.gba.web.services.gba.ple.relatieLijst.RelatieType;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.table.TableLayout;

public class RelativeLayout extends AbstractBronLayout {

  private final DossierOnderzoekBron bron;
  private final PersonForm           form;

  public RelativeLayout(DossierOnderzoekBron bron) {
    this.bron = bron;
    form = new PersonForm(bron);
    addComponent(form);
  }

  @Override
  public void reset() {
    form.setBron(resetBron(bron));
  }

  @Override
  public void save() {
    form.commit();
    bron.setAdresType(form.getBean().getAdresType());
    bron.setInst(form.getBean().getInstantie());
    bron.setInstAfdeling(form.getBean().getAfdeling());
    bron.setInstAanhef(FieldValue.from(form.getBean().getTavAanhef()).getStringValue());
    bron.setInstVoorl(form.getBean().getTavVoorl());
    bron.setInstNaam(form.getBean().getTavNaam());
    bron.setInstEmail(form.getBean().getEmail());
    bron.setAdr(form.getBean().getAdres());
    bron.setPc(form.getBean().getPc().getStringValue());
    bron.setPlaats(form.getBean().getPlaats());
    bron.setInstAanschr(form.getBean().getAanschrijving());
  }

  public void setRelatie(DossierPersoon dossierPersoon, DossierPersoon gerelateerde, RelatieType relatieType) {
    form.reset();
    form.getBean().setAanschrijving("");
    if (gerelateerde != null) {
      form.setPersoon(gerelateerde);
      form.getBean().setAanschrijving(String.format("Gerelateerde (%s) van %s",
          relatieType.getOms().toLowerCase(), dossierPersoon.getNaam()
              .getNaam_naamgebruik_eerste_voornaam()));
    }
    form.setBean(form.getBean());
  }

  public class PersonForm extends AbstractBronForm {

    public PersonForm(DossierOnderzoekBron bron) {
      setCaption("Aanschrijving gerelateerde");
      setColumnWidths("200px", "");
      setOrder(F_AANSCHRIJVING, F_INSTANTIE, F_TAV_AANHEF, F_TAV_VOORL, F_TAV_NAAM, F_ADRES, F_PC, F_PLAATS, F_EMAIL);
      setBron(bron);
    }

    @Override
    public void setColumn(TableLayout.Column column, Field field, Property property) {
      if (property.is(F_TAV_VOORL, F_TAV_NAAM, F_PLAATS)) {
        column.setAppend(true);
      }

      super.setColumn(column, field, property);
    }
  }
}
