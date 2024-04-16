package paq;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class prueba2 {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
String entrada = br.readLine();
String [] arr = entrada.split(",");
String salida = "";

int [] newArr = new int[arr.length];
for (int i = 0; i < newArr.length; i++) {
	newArr[i] = Integer.parseInt(arr[i]);
	
}


int temp;

for (int i = 0; i < newArr.length; i++) {
	for (int j = 0; j < newArr.length-1; j++) {
		System.out.println("n: " + newArr[j]);
		
			System.out.println("n+1: " + newArr[j+1]);
		
		if (newArr[j+1] > newArr[j]) {
			temp = newArr[j+1];
			System.out.println("temp: " + temp);
			newArr[j+1] = newArr[j];
			newArr[j] = temp;
			System.out.println("n: " + newArr[j] + " n1:" + newArr[j+1] +"\n");
		}
	}
}

for (int i = 0; i < newArr.length; i++) {
	salida += newArr[i]+",";
	
}

salida = salida.substring(0, salida.length()-1);

System.out.println( "\n\n" + salida);

	}

}
