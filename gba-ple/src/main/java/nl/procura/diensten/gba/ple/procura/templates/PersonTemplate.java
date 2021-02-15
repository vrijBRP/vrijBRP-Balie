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

package nl.procura.diensten.gba.ple.procura.templates;

import static nl.procura.burgerzaken.gba.core.enums.GBACat.*;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.pos;

import java.util.*;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.diensten.gba.ple.base.PLESkipException;
import nl.procura.diensten.gba.ple.procura.arguments.PLNumber;
import nl.procura.diensten.gba.ple.procura.templates.custom.CustomTemplate;
import nl.procura.diensten.gba.ple.procura.utils.PLStore;
import nl.procura.diensten.gba.ple.procura.utils.PLStore.Cat;
import nl.procura.diensten.gba.ple.procura.utils.PLStore.PL;
import nl.procura.diensten.gba.ple.procura.utils.SortableObject;
import nl.procura.diensten.gba.ple.procura.utils.SortableObjectConverter;
import nl.procura.gba.jpa.probev.db.*;

public class PersonTemplate extends PLETemplateProcura {

  private static String ORDER_KLADBLOK = "x.id.a1, x.id.a2, x.id.a3, x.id.vRegel";

  private final SortableObjectConverter converter = new SortableObjectConverter();
  private final Cat1InwTemplate         tInw      = new Cat1InwTemplate();
  private final Cat5HuwTemplate         tHuw      = new Cat5HuwTemplate();
  private final Cat2and3ParentTemplate  tParent   = new Cat2and3ParentTemplate();
  private final Cat10VbtTemplate        tVbt      = new Cat10VbtTemplate();
  private final Cat8VbTemplate          tVb       = new Cat8VbTemplate();
  private final Cat12ReisdTemplate      tReisd    = new Cat12ReisdTemplate();
  private final Cat6OverlTemplate       tOverl    = new Cat6OverlTemplate();
  private final Cat4NatioTemplate       tNat      = new Cat4NatioTemplate();
  private final Cat9KindTemplate        tKind     = new Cat9KindTemplate();
  private final Cat7InschrTemplate      tInschr   = new Cat7InschrTemplate();
  private final Cat11GezagTemplate      tGezag    = new Cat11GezagTemplate();
  private final Cat13KiesrTemplate      tkiesr    = new Cat13KiesrTemplate();
  private final Cat32WKTemplate         tWon      = new Cat32WKTemplate();
  private final Cat21VerwTemplate       tVerw     = new Cat21VerwTemplate();
  private final Cat34LokAfnIndTemplate  tLokAfn   = new Cat34LokAfnIndTemplate();
  private final Cat30DivTemplate        tDiv      = new Cat30DivTemplate();
  private final Cat33KladblokTemplate   tAant     = new Cat33KladblokTemplate();

  private List<PLNumber> numbers        = new ArrayList<>();
  private PLStore        plStore        = new PLStore();
  private CustomTemplate customTemplate = null;

  public void parse() {

    String template = getArguments().getCustomTemplate();

    if (fil(template)) {
      setCustomTemplate(loadCustomTemplate(template));
      getCustomTemplate().init(this);
    }

    addList(VERW, getArguments().hasCat(VERW), null, Verw.class, tVerw);
    addList(PERSOON, getArguments().hasCat(PERSOON), Inw.class, Xinw.class, tInw);
    addList(OUDER_1, getArguments().hasCat(OUDER_1), Mdr.class, Xmdr.class, tParent);
    addList(OUDER_2, getArguments().hasCat(OUDER_2), Vdr.class, Xvdr.class, tParent);
    addList(NATIO, getArguments().hasCat(NATIO), Nat.class, Xnat.class, tNat);
    addList(HUW_GPS, getArguments().hasCat(HUW_GPS), Huw.class, Xhuw.class, tHuw);
    addList(OVERL, getArguments().hasCat(OVERL), Overl.class, Xoverl.class, tOverl);
    addList(INSCHR, getArguments().hasCat(INSCHR), Inschr.class, Xinschr.class, tInschr);
    addList(VB, getArguments().hasCat(VB), Vb.class, Xvb.class, tVb);
    addList(KINDEREN, getArguments().hasCat(KINDEREN), Afst.class, Afst.class, tKind);
    addList(VBTITEL, getArguments().hasCat(VBTITEL), Vbt.class, Xvbt.class, tVbt);
    addList(GEZAG, getArguments().hasCat(GEZAG), Gezag.class, Xgezag.class, tGezag);
    addList(REISDOC, getArguments().hasCat(REISDOC), Reisd.class, Xreisd.class, tReisd);
    addList(KIESR, getArguments().hasCat(KIESR), Kiesr.class, Xkiesr.class, tkiesr);
    addList(DIV, getArguments().hasCat(DIV), Div.class, Xdiv.class, tDiv);
    addList(WK, getArguments().hasCat(WK), VboKrt.class, null, tWon, false);
    addList(KLADBLOK, getArguments().hasCat(KLADBLOK), AantPl.class, XaantPl.class, tAant, false, ORDER_KLADBLOK);
    addList(LOK_AF_IND, getArguments().hasCat(LOK_AF_IND), Aant3.class, Xaant3.class, tLokAfn, false);

    plStore.sort();
    Iterator<Long> it = plStore.getPs().keySet().iterator();

    while (it.hasNext()) {
      PL p = plStore.getPs().get(it.next());
      addPL();
      for (Cat c : p.getCats()) {
        if (c.getNr() != VERW.getCode() || getArguments().hasCat(VERW)) {
          parse(c.getNr(), p, c.getTemplate());
        }
      }
      getBuilder().finishPL();
    }
  }

  public List<PLNumber> getNumbers() {
    return numbers;
  }

  public void setNumbers(List<PLNumber> numbers) {
    this.numbers = numbers;
  }

  public CustomTemplate getCustomTemplate() {
    return customTemplate;
  }

  public void setCustomTemplate(CustomTemplate customTemplate) {
    this.customTemplate = customTemplate;
  }

  private CustomTemplate loadCustomTemplate(String template) {
    try {
      Class<?> aClass = Class.forName("nl.procura.diensten.gba.ple.procura.templates.custom." + template);
      return (CustomTemplate) aClass.newInstance();
    } catch (Exception e) {
      throw new RuntimeException("Fout bij laden template.", e);
    }
  }

  private void addList(GBACat cat, boolean catRequested, Class<?> currentClass, Class<?> archiveClass,
      PLETemplateProcura template, boolean hasHist) {
    addList(cat, catRequested, currentClass, archiveClass, template, hasHist, "");
  }

  private void addList(GBACat cat, boolean catRequested, Class<?> currentClass, Class<?> archiveClass,
      PLETemplateProcura template) {
    addList(cat, catRequested, currentClass, archiveClass, template, true, "");
  }

  private void addList(GBACat cat, boolean catRequested, Class<?> currentClass, Class<?> archiveClass,
      PLETemplateProcura template, boolean hasHist, String order) {

    if (catRequested) {
      addList(cat, getTable(subset(BRON_INW), currentClass, hasHist, order), template);

      if ((getArguments().isShowArchives() && (archiveClass != null)) || (currentClass == null)) {
        addList(cat, getTable(subset(BRON_VERW), archiveClass, hasHist, order), template);
      }

      if ((getArguments().isShowRemoved() && (archiveClass != null)) || (currentClass == null)) {
        addList(cat, getTable(subset(BRON_XINW), archiveClass, hasHist, order), template);
      }
    }
  }

  private void addList(GBACat cat, List<?> list, PLETemplateProcura template) {
    for (Object obj : list) {
      SortableObject so = converter.convert(obj);
      PL pl = plStore.getP(so);
      if (pl == null) {
        pl = plStore.newPL(so);
      }

      pl.addCat(cat.getCode(), so, template);
    }
  }

  private void parse(int code, PL p, PLETemplateProcura template) {
    Cat c = p.getCat(code);
    if (c != null) {
      for (SortableObject so : c.getObjects()) {
        try {
          if (getCustomTemplate() != null) {
            getCustomTemplate().parse(c.getNr(), so);
          } else {
            template.init(this);
            template.parse(so);
          }
        } catch (PLESkipException se) {
          // Hoeft niet te worden afgevangen.
        }
      }
    }
  }

  private Set<PLNumber> subset(int bron) {
    Set<PLNumber> nrs = new LinkedHashSet<>();
    for (PLNumber nummer : getNumbers()) {
      if (nummer.getSource() == bron) {
        if ((bron == BRON_VERW) && !getArguments().hasCat(VERW)) {
          continue;
        }

        nrs.add(nummer);
      }
    }

    return nrs;
  }

  private <T> List<T> getTable(Set<PLNumber> nummers, Class<T> c, boolean hasHist, String order) {
    if (c != null) {
      String n = c.getSimpleName();
      StringBuilder w = new StringBuilder();

      if (hasHist) {
        if (!getArguments().isShowHistory()) {
          if ((c == Inw.class) || (c == Xinw.class)) {
            w.append("x.hist in ('A','B','E','M','O','<')) and (");
          } else {
            w.append("x.hist in ('A', '<')) and (");
          }
        }
        if (!getArguments().isShowMutations()) {
          w.append("x.hist not in ('<')) and (");
        }
      }

      int i = 0;
      for (PLNumber nummer : nummers) {
        if (pos(nummer.getA1())) {
          if (i > 0) {
            w.append(" or ");
          }

          w.append("(x.id.a1 = ");
          w.append(nummer.getA1());
          w.append(" and x.id.a2 = ");
          w.append(nummer.getA2());
          w.append(" and x.id.a3 = ");
          w.append(nummer.getA3() + ")");
        }

        i++;
      }

      w.append(")");
      if (fil(order)) {
        w.append(" order by ");
        w.append(order);
      }

      if (i > 0) {
        String sql = (String.format("select x from %s x where (%s", n, w));
        return getEntityManager().createQuery(sql, c).getResultList();
      }
    }

    return new ArrayList<>();
  }
}
