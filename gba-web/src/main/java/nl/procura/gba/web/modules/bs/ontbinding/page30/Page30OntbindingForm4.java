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

package nl.procura.gba.web.modules.bs.ontbinding.page30;

import static nl.procura.gba.web.modules.bs.ontbinding.page30.Page30OntbindingBean4.DATUM_INGANG;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.validators.DatumVolgordeValidator;
import nl.procura.gba.web.modules.bs.ontbinding.page30.Page30Ontbinding.FormOntvangenDocumentenUitspraak;
import nl.procura.gba.web.services.bs.ontbinding.DossierOntbinding;
import nl.procura.vaadin.component.field.ProDateField;

public abstract class Page30OntbindingForm4 extends GbaForm<Page30OntbindingBean4> {

  private final FormOntvangenDocumentenUitspraak formOntvangenDocumentenUitspraak;

  public Page30OntbindingForm4(FormOntvangenDocumentenUitspraak formOntvangenDocumentenUitspraak) {

    this.formOntvangenDocumentenUitspraak = formOntvangenDocumentenUitspraak;
    setCaption("Ingangsdatum van de ontbinding / beëindiging");
    setColumnWidths("200px", "");
    setOrder(DATUM_INGANG);
  }

  @Override
  public Page30OntbindingBean4 getNewBean() {
    return new Page30OntbindingBean4();
  }

  public void setBean(DossierOntbinding zaakDossier) {

    Page30OntbindingBean4 bean = new Page30OntbindingBean4();
    bean.setDatumIngang(zaakDossier.getDatumIngang().getDate());

    setBean(bean);

    ProDateField datumVerzoek = formOntvangenDocumentenUitspraak.getField(Page30OntbindingBean1.DATUM_VERZOEK,
        ProDateField.class);
    ProDateField datumIngang = getField(DATUM_INGANG, ProDateField.class);

    datumIngang.addValidator(
        new DatumVolgordeValidator("Verzoek ontvangen op", datumVerzoek, "Datum ingang", datumIngang));
  }
}
