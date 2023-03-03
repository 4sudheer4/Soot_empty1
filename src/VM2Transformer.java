import soot.*;
import soot.jimple.*;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;
import conditions.SootConditionChecker;
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

public class VM2Transformer extends SceneTransformer {

    public static ArrayList<String> stringAdUnitsInserted = new ArrayList<>();
    private static final List<String> MethodsFoundArray = new ArrayList<String>();
    public static String publicVariableStringClassToInjectAdlistener = null;
    public static String publicVariableStringClassToInject = null;
    private static boolean runOnce = true;
    public static boolean hasAdListener = false;
    public static String StringMethodToInvestigate;
    public static String StringClassToInvestigate;

    private static boolean publicVariableBooleanRunImplementationOnce = true;
    private static SootClass publicVariableSootClass;

    private static String[] StringArrayOfSpecialInvokeMethodsToLookForAdSpecificMultipleTimesSearchFor = {"java.net.URL: void <init>(java.lang.String)"};

    private static String[] StringArrayOfVirtualInvokeMethodsToLookForAdSpecific = {"java.net.URL: java.net.URLConnection openConnection()","java.net.HttpURLConnection: void setRequestMethod(java.lang.String)", "com.google.android.gms.ads.interstitial.InterstitialAd: void show(android.app.Activity)", "com.google.android.gms.ads.AdView: void loadAd(com.google.android.gms.ads.AdRequest)", "com.google.android.gms.ads.AdView: void setAdUnitId(java.lang.String)"};


    @Override
    protected void internalTransform(String arg0, Map arg1) {
        SootClass mainClass = Scene.v().getMainClass(); // get main class
        SootConditionChecker sootconditionchecker = new SootConditionChecker();
        System.out.println("Methods: " + mainClass.getMethodCount()); // get methods count for class
        SootMethod testMethod = mainClass.getMethodByName("main"); // get method test from main class
        Body methodBody = testMethod.retrieveActiveBody();// get method body as an object

        //InitBody(testMethod);
        //Created SootUtil class Object

        SootUtil sootUtil = new SootUtil();
        Local argument1, argument2;

        IterateOverUnitsandInjectAdSpecificCalls(methodBody, "http call","hash" );


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
                        System.out.println("there is a static invoke");
                    }

                    //getting which method the Main is invoking

                    if (SootValueIsAStaticInvokeExpr) {
                        StaticInvokeExpression = (StaticInvokeExpr) v;
                        MethodName = StaticInvokeExpression.getMethod().getName().toString();
                        System.out.println("calling static invoke method name is: " + MethodName);
                    }


                }
            }
        }
    }

    public static void Print(String stringvalue)
    {
        System.out.println(stringvalue);
    }

    //run through units to understand whether if the methods calls are in the set we are interested in
    public static void IterateOverUnitsandInjectAdSpecificCalls(Body body, String App_Name, String Hash) {
        SootConditionChecker sootconditionchecker = new SootConditionChecker();
        UnitPatchingChain units = body.getUnits();
        String MethodNameOfInterst = null;

        for (Iterator<Unit> unit = units.snapshotIterator(); unit.hasNext(); ) {
            Unit LastKnownUnit = unit.next();
            String StringLastKnownUnit = LastKnownUnit.toString();
            boolean LastKnownUnitIsAStmt = sootconditionchecker.LastKnownUnitIsAStatement(LastKnownUnit);
            if (StringLastKnownUnit.contains("(java.net.HttpURLConnection) $r2")) {
                Print("FOUND:" + StringLastKnownUnit);
            }
            if (LastKnownUnitIsAStmt) {
                for (ValueBox SootValuebox : LastKnownUnit.getUseBoxes()) {
                    Value SootValue = SootValuebox.getValue();
                    VirtualInvokeExpr VirtualInvokeExpression = null;
                    SpecialInvokeExpr SpecialInvokeExpression = null;
                    StaticInvokeExpr StaticInvokeExpression = null;
                    String MethodName = null;
                    boolean SootValueIsAVirtualInvokeExpr = sootconditionchecker.ValueIsAVirtualInvokeExpr(SootValue);
                    boolean SootValueIsASpecialInvokeExpr = sootconditionchecker.ValueIsASpecialInvokeExpr(SootValue);
                    boolean SootValueIsAStaticInvokeExpr = sootconditionchecker.ValueIsAStaticInvokeExpr(SootValue);


                    if (SootValueIsAVirtualInvokeExpr) {
                        VirtualInvokeExpression = (VirtualInvokeExpr) SootValue;
                        MethodName = VirtualInvokeExpression.getMethod().getName().toString();
/*
                        IterateOverListAndInsertLogMessage("" + App_Name + "::" + Hash + "::", StringArrayOfVirtualInvokeMethodsToLookForNotAdSpecific, LastKnownUnit, units, MethodName + ":", false);
                        IterateOverListAndInsertLogMessage(App_Name + "::" + Hash + "::", StringArrayOfVirtualInvokeMethodsToLookForAdSpecificMultipleTimesSearchFor, LastKnownUnit, units, MethodName + ":", false);
*/
                        IterateOverListAndInsertLogMessage(App_Name + "::" + Hash + "::", StringArrayOfVirtualInvokeMethodsToLookForAdSpecific, LastKnownUnit, units, MethodName + ":", true);


                        if (StringLastKnownUnit.contains("java.net.URL: java.net.URLConnection openConnection()") || StringLastKnownUnit.contains("void loadAd(com.google.android.gms.ads.AdRequest)"))
                        {
                            Print("FOUND LOAD METHOD:" + MethodName);
                            StringMethodToInvestigate = body.getMethod().getSignature().toString();
                            StringClassToInvestigate = body.getMethod().getDeclaringClass().toString();
                        }
                        if (StringLastKnownUnit.contains("java.net.HttpURLConnection: void setRequestMethod(java.lang.String)") || StringLastKnownUnit.contains("void loadAd(com.google.android.gms.ads.AdRequest)"))
                        {
                            Print("FOUND LOAD METHOD:" + MethodName);
                            StringMethodToInvestigate = body.getMethod().getSignature().toString();
                            StringClassToInvestigate = body.getMethod().getDeclaringClass().toString();
                        }
                    }
                    //if(SootValueIsAStaticInvokeExpr & StringLastKnownUnit.contains("google")){
                    if (SootValueIsAStaticInvokeExpr) {
                        StaticInvokeExpression = (StaticInvokeExpr) SootValue;
                        MethodName = StaticInvokeExpression.getMethod().getName().toString();
                       // IterateOverListAndInsertLogMessage(App_Name + "::" + Hash + "::" + "", StringArrayOfStaticInvokeMethodsToLookForAdSpecific, LastKnownUnit, units, MethodName, true);
                    }
                    if (SootValueIsASpecialInvokeExpr) {
                        SpecialInvokeExpression = (SpecialInvokeExpr) SootValue;
                        MethodName = SpecialInvokeExpression.getMethod().getName().toString();
//                        IterateOverListAndInsertLogMessage(App_Name + "::" + Hash + "::", StringArrayOfSpecialInvokeMethodsToLookForAdSpecific, LastKnownUnit, units, MethodName + ":", true);
                        IterateOverListAndInsertLogMessage(App_Name + "::" + Hash + "::", StringArrayOfSpecialInvokeMethodsToLookForAdSpecificMultipleTimesSearchFor, LastKnownUnit, units, MethodName, false);
//                        IterateOverListAndInsertLogMessage(App_Name + "::" + Hash + "::" + "", StringArrayOfSpecialInvokeMethodsToLookForNotAdSpecific, LastKnownUnit, units, MethodName + ":", false);
                    }
                }
            }
        }
    }

    //we pass units that we are interested, in the above code and fetch memory location of them along with building a log message which can be used to insert.
    private static void IterateOverListAndInsertLogMessage(String InputMsg, String[] ArrayOfStatements, Unit LastKnownUnit, UnitPatchingChain units, String MethodName, boolean AdSpecific)
    {

        for (String StringMethod : ArrayOfStatements)
        {
            if(LastKnownUnit.toString().contains(StringMethod) & !AdSpecific)
            {
                String Message = MethodName + ":" + LastKnownUnit.toString();
                Local local = localDef(LastKnownUnit);
                if(local != null){
                    InsertLogMessageAfterUnit(InputMsg + Message +"---Memory Location of "+local.toString()+" is "+ VM.current().addressOf(local), LastKnownUnit, units);
                    local = null;
                }else{
                    InsertLogMessageAfterUnit(InputMsg + Message+"---null", LastKnownUnit, units);
                }
            }
            if(LastKnownUnit.toString().contains(StringMethod) & AdSpecific)
            {
                String Message = MethodName + ":" + LastKnownUnit.toString();
                if(!MethodsFoundArray.contains(StringMethod))
                {
                    MethodsFoundArray.add(StringMethod);
                    Local local = localDef(LastKnownUnit);
                    if(local != null){
                        InsertLogMessageAfterUnit(InputMsg + Message +"---Memory Location of "+local.toString()+" is "+ VM.current().addressOf(local), LastKnownUnit, units);
                        local = null;
                    }else{
                        InsertLogMessageAfterUnit(InputMsg + Message+"---null", LastKnownUnit, units);
                    }
                }
            }
        }
    }
    //this method will help you insert above generated logs after the units we are interested in.
    public static void InsertLogMessageAfterUnit(String Message, Unit LastKnownUnit, UnitPatchingChain units)
    {
        List<Value> listArgs = new ArrayList<Value>();
        listArgs.add(StringConstant.v("FiniteState"));
        listArgs.add(StringConstant.v(Message));

        SootClass mainClass = Scene.v().getMainClass(); // get main class

        //StaticInvokeExpr SM = Jimple.v().newStaticInvokeExpr(Scene.v().getMainClass().getMethodByName("main").makeRef(), listArgs);

        //System.out.println(SM);

        StaticInvokeExpr LogInvokeStmt = Jimple.v().newStaticInvokeExpr(Scene.v().getMainClass().getMethodByName("main").makeRef(), listArgs);
        InvokeStmt InvokeStatementLog = Jimple.v().newInvokeStmt(LogInvokeStmt);
        String stringInvokeStatementLog = InvokeStatementLog.toString();
        Print("Message:"+Message);
        // if(!stringInvokeStatementLog.contains("findView")){
        Print("static invoke In insert Log after unit ******" + stringInvokeStatementLog);
        // }
        String stringLastAdUnitInserted = InvokeStatementLog.toString();
        Print("LastKnownUnit TEST:"+LastKnownUnit.toString()+"\nNew Unit:"+InvokeStatementLog.toString());
        int intStringAdUnitsInsertedSize= stringAdUnitsInserted.size()-1;
        System.out.println("string units inserted"+intStringAdUnitsInsertedSize);
        if(intStringAdUnitsInsertedSize > 0){
            if(!stringLastAdUnitInserted.contains(stringAdUnitsInserted.get(stringAdUnitsInserted.size()-1))){
                stringAdUnitsInserted.add(new String(Message));
                units.insertAfter(InvokeStatementLog, LastKnownUnit);
                if(InvokeStatementLog.toString().contains("ADRELATED")){
                    Print("Injecting"+InvokeStatementLog.toString());
                }
            }
        }else{
            stringAdUnitsInserted.add(new String(stringInvokeStatementLog.toString()));
            units.insertAfter(InvokeStatementLog, LastKnownUnit);
            Print("LastKnownUNIT:"+LastKnownUnit);
            Print("Injecting"+InvokeStatementLog.toString());
        }

        // units.insertAfter(InvokeStatementLog, LastKnownUnit);
    }
    public static AssignStmt newAssignStmt(Value variable, Value rightvalue)
    {
        return new JAssignStmt(variable, rightvalue);
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