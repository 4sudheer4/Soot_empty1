public class Test {
    public static void main(String[] args) {
        int k = test(1, 2, 3);
    }
    private static int test(int a, int b, int c) {
        b = b + 1; //3
        c = c + b; //6
        a = b * c; //18
        print(a,b);
        return a; //18
    }
    private static void print(int a, int b){
        System.out.println(a);
    }

}