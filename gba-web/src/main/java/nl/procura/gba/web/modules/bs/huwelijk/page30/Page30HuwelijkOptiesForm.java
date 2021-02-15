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

package nl.procura.gba.web.modules.bs.huwelijk.page30;

import static nl.procura.standard.Globalfunctions.*;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.vaadin.data.validator.AbstractStringValidator;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Form;

import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.services.bs.huwelijk.DossierHuwelijkOptie;
import nl.procura.gba.web.services.gba.basistabellen.huwelijkslocatie.HuwelijksLocatieOptieType;
import nl.procura.gba.web.services.gba.basistabellen.huwelijkslocatieOptie.HuwelijksLocatieOptie;
import nl.procura.vaadin.component.container.NLBooleanContainer;
import nl.procura.vaadin.component.field.NumberField;
import nl.procura.vaadin.component.layout.table.TableLayout;

public class Page30HuwelijkOptiesForm extends Form {

  private final HashMap<DossierHuwelijkOptie, AbstractField> map = new HashMap<>();
  private List<DossierHuwelijkOptie>                         opties;

  public Page30HuwelijkOptiesForm(List<DossierHuwelijkOptie> opties) {

    this.opties = opties;

    setStyleName("tableform");

    setValidationVisible(true);

    setWriteThrough(false);

    setValidationVisibleOnCommit(true);

    setRequiredError("Veld is verplicht");

    setLayout(new Layout());
  }

  @Override
  public void commit() {

    for (Entry<DossierHuwelijkOptie, AbstractField> entry : map.entrySet()) {

      AbstractField component = entry.getValue();

      String waarde = astr(component.getValue());

      entry.getKey().setWaarde(waarde);
    }

    super.commit();
  }

  public List<DossierHuwelijkOptie> getOpties() {

    commit();

    return opties;
  }

  public void setOpties(List<DossierHuwelijkOptie> opties) {
    this.opties = opties;
  }

  public class Layout extends TableLayout {

    public Layout() {

      List<DossierHuwelijkOptie> list = new ArrayList<>(opties);

      MiscUtils.sort(list);

      setColumnWidths("400px", "");

      for (DossierHuwelijkOptie huwOpt : list) {

        HuwelijksLocatieOptie opt = huwOpt.getOptie();

        addLabel(opt.getHuwelijksLocatieOptieOms());

        AbstractField field;

        switch (opt.getOptieType()) {

          case BOOLEAN:

            field = new GbaNativeSelect();
            field.setWidth("60px");

            GbaNativeSelect select = (GbaNativeSelect) field;
            select.setContainerDataSource(new NLBooleanContainer());
            select.setItemCaptionPropertyId(NLBooleanContainer.JA_NEE);
            select.setValue(fil(huwOpt.getWaarde()) ? isTru(huwOpt.getWaarde()) : null);

            break;

          case NUMBER:

            field = new NumberField();
            field.setWidth("60px");

            NumberField nveld = (NumberField) field;
            nveld.setValue(huwOpt.getWaarde());

            break;

          case TEXT:

            field = new GbaTextField();
            field.setWidth("300px");

            GbaTextField text = (GbaTextField) field;
            text.setMaxLength(200);
            text.setValue(huwOpt.getWaarde());

            break;

          default:
            throw new IllegalArgumentException("Onbekend optietype: " + opt.getOptieType());
        }

        field.addValidator(new Val(opt));
        field.setRequired(opt.isVerplichteOptie());
        field.setRequiredError("Dit veld is verplicht");

        String id = astr(opt.getCodeHuwelijksLocatieOptie());

        addData(id, field);
        addField(id, field);

        map.put(huwOpt, field);
      }
    }
  }

  public class Val extends AbstractStringValidator {

    private final HuwelijksLocatieOptie optie;

    private Val(HuwelijksLocatieOptie optie) {
      super(MessageFormat.format("De waarde moet tussen {0} en {1} liggen.", optie.getMin(), optie.getMax()));
      this.optie = optie;
    }

    @Override
    protected boolean isValidString(String waarde) {

      boolean isNumber = (optie.getOptieType() == HuwelijksLocatieOptieType.NUMBER);

      return emp(waarde) || !isNumber || (isNumber && along(waarde) >= along(optie.getMin()) && along(
          waarde) <= along(optie.getMax()));
    }
  }

}
