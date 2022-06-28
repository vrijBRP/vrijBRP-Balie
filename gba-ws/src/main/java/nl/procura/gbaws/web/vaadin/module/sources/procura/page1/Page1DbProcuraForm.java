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

package nl.procura.gbaws.web.vaadin.module.sources.procura.page1;

import static nl.procura.burgerzaken.gba.core.enums.GBACat.*;
import static nl.procura.gbaws.web.vaadin.module.sources.procura.page1.Page1DbProcuraBean.PERSON_ID;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import org.apache.commons.io.IOUtils;

import com.vaadin.data.validator.AbstractStringValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Field;

import nl.procura.burgerzaken.gba.numbers.Anr;
import nl.procura.burgerzaken.gba.numbers.Bsn;
import nl.procura.diensten.gba.ple.base.converters.json.BasePLToJsonConverter;
import nl.procura.diensten.gba.ple.procura.PLE;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.diensten.gba.ple.procura.utils.PLECommandLineUtils;
import nl.procura.diensten.gba.ple.procura.utils.jpa.PLEJpaManager;
import nl.procura.gbaws.db.jpa.PleJpaUtils;
import nl.procura.gbaws.web.vaadin.layouts.forms.DefaultForm;
import nl.procura.vaadin.component.layout.table.TableLayout;
import nl.procura.vaadin.functies.downloading.DownloadHandlerImpl;

public class Page1DbProcuraForm extends DefaultForm {

  public Page1DbProcuraForm(String caption, String... names) {
    setCaption(caption);
    setOrder(names);
    setColumnWidths("160px", "");
  }

  @Override
  public void afterSetColumn(TableLayout.Column column, Field field, Property property) {
    if (property.is(PERSON_ID)) {
      column.addComponent(new Button("Zoeken", (Button.ClickListener) clickEvent -> onSearch(field)));
      field.addValidator(new AbstractStringValidator("Geen A-nummer of BSN") {

        @Override
        protected boolean isValidString(String value) {
          return new Anr(value).isCorrect() || new Bsn(value).isCorrect();
        }
      });
    }
    super.afterSetColumn(column, field, property);
  }

  private void onSearch(Field field) {
    commit();

    Anr anr = new Anr(field.getValue().toString());
    Bsn bsn = new Bsn(field.getValue().toString());
    String id = anr.isCorrect() ? anr.toString() : bsn.toString();

    PLEArgs args = new PLEArgs();
    args.addCat(PERSOON, OUDER_1, OUDER_2, NATIO, HUW_GPS, OVERL, INSCHR, VB, KINDEREN, VBTITEL, GEZAG, KIESR, VERW);
    args.setShowHistory(false);
    args.setShowMutations(false);
    args.setShowArchives(true);
    args.setShowRemoved(true);
    args.addNummer(id);
    args.setDatasource(PLEDatasource.PROCURA);
    args.setMaxFindCount(1);

    String cl = PLECommandLineUtils.convert(args);
    args = PLECommandLineUtils.convert(cl);

    PLEJpaManager plEm = PleJpaUtils.createManager();
    PLE ple = new PLE(plEm, args);

    try {
      ple.find();
      ByteArrayOutputStream stream = new ByteArrayOutputStream();
      BasePLToJsonConverter.toStream(stream, ple.getBuilder().getResult());
      new DownloadHandlerImpl(Page1DbProcuraForm.this.getWindow()).download(
          new ByteArrayInputStream(stream.toByteArray()), "pl-" + id + ".json",
          true);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      IOUtils.closeQuietly(plEm);
    }
  }

  @Override
  public Object getNewBean() {
    return new Page1DbProcuraBean();
  }

  @Override
  public Page1DbProcuraBean getBean() {
    return (Page1DbProcuraBean) super.getBean();
  }
}
