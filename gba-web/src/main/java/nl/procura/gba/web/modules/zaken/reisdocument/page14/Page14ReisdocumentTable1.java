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

package nl.procura.gba.web.modules.zaken.reisdocument.page14;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.gba.web.services.zaken.inhoudingen.InhoudingType.NIET_INGEHOUDEN;
import static nl.procura.gba.web.services.zaken.inhoudingen.InhoudingType.VAN_RECHTSWEGE_VERVALLEN;
import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.standard.Globalfunctions.fil;

import java.util.List;

import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhouding;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhoudingenService;
import nl.procura.gba.web.services.zaken.inhoudingen.InhoudingType;
import nl.procura.gba.web.services.zaken.reisdocumenten.Reisdocument;
import nl.procura.standard.ProcuraDate;

public class Page14ReisdocumentTable1 extends GbaTable {

  private final BasePLExt pl;

  public Page14ReisdocumentTable1(BasePLExt pl) {
    this.pl = pl;
  }

  @Override
  public void setColumns() {

    setSelectable(true);

    addColumn("Nummer", 100);
    addColumn("Geldig tot", 100).setUseHTML(true);
    addColumn("Ingehouden op", 100).setUseHTML(true);
    addColumn("Status");
    addColumn("Reisdocument");
    addColumn("Opmerking").setUseHTML(true);

    super.setColumns();
  }

  @Override
  public void setRecords() {

    DocumentInhoudingenService dbReisdocumenten = getApplication().getServices().getDocumentInhoudingenService();

    List<Reisdocument> documenten = dbReisdocumenten.getReisdocumentHistorie(pl);

    for (Reisdocument rd : documenten) {

      if (emp(rd.getNummerDocument().getVal())) {
        continue;
      }

      Record record = addRecord(rd);
      String soort = rd.getDocumentOmschrijvingFormat();
      String nummerDocument = rd.getNummerDocument().getVal();
      String ingehoudenOp = "";
      String status = "";
      String opmerking = "";
      InhoudingType type = InhoudingType.NIET_INGEHOUDEN;

      BasePLValue dEnd = rd.getDatumEindeGeldigheid();
      String geldigTot = dEnd.getDescr();

      // Zoek inhouding in Probev
      if (fil(rd.getAanduidingInhoudingVermissing().getVal())) {
        ingehoudenOp = rd.getDatumInhoudingVermissing().getDescr();
        type = InhoudingType.get(rd.getAanduidingInhoudingVermissing().getVal());
      }

      // Zoek inhouding in Proweb
      if (type.is(NIET_INGEHOUDEN, VAN_RECHTSWEGE_VERVALLEN)) {
        DocumentInhouding inh = dbReisdocumenten.getInhouding(pl, rd);
        if (inh != null) {
          ingehoudenOp = inh.getDatumIngang().getFormatDate();
          type = inh.getInhoudingType();
        }
      }

      boolean isVerlopen = type.isNogNietIngehouden() && new ProcuraDate(dEnd.getVal()).isExpired();
      if (type.isVanRechtswegeVervallen()) {
        isVerlopen = true;
        geldigTot = ingehoudenOp;
        ingehoudenOp = "";
      }

      status = type.getOms();
      if (type.isNogNietIngehouden()) {
        status = "In bezit burger";
      } else if (type.isVanRechtswegeVervallen()) {
        status = "Van rechtswege vervallen, maar nog in bezit burger";
      }

      if ((isVerlopen && type.isNogNietIngehouden()) || type.isVanRechtswegeVervallen()) {
        opmerking = setClass("red", "dient te worden ingeleverd");
      }

      record.addValue(nummerDocument);
      record.addValue(isVerlopen ? setClass(false, geldigTot) : geldigTot);
      record.addValue(ingehoudenOp);
      record.addValue(status);
      record.addValue(soort);
      record.addValue(opmerking);
    }

    super.setRecords();
  }
}
