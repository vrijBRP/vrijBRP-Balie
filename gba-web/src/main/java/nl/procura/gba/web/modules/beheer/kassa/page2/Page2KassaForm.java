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

package nl.procura.gba.web.modules.beheer.kassa.page2;

import static java.util.Arrays.asList;
import static nl.procura.gba.web.modules.beheer.kassa.page2.Page2KassaBean.*;
import static nl.procura.standard.Globalfunctions.pos;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import nl.procura.gba.web.components.containers.KassaTypeContainer;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.listeners.FieldChangeListener;
import nl.procura.gba.web.services.beheer.kassa.KassaProduct;
import nl.procura.gba.web.services.beheer.kassa.KassaType;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.gba.web.services.zaken.reisdocumenten.SoortReisdocument;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraagSoort;
import nl.procura.vaadin.component.container.ArrayListContainer;

public class Page2KassaForm extends GbaForm<Page2KassaBean> {

  private final List<DocumentRecord>         opgeslagenKassaUittreksels    = new ArrayList<>();
  private final List<SoortReisdocument>      opgeslagenKassaReisdocumenten = new ArrayList<>();
  private final List<RijbewijsAanvraagSoort> opgeslagenKassaRijbewijzen    = new ArrayList<>();
  private KassaProduct                       kassaProduct;

  public Page2KassaForm(KassaProduct kassa) {

    setOrder(KASSA, KASSATYPE, DOCUMENT, REISDOCUMENT, RIJBEWIJS, PRODUCTGROEP, PRODUCT, BUNDEL);
    setColumnWidths("100px", "");
    kassaProduct = kassa;

    initFields(kassa);
  }

  @Override
  public void attach() {
    updateFields();
    super.attach();
  }

  public void checkBundel() {
    checkBundel(kassaProduct.isStored() && isAnders() && isBundel());
  }

  @Override
  public void reset() {
    super.reset();
    kassaProduct = new KassaProduct();
    initFields(kassaProduct);
    updateFields();
  }

  @SuppressWarnings("unused")
  protected void checkBundel(Boolean bundel) {
  }

  /**
   * Deze functie controleert per documenttype of alle beschikbare documenten van dit type al opgeslagen zijn.
   * Zo ja dan wordt dit documenttype verwijderd uit de keuze voor kassatypen.
   */

  private void checkDocKassaTypes(EnumSet<KassaType> kassaTypeSet) {

    KassaType[] docKassaTypes = { KassaType.UITTREKSEL };

    for (KassaType kassaType : docKassaTypes) {
      List<DocumentRecord> containerDocs = getContainerUittreksels(kassaType);

      if (containerDocs.isEmpty()) {
        kassaTypeSet.remove(kassaType);
      }
    }
  }

  /**
   * Deze functie verwijdert alle opgeslagen kassatypes die geen formulier, naturalisatie, optie, uittreksel
   * of reisdocument zijn uit de keuzemogelijkheden.
   */

  private void checkOtherTypes(EnumSet<KassaType> kassaTypeSet) {

    List<KassaProduct> list = getApplication().getServices().getKassaService().getKassaProducten();

    for (KassaProduct kassaProduct : list) {
      KassaType kassaType = kassaProduct.getKassaType();

      if (!kassaType.is(KassaType.ANDERS, KassaType.UITTREKSEL, KassaType.REISDOCUMENT, KassaType.RIJBEWIJS)) {
        kassaTypeSet.remove(kassaType);
      }
    }
  }

  /**
   * Deze functie controleert of alle beschikbare reisdocumenten reeds opgeslagen zijn. Zo ja, dan wordt het kassatype 'reisdocument' verwijderd
   * uit de keuze voor de kassatypen.
   */
  private void checkReisdocument(EnumSet<KassaType> kassaTypeSet) {

    if (new ReisdocumentContainer(opgeslagenKassaReisdocumenten).getAllowed().isEmpty()) {
      kassaTypeSet.remove(KassaType.REISDOCUMENT);
    }
  }

  /**
   * Deze functie controleert of alle beschikbare rijbewijzen opgeslagen zijn.
   */
  private void checkRijbewijs(EnumSet<KassaType> kassaTypeSet) {

    if (new RijbewijsContainer(opgeslagenKassaRijbewijzen).getAllowed().isEmpty()) {
      kassaTypeSet.remove(KassaType.RIJBEWIJS);
    }
  }

  /**
   * Retourneert alle nog niet gekozen kassatypes.
   */

  private KassaType[] getAllowedKassaTypes() {

    EnumSet<KassaType> kassaTypeSet = EnumSet.allOf(KassaType.class);

    checkDocKassaTypes(kassaTypeSet);
    checkReisdocument(kassaTypeSet);
    checkRijbewijs(kassaTypeSet);
    checkOtherTypes(kassaTypeSet);

    // het kassatype van het opgeslagen kassaProduct moet wel getoond worden
    if (kassaProduct.isStored()) {
      kassaTypeSet.add(kassaProduct.getKassaType());
    }

    return kassaTypeSet.toArray(new KassaType[0]);
  }

  /**
   * Deze functie geeft alle nog beschikbare documenten bij het gekozen type.
   *
   * @return een lijstje van alle nog beschikbare documenten bij het gekozen kassatype.
   */

  private List<DocumentRecord> getContainerUittreksels(KassaType kassaType) {

    List<DocumentRecord> containerDocs = new ArrayList<>();

    if (KassaType.UITTREKSEL.equals(kassaType)) {
      containerDocs = getApplication().getServices().getDocumentService().getDocumenten(
          DocumentType.PL_UITTREKSEL);
    }

    containerDocs.removeAll(opgeslagenKassaUittreksels);

    return containerDocs;
  }

  private void initFields(KassaProduct kassa) {

    Page2KassaBean bean = new Page2KassaBean();

    if (kassa.isStored()) {

      bean.setKassa(kassa.getKassa());
      bean.setBundel(kassa.isKassaBundel());
      bean.setKassaType(kassa.getKassaType());

      if (KassaType.REISDOCUMENT.is(kassa.getKassaType())) {
        bean.setReisdocument(kassa.getKassaReisdocument());
      } else if (KassaType.RIJBEWIJS.is(kassa.getKassaType())) {
        bean.setRijbewijs(kassa.getKassaRijbewijs());
      } else if (KassaType.UITTREKSEL.is(kassa.getKassaType())) {
        bean.setDocument(kassa.getKassaDocument());
      } else if (KassaType.ANDERS.is(kassa.getKassaType())) {
        bean.setProductgroep(kassa.getProductgroep());
        bean.setAnders(kassa.getAnders());
      }
    } else {
      bean.setKassaType(KassaType.ONBEKEND);
    }

    setBean(bean);
  }

  private boolean isAnders() {
    return KassaType.ANDERS.is((KassaType) getField(KASSATYPE).getValue());
  }

  private boolean isBundel() {
    Object value = getField(BUNDEL).getValue();
    return (value != null && (Boolean) value);
  }

  /**
   * Deze functie zorgt ervoor dat het document of reisdocument van een
   * aangeklikt kassadocument altijd getoond wordt.
   */

  private void removeItemFromList(KassaProduct kassaProduct) {
    DocumentRecord doc = kassaProduct.getKassaDocument();
    SoortReisdocument reisDoc = kassaProduct.getKassaReisdocument();
    RijbewijsAanvraagSoort rijbewijs = kassaProduct.getKassaRijbewijs();

    if (pos(doc.getCDocument())) { // we hebben een kassadocument aangeklikt
      opgeslagenKassaUittreksels.remove(doc);
    }
    if (pos(reisDoc.getCReisdoc())) { // we hebben een kassareisdocument aangeklikt
      opgeslagenKassaReisdocumenten.remove(reisDoc);
    }
    if (pos(rijbewijs.getCode())) { // we hebben een kassarijbewijs aangeklikt
      opgeslagenKassaRijbewijzen.remove(rijbewijs);
    }
  }

  private void setBundelListener() {

    getField(BUNDEL).addListener(new FieldChangeListener<Boolean>() {

      @Override
      public void onChange(Boolean bundel) {
        checkBundel(kassaProduct.isStored() && isAnders() && bundel);
      }
    });
  }

  /**
   * Deze functie zorgt ervoor dat alleen nog niet opgeslagen kassaTypes worden getoond.
   * Voor de types reisdocumenten, uittreksels, formulier, naturalisatie en optie moet gecontroleerd worden of alle beschikbare documenten
   * al opgeslagen zijn. In dat geval vervalt de keuze om een van deze types te selecteren.
   */

  private void setKassaTypeContainer() {

    KassaType[] allowedKassaTypes = getAllowedKassaTypes();

    GbaNativeSelect type = getField(KASSATYPE, GbaNativeSelect.class);
    type.setDataSource(new KassaTypeContainer(allowedKassaTypes));
    type.setValue(getBean().getKassaType());

    getField(KASSATYPE).addListener((ValueChangeListener) event -> setSpecificContainers());
  }

  private void setSpecificContainers() {

    KassaType kassaType = (KassaType) getField(KASSATYPE).getValue();

    getField(DOCUMENT).setVisible(KassaType.UITTREKSEL.is(kassaType));
    getField(REISDOCUMENT).setVisible(KassaType.REISDOCUMENT.is(kassaType));
    getField(RIJBEWIJS).setVisible(KassaType.RIJBEWIJS.is(kassaType));

    getField(PRODUCT).setVisible(KassaType.ANDERS.is(kassaType));
    getField(PRODUCTGROEP).setVisible(KassaType.ANDERS.is(kassaType));
    getField(BUNDEL).setVisible(KassaType.ANDERS.is(kassaType));

    GbaNativeSelect d1 = getField(DOCUMENT, GbaNativeSelect.class);
    d1.setDataSource(new DocumentContainer(kassaType));
    d1.setValue(getBean().getDocument());

    GbaNativeSelect d2 = getField(REISDOCUMENT, GbaNativeSelect.class);
    d2.setDataSource(new ReisdocumentContainer(opgeslagenKassaReisdocumenten));
    d2.setValue(getBean().getReisdocument());

    GbaNativeSelect d3 = getField(RIJBEWIJS, GbaNativeSelect.class);
    d3.setDataSource(new RijbewijsContainer(opgeslagenKassaRijbewijzen));
    d3.setValue(getBean().getRijbewijs());

    checkBundel(kassaProduct.isStored() && isAnders() && isBundel());

    repaint();
  }

  /**
   * Deze functie controleert of alle uittrekseldocumenten en reisdocumenten al zijn opgeslagen.
   */

  private void updateFields() {

    updateLists();

    if (kassaProduct.isStored()) {
      removeItemFromList(kassaProduct);
    }

    setBundelListener();
    setKassaTypeContainer();
    setSpecificContainers();
  }

  /**
   * Stel de lijsten met reeds-ingevoerde-documenten opnieuw samen.
   */
  private void updateLists() {

    opgeslagenKassaUittreksels.clear();
    opgeslagenKassaReisdocumenten.clear();
    opgeslagenKassaRijbewijzen.clear();

    List<KassaProduct> list = getApplication().getServices().getKassaService().getKassaProducten();

    for (KassaProduct kassaProcuct : list) {

      DocumentRecord uittreksel = kassaProcuct.getKassaDocument();
      SoortReisdocument reisdocument = kassaProcuct.getKassaReisdocument();
      RijbewijsAanvraagSoort rijbewijs = kassaProcuct.getKassaRijbewijs();

      if (pos(uittreksel.getCDocument()) && !opgeslagenKassaUittreksels.contains(uittreksel)) {
        opgeslagenKassaUittreksels.add(uittreksel);
      } else if (pos(reisdocument.getCReisdoc()) && !opgeslagenKassaReisdocumenten.contains(reisdocument)) {
        opgeslagenKassaReisdocumenten.add(reisdocument);
      } else if (pos(rijbewijs.getCode()) && !opgeslagenKassaRijbewijzen.contains(rijbewijs)) {
        opgeslagenKassaRijbewijzen.add(rijbewijs);
      }
    }
  }

  private class DocumentContainer extends ArrayListContainer {

    public DocumentContainer(KassaType kassaType) {

      if (KassaType.UITTREKSEL.equals(kassaType)) {
        addItems(getContainerUittreksels(KassaType.UITTREKSEL));
      }
    }
  }

  private class ReisdocumentContainer extends ArrayListContainer {

    final List<SoortReisdocument> allowed;

    public ReisdocumentContainer(List<SoortReisdocument> list) {

      allowed = getApplication().getServices().getReisdocumentService().getSoortReisdocumenten();
      allowed.removeAll(list);

      addItems(allowed);
    }

    public ArrayList<SoortReisdocument> getAllowed() {
      return (ArrayList<SoortReisdocument>) allowed;
    }
  }

  private class RijbewijsContainer extends ArrayListContainer {

    final List<RijbewijsAanvraagSoort> allowed = new ArrayList<>(asList(RijbewijsAanvraagSoort.values()));

    public RijbewijsContainer(List<RijbewijsAanvraagSoort> list) {

      allowed.remove(RijbewijsAanvraagSoort.ONBEKEND); // Onbekend sowieso niet gebruiken
      allowed.removeAll(list);

      addItems(allowed);
    }

    public List<RijbewijsAanvraagSoort> getAllowed() {
      return allowed;
    }
  }
}
