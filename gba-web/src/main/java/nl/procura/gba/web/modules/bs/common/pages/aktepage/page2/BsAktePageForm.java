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

package nl.procura.gba.web.modules.bs.common.pages.aktepage.page2;

import static nl.procura.gba.web.modules.bs.common.pages.aktepage.page2.BsAktePageBean.*;
import static nl.procura.standard.Globalfunctions.*;

import java.util.List;

import com.vaadin.ui.Field;
import com.vaadin.ui.Label;

import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.listeners.FieldChangeListener;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.akte.*;
import nl.procura.gba.web.services.bs.algemeen.interfaces.DossierMetAkte;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.container.ArrayListContainer;
import nl.procura.vaadin.component.field.NumberField;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class BsAktePageForm extends GbaForm<BsAktePageBean> {

  private final GbaApplication application;
  private final Dossier        dossier;
  private final DossierAkte    akte;
  private final Label          opmerkingLabel = new Label("", Label.CONTENT_XHTML);

  public BsAktePageForm(GbaApplication application, Dossier dossier, DossierAkte akte) {

    this.application = application;
    this.dossier = dossier;
    this.akte = akte;

    setCaption("Aktegevens");
    setOrder(DATUM, TYPE, DEEL, NUMMER);

    BsAktePageBean bean = new BsAktePageBean();
    bean.setDatum(((DossierMetAkte) dossier.getZaakDossier()).getAkteDatum().getFormatDate());
    bean.setType(DossierAkteRegistersoort.get(dossier));
    bean.setDeel(getDeel(akte));
    bean.setNummer(astr(akte.getVnr()));

    setBean(bean);
  }

  @Override
  public void afterSetBean() {

    final GbaNativeSelect deelField = getField(DEEL, GbaNativeSelect.class);
    deelField.setReadOnly(false);
    deelField.setContainerDataSource(new DeelContainer());

    FieldChangeListener<DossierAkteDeel> deelListener = new FieldChangeListener<DossierAkteDeel>() {

      @Override
      public void onChange(DossierAkteDeel deel) {
        setVolgnummer("");
      }
    };
    deelField.addListener(deelListener);

    final NumberField volgnrField = getField(NUMMER, NumberField.class);

    if (volgnrField != null) {
      FieldChangeListener<String> volgnrListener = new FieldChangeListener<String>() {

        @Override
        public void onChange(String waarde) {
          setVolgnummer(waarde);
        }
      };

      volgnrField.addListener(volgnrListener);
    }

    setVolgnummer("");
    super.afterSetBean();
  }

  @Override
  public void afterSetColumn(Column column, Field field, Property property) {

    if (property.is(NUMMER)) {
      column.addComponent(opmerkingLabel);
    }

    super.afterSetColumn(column, field, property);
  }

  public DossierAkteNummer getAkteNummer() {
    return new DossierAkteNummer(getDatum(), getRegisterdeel(), getVolgNummer());
  }

  @Override
  public BsAktePageBean getNewBean() {
    return new BsAktePageBean();
  }

  private long getDatum() {
    return ((DossierMetAkte) dossier.getZaakDossier()).getAkteDatum().getLongDate();
  }

  private DossierAkteDeel getDeel(DossierAkte akte) {
    return application.getServices().getAkteService().getAkteRegisterDeel(akte);
  }

  private DossierAkteDeel getRegisterdeel() {
    return getValue(DEEL, DossierAkteDeel.class);
  }

  private long getVolgNummer() {
    return along(getBean().getNummer());
  }

  private void setVolgnummerOpmerking(String opmerking) {

    if (fil(opmerking)) {
      opmerkingLabel.setValue(opmerking);
    } else {
      DossierAkteDeel deel = (DossierAkteDeel) getField(DEEL).getValue();
      if (deel != null) {
        opmerkingLabel.setValue("(" + deel.getMin() + " - " + deel.getMax() + ")");
      } else {
        opmerkingLabel.setValue(opmerking);
      }
    }
  }

  private void setVolgnummer(String volgNr) {

    // De aktedatum kan verschillen per dossier
    DossierAkteDeel registerdeel = getRegisterdeel();
    if (registerdeel != null) {
      String volgNummer = volgNr;
      try {
        if (pos(volgNummer)) {
          DossierAkteDeel deel = (DossierAkteDeel) getField(DEEL).getValue();
          Services.getInstance()
              .getAkteService()
              .isCheck(akte, new DossierAkteNummer(getDatum(), deel, along(volgNummer)), null);
        } else {
          volgNummer = astr(application.getServices()
              .getAkteService()
              .getAkteVolgnummer(akte.getCode(), getDatum(), registerdeel));

          setVolgnummerOpmerking("");
        }
      } catch (ProException e) {
        setVolgnummerOpmerking(MiscUtils.setClass(false, e.getMessage()));
      } finally {
        if (getField(NUMMER) != null) {
          getField(NUMMER).setValue(volgNummer);
        }
      }
    }
  }

  private class DeelContainer extends ArrayListContainer {

    public DeelContainer() {

      AkteService aktes = application.getServices().getAkteService();
      List<DossierAkteDeel> registerDelen = aktes.getAkteRegisterDelen(
          DossierAkteRegistersoort.get(dossier).getCode());

      if (registerDelen != null) {
        addItems(registerDelen);
      }
    }
  }
}
