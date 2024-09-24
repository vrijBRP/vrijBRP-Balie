/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.modules.bs.naturalisatie.page60;

import static nl.procura.gba.web.modules.bs.naturalisatie.page60.Page60NaturalisatieBean.F_CEREMONIE_1;
import static nl.procura.gba.web.modules.bs.naturalisatie.page60.Page60NaturalisatieBean.F_CEREMONIE_2;
import static nl.procura.gba.web.modules.bs.naturalisatie.page60.Page60NaturalisatieBean.F_CEREMONIE_3;
import static nl.procura.gba.web.modules.bs.naturalisatie.page60.Page60NaturalisatieBean.F_DATUM_UITREIKING;
import static nl.procura.gba.web.modules.bs.naturalisatie.page60.Page60NaturalisatieBean.F_DATUM_VERVAL;

import java.util.Date;

import com.vaadin.ui.Field;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.bs.naturalisatie.page40.CeremonieComponent;
import nl.procura.gba.web.modules.bs.naturalisatie.page40.CeremonieComponent.CeremonieComponentValue;
import nl.procura.gba.web.modules.bs.naturalisatie.valuechoice.ValueChoiceConfig;
import nl.procura.gba.web.modules.bs.naturalisatie.valuechoice.ValueChoiceField;
import nl.procura.gba.web.services.bs.naturalisatie.DossierNaturalisatie;
import nl.procura.gba.web.services.bs.naturalisatie.DossierNaturalisatieVerzoeker;
import nl.procura.vaadin.component.field.ProDateField;
import nl.procura.vaadin.component.layout.table.TableLayout;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page60NaturalisatieForm extends GbaForm<Page60NaturalisatieBean> {

  private final DossierNaturalisatie dossier;

  public Page60NaturalisatieForm(DossierNaturalisatie dossier) {
    this.dossier = dossier;
    setCaption("Gegevens m.b.t. ceremonies");
    setReadThrough(true);
    setColumnWidths("300px", "");
    setOrder(F_CEREMONIE_1, F_CEREMONIE_2, F_CEREMONIE_3,
        F_DATUM_UITREIKING, F_DATUM_VERVAL);

    setBean(new Page60NaturalisatieBean());
  }

  @Override
  public void afterSetColumn(TableLayout.Column column, Field field, Property property) {
    if (property.is(F_CEREMONIE_1)) {
      column.addComponent(new ValueChoiceField(dossier, ValueChoiceConfig.<CeremonieComponentValue> create()
          .title("Ceremonie 1")
          .component(verzoeker -> new CeremonieComponent(1))
          .getter(
              verzoeker -> CeremonieComponent.value(
                  verzoeker.getCeremonie1DIn(),
                  verzoeker.getCeremonie1TIn(),
                  verzoeker.getCeremonie1Bijgewoond()))
          .setter((verzoeker, value) -> {
            verzoeker.setCeremonie1DIn(value.getDatum());
            verzoeker.setCeremonie1TIn(value.getTijdstip().getBigDecimalValue());
            verzoeker.setCeremonie1Bijgewoond(value.getBijgewoond());
          })
          .build()));

    }

    if (property.is(F_CEREMONIE_2)) {
      column.addComponent(new ValueChoiceField(dossier, ValueChoiceConfig.<CeremonieComponentValue> create()
          .title("Ceremonie 2")
          .component(v -> new CeremonieComponent(2))
          .getter(v -> CeremonieComponent.value(
              v.getCeremonie2DIn(),
              v.getCeremonie2TIn(),
              v.getCeremonie2Bijgewoond()))
          .setter((v, value) -> {
            v.setCeremonie2DIn(value.getDatum());
            v.setCeremonie2TIn(value.getTijdstip().getBigDecimalValue());
            v.setCeremonie2Bijgewoond(value.getBijgewoond());
          })
          .build()));

    }

    if (property.is(F_CEREMONIE_3)) {
      column.addComponent(new ValueChoiceField(dossier, ValueChoiceConfig.<CeremonieComponentValue> create()
          .title("Ceremonie 3")
          .component(v -> new CeremonieComponent(3))
          .getter(v -> CeremonieComponent.value(
              v.getCeremonie3DIn(),
              v.getCeremonie3TIn(),
              v.getCeremonie3Bijgewoond()))
          .setter((v, value) -> {
            v.setCeremonie3DIn(value.getDatum());
            v.setCeremonie3TIn(value.getTijdstip().getBigDecimalValue());
            v.setCeremonie3Bijgewoond(value.getBijgewoond());
          })
          .build()));

    }

    if (property.is(F_DATUM_UITREIKING)) {
      column.addComponent(new ValueChoiceField(dossier, ValueChoiceConfig.<Date> create()
          .title("Bevestiging / KB uitgereikt op")
          .component(v -> {
            ProDateField dateField = new ProDateField();
            dateField.setImmediate(true);
            return dateField;
          })
          .getter(DossierNaturalisatieVerzoeker::getCeremonieDUitreik)
          .setter(DossierNaturalisatieVerzoeker::setCeremonieDUitreik)
          .build()));
    }

    if (property.is(F_DATUM_VERVAL)) {
      column.addComponent(new ValueChoiceField(dossier, ValueChoiceConfig.<Date> create()
          .title("Vervaldatum")
          .component(v -> {
            ProDateField dateField = new ProDateField();
            dateField.setImmediate(true);
            return dateField;
          })
          .getter(DossierNaturalisatieVerzoeker::getCeremonieDVerval)
          .setter(DossierNaturalisatieVerzoeker::setCeremonieDVerval)
          .build()));
    }

    super.afterSetColumn(column, field, property);
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {
    if (property.is(F_DATUM_UITREIKING)) {
      getLayout().addFieldset("Overige gegevens");
    }
    super.setColumn(column, field, property);
  }

  @Override
  public Page60NaturalisatieBean getNewBean() {
    return new Page60NaturalisatieBean();
  }
}
