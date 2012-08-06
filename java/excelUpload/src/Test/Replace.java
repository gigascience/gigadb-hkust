/**
 * 
 */
package Test;

/**
 * @author 王森洪
 *
 * @date 2012-4-26
 */
public class Replace {
	public static void main(String[] args) throws Exception {  
	String a="The pig (Sus scrofa) is an economically important food source and also serves as an important model organism.  Here we ";
	a=a.replaceAll("\\u00a0", "*");
	System.out.println(a);
	for(int i=0;i<a.length();i++){
		System.out.println(a.charAt(i)+" : "+(int)(a.charAt(i)));
	}
	}
}
