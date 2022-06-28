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

package nl.procura.gba.web.modules.bs.onderzoek.page20;

import static nl.procura.gba.web.modules.bs.onderzoek.page20.Page20OnderzoekBean.*;

import java.util.Date;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.validators.DatumVolgordeValidator;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoek;
import nl.procura.gba.web.services.bs.onderzoek.enums.AanduidingOnderzoekType;
import nl.procura.vaadin.component.field.ProDateField;

public class Page20OnderzoekForm4 extends GbaForm<Page20OnderzoekBean> {

  private final DossierOnderzoek zaakDossier;

  public Page20OnderzoekForm4(DossierOnderzoek zaakDossier) {
    this.zaakDossier = zaakDossier;
    setCaption("Deelresultaat");
    setColumnWidths("450px", "");
    setOrder(DEELRESULTAAT, DATUM_AANVANG_DEELRESULTAAT, AAND_GEG_DEELRESULTAAT);
    setBean(zaakDossier);
  }

  public void setBean(DossierOnderzoek zaakDossier) {
    Page20OnderzoekBean bean = new Page20OnderzoekBean();
    bean.setDeelresultaat(zaakDossier.getAanduidingGegevensDeelresultaat() != AanduidingOnderzoekType.ONBEKEND);
    bean.setDatumAanvangDeelresultaat(zaakDossier.getDatumAanvangDeelresultaat().getDate());
    bean.setAanduidingGegevensDeelresultaat(zaakDossier.getAanduidingGegevensDeelresultaat().getCode());
    setBean(bean);
  }

  @Override
  public void afterSetBean() {
    super.afterSetBean();
    Date datumOntvangst = zaakDossier.getDatumOntvangstMelding().getDate();
    ProDateField datumAanvangDeelresultaat = getField(DATUM_AANVANG_DEELRESULTAAT, ProDateField.class);
    datumAanvangDeelresultaat.addValidator(
        new DatumVolgordeValidator("Datum ontvangst melding", () -> datumOntvangst,
            "Datum aanvang deelresultaat", () -> (Date) datumAanvangDeelresultaat.getValue()));

    onChangeDeelresultaat(AanduidingOnderzoekType.ONBEKEND != zaakDossier.getAanduidingGegevensDeelresultaat());
    getField(DEELRESULTAAT).addListener((ValueChangeListener) event -> {
      onChangeDeelresultaat((Boolean) event.getProperty().getValue());
      repaint();
    });

    repaint();
  }

  private void onChangeDeelresultaat(boolean isAandGeg) {
    getField(DATUM_AANVANG_DEELRESULTAAT).setVisible(isAandGeg);
    getField(AAND_GEG_DEELRESULTAAT).setVisible(isAandGeg);
  }
}
