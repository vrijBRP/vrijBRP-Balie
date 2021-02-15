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

package nl.procura.gba.web.modules.zaken.reisdocument.page21;

import static nl.procura.gba.web.modules.zaken.reisdocument.page21.Page21ReisdocumentBean.*;
import static nl.procura.gba.web.services.zaken.reisdocumenten.ToestemmingGegevenType.*;
import static nl.procura.standard.Globalfunctions.pos;
import static nl.procura.standard.Globalfunctions.trim;

import com.vaadin.ui.Field;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentAanvraag;
import nl.procura.gba.web.services.zaken.reisdocumenten.Toestemming;
import nl.procura.gba.web.services.zaken.reisdocumenten.ToestemmingGegevenType;
import nl.procura.gba.web.services.zaken.reisdocumenten.Toestemmingen;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public abstract class Page21ReisdocumentForm extends GbaForm<Page21ReisdocumentBean> {

  private ReisdocumentAanvraag aanvraag;

  public Page21ReisdocumentForm(ReisdocumentAanvraag aanvraag) {

    setAanvraag(aanvraag);
    initOrder();
    setColumnWidths(WIDTH_130, "100px", "50px", "");

    Page21ReisdocumentBean b = new Page21ReisdocumentBean();
    Toestemmingen t = aanvraag.getToestemmingen();

    final Toestemming o1 = t.getToestemmingOuder1();
    final Toestemming o2 = t.getToestemmingOuder2();
    final Toestemming o3 = t.getToestemmingDerde();
    final Toestemming o4 = t.getToestemmingCurator();

    b.setToestemming1(o1.getGegeven());
    b.setToestemming2(o2.getGegeven());
    b.setToestemming3(o3.getGegeven());
    b.setToestemming4(o4.getGegeven());

    b.setOuder1Label(trim(o1.getNaam() + " " + o1.getToelichting()));
    b.setConstatering1(getAanvraag().getToestemmingen().getToestemmingOuder1().getConstateringen().getString());
    b.setConclusie1(getAanvraag().getToestemmingen().getToestemmingOuder1().getConclusie());

    b.setOuder2Label(trim(o2.getNaam() + " " + o2.getToelichting()));
    b.setConstatering2(getAanvraag().getToestemmingen().getToestemmingOuder2().getConstateringen().getString());
    b.setConclusie2(getAanvraag().getToestemmingen().getToestemmingOuder2().getConclusie());

    b.setDerdeLabel(o3.getToelichting());
    b.setDerdeText(o3.getNaam());
    b.setConstatering3(getAanvraag().getToestemmingen().getToestemmingDerde().getConstateringen().getString());
    b.setConclusie3(getAanvraag().getToestemmingen().getToestemmingDerde().getConclusie());

    b.setCuratorLabel(o4.getToelichting());
    b.setCuratorText(o4.getNaam());
    b.setConstatering4(getAanvraag().getToestemmingen().getToestemmingCurator().getConstateringen().getString());
    b.setConclusie4(getAanvraag().getToestemmingen().getToestemmingCurator().getConclusie());

    setBean(b);

    // Als veld niet verplicht is dan uitzetten.
    if (getField(TOESTEMMING1) != null) {
      getField(TOESTEMMING1).addValidator(new ToestemmingValidator(o1));
    }

    if (getField(TOESTEMMING2) != null) {
      getField(TOESTEMMING2).addValidator(new ToestemmingValidator(o2));
    }

    if (getField(TOESTEMMING3) != null) {
      getField(TOESTEMMING3).addValidator(new ToestemmingValidator(o3));
      ValueChangeListener toestemming3cl = (ValueChangeListener) event -> {
        ToestemmingGegevenType t12 = (ToestemmingGegevenType) event.getProperty().getValue();
        setField(DERDE_TEXT, DERDE_LABEL, o3, t12);
        repaint();
      };
      getField(TOESTEMMING3).addListener(toestemming3cl);
    }

    if (getField(TOESTEMMING4) != null) {
      getField(TOESTEMMING4).addValidator(new ToestemmingValidator(o4));
      getField(TOESTEMMING4).setEnabled(o4.isVerplicht());

      ValueChangeListener toestemming4cl = (ValueChangeListener) event -> {
        ToestemmingGegevenType t1 = (ToestemmingGegevenType) event.getProperty().getValue();
        setField(CURATOR_TEXT, CURATOR_LABEL, o4, t1);
        repaint();
      };

      getField(TOESTEMMING4).addListener(toestemming4cl);
    }

    // Als het veld niet verplicht is of reeds bekend dan geen invoerveld van maken
    if (getField(DERDE_TEXT) != null) {
      setField(DERDE_TEXT, DERDE_LABEL, o3, o3.getGegeven());
    }

    // Als het veld niet verplicht is of reeds bekend dan geen invoerveld van maken
    if (getField(CURATOR_TEXT) != null) {
      setField(CURATOR_TEXT, CURATOR_LABEL, o4, o4.getGegeven());
    }

    repaint();
  }

  public abstract void initOrder();

  private void setField(String text, String label, Toestemming o, ToestemmingGegevenType t) {

    boolean visible1 = !NVT.equals(t) && o.isVerplicht() && !pos(o.getAnummer());
    boolean visible2 = (JA.equals(t) || VERVANGENDE.equals(t)) && !pos(o.getAnummer());

    getField(text).setVisible(visible1 || visible2);
    getField(text).setRequired(getField(text).isVisible());
    getField(label).setVisible(!getField(text).isVisible());
  }

  public ReisdocumentAanvraag getAanvraag() {
    return aanvraag;
  }

  public void setAanvraag(ReisdocumentAanvraag aanvraag) {
    this.aanvraag = aanvraag;
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {
    if (property.is(CONSTATERING1, CONSTATERING2, CONSTATERING3, CONSTATERING4, CONCLUSIE1, CONCLUSIE2, CONCLUSIE3,
        CONCLUSIE4)) {
      column.setColspan(3);
    }

    super.setColumn(column, field, property);
  }
}
