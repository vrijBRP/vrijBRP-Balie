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

package nl.procura.gba.web.modules.bs.registration.page10;

import static nl.procura.gba.web.modules.bs.registration.page10.Page10DeclarationBean.*;

import com.vaadin.ui.Button;
import com.vaadin.ui.Field;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.fields.CountryBox;
import nl.procura.gba.web.components.fields.DateReference;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.zaken.common.ToelichtingButton;
import nl.procura.gba.web.modules.zaken.common.ToelichtingWindow;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.registration.DossierRegistration;
import nl.procura.gba.web.services.bs.registration.OriginSituationType;
import nl.procura.gba.web.services.bs.registration.StaydurationType;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.table.TableLayout;

class Page10DeclarationForm extends GbaForm<Page10DeclarationBean> {

  private final Dossier             dossier;
  private final DossierRegistration dossRegistration;

  Page10DeclarationForm(Dossier dossier) {
    this.dossier = dossier;
    this.dossRegistration = (DossierRegistration) dossier.getZaakDossier();

    setColumnWidths("200px", "");
    setOrder(F_DATUM_AANGIFTE, F_ORIGIN_SITUATION, F_COUNTRY, F_STAY_DURATION);

    final Page10DeclarationBean bean = new Page10DeclarationBean();
    bean.setDatumAangifte(dossier.getDatumIngang().getDate());
    bean.setOriginSituation(OriginSituationType.valueOfCode(dossRegistration.getOriginSituation()));
    bean.setCountry(dossRegistration.getDepartureCountry());
    bean.setStayDuration(StaydurationType.valueOfCode(dossRegistration.getDuration()));
    setBean(bean);
  }

  @Override
  public void afterSetBean() {
    getField(F_ORIGIN_SITUATION).addListener((ValueChangeListener) e -> {
      checkCountry((OriginSituationType) e.getProperty().getValue());
    });
    checkCountry(getBean().getOriginSituation());
    DateReference.setField(getField(F_COUNTRY, CountryBox.class), getField(F_DATUM_AANGIFTE));
    super.afterSetBean();
  }

  @Override
  public void afterSetColumn(TableLayout.Column column, Field field, Property property) {

    if (property.is(F_ORIGIN_SITUATION)) {
      column.addComponent(new ToelichtingButton("Toelichting") {

        @Override
        public void buttonClick(Button.ClickEvent event) {
          String msg = "Deze procedure is ook van toepassing op een kind dat in Nederland is geboren, " +
              "maar niet op grond van het opmaken van een geboorteakte wordt ingeschreven, " +
              "omdat de moeder (uit wie het kind is geboren) niet als ingezetene in de BRP van een gemeente is " +
              "ingeschreven. <p>In dit geval worden er geen immigratiegegevens opgenomen in " +
              "categorie 08 Verblijfplaats. <br>Verder moet u in deze situatie een Iv11-bericht versturen aan " +
              "de geboortegemeente.";
          getWindow().addWindow(
              new ToelichtingWindow("Toelichting", "Toelichting 'Kind geboren in NL, moeder niet in BRP'", msg));
        }
      });
    }

    super.afterSetColumn(column, field, property);
  }

  private void checkCountry(OriginSituationType situationType) {
    Field countryField = getField(F_COUNTRY);
    if (OriginSituationType.CHILD_BORN_IN_NL.equals(situationType)) {
      countryField.setValue(null);
      countryField.setVisible(false);
    } else {
      countryField.setVisible(true);
    }
    repaint();
  }

  public void save() {
    commit();
    Page10DeclarationBean bean = getBean();
    dossier.setDatumIngang(new DateTime(bean.getDatumAangifte()));
    dossRegistration.setOriginSituation(bean.getOriginSituation().getCode());
    dossRegistration.setDepartureCountry(getField(F_COUNTRY).isVisible() ? bean.getCountry() : new FieldValue(-1));
    dossRegistration.setDuration(bean.getStayDuration().getCode());
  }
}
