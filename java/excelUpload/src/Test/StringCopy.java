/**
 * 
 */
package Test;

/**
 * @author 王森洪
 *
 * @date 2012-4-23
 */
public class StringCopy {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String aString="Adfa";
		aString=aString.toLowerCase();
		System.out.println(aString);
		String bString=new String(aString);
		bString="asd";
		System.out.println(aString);
		System.out.println(bString);
	}

}
