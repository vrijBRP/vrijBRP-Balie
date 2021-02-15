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

package nl.procura.gba.web.modules.bs.common.pages.nationaliteitpage;

import static nl.procura.gba.web.modules.bs.common.pages.nationaliteitpage.BsNatioBean.*;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierNationaliteitDatumVanafType.ANDERS;
import static nl.procura.standard.Globalfunctions.along;

import com.vaadin.ui.Field;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.listeners.FieldChangeListener;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.ZaakDossier;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierNationaliteitDatumVanafType;
import nl.procura.gba.web.services.bs.algemeen.interfaces.DossierNamenrecht;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteit;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteiten;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class BsNatioForm extends GbaForm<BsNatioBean> {

  private final DossierNationaliteiten dossier;

  public BsNatioForm(BsNatioType type, DossierNationaliteiten dossier) {

    this.dossier = dossier;

    setColumnWidths("90px", "");
    setReadThrough(true);

    if (getDossierNamenrecht(dossier) != null) {
      setOrder(NATIO_MIN, (type == BsNatioType.ERKENNING_AFST ? SINDS_ERK_AFST : SINDS), DATUM_VANAF, REDEN);
    } else {
      setOrder(NATIO_MAX, SINDS, DATUM_VANAF, REDEN);
    }
  }

  @Override
  public void setBean(Object bean) {

    super.setBean(bean);

    // Bij afstammingsrecht alleen nationaliteiten van afstammings personen gebruiken

    DossierNamenrecht namenrecht = getDossierNamenrecht(dossier);
    if (namenrecht != null) {
      getField(NATIO_MIN, GbaNativeSelect.class).setContainerDataSource(new NamenrechtNatioContainer(namenrecht));
    }

    if (getField(SINDS) != null) {
      getNatioField().addListener(new FieldChangeListener<FieldValue>() {

        @Override
        public void onChange(FieldValue natio) {
          doChange(natio, (DossierNationaliteitDatumVanafType) getField(SINDS).getValue());
        }
      });

      getField(SINDS).addListener(new FieldChangeListener<DossierNationaliteitDatumVanafType>() {

        @Override
        public void onChange(DossierNationaliteitDatumVanafType type) {
          doChange((FieldValue) getNatioField().getValue(), type);
        }
      });
    }
  }

  public DossierNationaliteit getDossierNationaliteit() {

    commit();

    DossierNationaliteit natio = new DossierNationaliteit();

    if (getField(NATIO_MIN) != null) {
      natio.setNationaliteit(getBean().getNationaliteitMin());
    } else {
      natio.setNationaliteit(getBean().getNationaliteitMax());
    }

    natio.setDatumVerkrijging(new DateTime(along(getBean().getDatumVanaf().getValue())));
    natio.setRedenverkrijgingNederlanderschap(getBean().getReden());
    natio.setVerkrijgingType(
        getField(SINDS) != null ? getBean().getSinds() : getBean().getSindsErkenningAfstamming());

    return natio;
  }

  @Override
  public BsNatioBean getNewBean() {
    return new BsNatioBean();
  }

  @Override
  public void setColumn(Column column, com.vaadin.ui.Field field, Property property) {

    if (property.is(DATUM_VANAF)) {
      column.setAppend(true);
    }

    super.setColumn(column, field, property);
  }

  /**
   * Is er sprake van een DossierNamenrecht dan dat teruggeven
   */
  private DossierNamenrecht getDossierNamenrecht(DossierNationaliteiten dossier) {

    if (dossier instanceof Dossier) {
      ZaakDossier zaakDossier = ((Dossier) dossier).getZaakDossier();
      if (zaakDossier instanceof DossierNamenrecht) {
        return (DossierNamenrecht) zaakDossier;
      }
    }

    return null;
  }

  private Field getNatioField() {
    return getField(NATIO_MIN) != null ? getField(NATIO_MIN) : getField(NATIO_MAX);
  }

  private void doChange(FieldValue natio, DossierNationaliteitDatumVanafType type) {

    boolean isNederlands = Landelijk.isNederland(natio);
    getField(DATUM_VANAF).setVisible(ANDERS == type);
    getField(REDEN).setVisible(isNederlands && ANDERS == type);
    // Leegmaken reden verkrijging

    if (!isNederlands) {
      getField(REDEN).setValue(new FieldValue());
      getBean().setReden(new FieldValue());
    }

    repaint();
  }
}
