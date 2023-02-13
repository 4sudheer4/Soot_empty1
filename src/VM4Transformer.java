import soot.*;
import soot.jimple.*;
import soot.toolkits.graph.BriefUnitGraph;
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

public class VM4Transformer extends SceneTransformer {

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
        SootMethod m = mainClass.getMethodByName("main"); // get method test from main class

        Body methodBody = m.retrieveActiveBody();// get method body as an object

        JimpleBody body = (JimpleBody) m.retrieveActiveBody();// get jimple method body as an object


        Body b = methodBody;

        System.out.println("=======================================");
        System.out.println(m.getName());

        UnitGraph graph = new ExceptionalUnitGraph(b);
        SimpleLiveLocals sll = new SimpleLiveLocals(graph);

        for (Unit u : graph) {
            List<Local> before = sll.getLiveLocalsBefore(u);
            List<Local> after = sll.getLiveLocalsAfter(u);
            UnitPrinter up = new NormalUnitPrinter(b);
            up.setIndent("");

            System.out.println("---------------------------------------");
            u.toString(up);
            System.out.println(up.output());
            System.out.print("Live in: {");
            sep = "";
            for (Local l : before) {
                System.out.print(sep);
                System.out.print(l.getName() + ": " + l.getType());
                sep = ", ";
            }
            System.out.println("}");
            System.out.print("Live out: {");
            sep = "";
            for (Local l : after) {
                System.out.print(sep);
                System.out.print(l.getName() + ": " + l.getType());
                sep = ", ";
            }
            System.out.println("}");
            System.out.println("---------------------------------------");
        }
        System.out.println("=======================================");


    }
}