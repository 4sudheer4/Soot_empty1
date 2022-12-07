import soot.*;
import soot.jimple.*;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;
import conditions.SootConditionChecker;
import Soot.SootUtil;
import java.util.*;
import java.util.Iterator;
import java.util.*;
import conditions.SootConditionChecker;
import Soot.SootUtil;
import soot.*;
import soot.Value;
import soot.jimple.*;
import soot.util.*;
import soot.options.Options;
import soot.jimple.internal.*;
import soot.jimple.Jimple;
import soot.jimple.JimpleMethodSource;
import soot.ValueBox;
import org.jf.dexlib2.immutable.reference.ImmutableFieldReference;
import org.jf.dexlib2.iface.reference.FieldReference;
import soot.jimple.parser.parser.ParserException;
import soot.jimple.parser.lexer.LexerException;

import java.io.*;
import soot.options.Options;
import soot.jimple.internal.*;
import org.jf.dexlib2.immutable.reference.ImmutableFieldReference;
import org.jf.dexlib2.iface.reference.FieldReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.openjdk.jol.vm.VM;

public class VM1Transformer extends SceneTransformer {

    public static ArrayList<String> stringAdUnitsInserted = new ArrayList<>();
    private static final List<String> MethodsFoundArray = new ArrayList<String>();
    private static String[] StringArrayOfStaticInvokeMethodsToLookForAdSpecific = {"void initialize(android.content.Context,com.google.android.gms.ads.initialization.OnInitializationCompleteListener)"};

    @Override
    protected void internalTransform(String arg0, Map arg1) {
        SootClass mainClass = Scene.v().getMainClass(); // get main class
        SootConditionChecker sootconditionchecker = new SootConditionChecker();
        System.out.println("Methods: " + mainClass.getMethodCount()); // get methods count for class
        SootMethod testMethod = mainClass.getMethodByName("main"); // get method test from main class
        Body methodBody = testMethod.retrieveActiveBody();// get method body as an object



        //creating sootutil object

        SootUtil sootUtil = new SootUtil();

        StaticInvokeExpr StaticInvokeExpression = null;
        String MethodName = null;
        UnitPatchingChain units = methodBody.getUnits();
        for (Unit ut : methodBody.getUnits()) {
            //List<ValueBox> defBoxes = ut.getDefBoxes();
            System.out.println("units: " +ut); //printing how units looks like
            Stmt s = (Stmt) ut;

            boolean LastKnownUnitIsAStmt = sootconditionchecker.LastKnownUnitIsAStatement(ut);
            /* code behind the above
            if (LastKnownUnit instanceof Stmt) {
                return true;
            }
             */

            if(LastKnownUnitIsAStmt)
            {
                for (ValueBox vb : ut.getUseBoxes()) {

                    Value v = vb.getValue();

                    // check if invoke is a static invoke
                    //checking this with the help of code in conditions package

                    boolean SootValueIsAStaticInvokeExpr = sootconditionchecker.ValueIsAStaticInvokeExpr(v);

                    if (SootValueIsAStaticInvokeExpr) {
                        System.out.println("there is a static invoke_____");
                    }

                    //getting which method the Main is invoking

                    if (SootValueIsAStaticInvokeExpr) {
                        StaticInvokeExpression = (StaticInvokeExpr) v;
                        MethodName = StaticInvokeExpression.getMethod().getName().toString();
                        System.out.println("calling static invoke method name is: " + MethodName);
                        sootUtil.IterateOverListAndInsertLogMessage("", StringArrayOfStaticInvokeMethodsToLookForAdSpecific, ut, units, MethodName, true);
                    }


                }
            }
        }
    }



    private static Local localDef(Unit u) {
        List<ValueBox> defBoxes = u.getDefBoxes();
        int size = defBoxes.size();

        if (size == 0) {
            return null;
        }

        if (size != 1) {
            throw new RuntimeException();
        }
        ValueBox vb = defBoxes.get(0);
        Value v = vb.getValue();

        if (!(v instanceof Local)) {
            return null;
        }
        return (Local) v;
    }
}