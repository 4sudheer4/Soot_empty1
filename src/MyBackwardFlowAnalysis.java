import soot.Local;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.*;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.BackwardFlowAnalysis;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardFlowAnalysis;
import soot.toolkits.scalar.ArraySparseSet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyBackwardFlowAnalysis extends BackwardFlowAnalysis<Unit, FlowSet<Value>> {

//    private final FlowSet<Unit>[] inSets;
//    private final FlowSet<Unit>[] outSets;
    public UnitGraph g;
    private void updateGlobalGraph(UnitGraph g){
        this.g = g;
    }
    @SuppressWarnings("unchecked")
    public MyBackwardFlowAnalysis(UnitGraph g) {
        super(g);
        updateGlobalGraph(g);
        doAnalysis();
    }
    @Override
    protected FlowSet<Value> newInitialFlow() {
        return new ArraySparseSet<>();
    }

    @Override
    protected FlowSet<Value> entryInitialFlow() {
        FlowSet<Value> initialSet = new ArraySparseSet<>();
        //add the point which we are interested in to the initial set
        //return the initialset
        boolean start = false;
        List<Value> callArgs = new ArrayList<Value>();
        for (Unit unit : g) {

            if (unit instanceof InvokeStmt) {
                System.out.println("displaying units");
                System.out.println(unit);
                InvokeStmt stmt = (InvokeStmt) unit;
                InvokeExpr ie = stmt.getInvokeExpr();
                callArgs = ie.getArgs();

            }
        }
        System.out.println("Argument: " + callArgs);
        for (Value arg : callArgs) {
            System.out.println("Argument: " + arg);
            initialSet.add(arg);
        }
        return initialSet;
    }
    @Override
    protected void merge(FlowSet<Value> inSet1, FlowSet<Value> inSet2, FlowSet<Value> outSet) {
        inSet1.union(inSet2, outSet);
    }
    @Override
    protected void copy(FlowSet<Value> source, FlowSet<Value> dest) {
        System.out.println("copy");
        source.copy(dest);
    }
    @Override
    protected void flowThrough(FlowSet<Value> inSet, Unit unit, FlowSet<Value> outSet) {

        FlowSet inSets = (FlowSet)inSet, outSets = (FlowSet)outSet;
        // Copy the inSet to the outSet
        inSet.copy(outSet);
        // Kill the definitions of the variables in the current unit
//        for (ValueBox def : unit.getDefBoxes()) {
//            if (def.getValue() instanceof Local) {
//                outSet.remove(def.getValue());
//            }
//        }
        for (ValueBox vb : unit.getUseBoxes()) {
            Value v = vb.getValue();
            if (unit instanceof AssignStmt) {
                if (v instanceof BinopExpr) {
//                    System.out.println("ValueBox Values is binary: " + vb);
//                    Value leftOp = ((AssignStmt) unit).getLeftOp();
//                    System.out.println("Values for left");
//                    System.out.println(leftOp);
//                    outSet.remove(leftOp);
                } else {
                    System.out.println("ValueBox Values is NOT binary: " + vb); //printing how value boxes looks like
                    if (v instanceof Local) {
                        System.out.println("Value for right: " + v);
                        outSet.add(v);
                    }

                }
            } else if (unit instanceof InvokeStmt) {

                // TODO: Handle method calls
            } else if (unit instanceof ReturnStmt || unit instanceof ReturnVoidStmt) {

                // TODO: Handle return statements
            } else if (unit instanceof IfStmt) {

                // TODO: Handle if statements
            }
        }
        // Store the IN and OUT flowsets for the current unit
        //int unitIndex = unitToIndex(unit);

//        inSets.copy(inSet);
        outSets.copy(outSet);

    }

    private int unitToIndex(Unit u) {
        return ((UnitGraph) graph).getTails().indexOf(u);
    }

    /*
    public FlowSet<Value> getInSet(Unit unit) {
        return inSets[unitToIndex(unit)];
    }

     */
}

