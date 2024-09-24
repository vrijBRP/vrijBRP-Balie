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

package nl.procura.gba.web.modules.zaken.protocol.page1;

import static nl.procura.standard.Globalfunctions.*;

import java.util.List;

import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.common.misc.ZaakPeriode;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.telling.Anders;
import nl.procura.gba.web.services.zaken.protocol.ProtocolZoekopdracht;
import nl.procura.java.reflection.ReflectionUtil;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.commons.core.exceptions.ProExceptionSeverity;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;
import nl.procura.vaadin.component.window.Message;

public class FormAndTablePage1 extends VerticalLayout {

  private final Page1ProtocolleringForm form;
  private final GbaTable                table;

  public FormAndTablePage1() {
    this(-1);
  }

  public FormAndTablePage1(long anr) {

    setSizeFull();
    setSpacing(true);

    form = new Page1ProtocolleringForm(anr);

    addComponent(form);

    table = new GbaTable() {

      @Override
      public void onClick(Record record) {

        try {
          ProtocolZoekopdracht ps = (ProtocolZoekopdracht) record.getObject();

          ProtocolZoekopdracht nps = ReflectionUtil.deepCopyBean(ProtocolZoekopdracht.class, ps);

          switch (ps.getGroep()) {

            case DATUM:
              nps.setPeriode(new ZaakPeriode("Anders", nps.getWaarde(), nps.getWaarde()));
              break;

            case ANUMMER:
              nps.setNummer(new AnrFieldValue(astr(nps.getWaarde()), nps.getOmschrijving()));
              break;

            case GEBRUIKER:
              nps.setGebruiker(new UsrFieldValue(nps.getWaarde(), nps.getOmschrijving()));
              break;

            default:
              break;
          }

          onSelectRecord(nps);
        } catch (Exception e) {
          getApplication().handleException(getWindow(), e);
        }
      }

      @Override
      public void setColumns() {

        setSizeFull();
        setSelectable(true);

        addColumn("Nr.", 60);
        addColumn("Omschrijving", 200);
        addColumn("Aantal");

        super.setColumns();
      }
    };

    addComponent(new Fieldset("Zoekresultaten"));
    addComponent(table);
    setExpandRatio(table, 1f);
  }

  @Override
  public GbaApplication getApplication() {
    return (GbaApplication) super.getApplication();
  }

  public void onSearch() {

    form.commit();

    Page1ProtocolleringBean b = form.getBean();

    UsrFieldValue gebruiker = b.getGebruiker();
    FieldValue anrBsn = b.getAnr();
    ZaakPeriode periode = b.getPeriode();

    table.getRecords().clear();

    table.reloadRecords();

    if (gebruiker == null && !pos(anrBsn.getValue()) && periode == null) {
      throw new ProException(ProExceptionSeverity.INFO, "Geen zoekargumenten ingegeven");
    }

    if (periode != null && periode.equals(new Anders())) {
      periode.setdFrom(along(b.getVan().getValue()));
      periode.setdTo(along(b.getTm().getValue()));
    }

    List<ProtocolZoekopdracht> records = getApplication().getServices()
        .getProtocolleringService()
        .getProtocolGroepen(gebruiker, anrBsn, periode, b.getGroep());

    if (records.size() > 0) {

      int nr = records.size();

      for (ProtocolZoekopdracht pn : records) {

        Record r = table.addRecord(pn);
        r.addValue(nr);
        r.addValue(pn.getOmschrijving());
        r.addValue(pn.getAantal());

        nr--;
      }

      table.reloadRecords();

    } else {

      new Message(getWindow(), "Geen records gevonden", Message.TYPE_INFO);
    }
  }

  public void reset() {
    form.reset();
  }

  @SuppressWarnings("unused")
  protected void onSelectRecord(ProtocolZoekopdracht nps) {
  } // Override
}
