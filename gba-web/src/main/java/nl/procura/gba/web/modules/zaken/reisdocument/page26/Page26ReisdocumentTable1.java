/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.zaken.reisdocument.page26;

import static nl.procura.gba.jpa.personen.dao.RdmAmpDao.isIngehoudenAndereAanvraag;
import static nl.procura.gba.jpa.personen.dao.RdmAmpDao.isIngehoudenDezeAanvraag;
import static nl.procura.standard.Globalfunctions.date2str;

import java.util.List;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhoudingenService;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentType;
import nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.Bezorging;
import nl.procura.vaadin.component.field.ProNativeSelect;

public class Page26ReisdocumentTable1 extends GbaTable {

  private final Bezorging bezorging;
  private final BasePLExt pl;

  public Page26ReisdocumentTable1(Bezorging bezorging, BasePLExt pl) {
    this.bezorging = bezorging;
    this.pl = pl;
  }

  @Override
  public void setColumns() {
    setSelectable(false);
    setClickable(true);

    addColumn("Innemen bij uitreiking", 180).setClassType(ProNativeSelect.class);
    addColumn("Einde geldigheid", 130);
    addColumn("Documentnummer", 130);
    addColumn("Soort document");
    super.setColumns();
  }

  @Override
  public void setRecords() {
    DocumentInhoudingenService inhoudingen = getApplication().getServices().getDocumentInhoudingenService();
    ReisdocumentType type = inhoudingen.getInTeleverenDocument(pl,
        ReisdocumentType.get(bezorging.getMelding().getDocType()));

    inhoudingen.getActueelReisdocumentHistorie(pl).forEach(reisdoc -> {
      BezorgingInhouding inh = new BezorgingInhouding(reisdoc);
      inh.setAutomatischInhouden(inh.getDocumentType() == type);

      boolean inDezeAanvraag = isIngehoudenDezeAanvraag(bezorging.getMelding(), inh.getDocumentNr());
      boolean inAndereAanvraag = isIngehoudenAndereAanvraag(bezorging.getMelding(), inh.getDocumentNr());

      if (inAndereAanvraag) {
        Record r = addRecord(inh);
        inh.setValueField(new BooleanField(inDezeAanvraag, false, false));
        r.addValue(inh.getValueField());
        r.addValue(date2str(inh.getDatumEindeGeldigheid()));
        r.addValue(inh.getDocumentNr());
        r.addValue(inh.getDocumentType().getOms());

      } else {
        if (inh.isAutomatischInhouden()) {
          Record r = addRecord(inh);
          inh.setValueField(new BooleanField(true, true, true));
          r.addValue(inh.getValueField());
          r.addValue(date2str(inh.getDatumEindeGeldigheid()));
          r.addValue(inh.getDocumentNr());
          r.addValue(inh.getDocumentType().getOms());

        } else {
          Record r = addRecord(inh);
          inh.setValueField(new BooleanField(inDezeAanvraag, false, true));
          r.addValue(inh.getValueField());
          r.addValue(date2str(inh.getDatumEindeGeldigheid()));
          r.addValue(inh.getDocumentNr());
          r.addValue(inh.getDocumentType().getOms());
        }
      }
    });
  }

  public static class BooleanField extends ProNativeSelect {

    public BooleanField(Boolean initialValue, boolean required, boolean allowed) {
      this.addContainerProperty("Omschrijving", String.class, "");
      setWidth("100%");
      setNullSelectionAllowed(false);
      if (allowed) {
        setReadOnly(required);
        addItem(true)
            .getItemProperty("Omschrijving")
            .setValue(required ? "Ja, vanwege vervanging" : "Ja");

        if (!required) {
          addItem(false)
              .getItemProperty("Omschrijving")
              .setValue("Nee");
        }
      } else {
        setReadOnly(true);
        addItem(false)
            .getItemProperty("Omschrijving")
            .setValue("Ja, bij andere bezorging");
      }

      setItemCaptionPropertyId("Omschrijving");
      setValue(initialValue);
    }
  }

  public List<BezorgingInhouding> getReisdocumenten() {
    return getAllValues(BezorgingInhouding.class);
  }
}
