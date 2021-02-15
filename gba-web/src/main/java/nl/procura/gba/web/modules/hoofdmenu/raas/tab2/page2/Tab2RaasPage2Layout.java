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

package nl.procura.gba.web.modules.hoofdmenu.raas.tab2.page2;

import static nl.procura.gba.web.modules.hoofdmenu.raas.tab2.page2.Tab2RaasPage2Bean.*;
import static nl.procura.vaadin.theme.twee.ProcuraTheme.ICOON_24.WARNING;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.ui.Button;

import nl.procura.dto.raas.bestand.ErrorDto;
import nl.procura.dto.raas.bestand.RaasBestandDto;
import nl.procura.gba.common.MiscUtils;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.layout.info.InfoLayout;

public class Tab2RaasPage2Layout extends VLayout {

  public final Button buttonZaak     = new Button("Toon zaak");
  public final Button buttonAanvraag = new Button("Toon RAAS aanvraag");
  public final Button buttonExport   = new Button("Exporteer bestand");

  public Tab2RaasPage2Layout(RaasBestandDto bestand) {
    setSizeFull();
    addComponent(new Form(bestand));
    List<ErrorDto> errors = bestand.getErrors().getAll();
    if (errors != null && !errors.isEmpty()) {
      addComponent(new InfoLayout("Inhoud bestand is niet valide", WARNING,
          MiscUtils.setClass(false, StringUtils.join(errors, "</br>"))));
    }
    addExpandComponent(new Tab2RaasPage2Table(bestand));
  }

  public class Form extends Tab2RaasPage2Form {

    public Form(RaasBestandDto bestand) {
      super(bestand);
      setCaption("Raasbestand");
    }

    @Override
    protected void createFields() {
      setOrder(TYPE, NAAM, AANVRAAGNUMMER, DATUM_TIJD, RICHTING, STATUS);
    }
  }
}
