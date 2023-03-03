import soot.*;
import soot.jimple.*;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.ClassicCompleteUnitGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;
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
import soot.toolkits.scalar.BackwardFlowAnalysis;
import soot.toolkits.scalar.SimpleLiveLocals;
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

public class BackwardsFlowAnalysis2 extends BackwardFlowAnalysis<Unit, Set<String>> {

    public BackwardsFlowAnalysis2(UnitGraph graph) {
        super(graph);
        doAnalysis();
    }

    @Override
    protected void flowThrough(Set<String> in, Unit unit, Set<String> out) {
        // Perform the flow analysis for a single unit
        // If the unit is an assignment statement, add the assigned variable to the output set
        // If the unit is a call statement, add all the arguments to the output set
        if (unit instanceof DefinitionStmt) {
            Value leftOp = ((DefinitionStmt) unit).getLeftOp();
            if (leftOp instanceof Local) {
                String varName = ((Local) leftOp).getName();
                out.add(varName);
            }
        } else if (unit instanceof InvokeStmt) {
            InvokeExpr invokeExpr = ((InvokeStmt) unit).getInvokeExpr();
            for (Value arg : invokeExpr.getArgs()) {
                if (arg instanceof Local) {
                    String varName = ((Local) arg).getName();
                    out.add(varName);
                }
            }
        }
        // Update the output set with the values from the input set
        out.addAll(in);
    }

    @Override
    protected void copy(Set<String> source, Set<String> dest) {
        // Copy the contents of the source set to the destination set
        dest.clear();
        dest.addAll(source);
    }

    @Override
    protected void merge(Set<String> in1, Set<String> in2, Set<String> out) {
        // Merge the contents of the two input sets into the output set
        out.clear();
        out.addAll(in1);
        out.addAll(in2);
    }

    @Override
    protected Set<String> newInitialFlow() {
        // Return an empty set as the initial input set
        return new HashSet<String>();
    }

    @Override
    protected Set<String> entryInitialFlow() {
        // Return an empty set as the initial output set for the entry point
        return new HashSet<String>();
    }

    public static void main(String[] args) {
        // Load the input class using Soot
        String className = "Test";
        SootClass sootClass = Scene.v().loadClassAndSupport(className);
        sootClass.setApplicationClass();

        // Perform the analysis on the main method of the input class
        SootMethod method = sootClass.getMethodByName("main");
        Body body = method.retrieveActiveBody();
        UnitGraph graph = new ExceptionalUnitGraph(body);
        BackwardsFlowAnalysis analysis = new BackwardsFlowAnalysis(graph);
        analysis.doAnalysis();
        // Print the output sets for each unit in the method
        Iterator<Unit> unitIt = graph.iterator();
        while (unitIt.hasNext()) {
            Unit unit = unitIt.next();
            System.out.println("Unit " + unit.toString() + " : " + analysis.getFlowBefore(unit));
        }
    }
}
public class VM4Transformer extends BodyTransformer {

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

    private static String[] StringArrayOfVirtualInvokeMethodsToLookForAdSpecific = {"java.net.URL: java.net.URLConnection openConnection()", "java.net.HttpURLConnection: void setRequestMethod(java.lang.String)", "com.google.android.gms.ads.interstitial.InterstitialAd: void show(android.app.Activity)", "com.google.android.gms.ads.AdView: void loadAd(com.google.android.gms.ads.AdRequest)", "com.google.android.gms.ads.AdView: void setAdUnitId(java.lang.String)"};


    @Override
    protected void internalTransform(String arg0, Map arg1) {
        SootClass mainClass = Scene.v().getMainClass(); // get main class
        SootConditionChecker sootconditionchecker = new SootConditionChecker();
        System.out.println("Methods: " + mainClass.getMethodCount()); // get methods count for class
        SootMethod testMethod = mainClass.getMethodByName("main"); // get method test from main class
        Body methodBody = testMethod.retrieveActiveBody();// get method body as an object

        SootMethod src = Scene.v().getMainClass().getMethodByName("main");
        UnitGraph unitGraph = new ExceptionalUnitGraph(methodBody);
        BackwardsFlowAnalysis analysis = new BackwardsFlowAnalysis(unitGraph);
        Iterator<Unit> unitIt = unitGraph.iterator();
        while (unitIt.hasNext()) {
            Unit u = unitIt.next();
            System.out.println("Statement: " + u);
            System.out.println("In set: " + analysis.getFlowBefore(u));
            System.out.println("Out set: " + analysis.getFlowAfter(u));
        }
    }
}