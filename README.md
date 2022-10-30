# Soot_empty1


•	In case you don’t know yet, Jimple is Soot’s primary intermediate representation, a three-address code that is basically a sort of simplified version of Java that only requires around 15 different kinds of statements. You can instruct Soot to convert .java or .class files to .jimple files or the other hand around. You can even have Soot generate .jimple from .java, modify the .jimple with a normal text editor and then convert your .jimple to .class, virtually hand-optimizing your program. But we are getting off-track here...



/Library/Java/JavaVirtualMachines/temurin-8.jdk/Contents/Home/jre/lib/rt.jar


java -cp sootclasses-trunk-jar-with-dependencies.jar soot.Main --help

run javac Main.java

and the resultant .class file will be passed on to the below command.
 

java -cp soot-4.3.0-jar-with-dependencies.jar  soot.Main -cp . -pp Main

java -cp soot-4.3.0-jar-with-dependencies.jar  soot.Main -cp . -pp -f jimple Main

•	This seems to have worked. Soot successfully processed the two .java files and placed resulting .class files into the sootOutput folder. Note that in general, Soot will process all classes you name on the command line and all classes referenced by those classes.
















Soot tutorial: https://www.sable.mcgill.ca/soot/tutorial/pldi03/tutorial.pdf




Bytecode to Jimple is done in ‘jb’ phase. 
o	This is a naïve translation, good starting point for analasys.
Bytocode to jimple using ‘bb’ or ‘gb’ phase
o	More spurious stores and loads.
o	Aggregates expressions and generate stack code or
o	Perform store load on naïve stack code.































 

 

 

Where ut.getUseBoxes() returns list of value boxes corresponding to all values which get used in unit.

 





Order:
Unit is nothing but a statement. Ex: X = Y op Z;

We can get Units from the source code using Body.getUnits()
Ut = Body.getUnits();

Once we get Units, to get values from each unit we use Ut.getDefBoxes() to get definition values and Ut.getUseBoxes() to get ‘Y’, ‘Z’, ‘Y op Z’
valuesBox = Ut.getUseBoxes(); //  ’X’
defBox = Ut.getDefBoxes(); //      ‘get ‘Y’, ‘Z’, ‘Y op Z’


To get and set values to the variables received from Boxes we can use getValues() and setValues()

valuesBox.getValues();	valuesBox.setValues();
defBox.getValues();		defBox.setValues();


 


Along with getValues, setValues we also have the following:

 

Commands in Soot_empty:

java -cp soot-4.3.0-jar-with-dependencies.jar  soot.Main -cp . -pp Test


java -cp soot-4.3.0-jar-with-dependencies.jar  soot.Main -cp . -pp -f jimple Test 
![image](https://user-images.githubusercontent.com/34088396/198894783-d7f9b004-1407-4c25-8f74-520b2f2e04ae.png)
