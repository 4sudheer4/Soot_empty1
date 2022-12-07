package ClassHelper;

import java.util.ArrayList;
import soot.*;
import soot.Value;
import soot.jimple.*;
import soot.util.*;
import soot.options.Options;
import soot.jimple.internal.*;
import soot.jimple.Jimple;
import soot.jimple.JimpleMethodSource;
import soot.ValueBox;
//import soot.jimple.internal.JAssignStmt.LinkedVariableBox;
//import soot.jimple.internal.JAssignStmt.LinkedRValueBox;
import org.jf.dexlib2.immutable.reference.ImmutableFieldReference;
import org.jf.dexlib2.iface.reference.FieldReference;
import Soot.SootUtil;


public class ClassLiteralMethodSourceonAdClicked implements soot.MethodSource {
    public String strClassToInject = null;
    public soot.Body getBody(soot.SootMethod sootMethod, String phaseName) {
        // create empty body
        SootUtil sootUtil = new SootUtil();
        // String strClassToInject = "com.google.android.gms.example.bannerexample.MyActivity$20";
        JimpleBody classBody = Jimple.v().newBody(sootMethod);
        // method.setActiveBody(classBody);
        Chain units = classBody.getUnits();
        Local arg1, arg2, tmpRef;

        arg1 = sootUtil.NewLocal("r0", RefType.v(strClassToInject)); //ERRORS out if you use $20 instead of $2
        classBody.getLocals().add(arg1);

        // Generate r0 := @this: com.google.android.gms.example.bannerexample.MyActivity$2;
        IdentityStmt thisStmt = sootUtil.NewIdentityStmtParameterRefThis(strClassToInject, 0, arg1);
        units.add(thisStmt);
        return classBody;

    }
}