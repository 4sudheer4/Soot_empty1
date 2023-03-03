
import soot.PackManager;
import soot.Scene;
import soot.Transform;
import soot.options.Options;

import java.util.ArrayList;

public class MyFFMain {

    public static void main(String[] args) {

        //a .class file provided in the 'inputs' folder will be sent to this transformer

        String classPath = "inputs";
        configureSoot(classPath);// configure soot
        Scene.v().loadNecessaryClasses(); // load all the library and dependencies for given program
        MyFFTransformer mMyFFTransformer = new MyFFTransformer();


        Transform mMyFFTransform = new Transform("wjtp.Malware", mMyFFTransformer);
        PackManager.v().getPack("wjtp").add(mMyFFTransform);


        PackManager.v().runPacks();  // process and build call graph
        PackManager.v().writeOutput();
    }

    public static void configureSoot(String classpath) {
        Options.v().set_whole_program(true);  // process whole program
        Options.v().set_allow_phantom_refs(true); // load phantom references
        Options.v().set_prepend_classpath(true); // prepend class path
        Options.v().set_src_prec(Options.src_prec_class); // process only .class files, change here to process other IR or class
        //Options.v().set_output_format(Options.output_format_jimple); // output jimple format, change here to output other IR
        Options.v().set_output_format(1);
        ArrayList<String> list = new ArrayList<>();
        list.add(classpath);
        Options.v().set_process_dir(list); // process all .class files in directory
        //Options.v().setPhaseOption("cg.spark", "on"); // use spark for call graph
    }
}
