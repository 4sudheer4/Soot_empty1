import soot.*;
import soot.jimple.*;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.InverseGraph;
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
import soot.toolkits.scalar.FlowAnalysis;
import soot.toolkits.scalar.ForwardFlowAnalysis;
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

public class MyBFTransformer extends SceneTransformer {

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
        String sep = File.separator;
        SootClass mainClass = Scene.v().getMainClass(); // get main class
        SootConditionChecker sootconditionchecker = new SootConditionChecker();
        System.out.println("Methods: " + mainClass.getMethodCount()); // get methods count for class
        SootMethod m = mainClass.getMethodByName("test"); // get method test from main class

        Body b = m.retrieveActiveBody();// get method body as an object

        System.out.println("=======================================");
        System.out.println(m.toString());

        UnitGraph graph = new ExceptionalUnitGraph(b);
//        InverseGraph graph = new InverseGraph(graph);
        MyBackwardFlowAnalysis flowAnalysis = new MyBackwardFlowAnalysis(graph);

//        // get the units in the method
//        List<Unit> units = graph.getHeads();

        // print IN and OUT sets for each statement
        for (Unit u : graph) {
            System.out.println("Statement: " + u.toString());
            System.out.println("IN Set: " + flowAnalysis.getFlowBefore(u).toString());
            System.out.println("OUT Set: " + flowAnalysis.getFlowAfter(u).toString());
            System.out.println();
        }

        System.out.println("=======================================");
    }

}
