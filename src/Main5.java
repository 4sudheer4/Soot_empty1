//import soot.*;
//import soot.jimple.*;
//import soot.options.Options;
//import soot.util.*;
//import java.io.*;
//import java.util.*;
//public class Main5 {
//    public static void main(String[] args) {
//        String classPath = "inputs";
//        configureSoot(classPath);// configure soot
//        Scene.v().loadNecessaryClasses(); // load all the library and dependencies for given program
//        VM5Transformer mVMTransformer = new VM5Transformer();
//
//
//        Transform mVMTransform = new Transform("wjtp.malware", mVMTransformer);
//        PackManager.v().getPack("wjtp").add(mVMTransform);
//
//
//        PackManager.v().runPacks();  // process and inject
//        PackManager.v().writeOutput();
//    }
//
//    public static void configureSoot(String classpath) {
//        Options.v().set_whole_program(true);  // process whole program
//        Options.v().set_allow_phantom_refs(true); // load phantom references
//        Options.v().set_prepend_classpath(true); // prepend class path
//        Options.v().set_src_prec(Options.src_prec_class); // process only .class files, change here to process other IR or class
//        Options.v().set_output_format(Options.output_format_jimple); // output jimple format, change here to output other IR
//        ArrayList<String> list = new ArrayList<>();
//        list.add(classpath);
//        Options.v().set_process_dir(list); // process all .class files in directory
//        //Options.v().setPhaseOption("cg.spark", "on"); // use spark for call graph
//    }
//}
import java.util.*;

import soot.*;
import soot.jimple.*;
import soot.options.Options;
import soot.toolkits.graph.*;
import soot.toolkits.scalar.*;
import soot.util.*;
import soot.SootClass;

public class Main5 extends BackwardFlowAnalysis<Unit, Set<String>> {

    public Main5(UnitGraph graph) {
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
        String classPath = "inputs";
        configureSoot(classPath);// configure soot
        Scene.v().loadNecessaryClasses(); // load all the library and dependencies for given program

        // Perform the analysis on the main method of the input class
        SootMethod method = SootClass.getMethodByName("main");
        Body body = method.retrieveActiveBody();
        UnitGraph graph = new ExceptionalUnitGraph(body);
        Main analysis = new Main(graph);

        // Print the output sets for each unit in the method
        Iterator<Unit> unitIt = graph.iterator();
        while (unitIt.hasNext()) {
            Unit unit = unitIt.next();
            System.out.println("Unit " + unit.toString() + " : " + analysis.getFlowBefore(unit));
        }
    }

        public static void configureSoot(String classpath) {
        Options.v().set_whole_program(true);  // process whole program
        Options.v().set_allow_phantom_refs(true); // load phantom references
        Options.v().set_prepend_classpath(true); // prepend class path
        Options.v().set_src_prec(Options.src_prec_class); // process only .class files, change here to process other IR or class
        Options.v().set_output_format(Options.output_format_jimple); // output jimple format, change here to output other IR
        ArrayList<String> list = new ArrayList<>();
        list.add(classpath);
        Options.v().set_process_dir(list); // process all .class files in directory
        //Options.v().setPhaseOption("cg.spark", "on"); // use spark for call graph
    }
}
