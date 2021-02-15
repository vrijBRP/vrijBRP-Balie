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

package nl.procura.rdw.generate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.xml.sax.ErrorHandler;

import com.sun.codemodel.*;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.generator.bean.ClassOutlineImpl;
import com.sun.tools.xjc.generator.bean.MethodWriter;
import com.sun.tools.xjc.model.CPropertyInfo;
import com.sun.tools.xjc.model.CTypeInfo;
import com.sun.tools.xjc.outline.Aspect;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.FieldOutline;
import com.sun.tools.xjc.outline.Outline;

public class CustomPlugin2 extends Plugin {

  private ClassOutlineImpl outline;
  private CPropertyInfo    prop;
  private JType            implType;
  private JType            exposedType;

  private JFieldVar field;

  @Override
  public String getOptionName() {
    return "Xpm2";
  }

  @Override
  public String getUsage() {
    return "  -Xpm2    : Change members visibility to private";
  }

  @Override
  public boolean run(Outline model, Options opt, ErrorHandler errorHandler) {

    FieldOutline[] fo = null;

    for (ClassOutline co : model.getClasses()) {
      for (JMethod x : new ArrayList<>(co.implClass.methods())) {
        co.implClass.methods().remove(x);
      }

      if (null != (fo = co.getDeclaredFields()) && 0 < fo.length) {
        for (int i = 0; i < fo.length; i++) {
          declareMethod((ClassOutlineImpl) co, fo[i]);
        }
      }
    }

    return true;
  }

  private void declareMethod(ClassOutlineImpl context, FieldOutline field) {

    MethodWriter writer = context.createMethodWriter();

    Map<String, JFieldVar> fields = null;

    outline = context;
    prop = field.getPropertyInfo();

    fields = outline.implClass.fields();

    this.field = fields.get(field.getPropertyInfo().getName(false));

    JMethod $get = writer.declareMethod(this.field.type(), "get" + camelCase(prop.getName(true)));

    JBlock getbody = $get.body();

    getbody._return(JExpr._this().ref(ref()));

    JMethod $set = writer.declareMethod(JType.parse(new JCodeModel(), "void"),
        "set" + camelCase(prop.getName(true)));

    $set.param(this.field.type(), prop.getName(true).toLowerCase());

    JBlock setbody = $set.body();

    setbody.assign(JExpr._this().ref(this.field.name()), JExpr.ref(this.field.name()));
  }

  private String camelCase(String name) {
    return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
  }

  protected final List<Object> listPossibleTypes(CPropertyInfo prop) {
    List<Object> r = new ArrayList<>();

    for (CTypeInfo tt : prop.ref()) {
      JType t = tt.getType().toType(outline.parent(), Aspect.EXPOSED);

      if (t.isPrimitive() || t.isArray()) {
        r.add(t.fullName());
      } else {
        r.add(t);
        r.add("\n");
      }
    }

    return r;
  }

  protected JFieldVar ref() {
    return field;
  }

  protected final JExpression castToImplType(JExpression exp) {
    return (implType == exposedType) ? exp : JExpr.cast(implType, exp);
  }
}
