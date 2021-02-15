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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab5.page2;

import static nl.procura.gba.web.modules.beheer.documenten.page1.tab5.page2.Tab5DocumentenPage2Bean.*;

import java.util.Optional;
import java.util.stream.Collectors;

import javax.print.PrintService;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab5.PrintOptieValue;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab5.PrinterContainer;
import nl.procura.gba.web.services.zaken.documenten.printen.LocalPrinterUtils;
import nl.procura.gba.web.services.zaken.documenten.printopties.PrintOptie;
import nl.procura.gba.web.services.zaken.documenten.printopties.PrintOptieType;
import nl.procura.vaadin.component.container.ArrayListContainer;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class Tab5DocumentenPage2Form extends GbaForm<Tab5DocumentenPage2Bean> {

  private final PrinterContainer printerContainer;

  public Tab5DocumentenPage2Form(PrinterContainer printerContainer, PrintOptie printOptie) {
    this.printerContainer = printerContainer;
    setCaption("Printoptie");
    setReadThrough(true);
    setOrder(CODE, NAME, PRINTER, MEDIA, COLOR, LOCATION, COMMAND, BSM_HASHCODE, MO_BERICHTTYPE);
    setColumnWidths("200px", "");
    initFields(printOptie);
  }

  @Override
  public void reset() {
    super.reset();
    initFields(new PrintOptie());
  }

  private void initFields(PrintOptie printOptie) {

    Tab5DocumentenPage2Bean bean = new Tab5DocumentenPage2Bean();

    if (printOptie.isStored()) {
      bean.setName(printOptie.getOms());
      setPrinterDriver(bean, printOptie);
      bean.setLocation(printOptie.getLocatie());

      if (printOptie.isCommandPrinter()) {
        bean.setCommand(printOptie.getCmd());
      } else {
        bean.setMedia(new FieldValue(printOptie.getMedia()));
        bean.setColor(new FieldValue(printOptie.getKleur()));
      }

      bean.setBsmHashcode(printOptie.getBsmId());
      bean.setMoBerichtType(printOptie.getMoBerichttype());
    }

    setBean(bean);
    getField(PRINTER).addListener((ValueChangeListener) event -> updateFields());
    getField(PRINTER, GbaNativeSelect.class).setContainerDataSource(printerContainer);
    getField(PRINTER).setValue(bean.getPrinter());
    updateFields();
  }

  private void setLocalColorContainer(String name) {
    Optional<PrintService> printService = LocalPrinterUtils.getPrinter(name);

    if (printService.isPresent()) {
      ((GbaNativeSelect) getField(COLOR)).setDataSource(new LocalColorContainer(printService.get()));
      getField(COLOR).setValue(getBean().getColor());
    }
  }

  private void setLocalMediaContainer(String name) {
    Optional<PrintService> printService = LocalPrinterUtils.getPrinter(name);
    if (printService.isPresent()) {
      ((GbaNativeSelect) getField(MEDIA)).setDataSource(new LocalMediaContainer(printService.get()));
      getField(MEDIA).setValue(getBean().getMedia());
    }
  }

  private void setNetworkColorContainer(String name) {
    if (name != null) {
      ((GbaNativeSelect) getField(COLOR)).setDataSource(new NetworkColorContainer(name));
      getField(COLOR).setValue(getBean().getColor());
    }
  }

  private void setNetworkMediaContainer(String name) {
    if (name != null) {
      ((GbaNativeSelect) getField(MEDIA)).setDataSource(new NetworkMediaContainer(name));
      getField(MEDIA).setValue(getBean().getMedia());
    }
  }

  private void setPrinterDriver(String name) {
    getField(PRINTER).setValue(new FieldValue(name));
  }

  private static void setPrinterDriver(Tab5DocumentenPage2Bean bean, PrintOptie printOptie) {
    if (printOptie.getPrintoptie() == null) {
      bean.setPrinter(null);
    } else {
      bean.setPrinter(new PrintOptieValue(printOptie.getPrintType(), printOptie.getPrintoptie()));
    }
  }

  private void updateFields() {
    PrintOptieValue printOptie = (PrintOptieValue) getField(PRINTER).getValue();
    getField(MEDIA).setVisible(false);
    getField(COLOR).setVisible(false);
    getField(BSM_HASHCODE).setVisible(false);
    getField(MO_BERICHTTYPE).setVisible(false);
    getField(COMMAND).setVisible(false);

    if (printOptie != null) {
      if (printOptie.isMediaPrinter()) {
        setPrinterDriver(printOptie.getStringValue());
      }

      if (printOptie.isLocalPrinter()) {
        setLocalMediaContainer(printOptie.getStringValue());
        setLocalColorContainer(printOptie.getStringValue());

      } else if (printOptie.isNetworkPrinter()) {
        setNetworkMediaContainer(printOptie.getStringValue());
        setNetworkColorContainer(printOptie.getStringValue());
      }

      getField(MEDIA).setVisible(printOptie.isMediaPrinter());
      getField(COLOR).setVisible(printOptie.isMediaPrinter());
      getField(BSM_HASHCODE).setVisible(printOptie.isMijnOverheid());
      getField(MO_BERICHTTYPE).setVisible(printOptie.isMijnOverheid());
      getField(COMMAND).setVisible(printOptie.isCommandPrinter());
    }

    repaint();
  }

  public class NetworkColorContainer extends ArrayListContainer {

    private NetworkColorContainer(String name) {
      Optional<PrintOptieValue> value = printerContainer.get(PrintOptieType.NETWORK_PRINTER, name);
      if (value.isPresent()) {
        addItems(value.get().getNetworkPrinter()
            .getChromacities().stream()
            .map(c -> new FieldValue(c.toString().trim()))
            .collect(Collectors.toList()));
      }
    }
  }

  public class NetworkMediaContainer extends ArrayListContainer {

    private NetworkMediaContainer(String name) {
      Optional<PrintOptieValue> value = printerContainer.get(PrintOptieType.NETWORK_PRINTER, name);
      if (value.isPresent()) {
        addItems(value.get().getNetworkPrinter()
            .getMedia().stream()
            .map(c -> new FieldValue(c.toString().trim()))
            .collect(Collectors.toList()));
      }
    }
  }

  public static class LocalColorContainer extends ArrayListContainer {

    private LocalColorContainer(PrintService printService) {
      addItems(LocalPrinterUtils.getColors(printService).stream()
          .map(c -> new FieldValue(c.toString().trim()))
          .collect(Collectors.toList()));
    }
  }

  public static class LocalMediaContainer extends ArrayListContainer {

    private LocalMediaContainer(PrintService printService) {
      addItems(LocalPrinterUtils.getMedia(printService).stream()
          .map(m -> new FieldValue(m.toString().trim()))
          .collect(Collectors.toList()));
    }
  }
}
