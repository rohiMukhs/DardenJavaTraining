package lambdaExpAsmt;

public class LambdaFirstQ {

	public static void main(String[] args) {
		
		Arith add = (int n1,int n2)-> (n1+n2);
		Arith sub = (int n1,int n2)-> (n1-n2);
		Arith mul = (int n1,int n2)-> (n1*n2);
		Arith div = (int n1,int n2)-> (n1/n2);
		Arith mod = (int n1,int n2)-> (n1%n2);
		
		System.out.println("Adding 30 and 20 = " + add.operate(30, 20));
		System.out.println("Subtracting 20 from 30 = " + sub.operate(30, 20));
		System.out.println("Multiplying 30 and 20 = " + mul.operate(30, 20));
		System.out.println("Dividing 30 by 20 = " + div.operate(30, 20));
		System.out.println("Modular division of 30 by 20 = " + mod.operate(30, 20));

	}

}

@FunctionalInterface
interface Arith{
	int operate(int n1, int n2);
}
