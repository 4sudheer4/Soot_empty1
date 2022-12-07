package Soot;
import java.util.*;
import java.util.Iterator;
import java.util.*;
import conditions.SootConditionChecker;
import ClassHelper.ClassLiteralMethodSourceonAdClicked;

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
import soot.jimple.parser.parser.ParserException;
import soot.jimple.parser.lexer.LexerException;

import java.io.*;
import soot.options.Options;
import soot.jimple.internal.*;
import org.jf.dexlib2.immutable.reference.ImmutableFieldReference;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.openjdk.jol.vm.VM;

public class SootUtil
{
    private static List<String> MethodsFoundArray = new ArrayList<String>();
    private static String[] StringArrayOfVirtualInvokeMethodsToLookForAdSpecific = {"com.google.android.gms.ads.AdView: void loadAd(com.google.android.gms.ads.AdRequest", "performClick", "com.google.android.gms.ads.AdRequest build()", "com.google.android.gms.ads.interstitial.InterstitialAd: void show(android.app.Activity)", "com.google.android.gms.ads.AdView: void loadAd(com.google.android.gms.ads.AdRequest)", "com.google.android.gms.ads.AdView: void setAdUnitId(java.lang.String)"};
    private static String[] StringArrayOfVirtualInvokeMethodsToLookForAdSpecificMultipleTimesSearchFor = {"android.widget.FrameLayout: void addView(android.view.View)"};
    private static String[] StringArrayOfSpecialInvokeMethodsToLookForAdSpecific = {"onAdClicked()", "onAdClosed()", "void onAdImpression()", "AdListener: void onAdLoaded()", "com.google.android.gms.ads.AdSize getAdSize()"};
    private static String[] StringArrayOfSpecialInvokeMethodsToLookForAdSpecificMultipleTimesSearchFor = {"android.view.View findViewById(int)", "com.google.android.gms.ads.AdView: void <init>(android.content.Context)"};
    private static String[] StringArrayOfStaticInvokeMethodsToLookForAdSpecific = {"void initialize(android.content.Context,com.google.android.gms.ads.initialization.OnInitializationCompleteListener)"};
    public static ArrayList<String> stringAdUnitsInserted = new ArrayList<>();

    //NOT AD SPECIFIC
    public static String[] StringArrayOfVirtualInvokeMethodsToLookForNotAdSpecific = {"void setContentView(int)", "android.view.View findViewById(int)"};
    public static String[] StringArrayOfSpecialInvokeMethodsToLookForNotAdSpecific = {"void onCreate(android.os.Bundle)>($r1)"};
    private static boolean runOnce = true;
    public static boolean hasAdListener = false;
    public static String StringMethodToInvestigate;
    public static String StringClassToInvestigate;
    public static String publicVariableStringClassToInjectAdlistener = null;
    public static String publicVariableStringClassToInject = null;
    private static SootClass publicVariableSootClass;

    public static void Print(String stringvalue)
    {
        System.out.println(stringvalue);
    }

    // the following setting should be changed to the local path
    public static Local NewLocal(String strlocal, Type valueref)
    {
        return Jimple.v().newLocal(strlocal, valueref);
    }

    public static ValueBox newLocalBox(Value value)
    {
        return Jimple.v().newLocalBox(value);
    }

    public static IdentityStmt NewIdentityStmtParameterRef(String strClassToInjectAdListenerClass, int intVal, Local arg)
    {
        ParameterRef ParamRefInit = Jimple.v().newParameterRef(RefType.v(strClassToInjectAdListenerClass), intVal);
        return Jimple.v().newIdentityStmt(arg, ParamRefInit);
    }

    public static IdentityStmt NewIdentityStmtParameterRefThis(String strClassToInjectAdListenerClass, int intVal, Local arg)
    {
        ThisRef RefThis = Jimple.v().newThisRef(RefType.v(strClassToInjectAdListenerClass));
        return Jimple.v().newIdentityStmt(arg, RefThis);
    }

    public static void AddFinalFieldToSootClass(SootClass sClass, String strVar, String strClassToInjectAdListenerClass)
    {
        SootField field = Scene.v().makeSootField(strVar, RefType.v(strClassToInjectAdListenerClass), Modifier.FINAL);
        sClass.addField(field);
    }

    public static void InsertLogMessageAfterUnit(String Message, Unit LastKnownUnit, UnitPatchingChain units)
    {
        List<Value> listArgs = new ArrayList<Value>();
        listArgs.add(StringConstant.v("FiniteState"));
        listArgs.add(StringConstant.v(Message));
        StaticInvokeExpr LogInvokeStmt = Jimple.v().newStaticInvokeExpr(Scene.v().getMethod("<android.util.Log: int d(java.lang.String,java.lang.String)>").makeRef(), listArgs);
        InvokeStmt InvokeStatementLog = Jimple.v().newInvokeStmt(LogInvokeStmt);
        String stringInvokeStatementLog = InvokeStatementLog.toString();
        // Print("Message:"+Message);
        // if(!stringInvokeStatementLog.contains("findView")){
        Print(stringInvokeStatementLog);
        // }
        String stringLastAdUnitInserted = InvokeStatementLog.toString();
        // Print("LastKnownUnit TEST:"+LastKnownUnit.toString()+"\nNew Unit:"+InvokeStatementLog.toString());
        int intStringAdUnitsInsertedSize= stringAdUnitsInserted.size()-1;

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
        }

        // units.insertAfter(InvokeStatementLog, LastKnownUnit);
    }

    public static void IterateOverListAndInsertLogMessage(String InputMsg, String[] ArrayOfStatements, Unit LastKnownUnit, UnitPatchingChain units, String MethodName, boolean AdSpecific)
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
    public static void Wait(int ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }
    public static void InvestigateClassAndInjectAdListenerCalls(Body body)
    {
        if(StringClassToInvestigate != null && StringMethodToInvestigate != null)
        {
            // Print("Data:" + StringClassToInvestigate + " : " + StringMethodToInvestigate);
            Wait(10);
            SootClass classToInvestigate = Scene.v().getSootClass(StringClassToInvestigate);
            SootMethod methodToInvestigate = classToInvestigate.getMethod(StringMethodToInvestigate);
            Body bodyToInvestigate = methodToInvestigate.getActiveBody();
            UnitPatchingChain units = body.getUnits();
            SootConditionChecker sootconditionchecker = new SootConditionChecker();
            for (Iterator<Unit> unit = units.snapshotIterator(); unit.hasNext();)
            {
                Unit LastKnownUnit = unit.next();
                String StringLastKnownUnit = LastKnownUnit.toString();
                boolean LastKnownUnitIsAStmt = sootconditionchecker.LastKnownUnitIsAStatement(LastKnownUnit);
                if (LastKnownUnitIsAStmt)
                {
                    for (ValueBox SootValuebox : LastKnownUnit.getUseBoxes())
                    {
                        Value SootValue = SootValuebox.getValue();
                        VirtualInvokeExpr VirtualInvokeExpression = null;
                        SpecialInvokeExpr SpecialInvokeExpression = null;
                        StaticInvokeExpr StaticInvokeExpression = null;
                        String MethodName = null;
                        boolean SootValueIsAVirtualInvokeExpr = sootconditionchecker.ValueIsAVirtualInvokeExpr(SootValue);
                        boolean SootValueIsASpecialInvokeExpr = sootconditionchecker.ValueIsASpecialInvokeExpr(SootValue);
                        boolean SootValueIsAStaticInvokeExpr = sootconditionchecker.ValueIsAStaticInvokeExpr(SootValue);
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

    public static void IterateOverUnitsandInjectAdSpecificCalls(Body body, String App_Name, String Hash)
    {
        SootConditionChecker sootconditionchecker = new SootConditionChecker();
        UnitPatchingChain units = body.getUnits();
        String MethodNameOfInterst = null;
        if(runOnce)
        {
            for (Local local : body.getLocals())
            {
                // if (local.getName().equals(name)) {
                if(local.getType().toString().contains("com.google.android.gms.ads.admanager.AdManagerAdView"))
                {
                    // Print("Locals:"+local.getType().toString());
                    // Print("IterateOverUnitsandInjectAdSpecificCalls TESTING:"+body.getClass().toString());
                    hasAdListener = true;
                    runOnce = false;
                }
            }
        }

        // List<Local> localList = new ArrayList<Local>();
        for (Iterator<Unit> unit = units.snapshotIterator(); unit.hasNext();)
        {
            Unit LastKnownUnit = unit.next();
            String StringLastKnownUnit = LastKnownUnit.toString();
            boolean LastKnownUnitIsAStmt = sootconditionchecker.LastKnownUnitIsAStatement(LastKnownUnit);
            if(StringLastKnownUnit.contains("com.google.android.gms.example.bannerexample.MyActivity this$0"))
            {
                Print("FOUND:" + StringLastKnownUnit);
            }
            if (LastKnownUnitIsAStmt)
            {
                for (ValueBox SootValuebox : LastKnownUnit.getUseBoxes())
                {
                    Value SootValue = SootValuebox.getValue();
                    VirtualInvokeExpr VirtualInvokeExpression = null;
                    SpecialInvokeExpr SpecialInvokeExpression = null;
                    StaticInvokeExpr StaticInvokeExpression = null;
                    String MethodName = null;
                    boolean SootValueIsAVirtualInvokeExpr = sootconditionchecker.ValueIsAVirtualInvokeExpr(SootValue);
                    boolean SootValueIsASpecialInvokeExpr = sootconditionchecker.ValueIsASpecialInvokeExpr(SootValue);
                    boolean SootValueIsAStaticInvokeExpr = sootconditionchecker.ValueIsAStaticInvokeExpr(SootValue);

                    // if(StringLastKnownUnit.contains("r0.<com.google.android.gms.example.bannerexample.MyActivity$2: com.google.android.gms.example.bannerexample.MyActivity this$0>"))
                    // {
                    //     // Print("DEFBOX UNIT:" + LastKnownUnit.getDefBoxes().get(0).getValue().getType().toString());
                    //     Print("DEFBOX UNIT:" + LastKnownUnit.getDefBoxes().toString());
                    //     // Print("UseBox UNIT:" + LastKnownUnit.getDefBoxes().toString());
                    // }
                    if(SootValueIsAVirtualInvokeExpr)
                    {
                        VirtualInvokeExpression = (VirtualInvokeExpr) SootValue;
                        MethodName = VirtualInvokeExpression.getMethod().getName().toString();

                        IterateOverListAndInsertLogMessage(""+App_Name+"::"+Hash+"::", StringArrayOfVirtualInvokeMethodsToLookForNotAdSpecific, LastKnownUnit, units, MethodName+":", false);
                        IterateOverListAndInsertLogMessage(App_Name+"::"+Hash+"::", StringArrayOfVirtualInvokeMethodsToLookForAdSpecificMultipleTimesSearchFor, LastKnownUnit, units, MethodName+":", false);
                        IterateOverListAndInsertLogMessage(App_Name+"::"+Hash+"::", StringArrayOfVirtualInvokeMethodsToLookForAdSpecific, LastKnownUnit, units, MethodName+":", true);
                        //IterateOverListAndInsertLogMessage(StringArrayOfVirtualInvokeMethodsToLookForAdSpecificMultipleTimesSearchFor, LastKnownUnit, units, MethodName, true);

                        // Print("SootValueIsAVirtualInvokeExpr:" + VirtualInvokeExpression.toString());
                        if(StringLastKnownUnit.contains("void loadAd(com.google.android.gms.ads.admanager.AdManagerAdRequest)") || StringLastKnownUnit.contains("void loadAd(com.google.android.gms.ads.AdRequest)"))
                        // if(StringLastKnownUnit.contains("void loadAd(com.google.android.gms.ads.admanager.AdManagerAdRequest)"))
                        {
                            // Print("FOUND LOADAD:" + MethodName);
                            StringMethodToInvestigate = body.getMethod().getSignature().toString();
                            StringClassToInvestigate = body.getMethod().getDeclaringClass().toString();
                        }
                    }
                    //if(SootValueIsAStaticInvokeExpr & StringLastKnownUnit.contains("google")){
                    if(SootValueIsAStaticInvokeExpr)
                    {
                        StaticInvokeExpression = (StaticInvokeExpr) SootValue;
                        MethodName = StaticInvokeExpression.getMethod().getName().toString();
                        IterateOverListAndInsertLogMessage(App_Name+"::"+Hash+"::"+"", StringArrayOfStaticInvokeMethodsToLookForAdSpecific, LastKnownUnit, units, MethodName, true);
                    }
                    if(SootValueIsASpecialInvokeExpr)
                    {
                        SpecialInvokeExpression = (SpecialInvokeExpr) SootValue;
                        MethodName = SpecialInvokeExpression.getMethod().getName().toString();
                        IterateOverListAndInsertLogMessage(App_Name+"::"+Hash+"::", StringArrayOfSpecialInvokeMethodsToLookForAdSpecific, LastKnownUnit, units, MethodName+":", true);
                        IterateOverListAndInsertLogMessage(App_Name+"::"+Hash+"::", StringArrayOfSpecialInvokeMethodsToLookForAdSpecificMultipleTimesSearchFor, LastKnownUnit, units, MethodName, false);
                        IterateOverListAndInsertLogMessage(App_Name+"::"+Hash+"::"+"", StringArrayOfSpecialInvokeMethodsToLookForNotAdSpecific, LastKnownUnit, units, MethodName+":", false);
                    }
                }
            }
        }
    }
}