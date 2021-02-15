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

package nl.procura.gba.web.modules.bs.ontbinding.overzicht.form1;

import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.bs.ontbinding.DossierOntbinding;

public class OntbindingOverzichtForm1 extends ReadOnlyForm {

  private final DossierOntbinding dossierOntbinding;

  public OntbindingOverzichtForm1(DossierOntbinding dossierOntbinding) {

    this.dossierOntbinding = dossierOntbinding;

    setReadonlyAsText(true);

    setColumnWidths("220px", "");

    init();

    // Sluiting + Akte
    OntbindingOverzichtBean1 bean = new OntbindingOverzichtBean1();

    StringBuilder sluiting = new StringBuilder();
    sluiting.append(dossierOntbinding.getSoortVerbintenis());
    sluiting.append(": ");
    sluiting.append(dossierOntbinding.getDatumVerbintenis());
    sluiting.append(" ");
    sluiting.append(dossierOntbinding.getPlaatsVerbintenis());
    sluiting.append(",");
    sluiting.append(dossierOntbinding.getLandVerbintenis());

    StringBuilder akte = new StringBuilder();
    akte.append(dossierOntbinding.getBsAkteNummerVerbintenis());
    akte.append(", ");
    akte.append(dossierOntbinding.getAktePlaatsVerbintenis());
    akte.append(", ");
    akte.append(dossierOntbinding.getAkteJaarVerbintenis());

    bean.setSluiting(sluiting.toString());
    bean.setAkte(akte.toString());

    bean.setDoor(dossierOntbinding.getWijzeBeeindigingVerbintenis());

    // Brondocument
    bean.setUitspraak(dossierOntbinding.getUitspraakDoor());
    bean.setDatumGewijsde(dossierOntbinding.getDatumGewijsde());
    bean.setVerzoekInschrijvingDoor(dossierOntbinding.getVerzoekTotInschrijvingDoor());
    bean.setDatumVerzoek(dossierOntbinding.getDatumVerzoek());
    bean.setBinnenTermijn(dossierOntbinding.isBinnenTermijn() ? "Ja" : "Nee");

    bean.setDatumVerklaring(dossierOntbinding.getDatumVerklaring());
    bean.setOndertekendDoor(dossierOntbinding.getOndertekendDoor());
    bean.setDatumOndertekening(dossierOntbinding.getDatumOndertekening());

    setBean(bean);
  }

  @Override
  public OntbindingOverzichtBean1 getBean() {
    return (OntbindingOverzichtBean1) super.getBean();
  }

  public DossierOntbinding getDossierOntbinding() {
    return dossierOntbinding;
  }

  @Override
  public Object getNewBean() {
    return new OntbindingOverzichtBean1();
  }

  protected void init() {
  }
}
