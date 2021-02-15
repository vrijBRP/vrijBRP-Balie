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

package nl.procura.gba.web.modules.zaken.tmv.page4;

import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.zaken.tmv.TerugmeldingAanvraag;
import nl.procura.gba.web.services.zaken.tmv.TerugmeldingRegistratie;

public class Page4TmvForm4 extends ReadOnlyForm {

  private static final String DATUMAANLEGDOSSIER        = "datumAanlegDossier";
  private static final String DATUMWIJZIGINGDOSSIER     = "datumWijzigingDossier";
  private static final String BEHANDELENDEGEMEENTE      = "behandelendeGemeente";
  private static final String DATUMVERWACHTEAFHANDELING = "datumVerwachteAfhandeling";
  private static final String STATUSDOSSIER             = "statusDossier";
  private static final String RESULTAATCODEONDERZOEK    = "resultaatcodeOnderzoek";
  private static final String TOELICHTINGRESULTAAT      = "toelichtingResultaat";

  public Page4TmvForm4(TerugmeldingAanvraag tmv) {

    setCaption("Externe status");
    setOrder(DATUMAANLEGDOSSIER, DATUMWIJZIGINGDOSSIER, BEHANDELENDEGEMEENTE, DATUMVERWACHTEAFHANDELING,
        STATUSDOSSIER, RESULTAATCODEONDERZOEK, TOELICHTINGRESULTAAT);
    setColumnWidths("200px", "");

    Page4TmvBean4 bean = new Page4TmvBean4();

    TerugmeldingRegistratie registratie = tmv.getStatusTmv();

    if (registratie.isStored()) {

      bean.setBehandelendeGemeente(registratie.getGemBeh().getDescription());
      bean.setDatumAanlegDossier(registratie.getAanleg().toString());
      bean.setDatumVerwachteAfhandeling(registratie.getVerwAfh().toString());
      bean.setDatumWijzigingDossier(registratie.getWijz().toString());
      bean.setResultaatcodeOnderzoek(registratie.getOnderzoekResultaat().toString());
      bean.setStatusDossier(registratie.getStatus().toString());
      bean.setToelichtingResultaat(registratie.getToelichtingResultaat());
    }

    setBean(bean);
  }
}
