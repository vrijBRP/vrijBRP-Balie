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

package nl.procura.gba.web.services.beheer.kassa;

import static nl.procura.gba.common.MiscUtils.copyList;
import static nl.procura.standard.Globalfunctions.*;

import java.util.List;

import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.jpa.personen.db.BaseEntity;
import nl.procura.gba.jpa.personen.db.Document;
import nl.procura.gba.jpa.personen.db.Kassa;
import nl.procura.gba.jpa.personen.db.Reisdoc;
import nl.procura.gba.web.services.interfaces.DatabaseTable;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.reisdocumenten.SoortReisdocument;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraagSoort;
import nl.procura.java.reflection.ReflectionUtil;

public class KassaProduct extends Kassa implements DatabaseTable {

  private DocumentRecord     kassaDocument       = DocumentRecord.getDefault();
  private SoortReisdocument  kassaReisdocument   = SoortReisdocument.getDefault();
  private List<KassaProduct> gekoppeldeProducten = null;

  public KassaProduct() {
    setKassaBundel(false);
    setDocument(new Document(BaseEntity.DEFAULT));
    setReisdoc(new Reisdoc(BaseEntity.DEFAULT));
    setcRijb(toBigDecimal(BaseEntity.DEFAULT));
  }

  public String getDescr() {
    if (KassaType.REISDOCUMENT.is(getKassaType())) {
      return getKassaReisdocument().toString();
    } else if (KassaType.RIJBEWIJS.is(getKassaType())) {
      return getKassaRijbewijs().getCode() + ": " + getKassaRijbewijs().getOms();
    } else if (KassaType.UITTREKSEL.is(getKassaType())) {
      return getKassaDocument().toString();
    }
    if (KassaType.ANDERS.is(getKassaType())) {
      return getProductgroep() + " / " + getAnders();
    }

    return getKassaType().getOms();
  }

  public List<KassaProduct> getGekoppeldeProducten() {
    if (gekoppeldeProducten == null) {
      gekoppeldeProducten = copyList(getSubKassas(), KassaProduct.class);
    }
    return gekoppeldeProducten;
  }

  public DocumentRecord getKassaDocument() {
    return kassaDocument;
  }

  public void setKassaDocument(DocumentRecord kassaDocument) {
    this.kassaDocument = kassaDocument;
    if (kassaDocument != null) {
      setDocument(ReflectionUtil.deepCopyBean(Document.class, kassaDocument));
    }
  }

  public SoortReisdocument getKassaReisdocument() {
    return kassaReisdocument;
  }

  public void setKassaReisdocument(SoortReisdocument kassaReisdocument) {
    this.kassaReisdocument = kassaReisdocument;
    if (kassaReisdocument != null) {
      setReisdoc(ReflectionUtil.deepCopyBean(nl.procura.gba.jpa.personen.db.Reisdoc.class, kassaReisdocument));
    }
  }

  public RijbewijsAanvraagSoort getKassaRijbewijs() {
    return RijbewijsAanvraagSoort.get(along(getcRijb()));
  }

  public void setKassaRijbewijs(RijbewijsAanvraagSoort soort) {
    setcRijb(toBigDecimal(soort.getCode()));
  }

  public KassaType getKassaType() {
    return KassaType.getType(aval(getType()));
  }

  public void setKassaType(KassaType type) {
    setType(toBigDecimal(type.getNr()));
  }

  public boolean heeftAlleGekoppeldeProducten(List<KassaProduct> list) {
    if (getGekoppeldeProducten().size() > 0) {
      for (KassaProduct gk : getGekoppeldeProducten()) {
        if (!list.contains(gk)) {
          return false;
        }
      }
      return true;
    }

    return false;
  }

  public boolean isGekoppeld(KassaProduct koppelProduct) {
    return MiscUtils.contains(koppelProduct, getSubKassas());
  }

  public boolean isKassaBundel() {
    return pos(getBundel());
  }

  public void setKassaBundel(boolean kassaBundel) {
    setBundel(toBigDecimal(kassaBundel ? 1 : 0));
  }

  public void koppel(KassaProduct koppelProduct) {
    getSubKassas().add(ReflectionUtil.deepCopyBean(Kassa.class, koppelProduct));
    koppelProduct.getParentKassas().add(ReflectionUtil.deepCopyBean(Kassa.class, this));
    gekoppeldeProducten = null;
  }

  public void ontKoppel(KassaProduct koppelProduct) {
    getSubKassas().remove(koppelProduct);
    koppelProduct.getParentKassas().remove(this);
    gekoppeldeProducten = null;
  }
}
