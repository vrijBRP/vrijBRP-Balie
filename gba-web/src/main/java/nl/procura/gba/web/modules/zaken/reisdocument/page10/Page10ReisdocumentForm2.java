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

package nl.procura.gba.web.modules.zaken.reisdocument.page10;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.Page10ReisdocumentBean2.*;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.fil;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhoudingenService;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentType;
import nl.procura.gba.web.services.zaken.reisdocumenten.SignaleringResult;

public class Page10ReisdocumentForm2 extends ReadOnlyForm<Page10ReisdocumentBean2> {

  private final BasePLExt                  pl;
  private final DocumentInhoudingenService db;
  private final SignaleringResult          signalering;

  public Page10ReisdocumentForm2(BasePLExt pl, DocumentInhoudingenService db, SignaleringResult signalering) {

    this.pl = pl;
    this.db = db;
    this.signalering = signalering;

    setCaption("Indicaties");
    setReadThrough(true);
    setOrder(NLNATIONALITEIT, NOGDOCINLEVEREN, SIGNALERING, DERDEGEZAG, VERBLIJFSTITEL, STAATLOZE);
    setColumnWidths(WIDTH_130, "500px", "160px", "");

    reCheck();
  }

  public void checkDerdeGezag(ReisdocumentType type) {

    String dg = pl.getGezag().heeftGezagDerden() ? setClass("red", "Ja, derde gezag") : "";
    boolean idKaart = (type == ReisdocumentType.NEDERLANDSE_IDENTITEITSKAART);
    String ct = (pl.getGezag().staatOnderCuratele()
        ? (idKaart ? "Ja, onder curatele, maar n.v.t. op dit document"
            : setClass(
                "red", "Ja, onder curatele"))
        : "");

    getBean().setDerdeGezag(fil(dg) ? dg : (fil(ct) ? ct : setClass("green", "Nee")));
  }

  public void checkSignalering() {
    String s = isSprakeVanSignalering() ? setClass("red", "Ja") : setClass("green", "Nee");
    getBean().setSignalering(s);
  }

  public boolean isSprakeVanSignalering() {
    return signalering != null;
  }

  public boolean moetNogDocumentInleveren() {
    return db.moetNogInleveren(pl);
  }

  public void recheckDocumenten() {

    String isNogDoc = moetNogDocumentInleveren()
        ? setClass("red", "Ja, nog " + db.getInTeLeverenDocumenten(pl).size())
        : "";
    getBean().setNogDocInleveren(fil(isNogDoc) ? isNogDoc : setClass("green", "Nee"));
  }

  private void reCheck() {

    Page10ReisdocumentBean2 b = new Page10ReisdocumentBean2();

    setBean(b);

    boolean isNL = pl.getNatio().isNederlander();
    boolean isNLB = pl.getNatio().isBehandeldAlsNederlander();

    b.setNlNationaliteit(
        isNL ? setClass("green", "Ja") : (isNLB ? "Behandeld als Nederlander" : setClass("red", "Nee")));

    recheckDocumenten();

    long code = along(pl.getVerblijfstitel().getVerblijfstitel().getVal());
    String vbt = pl.getVerblijfstitel().getVerblijfstitel().getDescr();

    b.setVerblijfstitel((code > 0) ? (code + " - " + vbt) : "Geen");
    b.setStaatloze(pl.getNatio().isStaatloos() ? setClass("red", "Ja") : setClass("green", "Nee"));
  }
}
