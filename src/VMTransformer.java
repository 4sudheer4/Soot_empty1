import soot.*;
import soot.jimple.*;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class VMTransformer extends SceneTransformer {

    @Override
    protected void internalTransform(String arg0, Map arg1) {
        SootClass mainClass = Scene.v().getMainClass(); // get main class

        System.out.println("Methods: " + mainClass.getMethodCount()); // get methods count for class
        SootMethod testMethod = mainClass.getMethodByName("main"); // get method test from main class
        Body methodBody = testMethod.retrieveActiveBody();// get method body as an object
        // Create directed-graph based on the method body
        //Unit u = methodBody.getUnits().getFirst();

        for (Unit ut : methodBody.getUnits()) {
            //List<ValueBox> defBoxes = ut.getDefBoxes();
            System.out.println("units: " +ut);
            for (ValueBox vb : ut.getUseBoxes()) {
                Value v = vb.getValue();
                System.out.println("Box Values: " + v);
                if (v.toString().contains("staticinvoke"))
                {
                    System.out.println("yes, there is a static invoke");
                }
                if (v instanceof Local) {
                    Local l = (Local) v; //stores ix versions of integers
                    int lno = l.getNumber();

                    System.out.println("Box Values: " + v);
                }
            }

 /*           int size = defBoxes.size();
            if (size == 0) {
                System.out.println("Null");
            }
            if (size != 1) {
                throw new RuntimeException();
            }
            ValueBox vb = defBoxes.get(0);
            Value v = vb.getValue();
            if (!(v instanceof Local)) {
                System.out.println("Null");
            }
            System.out.println((Local)v);
            */
        }

}
}