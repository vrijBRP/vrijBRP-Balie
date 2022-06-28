/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.modules.beheer.verkiezing.page3.exporting;

import java.io.*;
import java.lang.annotation.ElementType;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import nl.procura.gba.jpa.personen.db.KiesrVerk;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.services.beheer.verkiezing.StempasQuery;
import nl.procura.gba.web.services.beheer.verkiezing.StempasResult;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.standard.ProcuraDate;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.component.container.ArrayListContainer;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.functies.downloading.DownloadHandlerImpl;

import au.com.bytecode.opencsv.CSVWriter;
import lombok.Data;

public class ROSExportWindow extends GbaModalWindow {

  private static final String F_AANTAL          = "aantal";
  private static final String F_BESTANDINHOUD   = "bestandInhoud";
  private static final String F_SCHEIDINGSTEKEN = "scheidingsteken";

  private StempasResult   result;
  private final KiesrVerk verkiezing;

  public ROSExportWindow(KiesrVerk verkiezing) {
    super("Exporteren ROS (Escape om te sluiten)", "500px");
    this.verkiezing = verkiezing;
  }

  @Override
  public void attach() {
    super.attach();
    addComponent(new MainModuleContainer(false, new Page()));
  }

  public class Page extends NormalPageTemplate {

    private Form form;

    public Page() {
      super("");
    }

    @Override
    public void event(PageEvent event) {
      if (event.isEvent(InitPage.class)) {
        result = getServices().getKiezersregisterService().getStempassen(StempasQuery
            .builder(verkiezing)
            .opgenomenInROS(true)
            .build());
        form = new Form();
        addButton(buttonSave, 1f);
        addButton(buttonClose);
        addComponent(form);
        buttonSave.setCaption("Exporteren (F9)");
      }
      super.event(event);
      buttonClose.focus();
    }

    @Override
    public void onClose() {
      getWindow().closeWindow();
    }

    @Override
    public void onSave() {
      form.commit();

      ROSExport rosExport = form.getBean().getBestandInhoud();
      ROSScheidingstekenType scheidingsteken = form.getBean().getScheidingsteken();
      List<String[]> lines = rosExport.getExport(result.getStempassen(), getServices());
      byte[] bytes = exportCsv(scheidingsteken, lines);
      DownloadHandlerImpl downloadHandler = new DownloadHandlerImpl(getWindow().getParent());
      downloadHandler.download(new ByteArrayInputStream(bytes), getBestandsnaam(), true);
    }

    private String getBestandsnaam() {
      return String.format("ROS-%s-%s-%s.csv",
          verkiezing.getAfkVerkiezing(),
          getServices().getGebruiker().getGemeenteCode(),
          new ProcuraDate().getSystemDate());
    }

    private byte[] exportCsv(ROSScheidingstekenType scheidingsteken, List<String[]> lines) {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      CSVWriter csv;

      try {
        csv = new CSVWriter(new OutputStreamWriter(bos, StandardCharsets.UTF_8), scheidingsteken.getKarakter(), '"');
        lines.forEach(csv::writeNext);
        try {
          csv.flush();
        } finally {
          IOUtils.closeQuietly(csv);
        }
        return bos.toByteArray();
      } catch (IOException e) {
        throw new ProException("Fout bij maken spreadsheet", e);
      } finally {
        IOUtils.closeQuietly(bos);
      }
    }
  }

  public class Form extends GbaForm<Bean> {

    public Form() {
      setOrder(F_AANTAL, F_BESTANDINHOUD, F_SCHEIDINGSTEKEN);
      setColumnWidths(WIDTH_130, "");
      Bean bean = new Bean();
      bean.setAantal(result.getAantal());
      setBean(bean);
    }

    @Override
    public void afterSetBean() {
      List<ROSExport> exports = new ArrayList<>();
      exports.add(new ROSExport1());
      ArrayListContainer formatenContainer = new ArrayListContainer(exports);
      ArrayListContainer scheidingstekenContainer = new ArrayListContainer(ROSScheidingstekenType.values());
      getField(F_BESTANDINHOUD, GbaNativeSelect.class).setContainerDataSource(formatenContainer);
      getField(F_SCHEIDINGSTEKEN, GbaNativeSelect.class).setContainerDataSource(scheidingstekenContainer);
    }
  }

  @Data
  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public static class Bean implements Serializable {

    @Field(type = Field.FieldType.LABEL,
        caption = "Aantal stempassen")
    private Object aantal;

    @Field(customTypeClass = GbaNativeSelect.class,
        caption = "Soort uitvoer",
        width = "250px",
        required = true)
    private ROSExport bestandInhoud;

    @Field(customTypeClass = GbaNativeSelect.class,
        caption = "Scheidingsteken",
        width = "250px",
        required = true)
    private ROSScheidingstekenType scheidingsteken;
  }
}
